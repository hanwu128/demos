package com.lenovo.iot.devicemanager.mqttsubscriber;

import java.net.InetAddress;
import java.net.NetworkInterface;

public class Test {
	// 获取MAC地址的方法
/*
	private static String getMACAddress() throws Exception {
		InetAddress ia = InetAddress.getLocalHost();
		
		// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

		// 下面代码是把mac地址拼装成String
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mac.length; i++) {
//			if (i != 0) {
//				sb.append("-");
//			}
			// mac[i] & 0xFF 是为了把byte转化为正整数
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}

		// 把字符串所有小写字母改为大写成为正规的mac地址并返回
		return sb.toString();
	}
 */
	
	public static void main(String[] args) {
		String s = "339076602";
		Long l = Long.parseLong(s);
		
		String result = String.format("%010d", l);
		
		System.out.println(result);
	}
}
