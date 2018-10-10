## Digital Twin 前端页面API


编号: 006（废弃）
功能：更新DigitalTwin名称
地址：http://host:port/devicemanager/dt/front/updateDtName.do
方法：POST
参数：
{
	"id": "1",
	"digitaltwinname": "haloword_UPDATE"
}

返回：

成功：
{
    "code": 200,
    "data": {
        "actionFlag": true,
        "actionResult": 1
    },
    "message": "success"
}

失败：
{
	"code": 500,
	"data": "error",
	"message": "error"
}


编号: 005
功能：删除整个DigitalTwin(级联删除)
地址：http://host:port/devicemanager/dt/front/delete.do
方法：POST
参数：
{"id":"1"}

返回：

成功：
{
    "code": 200,
    "data": {
        "actionFlag": true,
        "actionResult": {
            "flagDelete_digitaltwin": 2,
            "flagDelete_digitaltwindaddy": 1
        }
    },
    "message": "success"
}

失败：
{
	"code": 500,
	"data": "error",
	"message": "error"
}


编号: 004
功能：查询DT详情
地址：http://host:port/devicemanager/dt/front/detail.do
方法：POST
参数：
{
	"id": 15  // DT唯一标识
}

返回：

成功：
{
	"code": 200,
	"data": {
		"attrList": [
			{
                "createtimestamp": 1526027723000,
                "daddyid": 15,
                "describemessage": "yyy update",
                "digitaltwinname": "digital twin name update",
                "expectedvalue": 0,
                "id": 29,
                "metric": "metricnamey update",
                "sonname": "yyy update",
                "tagkv": {
                    "tagk1": "tagv3 update",
                    "tagk2": "tagv4 update"
                },
                "unit": "yyy update",
                "updatetimestamp": 1526027723000,
                "value": 0
            },
            {
                "createtimestamp": 1526027723000,
                "daddyid": 15,
                "describemessage": "xxx update",
                "digitaltwinname": "digital twin name update",
                "expectedvalue": 0,
                "id": 28,
                "metric": "metricnamex update",
                "sonname": "xxx update",
                "tagkv": {
                    "tagk1": "tagv1 update",
                    "tagk2": "tagv2 update"
                },
                "unit": "xxx update",
                "updatetimestamp": 1526027723000,
                "value": 0
            }
		],
		"createtimestamp": 1526030807000,
		"describemessage": "xxx update",
		"digitaltwinname": "digital twin name update",
		"id": 15,
		"updatetimestamp": 1526030807000
	},
	"message": "success"
}

失败：
{
	"code": 500,
	"data": "error",
	"message": "error"
}


编号: 003
功能：更新DT
地址：http://host:port/devicemanager/dt/front/update.do
方法：POST
参数：
{
	"id": 15,                                         // DT唯一标识
	"digitaltwinname": "digital twin name update",    // DT名称
	"describemessage": "xxx update",                  // DT描述
	"attrList": [                                     // 属性列表
		{
			"id": 28,                                 // 需要更新的属性ID
			"sonname": "xxx update",                  // 属性名
			"describemessage": "xxx update",          // 属性描述
			"unit": "xxx update",                     // 属性单位
			"metric": "metricnamex update",           // 属性Metric
			"tagkv": {                                // 属性Tag列表
				"tagk1": "tagv1 update",
				"tagk2": "tagv2 update"
			}
		},
		{
			"id": 29,
			"sonname": "yyy update",
			"describemessage": "yyy update",
			"unit": "yyy update",
			"metric": "metricnamey update",
			"tagkv": {
				"tagk1": "tagv3 update",
				"tagk2": "tagv4 update"
			}
		},
		{                                             // 新添加的属性
			"name": "zzz",
			"desc": "zzz",
			"unit": "zz",
			"metric": "metricnamey",
			"tags": {
				"tagk1": "tagv5",
				"tagk2": "tagv6"
			}
		}
	]
}

返回：

成功：
{
	"code": 200,
	"data": "success",
	"message": "success"
}

失败：
{
	"code": 500,
	"data": "error",
	"message": "error"
}


编号: 002
功能：根据名称查询DT列表
地址：http://host:port/devicemanager/dt/front/list.do
方法：POST
参数：
{
	"name": "",                         // DT名称,模糊查询（查询全部时为空）
	"current": 1,                       // 分页参数，第几页
	"pagesize": 10                      // 分页参数，当前页的数量
}
                        

返回：
{
	"code": 200,
	"data": {
		"current": 1,                   // 当前页
		"total": 2,                     // 总数
		"pagesize": 10,                 // 当前页数量
		"rows": [                       // 数据列表
			{
				"createtimestamp": 1526027886000,         // 创建时间
				"describemessage": "xxx",                 // DT描述
				"digitaltwinname": "digital twin name",   // DT名称
				"id": 1,                                  // 数据库ID
				"updatetimestamp": 1526025777000          // 更新时间
			},
			{
				"createtimestamp": 1526027723000,
				"describemessage": "xxx",
				"digitaltwinname": "digital twin name",
				"id": 15,
				"updatetimestamp": 1526027723000
			}
		]
	},
	"message": "success"
}


编号: 001（这个接口废弃，添加，更新DT使用统一的操作：/dt/front/update.do）
功能：添加DT
地址：http://host:port/devicemanager/dt/front/add.do
方法：POST
参数：
{
	"name": "digital twin name",        // DT名称
	"desc": "xxx",                      // DT描述
	"attr": [                           // 属性列表
		{
			"name": "xxx",              // 属性名称
			"desc": "xxx",              // 属性描述
			"unit": "xxx",              // 属性单位
			"metric": "metricnamex",    // 属性Metric
			"tags": {                   // 属性tag列表
				"tagk1": "tagv1",
				"tagk2": "tagv2"
			}
		},
		{
			"name": "yyy",
			"desc": "yyy",
			"unit": "yyy",
			"metric": "metricnamey",
			"tags": {
				"tagk1": "tagv3",
				"tagk2": "tagv4"
			}
		}
	]
}

返回：

成功：
{
	"code": 200,
	"data": "success",
	"message": "success"
}

失败：
{
	"code": 500,
	"data": "error",
	"message": "error"
}


编号: 001（反向控制）
功能：反向控制
地址：http://localhost:8080/CountercontrolController/countercontrolbyid.do
方法：POST
参数：
{
	"id": 1,
	"attr": [
{
		"id": 91,
		"value": 777
	},
	{
		"id": 90,
		"value": 12
	}

]
}

返回：

成功：
{
    "code": 200,
    "data": [
        {
            "result": {
                "createtimestamp": 1527070453000,
                "describemessage": "",
                "digitaltwinname": "0000a模块",
                "id": 91,
                "metric": "iot.service.test",
                "tagkv": "{\"lable\":\"humi\",\"deviceId\":\"10000100000B9A150F9A9405D5F60664\"}",
                "updatetimestamp": 1527070453000
            },
            "sendMQTTFlag": true
        },
        {
            "result": {
                "createtimestamp": 1527070453000,
                "describemessage": "",
                "digitaltwinname": "0000a模块",
                "id": 90,
                "metric": "iot.service.test",
                "tagkv": "{\"lable\":\"temp\",\"deviceId\":\"100001000008EB84BC3ACB10E9964891\"}",
                "updatetimestamp": 1527070453000
            },
            "sendMQTTFlag": false
        }
    ],
    "message": "success"
}
失败：
{
	"code": 500,
	"data": "error",
	"message": "error"
}




















