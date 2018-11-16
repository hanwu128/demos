package com.hw;

/**
 * 创建线程方式二：
 * 1、实现Runable、重写Run()
 */
public class StartRun implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println("一边听歌！");
        }
    }

    public static void main(String[] args) {
        //启动线程
        StartRun st = new StartRun();
        Thread t = new Thread(st);
        t.start();

        for (int i = 0; i < 20; i++) {
            System.out.println("一边上课！");
        }
    }
}
