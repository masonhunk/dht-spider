package top.readm.demo.dhtnetwork.bt.nio;

import java.nio.channels.SocketChannel;

public interface ConnectionAcceptEventListener {

    void onChannelEstablished(SocketChannel socketChannel);

}
