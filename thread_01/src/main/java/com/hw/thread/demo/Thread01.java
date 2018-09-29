package com.hw.thread.demo;

/**
 * 排队叫号,假设最大排队数10人,三个窗口同时叫号
 */
public class Thread01 implements Runnable {

    private int maxPersonCount;//最大排队数
    private final Object obj = new Object();

    public void run() {

        while (true) {
            if (ticket()) {
                break;
            }
        }
    }

    private boolean ticket() {
        synchronized (obj) {
            if (maxPersonCount > 10) {
                return true;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "-" + maxPersonCount++);
        }
        return false;
    }

}
