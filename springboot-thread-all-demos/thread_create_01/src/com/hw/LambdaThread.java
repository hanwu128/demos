package com.hw;

/**
 * Lambda表达式简化(用一次)的使用
 */
public class LambdaThread {

    //静态内部类
    static class Test implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                System.out.println("一边听歌......");
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new Test()).start();

        //局部内部类
        class Test2 implements Runnable {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    System.out.println("一边听歌......");
                }
            }
        }

        //匿名内部类
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    System.out.println("一边听歌......");
                }
            }
        }).start();

        //jdk8简化
        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                System.out.println("一边听歌......");
            }
        }).start();
    }
}
