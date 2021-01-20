package top.readm.demo.dhtnetwork.bencode;

import java.io.*;

public class BString implements BencodeType<String>{

    public BString(){ }


    public BString(String strVal){
        this.strLen = strVal.length();
        this.strVal = strVal;
    }


    private int strLen;
    private String strVal;

    public int getLength() {
        return strLen;
    }

    @Override
    public String getData() {
        return strVal;
    }

    @Override
    public void encode(Writer w) throws IOException {
        w.write(Integer.toString(strLen));
        w.write(':');
        w.write(strVal);
        w.flush();
    }

    @Override
    public void decode(Reader r) throws IOException {
        /**
         * 1. Read length. Length is started with a digit, then end with ":".
         */
        int strLen = readStringLength(r);

        /**
         * 2. Read string.
         */
        String strVal = readStringVal(r, strLen);

        /**
         * 3. Set field
         */
        this.strLen = strLen;
        this.strVal = strVal;
    }

    private int readStringLength(Reader reader) throws IOException{
        int length = 0;
        while (true){
            int c = reader.read();
            if(c == -1)  throw new EOFException("Unexpected EOF");
            char ch = (char)c;
            if(ch == ':') return length;
            int digit = ch - '0';
            length *= 10;
            length += digit;
        }
    }

    private String readStringVal(Reader reader, int expectedLen) throws IOException{
        int dataCount = 0;
        StringBuilder sb = new StringBuilder();
        while (dataCount < expectedLen){
            int c = reader.read();
            if(c == -1)  throw new EOFException("Unexpected EOF");
            sb.append((char)c);
            dataCount++;
        }
        return sb.toString();
    }
}
