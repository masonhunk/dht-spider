package top.readm.demo.dhtnetwork.bt.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class AcceptHandler {

    private Reactor reactor;
    private ConnectionAcceptEventListener listener;
    public AcceptHandler(Reactor reactor, ConnectionAcceptEventListener listener){
        this.reactor = reactor;
        this.listener = listener;
    }

    public void handle(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel)key.channel();;
        SocketChannel channel = ssc.accept();
        reactor.register(channel);
        this.listener.onChannelEstablished(channel);
    }


}
