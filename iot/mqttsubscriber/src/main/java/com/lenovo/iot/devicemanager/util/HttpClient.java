package com.lenovo.iot.devicemanager.util;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClient {
//	private static Logger logger = LoggerFactory.getLogger(HttpClient.class); // 日志记录

	public static String SERVER_URL;
	public static ExecutorService executorService = Executors.newFixedThreadPool(100);

//	public static void httpPost(final String uri, final String data) {
//		URL url = null;
//		try {
//			url = new URL(SERVER_URL + uri);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		final URL url2 = url;
//		executorService.execute(new Runnable() {
//
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					byte[] postData = data.getBytes("UTF-8");
//					int postDataLength = postData.length;
//					HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
//					conn.setDoOutput(true);
//					conn.setInstanceFollowRedirects(false);
//					conn.setRequestMethod("POST");
//					conn.setRequestProperty("Content-Type", "application/json");
//					conn.setRequestProperty("charset", "utf-8");
//					conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
//					conn.setUseCaches(false);
//					DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
//					wr.write(postData);
//					wr.close();
//					//BufferedReader respon = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//					//String retString = respon.readLine();
//					//System.out.println(retString);
//					InputStream inStream = conn.getInputStream();
//					if (inStream != null) {
//						ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//						// 创建一个Buffer字符串
//						byte[] buffer = new byte[1024];
//						// 每次读取的字符串长度，如果为-1，代表全部读取完毕
//						int len = 0;
//						// 使用一个输入流从buffer里把数据读取出来
//						while ((len = inStream.read(buffer)) != -1) {
//							// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
//							outStream.write(buffer, 0, len);
//						}
//						// 关闭输入流
//						inStream.close();
//						// 把outStream里的数据写入内存
//						//result.bresult = outStream.toByteArray();
//						//return result;
//					}
//					
//					conn.disconnect();
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * post请求
	 * 
	 * @param url
	 *            url地址
	 * @param jsonParam
	 *            参数
	 * @return
	 */
	public static void httpPost(final String uri, final JSONObject jsonParam) {
		final String url = SERVER_URL + uri;
		
		// post请求返回结果
		executorService.execute(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				DefaultHttpClient httpClient = new DefaultHttpClient();
//				JSONObject jsonResult = null;
				HttpPost method = null;

				try {
					method = new HttpPost(url);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}

				try {
					if (null != jsonParam) {
						// 解决中文乱码问题
						StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
						entity.setContentEncoding("UTF-8");
						entity.setContentType("application/json");
						method.setEntity(entity);
					}
					HttpResponse result = httpClient.execute(method);
					
					/** 请求发送成功，并得到响应 **/
					if (result.getStatusLine().getStatusCode() == 200) {
						result.getEntity();
//						String str = "";
//						try {
//							/** 读取服务器返回过来的json字符串数据 **/
//							str = EntityUtils.toString(result.getEntity());
//							/** 把json字符串转换成json对象 **/
//							jsonResult = JSONObject.fromObject(str);
//							
//							//logger.info(jsonResult.toString());
//							// return jsonResult;
//						} catch (Exception e) {
//							System.out.println(e.toString());
//							//logger.error("post请求提交失败:" + url, e);
//						}
					} else {
						System.out.println("response code:" + result.getStatusLine().getStatusCode());
					}
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
		});
	}

/*	
	public static JSONObject httpGet(String url) {
		// get请求返回结果
		JSONObject jsonResult = null;
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			// 发送get请求
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String strResult = EntityUtils.toString(response.getEntity());
				jsonResult = JSONObject.fromObject(strResult);
				//url = URLDecoder.decode(url, "UTF-8");
			} else {
				logger.error("get请求提交失败:" + url);
			}
		} catch (IOException e) {
			logger.error("get请求提交失败:" + url, e);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
*/
}
