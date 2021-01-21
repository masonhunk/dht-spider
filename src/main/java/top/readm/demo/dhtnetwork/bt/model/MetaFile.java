package top.readm.demo.dhtnetwork.bt.model;

import lombok.Builder;
import lombok.Data;

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

    private Info info;

    /**
     * Optional fields
     */
    private List<List<String>> announceList;

    private String comment;

    private LocalDateTime creationDate;

    private String createdBy;

    private String encoding;

}
