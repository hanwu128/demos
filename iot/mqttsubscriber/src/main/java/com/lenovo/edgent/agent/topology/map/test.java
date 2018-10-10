package com.lenovo.edgent.agent.topology.map;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class test implements ICustomMap<String> {
	@Override
	public String map(String line) {
		// TODO Auto-generated method stub
		JsonObject jo = new JsonParser().parse(line).getAsJsonObject();
		JsonObject jo_reported = jo.get("reported").getAsJsonObject();
		JsonObject jo_reported_new = new JsonObject();
		
		String map = "{\"temperature\": \"wendu\",\"humidity\": \"shidu\"}";
		JsonObject jo_map = new JsonParser().parse(map).getAsJsonObject();
		for(java.util.Map.Entry<String, JsonElement> entry : jo_map.entrySet()){
			if (jo_reported.has(entry.getKey())) {
				jo_reported_new.add(jo_map.get(entry.getKey()).getAsString(), jo_reported.get(entry.getKey()));
			}
		}
		
		jo.remove("reported");
		jo.add("reported", jo_reported_new);
		
		return jo.toString();
	}
	
	public static void main(String args[]) {
		try {
			System.out.println(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
