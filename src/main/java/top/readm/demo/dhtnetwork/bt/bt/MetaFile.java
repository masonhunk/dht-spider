package top.readm.demo.dhtnetwork.bt.bt;

import lombok.Builder;
import lombok.Data;
import top.readm.demo.dhtnetwork.bt.bencode.BDictionary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * BT下载种子的结构
 */
@Data
@Builder
public class MetaFile {

    /**
     * Required fields
     */

    private String announce;

    private BDictionary info;

    /**
     * Optional fields
     */
    private List<List<String>> announceList;

    private String comment;

    private LocalDateTime creationDate;

    private String createdBy;

    private String encoding;

    public byte[] getInfoSha1(){
        /**
         * 1. Read info bytes
         */
        byte[] bytes = null;
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();){
            info.encode(baos);
            bytes = baos.toByteArray();
        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }

        /**
         * 2. Sha1
         */
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            return digest.digest(bytes);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

    }

}
