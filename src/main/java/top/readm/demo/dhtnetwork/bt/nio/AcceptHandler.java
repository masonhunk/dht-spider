package top.readm.demo.dhtnetwork.bt.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class AcceptHandler {

    private Reactor reactor;
    public AcceptHandler(Reactor reactor){
        this.reactor = reactor;
    }

    public void handle(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel)key.channel();;
        SocketChannel channel = ssc.accept();
        reactor.register(channel);
    }


}
