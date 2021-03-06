package top.readm.demo.dhtnetwork.bt.peer;

import top.readm.demo.dhtnetwork.bt.nio.MessageDecoderFactory;
import top.readm.demo.dhtnetwork.bt.nio.Reactor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PeerManager {

    private ConcurrentHashMap<PeerInfo, SocketChannel> peerConnections;
    private ConcurrentHashMap<PeerInfo, PeerStatisticData> peerStatisticData;

    public PeerManager() throws IOException{
        peerConnections = new ConcurrentHashMap<>();
        peerStatisticData = new ConcurrentHashMap<>();
    }

    /**
     * Put a peer into management, and start handshaking process.
     * This method is called on two situations: 1). It is acquired from tracker response 2). It is from connection
     * @param p
     */
    public void addPeer(PeerInfo p, SocketChannel channel) throws IOException {
        if(peerConnections.contains(p)) return;
    }

    /**
     * Update the last message received time. It is called when receiving any message outside
     * @param peerInfo
     * @param statisticData
     */
    public void updateStatistics(PeerInfo peerInfo, PeerStatisticData statisticData){}

    /**
     * Send keep alive messages. And kick out the timeout data. Called by schedule thread
     */
    public void tick(){
        SocketChannel channel;
    }

    /**
     * Kick out some peer. Close connection ...Called by owner, or when IOException happens
     * @param peerInfo
     */
    public void stopPeer(PeerInfo peerInfo){}
}
