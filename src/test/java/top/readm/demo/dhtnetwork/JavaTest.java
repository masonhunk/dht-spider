package top.readm.demo.dhtnetwork;

import org.junit.Assert;
import org.junit.Test;
import top.readm.demo.dhtnetwork.bt.peer.PeerInfo;

import java.util.HashMap;
import java.util.HashSet;

public class JavaTest {

    @Test
    public void testPeerInfo(){
        PeerInfo p1 = new PeerInfo(null, "127.0.0.1", 5050);
        PeerInfo p2 = new PeerInfo(null, "127.0.0.1", 5050);
        //Assert.assertEquals(p1.hashCode(), p2.hashCode());
        //Assert.assertEquals(p1, p2);

        /**
         * 1. If you don't rewrite hash code, this will be "2"
         */
        HashMap<PeerInfo,Integer> peers = new HashMap<>();
        peers.put(p1,1);
        peers.put(p2,1);
        Assert.assertEquals(1, peers.size());

        /**
         * 2. If you dont rewrite equals, then matching peer will not be loaded even hashcode is same
         */
        peers = new HashMap<>();
        peers.put(p1,1);

        Assert.assertEquals(1, peers.get(p2).intValue());
    }


}
