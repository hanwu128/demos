/**********************
* 统一存储接口,使用模块名+接口名
* @version v1.0.0
* @author:Mad
* @QQ:1823737636
* @date:2017-9-21
* @edit:2017-12-13
**********************/
const DEBUG = true
// let BASE_URL = DEBUG ? 'http://223.203.218.93' : '';//产线环境
//let BASE_URL = DEBUG ? 'http://10.100.157.19:8080' : '';
//let BASE_URL = DEBUG ? 'http://172.17.199.109:8080' : '';//测试环境
//let BASE_URL = DEBUG ? 'http://10.100.157.34:8080' : '';//本地环境
//let LOGIN_URL = DEBUG ? 'http://10.100.156.81:8080' : '';
let BASE_URL = DEBUG ? 'http://172.17.171.17:8080' : '';
const API = {
    //版本号控制
    'version':'版本号：0.8.XX21',//版本
    //权限名称
    'permission':{
        "edge_analyse":"edge:analyse",// 边缘分析
        "edge_manage":"edge:manage",// edge管理
        "datastream_manage":"datastream:manage",// 数据流管理
        "devmirror_manage":"devmirror:manage",// 镜像管理
        "procmodule_manage":"procmodule:manage",// 处理模块
        "information_manage":"information:manage",// 信息管理
        "company_information":"company:information",//企业信息
        "company_setting":"company:setting",// 企业信息设置
        "account_manage":"account:manage"//账户管理
    },
    //登录
    'login':{
        'imgCode':BASE_URL + '/devicemanager/authcode/image.url',//验证码
        'accountLogin' : BASE_URL + '/devicemanager/account/login.do',//登录接口
        'logOut': BASE_URL + '/devicemanager/account/logout.do'//登出接口
    },
    //信息管理
    'infoManage':{
        'personalData':BASE_URL + '/devicemanager/account/meta.do',//查看个人资料，get请求
        'editPersonalData' : BASE_URL + '/devicemanager/account/metasetting.do',//编辑个人资料，post请求
        'getCode': BASE_URL + '/devicemanager/authcode/code.do',//获取邮箱验证码，get请求
        'resetCheckPwd': BASE_URL + '/devicemanager/account/resetpassword/step1.do',//验证验证码，post请求
        'resetPwd': BASE_URL + '/devicemanager/account/resetpassword/step2.do',//重置密码，post请求
        //账号管理
        'accountList': BASE_URL + '/devicemanager/account/list.do',//账号列表，get请求
        'accountEnabled': BASE_URL + '/devicemanager/account/enabled.do',//启动，暂停
        'accountDelete': BASE_URL + '/devicemanager/account/delete.do',//删除
        'accountAdd': BASE_URL + '/devicemanager/account/add.do',//添加账户
        'setPermission': BASE_URL + '/devicemanager/account/permissionsetting.do',//设置权限
        'searchPermission': BASE_URL + '/devicemanager/account/permission.do',//查询权限
        //企业信息
        'getInfo': BASE_URL + '/devicemanager/company/get.do',//查询信息
        'updateInfo': BASE_URL + '/devicemanager/company/update.do'//更新信息
    },
    //网关设备管理
    'edgeManage':{
        'deviceList':BASE_URL + '/devicemanager/device/list.do',//网关设备列表
        'deviceDelete':BASE_URL + '/devicemanager/device/delete.do',//网关设备删除
        'deviceAdd':BASE_URL + '/devicemanager/device/add.do',//创建网关设备
        'detailProcess':BASE_URL + '/devicemanager/device/get.do',//详情处理模块信息
        'detailSoftUpdate':BASE_URL + '/devicemanager/TaskController/findTasklistbyparameters.do',//详情软件更新信息
        'detailData':BASE_URL + '/devicemanager/device/get_message_statistics.do',//详情数据量
        'detailStatusHistory':BASE_URL + '/devicemanager/device/online_history.do',//详情状态历史
        'detailRun':BASE_URL + '/devicemanager/TaskController/run_app.do',//详情启动暂停
        'detailKeyUpdate':BASE_URL + '/devicemanager/device/update_secret.do',//详情更新秘钥
        'detailProcessDialog':BASE_URL + '/devicemanager/StreamingPipeLineController/findStreamingDeviceAppVOBaseById.do',//处理模块信息详情
        'deviceReboot':BASE_URL + '/devicemanager/device/reboot.do',//设备重启，post请求，参数device_id
        'deviceUpload':BASE_URL + '/devicemanager/device/add_batch_check.do',//设备批量导入上传，post请求
        'deviceImportSave':BASE_URL +'/devicemanager/device/add_batch.do',//设备批量导入保存，post请求
        'deviceBatchTemplate':BASE_URL +'/devicemanager/file/device_batch_template.xls',//下载模板
        'deviceGetVersion':BASE_URL +'/devicemanager/device/list_version.do'//获取版本,get请求
    },
  //流计算
  'edgeljs': {
        'ljsurl': 'http://172.17.171.16:18630/collector/home/iframe'
  },
  //数据洞察
  'edgesjdc': {
    'sjdc': 'http://172.17.171.14:3000/dashboards'
  },
  //设备镜像管理
  'edgeImg':{
    'devicedetaiList':BASE_URL + '/devicemanager/dt/front/list.do',//设备镜像列表
    'devicedetailDelete':BASE_URL + '/devicemanager/dt/front/delete.do', //删除设备镜像列表
    'devicedetail': BASE_URL + '/devicemanager/dt/front/detail.do',//设备镜像详情列表
    'deviceSave': BASE_URL + '/devicemanager/dt/front/update.do',//设备镜像列表保存
    'deviceSxzb': BASE_URL + '/devicemanager/tsdb/suggest.do'//设备镜像属性指标
  },
    //数据流管理
    'pipeLineManage':{
        'pipeLineList':BASE_URL + '/devicemanager/StreamingPipeLineController/findStreamingDeviceAppVOBaseByParameters.do',//数据流列表
        'pipeLineDelete':BASE_URL + '/devicemanager/StreamingPipeLineController/deleteStreaming.do',//数据流删除
        'pipeLineDetail':BASE_URL + '/devicemanager/StreamingPipeLineController/findStreamingDeviceAppVOBaseById.do',//数据流详情
        'createDeviceId':BASE_URL + '/devicemanager/device/list2.do',//数据流创建获取网关设备id
        'createFindAllTask':BASE_URL + '/devicemanager/TaskController/listapps.do',//数据流创建获取属性列表
        'createDescApp':BASE_URL + '/devicemanager/TaskController/descApp.do',//数据流创建获取处理模块
        'createPipeLine':BASE_URL + '/devicemanager/StreamingPipeLineController/nurturedStreamingPipeLine.do',//数据流创建
        'createRepeatCheck':BASE_URL + '/devicemanager/StreamingPipeLineController/findStreamingDeviceAppVOBaseByParameters.do',//数据流创建重复判断
        'pipeLineDataNum':BASE_URL + '/devicemanager/device/get_message_statistics.do',//数据发送量,get请求,参数device_id和app_name
        'pipeLineChoice':BASE_URL + '/devicemanager/device/get_applist.do',//选择处理模块,post请求,参数device_id和app_type
        'pipeLineDataNumDetail':BASE_URL + '/devicemanager/device/get_message_list.do'//选择处理模块,post请求,参数device_id,app_name,pagesize,current,sort_orderby,create_stamp,sort_rule;

    },
    //设备镜像管理
    'shadowManage':{
        'shadowList':BASE_URL + '/devicemanager/deviceshadow/list.do',//设备镜像列表
        'configList':BASE_URL + '/devicemanager/deviceshadow/get.do',//设备镜像配置列表,详情
        'configSave':BASE_URL + '/devicemanager/deviceshadow/update_item_expectvalue.do',//设备镜像配置保存
        'shadowDelete':BASE_URL + '/devicemanager/deviceshadow/delete.do',//设备镜像删除
        'createDescApp':BASE_URL + '/devicemanager/TaskController/descApp.do',//设备镜像创建获取处理模块
        'createFindAllTask':BASE_URL + '/devicemanager/TaskController/findAllTaskJSONP.do',//设备镜像创建获取属性列表
        'shadowAdd':BASE_URL + '/devicemanager/deviceshadow/add.do',//设备镜像创建获取属性列表
        'shadowExist':BASE_URL + '/devicemanager/deviceshadow/exist.do',//设备镜像创建重复判断，get请求，参数：device_id,app_name
        'deviceSelect':BASE_URL + '/devicemanager/device/get_applist2.do'//设备镜像创建下拉框以及列表，post请求，参数：device_id，app_type
    },
    //处理模块管理
    'processManage':{
        'processList':BASE_URL + '/devicemanager/TaskController/listapps.do',//处理模块列表
        //'processAdd':BASE_URL + '/devicemanager/JarController/springUpload.do',//处理模块添加
        'processAddCheck':BASE_URL + '/devicemanager/JarController/upload_check.do',//处理模块添加检查
        'processAdd':BASE_URL + '/devicemanager/JarController/upload.do',//处理模块添加保存
        'upgradeList':BASE_URL + '/devicemanager/device/list2.do',//处理模块升级列表
        'upgradeSave':BASE_URL + '/devicemanager/TaskController/createTaskAndFireBatch.do',//处理模块升级下发
        'processDetailList':BASE_URL + '/devicemanager/TaskController/findTasklistbyparameters.do',//处理模块列表
    },
    //标签管理
    'labelManage':{
        'labelList':BASE_URL + '/devicemanager/SensorTemperatureDayVOController/findSensorTemperatureDayVOByParameter.do',//标签列表
        'labelLine':BASE_URL + '/devicemanager/SensorTemperatureDayVOController/listSensorTemperatureHistoryBySensorstampwithInterval.do',//标签实时刷新折线图
        'labelDetailList':BASE_URL + '/devicemanager/SensorTemperatureDayVOController/descByPrimarykeyAndTimeat.do'//标签详情列表
    },
    //冷链管理
    //数据概述
    'dataOverview':{
        'overviewDeviceList':BASE_URL + '/devicemanager/stream/devicelist.do',//设备列表查询
        'mapData':BASE_URL + '/devicemanager/stream/get_gps.do',//地图接口
        'overviewNum':BASE_URL + '/devicemanager/stream/get_breif.do',//概览数据
        'overviewPie':BASE_URL + '/devicemanager/stream/get_data_proportion.do',//饼图接口
        'actualLine':BASE_URL + '/devicemanager/SensorTemperatureDayVOController/getPiechar.do',//实时数据折线图
        'trendLine':BASE_URL + '/devicemanager/stream/get_data_trend.do'//趋势折线图
    },
    //实时数据
    'realtimeData':{
        'realtimeList':BASE_URL + '/devicemanager/SensorTemperatureDayVOController/findSensorTemperatureDayVOByParameter.do',//实时数据标签列表查询
    }
}
export default API;
