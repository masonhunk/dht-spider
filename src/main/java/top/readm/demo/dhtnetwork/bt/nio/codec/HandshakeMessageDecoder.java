package top.readm.demo.dhtnetwork.bt.nio.codec;

import top.readm.demo.dhtnetwork.bt.handshake.HandshakeMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

/**
 * Handshake message have different formats. So I have to write another decoding logic.
 */
public class HandshakeMessageDecoder implements MessageDecoder<HandshakeMessage>{

    private byte[] bodyBytes = new byte[68];
    private int totalRead = 0;

    @Override
    public HandshakeMessage decode(ByteBuffer bb) {
        /*
         * Read handshake until get a full message
         */
        while(bb.hasRemaining() && totalRead < bodyBytes.length){
            bodyBytes[totalRead++] = bb.get();
        }
        if(totalRead < 68) return null;
        /**
         * Verify this is a valid message
         */
        byte ptrLen = bodyBytes[0];
        if(ptrLen != HandshakeMessage.PTR_LEN) throw new RuntimeException("Not a valid handshake message: invalid ptrLen "+ptrLen);

        String ptrStr = new String(bodyBytes, 1, 19);
        if(!Objects.equals(HandshakeMessage.PTR, ptrStr)) throw new RuntimeException("Not a valid handshake message: Invalid ptrString "+ptrStr);

        byte[] infoHash = Arrays.copyOfRange(bodyBytes, 28, 48);
        byte[] peerBytes = Arrays.copyOfRange(bodyBytes, 48, 68);
        HandshakeMessage msg = new HandshakeMessage();
        msg.setPtrLen(ptrLen);
        msg.setPtrstr(ptrStr);
        msg.setInfoHash(infoHash);
        msg.setPeerId(new String(peerBytes));

        return msg;
    }
}
