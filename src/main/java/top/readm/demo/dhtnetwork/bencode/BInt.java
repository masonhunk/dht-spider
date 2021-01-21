package top.readm.demo.dhtnetwork.bencode;


import java.io.*;
import java.math.BigInteger;

public class BInt implements BencodeType<BigInteger> {

    private BigInteger intVal;

    public BInt(){
        intVal = BigInteger.ZERO;
    }

    public BInt(BigInteger val){
        this.intVal = val;
    }

    public BInt(int val){
        this.intVal = BigInteger.valueOf(val);
    }

    @Override
    public BigInteger getData() {
        return intVal;
    }

    public void encode(OutputStream out) throws IOException {
        out.write('i');
        out.write(this.intVal.toString(10).getBytes());
        out.write('e');
        out.flush();
    }

    @Override
    public void decode(InputStream in) throws IOException {
        int c = in.read();
        if(c != 'i')  throw new IOException("Must be :i instead of "+ c);
        while(true){
            c = in.read();
            if(c == -1) throw new EOFException("Impossible EOF, this file is not right");
            char ch = (char)c;
            if(ch == 'e'){
                break;
            }
            intVal = intVal.multiply(BigInteger.valueOf(10));
            intVal = intVal.add(BigInteger.valueOf(ch - '0'));
        }
    }

    @Override
    public String toString(){
        return this.intVal.toString(10);
    }

}
