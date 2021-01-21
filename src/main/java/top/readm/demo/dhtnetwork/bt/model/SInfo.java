package top.readm.demo.dhtnetwork.bt.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SInfo extends Info {

    private String name;

    private long length;

    private long pieceLength;

    private byte[] pieces;
}
