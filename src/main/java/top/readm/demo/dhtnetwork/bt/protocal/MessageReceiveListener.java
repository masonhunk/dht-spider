package top.readm.demo.dhtnetwork.bt.protocal;

import lombok.extern.slf4j.Slf4j;
import top.readm.demo.dhtnetwork.bt.nio.MessageDecodeListener;

@Slf4j
public class MessageReceiveListener implements MessageDecodeListener {
    @Override
    public void onMessage(BTMessage message) {
        log.info("Message received!"+message.getClass());
    }
}
