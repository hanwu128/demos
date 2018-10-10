package com.lenovo.iot.devicemanager.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by root on 2017/8/21.
 */
public class MDFIVE {


    public static String getFileMD5(File file){

        String md5Str = "";

        try {

            FileInputStream fis = new FileInputStream(file);

            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] buffer = new byte[1024];

            int length = -1;

            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            fis.close();
            
            BigInteger bigInt = new BigInteger(1, md.digest());

            md5Str = bigInt.toString(16);

            System.out.println("文件md5值：" + bigInt.toString(16));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  md5Str;
    }




}
