# DT Stream API

提供给Stream中运行的DT Stage使用，以实现在DT Stage中访问物实例数据。



## 开发环境

HOST：172.17.200.61
PORT：8400



## 定义说明

### 单词简写

- DT： DigitalTwin简写
- HOST：API服务IP地址或者域名
- PORT：API服务端口


### 格式约定

REST风格的API。


###  接口安全

无


## API列表

### 查询DT实例列表

- 地址：http://HOST:PORT/dt/stream/instances?detail=false
- 方法：GET
- 参数：
    - detail：是否查询DT实例详情，可选

- 返回：

查询DT实例概要列表：
```json
{
	"code": 200,
	"message": "success",
	"data": [
		{
			"id": 5,
			"name": "dsdsdsds测词典"
		}
	]
}
```
- 返回值含义：
    - code：响应状态码
    - message：响应消息
    - data：响应数据
        - id：DT实例ID
        - name：DT实例名称


查询DT实例详情列表：
```json
{
	"code": 200,
	"message": "success",
	"data": [{
		"id": 2,
		"name": "主机实例",
		"attr": [{
				"id": 2,
				"attrtype": "Number",
				"datatype": "Stream",
				"instance": 2,
				"metric": ""
			}
		]
	}]
}
```
- 返回值含义：
    - code：响应状态码
    - message：响应消息
    - data：响应数据
        - id：DT实例ID
        - name：DT实例名称
        - attr：DT属性列表
            - id：属性ID
            - attrtype：属性类型
            - datatype：数据类型
            - metric：属性指标
        

### 查询DT实例详情

- 地址：http://HOST:PORT/dt/stream/instance/{id}
- 方法：GET
- 参数：
    - {id}：DT实例ID

- 返回：
```json
{
	"code": 200,
	"message": "success",
	"data": [
		{
			"id": 9,
			"name": "属性名称",
			"displayname": "显示名称",
			"attrtype": "stream",
			"datatype": "string",
			"unit": "度",
			"instance": 5
		}
	]
}
```
- 返回值含义：
    - code：响应状态码
    - message：响应消息
    - data：响应数据
        - id：DT实例属性ID
        - name：DT实例属性名称
        - displayname：DT实例属性显示名称
        - attrtype：属性类型
        - datatype：数据类型 
        - unit：单位
        - instance：DT实例ID


### 更新物实例属性

- 地址：http://HOST:PORT/dt/stream/instance/attributes
- 方法：POST
- 参数：
```json
[{
  "id": 12,
  "metric": "sensor1",
  "device_id": "device-12-1234",
  "value": 123
}]
```
- 参数含义：
    - id: 物实例属性id
    - metric：物实例属性metric
    - device_id：物实例设备ID
    - value：物实例属性值


- 返回：
```json
{
	"code": 200,
	"message": "success",
	"data": {}
}
```


### 更新物实例锁状态

- 地址：http://HOST:PORT/dt/stream/instance/lockup/{id}?lockup=1
- 方法：PATCH
- 参数：
    - {id}：DT实例ID
    - lockup：锁状态，0:释放锁,1:获取锁

- 返回：
```json
{
	"code": 200,
	"message": "success",
	"data": {}
}
```
- 返回值含义：
    - code：响应状态码
    - message：响应消息
    - data：响应数据，为空