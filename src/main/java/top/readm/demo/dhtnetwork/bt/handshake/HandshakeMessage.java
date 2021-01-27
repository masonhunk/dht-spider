package top.readm.demo.dhtnetwork.bt.handshake;

import lombok.Data;
import lombok.Getter;

import java.util.Arrays;

@Data
public class HandshakeMessage{

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

}
