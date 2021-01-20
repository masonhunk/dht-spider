package top.readm.demo.dhtnetwork.bencode;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

public class BMap implements BencodeType<Map<String, BencodeType>> {
    @Override
    public Map<String, BencodeType> getData() {
        return null;
    }

    @Override
    public void encode(Writer w) throws IOException {

    }

    @Override
    public void decode(Reader r) throws IOException {

    }
}
