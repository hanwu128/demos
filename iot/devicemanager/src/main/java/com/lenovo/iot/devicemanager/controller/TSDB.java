package com.lenovo.iot.devicemanager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lenovo.iot.devicemanager.config.TsDb;

@RequestMapping("/tsdb")
@Controller
public class TSDB {

//	private static Logger log = Logger.getLogger("fordebug");

	@Autowired
	private TsDb tsdb;
    
	@RequestMapping(value = "/suggest.do")
	@ResponseBody
	@CrossOrigin(origins="*")
	public JSONArray suggest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		JSONArray result = new JSONArray();
		
		String uri = request.getQueryString();
		String url = "http://" + tsdb.getHost() + ":" + tsdb.getPort() + tsdb.getApiSuggest() + "?" + uri;
		
        try {
            HttpResponse res = new DefaultHttpClient().execute(new HttpGet(url));
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(res.getEntity());
                
                result = JSONArray.fromObject(strResult);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
		
		return result;
	}
}
