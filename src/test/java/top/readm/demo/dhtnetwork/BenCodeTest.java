package top.readm.demo.dhtnetwork;

import org.apache.commons.codec.Charsets;
import org.junit.Assert;
import org.junit.Test;
import top.readm.demo.dhtnetwork.bencode.*;
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
        Map<BBytes, BencodeType> map = new HashMap<>();
        map.put(new BBytes("name".getBytes()),new BBytes("cyz".getBytes()));
        map.put(new BBytes("age".getBytes()),new BInt(66));
        BMap bMap = new BMap( map);
        System.out.println(bMap.getData());
        String s = "d4:name11:create chen3:agei23ee";
        bMap = new BMap();
        bMap.decode(new ByteArrayInputStream(s.getBytes()));
        System.out.println(bMap);
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
    public void testTorrent() throws Exception{
        //String torrent = "d8:announce34:http://tracker.ydy.com:86/announce10:createdby13:BitComet/0.5813:creationdatei1117953113e8:encoding3:GBK4:infod6:lengthi474499162e4:name51:05.262005.StarWars Episode IV A New Hope-Rv9.rmvb10:name.utf-851:05.26.2005.Star WasEpisode IV A New Hope-Rv9.rmvb12:piecelengthi262144e6:pieces36220:XXXXXXXXXXXXXXX";
        String torrent = "d8:announce34:http://tracker.ydy.com:86/announce9:createdby13:BitComet/0.5812:creationdatei1117953113e8:encoding3:GBK4:infod6:lengthi474499162e4:name49:05.262005.StarWars Episode IV A New Hope-Rv9.rmvb10:name.utf-849:05.26.2005.Star WasEpisode IV A New Hope-Rv9.rmvb11:piecelengthi262144eee";
        BencodeType t = new BencodeReader().read(new ByteArrayInputStream(torrent.getBytes()));
        System.out.println(t);
    }


    @Test
    public void testTorrent2() throws Exception{
        InputStream ins = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("sample.torrent");
            BencodeType t = new BencodeReader().read(ins);
        System.out.println(t);
    }

    @Test
    public void te() throws Exception{
        InputStream in = new ByteArrayInputStream("d8:announce39:http://torrent.ubuntu.com:6969/announce13:announce-listll39:http://torrent.ubuntu.com:6969/announceel44:http://ipv6.torrent.ubuntu.com:6969/announceee7:comment29:Ubuntu CD releases.ubuntu.com13:creation datei1429786237e4:infod6:lengthi1150844928e4:name30:ubuntu-15.04-desktop-amd64.iso12:piece lengthi524288e6:pieces43920".getBytes());
        BencodeType t = new BencodeReader().read(in);
        System.out.println(t);
    }



}
