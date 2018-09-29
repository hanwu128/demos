package com.hw.thread.demo;

public class Thread01Test {
    public static void main(String[] args) {
        Thread01 thread01 = new Thread01();

        Thread thread1 = new Thread(thread01);
        Thread thread2 = new Thread(thread01);
        Thread thread3 = new Thread(thread01);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
