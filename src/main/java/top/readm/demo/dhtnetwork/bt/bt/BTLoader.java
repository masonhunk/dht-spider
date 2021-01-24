package top.readm.demo.dhtnetwork.bt.bt;

import lombok.extern.slf4j.Slf4j;
import top.readm.demo.dhtnetwork.bt.bencode.*;
import top.readm.demo.dhtnetwork.bt.bt.Info;
import top.readm.demo.dhtnetwork.bt.bt.MInfo;
import top.readm.demo.dhtnetwork.bt.bt.MetaFile;
import top.readm.demo.dhtnetwork.bt.bt.SInfo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
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
        BDictionary root = readBMap(in);
        /**
         * 2. Build meta file from RootElement
         */
        MetaFile metaFile = convertToMetafile(root);

        return metaFile;
    }

    private BDictionary readBMap(InputStream in){
        try(BufferedInputStream bis = new BufferedInputStream(in);){
            return (BDictionary) new BencodeReader().read(bis);
        }
        catch (IOException ex){
            log.error("Error deserializing bt meta file", ex);
            return null;
        }
    }

    private MetaFile convertToMetafile(BDictionary bDictionary){
        MetaFile metaFile
                = MetaFile.builder()
                .announce(getField(bDictionary,"announce", false))
                .info(((BDictionary) bDictionary.get("info")))
                .announceList(resolveAnnounceList((BList) bDictionary.get("announce-list")))
                .comment(getField(bDictionary,"comment", false))
                .createdBy(getField(bDictionary,"created by", false))
                .creationDate(resolveDateTime(bDictionary))
                .encoding(getField(bDictionary,"announce", false))
                .build();
        return metaFile;
    }

    private String getField(BDictionary rawResponse, String field, boolean required){
        BencodeType val = rawResponse.get(field);
        if(required && val == null){
            throw new RuntimeException("Response missing required field:" + field);
        }
        return val != null?val.toString():null;
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

    private LocalDateTime resolveDateTime(BDictionary bDictionary){
        BInt bitem = (BInt) bDictionary.get("creation date");
        if(bitem == null) return null;
        BigInteger timeval = bitem.getData();
        return new Date(timeval.longValue()*1000).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
