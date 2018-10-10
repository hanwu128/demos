package com.lenovo.iot.devicemanager.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by root on 2017/1/13.
 */
public class ReadProperties {



    public  String readProperties(String propertiesFilePath,String keyStr){


        if(null == keyStr || keyStr.length()<=0){

            return null;
        }

        if(null == propertiesFilePath || propertiesFilePath.trim().length()<=0){
            propertiesFilePath="/common.properties";
        }


        String value = "";

        InputStream _InputStream = this.getClass().getResourceAsStream(propertiesFilePath);
		if(_InputStream != null) {
	        try {
		        Properties _Properties =  new Properties();
	            _Properties.load(_InputStream);
	
	            value = _Properties.getProperty(keyStr);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	        	try {
					_InputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		}

        return value;
    }
}
