package com.lenovo.iot.devicemanager.mqttsubscriber;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import com.lenovo.iot.devicemanager.service.DeviceShadowService;
import com.lenovo.iot.devicemanager.service.DeviceStreamService;
import com.lenovo.iot.devicemanager.service.KafkaProducerService;
import com.lenovo.iot.devicemanager.service.OnlineDeviceService;
import com.lenovo.iot.devicemanager.util.HttpClient;

public class MqttSubscriber implements MqttCallback {
	// Private instance variables
	private MqttClient client;
	private String brokerUrl;
	private MqttConnectOptions conOpt;
	private boolean cleanSession;
	private String password;
	private String userName;
	private int keepAlive;

	public MqttSubscriber(String brokerUrl, String clientId, boolean cleanSession, String userName, String password, int keepAlive) throws MqttException {
		this.brokerUrl = brokerUrl;
		this.cleanSession = cleanSession;
		this.password = password;
		this.userName = userName;
		this.keepAlive = keepAlive;
		// This sample stores in a temporary directory where messages temporarily stored until the message has been delivered to the server.
		// a real application ought to store them somewhere where they are not likely to get deleted or tampered with
		String tmpDir = System.getProperty("java.io.tmpdir");
		MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

		try {
			// Construct the connection options object that contains connection parameters such as cleanSession and LWT
			conOpt = new MqttConnectOptions();
			conOpt.setAutomaticReconnect(true); //自动重连没有回调，若要回调需要使用 MqttAsyncClient
			conOpt.setCleanSession(this.cleanSession);
			if (password != null) {
				conOpt.setPassword(this.password.toCharArray());
			}
			if (userName != null) {
				conOpt.setUserName(this.userName);
			}
			conOpt.setKeepAliveInterval(this.keepAlive);

			// Construct an MQTT blocking mode client
			client = new MqttClient(this.brokerUrl, clientId, dataStore);

			// Set this wrapper as the callback handler
			client.setCallback(this);
			
			try {
				String path = SSL.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
				int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
				int lastIndex = path.lastIndexOf(File.separator) + 1;
				path = path.substring(firstIndex, lastIndex);
				if(!path.isEmpty()) {
					path = path + "/";
				}
				
                conOpt.setSocketFactory(new SSL(path).getSSLSocktet());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

		} catch (MqttException e) {
			e.printStackTrace();
			log("Unable to set up client: " + e.toString());
			System.exit(1);
		}
	}
	
	public void subscribe(String topicName, int qos) throws MqttException {

		// Connect to the MQTT server
		client.connect(conOpt);
		log("Connected to " + brokerUrl + " with client ID " + client.getClientId());

		// Subscribe to the requested topic
		// The QoS specified is the maximum level that messages will be sent to the client at.
		// For instance if QoS 1 is specified, any messages originally published at QoS 2 will be downgraded to 1 when delivering to the client
		// but messages published at 1 and 0 will be received at the same level they were published at.
		
		String[] topicArray = topicName.split(";");
		int[] qosArray = new int[topicArray.length];
		for(int i = 0; i < topicArray.length; i++) {
			qosArray[i] = qos;
		}
		
		client.subscribe(topicArray, qosArray); //MOC消息
		for(int i = 0; i < topicArray.length; i++) {
			log("Subscribing to topic \"" + topicArray[i] + "\" qos " + qosArray[i]);
		}

//		client.subscribe(topicName, qos); //MOC消息
//		client.subscribe("$SYS/brokers/emqttd@172.17.199.108/clients/#", 0); //设备上下线消息
//		client.subscribe("$SYS/brokers/emqttd@172.17.199.109/clients/#", 0); //设备上下线消息
	}
	
	public void publish(String topicName, String message, int qos) throws MqttException {
		MqttMessage msg = new MqttMessage();
		msg.setPayload(message.getBytes());
		msg.setQos(qos);
		
		client.publish(topicName, msg);
	}
	
	public void close() {
		// Disconnect the client from the server
		try {
			client.disconnect();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void log(String message) {
		System.out.println(message);
	}

	/****************************************************************/
	/* Methods to implement the MqttCallback interface */
	/****************************************************************/
	public void connectionLost(Throwable cause) {
		// Called when the connection to the server has been lost.
		// An application may choose to implement reconnection logic at this point. Here simply exits.
		log("Connection to " + brokerUrl + " lost!" + cause);
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
	}

	public void messageArrived(String topic, MqttMessage message) throws MqttException {
		String[] topicArray = topic.split("/");
		String messageString = new String(message.getPayload());
		
		log(topic);
		log(messageString);
		
		try {
			if(topic.startsWith("$SYS/brokers/")) {
				String broker = topicArray[2];
				if(topicArray[3].equals("clients")) {
					String clientId = topicArray[4];
					if(topicArray[5].equals("connected")) {
						//设备上线
						OnlineDeviceService.online(broker, clientId, messageString);
					} else if(topicArray[5].equals("disconnected")) {
						//设备下线
						OnlineDeviceService.offline(broker, clientId, messageString);
					}
				}
			} else if(topic.startsWith("$MOC/")) {
				//String device_group = topicArray[1];
				String clientId = topicArray[2];

				//元数据信息注册
				if(topicArray[3].equals("shadow")) {
					if(topicArray[4].equals("update")) {
						//影子变化 - 通知应用服务更新
						DeviceShadowService.transfer(clientId, topic, messageString, message.getQos());
					}
				} else if(topicArray[3].equals("stream")) {
					if(topicArray[4].equals("up")) {
						DeviceStreamService.transfer(clientId, topic, messageString, message.getQos());
						//流数据发送到 kafka						
						//String mocString = EdgeMessage.Build1_0_2(clientId, "900219", "__EDGE_EVENT_MONITOR", "stream", messageString);
						//kafka.send("topic4dbflume1", UUID.randomUUID().toString(), mocString);
					}
				} else if(topicArray[3].equals("meta")) {
					if(topicArray[4].equals("update")) {
						//设备上报基本信息
						JSONObject JSONObjectmessageString = JSONObject.fromObject(messageString);
						HttpClient.httpPost("ApachEdgentVersionController/insertApacheAdgentMetaData.url", JSONObjectmessageString);
					}
				} else if(topicArray[3].equals("app")) {
					if(topicArray[4].equals("update")) {
						if(topicArray.length>4 && topicArray[5].equals("reponse")) {
							//设备上报app下发结果
							com.alibaba.fastjson.JSONObject JSONObject_messageString = com.alibaba.fastjson.JSONObject.parseObject(messageString);

							String task_id = JSONObject_messageString.getString("task_id");
							Boolean is_success = JSONObject_messageString.getBoolean("is_success");

							//设备上报基本信息
							JSONObject _JSONObject4Message = new JSONObject();
							_JSONObject4Message.put("taskId", task_id);
							_JSONObject4Message.put("statusCallbackFromApacheAdgetn", is_success);
							HttpClient.httpPost("ApachEdgentVersionController/checkApacheAdgentBroker.url", _JSONObject4Message);
						}
					}
				}
			} else {
				log("unkown topic & message:" + topic + "," + messageString);
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("exeption occured when process mqtt message.");
		}
	}
	/****************************************************************/
	/* End of MqttCallback methods */
	/****************************************************************/

	/**
	 * The main entry point of the sample.
	 * 
	 * This method handles parsing of the arguments specified on the
	 * command-line before performing the specified action.
	 */
	
	/*	
	private static String buildMap(String appname, String mapItems) {
		mapItems = "\"" + StringEscapeUtils.escapeJava(mapItems) + "\"";
		
		String className = "map_" + System.currentTimeMillis();
		
		StringBuilder code = new StringBuilder();
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
    	
    	JSONObject obj = new JSONObject();
    	obj.put("app_name", appname);
    	obj.put("name", "com.lenovo.edgent.agent.topology.map." + className);
    	obj.put("code", code.toString());
    	
    	return obj.toString();
	}

	private static String buildFilter(String appname, Filter[] filters) {
		String filterString;
		if(filters == null || filters.length == 0) {
			filterString = "true";
		} else {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < filters.length; i++) {
				Filter f = filters[i];
				
				sb.append("(");
				
				String ff;
				if(f.getDataType().equalsIgnoreCase("string")) {
					ff = "getAsDouble()";
				} else if(f.getDataType().equalsIgnoreCase("int")) {
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
*/
	
	public static void main(String[] args) {
		String configFile = "mqtt.conf";
		
		String broker = "ssl://223.203.218.93:8883";
		String clientId = "";
		String userName = "";
		String password = "";
		String topic = "";
		int qos = 1;
		int keepAlive = 90;
		boolean cleanSession = false; // Durable subscriptions
		String devicemanager = "http://223.203.218.93/devicemanager/";
		String broker_kafka = "node3.leap.com:6667,node4.leap.com:6667,node5.leap.com:6667,node6.leap.com:6667";
		
        // Read settings from configuration file
		try {
			String path = MqttSubscriber.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
			int lastIndex = path.lastIndexOf(File.separator) + 1;
			path = path.substring(firstIndex, lastIndex);
			if(!path.isEmpty()) {
				path = path + "/";
			}
			configFile = path + configFile;

			FileInputStream is = new FileInputStream(configFile);
			   
			Properties dbProps = new Properties();
			dbProps.load(is);
			broker = dbProps.getProperty("broker");
			clientId = dbProps.getProperty("client_id");
			topic = dbProps.getProperty("topic");
			userName = dbProps.getProperty("username");
			password = dbProps.getProperty("password");
			String qos_string = dbProps.getProperty("qos");
			String keepAlive_string = dbProps.getProperty("keep_alive");
			String clean_session_String = dbProps.getProperty("clean_session");
			devicemanager = dbProps.getProperty("devicemanager");
			broker_kafka = dbProps.getProperty("broker_kafka");
			
			if(!qos_string.isEmpty()) {
				qos = Integer.parseInt(qos_string);
			}
			if(!keepAlive_string.isEmpty()) {
				keepAlive = Integer.parseInt(keepAlive_string);
			}			
			if(!clean_session_String.isEmpty()) {
				cleanSession = Boolean.parseBoolean(clean_session_String);
			}
			
			is.close();
		} catch (Exception e) {
			System.err.println("failed to read config file.");
			e.printStackTrace();
			//System.exit(1);
		}
		
		// Read settings from command arguments
        final org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
//      final org.apache.commons.cli.Option option1 = new org.apache.commons.cli.Option("c", "conf", true, "Configuration file path");
        final org.apache.commons.cli.Option option1 = new org.apache.commons.cli.Option("b", "broker", true, "Subscriber topic");
        final org.apache.commons.cli.Option option2 = new org.apache.commons.cli.Option("d", "deviceid", true, "Device id");
        final org.apache.commons.cli.Option option3 = new org.apache.commons.cli.Option("u", "username", true, "User name");
        final org.apache.commons.cli.Option option4 = new org.apache.commons.cli.Option("p", "password", true, "Password");
        final org.apache.commons.cli.Option option5 = new org.apache.commons.cli.Option("t", "topic", true, "Subscriber topic");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOption(option5);
        
        final org.apache.commons.cli.CommandLineParser parser = new org.apache.commons.cli.PosixParser();
        org.apache.commons.cli.CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("t")) {
            	topic = cmd.getOptionValue("t");
            }
            if (cmd.hasOption("d")) {
            	clientId = cmd.getOptionValue("d");
            }
            if (cmd.hasOption("u")) {
            	userName = cmd.getOptionValue("u");
            }
            if (cmd.hasOption("p")) {
            	password = cmd.getOptionValue("p");
            }
        } catch (final org.apache.commons.cli.ParseException e) {
            System.out.println("parser command line error:" + e.getMessage());
        }
        
        // Connecting to MQTT Server
		HttpClient.SERVER_URL = devicemanager;
		KafkaProducerService.getInstance().init(broker_kafka);

		// With a valid set of arguments, the real work of driving the client API can begin
		try {
			// Create an instance of this class
			MqttSubscriber emqtt = new MqttSubscriber(broker, clientId, cleanSession, userName, password, keepAlive);
			emqtt.subscribe(topic, qos);
			
			while(true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//            try {
//				String commandLine = "";
//				InputStreamReader converter = new InputStreamReader(System.in);
//				BufferedReader in = new BufferedReader(converter);
//				while (commandLine != null && !(commandLine.equals("exit"))) {
//					commandLine = in.readLine();
//					if(commandLine != null) {
//						if(commandLine.startsWith("map:")) {
//							//String map = commandLine.substring(4);
//							String map = "{\"temperature\": \"temperature\",\"humidity\": \"humidity\"}";
//							//String map = "{\"temperature\": \"temperature\"}";
//							String message = buildMap("HTStreamApp", map);
//							
//							emqtt.publish("$MOC/control/test_deviceid_01/map/update", message, 0);
//						} else if(commandLine.startsWith("filter:")) {
//							//String filter = commandLine.substring(7);
//							//String filter = "temperature>0f && humidity>20f";
//							
//							Filter f1 = new Filter("wendu", "double", ">=", "0", "&&");
//							Filter f2 = new Filter("shidu", "double", ">=", "20");
//							String message = buildFilter("HTStreamApp", new Filter[]{ f1, f2 });
//							
//							emqtt.publish("$MOC/control/test_deviceid_01/filter/update", message, 0);
//						}
//					}
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
//			emqtt.close();
		} catch (MqttException me) {
			// Display full details of any exception that occurs
			System.out.println("reason: " + me.getReasonCode());
			System.out.println("msg: " + me.getMessage());
			System.out.println("loc: " + me.getLocalizedMessage());
			System.out.println("cause: " + me.getCause());
			System.out.println("exception: " + me);
			me.printStackTrace();
		}
		
		System.out.println("main thread exit!!!");
	}
}
