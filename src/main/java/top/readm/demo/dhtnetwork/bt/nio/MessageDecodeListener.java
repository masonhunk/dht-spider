package top.readm.demo.dhtnetwork.bt.nio;

import top.readm.demo.dhtnetwork.bt.protocal.BTMessage;

public interface MessageDecodeListener {

    void onMessage(BTMessage message);

}
