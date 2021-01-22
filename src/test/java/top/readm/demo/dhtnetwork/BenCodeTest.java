package top.readm.demo.dhtnetwork;

import org.apache.commons.codec.Charsets;
import org.junit.Assert;
import org.junit.Test;
import top.readm.demo.dhtnetwork.bt.bencode.*;
import top.readm.demo.dhtnetwork.dht.Utils.BencodeUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BenCodeTest {

    @Test
    public void BintTest() throws Exception{
        //Decode
        BInt bint = new BInt();
        String s = "i141414e";
        ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());

        bint.decode(bais);
        Assert.assertEquals(141414, bint.getData().intValue());
        //Encode
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bint.encode(baos);
        System.out.println(new String(baos.toByteArray()));
    }

    @Test
    public void BBytesTest() throws Exception{
        BBytes bs = new BBytes("announce".getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bs.encode(baos);
        Assert.assertEquals("8:announce", new String(baos.toByteArray()));

        String str = "8:announce";
        BBytes bs2 = new BBytes();
        bs2.decode(new ByteArrayInputStream(str.getBytes()));
        System.out.println(bs2.getData().length);
    }

    @Test
    public void BBytesTest2() throws Exception{
        BBytes bs = new BBytes("你好".getBytes(Charsets.UTF_8));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bs.encode(baos);
        Assert.assertEquals("6:你好", new String(baos.toByteArray(), Charsets.UTF_8));

        String str = "8:announce";
        BBytes bs2 = new BBytes();
        bs2.decode(new ByteArrayInputStream(str.getBytes()));
        System.out.println(bs2.getData().length);
    }



    @Test
    public void BListTest() throws Exception{
        String s = "l4:spam4:eggsi9ee";
        BList bList = new BList();
        bList.decode(new ByteArrayInputStream(s.getBytes()));

        System.out.println(bList.getData());
    }


    @Test
    public void BMapTest() throws Exception{
        Map<String, BencodeType> map = new HashMap<>();
        map.put("name",new BBytes("cyz".getBytes()));
        map.put("age",new BInt(66));
        BDictionary bDictionary = new BDictionary( map);
        System.out.println(bDictionary.getData());
        String s = "d4:name11:create chen3:agei23ee";
        bDictionary = new BDictionary();
        bDictionary.decode(new ByteArrayInputStream(s.getBytes()));
        System.out.println(bDictionary);
    }

    @Test
    public void testBencode(){
        String content = "d7:balancei1000e4:coin3:btc4:name5:jisene";
        Object obj = BencodeUtils.decode(content, null);
        System.out.println(obj);

        content = "i3e";
        obj = BencodeUtils.decode(content, null);
        System.out.println(obj);
    }


    @Test
    public void testBencodeReader() throws Exception{
        BencodeType t = new BencodeReader().read(new ByteArrayInputStream("d4:name11:create chen3:agei23ee".getBytes()));
        System.out.println(t);
    }


    @Test
    public void testSample() throws Exception{
        InputStream ins = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("sample.torrent");
            BencodeType t = new BencodeReader().read(ins);
        System.out.println(t);
    }

    @Test
    public void testSample2() throws Exception{
        InputStream ins = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("sample2.torrent");
        BencodeType t = new BencodeReader().read(ins);
        System.out.println(t);
    }

    @Test
    public void testNest() throws Exception{
     //   InputStream in = new ByteArrayInputStream("d8:announce39:http://torrent.ubuntu.com:6969/announce13:announce-listll39:http://torrent.ubuntu.com:6969/announceel44:http://ipv6.torrent.ubuntu.com:6969/announceee7:comment29:Ubuntu CD releases.ubuntu.com13:creation datei1429786237e4:infod6:lengthi1150844928e4:name30:ubuntu-15.04-desktop-amd64.iso12:piece lengthi524288e6:pieces43920".getBytes());
        InputStream in = new ByteArrayInputStream("d4:key1li1ee4:key2i2ee".getBytes());

        BencodeType t = new BencodeReader().read(in);
        System.out.println(t);
    }

    @Test
    public void testNest2() throws Exception{
        InputStream in = new ByteArrayInputStream("d8:announce39:http://torrent.ubuntu.com:6969/announce13:announce-listll39:http://torrent.ubuntu.com:6969/announceel44:http://ipv6.torrent.ubuntu.com:6969/announceee7:comment29:Ubuntu CD releases.ubuntu.com13:creation datei1429786237e4:infod6:lengthi1150844928e4:name30:ubuntu-15.04-desktop-amd64.iso12:piece lengthi524288eee".getBytes());

        BencodeType t = new BencodeReader().read(in);
        System.out.println(t);
    }



}
