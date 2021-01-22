package top.readm.demo.dhtnetwork.bt;

import lombok.extern.slf4j.Slf4j;
import top.readm.demo.dhtnetwork.bt.bencode.*;
import top.readm.demo.dhtnetwork.bt.model.Info;
import top.readm.demo.dhtnetwork.bt.model.MInfo;
import top.readm.demo.dhtnetwork.bt.model.MetaFile;
import top.readm.demo.dhtnetwork.bt.model.SInfo;

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
                .announce(bDictionary.get("announce").toString())
                .info(((BDictionary) bDictionary.get("info")))
                .announceList(resolveAnnounceList((BList) bDictionary.get("announce-list")))
                .comment(resolveProperty(bDictionary, "comment"))
                .createdBy(resolveProperty(bDictionary, "created by"))
                .creationDate(resolveDateTime(bDictionary))
                .encoding(resolveProperty(bDictionary, "encoding"))
                .build();
        return metaFile;
    }

    private Info convertInfo(BDictionary bDictionary){
        BencodeType filesField = bDictionary.get("files");
        //Single file
        if(filesField == null){
            SInfo sInfo = SInfo.builder()
                    .name(resolveProperty(bDictionary, "name"))
                    .length(Long.valueOf(bDictionary.get("length").toString()))
                    .pieceLength(Long.valueOf(bDictionary.get("piece length").toString()))
                    .pieces(resolvePieces(bDictionary))
                    .build();
            return sInfo;
        }
        //Multi file
        else{
            return new MInfo();
        }
    }

    private byte[] resolvePieces(BDictionary bDictionary) {
        BBytes bBytes = (BBytes) bDictionary.get("pieces");
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

    private String resolveProperty(BDictionary bDictionary, String key){
        BencodeType bencodeVal = bDictionary.get(key);
        return bencodeVal != null?bencodeVal.toString():null;
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
