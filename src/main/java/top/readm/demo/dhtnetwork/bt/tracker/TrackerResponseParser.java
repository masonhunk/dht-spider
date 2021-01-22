package top.readm.demo.dhtnetwork.bt.tracker;

import top.readm.demo.dhtnetwork.bencode.BBytes;
import top.readm.demo.dhtnetwork.bencode.BMap;
import top.readm.demo.dhtnetwork.bencode.BencodeType;
import top.readm.demo.dhtnetwork.bt.peer.PeerInfo;
import top.readm.demo.dhtnetwork.bt.peer.PeerInfoParser;

import java.util.List;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/22
 */
public class TrackerResponseParser {

    public TrackerResponse parse(BMap rawResponse) {
        TrackerResponse response = new TrackerResponse();
        response.setFailureReason(getField(rawResponse, "failure_reason", false));
        if (!response.isSuccess()) {
            throw new RuntimeException(response.getFailureReason());
        }
        response.setInterval(Integer.parseInt(getField(rawResponse, "interval", true)));
        response.setComplete(Integer.parseInt(getField(rawResponse, "complete", true)));
        response.setIncomplete(Integer.parseInt(getField(rawResponse, "incomplete", true)));
        response.setTrackerId(getField(rawResponse, "tracker id", false));
        response.setPeers(parsePeers(rawResponse));
        return response;

    }

    private String getField(BMap rawResponse, String field, boolean required){
        BencodeType val = rawResponse.get(field);
        if(required && val == null){
            throw new RuntimeException("Response missing required field:" + field);
        }
        return val != null?val.toString():null;
    }

    private List<PeerInfo> parsePeers(BMap rawResponse){
        BencodeType peersRaw = rawResponse.get("peers");
        if(peersRaw == null) throw new RuntimeException("Response missing peers");
        /**
         * Binary mode https://wiki.theory.org/BitTorrentSpecification#request:_.3Clen.3D0013.3E.3Cid.3D6.3E.3Cindex.3E.3Cbegin.3E.3Clength.3E
         */
        if(peersRaw instanceof BBytes){
            BBytes bBytes = BBytes.class.cast(peersRaw);
            return PeerInfoParser.parseBinary(bBytes.getData());
        }

        /**
         * Dictionary mode
         */
        if(peersRaw instanceof BMap){
            BMap bMap = BMap.class.cast(peersRaw);
            return PeerInfoParser.parseDictionary(bMap);
        }

        throw new RuntimeException("Not supported");
    }



}
