## Digital Twin LEAP Stream API




编号：001
功能：更新Digital Twin属性值（leap stream调用）
地址：http://host:port/devicemanager/DigitalTwinStreamSetHandlerController/update_value_dt.do
方法：POST
参数：
[{
	"timestamp": 1523848208232,
	"profileVersion": 1,
	"value": 22.222,
	"metric": "temperature",
	"tags": {
		"deviceId": "dvid001",
		"tagk1": "tagv1"
	}
}]


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

