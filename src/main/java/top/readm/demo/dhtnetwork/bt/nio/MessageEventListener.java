package top.readm.demo.dhtnetwork.bt.nio;

import top.readm.demo.dhtnetwork.bt.protocal.BTProtocalMessage;

public interface MessageEventListener {

    void onMessage(BTProtocalMessage message);

}
