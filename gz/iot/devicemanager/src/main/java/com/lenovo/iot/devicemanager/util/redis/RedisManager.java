package com.lenovo.iot.devicemanager.util.redis;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by root on 2017/3/25.
 * http://223.203.218.93/devicemanager/device/register.do?device_id=1234567890
 * {"device_id":"1234567890","broker":"tcp://223.203.218.93:1883","access_key":"1234567890","secret_key":"E2AF2A4413644985BFFD7DAB2940867A","keep_alive":90,"clean_session":false,"result":true}
 */
public class RedisManager {

    public static String readFixFileFromJarDescJSON(String jarFilewholeFolder,String relativeFilePath) {

        StringBuilder sb = new StringBuilder();
        try {
            ZipFile zipFile = new ZipFile(jarFilewholeFolder);
            Enumeration<? extends ZipEntry> e = zipFile.entries();

            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                // if the entry is not directory and matches relative file then extract it
                if (!entry.isDirectory() && entry.getName().equals(relativeFilePath)) {
                    BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

                    int bytesRead = 0;
                    byte[] buffer = new byte[1024];
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        String chunk = new String(buffer, 0, bytesRead);
                        sb.append(chunk);
                    }
                    bis.close();
                } else {
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }














}
