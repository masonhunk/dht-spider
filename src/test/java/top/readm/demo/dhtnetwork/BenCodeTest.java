package top.readm.demo.dhtnetwork;

import org.apache.commons.codec.Charsets;
import org.junit.Assert;
import org.junit.Test;
import top.readm.demo.dhtnetwork.bencode.BInt;
import top.readm.demo.dhtnetwork.bencode.BList;
import top.readm.demo.dhtnetwork.bencode.BString;
import top.readm.demo.dhtnetwork.bencode.BencodeReader;
import top.readm.demo.dhtnetwork.dht.Utils.BencodeUtils;

import java.io.*;

public class BenCodeTest {

    @Test
    public void BintTest() throws Exception{
        BInt bint = new BInt();
        String s = "i141414e";
        StringReader r = new StringReader(s);
        bint.decode(r);

        Assert.assertEquals(141414, bint.getData().intValue());

    }

    @Test
    public void BStringTest() throws Exception{
        BString bs = new BString("announce");
        StringWriter writer = new StringWriter();
        bs.encode(writer);
        Assert.assertEquals("8:announce", writer.toString());

        String str = "8:announce";
        BString bs2 = new BString();
        bs2.decode(new StringReader(str));
        System.out.println(bs2.getData());
    }

    @Test
    public void BListTest() throws Exception{
        String s = "l4:spam4:eggsi9ee";
        BencodeReader br = new BencodeReader();
        BList bList = new BList(br);
        bList.decode(new PushbackReader(new StringReader(s)));

        System.out.println(bList.getData());
    }

    @Test
    public void testBencode(){
        String content = "d7:balancei1000e4:coin3:btc4:name5:jisene";
        Object obj = BencodeUtils.decode(content, null);
        System.out.println(obj);

        content = "i3e8:announce";
        obj = BencodeUtils.decode(content, null);
        System.out.println(obj);
    }

    @Test
    public void testBencode2() throws Exception{
        String bt = read();
        Object obj = BencodeUtils.decode(bt, null);
        System.out.println(obj);
    }

    private String read() throws Exception{
        FileInputStream ins = new FileInputStream("C:\\Users\\unnamed\\Desktop\\sample.torrent");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buf = new byte[2048];
        int n = 0;
        while (true){
            n = ins.read(buf);
            if(n < 0) break;
            baos.write(buf, 0, n);
        }
        byte[] b = baos.toByteArray();
        return new String(b, Charsets.UTF_8);
    }

}
