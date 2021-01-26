package top.readm.demo.dhtnetwork.bt.peer;

import lombok.extern.slf4j.Slf4j;
import top.readm.demo.dhtnetwork.bt.nio.ConnectionAcceptEventListener;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

@Slf4j
public class PeerPassiveConnectionListener implements ConnectionAcceptEventListener {

    private PeerManager peerManager;

    public PeerPassiveConnectionListener(PeerManager peerManager){
        this.peerManager = peerManager;
    }

    /**
     * Resolve Peer info from remote connection. Put it into peer manager.
     * If the connection is bad, then remove it.
     * @param socketChannel
     */
    @Override
    public void onChannelEstablished(SocketChannel socketChannel) {
        log.info("socket establised");

        PeerInfo p = null;
        try {
            /**
             * Resolve PeerInfo from socketchannel
             */
            InetSocketAddress r = InetSocketAddress.class.cast(socketChannel.getRemoteAddress());
            p = new PeerInfo(null, r.getHostName(), r.getPort());
            System.out.println("host name:" + r.getHostName());
            System.out.println("host string:" + r.getHostString());
            /**
             * 2. Put it into management
             */
            this.peerManager.addPeer(p, socketChannel);
        } catch (Exception ex) {
            log.error("Something happened on channel established", ex);
            return;
        }
    }
}
