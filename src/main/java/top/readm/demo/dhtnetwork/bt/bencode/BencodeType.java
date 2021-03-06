package top.readm.demo.dhtnetwork.bt.bencode;

import java.io.*;

/**
 * 基本数据类型：面向原语的思想，亦是OO的起点：模拟真实物体。
 * 采用WriterReader而不是面向流，这是因为整个编解码协议就是面向字符串的，而这些是面向字符流，而非字节流。
 * 这是面向字符流的编码协议！
 */
public interface BencodeType<T> {

    T getData();

    void encode(OutputStream out) throws IOException;

    void decode(InputStream in) throws IOException;

}
