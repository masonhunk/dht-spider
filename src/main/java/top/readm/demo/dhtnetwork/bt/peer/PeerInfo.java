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
        return ip+":"+port;
    }

    @Override
    public int hashCode(){
        int r = 17;
        r = r * 31 + ip.hashCode();
        r = r * 31 + Integer.hashCode(port);
        return r;
    }

    @Override
    public boolean equals(Object other){
        if(other == null) return false;
        if(!(other instanceof PeerInfo)) return false;
        PeerInfo p = PeerInfo.class.cast(other);
        return p.ip.equals(this.ip) && p.port == port;
    }


}
