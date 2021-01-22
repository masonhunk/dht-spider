package top.readm.demo.dhtnetwork.bt.tracker;

import lombok.Builder;
import lombok.Data;

/**
 * Tracker客户端，用于向Tracker服务器上报自己的状态，并获取资源的种子信息。主要可获取：种子都在哪些家？
 * 当自己下载完毕的时候，也需要向Tracker上报，让自己做种。
 */
@Data
@Builder
public class TrackerParam {
    /**
     * The sha1 hash of info marked in metafile
     */
    private String infoHash;

    /**
     * A 20 length peer id. Generated at download start???
     */
    private String peerId;

    /**
     * Opitonal
     */
    private String ip;

    private int port;

    private long uploaded;

    private long downloaded;

    private long left;

    private String event;

}
