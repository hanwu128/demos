package com.lenovo.iot.devicemanager.model.bo;

import java.io.Serializable;

/**
 * Created by root on 2017/7/19.
 * 
 * Metadata json定义更新为： { "json_version":"1.0.0",//json version "manufactor":
 * "",//厂家 "model": "",//型号 "device_id": "",//设备id（厂家） "hardware_location":
 * "",//硬件位置 "customer_id": "",//客户ID "edgent_agent_os": "",//agent操作系统
 * "edgent_agent_os_version": "",//操作系统版本 "firmware_version": "",//firmware版本
 * "app_key": "",//客户APP ID "edgent_agent_version": "",//Agent 版本
 * "edgent_agent_app_list": [//Agent App list { "app_name": "",//Name
 * "app_version": "",//版本号 "app_update_time": 11111111111 //update时间 } ],
 * "console_url": "" //conslo url "timestampat":11111111111 // 发送时间
 * 
 * }
 * 
 * 
 * 
 */
public class ApacheAdgentMetaData implements Serializable {

	private static final long serialVersionUID = -8997153735796272771L;

	private Integer id;// primaryKey

	private String json_version;// json version
	private String hardware_manufactor;// 厂家
	private String hardware_model;// 型号 syn
	private String hardware_device_id;// 设备id（厂家） syn
	private String hardware_location;// 硬件位置 syn
	private String customer_id;// 客户ID syn
	private String hardware_os;// agent操作系统 syn
	private String hardware_os_version;// syn
	private String firmware_version;// firmware版本 syn
	private String host_ip;// 客户APP ID syn
	private String edgent_agent_name;
	private String edgent_agent_version;// Agent 版本 syn
	private String edgent_agent_app_list;// syn
	private String console_url;// syn
	private Long timestampat;// 发送时间 syn

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJson_version() {
		return json_version;
	}
	public String getJsonversion() {
		return json_version;
	}

	public void setJson_version(String json_version) {
		this.json_version = json_version;
	}
	public void setJsonversion(String json_version) {
		this.json_version = json_version;
	}

	public String getHardware_location() {
		return hardware_location;
	}
	public String getHardwarelocation() {
		return hardware_location;
	}

	public void setHardware_location(String hardware_location) {
		this.hardware_location = hardware_location;
	}
	public void setHardwarelocation(String hardware_location) {
		this.hardware_location = hardware_location;
	}

	public String getCustomer_id() {
		return customer_id;
	}
	public String getCustomerid() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public void setCustomerid(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getFirmware_version() {
		return firmware_version;
	}
	public String getFirmwareversion() {
		return firmware_version;
	}

	public void setFirmware_version(String firmware_version) {
		this.firmware_version = firmware_version;
	}
	public void setFirmwareversion(String firmware_version) {
		this.firmware_version = firmware_version;
	}

	public String getHost_ip() {
		return host_ip;
	}
	public String getHostip() {
		return host_ip;
	}

	public void setHost_ip(String host_ip) {
		this.host_ip = host_ip;
	}
	public void setHostip(String host_ip) {
		this.host_ip = host_ip;
	}

	public String getEdgent_agent_version() {
		return edgent_agent_version;
	}
	public String getEdgentagentversion() {
		return edgent_agent_version;
	}

	public void setEdgent_agent_version(String edgent_agent_version) {
		this.edgent_agent_version = edgent_agent_version;
	}
	public void setEdgentagentversion(String edgent_agent_version) {
		this.edgent_agent_version = edgent_agent_version;
	}

	public String getEdgent_agent_name() {
		return edgent_agent_name;
	}
	public String getEdgentagentname() {
		return edgent_agent_name;
	}

	public void setEdgent_agent_name(String edgent_agent_name) {
		this.edgent_agent_name = edgent_agent_name;
	}
	public void setEdgentagentname(String edgent_agent_name) {
		this.edgent_agent_name = edgent_agent_name;
	}

	public String getEdgent_agent_app_list() {
		return edgent_agent_app_list;
	}
	public String getEdgentagentapplist() {
		return edgent_agent_app_list;
	}

	public void setEdgent_agent_app_list(String edgent_agent_app_list) {
		this.edgent_agent_app_list = edgent_agent_app_list;
	}
	public void setEdgentagentapplist(String edgent_agent_app_list) {
		this.edgent_agent_app_list = edgent_agent_app_list;
	}

	public String getConsole_url() {
		return console_url;
	}
	public String getConsoleurl() {
		return console_url;
	}

	public void setConsole_url(String console_url) {
		this.console_url = console_url;
	}
	public void setConsoleurl(String console_url) {
		this.console_url = console_url;
	}

	public String getHardware_os_version() {
		return hardware_os_version;
	}
	public String getHardwareosversion() {
		return hardware_os_version;
	}

	public void setHardware_os_version(String hardware_os_version) {
		this.hardware_os_version = hardware_os_version;
	}
	public void setHardwareosversion(String hardware_os_version) {
		this.hardware_os_version = hardware_os_version;
	}

	public String getHardware_os() {
		return hardware_os;
	}
	public String getHardwareos() {
		return hardware_os;
	}

	public void setHardware_os(String hardware_os) {
		this.hardware_os = hardware_os;
	}
	public void setHardwareos(String hardware_os) {
		this.hardware_os = hardware_os;
	}

	public String getHardware_device_id() {
		return hardware_device_id;
	}
	public String getHardwaredeviceid() {
		return hardware_device_id;
	}

	public void setHardware_device_id(String hardware_device_id) {
		this.hardware_device_id = hardware_device_id;
	}
	public void setHardwaredeviceid(String hardware_device_id) {
		this.hardware_device_id = hardware_device_id;
	}

	public String getHardware_model() {
		return hardware_model;
	}
	public String getHardwaremodel() {
		return hardware_model;
	}

	public void setHardware_model(String hardware_model) {
		this.hardware_model = hardware_model;
	}
	public void setHardwaremodel(String hardware_model) {
		this.hardware_model = hardware_model;
	}

	public String getHardware_manufactor() {
		return hardware_manufactor;
	}
	public String getHardwaremanufactor() {
		return hardware_manufactor;
	}

	public void setHardware_manufactor(String hardware_manufactor) {
		this.hardware_manufactor = hardware_manufactor;
	}
	public void setHardwaremanufactor(String hardware_manufactor) {
		this.hardware_manufactor = hardware_manufactor;
	}

	public Long getTimestampat() {
		return timestampat;
	}

	public void setTimestampat(Long timestampat) {
		this.timestampat = timestampat;
	}
}
