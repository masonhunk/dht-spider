package top.readm.demo.dhtnetwork.bencode;

import org.apache.commons.codec.binary.Hex;

import java.io.*;

public class BBytes implements BencodeType<byte[]>{

    public BBytes(){ }


    public BBytes(byte[] bytes){
        this.strBytes = bytes;
        this.bytesLength = strBytes.length;
    }


    private int bytesLength;
    private byte[] strBytes;

    public int getLength() {
        return bytesLength;
    }

    @Override
    public byte[] getData() {
        return strBytes;
    }

    @Override
    public void encode(OutputStream out) throws IOException {
        out.write(Integer.toString(bytesLength, 10).getBytes());
        out.write(':');
        out.write(strBytes);
        out.flush();
    }

    @Override
    public void decode(InputStream in) throws IOException {
        /**
         * 1. Read string bytes length. Length is started with a digit, then end with ":".
         */
        int bytesLen = readBytesLength(in);

        /**
         * 2. Read string bytes.
         */
        byte[] strVal = readStringBytes(in, bytesLen);

        /**
         * 3. Set field
         */
        this.bytesLength = bytesLen;
        this.strBytes = strVal;
    }

    private int readBytesLength(InputStream in) throws IOException{
        int length = 0;
        while (true){
            int c = in.read();
            if(c == -1)  throw new EOFException("Unexpected EOF");
            char ch = (char)c;
            if(ch == ':') return length;
            int digit = ch - '0';
            length *= 10;
            length += digit;
        }
    }

    /**
     * 这个到底是字符数，还是字节数？
     * append添加的字符，还是字节？
     */
    private byte[] readStringBytes(InputStream in, int expectedLen) throws IOException{
        byte[] bytes = new byte[expectedLen];
        int dataCount = 0;
        while (dataCount < expectedLen){
            int c = in.read();
            if(c == -1)  throw new EOFException("Unexpected EOF");
            bytes[dataCount++] = (byte)c;
        }
        return bytes;
    }

    @Override
    public String toString(){
        return new String(this.strBytes);
    }
}
