package com.nio.demo.nio;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class TransferDemo {
    public static void main(String[] args) throws IOException {
        String workUrl = System.getProperty("user.dir");
        String[] intxt = new String[]{workUrl + "/input1.txt", workUrl + "/input2.txt", workUrl + "/input3.txt", workUrl + "/input4.txt"};
        String outtxt = workUrl + "/out.txt";
        FileOutputStream ouput = new FileOutputStream(new File(outtxt));
        WritableByteChannel wbc = ouput.getChannel();
        for (int i = 0; i < intxt.length; i++) {
            FileInputStream input = new FileInputStream(intxt[i]);
            FileChannel channel = input.getChannel();
            channel.transferTo(0, channel.size(), wbc);
            channel.close();
            input.close();
        }
        wbc.close();
        ouput.close();
        System.out.println("ga ga ga");
    }
}
