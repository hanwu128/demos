# Digital Twin 第三方应用调用API


## 目录

- [定义说明](#定义说明)
    - [单词简写](#单词简写)
    - [状态码](#状态码)
- [API列表](#api列表)
    - [1.查询DT列表](#1查询dt列表)
    - [2.查询DT详情](#2查询dt详情)
    - [3.添加/更新DT](#3添加更新dt)
    - [4.删除DT](#4删除dt)
    - [5.反控DT](#5反控dt)
    - [6.查询DT属性历史数据](#6查询dt属性历史数据)



## 定义说明

### 单词简写

- DT：Digital Twin
- HOST：API服务IP地址或者域名
- PORT：API服务端口


### 状态码

1. 响应HTTP状态码固定只使用2个：200，400，分别表示响应成功和错误。
2. 每一个API都会返回响应消息体，消息体格式定义如下：
```json
{
	"code": 200,
	"data": "yyy",
	"message": "zzz"
}
```
- code：业务状态码，目前使用标准的HTTP状态码，含义也与标准HTTP状态码保持一致；今后根据业务需要可能会自定义其他状态码。
- data：消息体数据。
- message：提示信息，目前只定义了2个值：success，error。


## API列表

### 1.查询DT列表
- 地址：http://HOST:PORT/dt/open/list.do
- 方法：POST
- 参数：
```json
{
	"name": "",     
	"current": 1,   
	"pagesize": 10   
}
```
- 参数含义：
    - name：根据DT名称模糊查询，为空时查询全部DT列表
    - current：分页参数，当前请求页数
    - pagesize：当前页请求记录数


- 返回：
```json
{
	"code": 200,
	"data": {
		"current": 1,
		"total": 3,
		"pagesize": 10,
		"rows": [
			{
				"createtimestamp": 1526459318000,  
				"describemessage": "",				 
				"digitaltwinname": "test01",		 
				"id": 24,							 
				"updatetimestamp": 1526459318000	 
			}
		]
	},
	"message": "success"
}
```
- 返回值含义：
	- current：分页参数，第几页
	- total：记录总数
	- pagesize：当前页数量
	- rows：DT列表
		- createtimestamp：创建DT时间戳
		- describemessage：DT描述
		- digitaltwinname：DT名称
		- id：DT唯一标识
		- updatetimestamp：DT更新时间戳
  

### 2.查询DT详情
- 地址：http://HOST:PORT/dt/open/detail.do
- 方法：POST
- 参数：
```json
{
	"id": 20
}
```
- 参数含义：
    - id：DT唯一标识


- 返回：
```json
{
	"code": 200,
	"data": {
		"attrList": [
			{
				"createtimestamp": 1526372431000,
				"daddyid": 20,
				"describemessage": "",
				"digitaltwinname": "mytest",
				"expectedvalue": 0,
				"id": 65,
				"metric": "iot.service.temperature",
				"sonname": "",
				"tagkv": {
					"lable": "8ACE0001",
					"deviceId": "10000100000E9BBCF0BBFF7B4B075929"
				},
				"unit": "",
				"updatetimestamp": 1526372431000,
				"value": 28.92,
				"valuetimestamp": 1526889609
			}
		],
		"createtimestamp": 1526465850000,
		"describemessage": "",
		"digitaltwinname": "mytest",
		"id": 20,
		"updatetimestamp": 1526465850000
	},
	"message": "success"
}
```
- 返回值含义：
	- attrList：DT属性列表
		- createtimestamp：属性创建时间戳
		- daddyid：DT唯一标识
		- describemessage：属性描述
		- digitaltwinname：DT名称
		- expectedvalue：期望值
		- id：属性唯一标识
		- metric：标签
		- sonname：属性名称
		- tagkv：属性tag
			- lable：tag键名称
			- deviceId：tag值
		- unit：属性单位
		- updatetimestamp：属性更新时间戳
		- value：属性值
		- valuetimestamp：从OpenSDB查询到的属性值时间戳
	- createtimestamp：DT创建时间戳
	- describemessage：DT描述
	- digitaltwinname：DT名称
	- id：DT唯一标识
	- updatetimestamp：DT更新时间戳


### 3.添加/更新DT
- 地址：http://HOST:PORT/dt/open/update.do
- 方法：POST
- 参数：
```json
{
	"id": 15,
	"digitaltwinname": "digital twin name update",
	"describemessage": "xxx update",
	"attrList": [
		{
			"id": 28,
			"sonname": "xxx update",
			"describemessage": "xxx update",
			"unit": "xxx update",
			"metric": "metricnamex update",
			"tagkv": {
				"tagk1": "tagv1 update",
				"tagk2": "tagv2 update"
			}
		}
	]
}
```
- 参数含义：
    - id：DT唯一标识，新建DT时为空
    - digitaltwinname：DT名称
    - describemessage：DT描述
    - attrList：属性列表
    	- id：属性id，新建属性时为空 
    	- sonname：属性名称
    	- describemessage：属性描述
    	- unit：属性单位 
    	- metric：属性标签
    	- tagkv：属性tag
    		


- 返回：
```json
{
	"code": 200,
	"data": "success",
	"message": "success"
}
```
- 返回值含义：
	- code：执行结果码，含义与标准HTTP状态码相同
	- data：执行结果数据
	- message：执行结果消息


### 4.删除DT
- 地址：http://HOST:PORT/dt/open/delete.do
- 方法：POST
- 参数：
```json
{
	"id": 1
}
```
- 参数含义：
    - id：DT唯一标识

- 返回：
```json
{
	"code": 200,
	"data": "success",
	"message": "success"
}
```
- 返回值含义：
	- code：执行结果码，含义与标准HTTP状态码相同
	- data：执行结果数据
	- message：执行结果消息


### 5.反控DT
- 地址：http://HOST:PORT/dt/open/rc.do
- 方法：POST
- 参数：
```json
{
	"id": 1,
	"attr": [
		{
			"id": 1,
			"value": 11
		},
		{
			"id": 2,
			"value": 12
		}
	]
}
```
- 参数含义：
    - id： DT唯一标识
    - attr：DT属性列表
        - id：DT属性唯一标识
        - value：DT属性反控值

- 返回：
```json
{
	"code": 200,
	"data": "success",
	"message": "success"
}
```
- 返回值含义：
	- code：执行结果码，含义与标准HTTP状态码相同
	- data：执行结果数据
	- message：执行结果消息


- 描述：
    在反控流程中，可能需要增加如下处理逻辑：设备在接收到通过MQTT发送的反控消息并执行状态更新之后，如果状态更新成功，应该再次以MQTT消息的方式实现一个反馈通知。
    

### 6.查询DT属性历史数据
- 地址：http://HOST:PORT/dt/open/attribute.do
- 方法：POST
- 参数：
```json
{
	"ids": [10, 20],
	"start": 1526383237000,
	"end": 1529061637000
}
```
- 参数含义：
    - ids： DT属性ID列表
    - start：查询开始时间戳（单位：毫秒）
    - end：查询结束时间戳（单位：毫秒）

- 返回：
```json
{
	"code": 200,
	"data": [{
			"id": 10,
			"sonname": "",
			"unit": "",
			"metric": "app.topology.services.temperaturedemoapp",
			"tags": {
				"label": "6002",
				"status": "0",
				"device": "10000100000EEC018EFC18C737D47899"
			},
			"values": {
				"1526383237000": 26.86,
				"1529061637000": 30.12
			}
		},
		{
			"id": 20,
			"sonname": "",
			"unit": "",
			"metric": "app.topology.services.temperaturedemoapp",
			"tags": {
				"label": "6002",
				"status": "0",
				"device": "10000100000EEC018EFC18C737D47899"
			},
			"values": {
				"1526383237000": 26.86,
				"1529061637000": 30.12
			}
		}
	],
	"message": "success"
}
```
- 返回值含义：
	- id：属性唯一标识
	- sonname：属性名称
	- unit：属性单位
	- metric：标签
	- tags：属性tag
		- label：tag键名称
		- status：tag键名称
		- device：tag键名称
	- values：属性值，其中以键值对存储指定时间对应对应的值
