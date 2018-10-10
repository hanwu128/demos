package com.lenovo.iot.devicemanager.mqtt;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lenovo.iot.devicemanager.dao.DeviceDao;
import com.lenovo.iot.devicemanager.model.Device;
import com.lenovo.iot.devicemanager.model.Filter;

@Service
public class EmqttInstance {
	
	@Autowired
	private DeviceDao devicedao;

	private class EmqttClient implements MqttCallback {
		private MqttClient client;

		public EmqttClient(String brokerUrl, String clientId, String userName, String password, int keepAlive, boolean cleanSession) {
			//this.m_callback = callback;
			
			String tmpDir = System.getProperty("java.io.tmpdir");
			MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

			try {
				// Construct the connection options object that contains connection parameters such as cleanSession and LWT
				MqttConnectOptions conOpt = new MqttConnectOptions();
				conOpt.setAutomaticReconnect(true); //自动重连没有回调，若要回调需要使用  MqttAsyncClient
				conOpt.setCleanSession(cleanSession);
				if (password != null) {
					conOpt.setPassword(password.toCharArray());
				}
				if (userName != null) {
					conOpt.setUserName(userName);
				}
				conOpt.setKeepAliveInterval(keepAlive);

				// Construct an MQTT blocking mode client
				client = new MqttClient(brokerUrl, clientId, dataStore);

				// Set this wrapper as the callback handler
				client.setCallback(this);
				
				try {
					InputStream is = this.getClass().getResourceAsStream("/mqttserver.crt");
	                conOpt.setSocketFactory(SSL.getSSLSocktet(is));
	            } catch (Exception e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
				
				client.connect(conOpt);
				//client.subscribe(topic, qos); //MOC消息
				
				log("Mqtt server connected!");
			} catch (MqttException e) {
				e.printStackTrace();
				log("Unable to set up client: " + e.toString());
				//System.exit(1);
			}
		}
		
		public void publish(String topicName, String message, int qos) throws MqttException {
			MqttMessage msg = new MqttMessage();
            msg.setPayload(message.getBytes());
            msg.setQos(qos);
			
			client.publish(topicName, msg);
		}
		
//		public void close() {
//			// Disconnect the client from the server
//			try {
//				client.disconnect();
//			} catch (MqttException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

		private void log(String message) {
			System.out.println(message);
		}

		/****************************************************************/
		/* Methods to implement the MqttCallback interface */
		/****************************************************************/
		public void connectionLost(Throwable cause) {
			// Called when the connection to the server has been lost.
			// An application may choose to implement reconnection logic at this point. Here simply exits.
			log("Connection to " + client.getServerURI() + " lost!" + cause);
		}

		public void deliveryComplete(IMqttDeliveryToken token) {
		}

		public void messageArrived(String topic, MqttMessage message) throws MqttException {
		}
		/****************************************************************/
		/* End of MqttCallback methods */
		/****************************************************************/
	}
	
	
	
	private Logger log = Logger.getLogger("fordebug");
	private EmqttClient client;
	private String brokerUrl;
	private String clientId;
	private String userName;
	private String password;
	private int keepAlive = 60;
	private boolean cleanSession = true;
	private String topic_group;
	
	public EmqttInstance() {
		// Read settings from configuration file
		try {
	        InputStream is = this.getClass().getResourceAsStream("/mqtt.conf");
			   
			Properties dbProps = new Properties();
			dbProps.load(is);
			brokerUrl = dbProps.getProperty("broker");
			clientId = dbProps.getProperty("client_id");
			//topic = dbProps.getProperty("topic");
			userName = dbProps.getProperty("username");
			password = dbProps.getProperty("password");
			//String qos_string = dbProps.getProperty("qos");
			String keepAlive_string = dbProps.getProperty("keep_alive");
			String clean_session_String = dbProps.getProperty("clean_session");
			
			//if(!qos_string.isEmpty()) {
			//	qos = Integer.parseInt(qos_string);
			//}
			if(!keepAlive_string.isEmpty()) {
				keepAlive = Integer.parseInt(keepAlive_string);
			}
			if(!clean_session_String.isEmpty()) {
				cleanSession = Boolean.parseBoolean(clean_session_String);
			}
			
			topic_group = dbProps.getProperty("topic_group");
			
			is.close();
			
			client = new EmqttClient(brokerUrl, clientId, userName, password, keepAlive, cleanSession);
		} catch (Exception e) {
			System.err.println("failed to read config file.");
			e.printStackTrace();
			//System.exit(1);
		}
	}

//    public void onApplicationEvent(ApplicationEvent event) {
//        if (event instanceof ContextRefreshedEvent) {
//            //ApplicationContext applicationContext = ((ContextRefreshedEvent)event).getApplicationContext();
//            //TestClass test =  (TestClass)applicationContext.getBean("testClass");
//            //test.Do();
//        	
//        	try {
//				client = new EmqttClient();
//			} catch (MqttException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        }
//    }
    
    public String getBrokerUrl() {
    	return this.brokerUrl;
    	//return "tcp://223.203.218.93:1883";
    }
    
    public String getDeviceTopicGroup() {
    	return this.topic_group;
    	//return "tcp://223.203.218.93:1883";
    }
    
    public boolean pulishMessage(com.lenovo.iot.devicemanager.model.MqttMessage msgObj) {
    	boolean result = false;
    	
    	try {
    		Device device = devicedao.getDevice(msgObj.getDevice_id());
    		if(device.getOnline() == 1) {
				client.publish(msgObj.getTopic(), msgObj.getMessage(), msgObj.getQos());
				
				//历史记录
				if(msgObj.getMessage() != null && !msgObj.getMessage().isEmpty()) {
					msgObj.setPayload(msgObj.getMessage().getBytes("UTF-8").length);
				}
				msgObj.setMessage_stamp(new Timestamp(System.currentTimeMillis()));
				msgObj.setReceived(true);
				msgObj.setIsvalid(true);
				devicedao.addMqttMessage(msgObj);
	
				result = true;
    		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug(e.getMessage());
		}
    	
    	return result;
    }
    
    public static String buildMap(String appname, String mapItems) {
    	mapItems = "\"" + StringEscapeUtils.escapeJava(mapItems) + "\"";
		
		String className = "map_" + System.currentTimeMillis();
		
		StringBuilder code = new StringBuilder();
		if(mapItems == null || mapItems.isEmpty()) {
	    	code.append("package com.lenovo.edgent.agent.topology.map;\n");
	    	code.append("public class " + className + " implements ICustomMap<String> {\n");
	    	code.append("    @Override\n");
	    	code.append("    public String map(String line) {\n");
	    	code.append("        return line;\n");
	    	code.append("    }\n");
	    	code.append("}\n");
	    } else {
	    	code.append("package com.lenovo.edgent.agent.topology.map;\n");
	    	code.append("import com.google.gson.JsonElement;\n");
	    	code.append("import com.google.gson.JsonObject;\n");
	    	code.append("import com.google.gson.JsonParser;\n");
	    	code.append("public class " + className + " implements ICustomMap<String> {\n");
	    	code.append("    @Override\n");
	    	code.append("    public String map(String line) {\n");
	    	code.append("        JsonObject jo = new JsonParser().parse(line).getAsJsonObject();\n");
	    	code.append("        JsonObject jo_reported = jo.get(\"reported\").getAsJsonObject();\n");
	    	code.append("        JsonObject jo_reported_new = new JsonObject();\n");
	    	code.append("        JsonObject jo_map = new JsonParser().parse(" + mapItems + ").getAsJsonObject();\n");
	    	code.append("        for(java.util.Map.Entry<String, JsonElement> entry : jo_map.entrySet()){\n");
	    	code.append("        	if (jo_reported.has(entry.getKey())) {\n");
	    	code.append("        		jo_reported_new.add(jo_map.get(entry.getKey()).getAsString(), jo_reported.get(entry.getKey()));\n");
	    	code.append("        	}\n");
	    	code.append("        }\n");
	    	code.append("        jo.remove(\"reported\");\n");
	    	code.append("        jo.add(\"reported\", jo_reported_new);\n");
	    	code.append("        return jo.toString();\n");
	    	code.append("    }\n");
	    	code.append("}\n");
		}
    	
    	JSONObject obj = new JSONObject();
    	obj.put("app_name", appname);
    	obj.put("name", "com.lenovo.edgent.agent.topology.map." + className);
    	obj.put("code", code.toString());
    	
    	return obj.toString();
	}
	
    public static String buildFilter(String appname, Filter[] filters) {
		String filterString;
		if(filters == null || filters.length == 0) {
			filterString = "true";
		} else {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < filters.length; i++) {
				Filter f = filters[i];
				
				sb.append("(");
				
				String ff;
				if(f.getDataType().equalsIgnoreCase("int")) {
					ff = "getAsInt()";
				} else if(f.getDataType().equalsIgnoreCase("float")) {
					ff = "getAsFloat()";
				} else if(f.getDataType().equalsIgnoreCase("double")) {
					ff = "getAsDouble()";
				} else if(f.getDataType().equalsIgnoreCase("boolean")) {
					ff = "getAsBoolean()";
				} else {
					ff = "getAsString()";
				}
				String s = String.format("reported.has(\"%1$s\") && reported.get(\"%1$s\").%2$s%3$s%4$s", f.getPropertyName(), ff, f.getOperator(), f.getValue());
				sb.append(s);
				sb.append(")");
				
				if(i < filters.length - 1) {
					sb.append(f.getConjunction());
				}
			}
			
			filterString = sb.toString();
		}
		
		String className = "filter_" + System.currentTimeMillis();

    	StringBuilder code = new StringBuilder();
    	code.append("package com.lenovo.edgent.agent.topology.filter;\n");
    	code.append("import com.google.gson.JsonObject;\n");
    	code.append("import com.google.gson.JsonParser;\n");
    	code.append("public class " + className + " implements ICustomFilter<String> {\n");
    	code.append("    @Override\n");
    	code.append("    public boolean filter(String line) {\n");
    	code.append("        JsonObject jo = new JsonObject();\n");
    	code.append("        JsonParser jp = new JsonParser();\n");
    	code.append("        jo = jp.parse(line).getAsJsonObject();\n");
    	code.append("        JsonObject reported = jo.get(\"reported\").getAsJsonObject();\n");
    	code.append("        if (" + filterString + ") {\n");
    	code.append("            return true;\n");
    	code.append("        } else {\n");
    	code.append("        	 return false;\n");
    	code.append("        }\n");
    	code.append("    }\n");
    	code.append("}\n");
    	
    	JSONObject obj = new JSONObject();
    	obj.put("app_name", appname);
    	obj.put("name", "com.lenovo.edgent.agent.topology.filter." + className);
    	obj.put("code", code.toString());

    	return obj.toString();
	}
}
