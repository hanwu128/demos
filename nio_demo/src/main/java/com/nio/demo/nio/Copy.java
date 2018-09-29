package com.nio.demo.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class Copy {

    private static void copyData(ReadableByteChannel rbc, WritableByteChannel wbc) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocateDirect(20 * 1024);
        while (rbc.read(buffer) != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                wbc.write(buffer);
            }
            buffer.clear();
        }
    }

    public static void main(String[] args)throws IOException {
        String textUrl = System.getProperty("user.dir");
        FileInputStream input = new FileInputStream(textUrl+"/testin.txt");
        ReadableByteChannel rbc = input.getChannel();
        FileOutputStream output = new FileOutputStream(textUrl+"/testout.txt");
        WritableByteChannel wbc = output.getChannel();
        copyData(rbc,wbc);
        input.close();
        output.close();
        System.out.println("copy end");
    }
}
