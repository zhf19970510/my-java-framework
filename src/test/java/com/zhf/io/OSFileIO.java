package com.zhf.io;

import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class OSFileIO {

    @Test
    public void whatByteBuffer() {
        // 可以理解为是一个字节数组，但是不是真正的字节数组，可以对其进行一些操作，比如放入字节，翻转，读取字节等等
        // 分配在堆上
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 分配在堆外
        // ByteBuffer.allocateDirect(1024);

        // 偏移指针
        System.out.println("position:" + buffer.position());
        // 大小限制
        System.out.println("limit:" + buffer.limit());
        // 总大小
        System.out.println("capacity:" + buffer.capacity());
        System.out.println("mark:" + buffer);

        buffer.put("123".getBytes());
        System.out.println("mark:" + buffer);

        // 读写交替，想读的时候flip，想写的时候compact
        buffer.flip();

        System.out.println("---------flip---------");
        System.out.println("mark:" + buffer);

        buffer.get();

        System.out.println("---------get---------");
        System.out.println("mark:" + buffer);

        // 进行压缩，读取过的数据会被移动到，未读取的压缩到最前面
        buffer.compact();

        System.out.println("---------compact---------");
        System.out.println("mark:" + buffer);

        buffer.clear();

        System.out.println("---------clear---------");
        System.out.println("mark:" + buffer);

    }

    // 基于文件的NIO测试:普通写、随机写、堆外映射写
    @Test
    public void testRandomAccessFileWrite() throws Exception{
        RandomAccessFile raf = new RandomAccessFile("D:\\test\\test.txt", "rw");

        // 普通写入
        raf.write("hello zhfjoifejofa\n".getBytes());
        raf.write("hello abcdefghi\n".getBytes());
        System.out.println("write-----------------");

        System.in.read();

        raf.seek(4);
        raf.write("ooxx".getBytes());

        System.out.println("seek-----------------");
        System.in.read();

        FileChannel channel = raf.getChannel();
        // 只有文件有map，socket没有map
        // mmap 对外内存 和 文件映射
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 4096);
        // 不是系统调用，但数据会到达内核的page cache，数据会写入磁盘，但数据不会立即写入磁盘，这里没有了系统调用，减少了用户态和内核态的切换，性能更高
        // mmap的内存映射，依然是内核的page cache体系所约束的。换言之，可能会丢数据
        map.put("@@@".getBytes());
        System.out.println("map--put-----------------");
        System.in.read();

        // 要么等待page cache的刷盘策略进行自动刷新，要么调用force方法进行手动刷新
        // map.force(); // flush

        raf.seek(0);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8192);

        int read = channel.read(byteBuffer);
        System.out.println("read:" + byteBuffer);
        byteBuffer.flip();

        System.out.println(byteBuffer);
        for (int i = 0; i < byteBuffer.limit(); i++) {
            Thread.sleep(200);
            System.out.print((char) byteBuffer.get());
        }
    }

    // 基于Socket的NIO测试
//    public void
}
