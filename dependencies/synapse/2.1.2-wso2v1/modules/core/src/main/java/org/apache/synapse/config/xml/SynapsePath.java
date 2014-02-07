package org.apache.synapse.config.xml;


import org.apache.axiom.om.*;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.logging.Log;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.apache.synapse.transport.passthru.PassThroughConstants;
import org.apache.synapse.transport.passthru.Pipe;
import org.apache.synapse.transport.passthru.util.RelayUtils;
import org.apache.synapse.util.streaming_xpath.custom.components.ParserComponent;
import org.apache.synapse.util.xpath.DOMSynapseXPathNamespaceMap;
import org.apache.synapse.util.xpath.SynapseJsonPath;
import org.apache.synapse.util.xpath.SynapseXPath;
import org.jaxen.JaxenException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Iterator;

public abstract class SynapsePath extends AXIOMXPath {

    public static final String X_PATH = "X_PATH";
    public static final String JSON_PATH = "JSON_PATH";
    private String pathType = null;

    public DOMSynapseXPathNamespaceMap domNamespaceMap = new DOMSynapseXPathNamespaceMap();

    public String expression;

    public int bufferSizeSupport = 1024*8;

    public Log log;

    public boolean contentAware;

    public SynapsePath(OMElement element, String xpathExpr, Log log) throws JaxenException {
        super(element, xpathExpr);
        this.pathType = inferPathType(xpathExpr);
        this.log = log;
    }

    public SynapsePath(String xpathExpr, Log log) throws JaxenException {
        super(xpathExpr);
        this.pathType = inferPathType(xpathExpr);
        this.log = log;
    }

    public SynapsePath(String path, String pathType, Log log) throws JaxenException {
        super("/");
        this.expression = path;
        this.pathType = inferPathType(path);
        this.log = log;
    }

    private String inferPathType(String expression) {
        if(expression.startsWith("json-eval(")) {
            return X_PATH;
        } else {
            return JSON_PATH;
        }
    }

    public SynapsePath(OMAttribute attribute, Log log) throws JaxenException {
        super(attribute);
        this.pathType = X_PATH;
        this.log = log;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getPathType() {
        return this.pathType;
    }

    public void setPathType(String pathType) {
        this.pathType = pathType;
    }

    public boolean isContentAware() {
        return this.contentAware;
    }

    public abstract String stringValueOf(MessageContext synCtx);

    public void handleException(String msg, Throwable e) {
        log.error(msg, e);
        throw new SynapseException(msg, e);
    }

    public void addNamespacesForFallbackProcessing(OMElement element){

        OMElement currentElem = element;

        while (currentElem != null) {
            Iterator it = currentElem.getAllDeclaredNamespaces();
            while (it.hasNext()) {

                OMNamespace n = (OMNamespace) it.next();
                // Exclude the default namespace as explained in the Javadoc above
                if (n != null && !"".equals(n.getPrefix())) {
                    ParserComponent.addToNameSpaceMap(n.getPrefix(), n.getNamespaceURI());
                    domNamespaceMap.addNamespace(n.getPrefix(), n.getNamespaceURI());
                }
            }

            OMContainer parent = currentElem.getParent();
            //if the parent is a document element or parent is null ,then return
            if (parent == null || parent instanceof OMDocument) {
                return;
            }
            if (parent instanceof OMElement) {
                currentElem = (OMElement) parent;
            }
        }

    }

    public InputStream getMessageInputStreamPT(org.apache.axis2.context.MessageContext context) throws IOException {
        Pipe pipe= (Pipe) context.getProperty(PassThroughConstants.PASS_THROUGH_PIPE);
        if (pipe != null && context.getProperty(PassThroughConstants.BUFFERED_INPUT_STREAM) != null){
            BufferedInputStream bufferedInputStream= (BufferedInputStream) context.getProperty(PassThroughConstants.BUFFERED_INPUT_STREAM);
            try{
                bufferedInputStream.reset();
                bufferedInputStream.mark(0);
            }catch (Exception e) {
                //just ignore the error
            }
            return bufferedInputStream;
        }

        if(pipe != null ){
            BufferedInputStream bufferedInputStream =new BufferedInputStream(pipe.getInputStream());
            bufferedInputStream.mark(128 * 1024);
            OutputStream resetOutStream = pipe.resetOutputStream();

            ReadableByteChannel inputChannel = Channels.newChannel(bufferedInputStream);
            WritableByteChannel outputChannel = Channels.newChannel(resetOutStream);
            if(!fastChannelCopy(inputChannel,  outputChannel)){
                //TODO:need to find a proper solution
                try {
                    bufferedInputStream.reset();
                    bufferedInputStream.mark(0);
                    context.setProperty(PassThroughConstants.BUFFERED_INPUT_STREAM,bufferedInputStream);
                    RelayUtils.buildMessage(context);
                } catch (Exception e) {
                    log.error("Error while building message", e);
                }
                return null;
            }
            try {
                bufferedInputStream.reset();
                bufferedInputStream.mark(0);
            } catch (Exception e) {
                // just ignore the error
            }
            pipe.setRawSerializationComplete(true);
            return bufferedInputStream;
        }
        return null;
    }

    //Kind of a hack, where when need to override the buffers, we may need to
    //figure out the size of the io buffer, the in-stream is support to fit in
    //then we will write in to the outputbuffer, otherwise will have to go in normal
    //TODO:need a betterway to to resolve this
    public boolean fastChannelCopy(final ReadableByteChannel src, final WritableByteChannel dest) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(16*1024);
        int i =1;
        int size = bufferSizeSupport;
        while (src.read(buffer) != -1) {
            int remains =size-(8*1024*i);
            if(remains<0){//remains zero..
                return false;
            }

            // prepare the buffer to be drained
            buffer.flip();
            // write to the channel, may block
            dest.write(buffer);
            // If partial transfer, shift remainder down
            // If buffer is empty, same as doing clear()
            buffer.compact();
            i++;

        }
        // EOF will leave buffer in fill state
        buffer.flip();
        // make sure the buffer is fully drained.
        while (buffer.hasRemaining()) {
            dest.write(buffer);
        }

        return true;
    }

}
