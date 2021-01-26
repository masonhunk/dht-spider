package top.readm.demo.dhtnetwork.bt.nio;

import top.readm.demo.dhtnetwork.bt.nio.codec.MessageDecoder;
import top.readm.demo.dhtnetwork.bt.peer.PeerManager;
import top.readm.demo.dhtnetwork.bt.protocal.BTMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;
import java.util.function.Function;

public class ReadHandler {
    private MessageDecoderFactory decoderFactory;

    private ConcurrentHashMap<SocketChannel, MessageDecoder> decoders;

    private MessageDecodeListener listener;

    private ExecutorService executor;

    public ReadHandler(MessageDecoderFactory decoderFactory, MessageDecodeListener listener){
        this.decoders = new ConcurrentHashMap<>();
        this.decoderFactory = decoderFactory;
        this.listener = listener;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void handle(SelectionKey key) throws IOException {
        //decodingThreads.execute(() -> doHandle(key));

        SocketChannel channel = (SocketChannel) key.channel();
        MessageDecoder decoder = decoders.computeIfAbsent(channel, socketChannel -> decoderFactory.createDecoder());
        ByteBuffer buf = (ByteBuffer) key.attachment();
        try {
            /**
             * 1. Read bytes
             */
            if (!key.isValid()) {
                key.cancel();
                channel.close();
                return;
            }
            ;
            int nread = channel.read(buf);
            System.out.println("nread " + nread);
            if (nread == -1) {
                key.cancel();
                return;
            }
            buf.flip();
            /**
             * 2. Decode bytes into messages. Make sure buf is fully read
             */
            Object msg;
            while ((msg = decoder.decode(buf)) != null) {
                final BTMessage btMessage = (BTMessage)msg;
                this.executor.submit(()->this.listener.onMessage((BTMessage) btMessage));
            }
            buf.clear();
        } catch (IOException | CancelledKeyException ex) {
            ex.printStackTrace();
            key.cancel();
            try {
                channel.close();
            } catch (IOException e) {
            }
        }

    }

}
