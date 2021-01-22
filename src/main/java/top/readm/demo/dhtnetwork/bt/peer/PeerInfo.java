package top.readm.demo.dhtnetwork.bt.peer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeerInfo {

    private String peerId;

    private String ip;

    private int port;

    @Override
    public String toString(){
        return "ip: "+ ip +" ;port: " + port + " ;peerId:"+peerId;
    }

}
