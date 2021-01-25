package top.readm.demo.dhtnetwork.bt.peer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import top.readm.demo.dhtnetwork.bt.nio.MessageDecoderFactory;
import top.readm.demo.dhtnetwork.bt.nio.Reactor;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Slf4j
public class Me {
    private static final int PORT_START = 6881;
    private static final int PORT_END = 6889;
    private AtomicBoolean started;
    private Reactor reactor;
    private int port;
    public Me() throws IOException {
        this.started = new AtomicBoolean(false);
        this.reactor = new Reactor(new MessageDecoderFactory());
    }

    public void startListening() throws IOException{
        if(!this.started.compareAndSet(false, true)){
            return;
        }
        /**
         * Find a port
         */
        ServerSocketChannel ssc = tryBindOnePort();

        /**
         * Register into reactor
         */
        ssc.configureBlocking(false);
        this.reactor.register(ssc);
        this.reactor.start();
        log.info("Start listening on {}", port);
    }

    public Reactor getReactor(){
        return this.reactor;
    }

    private ServerSocketChannel tryBindOnePort() throws IOException {
        IOException lastEx = null;
        for (port = PORT_START; port <= PORT_END; port++) {
            try {
                ServerSocketChannel ssc = ServerSocketChannel.open();
                ssc.bind(new InetSocketAddress("0.0.0.0", port));

                System.out.println("Hi");
                log.info("Binding to {}", port);
                return ssc;
            } catch (IOException ex) {
                lastEx = ex;
            }
        }
        throw lastEx;
    }



}
