package top.readm.demo.dhtnetwork.bt.bencode;

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
    public void encode(OutputStream out) throws IOException {
        out.write('l');
        for(BencodeType bItem: list){
            bItem.encode(out);
        }
        out.write('e');
        out.flush();
    }

    @Override
    public void decode(InputStream in) throws IOException {
        int c = in.read();
        if(c != 'l') throw new IOException("Expect a : l");
        List<BencodeType> vector = new ArrayList<>();
        while (notEnd(in)){
            BencodeType bItem = bencodeReader.read(in);
            vector.add(bItem);
        }
        in.read();//Skip 'e'
        this.list = vector;
    }

    private boolean notEnd(InputStream in) throws IOException{
        in.mark(0);
        int c = in.read();
        if(c == -1) throw new EOFException("Unexpected EOF");
        in.reset();
        return (char)c != 'e';
    }

    @Override
    public String toString(){
        return this.list.toString();
    }
}
