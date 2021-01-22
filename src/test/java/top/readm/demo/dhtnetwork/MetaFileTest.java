package top.readm.demo.dhtnetwork;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;
import top.readm.demo.dhtnetwork.bencode.BencodeReader;
import top.readm.demo.dhtnetwork.bencode.BencodeType;
import top.readm.demo.dhtnetwork.bt.BTLoader;
import top.readm.demo.dhtnetwork.bt.model.MetaFile;

import javax.xml.crypto.dsig.DigestMethod;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class MetaFileTest {

    @Test
    public void testSample1() throws Exception{
        InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("sample.torrent");
        MetaFile metaFile = new BTLoader().load(in);
        Assert.assertEquals(2,metaFile.getAnnounceList().size());
    }

    @Test
    public void testSample1InfoHash() throws Exception{
        InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("sample.torrent");
        MetaFile metaFile = new BTLoader().load(in);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        metaFile.getInfo().encode(baos);
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        digest.update(baos.toByteArray());
        byte[] hash = digest.digest();
        Assert.assertEquals("fc8a15a2faf2734dbb1dc5f7afdc5c9beaeb1f59", Hex.encodeHexString(hash));
    }

    @Test
    public void testSample2() throws Exception{
        InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("sample2.torrent");
        MetaFile metaFile = new BTLoader().load(in);
        Assert.assertEquals(163,metaFile.getAnnounceList().size());
    }

}
