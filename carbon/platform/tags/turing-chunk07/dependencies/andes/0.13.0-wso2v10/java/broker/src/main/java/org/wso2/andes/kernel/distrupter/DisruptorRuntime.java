package org.wso2.andes.kernel.distrupter;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisruptorRuntime<T> {
    private ExecutorService executorPool = Executors.newCachedThreadPool();
    private Disruptor<T> disruptor;
    private final RingBuffer<T> ringBuffer;

    // TODO : Multiple disruptor runtime for seq and mul
    public DisruptorRuntime(EventFactory<T> eventFactory, EventHandler<T>[] handlers) {
        disruptor = new Disruptor<T>(eventFactory, executorPool,
                new MultiThreadedLowContentionClaimStrategy(65536), // this is for multiple publishers
                new BlockingWaitStrategy());
// Using Blocking wait strategy over Sleeping wait strategy to over come CPU load issue happening when starting up the sever. With this it will
// to resolve the issue https://wso2.org/jira/browse/MB-372
//                new SleepingWaitStrategy());

        //cannot solve the warrning http://stackoverflow.com/questions/1445233/is-it-possible-to-solve-the-a-generic-array-of-t-is-created-for-a-varargs-param
        disruptor.handleEventsWith(handlers);
        ringBuffer = disruptor.start();
    }

    public RingBuffer<T> getRingBuffer() {
        return ringBuffer;
    }
}
