package com.hw;

/**
 * 创建线程方式一：
 * 1、集成Thread、重写Run()
 */
public class StartThread extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println("一边听歌！");
        }
    }

    public static void main(String[] args) {
        //启动线程
        StartThread st = new StartThread();
        st.start();

        for (int i = 0; i < 20; i++) {
            System.out.println("一边上课！");
        }
    }
}
