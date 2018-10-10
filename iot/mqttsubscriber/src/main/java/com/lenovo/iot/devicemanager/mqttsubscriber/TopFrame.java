package com.lenovo.iot.devicemanager.mqttsubscriber;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 2017/11/15.
 * java -jar etcdartifactid-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/mydesktop/etcd.conf
 * tail -f nohup.out | perl -pe 's/(ThreadLetter)/\e[1;31m$1\e[0m/g'
 *
 */
public class TopFrame {

    public static  Object _ThreadLockFlag = new Object();

    public static Map<String,String> mapForRabbitMQ = new HashMap<String,String>();


    public static void main(String[] args){

        String path = args[0];


        //消费
        callOneThreadToLive__SentinelSubscripter();
        if(null == path){
            System.out.println("WARRING:null == path MAY BE THIS IS TEST ON WINDOWS");
            path = "F:\\lenovoworkspace\\etcdallinone\\etcdstand\\etcdBFG\\src\\main\\resources\\etcd.conf";
        }

        //生产
        Sentinel.callOneThreadToLiveETCDSentinel( null,
                                                    path,_ThreadLockFlag,mapForRabbitMQ);

    }



    public static String callOneThreadToLive__SentinelSubscripter(){

        (new Thread(new SentinelSubscripter(_ThreadLockFlag,mapForRabbitMQ))).start();

        System.out.println("--->>>>already callOneThreadToLive__SentinelSubscripter");

        return "success";
    }





    public  static  void demoThread(){

        Long threadId = 1L;

        Thread _FixedThread = findThread(threadId);

        _FixedThread.interrupt();



    }

    public static Thread findThread(long threadId) {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        while(group != null) {
            Thread[] threads = new Thread[(int)(group.activeCount() * 1.2)];
            int count = group.enumerate(threads, true);
            for(int i = 0; i < count; i++) {
                if(threadId == threads[i].getId()) {
                    return threads[i];
                }
            }
            group = group.getParent();
        }
        return null;
    }



}
