# 前端调试API汇总
host: 10.100.156.81
port: 8080

测试账户信息：
一级账号/密码：chench9/111111
二级账号/密码：test/111111



##编号: 014
功能：获取邮箱验证码
地址：http://localhost:8080/devicemanager/authcode/code.do
方法：POST
参数：
{
	"loginName": "chench9" // 账户登录名
}

返回：
{
	"code": 200, // 发送验证码成功
	"data": "success",
	"message": "success"
}

{
	"code": 404, // 账户不存在
	"data": "account not found",
	"message": "error"
}

{
	"code": 500, // 发送验证码失败
	"data": "error",
	"message": "error"
}



##编号: 013
功能：退出登录
地址：http://localhost:8080/devicemanager/account/logout.do
方法：POST
参数：
无

返回：
{
	"code": 200, // 退出登录成功
	"data": "success",
	"message": "success"
}

{
	"code": 420, // 管理员未登录
	"data": "account not logined",
	"message": "error"
}


##编号: 012
功能：账号管理-删除账号
地址：http://localhost:8080/devicemanager/account/delete.do
方法：POST
参数：
loginName=chench9 // 账户登录名

返回：
{
	"code": 200, // 删除成功
	"data": "success",
	"message": "success"
}

{
	"code": 420, // 管理员未登录
	"data": "account not logined",
	"message": "error"
}

{
	"code": 500, // 删除失败
	"data": "error",
	"message": "error"
}

##编号: 011
功能：账号管理-设置账户权限
地址：http://localhost:8080/devicemanager/account/permissionsetting.do
方法：POST
参数：
loginName=chench9 // 账户登录名
permission=datastream_manage // 权限数组
permission=devmirror_manage

返回：
{
	"code": 200, // 设置成功
	"data": "success",
	"message": "success"
}

{
	"code": 400, // 权限未空
	"data": "permission is empty",
	"message": "error"
}

{
	"code": 420, // 管理员未登录
	"data": "account not logined",
	"message": "error"
}

{
	"code": 500, // 设置失败
	"data": "error",
	"message": "error"
}


##编号: 010
功能：账号管理-查询账户权限列表
地址：http://localhost:8080/devicemanager/account/permission.do?loginName=guest
方法：GET
参数：
loginName=chench9 // 账户登录名

返回：
{
	"code": 200, // 成功获取指定账户权限列表
	"data": [
		"company:manage",
		"information:manage",
		"edge:analyse"
	],
	"message": "success"
}

{
	"code": 420, // 管理员未登录
	"data": "account not logined",
	"message": "error"
}


##编号: 009
功能：账号管理-激活/解冻账户
地址：http://localhost:8080/devicemanager/account/enabled.do
方法：POST
参数：
loginName=chench9 // 登录名，账号
enable=0 // 0:冻结账户, 1:解冻账户

返回：
{
	"code": 200, // 操作成功
	"data": "success",
	"message": "success"
}

{
	"code": 420, // 管理员未登录
	"data": "account not logined",
	"message": "error"
}

{
	"code": 500, // 操作失败
	"data": "error",
	"message": "error"
}


##编号: 008
功能：账号管理-添加账号
地址：http://localhost:8080/devicemanager/account/add.do
方法：POST
参数：
loginName=chench9 // 登录名，账号
password=96e79218965eb72c92a549dd5a330112 // 密码,md5加密
name=陈昌辉 // 姓名

返回：
{
	"code": 200, // 添加成功
	"data": "success",
	"message": "success"
}

{
	"code": 409, // 账户已经存在
	"data": "login name exists",
	"message": "error"
}

{
	"code": 420, // 管理员未登录
	"data": "account not logined",
	"message": "error"
}




##编号: 007
功能：账号管理-账号列表
地址：http://localhost:8080/devicemanager/account/list.do?start=0&offset=50
方法：GET
参数：
start=0  // 分页参数，表示当前需要从第几个数据开始
offset=50 // 分页参数，表示当前需要从取多少数据

返回：
{
	"code": 200, // 成功获取到账户列表
	"data": {
		"total": 3,
		"list": [
			{
				"companyId": 100000,
				"createTime": 1510214667000,
				"email": "guest@lenovo.com",
				"enable": 1,
				"id": 4,
				"loginName": "guest",
				"name": "陈昌辉_2",
				"password": "",
				"phone": "13241470086",
				"updateTime": 1510214667000
			},
			{
				"companyId": 100000,
				"createTime": 1510213666000,
				"email": "chench9@lenovo.com",
				"enable": 1,
				"id": 1,
				"loginName": "chench9",
				"name": "陈昌辉_2",
				"password": "",
				"phone": "13241470086",
				"updateTime": 1510213666000
			},
			{
				"companyId": 100000,
				"createTime": 1510213666000,
				"email": "test@lenovo.com",
				"enable": 1,
				"id": 2,
				"loginName": "test",
				"name": "测试账户",
				"password": "",
				"phone": "",
				"updateTime": 1510213666000
			}
		]
	},
	"message": "success"
}

{
	"code": 420, // 账户未登录
	"data": "account not logined",
	"message": "error"
}


##编号: 006
功能：编辑个人资料
地址：http://localhost:8080/devicemanager/account/metasetting.do
方法：POST
参数：
name=陈昌辉 // 姓名
phone=13241470086 // 联系方式

返回：
{
	"code": 200, // 编辑成功
	"data": "success",
	"message": "success"
}

{
	"code": 420, // 账户未登录
	"data": "account not logined",
	"message": "error"
}

{
	"code": 500, // 编辑失败
	"data": "error",
	"message": "error"
}


##编号: 005
功能：查看个人资料
地址：http://localhost:8080/devicemanager/account/meta.do
方法：GET
参数：
无

返回：
{
	"code": 200, // 返回账号信息
	"data": {
		"companyId": 100000,
		"createTime": 1510107503000,
		"email": "chench9@lenovo.com",
		"enable": 1,
		"loginName": "chench9",
		"name": "陈昌辉",
		"phone": "",
		"updateTime": 1510107503000
	},
	"message": "success"
}

{
	"code": 420, // 账户未登录
	"data": "account not logined",
	"message": "error"
}



##编号: 004
功能：重置密码-执行重置密码
地址：http://localhost:8080/devicemanager/account/resetpassword/step2.do
方法：POST
参数：
loginName=chench9 // 登录名
authCode=123 // 验证码
password=96e79218965eb72c92a549dd5a330112 // 密码，md5编码

返回：
{
	"code": 200, // 重置密码成功
	"data": "success",
	"message": "success"
}

{
	"code": 404, // 账户不存在
	"data": "account not found",
	"message": "error"
}

{
	"code": 418, // 验证码不正确
	"data": "auth code error",
	"message": "error"
}

{
	"code": 419, // 密码为空
	"data": "password is empty",
	"message": "error"
}

{
	"code": 420, // 账户未登录
	"data": "account not logined",
	"message": "error"
}

{
	"code": 500, // 重置密码失败
	"data": "success",
	"message": "success"
}


##编号: 003
功能：重置密码-验证账号和验证码
地址：http://localhost:8080/devicemanager/account/resetpassword/step1.do
方法：POST
参数：
loginName=chench9 // 登录名
authCode=123 // 验证码

返回：
{
	"code": 200, // 验证成功
	"data": "success",
	"message": "success"
}

{
	"code": 404, // 账户不存在
	"data": "account not found",
	"message": "error"
}

{
	"code": 418, // 验证码不正确
	"data": "auth code error",
	"message": "error"
}

{
	"code": 420, // 账户未登录
	"data": "account not logined",
	"message": "error"
}


##编号: 002
功能：登录验证码
地址：http://localhost:8080/devicemanager/authcode/image.url
方法：GET
参数：
无

返回：
返回验证码图片二进制数据，直接在前端通过`<img>`标签引用即可。
例如：<img alt="验证码" id="imagecode" src="http://localhost:8080/devicemanager/authcode/image.url"/>。
为了实现动态刷新验证码，可以在请求url之后动态添加参数，例如：可以动态变化时间戳参数。


##编号: 001
功能：管理员账户登录
地址：http://localhost:8080/devicemanager/account/login.do
方法：POST
参数：
loginName=chench9 // 登录名
password=96e79218965eb72c92a549dd5a330112 // 密码,md5加密
authCode=123 // 等验证码，改参数不是必须的，当需要输入的时候才携带该参数

返回：
{
	"code": 200, // 登录成功
	"data": {
		"permission": [ // 用户权限列表
			"edge:analyse", // 边缘分析
			"edge:manage",  // edge管理
			"datastream:manage", // 数据流管理
			"devmirror:manage", // 镜像管理
			"procmodule:manage", // 处理模块
			"information:manage", // 信息管理
			"company:information", // 企业信息
			"company:setting", // 企业信息设置
			"account:manage" // 账户管理
		],
		"account": {
			"companyId": 100000,
			"createTime": 1510107503000,
			"email": "chench9@lenovo.com",
			"enable": 1,
			"loginName": "chench9",
			"name": "陈昌辉",
			"phone": "",
			"updateTime": 1510107503000
		}
	},
	"message": "success"
}

{
	"code": 404, // 用户名不正确
	"data": "account not found",
	"message": "error"
}

{
	"code": 424, // 账户被冻结
	"data": "account has been disabled",
	"message": "error"
}

{
	"code": 418, // 验证码错误
	"data": "auth code error",
	"message": "error"
}

{
	"code": 421, // 密码错误
	"data": "account credentials incorrect",
	"message": "error"
}

{
	"code": 500, // 未知错误
	"data": "error",
	"message": "error"
}
