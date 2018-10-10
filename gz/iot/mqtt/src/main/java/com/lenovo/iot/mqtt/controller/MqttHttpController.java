package com.lenovo.iot.mqtt.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.lenovo.iot.mqtt.service.MqttHttpService;

@RestController
@RequestMapping(value = "/mqtt")
public class MqttHttpController {

	@Autowired
	private MqttHttpService mqttHttpService;

	/**
	 * 获取所有节点
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/nodes", produces = { "application/json;charset=UTF-8" })
	private String get_nodes(HttpServletRequest request, HttpServletResponse response) {
		try {
			return mqttHttpService.get_nodes();
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return getExceptionResult(e, request).toString();
		}
	}

	/**
	 * 获取所有节点的  metric+stats 并汇总
	 */
	@RequestMapping(value = "/metrics", produces = { "application/json;charset=UTF-8" })
	private String get_metrics(HttpServletRequest request, HttpServletResponse response) {
		try {
			return mqttHttpService.get_metrics();
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return getExceptionResult(e, request).toString();
		}
	}

	/**
	 * 获取节点的 clients
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/nodes/*/clients", produces = { "application/json;charset=UTF-8" })
	private String get_clients(HttpServletRequest request, HttpServletResponse response) {
		try {
			String uri = request.getRequestURI();
			String queryString = request.getQueryString();
			
			String ip = uri.split("/")[3];
			return mqttHttpService.get_clients(ip, queryString);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return getExceptionResult(e, request).toString();
		}
	}
	
	/**
	 * 获取节点的 sessions
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/nodes/*/sessions", produces = { "application/json;charset=UTF-8" })
	private String get_session(HttpServletRequest request, HttpServletResponse response) {
		try {
			String uri = request.getRequestURI();
			String queryString = request.getQueryString();
			
			String ip = uri.split("/")[3];
			return mqttHttpService.get_sessions(ip, queryString);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return getExceptionResult(e, request).toString();
		}
	}
	
	/**
	 * 获取节点的 topics
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/nodes/*/topics", produces = { "application/json;charset=UTF-8" })
	private String get_topic(HttpServletRequest request, HttpServletResponse response) {
		try {
			String uri = request.getRequestURI();
			String queryString = request.getQueryString();
			
			String ip = uri.split("/")[3];
			return mqttHttpService.get_topics(ip, queryString);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return getExceptionResult(e, request).toString();
		}
	}
	
	/**
	 * 获取节点的 subscription
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/nodes/*/subscriptions", produces = { "application/json;charset=UTF-8" })
	private String get_subscription(HttpServletRequest request, HttpServletResponse response) {
		try {
			String uri = request.getRequestURI();
			String queryString = request.getQueryString();
			
			String ip = uri.split("/")[3];
			return mqttHttpService.get_subscriptions(ip, queryString);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return getExceptionResult(e, request).toString();
		}
	}
	
	private JsonObject getExceptionResult(Exception e, HttpServletRequest request) {
		//StringWriter sw = new StringWriter();
		//PrintWriter pw = new PrintWriter(sw);
		//e.printStackTrace(pw);
		
		JsonObject jo = new JsonObject();
		jo.addProperty("error", e.toString());
		jo.addProperty("uri", new StringBuilder(request.getRequestURI()).append("?").append(request.getQueryString()).toString());
		//jo.addProperty("stackinfo", sw.toString());
		
		return jo;
	}
}
