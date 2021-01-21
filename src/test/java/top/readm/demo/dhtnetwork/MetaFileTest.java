package top.readm.demo.dhtnetwork;

import org.junit.Assert;
import org.junit.Test;
import top.readm.demo.dhtnetwork.bencode.BencodeReader;
import top.readm.demo.dhtnetwork.bencode.BencodeType;
import top.readm.demo.dhtnetwork.bt.BTLoader;
import top.readm.demo.dhtnetwork.bt.model.MetaFile;

import java.io.InputStream;

public class MetaFileTest {

    @Test
    public void testSample1() throws Exception{
        InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("sample.torrent");
        MetaFile metaFile = new BTLoader().load(in);
        Assert.assertEquals(2,metaFile.getAnnounceList().size());
    }

    @Test
    public void testSample2() throws Exception{
        InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("sample2.torrent");
        MetaFile metaFile = new BTLoader().load(in);
        Assert.assertEquals(163,metaFile.getAnnounceList().size());
    }

}
