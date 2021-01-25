package top.readm.demo.dhtnetwork.bt.nio;

import top.readm.demo.dhtnetwork.bt.peer.Me;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/25
 */
public class NioTest {


    //Server
    public static void main(String[] args) throws Exception{
        Me me = new Me();
        me.startListening();
        System.in.read();
    }

}
