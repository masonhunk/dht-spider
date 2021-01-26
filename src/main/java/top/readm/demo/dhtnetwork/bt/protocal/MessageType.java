package top.readm.demo.dhtnetwork.bt.protocal;

import lombok.Getter;

@Getter
public enum  MessageType {

    KeepAlive((byte)-1),

    Handshake((byte)0),

    BitField((byte)5);

    private byte type;
    MessageType(byte type){
        this.type = type;
    }

}
