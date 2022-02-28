package org.netty.demo.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Auther: zdw
 * @Date: 2022/02/14/14:40
 * http://www.imooc.com/wiki/nettylesson/netty16.html
 */
public class ByteBufTest {


    public static void main(String[] args) {
        Charset utf8 = Charset.forName("UTF-8");
//        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action  ", CharsetUtil.UTF_8);
//        ByteBuf slice = buf.slice(0, 15);
//        System.out.println(slice.toString(utf8));
//        buf.setByte(0, (byte) 'J');
//        System.out.println(buf.toString(utf8));
//        System.out.println(slice.toString(utf8));


        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action  ", CharsetUtil.UTF_8);
        ByteBuf copy = buf.copy(0, 15);
        System.out.println(copy.toString(utf8));
        buf.setByte(0, (byte) 'J');
        System.out.println(buf.toString(utf8));
        System.out.println(copy.toString(utf8));


//        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8);
//        ByteBuf buf = byteBuf.readerIndex(0);//将 readerIndex 移动到指定的位置
//        buf.markReaderIndex();//标记当前的 readerIndex
//        while (buf.isReadable()){
//            System.out.print((char) buf.readByte());
//        }
//        buf.resetReaderIndex();//回退到之前标记的 readerIndex
//        while (buf.isReadable()){
//            System.out.print((char) buf.readByte());
//        }


//        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8);
//        int i=0;
//        while (byteBuf.writableBytes() >= 4){
//            i++;
//            byteBuf.writeByte(65);
//        }
//        while (byteBuf.isReadable()){
//            System.out.print((char) byteBuf.readByte());
//        }
//        System.out.println(i);
    }



    @Test
    public void byteBufOp() {
        Charset utf8 = Charset.forName("UTF-8");
        // 创建一个用于保存给定字符串的字节的ByteBuf
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);

        ByteBuf duplicate = buf.duplicate();

        ByteBuf sliced = buf.slice(0, 15);

        ByteBuf readSlice=buf.readSlice(10);
        System.out.println(readSlice.toString(utf8));
        // 将打印“Netty in Action”
        System.out.println(sliced.toString(utf8));
        // 更新索引0 处的字节
        buf.setByte(0, (byte) 'J');
        // 将会成功，因为数据是共享的，对其中一个所做的更改对另外一个也是可见的
        assert buf.getByte(0) == sliced.getByte(0);
    }

    @Test
    public void readAllDataTest() {
        ByteBuf buf = Unpooled.buffer(16); //get reference form somewhere
        //写数据到buffer
        for(int i=0; i<buf.capacity(); i++){
            buf.writeByte(i+1);
        }


        for(int i=0; i<buf.capacity()-1; i++){
            System.out.println(buf.readByte());
        }

        System.out.println("-----------------");
        buf.discardReadBytes();
        while (buf.isReadable()) {
            System.out.println(buf.readByte());
           // buf.discardReadBytes();
        }
    }








    @Test
    public void byteBufTest() {
        ByteBuf byteBuf = Unpooled.buffer(10);
        byteBuf.writeInt(0xabef0101);
        byteBuf.writeInt(1024);
        byteBuf.writeInt(4396);
       // byteBuf.writeByte((byte) 1);
       // byteBuf.writeByte((byte) 0);

        // 开始读取
        printDelimiter(byteBuf);
        printLength(byteBuf);

        // 派生一个ByteBuf，取剩下2个字节，但读索引不动
        ByteBuf duplicatBuf = byteBuf.duplicate();
        printByteBuf(duplicatBuf);

        // 派生一个ByteBuf，取剩下2个字节，读索引动了
        ByteBuf sliceBuf = byteBuf.readSlice(12);
        System.out.println("sliceBuf:"+sliceBuf.readInt());
        printByteBuf(byteBuf);

        // 两个派生的对象其实是一样的
       // assertEquals(duplicatBuf, sliceBuf);
    }

    private void printDelimiter(ByteBuf buf) {
        int newDelimiter = buf.readInt();
        System.out.printf("delimeter: %s\n", Integer.toHexString(newDelimiter));
        printByteBuf(buf);
    }

    private void printLength(ByteBuf buf) {
        int length = buf.readInt();
        System.out.printf("length: %d\n", length);
        printByteBuf(buf);
    }

    private void printByteBuf(ByteBuf buf) {
        System.out.printf("reader Index: %d, writer Index: %d, capacity: %d\n", buf.readerIndex(), buf.writerIndex(), buf.capacity());
    }


}
