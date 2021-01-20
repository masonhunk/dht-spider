package top.readm.demo.dhtnetwork.bencode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BList implements BencodeType<List<BencodeType>> {

    private BencodeReader bencodeReader;
    private List<BencodeType> list;

    public BList(List<BencodeType> list, BencodeReader reader){
        this.list = list;
        this.bencodeReader = reader;
    }

    public BList(BencodeReader reader){
        this(null, reader);
        this.list = new ArrayList<>();
    }

    public BList(List<BencodeType> list){
        this(list, null);
    }

    @Override
    public List<BencodeType> getData() {
        return list;
    }

    @Override
    public void encode(Writer w) throws IOException {
        w.write("l");
        for(BencodeType bItem: list){
            bItem.encode(w);
        }
        w.write("e");
    }

    @Override
    public void decode(Reader r) throws IOException {
        PushbackReader pReader = (PushbackReader)r;
        int c = pReader.read();
        if(c != 'l') throw new IOException("Expect a : l");
        List<BencodeType> vector = new ArrayList<>();
        while (true){
            BencodeType bItem = this.bencodeReader.read(pReader);
            if(bItem == null) break;
            vector.add(bItem);
        }

        this.list = vector;
    }
}
