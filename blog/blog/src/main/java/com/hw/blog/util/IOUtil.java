package com.hw.blog.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO工具类
 */
public class IOUtil {

    /**
     * 关闭输入流InputStream
     *
     * @param is
     */
    public static void closeInputStream(InputStream is) {
        if (is == null) {
            return;
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输出流OutputStream
     *
     * @param outputStream
     */
    public static void closeOutputStream(OutputStream outputStream) {
        if (outputStream == null) {
            return;
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭流Closeable
     *
     * @param closeable
     */
    public static void closeCloseable(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IOUtil() {
    }
}
