package com.hw.blog.interceptor;

import com.hw.blog.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * 对request流进行重写
 */
public class BodyReaderWrapper extends HttpServletRequestWrapper {
    private static final Logger logger = LoggerFactory.getLogger(BodyReaderWrapper.class);

    private byte[] body;

    public BodyReaderWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = getBodyByte(request);
    }

    /**
     * 获取请求Body
     *
     * @param request
     * @return
     */
    private byte[] getBodyByte(final ServletRequest request) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            inputStream = cloneInputStream(request.getInputStream());
            byteArrayOutputStream = new ByteArrayOutputStream();
            int size = 0;
            byte[] buffer = new byte[1024];
            while ((size = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, size);
            }
            body = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            logger.error("fetch parameter failed:{}", e.getMessage());
            e.printStackTrace();
        } finally {
            IOUtil.closeInputStream(inputStream);
            IOUtil.closeOutputStream(byteArrayOutputStream);
        }
        return body;
    }

    /**
     * Description: 复制输入流
     *
     * @param inputStream
     * @return
     */
    private InputStream cloneInputStream(ServletInputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int size = 0;
        try {
            while ((size = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, size);
            }
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            logger.error("stream copy failed:{}", e.getMessage());
            e.printStackTrace();
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return byteArrayInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }
}
