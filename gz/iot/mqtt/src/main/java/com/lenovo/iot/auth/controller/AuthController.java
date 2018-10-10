package com.lenovo.iot.auth.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lenovo.iot.auth.service.AuthService;

@RequestMapping("/auth")
@RestController
public class AuthController {

	//private Logger log = Logger.getLogger("auth");

	@Autowired
	private AuthService mqttService;

	/* EMQTT Auth */
	@RequestMapping(value = "/auth.do", produces = { "text/plain; charset=UTF-8" })
	public void Auth(String clientid, String username, String password, HttpServletResponse response) throws Exception {
		boolean result = mqttService.auth(clientid, username, password);
		if (result) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	/* EMQTT ACL */
	@RequestMapping(value = "/acl.do", produces = { "text/plain; charset=UTF-8" })
	public void ACL(String access, String username, String clientid, String ipaddr, String topic, HttpServletResponse response) throws Exception {
		boolean result = mqttService.acl(access, username, clientid, ipaddr, topic);
		if (result) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}
