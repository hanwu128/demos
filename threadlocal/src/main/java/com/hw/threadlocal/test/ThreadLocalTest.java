package com.hw.threadlocal.test;

public class ThreadLocalTest {
    /*定义一个全局变量 来存放线程需要的变量*/
    //public static ThreadLocal<Integer> ti = new ThreadLocal<Integer>();

    public static void main(String[] args) {
        /*创建两个线程*/
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Double d = Math.random() * 10;
                    /*存入当前线程独有的值*/
                    //ti.set(d.intValue());
                    ThreadLocalDemo.getThreadInstance().setName("name"+d);
                    new A().get();
                    new B().get();
                }
            }).start();
        }
    }

    static class A {
        public void get() {
            /*取得当前线程所需要的值*/
            //System.out.println(ti.get());
            System.out.println(ThreadLocalDemo.getThreadInstance().getName());
        }
    }

    static class B {
        public void get() {
            /*取得当前线程所需要的值*/
            //System.out.println(ti.get());
            System.out.println(ThreadLocalDemo.getThreadInstance().getName());
        }
    }
}

