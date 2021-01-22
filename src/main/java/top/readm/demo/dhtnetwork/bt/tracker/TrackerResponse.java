package top.readm.demo.dhtnetwork.bt.tracker;

import lombok.Data;
import top.readm.demo.dhtnetwork.bt.peer.PeerInfo;

import java.util.List;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/22
 */
@Data
public class TrackerResponse {

    private int interval;

    private String trackerId;

    private int complete;

    private int incomplete;

    private List<PeerInfo> peers;

    private String failureReason;

    public boolean isSuccess(){
        return failureReason == null;
    }

}
