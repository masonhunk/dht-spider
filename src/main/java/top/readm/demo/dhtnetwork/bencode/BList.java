package top.readm.demo.dhtnetwork.bencode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BList implements BencodeType<List<BencodeType>> {

    private static BencodeReader bencodeReader = new BencodeReader();
    private List<BencodeType> list;

    public BList(){
        this.list = new ArrayList<>();
    }

    public BList(List<BencodeType> list){
        this.list = list;
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
        while (notEnd(pReader)){
            BencodeType bItem = bencodeReader.read(pReader);
            vector.add(bItem);
        }

        this.list = vector;
    }

    private boolean notEnd(PushbackReader pReader) throws IOException{
        int c = pReader.read();
        if(c == -1) throw new EOFException("Unexpected EOF");
        pReader.unread(c);
        return (char)c != 'e';
    }

    @Override
    public String toString(){
        return this.list.toString();
    }
}
