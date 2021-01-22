package top.readm.demo.dhtnetwork.bt.peer;

import top.readm.demo.dhtnetwork.bencode.*;
import top.readm.demo.dhtnetwork.util.BinaryUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/22
 */
public class PeerInfoParser {

    /**
     * Parse ip,port from binary peer bytes. See
     * https://wiki.theory.org/BitTorrentSpecification#request:_.3Clen.3D0013.3E.3Cid.3D6.3E.3Cindex.3E.3Cbegin.3E.3Clength.3E
     * @param data
     * @return
     */
    public static List<PeerInfo> parseBinary(byte[] data){
        if(data.length % 6 != 0) throw new RuntimeException("Invalid data length:"+data.length);
        List<PeerInfo> peers = new ArrayList<>();
        for(int i=0;i<data.length;i+=6) {
            PeerInfo peer = new PeerInfo();
            /**
             * 1. Parse ip
             */
            StringBuilder sb = new StringBuilder();
            sb.append(data[i] & 0xFF);
            sb.append(".");
            sb.append(data[i + 1] & 0xFF);
            sb.append(".");
            sb.append(data[i + 2] & 0xFF);
            sb.append(".");
            sb.append(data[i + 3] & 0xFF);
            peer.setIp(sb.toString());
            /**
             * 2. Parse port
             */
            int port = data[i+4] << 8;
            port += data[i+5];
            port &= 0xFFFF;
            peer.setPort(port);

            peers.add(peer);
        }
        return peers;
    }

    public static List<PeerInfo> parseDictionary(BMap bMap){
        BList bList = BList.class.cast(bMap.get("peers"));
        List<PeerInfo> peers = new ArrayList<>();
        for(BencodeType b: bList.getData()) {
            BMap peerRaw = BMap.class.cast(b);
            PeerInfo peerInfo = new PeerInfo();
            BBytes peerId = (BBytes) peerRaw.get("peer id");
            BBytes ip = (BBytes) peerRaw.get("ip");
            BInt port = (BInt)peerRaw.get("port");
            if(peerId != null){
                peerInfo.setPeerId(peerId.toString());
            }

            if(ip == null) throw new RuntimeException("Missing ip");
            peerInfo.setIp(ip.toString());
            if(port == null) throw new RuntimeException("Missing port");
            peerInfo.setPort(port.getData().intValue());
        }
        return peers;
    }

}
