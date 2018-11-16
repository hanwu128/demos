package com.hw;


import java.util.concurrent.*;

public class CDownloader implements Callable<Boolean> {

    private String url;
    private String name;

    public CDownloader(String url, String name) {
        this.url = url;
        this.name = name;
    }

    @Override
    public Boolean call() throws Exception {
        WebDownloader wd = new WebDownloader();
        wd.download(url, name);
        return true;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CDownloader td1 = new CDownloader("", "");
        CDownloader td2 = new CDownloader("", "");
        CDownloader td3 = new CDownloader("", "");

        ExecutorService ser = Executors.newFixedThreadPool(3);
        Future<Boolean> res1 = ser.submit(td1);
        Future<Boolean> res2 = ser.submit(td2);
        Future<Boolean> res3 = ser.submit(td3);

        Boolean r1 = res1.get();
        Boolean r2 = res2.get();
        Boolean r3 = res3.get();

        ser.shutdown();

    }

}
