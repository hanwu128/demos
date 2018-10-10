# 数字镜像开放API


## 目录

- [定义说明](#定义说明)
    - [单词简写](#单词简写)
    - [响应格式](#响应格式)
    - [业务状态码](#业务状态码)
    - [接口安全](#接口安全)
    - [版本管理](#版本管理)
- [API列表](#api列表)
    - [物实例管理](#物实例管理)
        - [查询物实例列表](#查询物实例列表)
        - [查询物实例详情](#查询物实例详情)
        - [设备反控](#设备反控)
    - [物实例属性管理](#物实例属性管理)
        - [查询物实例属性数据](#查询物实例属性数据)


## 定义说明

### 单词简写

- DT： DigitalTwin简写
- HOST：API服务IP地址或者域名
- PORT：API服务端口


### 响应格式

所有接口均为RESTFul API，响应成功消息统一为如下JSON格式：
```json
{
	"code": 200,
	"message": "success",
	"data": {}
}
```

- code：业务状态码
- message：响应消息
- data：响应数据，JSON对象


### 业务状态码

| 业务码 |     描述     |    响应消息     |
| ------ | ------------ | --------------- |
| 200    | 请求成功     | success         |
| 400    | 参数错误     | paramater error |
| 404    | 资源不存在   | not found       |
| 500    | 服务器端报错 | error           |


###  接口安全

无


### 版本管理

所有API接口在URL中都具有对应的版本标识，当前版本：v1。


## API列表

### 物实例管理

#### 查询物实例列表

**功能描述**

批量查询物实例列表，支持分页。

**请求地址**

| 方法  | URL                                                       | 描述      |
|-----|-----------------------------------------------------------|---------|
| GET | http://HOST:PORT/dt/open/v1/instances?name=&page=1&offset=10 | 查询物实例列表 |

**请求参数**

| 参数   | 类型   | 必填 | 最大长度  | 描述                                           | 示例值 |
| ------ | ------ | ---- | --------- | ---------------------------------------------- | ------ |
| name   | String | 否   | 128个字符 | 实例名称，为空时查询所有，不为空按名称过滤查询 | 实例   |
| page   | Number | 否   | 4字节     | 分页参数，表示查询第几页，默认值为1            | 1      |
| offset | Number | 否   | 4字节     | 分页参数，表示当前请求页数量，默认返回数量为10 | 10     |

**响应参数**

| 参数      | 类型   | 描述                     | 示例值        |
| --------- | ------ | ------------------------ | ------------- |
| total     | Number | 记录总数                 | 1             |
| pageTotal | Number | 总页数                   | 1             |
| page      | Number | 第几页                   | 1             |
| offset    | Number | 当前查询记录数           | 10            |
| rows      | JSON   | 数据列表，JSON数组       | -             |
| - id      | Number | 物实例ID                 | 1             |
| - tpl     | Number | 物实例继承的物模板ID     | 1             |
| - name    | String | 物实例名称               | 实例名称      |
| - desp    | String | 物实例描述               | 实例描述      |
| - ctime   | Number | 物实例创建时间戳         | 1528502400000 |
| - mtime   | Number | 物实例更新时间戳         | 1528502400000 |
| - atime   | Number | 物实例最后一次活跃时间戳 | 1529625600000 |
| - tplname | String | 物实例继承的物模板名称   | 模板名称1     |
| - attrnum | Number | 物实例属性个数           | 2             |

**请求示例**

GET  http://HOST:PORT/dt/open/v1/instances?name=实例&page=1&offset=10

**响应示例**

```json
{
	"code": 200,
	"message": "success",
	"data": {
		"total": 1,
		"pageTotal": 1,
		"page": 1,
		"offset": 5,
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
				"attrnum": 2
			}
		]
	}
}
```

#### 查询物实例详情

**功能描述**

根据ID查询物实例详细参数，支持批量查询。

**请求地址**

| 方法 | URL                                       | 描述           |
| ---- | ----------------------------------------- | -------------- |
| GET  | http://HOST:PORT/dt/open/v1/instance?ids=1,2 | 查询物实例详情 |

**请求参数**

| 参数  | 类型     | 必填  | 最大长度 | 描述    | 示例值 |
|-----|--------|-----|------|-------|-----|
| ids  | String | 是   | 8字节  | 物实例ID，支持批量查询，ID之间用逗号隔开 | 1,2  |

**响应参数**

| 参数          | 类型   | 描述                                                         | 示例值        |
| ------------- | ------ | ------------------------------------------------------------ | ------------- |
| id            | Number | 物实例ID                                                     | 1             |
| tpl           | Number | 物实例继承的物模板ID                                         | 1             |
| name          | String | 物实例名称                                                   | 实例名称      |
| desp          | String | 物实例描述                                                   | 实例描述      |
| ctime         | Number | 物实例创建时间戳                                             | 1528502400000 |
| mtime         | Number | 物实例更新时间戳                                             | 1528502400000 |
| atime         | Number | 物实例最后一次活跃时间戳                                     | 1529625600000 |
| tplname       | String | 物实例继承的物模板名称                                       | 模板名称      |
| attrnum       | Number | 物实例属性个数                                               | 2             |
| attr          | JSON   | 物实例属性列表，JSON数组                                     | -             |
| - id          | Number | 属性ID                                                       | 1             |
| - name        | String | 属性名称                                                     | 属性名称      |
| - displayname | String | 显示名称                                                     | 显示名称      |
| - attrtype    | String | 属性类型，可选值：State，Description，Stream                 | State         |
| - datatype    | String | 数据类型，可选值：String，Boolean，Number，Json Object，Json Array | String        |
| - value       | String | 当前值                                                       | 0.00          |
| - unit        | String | 单位                                                         | 度            |
| - ctime       | Number | 创建时间戳                                                   | 1528502400000 |
| - mtime       | Number | 更新时间戳                                                   | 1528502400000 |
| - instance    | Number | 所属物实例ID                                                 | 3             |
| - expectvalue | String | 期望值                                                       | 20.12         |
| - metric      | String | 指标                                                         | cpu.usage     |
| - deviceid    | Number | 设备ID                                                       | device-123456 |
| - atime       | Number | 最后一次活跃时间戳                                           | 1528502400000 |
| - stime       | Number | 发送时间戳                                                   | 1528502400000 |


**请求示例**

GET  http://HOST:PORT/dt/open/v1/instance?ids=1

**响应示例**

```json
{
	"code": 200,
	"message": "success",
	"data": [{
		"id": 1,
		"tpl": 1,
		"name": "计算机",
		"desp": "计算机实例",
		"ctime": 1534143917000,
		"mtime": 1534143917000,
		"atime": 0,
		"tplname": "chench9_test",
		"attrnum": 3,
		"attr": [{
			"id": 1,
			"name": "humidity",
			"displayname": "湿度",
			"attrtype": "Stream",
			"datatype": "Number",
			"value": "",
			"unit": "",
			"ctime": 1534143917000,
			"mtime": 1534151655000,
			"instance": 1,
			"metric": "humidity",
			"deviceid": "device-12312-14",
			"atime": 0,
			"stime": 0
		}]
	}]
}
```

#### 设备反控

**功能描述**

设置物实例属性期望值，以此反向控制设备属性值（**注：** 只有State类型的物实例属性允许设置期望值）。

**请求地址**

| 方法  | URL                                    | 描述     |
| ----- | -------------------------------------- | -------- |
| PATCH | http://HOST:PORT/dt/open/v1/instance/{id} | 反控设备 |

**请求参数**

- 路径参数

| 参数 | 类型   | 必填 | 最大长度 | 描述     | 示例值 |
| ---- | ------ | ---- | -------- | -------- | ------ |
| id   | Number | 是   | 8字节    | 物实例ID | 1      |

- 消息体参数

| 参数 | 类型   | 必填 | 最大长度 | 描述     | 示例值 |
| ---- | ------ | ---- | -------- | -------- | ------ |
| id   | Number | 是   | 8字节    | 物实例属性ID | 1      |
| expectvalue | String | 是  | - | 物实例属性期望值 | "12.03" |


**响应参数**

无


**请求示例**

PATCH  http://HOST:PORT/dt/open/v1/instance/2
```json
[{
		"id": 2,
		"expectvalue": "12.20"
	},
	{
		"id": 3,
		"expectvalue": "12.31"
	}
]
```

**响应示例**

```json
{
	"code": 200,
	"message": "success",
	"data": {}
}
```


### 物实例属性管理

#### 查询物实例属性数据

**功能描述**

查询物实例属性值，若查询的属性类型为State类型或者Description类型，则返回该属性对应的字符串值；如果查询的属性类型为Stream类型，则返回该属性的时序数据列表。

**请求地址**

| 方法 | URL                                                          | 描述                        |
| ---- | ------------------------------------------------------------ | --------------------------- |
| GET  | http://HOST:PORT/dt/open/v1/instance/attribute/{id}          | 查询State类型属性数据       |
| GET  | http://HOST:PORT/dt/open/v1/instance/attribute/{id}          | 查询Description类型属性数据 |
| GET  | http://HOST:PORT/dt/open/v1/instance/attribute/{id}?start=1530845483000&end=1530845543000 &downsample=10m-count&aggregator=avg | 查询Stream类型属性数据      |

**请求参数**

| 参数       | 类型           | 必填                         | 最大长度 | 描述                                                         | 示例值                 |
| ---------- | -------------- | ---------------------------- | -------- | ------------------------------------------------------------ | ---------------------- |
| id         | Number         | 是                           | 8字节    | 物实例属性ID                                                 | 15                     |
| start      | Number或String | 查询Stream类型属性数据时必填 | 8字节    | 起始时间，查询结果包含该时间的值，时间格式请参见“时间格式说明” | 1530845483000 或1h-ago |
| end        | Number或String | 可选，时间格式与start一致    | 8字节    | 结束时间，查询结果包含该时间的值                             | 1530845483000或1h-ago  |
| downsample | String         | 可选                         | 8字节    | 采样精度，参见“downsample说明”                               | 5m-avg                 |
| aggregator | String         | 可选                         | 8字节    | 聚合函数名称，参见“aggregator说明”                           | sum                    |

**时间格式说明**
**相对格式化时间**
`<amount><time unit>-ago`
`<amount>` 表示时间值，`<time unit>`表示时间单位，比如1h-ago表示从现在开始的一小时之前。时间单位包括：
- ms - Milliseconds
- s - Seconds
- m - Minutes
- h - Hours
- d - Days (24 hours)
- w - Weeks (7 days)
- n - Months (30 days)
- y - Years (365 days)

***绝对Unix时间***
Unix格式的时间戳，支持秒或毫秒时间戳，比如1364410924表示 ISO 8601:2013-03-27T19:02:04Z。

***绝对格式化时间***
支持以下格式的时间：
- yyyy/MM/dd-HH:mm:ss
- yyyy/MM/dd HH:mm:ss
- yyyy/MM/dd-HH:mm
- yyyy/MM/dd HH:mm
- yyyy/MM/dd

***aggregator说明***

Aggregator可在聚合多个时序和降低采样downsampling时使用，通过算子将多个数据点汇聚成一个数据点。 聚合算子请参见下表： 

| 算子    | 描述                        | 补值方式 |
| ------- | --------------------------- | -------- |
| avg     | 平均值                      | 线性插值 |
| count   | 数据点数                    | 补0      |
| dev     | 计算标准偏差                | 线性插值 |
| ep50r3  | 用R-3方法计算的第50百分位   | 线性插值 |
| ep50r7  | 用R-7方法计算的第70百分位   | 线性插值 |
| ep75r3  | 用R-3方法计算的第75百分位   | 线性插值 |
| ep75r7  | 用R-7方法计算的第75百分位   | 线性插值 |
| ep90r3  | 用R-3方法计算的第90百分位   | 线性插值 |
| ep90r7  | 用R-7方法计算的第90百分位   | 线性插值 |
| ep95r3  | 用R-3方法计算的第95百分位   | 线性插值 |
| ep95r7  | 用R-7方法计算的第95百分位   | 线性插值 |
| ep99r3  | 用R-3方法计算的第99百分位   | 线性插值 |
| ep99r7  | 用R-7方法计算的第99百分位   | 线性插值 |
| ep999r3 | 用R-3方法计算的第99.9百分位 | 线性插值 |
| ep999r7 | 用R-7方法计算的第99.9百分位 | 线性插值 |
| first   | 取第一个值                  | -        |
| last    | 取最后一个值                | -        |
| mimmin  | 最小值                      | 补最大值 |
| mimmax  | 最大值                      | 补最小值 |
| min     | 最小值                      | 线性插值 |
| max     | 最大值                      | 线性插值 |
| none    | 不做计算                    | 补0      |
| p50     | 第50百分位                  | 线性插值 |
| p75     | 第75百分位                  | 线性插值 |
| p90     | 第90百分位                  | 线性插值 |
| p95     | 第95百分位                  | 线性插值 |
| p99     | 第99百分位                  | 线性插值 |
| p999    | 第99.9百分位                | 线性插值 |
| sum     | 求和                        | 线性插值 |
| zimsum  | 求和                        | 补0      |

***downsample说明***
当查询的时间跨度很大，例如每秒都将温度作为数据写入TSDB，每个小时会产生3600条数据点，当查询一周的数据时，会返回604800个数据点。展示如此多数据会显得很乱，通常也不需要这样精确的数据。使用降精度的方式将一段时间的数据点聚合后当作一个数据点，比如将每个小时的数据聚合为1个数据点，这样就会只显示168个数据点。
格式：
`<Interval><units>-<aggregator>[c][-]`
- Interval：时间数值；unit：时间单位，s（秒），m（分），h（小时)，d（天）。
- aggregator：聚合策略，将一段时间内的数据点集聚合为一个数据点的策略，具体取值请参见“聚合算子”。
- c：表示使用时区日历采样，可选项。此参数需要和timezone 参数配合使用，例如：&timezone=Asia/Kabul
- fill policy：补值策略，可选项。当使用aggreator计算一段时间内的聚合值时，遇到中间缺少的数据点时，会使用一定的策略补充数据。补值策略如下表所示：

| 名称 | 描述         |
| ---- | ------------ |
| none | 默认，不补值 |
| nan  | 补NaN        |
| null | 补null       |
| zero | 补0          |
- 示例
  1h-sum
  30m-avg-nan
  24h-max-zero
  

**公共响应参数**

| 参数        | 类型   | 描述               | 示例值        |
| ----------- | ------ | ------------------ | ------------- |
| id          | Number | 属性ID             | 1             |
| name        | String | 属性名称           | 属性名称      |
| displayname | String | 属性显示名称       | 显示名称      |
| attrtype    | String | 属性类型           | State         |
| datatype    | String | 数据类型           | String        |
| value       | Object | 当前值             | 12.23         |
| unit        | String | 单位               | 度            |
| ctime       | Number | 创建时间戳         | 1528502400000 |
| mtime       | Number | 更新时间戳         | 1528502400000 |
| instance    | Number | 所属物实例ID       | 3             |
| expectvalue | String | 期望值             | 12.32         |
| metric      | String | 指标               | cpu.usage     |
| deviceid    | Number | 设备ID             | 3             |
| atime       | Number | 最后一次活跃时间戳 | 1528502400000 |
| stime       | Number | 发送时间戳         | 1528502400000 |

**特别说明：** 查询State类型或者Description类型的属性数据时，value值为字符串；查询Stream类型的属性数据时，value值为JSON数组，字段含义如下表所示。

| 参数   | 类型   | 描述                                 | 示例值       |
| ------ | ------ | ------------------------------------ | ------------ |
| metric | String | 指标名称                             | sys.cpu.nice |
| tags   | JSON   | tags列表，JSON数组                   | -            |
| dps    | JSON   | 时序数据集合，数据点由时间戳和值组成 | -            |

**请求示例**

**查询State类型或者Description类型数据**

GET  http://HOST:PORT/dt/open/v1/instance/attribute/{id}

**响应实例**

```json
{
	"code": 200,
	"message": "success",
	"data": {
		"id": 15,
		"name": "属性名称",
		"displayname": "显示名称",
		"attrtype": "state",
		"datatype": "string",
		"value": "",
		"unit": "度",
		"ctime": 1529971200000,
		"mtime": 1529971200000,
		"instance": 8,
		"expectvalue": "",
		"metric": "",
		"deviceid": "",
		"atime": 1529971200000,
		"stime": 1529971200000
	}
}
```

**查询Stream类型数据**

- 时间参数为绝对时间戳数字

GET  http://HOST:PORT/dt/open/v1/instance/attribute/1?start=1534147200&downsample=10m-count&aggregator=avg

- 时间参数为相对格式化时间

GET  http://HOST:PORT/dt/open/v1/instance/attribute/1?start=2h-ago&downsample=15m-count&aggregator=avg

- 时间参数为绝对格式化时间

GET  http://HOST:PORT/dt/open/v1/instance/attribute/1?start=2018/08/13&downsample=15m-count&aggregator=avg

**响应示例**

```json
{
	"code": 200,
	"message": "success",
	"data": {
		"id": 1,
		"name": "humidity",
		"displayname": "湿度",
		"attrtype": "Stream",
		"datatype": "Number",
		"value": [{
			"metric": "humidity",
			"dps": {
				"1534147200": 19,
				"1534147260": 20,
				"1534147320": 20,
				"1534147380": 20
			},
			"tags": {
				"device_id": "device-12312-14",
				"dt_name": "计算机"
			}
		}],
		"unit": "",
		"ctime": 1534143917000,
		"mtime": 1534151655000,
		"instance": 1,
		"expectvalue": "",
		"metric": "humidity",
		"deviceid": "device-12312-14",
		"atime": 0,
		"stime": 0
	}
}

```