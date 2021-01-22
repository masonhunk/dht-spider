package top.readm.demo.dhtnetwork.bt.peer;

import java.util.UUID;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/22
 */
public class PeerNode {

    private String peerId;

    public PeerNode(String peerId){
        this.peerId = peerId;
    }

    public PeerNode(){
        this.peerId = UUID.randomUUID().toString().substring(0, 20);
    }

    public String getPeerId(){
        return this.peerId;
    }

}
