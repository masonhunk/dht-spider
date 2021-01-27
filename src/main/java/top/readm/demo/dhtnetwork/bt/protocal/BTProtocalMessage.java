package top.readm.demo.dhtnetwork.bt.protocal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface BTProtocalMessage {

    static BTProtocalMessage of(byte[] bodyBytes) {//
        //Keep alive message is really special, it does not have a type
        if(bodyBytes.length == 0){
            return new KeepAliveMessage();
        }
        byte type = bodyBytes[0];
        if(type == MessageType.BitField.getType()){
            return new BitfieldMessage();
        }
        throw new IllegalArgumentException("Not supported");
    }

    void write(OutputStream out) throws IOException;

    void read(InputStream in) throws IOException;

}
