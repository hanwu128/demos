package com.lenovo.iot.devicemanager.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Desc: IO工具类
 * Name: com.lenovo.iot.devicemanager.util.IOUtil
 * Author: chench9@lenovo.com
 * Date: 2018/5/10 17:57
 **/
public class IOUtil {

    /**
     * 从输入流中读取数据
     * @param is
     * @return
     */
    public static byte[] read(InputStream is) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int count = -1;
        byte[] buffer = new byte[512];
        try {
            while((count = is.read(buffer)) > 0) {
                bos.write(buffer, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos.toByteArray();
    }

    private IOUtil() {
    }
}
