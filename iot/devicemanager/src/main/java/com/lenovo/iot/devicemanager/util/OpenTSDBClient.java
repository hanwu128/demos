package com.lenovo.iot.devicemanager.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenTSDBClient {

	public static OpenTSDBResult httpPost(final String urlString, final String data) {
		OpenTSDBResult response = new OpenTSDBResult();
		
		try {
			URL url = new URL(urlString);

			byte[] postData = data.getBytes("UTF-8");
			int postDataLength = postData.length;
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.connect();

			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.write(postData);
			wr.close();

			InputStream inStream;
			int status_code = conn.getResponseCode();
			response.setStatus_code(status_code);
			if (status_code == HttpURLConnection.HTTP_OK || status_code == HttpURLConnection.HTTP_CREATED || status_code == HttpURLConnection.HTTP_ACCEPTED) {
				inStream = conn.getInputStream();
			} else {
				inStream = conn.getErrorStream();
			}
			if (inStream != null) {
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				inStream.close();

				byte[] res = outStream.toByteArray();
				String restr = new String(res);
				response.setResponse(restr);
			}

			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
}
