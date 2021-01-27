package top.readm.demo.dhtnetwork.bt.nio.codec;

import top.readm.demo.dhtnetwork.bt.handshake.HandshakeMessage;
import top.readm.demo.dhtnetwork.bt.protocal.BTProtocalMessage;

import java.nio.ByteBuffer;

/**
 * This is not thread safe.
 */
public class MessageDecoderAdaptor implements MessageDecoder{

    private HandshakeMessageDecoder hDecoder = new HandshakeMessageDecoder();
    private ProtocalMessageDecoder btDecoder = new ProtocalMessageDecoder();
    private boolean shouldHandshake = true;




    @Override
    public Object decode(ByteBuffer bb) {
        if(shouldHandshake){
            HandshakeMessage handshakeMsg = this.hDecoder.decode(bb);
            if(handshakeMsg != null) {
                shouldHandshake = false;
            }
            //I need  to deliver it to my peer manager
            //to verify that this is good nodeid
            //now can i find a container contains current context, including me, peer manager, our data?
            //Such thing is a nodeId based. we should verify that this node id is good
            //To make it simple, such context contains everything we need
            //What about make "me" ?Since there is only one "me".
            //Lets reconsider this situation...
        }

        return this.btDecoder.decode(bb);
    }
}
