package com.lenovo.iot.auth.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lenovo.iot.auth.service.AuthService;

@Controller
public class AuthController {
	
	private static Logger log = Logger.getLogger("fordebug");	
	
	@Autowired
	private AuthService mqttService;
	
	/*EMQTT Auth*/
	@RequestMapping(value = "/auth.do", produces = { "application/json;charset=UTF-8" })
	public @ResponseBody
	void Auth(String clientid, String username, String password, HttpServletResponse response) throws Exception {
		boolean result = mqttService.auth(clientid, username, password);
		if(result) {
			log.debug("auth ok.");
			response.setStatus(200);
		} else {
			log.debug("auth failed.");
			response.setStatus(401);
		}
	}
	
	/*EMQTT ACL*/
	@RequestMapping(value = "/acl.do", produces = { "application/json;charset=UTF-8" })
	public @ResponseBody
	void ACL(String access, String username, String clientid, String ipaddr, String topic, HttpServletResponse response) throws Exception {
		boolean result = mqttService.acl(access, username, clientid, ipaddr, topic);
		if(result) {
			response.setStatus(200);
		} else {
			response.setStatus(401);
		}
	}
}
