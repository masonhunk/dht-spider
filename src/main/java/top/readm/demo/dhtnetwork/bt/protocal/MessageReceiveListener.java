package top.readm.demo.dhtnetwork.bt.protocal;

import lombok.extern.slf4j.Slf4j;
import top.readm.demo.dhtnetwork.bt.nio.MessageEventListener;

@Slf4j
public class MessageReceiveListener implements MessageEventListener {
    @Override
    public void onMessage(BTProtocalMessage message) {
        log.info("Message received!"+message.getClass());
    }
}
