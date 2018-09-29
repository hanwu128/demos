package com.nio.demo.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class TimeQuery {
    private static int port = 8125;
    private static Charset charset = Charset.forName("UTF-8");
    private static CharsetDecoder decoder = charset.newDecoder();
    private static ByteBuffer dbuf = ByteBuffer.allocateDirect(1024);

    private static void query(String host) throws IOException {
        InetSocketAddress isa = new InetSocketAddress(InetAddress.getByName(host), port);
        SocketChannel sc = null;

        try {

            sc = SocketChannel.open();
            sc.connect(isa);

            dbuf.clear();
            sc.read(dbuf);

            dbuf.flip();
            CharBuffer cb = decoder.decode(dbuf);
            System.out.print(isa + " : " + cb);

        } finally {
            if (sc != null)
                sc.close();
        }
    }

    public static void main(String[] args) throws UnknownHostException {

        String host = InetAddress.getLocalHost().getHostAddress();
        System.out.println("Connect to Host:" + host + ", port:" + port);
        try {
            query(host);
        } catch (IOException x) {
            System.err.println(host + ": " + x);
        }
    }
}
