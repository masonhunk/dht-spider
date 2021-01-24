package top.readm.demo.dhtnetwork.bt.nio;

import top.readm.demo.dhtnetwork.bt.nio.codec.MessageDecoder;
import top.readm.demo.dhtnetwork.bt.protocal.BTMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;
import java.util.function.Function;

public class ReadHandler {


    /**
     * It is used to do decoding
     */
    private ThreadPoolExecutor decodingThreads;

    private MessageDecoderFactory decoderFactory;

    private ConcurrentHashMap<SocketChannel, MessageDecoder> decoders;



    private BlockingQueue decodedMessage;

    public ReadHandler(MessageDecoderFactory decoderFactory){
        this.decoders = new ConcurrentHashMap<>();
        this.decoderFactory = decoderFactory;
        this.decodingThreads = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors(),
                0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        this.decodedMessage = new LinkedBlockingQueue<>();;
    }

    public void handle(SelectionKey key) throws IOException {
        decodingThreads.execute(() -> doHandle(key));
    }

    private void doHandle(SelectionKey key){
        SocketChannel channel = (SocketChannel) key.channel();
        MessageDecoder decoder = decoders.computeIfAbsent(channel, socketChannel -> decoderFactory.createDecoder());
        ByteBuffer buf = (ByteBuffer) key.attachment();
        try{
            /**
             * 1. Read bytes
             */
            channel.read(buf);
            buf.flip();
            /**
             * 2. Decode bytes into messages. Make sure buf is fully read
             */
            Object msg;
            while((msg = decoder.decode(buf)) != null){
                decodedMessage.offer(msg);
            }
            buf.clear();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }

    }

}
