package com.nio.demo.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BufferReader {

    public static void main(String[] args) {
        Path file = null;
        BufferedReader bufferReader = null;
        String testUrl = System.getProperty("user.dir");
        try {
            file = Paths.get(testUrl+"/testout.txt");
            InputStream inputStream = Files.newInputStream(file);
            bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            System.out.println("====="+bufferReader.readLine());
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                bufferReader.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
