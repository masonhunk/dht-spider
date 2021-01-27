package top.readm.demo.dhtnetwork;

import org.junit.Test;
import top.readm.demo.dhtnetwork.bt.nio.MessageDecoderFactory;
import top.readm.demo.dhtnetwork.bt.nio.Reactor;
import top.readm.demo.dhtnetwork.bt.peer.Me;
import top.readm.demo.dhtnetwork.bt.peer.PeerManager;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/25
 */
public class NioTest {


    //Server
    public static void main(String[] args) throws Exception{
        Me me = new Me();
        me.startListening(new PeerManager());
        System.in.read();
    }

}
