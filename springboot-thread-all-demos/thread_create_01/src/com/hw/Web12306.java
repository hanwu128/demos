package com.hw;

public class Web12306 implements Runnable {

    private int ticketNums = 99;//票数

    @Override
    public void run() {
        while (true) {
            if (ticketNums < 0) {
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "-->" + ticketNums--);
        }
    }

    public static void main(String[] args) {
        //一份资源多个代理
        Web12306 web = new Web12306();
        new Thread(web, "代理1").start();
        new Thread(web, "代理2").start();
        new Thread(web, "代理3").start();
    }
}
