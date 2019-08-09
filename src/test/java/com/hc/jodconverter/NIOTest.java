package com.hc.jodconverter;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOTest {
    public static void main(String[] args) throws  Exception {

        Long start = System.currentTimeMillis();

        FileInputStream fileIn = new FileInputStream("D:\\tmp\\111.pdf");
        ByteBuffer byteBuf = ByteBuffer.allocate(65535);
        FileChannel fileChannel = fileIn.getChannel();
        int bytes = -1;
        do {
            bytes = fileChannel.read(byteBuf);
            if (bytes != -1) {
                byte[] array = new byte[bytes];
                byteBuf.flip();
                byteBuf.get(array);
                byteBuf.clear();

            }
        } while (bytes > 0);
        byteBuf.clear();
        fileChannel.close();
        fileIn.close();
        Long end = System.currentTimeMillis();
        System.out.println(end-start);
    }
}
