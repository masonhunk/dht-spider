package top.readm.demo.dhtnetwork.bencode;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BMap implements BencodeType<Map<String, BencodeType>> {

    private static BencodeReader bencodeReader = new BencodeReader();
    private Map<String, BencodeType> bencodeTypeMap;

    public BMap(){
        this.bencodeTypeMap = new HashMap<>();
    }

    public BMap(Map<String, BencodeType> bencodeTypeMap){
        this.bencodeTypeMap = bencodeTypeMap;
    }

    @Override
    public Map<String, BencodeType> getData() {
        return this.bencodeTypeMap;
    }

    @Override
    public void encode(Writer w) throws IOException {
        w.write('d');
        for(Map.Entry<String, BencodeType> entry:bencodeTypeMap.entrySet()){
            String key = entry.getKey();
            BencodeType value = entry.getValue();
            new BString(key).encode(w);
            value.encode(w);
        }
        w.write('e');
    }

    @Override
    public void decode(Reader r) throws IOException {
        int c = r.read();
        if(c != 'd') throw new IOException("Expect a: d");
        while (notEnd(r)){
            BencodeType key = bencodeReader.read(r);
            BencodeType value = bencodeReader.read(r);
            this.bencodeTypeMap.put((String)key.getData(), value);
        }
    }


    private boolean notEnd(Reader reader) throws IOException{
        reader.mark(0);
        int c = reader.read();
        if(c == -1) throw new EOFException("Unexpected EOF");
        reader.reset();
        return (char)c != 'e';
    }

    @Override
    public String toString(){
        return this.bencodeTypeMap.toString();
    }

}
