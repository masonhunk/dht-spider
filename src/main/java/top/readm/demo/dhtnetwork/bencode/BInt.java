package top.readm.demo.dhtnetwork.bencode;


import java.io.*;
import java.math.BigInteger;

public class BInt implements BencodeType<BigInteger> {

    private BigInteger intVal;

    @Override
    public BigInteger getData() {
        return intVal;
    }

    public void encode(Writer w) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("i");
        sb.append(intVal.toString(10));
        sb.append("e");

        w.write(sb.toString());
        w.flush();
    }

    @Override
    public void decode(Reader r) throws IOException {
        int c = r.read();
        if(c != 'i')  throw new IOException("Must be :i instead of "+ c);
        BigInteger curr = BigInteger.valueOf(0);
        while(true){
            c = r.read();
            if(c == -1) throw new EOFException("Impossible EOF, this file is not right");
            char ch = (char)c;
            if(ch == 'e'){
                this.intVal = curr;
                break;
            }
            curr = curr.multiply(BigInteger.valueOf(10));
            curr = curr.add(BigInteger.valueOf(ch - '0'));
        }
    }


}
