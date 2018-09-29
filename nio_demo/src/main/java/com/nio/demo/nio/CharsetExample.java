package com.nio.demo.nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class CharsetExample {
    public static void main(String[] args) {
        Charset cs = Charset.forName("UTF-8");
        System.out.println(cs.displayName());
        System.out.println(cs.canEncode());
        String st = "Welcome to yiibai.com, it is Charset test Example.瞎搞";
        ByteBuffer bytebuffer = ByteBuffer.wrap(st.getBytes());
        CharBuffer charbuffer = cs.decode(bytebuffer);
        ByteBuffer newbytebuffer = cs.encode(charbuffer);
        while (newbytebuffer.hasRemaining()) {
            char ca = (char) newbytebuffer.get();
            System.out.print(ca);
        }
        newbytebuffer.clear();
    }
}
