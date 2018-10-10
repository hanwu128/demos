package com.lenovo.iot.devicemanager.mqttsubscriber;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by root on 2017/11/15.
 */
public class SentinelSubscripter implements Runnable{

    public Object _ThreadLockFlag;

    public Map<String,String> mapForRabbitMQ = null;

    static {
        //CommonTools.InitLog4jConfig();
    }

    public SentinelSubscripter(Object _ThreadLockFlag, Map<String,String> mapForRabbitMQ){
        this._ThreadLockFlag = _ThreadLockFlag;
        this.mapForRabbitMQ = mapForRabbitMQ;
    }


    public void run() {

        synchronized(_ThreadLockFlag){

            String letterStr = "";

            while (true){

                String _ThreadLetter = mapForRabbitMQ.get("ThreadLetter");

                System.out.println("_ThreadLetter--->>>"+_ThreadLetter+":"+System.currentTimeMillis());

                mapForRabbitMQ.put("ThreadLetter",null);

                if(_ThreadLetter == null || _ThreadLetter.equals("")) {

                    try {
                        _ThreadLockFlag.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }else{

                    if(!letterStr.equals(_ThreadLetter)){

                        String currentThreadAlreadyRole = getCurrentThreadRole(letterStr);

                        letterStr=_ThreadLetter;

                        System.out.println("_ThreadLetter--->>>>>>>>>>"+_ThreadLetter);

                        mapForRabbitMQ.put("ThreadLetter",null);
                        JSONObject _JSONObject_ThreadLetter = (JSONObject) JSONObject.parse(_ThreadLetter);
                        String role = _JSONObject_ThreadLetter.getString("role");

                        //Subscripter _Subscripter = new Subscripter();
                        MqttSubscriberStandByETCD _MqttSubscriberStandByETCD = null;

                        //如果上次我已经是 master 则  我不需要 切换 成  其他 subscriber的 master
                        if(null != role && !currentThreadAlreadyRole.equals("master") && role.equals("master")){
                            String fixedCommandParameter = _JSONObject_ThreadLetter.getString("fixedCommandParameter");
                            String[] parameterStrArray = fixedCommandParameter.split("@@@@");
                            String deviceidFromETCDBFC = parameterStrArray[0];//parameterStrArray[1];
                            String access_key = parameterStrArray[1];
                            String secret_key = parameterStrArray[2];
                            String topicFromETCDBFC=parameterStrArray[3];

                            System.out.println("deviceidFromETCDBFC--->>>"+deviceidFromETCDBFC);
                            System.out.println("access_key--->>>"+access_key);
                            System.out.println("secret_key--->>>"+secret_key);
                            System.out.println("topicFromETCDBFC--->>>"+topicFromETCDBFC);

                            try {
                                _MqttSubscriberStandByETCD = new MqttSubscriberStandByETCD(topicFromETCDBFC,deviceidFromETCDBFC,access_key,secret_key);
                            } catch (Exception e) {
                                e.printStackTrace();
                                _MqttSubscriberStandByETCD.stopSubscriber();
                                _MqttSubscriberStandByETCD = null;
                                System.gc();
                            }
                        }

                        if(null != role && role.equals("standby")){
                            if(null != _MqttSubscriberStandByETCD){
                                System.out.println("null != _MqttSubscriberStandByETCD--->>>>"+"stopSubscriber");
                                _MqttSubscriberStandByETCD.stopSubscriber();
                                _MqttSubscriberStandByETCD = null;
                                System.gc();

                            }else{
                                System.out.println("null is _MqttSubscriberStandByETCD");
                                _MqttSubscriberStandByETCD = null;
                                System.gc();
                            }
                        }
                    }
                }

                //System.out.println("wait success----->>>>>.");

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }

    }
//    public String callOneThreadToLive__SentinelSubscripter(){
//
//        (new Thread(new SentinelSubscripter(_ThreadLockFlag,mapForRabbitMQ))).start();
//
//        System.out.println("--->>>>already callOneThreadToLive__SentinelSubscripter");
//
//        return "success";
//    }

//    public String callOneThreadToLiveETCDSentinel(){
//        return "true";
//    }

    public String getCurrentThreadRole(String letterStr_JSON ){

        if(null == letterStr_JSON || letterStr_JSON.trim().length()<=0){
            return "";
        }

        JSONObject _JSONObject_ThreadLetter = (JSONObject) JSONObject.parse(letterStr_JSON);

        String role = _JSONObject_ThreadLetter.getString("role");

        if(null == role || role.trim().length()<=0){
            role = "";
        }

        return role;
    }



}
