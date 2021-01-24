package top.readm.demo.dhtnetwork.bt.nio;

import top.readm.demo.dhtnetwork.bt.nio.codec.BTMessageDecoder;
import top.readm.demo.dhtnetwork.bt.nio.codec.MessageDecoder;

public class MessageDecoderFactory {

    public MessageDecoder createDecoder(){
        return new BTMessageDecoder();
    }

}
