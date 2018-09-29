package com.nio.demo.nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class SelectorExample {

    @Test
    public void selectorServer() throws IOException {
        //打开一个监听器
        Selector selector = Selector.open();
        System.out.println("===" + selector.isOpen());
        //获取服务端通道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8080);
        ssc.bind(hostAddress);
        //设置为非阻塞模式
        ssc.configureBlocking(false);
        int ops = ssc.validOps();
        SelectionKey selectionKey = ssc.register(selector, ops, null);
        for (; ; ) {
            System.out.println("......");
            int noOfKey = selector.select();
            System.out.println("===" + noOfKey);
            Set selectKeys = selector.selectedKeys();
            Iterator itr = selectKeys.iterator();
            while (itr.hasNext()) {
                SelectionKey key = (SelectionKey) itr.next();
                if (key.isAcceptable()) {
                    SocketChannel client = ssc.accept();
                    client.configureBlocking(false);
                    //向监听器注册接收事件
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("===========" + client);
                } else if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    client.read(buffer);
                    String output = new String(buffer.array()).trim();
                    System.out.println("Message read from client: " + output);
                    if (output.equals("Bye Bye")) {
                        client.close();
                        System.out.println("The Client messages are complete; close the session.");
                    }
                }
                itr.remove();
            }
        }
    }

    @Test
    public void selectorServer2() throws IOException {
        //1. 获取服务端通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.bind(new InetSocketAddress(8080));
        //2. 设置为非阻塞模式
        ssChannel.configureBlocking(false);

        //3. 打开一个监听器
        Selector selector = Selector.open();
        //4. 向监听器注册接收事件
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0) {
            //5. 获取监听器上所有的监听事件值
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            //6. 如果有值
            while (it.hasNext()) {
                //7. 取到SelectionKey
                SelectionKey key = it.next();

                //8. 根据key值判断对应的事件
                if (key.isAcceptable()) {
                    //9. 接入处理
                    SocketChannel socketChannel = ssChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    //10. 可读事件处理
                    SocketChannel channel = (SocketChannel) key.channel();
                    readMsg(channel);
                }
                //11. 移除当前key
                it.remove();
            }
        }
    }

    private void readMsg(SocketChannel channel) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        int len = 0;
        while ((len = channel.read(buf)) > 0) {
            buf.flip();
            byte[] bytes = new byte[1024];
            buf.get(bytes, 0, len);
            System.out.println(new String(bytes, 0, len));
        }
    }

    @Test
    public void selectorClient() throws IOException {
        //1. 获取socketChannel
        SocketChannel sChannel = SocketChannel.open();
        //2. 创建连接
        sChannel.connect(new InetSocketAddress("127.0.0.1", 8080));
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //3. 设置通道为非阻塞
        sChannel.configureBlocking(false);

        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.nextLine();

            buf.put((new Date() + "：" + msg).getBytes());
            buf.flip();
            //4. 向通道写数据
            sChannel.write(buf);
            buf.clear();
        }
    }
}
