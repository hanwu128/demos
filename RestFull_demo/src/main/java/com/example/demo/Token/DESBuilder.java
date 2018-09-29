package com.example.demo.Token;

import sun.misc.BASE64Encoder;

import javax.crypto.KeyGenerator;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.SecureRandom;

public class DESBuilder {
    /**
     * 加密算法
     */
    public static final String KEY_ALGORTHM = "DES";

    private Key key;

    public DESBuilder(String str) {
        generatorRandomKey(str);
    }

    public void generatorRandomKey(String strKey) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(KEY_ALGORTHM);
            generator.init(new SecureRandom(strKey.getBytes()));
            this.key = generator.generateKey();
            generator = null;
        } catch (Exception e) {
            throw new RuntimeException("ERROR");
        }
    }

    public String getKeyToString(){
        return (new BASE64Encoder().encodeBuffer(key.getEncoded()));
    }

    public void getKeyToFile(String keyAddress){
        FileOutputStream fileOutput = null;
        ObjectOutputStream objectOutput = null;
            try {
                fileOutput = new FileOutputStream(keyAddress);
                objectOutput = new ObjectOutputStream(fileOutput);
                objectOutput.writeObject(this.key);
                System.out.println("sucess"+keyAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    fileOutput.close();;
                    objectOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}
