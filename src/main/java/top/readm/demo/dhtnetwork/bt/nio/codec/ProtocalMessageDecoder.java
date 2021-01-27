package top.readm.demo.dhtnetwork.bt.nio.codec;

import top.readm.demo.dhtnetwork.bt.protocal.BTProtocalMessage;
import top.readm.demo.dhtnetwork.bt.handshake.HandshakeMessage;
import top.readm.demo.dhtnetwork.util.BinaryUtil;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Stateful decoder... used to decode message.
 * Each run of this decoder will at most decode one message, so it will may not consume the byte buffer completely. Please
 * consume bytebuffer in a loop until it is consumed fully
 *
 * This is not thread safe, please use it in thread safe mode
 */
public class ProtocalMessageDecoder implements MessageDecoder<BTProtocalMessage> {

    /**
     * Header related
     */
    private byte[] headerBytes = new byte[4];
    private int headerBytesRead;

    /**
     * Body related
     */
    private byte[] bodyBytes;
    private int bodyBytesRead;

    private boolean readingHeader = true;
    @Override
    public BTProtocalMessage decode(ByteBuffer bb) {
        /**
         * 1. Read header bytes
         */
        if (readingHeader) {
            if(!consumeHeader(bb)){
                return null;
            }
            prepareBodyReading(BinaryUtil.binaryToBigEndianInt(headerBytes));
        }
        /**
         * 2. Handle body bytes
         */
        //Body is truncated for now
        if(!consumeBody(bb)) return null;
        //Consume the whole message body and expect header of next message
        finishBodyReading();
        /**
         * 3. Convert body bytes to message.
         */
        //Handshake
        if(bodyBytes.length == 19 && Arrays.equals(bodyBytes, HandshakeMessage.PTR_BYTES)){
            prepareBodyReading(48);
            if(!consumeBody(bb)) return null;
        }
        return BTProtocalMessage.of(bodyBytes);
    }

    /**
     * Consume a bb, if it reads a complete header, return true. otherwise return false
     * @param bb
     * @return
     */
    private boolean consumeHeader(ByteBuffer bb){
        while (bb.hasRemaining() && headerBytesRead < headerBytes.length) {
            headerBytes[headerBytesRead++] = bb.get();
        }
        return headerBytesRead == headerBytes.length;
    }

    private boolean consumeBody(ByteBuffer bb){
        while (bb.hasRemaining() && bodyBytesRead < bodyBytes.length) {
            bodyBytes[bodyBytesRead++] = bb.get();
        }
        return bodyBytesRead == bodyBytes.length;
    }

    /**
     * According to header length, create body buffer to receive payload.
     */
    private void prepareBodyReading(int bodyLen){
        bodyBytes = new byte[bodyLen];
        bodyBytesRead = 0;
        readingHeader = false;
    }

    /**
     * Clear status, prepare to read header bytes again(next message)
     */
    private void finishBodyReading(){
        readingHeader = true;
        headerBytesRead = 0;
    }

    public boolean isReadingHeader(){
        return this.readingHeader;
    }

    public int getHeaderBytesRead(){
        return this.headerBytesRead;
    }

    public int getBodyBytesRead(){
        return this.bodyBytesRead;
    }

}
