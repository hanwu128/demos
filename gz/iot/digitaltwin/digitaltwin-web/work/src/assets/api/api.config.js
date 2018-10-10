/**********************
* 统一存储接口,使用模块名+接口名
* @version v1.0.0
* @author:Mad
* @QQ:1823737636
* @date:2017-9-21
* @edit:2017-12-13
**********************/
const DEBUG = false
// let BASE_URL = DEBUG ? 'http://223.203.218.93' : '';//产线环境
//let BASE_URL = DEBUG ? 'http://10.100.157.19:8080' : '';
// let BASE_URL = DEBUG ? 'http://172.17.199.109:8080' : '';//测试环境
//let BASE_URL = DEBUG ? 'http://10.100.157.34:8080' : '';//本地环境
//let LOGIN_URL = DEBUG ? 'http://10.100.156.81:8080' : '';
let BASE_URL = DEBUG ? 'http://172.17.200.61:8400' : '';
let token = '';
// debugger
if(location.hash.split('?')[1]){
   token = location.hash.split('?')[1];
   if(token=='hidden=hidden'){
    token=''
   }else{
    token = '?token='+location.hash.split('?')[1];
   }
}else{
        token = ''
}
const API = {
	// 查询DT列表
    'temmodel':{
        'temList':BASE_URL + '/dt/front/tpl/list.ft' + token,//物模块列表
        'temlistdec':BASE_URL + '/dt/front/tpl/detail.ft' + token,//物模块列表详情
        'addteminfor':BASE_URL + '/dt/front/tpl/add.ft' + token,//物模板添加
        'deletetem':BASE_URL + '/dt/front/tpl/delete.ft' + token,//物模板删除
        'updatetem':BASE_URL + '/dt/front/tpl/update.ft' + token,//编辑物模块
        'isRename':BASE_URL + '/dt/front/check/template.ft' + token,//物模板名称是否重名

    },
    'RealColumn':{
    	'colList':BASE_URL + '/dt/front/instance/list.ft' + token,//物实例列表
    	'addexample':BASE_URL + '/dt/front/instance/add.ft' + token,//添加物实例
    	'deleteExp':BASE_URL + '/dt/front/instance/delete.ft' + token,//物实例删除
    	'exampledec':BASE_URL + '/dt/front/instance/detail.ft' + token,//查看物实例详情
        'updatedatil':BASE_URL + '/dt/front/instance/update.ft' + token,//更新物实例
        'updateexa':BASE_URL + '/dt/front/instance/update/expectvalue.ft' + token,//更新编辑物实例
        'charts':BASE_URL + '/dt/front/instance/tsdb.ft' + token,//更新编辑物实例
        'isRename':BASE_URL + '/dt/front/check/instance.ft' + token,//物实例名称是否重名
    }
}
export default API;
