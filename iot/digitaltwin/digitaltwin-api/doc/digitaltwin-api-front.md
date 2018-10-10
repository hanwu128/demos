# DT前端API


## 目录

- [开发环境](#开发环境)
- [定义说明](#定义说明)
    - [单词简写](#单词简写)
    - [格式约定](#格式约定)
    - [业务状态码](#业务状态码)
    - [接口安全](#接口安全)
- [API列表](#api列表)
    - [DT模板管理](#dt模板管理)
        - [查询DT模板列表](#查询dt模板列表)
        - [查询DT模板详情](#查询dt模板详情)
        - [添加DT模板](#添加dt模板)
        - [更新DT模板](#更新dt模板)
        - [删除DT模板](#删除dt模板)
    - [DT实例管理](#dt实例管理)
        - [查询DT实例列表](#查询dt实例列表)
        - [查询DT实例详情](#查询dt实例详情)
        - [添加DT实例](#添加dt实例)
        - [更新DT实例](#更新dt实例)
        - [更新DT实例期望值](#更新dt实例期望值)
        - [删除DT实例](#删除dt实例)
        - [查询DT实例属性数据](#查询dt实例属性数据)
    - [DT名称检查](#dt名称检查)
        - [DT模板名称检查](#dt模板名称检查)
        - [DT实例名称检查](#dt实例名称检查)



## 开发环境

HOST：172.17.200.79
PORT：8080



## 定义说明

### 单词简写

- DT： DigitalTwin简写
- HOST：API服务IP地址或者域名
- PORT：API服务端口


### 格式约定

所有请求的HTTP响应状态码只有一个，值为200。
响应消息统一为如下格式：
```json
{
	"code": 200,
	"message": "success",
	"data": {}
}
```

- code：业务状态码，前端通过该状态码值确定请求是否执行成功。
- message：响应消息
- data：响应数据，json格式


### 业务状态码

| 状态码 | 信息                                            | 详细描述                               |
| ------ | ----------------------------------------------- | -------------------------------------- |
| 200    | success                                         | 请求成功                               |
| 400    | request parameter error                         | 请求参数错误                           |
| 404    | resource not found                              | 请求资源未找到                         |
| 409    | template/instance conflict with name            | 物模板/实例名称冲突                    |
| 419    | template/instance attribute conflict with name  | 物模板/实例属性名称冲突                |
| 420    | template/instance attribute  value format error | 物模板/实例属性value值与数据类型不一致 |
| 500    | server internal error                           | 服务器端执行出错                       |
| 510    | instance be locked                              | 物实例正在被实时数据流使用             |


###  接口安全

为了在一定程度上实现对API接口的安全保护，如：防止数据篡改，防止重放攻击等，需要使用相应的API安全策略。
具体的规约如下：
1. 必须在HTTP请求消息头中携带用户token，格式为：`Authorization: Bearer access_token`。
2. 必须在请求URL参数中携带时间参数，参数名为t，值为当前时间戳（单位：毫秒），如：http://HOST:PORT/path/action.do?t=1528717578000 。
3. 必须在请求URL参数中设置签名参数，参数名为sign，值根据如下规则计算（32位MD5值）：
    - 如果是GET请求，将所有URL参数值一起计算签名，即：sign=MD5(URL Param1 Value + "_" + URL Param2 Value...)
    - 如果是POST请求，将请求消息体参数和所有URL参数值一起计算签名，即：sign=MD5(POST Body + "|" + URL Param1 Value + "." + URL Param2 Value...)



## API列表

### DT模板管理

#### 查询DT模板列表

- 地址：http://HOST:PORT/dt/front/tpl/list.ft
- 方法：POST
- 参数：
```json
{
	"page": 0,
	"offset": 10,
	"name": ""
}
```
- 参数含义：
    - page：分页参数，第几页
    - offset：当前请求记录数
    - name：模板名称，可以使用模板名称过滤查询，为空时查询全部


- 返回：
```json
{
	"code": 200,
	"message": "success",
	"data": {
		"total": 1,
		"pageTotal": 1,
		"offset": 10,
		"page": 0,
		"rows": [
			{
				"id": 1,
				"name": "模板1",
				"desp": "模板1描述",
				"ctime": 1528329600000,
				"mtime": 1528329600000,
				"attrnum": 2,
				"attr": [],
				"istnum": 2
			}
		]
	}
}
```
- 返回值含义：
  - code：响应状态码
  - message：响应消息
  - data：响应数据
        - total: 记录总数
        - pageTotal: 总页数
        - offset: 当前查询记录数
        - page：第几页
        - rows：数据列表
            - id: 模板ID
            - name：模板名称
            - desp：模板描述
            - ctime：创建时间戳
            - mtime：更新时间戳
            - attrnum：属性数量
            - attr：属性列表，为空
            - istnum：继承该模板的实例数量，大于0时模板不允许被编辑


#### 查询DT模板详情

- 地址：http://HOST:PORT/dt/front/tpl/detail.ft
- 方法：POST
- 参数：
```json
{
	"id": 1
}
```
- 参数含义：
    - id：DT模板ID


- 返回：
```json
{
	"code": 200,
	"message": "success",
	"data": {
		"id": 1,
		"name": "模板1",
		"desp": "模板1描述",
		"ctime": 1528329600000,
		"mtime": 1528329600000,
		"attrnum": 2,
		"attr": [
			{
				"id": 1,
				"tpl": 1,
				"name": "模板属性1",
				"displayname": "模板属性显示名称",
				"attrtype": "Stream",
				"datatype": "String",
				"value": "12.12",
				"unit": "度",
				"ctime": 1528329600000,
				"mtime": 1528329600000
			}
		]
	}
}
```
- 返回值含义：
  - code：响应状态码
  - message：响应消息
  - data：响应数据
        - id：DT模板ID
        - name：DT模板名称
        - desp：DT模板描述
        - ctime：DT模板创建时间戳
        - mtime：DT模板修改时间戳
        - attr：DT模板属性列表
            - id：属性ID
            - tpl：DT模板ID
            - name：属性名称
            - displayname：属性显示名称
            - attrtype：属性类型
            - datatype：数据类型
            - value：默认值
            - unit：单位 
            - ctime：属性创建时间戳
            - mtime：属性修改时间戳


#### 添加DT模板

- 地址：http://HOST:PORT/dt/front/tpl/add.ft
- 方法：POST
- 参数：
```json
{
	"name": "模板名称",
	"desp": "模板描述",
	"attr": [
		{
			"name": "属性名称",
			"displayname": "显示名称",
			"attrtype": "属性类型",
			"datatype": "数据类型",
			"value": "默认值",
			"unit": "度"
		}
	]
}
```
- 参数含义：
    - name：DT模板名称
    - desp：DT模板描述
    - attr：DT模板属性列表
    	- name：属性名称
    	- displayname：属性显示名称
    	- attrtype：属性类型
    	- datatype：数据类型
    	- value：默认值
    	- unit：单位


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


#### 更新DT模板

- 地址：http://HOST:PORT/dt/front/tpl/update.ft
- 方法：POST
- 参数：
```json
{
	"id": 1,
	"name": "模板名称",
	"desp": "模板描述",
	"delete": "3",
	"attr": [
		{
			"name": "属性名称",
			"displayname": "显示名称",
			"attrtype": "属性类型",
			"datatype": "数据类型",
			"value": "默认值",
			"unit": "度"
		},
		{
			"id": 1,
			"name": "属性2",
			"displayname": "属性2显示名称",
			"attrtype": "属性类型",
			"datatype": "数据类型",
			"value": "默认值",
			"unit": "度"
		}
	]
}
```
- 参数含义：
	- id：DT模板ID
	- name：DT模板名称
	- desp：DT模板描述
	- delete：需要删除的DT模板属性ID列表，用逗号分隔；不需要删除任何属性时值为空
	- attr：DT模板属性列表
		- id：DT模板属性ID，该值为空时为添加新属性
		- name：属性名称
		- displayname：属性显示名称
		- attrtype：属性类型
		- datatype：数据类型
		- value：默认值
		- unit：属性单位


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



#### 删除DT模板

- 地址：http://HOST:PORT/dt/front/tpl/delete.ft
- 方法：POST
- 参数：
```json
{
	"ids": "1,2"
}
```
- 参数含义：
    - ids：DT模板ID，支持批量删除，用逗号分隔


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



### DT实例管理

#### 查询DT实例列表

- 地址：http://HOST:PORT/dt/front/instance/list.ft
- 方法：POST
- 参数：
```json
{
	"page": 0,
	"offset": 10,
	"name": ""
}
```
- 参数含义：
    - page：分页参数，第几页
    - offset：当前请求页数量
    - name：实例名称，可以使用实例名称过滤查询，为空查询全部

- 返回：
```json
{
	"code": 200,
	"message": "success",
	"data": {
		"total": 1,
		"pageTotal": 1,
		"page": 0,
		"offset": 10,
		"rows": [
			{
				"id": 1,
				"tpl": 1,
				"name": "实例名称",
				"desp": "实例描述",
				"state": 0,
				"ctime": 1528502400000,
				"mtime": 1528502400000,
				"atime": 1529625600000,
				"tplname": "模板名称1",
				"attrnum": 2,
				"attr": []
			}
		]
	}
}
```
- 返回值含义：
	- code：响应状态码
	- message：响应消息
	- data：响应数据
		- total：记录总数
		- pageTotal：总页数
		- page：第几页
		- offset：分页参数，当前查询记录数
		- rows：数据列表
			- id：DT实例ID
			- tpl：DT实例继承的模板ID
			- name：DT实例名称
			- desp：DT实例描述
			- state：DT实例状态，0：离线，1：在线
			- ctime：DT实例创建时间
			- mtime：DT实例修改时间
			- atime：最后一次活跃时间戳
			- tplname：DT实例所继承模板名称
			- attrnum：DT实例属性个数
			- attr：DT实例属性列表，为空


#### 查询DT实例详情

- 地址：http://HOST:PORT/dt/front/instance/detail.ft
- 方法：POST
- 参数：
```json
{
	"id": 3
}
```
- 参数含义：
    - id：DT实例ID

- 返回：
```json
{
	"code": 200,
	"message": "success",
	"data": {
		"id": 3,
		"tpl": 1,
		"name": "实例名称",
		"desp": "实例描述",
		"state": 0,
		"ctime": 1528502400000,
		"mtime": 1528502400000,
		"atime": 1529625600000,
		"tplname": "模板名称1",
		"attrnum": 1,
		"attr": [
			{
				"id": 1,
				"name": "属性名称",
				"displayname": "显示名称",
				"attrtype": "",
				"datatype": "String",
				"value": "",
				"unit": "度",
				"ctime": 1528502400000,
				"mtime": 1528502400000,
				"instance": 3,
				"expectvalue": "",
				"metric": "",
				"deviceid": "",
				"atime": 1528502400000,
				"stime": 1528502400000
			}
		]
	}
}
```
- 返回值含义：
	- code：响应状态码
	- message：响应消息
	- data：响应数据
		- id: DT实例ID
		- tpl：DT实例继承的模板ID
		- name：DT实例名称
		- desp：DT实例描述
		- state：DT实例状态，0：离线，1：在线
		- ctime：DT实例创建时间戳
		- mtime：DT实例修改时间戳
		- atime：DT实例最后一次获取时间戳
		- tplname：DT实例所继承模板名称
		- attrnum：DT实例属性个数
		- attr：DT实例属性列表
			- id：属性ID
			- name：属性名称
			- displayname：显示名称
			- attrtype：属性类型
			- datatype：数据类型
			- value：当前值
			- unit：单位
			- ctime：属性创建时间戳
			- mtime：属性修改时间戳
			- instance：所属DT实例ID
			- expectvalue：期望值
			- metric：指标
			- deviceid：设备ID
			- atime：最后一次活跃时间戳
			- stime：发送时间戳


#### 添加DT实例

- 地址：http://HOST:PORT/dt/front/instance/add.ft
- 方法：POST
- 参数：
```json
{
	"name": "实例名称",
	"desp": "实例描述",
	"tpl": 1
}
```
- 参数含义：
    - name：实例名称
    - desp：实例描述
    - tpl：模板ID

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


#### 更新DT实例
- 地址：http://HOST:PORT/dt/front/instance/update.ft
- 方法：POST
- 参数：
```json
{
	"id": 2,
	"name": "实例修改",
	"desp": "实例修改",
	"tpl": 1
}
```
- 参数含义：
	- id:DT实例的ID
	- name：实例名称
	- desp：实例描述
	- tpl：模板ID


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


#### 更新DT实例期望值

- 地址：http://HOST:PORT/dt/front/instance/update/expectvalue.ft
- 方法：POST
- 参数：
```json
{
	"id": 1,
	"attr": [
		{
			"id": 1,
			"expectvalue": "12.20"
		},
		{
			"id": 2,
			"expectvalue": "12.31"
		}
	]
}
```
- 参数含义：
	- id：DT实例ID
	- attr：DT实例属性列表
		- id：DT实例属性ID
		- expectvalue：DT实例属性期望值

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


#### 删除DT实例

- 地址：http://HOST:PORT/dt/front/instance/delete.ft
- 方法：POST
- 参数：
```json
{
	"ids": "1"
}
```
- 参数含义：
	- ids：DT实例ID，支持批量删除，以逗号分隔


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

#### 查询DT实例属性数据

- 地址：http://HOST:PORT/dt/front/instance/tsdb.ft
- 方法：POST
- 参数：
```json
{
	"id": 124
}
```
- 参数含义：
	- id：DT实例属性ID，

- 返回：
```json
{
	"code": 200,
	"message": "success",
	"data": {
		"id": 9,
		"time": [
			"13:00:00",
			"13:00:01",
			"13:00:03",
			"13:00:04",
			"13:00:07"
		],
		"value": [
			13,
			13,
			12,
			12,
			12
		]
	}
}
```
- 返回值含义：
	- code：响应状态码
	- message：响应消息
	- data：响应数据
		- id：DT实例属性ID
		- time：时间集合
		- value：time对应值集合

### DT名称检查
#### DT模板名称检查

- 地址：http://HOST:PORT/dt/front/check/template.ft
- 方法：POST
- 参数：
```json
{
	"name":"test"
}
```
- 参数含义：
	- name：DT模板名称

- 返回：
```json
{
	"code": 200,
	"message": "success",
	"data": {}
}
```
- 返回值含义：
	- code：响应状态码，名称没有冲突返回200，名称有冲突返回409
	- message：响应消息
	- data：响应数据为空

#### DT实例名称检查

- 地址：http://HOST:PORT/dt/front/check/instance.ft
- 方法：POST
- 参数：
```json
{
	"name":"test"
}
```
- 参数含义：
	- name：DT实例名称

- 返回：
```json
{
	"code": 200,
	"message": "success",
	"data": {}
}
```
- 返回值含义：
	- code：响应状态码，名称没有冲突返回200，名称有冲突返回409
	- message：响应消息
	- data：响应数据为空