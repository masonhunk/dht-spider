package top.readm.demo.dhtnetwork.bt.nio.codec;

import top.readm.demo.dhtnetwork.bt.protocal.BTMessage;
import top.readm.demo.dhtnetwork.util.BinaryUtil;

import java.nio.ByteBuffer;

/**
 * Stateful decoder... used to decode message.
 * I feel beautiful.
 */
public class BTMessageDecoder implements MessageDecoder<BTMessage> {

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
    public BTMessage decode(ByteBuffer bb) {
        /**
         * 1. Read header bytes
         */
        if (readingHeader) {
            while (bb.hasRemaining() && headerBytesRead < 4) {
                headerBytes[headerBytesRead++] = bb.get();
            }
            if (headerBytesRead < headerBytes.length) {
                //Header is truncated, so we are still waiting for bytes
                return null;
            }
            //Converting to handling body bytes
            int headerLength = BinaryUtil.binaryToBigEndianInt(headerBytes);
            bodyBytes = new byte[headerLength];
            bodyBytesRead = 0;
            readingHeader = false;
        }
        /**
         * 2. Handle body bytes
         */
        while (bb.hasRemaining() && bodyBytesRead < bodyBytes.length) {
            bodyBytes[bodyBytesRead++] = bb.get();
        }
        if (bodyBytesRead < bodyBytes.length) {
            //Truncated body. wait for more
            return null;
        }
        //Consume the whole message body and expect header of next message
        readingHeader = true;
        headerBytesRead = 0;
        return BTMessage.of(bodyBytes);
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
