package top.readm.demo.dhtnetwork.bencode;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BMap implements BencodeType<Map<BBytes, BencodeType>> {

    private static BencodeReader bencodeReader = new BencodeReader();
    private Map<BBytes, BencodeType> bencodeTypeMap;

    public BMap(){
        this.bencodeTypeMap = new HashMap<>();
    }

    public BMap(Map<BBytes, BencodeType> bencodeTypeMap){
        this.bencodeTypeMap = bencodeTypeMap;
    }

    @Override
    public Map<BBytes, BencodeType> getData() {
        return this.bencodeTypeMap;
    }

    @Override
    public void encode(OutputStream out) throws IOException {
        out.write('d');
        for(Map.Entry<BBytes, BencodeType> entry:bencodeTypeMap.entrySet()){
            BBytes key = entry.getKey();
            BencodeType value = entry.getValue();
            key.encode(out);
            value.encode(out);
        }
        out.write('e');
    }

    @Override
    public void decode(InputStream in) throws IOException {
        int c = in.read();
        if(c != 'd') throw new IOException("Expect a: d");
        while (notEnd(in)){
            BencodeType key = bencodeReader.read(in);
            BencodeType value = bencodeReader.read(in);
            this.bencodeTypeMap.put((BBytes)key, value);
        }
        in.read();//Skip 'e'
    }


    private boolean notEnd(InputStream in) throws IOException{
        in.mark(0);
        int c = in.read();
        if(c == -1) throw new EOFException("Unexpected EOF");
        in.reset();
        char ch = (char)c;
        return ch != 'e';
    }

    @Override
    public String toString(){
        return this.bencodeTypeMap.toString();
    }

}
