package com.lenovo.iot.devicemanager.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Test {
/*
	private static String readJar(String jarFile) {

		StringBuilder sb = new StringBuilder();
		//String uuid = UUID.randomUUID().toString();
		final String relativeFilePath = "self_desc.json";
		try {
			ZipFile zipFile = new ZipFile(jarFile);
			Enumeration<? extends ZipEntry> e = zipFile.entries();

			while (e.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) e.nextElement();
				// if the entry is not directory and matches relative file then extract it
				if (!entry.isDirectory() && entry.getName().equals(relativeFilePath)) {
					BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

					int bytesRead = 0;
					byte[] buffer = new byte[1024];
					while ((bytesRead = bis.read(buffer)) != -1) {
						String chunk = new String(buffer, 0, bytesRead);
						sb.append(chunk);
					}
					bis.close();
				} else {
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
*/
	
	public static void main(String[] agrs) {
		//String result = readJar("D:\\HTStreamApp.jar");
		
		//Date d = new Date(1504832187393l);
		String device_id = "1504832187393l";
		String x = device_id.substring(device_id.length() - 1, device_id.length());
		System.out.print(x);
	}
}
