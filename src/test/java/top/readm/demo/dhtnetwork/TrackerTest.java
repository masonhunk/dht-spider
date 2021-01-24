package top.readm.demo.dhtnetwork;

import org.junit.Test;
import top.readm.demo.dhtnetwork.bt.bt.BTLoader;
import top.readm.demo.dhtnetwork.bt.peer.PeerNode;
import top.readm.demo.dhtnetwork.bt.bt.MetaFile;
import top.readm.demo.dhtnetwork.bt.tracker.TrackerParam;
import top.readm.demo.dhtnetwork.bt.tracker.TrackerResponse;
import top.readm.demo.dhtnetwork.bt.tracker.client.HttpTrackerClient;
import top.readm.demo.dhtnetwork.util.BinaryUtil;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/22
 */
public class TrackerTest {

    @Test
    public void requestFindPeers() throws Exception{
        InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("sample2.torrent");
        MetaFile metaFile = new BTLoader().load(in);

        TrackerParam param = TrackerParam
                .builder().infoHash(BinaryUtil.byteArrayToURLString(metaFile.getInfoSha1()))
                .port(6881)
                .peerId(new PeerNode().getPeerId())
                .event("started")
                .numwant(10)
                .build();

        HttpTrackerClient client = new HttpTrackerClient();
        if(metaFile.getAnnounce() != null && metaFile.getAnnounce().startsWith("http")){
            TrackerResponse  response = client.tracker(URLDecoder.decode(metaFile.getAnnounce()), param).get();
            System.out.println(response.getPeers());
        }
        else{
            List<String> annouceList = metaFile.getAnnounceList().stream().flatMap(a->a.stream())
                    .collect(Collectors.toList());
            for(String announce: annouceList){
                if(announce != null && announce.startsWith("http")){
                    TrackerResponse  response = client.tracker(URLDecoder.decode(announce), param).get();
                    System.out.println(response.getPeers());
                    break;
                }
            }
        }


    }

}
