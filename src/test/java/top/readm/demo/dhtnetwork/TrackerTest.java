package top.readm.demo.dhtnetwork;

import org.junit.Test;
import top.readm.demo.dhtnetwork.bencode.BMap;
import top.readm.demo.dhtnetwork.bt.BTLoader;
import top.readm.demo.dhtnetwork.bt.peer.PeerNode;
import top.readm.demo.dhtnetwork.bt.model.MetaFile;
import top.readm.demo.dhtnetwork.bt.tracker.TrackerParam;
import top.readm.demo.dhtnetwork.bt.tracker.TrackerResponse;
import top.readm.demo.dhtnetwork.bt.tracker.client.HttpTrackerClient;
import top.readm.demo.dhtnetwork.util.BinaryUtil;

import java.io.InputStream;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/22
 */
public class TrackerTest {

    @Test
    public void requestSample3() throws Exception{
        InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("sample3.torrent");
        MetaFile metaFile = new BTLoader().load(in);

        TrackerParam param = TrackerParam
                .builder().infoHash(BinaryUtil.byteArrayToURLString(metaFile.getInfoSha1()))
                .port(6881)
                .peerId(new PeerNode().getPeerId())
                .event("started")
                .build();

        HttpTrackerClient client = new HttpTrackerClient();
        TrackerResponse  response = client.tracker(metaFile.getAnnounce(), param).get();
        System.out.println(response.getPeers());


    }

}
