package com.nio.demo.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public class ScatterGatherIO {
    public static void main(String[] args) {
        String data = "gua wa zi!";
        gatherBytes(data);
        scatterBytes();

    }

    public static void gatherBytes(String date) {
        String workUrl = System.getProperty("user.dir");
        ByteBuffer buffer1 = ByteBuffer.allocate(8);
        ByteBuffer buffer2 = ByteBuffer.allocate(400);
        buffer1.asIntBuffer().put(420);
        buffer2.asCharBuffer().put(date);
        GatheringByteChannel gather = createChannelInstance(workUrl + "/testout.txt", true);
        try {
            gather.write(new ByteBuffer[]{buffer1, buffer2});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void scatterBytes() {
        String workUrl = System.getProperty("user.dir");
        ByteBuffer buffer1 = ByteBuffer.allocate(8);
        ByteBuffer buffer2 = ByteBuffer.allocate(420);
        ScatteringByteChannel scatter = createChannelInstance(workUrl + "/testout.txt", false);
        try {
            scatter.read(new ByteBuffer[]{buffer1, buffer2});
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer1.rewind();
        buffer2.rewind();
        int bufferOne = buffer1.asIntBuffer().get();
        String bufferTwo = buffer2.asCharBuffer().toString();
        System.out.println(bufferOne);
        System.out.println(bufferTwo);
    }

    private static FileChannel createChannelInstance(String file, boolean isOutput) {
        FileChannel channel = null;
        try {
            if (isOutput) {
                channel = new FileOutputStream(file).getChannel();
            } else {
                channel = new FileInputStream(file).getChannel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
