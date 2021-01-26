package top.readm.demo.dhtnetwork.bt.protocal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class HandshakeMessage implements BTMessage{

    public static final int PTR_LEN;
    public static final String PTR;
    public static final byte[] PTR_BYTES;

    static {
        PTR_LEN = 19;
        PTR = "BitTorrent protocol";
        PTR_BYTES = PTR.getBytes();
    }

    private int ptrLen = PTR_LEN;

    private String ptrstr = PTR;

    private byte[] reserved;

    private byte[] infoHash;

    private String peerId;

    public HandshakeMessage populateFieldsFromExtBytes(byte[] extBytes) {
        this.reserved = Arrays.copyOfRange(extBytes, 0, 8);
        this.infoHash = Arrays.copyOfRange(extBytes,8,28);
        this.peerId = new String(Arrays.copyOfRange(extBytes,28,48));
        return this;
    }

    @Override
    public void write(OutputStream out) throws IOException {

    }

    @Override
    public void read(InputStream in) throws IOException {

    }
}
