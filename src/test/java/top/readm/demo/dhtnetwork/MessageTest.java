package top.readm.demo.dhtnetwork;

import org.junit.Assert;
import org.junit.Test;
import top.readm.demo.dhtnetwork.bt.nio.MessageDecoderFactory;
import top.readm.demo.dhtnetwork.bt.nio.codec.ProtocalMessageDecoder;
import top.readm.demo.dhtnetwork.bt.nio.codec.MessageDecoder;
import top.readm.demo.dhtnetwork.bt.protocal.BitfieldMessage;
import top.readm.demo.dhtnetwork.bt.handshake.HandshakeMessage;
import top.readm.demo.dhtnetwork.bt.protocal.KeepAliveMessage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

public class MessageTest {

    @Test
    public void testHandshake() throws Exception{
        HandshakeMessage message = new HandshakeMessage();
        //Decoder
        MessageDecoderFactory factory = new MessageDecoderFactory();
        MessageDecoder decoder = factory.createDecoder();
        //Prepare one message
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(message.getPtrLen());
        dos.write(message.getPtrstr().getBytes());
        dos.write(new byte[8]);
        dos.write(new byte[39]);
        dos.writeByte(1);
        ByteBuffer bb = ByteBuffer.wrap(baos.toByteArray());

        //Decode
        Object obj;
        while ((obj = decoder.decode(bb))!= null)
        {
            System.out.println(obj.getClass());
        }
    }

    @Test
    public void testBasic() throws Exception{
        //Decoder
        ProtocalMessageDecoder decoder = new ProtocalMessageDecoder();
        //Prepare one message
        Assert.assertTrue(decoder.isReadingHeader());
    }

    @Test
    public void testDecodeKeepAlive1() throws Exception{
        //Decoder
        MessageDecoderFactory factory = new MessageDecoderFactory();
        MessageDecoder decoder = factory.createDecoder();
        //Prepare one message
        KeepAliveMessage kam = new KeepAliveMessage();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(0);
        ByteBuffer bb = ByteBuffer.wrap(baos.toByteArray());

        //Decode
        Object obj = decoder.decode(bb);

        Assert.assertEquals(KeepAliveMessage.class, obj.getClass());
    }

    @Test
    public void testDecodeKeepAlive2() throws Exception{
        //Decoder
        MessageDecoderFactory factory = new MessageDecoderFactory();
        MessageDecoder decoder = factory.createDecoder();
        //Prepare one message
        KeepAliveMessage kam = new KeepAliveMessage();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(0);
        dos.writeInt(0);
        ByteBuffer bb = ByteBuffer.wrap(baos.toByteArray());

        //Decode
        Object obj;
        while ((obj = decoder.decode(bb))!= null)
        {
            Assert.assertEquals(KeepAliveMessage.class, obj.getClass());
        }
    }

    @Test
    public void testNormalMessage() throws Exception{
        //Decoder
        ProtocalMessageDecoder decoder = new ProtocalMessageDecoder();
        //Prepare one message
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(5);
        dos.write(5);
        dos.writeInt(4444);
        ByteBuffer bb = ByteBuffer.wrap(baos.toByteArray());

        //Decode
        Object obj = decoder.decode(bb);
        Assert.assertEquals(BitfieldMessage.class, obj.getClass());
        Assert.assertTrue(decoder.isReadingHeader());
        Assert.assertEquals(0, decoder.getHeaderBytesRead());
    }

    @Test
    public void testNormalMessage2() throws Exception{
        //Decoder
        ProtocalMessageDecoder decoder =new ProtocalMessageDecoder();
        //Prepare one message
        KeepAliveMessage kam = new KeepAliveMessage();
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(5);
        dos.write(5);
        dos.writeInt(4444);
        dos.writeInt(6);
        dos.write(5);
        dos.write(new byte[5]);
        ByteBuffer bb = ByteBuffer.wrap(baos.toByteArray());

        //Decode
        Object obj = decoder.decode(bb);
        Assert.assertEquals(BitfieldMessage.class, obj.getClass());
        obj = decoder.decode(bb);
        Assert.assertEquals(BitfieldMessage.class, obj.getClass());
        Assert.assertTrue(decoder.isReadingHeader());
        Assert.assertEquals(0, decoder.getHeaderBytesRead());
    }

    @Test
    public void testHeaderBreak() throws Exception{
        //Decoder
        ProtocalMessageDecoder decoder =new ProtocalMessageDecoder();
        //Prepare header-breaked message
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeByte(0);
        ByteBuffer bb = ByteBuffer.wrap(baos.toByteArray());

        //Decode
        Object obj = decoder.decode(bb);
        Assert.assertEquals(null, obj);
        Assert.assertTrue(decoder.isReadingHeader());
        Assert.assertEquals(1, decoder.getHeaderBytesRead());

        //Prepare header bytes 2 but still header break
        baos= new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
        dos.writeByte(0);
        bb = ByteBuffer.wrap(baos.toByteArray());

        //Decode
        obj = decoder.decode(bb);
        Assert.assertEquals(null, obj);
        Assert.assertTrue(decoder.isReadingHeader());
        Assert.assertEquals(2, decoder.getHeaderBytesRead());

        //Prepare remaining data
        baos= new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
        dos.writeShort(5);
        dos.writeByte(5);
        dos.writeInt(6666);
        bb = ByteBuffer.wrap(baos.toByteArray());

        //Decode
        obj = decoder.decode(bb);
        Assert.assertEquals(BitfieldMessage.class, obj.getClass());
        Assert.assertTrue(decoder.isReadingHeader());
        Assert.assertEquals(0, decoder.getHeaderBytesRead());
    }

    @Test
    public void testBodyBreak() throws Exception{
        //Decoder
        ProtocalMessageDecoder decoder =new ProtocalMessageDecoder();
        //Prepare header-breaked message
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(5);
        dos.writeByte(5);
        ByteBuffer bb = ByteBuffer.wrap(baos.toByteArray());

        //Decode
        Object obj = decoder.decode(bb);
        Assert.assertEquals(null, obj);
        Assert.assertFalse(decoder.isReadingHeader());
        Assert.assertEquals(1, decoder.getBodyBytesRead());

        //Prepare header bytes 2 but still header break
        baos= new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
        dos.writeInt(666);
        bb = ByteBuffer.wrap(baos.toByteArray());

        //Decode
        obj = decoder.decode(bb);
        Assert.assertEquals(BitfieldMessage.class, obj.getClass());
        Assert.assertTrue(decoder.isReadingHeader());
        Assert.assertEquals(0, decoder.getHeaderBytesRead());
    }
}
