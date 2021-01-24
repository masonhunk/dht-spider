package top.readm.demo.dhtnetwork.bt.peer;

import lombok.Getter;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

@Getter
public class Me {

    private ServerSocketChannel serverSocketChannel;
    public Me(int port) throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("0.0.0.0", port));
    }

}
