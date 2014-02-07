/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.cassandra.thrift;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import org.apache.cassandra.utils.FBUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.concurrent.JMXEnabledThreadPoolExecutor;
import org.apache.cassandra.concurrent.NamedThreadFactory;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * This is a interim solution till THRIFT-1167 gets committed...
 *
 * The idea here is to avoid sticking to one CPU for IO's. For better throughput
 * it is spread across multiple threads. Number of selector thread can be the
 * number of CPU available.
 */
public class CustomTHsHaServer extends TNonblockingServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomTHsHaServer.class.getName());
    private final Set<SelectorThread> ioThreads = new HashSet<SelectorThread>();
    private volatile boolean stopped = true;
    private final ExecutorService invoker;

    /**
     * All the arguments to Non Blocking Server will apply here. In addition,
     * executor pool will be responsible for creating the internal threads which
     * will process the data. threads for selection usually are equal to the
     * number of cpu's
     */
    public CustomTHsHaServer(Args args, ExecutorService invoker, int threadCount)
    {
        super(args);
        this.invoker = invoker;
        // Create all the Network IO Threads.
        for (int i = 0; i < threadCount; ++i)
            ioThreads.add(new SelectorThread("Selector-Thread-" + i));
    }

    /** @inheritDoc */
    @Override
    public void serve()
    {
        if (!startListening())
            return;
        if (!startThreads())
            return;
        setServing(true);
        joinSelector();
        invoker.shutdown();
        setServing(false);
        stopListening();
    }

    /**
     * Save the remote socket as a thead local for future use of client state.
     */
    protected class Invocation implements Runnable
    {
        private final FrameBuffer frameBuffer;
        private final SelectorThread thread;

        public Invocation(final FrameBuffer frameBuffer, SelectorThread thread)
        {
            this.frameBuffer = frameBuffer;
            this.thread = thread;
        }

        public void run()
        {
            TNonblockingSocket socket = (TNonblockingSocket) frameBuffer.trans_;
            ThriftSessionManager.instance.setCurrentSocket(socket.getSocketChannel().socket().getRemoteSocketAddress());
            frameBuffer.invoke();
            // this is how we let the same selector thread change the selection type.
            thread.requestSelectInterestChange(frameBuffer);
        }
    }

    protected boolean startThreads()
    {
        stopped = false;
        // start all the threads.
        for (SelectorThread thread : ioThreads)
            thread.start();
        return true;
    }

    @Override
    protected void joinSelector()
    {
        try
        {
            // wait till all done with stuff's
            for (SelectorThread thread : ioThreads)
                thread.join();
        }
        catch (InterruptedException e)
        {
            LOGGER.error("Interrupted while joining threads!", e);
        }
    }

    /**
     * Stop serving and shut everything down.
     */
    @Override
    public void stop()
    {
        stopListening();
        stopped = true;
        for (SelectorThread thread : ioThreads)
            thread.wakeupSelector();
        joinSelector();
    }

    /**
     * IO Threads will perform expensive IO operations...
     */
    protected class SelectorThread extends Thread
    {
        private final Selector selector;
        private final TNonblockingServerTransport serverTransport;
        private final Set<FrameBuffer> selectInterestChanges = new HashSet<FrameBuffer>();

        public SelectorThread(String name)
        {
            super(name);
            try
            {
                this.selector = SelectorProvider.provider().openSelector();
                this.serverTransport = (TNonblockingServerTransport) serverTransport_;
                this.serverTransport.registerSelector(selector);
            }
            catch (IOException ex)
            {
                throw new RuntimeException("Couldnt open the NIO selector", ex);
            }
        }

        public void run()
        {
            try
            {
                while (!stopped)
                {
                    select();
                }
            }
            catch (Throwable t)
            {
                LOGGER.error("Uncaught Exception: ", t);
            }
            finally
            {
                try
                {
                    selector.close(); // CASSANDRA-3867
                }
                catch (IOException e)
                {
                    // ignore this exception.
                }
            }
        }

        private void select() throws InterruptedException, IOException
        {
            // wait for new keys
            selector.select();
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext())
            {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();

                try
                {
                    if (!key.isValid())
                    {
                        // if invalid cleanup.
                        cleanupSelectionkey(key);
                        continue;
                    }

                    if (key.isAcceptable())
                        handleAccept();
                    if (key.isReadable())
                        handleRead(key);
                    else if (key.isWritable())
                        handleWrite(key);
                    else
                        LOGGER.debug("Unexpected state " + key.interestOps());
                }
                catch (Exception io)
                {
                    // just ignore (?)
                    cleanupSelectionkey(key);
                }
            }
            // process the changes which are inserted after completion.
            processInterestChanges();
        }

        private void handleAccept()
        {
            SelectionKey clientKey = null;
            TNonblockingTransport client = null;
            try
            {
                // accept the connection
                client = (TNonblockingTransport) serverTransport.accept();
                clientKey = client.registerSelector(selector, SelectionKey.OP_READ);
                // add this key to the map
                FrameBuffer frameBuffer = new FrameBuffer(client, clientKey);
                clientKey.attach(frameBuffer);
            } catch (TTransportException ex)
            {
                // ignore this might have been handled by the other threads.
                // serverTransport.accept() as it returns null as nothing to accept.
                return;
            }
            catch (IOException tte)
            {
                // something went wrong accepting.
                LOGGER.warn("Exception trying to accept!", tte);
                tte.printStackTrace();
                if (clientKey != null)
                    cleanupSelectionkey(clientKey);
                if (client != null)
                    client.close();
            }
        }

        private void handleRead(SelectionKey key)
        {
            FrameBuffer buffer = (FrameBuffer) key.attachment();
            if (!buffer.read())
            {
                cleanupSelectionkey(key);
                return;
            }

            if (buffer.isFrameFullyRead())
            {
                if (!requestInvoke(buffer, this))
                    cleanupSelectionkey(key);
            }
        }

        private void handleWrite(SelectionKey key)
        {
            FrameBuffer buffer = (FrameBuffer) key.attachment();
            if (!buffer.write())
                cleanupSelectionkey(key);
        }

        public void requestSelectInterestChange(FrameBuffer frameBuffer)
        {
            synchronized (selectInterestChanges)
            {
                selectInterestChanges.add(frameBuffer);
            }
            // Wake-up the selector, if it's currently blocked.
            selector.wakeup();
        }

        private void processInterestChanges()
        {
            synchronized (selectInterestChanges)
            {
                for (FrameBuffer fb : selectInterestChanges)
                    fb.changeSelectInterests();
                selectInterestChanges.clear();
            }
        }

        private void cleanupSelectionkey(SelectionKey key)
        {
            FrameBuffer buffer = (FrameBuffer) key.attachment();
            if (buffer != null)
            {
                buffer.close();
                TNonblockingSocket socket = (TNonblockingSocket) buffer.trans_;
                if (socket != null)
                    ThriftSessionManager.instance.connectionComplete(socket.getSocketChannel().socket().getRemoteSocketAddress());
            }
            key.cancel();
        }

        public void wakeupSelector()
        {
            selector.wakeup();
        }
    }

    protected boolean requestInvoke(FrameBuffer frameBuffer, SelectorThread thread)
    {
        try
        {
            Runnable invocation = new Invocation(frameBuffer, thread);
            invoker.execute(invocation);
            return true;
        }
        catch (RejectedExecutionException rx)
        {
            LOGGER.warn("ExecutorService rejected execution!", rx);
            return false;
        }
    }

    @Override
    protected void requestSelectInterestChange(FrameBuffer fb)
    {
        // Dont change the interest here, this has to be done by the selector
        // thread because the method is not synchronized with the rest of the
        // selectors threads.
    }

    public static class Factory implements TServerFactory
    {
        public TServer buildTServer(Args args)
        {
            if (DatabaseDescriptor.getClientEncryptionOptions().enabled)
                throw new RuntimeException("Client SSL is not supported for non-blocking sockets (hsha). Please remove client ssl from the configuration.");

            final InetSocketAddress addr = args.addr;
            TNonblockingServerTransport serverTransport;
            try
            {
                serverTransport = new TCustomNonblockingServerSocket(addr, args.keepAlive, args.sendBufferSize, args.recvBufferSize);
            }
            catch (TTransportException e)
            {
                throw new RuntimeException(String.format("Unable to create thrift socket to %s:%s", addr.getAddress(), addr.getPort()), e);
            }

            // This is NIO selector service but the invocation will be Multi-Threaded with the Executor service.
            ExecutorService executorService = new JMXEnabledThreadPoolExecutor(DatabaseDescriptor.getRpcMinThreads(),
                                                                               DatabaseDescriptor.getRpcMaxThreads(),
                                                                               60L,
                                                                               TimeUnit.SECONDS,
                                                                               new SynchronousQueue<Runnable>(),
                                                                               new NamedThreadFactory("RPC-Thread"), "RPC-THREAD-POOL");
            TNonblockingServer.Args serverArgs = new TNonblockingServer.Args(serverTransport).inputTransportFactory(args.inTransportFactory)
                                                                               .outputTransportFactory(args.outTransportFactory)
                                                                               .inputProtocolFactory(args.tProtocolFactory)
                                                                               .outputProtocolFactory(args.tProtocolFactory)
                                                                               .processor(args.processor);
            // Check for available processors in the system which will be equal to the IO Threads.
            return new CustomTHsHaServer(serverArgs, executorService, FBUtilities.getAvailableProcessors());
        }
    }
}
