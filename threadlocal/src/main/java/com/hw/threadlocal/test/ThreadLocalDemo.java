package com.hw.threadlocal.test;

public class ThreadLocalDemo {

    private static ThreadLocal<ThreadLocalDemo> t = new ThreadLocal<ThreadLocalDemo>();

    private ThreadLocalDemo() {
    }

    public static ThreadLocalDemo getThreadInstance() {
        ThreadLocalDemo threadLocalDemo = t.get();
        if (threadLocalDemo == null) {
            threadLocalDemo = new ThreadLocalDemo();
            t.set(threadLocalDemo);
        }
        return threadLocalDemo;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
