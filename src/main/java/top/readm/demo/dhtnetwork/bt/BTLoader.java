package top.readm.demo.dhtnetwork.bt;

import lombok.extern.slf4j.Slf4j;
import top.readm.demo.dhtnetwork.bencode.*;
import top.readm.demo.dhtnetwork.bt.model.Info;
import top.readm.demo.dhtnetwork.bt.model.MInfo;
import top.readm.demo.dhtnetwork.bt.model.MetaFile;
import top.readm.demo.dhtnetwork.bt.model.SInfo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class BTLoader {

    /**
     * 读取BT文件，生成MetaFile格式
     * @param in
     * @return
     */
    public MetaFile load(InputStream in){
        /**
         * 1. Read root element
         */
        BMap root = readBMap(in);
        /**
         * 2. Build meta file from RootElement
         */
        MetaFile metaFile = convertToMetafile(root);

        return metaFile;
    }

    private BMap readBMap(InputStream in){
        try(BufferedInputStream bis = new BufferedInputStream(in);){
            return (BMap) new BencodeReader().read(bis);
        }
        catch (IOException ex){
            log.error("Error deserializing bt meta file", ex);
            return null;
        }
    }

    private MetaFile convertToMetafile(BMap bMap){
        MetaFile metaFile
                = MetaFile.builder()
                .announce(bMap.get("announce").toString())
                .info(((BMap) bMap.get("info")))
                .announceList(resolveAnnounceList((BList) bMap.get("announce-list")))
                .comment(resolveProperty(bMap, "comment"))
                .createdBy(resolveProperty(bMap, "created by"))
                .creationDate(resolveDateTime(bMap))
                .encoding(resolveProperty(bMap, "encoding"))
                .build();
        return metaFile;
    }

    private Info convertInfo(BMap bMap){
        BencodeType filesField = bMap.get("files");
        //Single file
        if(filesField == null){
            SInfo sInfo = SInfo.builder()
                    .name(resolveProperty(bMap, "name"))
                    .length(Long.valueOf(bMap.get("length").toString()))
                    .pieceLength(Long.valueOf(bMap.get("piece length").toString()))
                    .pieces(resolvePieces(bMap))
                    .build();
            return sInfo;
        }
        //Multi file
        else{
            return new MInfo();
        }
    }

    private byte[] resolvePieces(BMap bMap) {
        BBytes bBytes = (BBytes)bMap.get("pieces");
        return bBytes.getData();
    }

    private List<List<String>> resolveAnnounceList(BList bList){
        List<BencodeType> rawList = bList.getData();
        List<List<String>> result = new ArrayList<>();
        for(BencodeType element: rawList){
            BList elementBlist = (BList)element;
            List<String> elementResult = new ArrayList<>();
            for(BencodeType item: elementBlist.getData()){
                elementResult.add(item.toString());
            }
            result.add(elementResult);
        }
        return result;
    }

    private String resolveProperty(BMap bMap, String key){
        BencodeType bencodeVal = bMap.get(key);
        return bencodeVal != null?bencodeVal.toString():null;
    }
    private LocalDateTime resolveDateTime(BMap bMap){
        BInt bitem = (BInt) bMap.get("creation date");
        if(bitem == null) return null;
        BigInteger timeval = bitem.getData();
        return new Date(timeval.longValue()*1000).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
