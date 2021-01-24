package top.readm.demo.dhtnetwork.bt.nio.codec;

import java.nio.ByteBuffer;

/**
 * Decode bytes into message
 */
public interface MessageDecoder<T> {

    T decode(ByteBuffer bb);

}
