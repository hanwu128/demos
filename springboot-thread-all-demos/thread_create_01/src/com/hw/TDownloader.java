package com.hw;


public class TDownloader extends Thread {

    private String url;
    private String name;

    public TDownloader(String url, String name) {
        this.url = url;
        this.name = name;
    }

    @Override
    public void run() {
        WebDownloader wd = new WebDownloader();
        wd.download(url, name);
    }

    public static void main(String[] args) {
        TDownloader td1 = new TDownloader("","");
        TDownloader td2 = new TDownloader("","");
        TDownloader td3 = new TDownloader("","");

        td1.start();
        td2.start();
        td3.start();

    }

}
