/**
 * 
 */
package com.lenovo.iot.devicemanager.util.httppost;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * IO工具.
 * @Description: org.chench.util.IOUtil
 * @Author chench email: chenchanghui@lizi-inc.com
 * @date 2015-12-19
 */
public class IOUtil {
	
	/**
	 * 关闭输出流
	 * @param output 输出流
	 */
	public static void closeOutputStream(OutputStream output) {
		if(output == null) {
			return;
		}
		
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭输入流
	 * @param input 输入流
	 */
	public static void closeInputStream(InputStream input) {
		if(input == null) {
			return;
		}
		
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭HTTP连接
	 * @param conn
	 */
	public static void closeHttpURLConnection(HttpURLConnection conn) {
		if(conn == null) {
			return;
		}
		
		try {
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从输入流中读取数据并关闭输入流
	 * @param is
	 * @return
	 */
	public static byte[] getContentFromInputstream(InputStream is) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {

			int count = 0;

			byte[] buffer = new byte[512];

			while((count = is.read(buffer)) > 0) {
				bos.write(buffer, 0, count);
			}

			bos.flush();

			return bos.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeInputStream(is);
		}
		return null;
	}
	
	private IOUtil() {
	}

}
