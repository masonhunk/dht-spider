package top.readm.demo.dhtnetwork.bencode;

import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BDictionary implements BencodeType<Map<String, BencodeType>> {

    private static BencodeReader bencodeReader = new BencodeReader();
    private Map<String, BencodeType> bencodeTypeMap;

    public BDictionary(){
        this.bencodeTypeMap = new HashMap<>();
    }

    public BDictionary(Map<String, BencodeType> bencodeTypeMap){
        this.bencodeTypeMap = bencodeTypeMap;
    }

    @Override
    public Map<String, BencodeType> getData() {
        return this.bencodeTypeMap;
    }

    @Override
    public void encode(OutputStream out) throws IOException {
        /**
         * 1. Put all mappings into a treemap thus make it ordered，so the info hash can be
         * determined ignoring orders
         */
        TreeMap<String, BencodeType> treeMap = new TreeMap<>(String::compareTo);
        treeMap.putAll(this.bencodeTypeMap);
        /**
         * 2. Output these records
         */
        out.write('d');
        for(Map.Entry<String, BencodeType> entry:treeMap.entrySet()){
            String key = entry.getKey();
            BencodeType value = entry.getValue();
            new BBytes(key.getBytes()).encode(out);
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
            this.bencodeTypeMap.put(key.toString(), value);
        }
        in.read();//Skip 'e'
    }

    public BencodeType get(String key){
        return this.getData().get(key);
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
