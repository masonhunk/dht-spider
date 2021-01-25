package top.readm.demo.dhtnetwork.bt.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Manages a collection of socket connections. You can create multiple reactor
 */
public class Reactor {

    /**
     * Core component.
     */
    private Selector selector;
    private AcceptHandler acceptHandler;
    private ReadHandler readHandler;
    private AtomicBoolean isRunning;

    private ReentrantLock selectorLock;

    public Reactor(MessageDecoderFactory decoderFactory) throws IOException {
        this.selector = Selector.open();
        this.acceptHandler = new AcceptHandler(this);
        this.readHandler = new ReadHandler(decoderFactory);
        isRunning = new AtomicBoolean(false);
        this.selectorLock = new ReentrantLock();
    }

    public void register(ServerSocketChannel serverSocketChannel) throws IOException{
        selectorLock.lock();
        try{
            serverSocketChannel.configureBlocking(false);
            this.selector.wakeup();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }
        finally {
            selectorLock.unlock();
        }
    }

    public void register(SocketChannel socketChannel) throws IOException{
        selectorLock.lock();
        try{
            socketChannel.configureBlocking(false);
            this.selector.wakeup();
            socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(4096));
        }
        finally {
            selectorLock.unlock();
        }

    }

    /**
     * Start reactor.
     */
    public void start() throws IOException{
        if(!isRunning.compareAndSet(false, true)){
            return;
        }
        System.out.println("Started");
        while(isRunning.get()){
            int n = selector.select();//It will block register!
            this.selectorLock.lock();
            this.selectorLock.unlock();
            if(n == 0){
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for(SelectionKey key: selectionKeys){
                if(key.isAcceptable()){
                    this.acceptHandler.handle(key);
                }
                if(key.isReadable()){
                    this.readHandler.handle(key);
                }
            }
            selectionKeys.clear();;
        }
    }

    /**
     * Stop the reactor. It is called by some man
     */
    public void stop(){
        isRunning.set(false);
    }

}
