<template>
    <div>
        <div class="page_title_content">
          <span class="page_title">边缘分析&nbsp;&gt;&nbsp;<router-link to="/edge/processModule" class="btn_color">处理模块管理</router-link>&nbsp;&gt;&nbsp;详情</span>
        </div>
        <div class="right_content">
            <!--基本信息-->
            <div class="information">
                <ul class="detail_basicmsg">
                    <li><label>处理模块名称:</label><span>{{dialogInfo.app.appname}}</span></li>
                    <li><label>处理模块类型:</label><span>{{dialogInfo.app.apptype_text}}</span></li>
                    <li><label>版本信息:</label><span>{{dialogInfo.app.appversion}}</span></li>
                    <li><label>处理模块描述:</label><span>{{dialogInfo.app.appdesc}}</span></li>
                    <li><label>属性:</label></li>
                </ul>
            </div>
            <div class="map_cont">
                <div class="list_div">
                    <table class="list">
                         <thead>
                            <tr>
                                <th style='width:20%'>属性名称</th>
                                <th style='width:20%'>显示名称</th>
                                <th style='width:20%'>默认值</th>
                                <th style='width:20%'>类型</th>
                                <th style='width:20%'>单位</th>
                            </tr>
                         </thead>
                        <tbody>
                            <tr v-for="(item,index) in dialogInfo.appsourcearray">
                                <td>{{ item.sourcename }}</td>
                                <td>{{ item.sourcedisplayname }}</td>
                                <td>{{ item.sourcedefault }}</td>
                                <td>{{ item.sourcedatatype }}</td>
                                <td>{{ item.sourceunit }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- 下发历史 -->
            <span class="module_title">下发历史</span>
            <div class="list_div">
                <table class="list">
                     <thead>
                        <tr>
                            <th style='width:25%'>下发时间</th>
                            <th style='width:25%'>设备名称</th>
                            <th style='width:25%'>版本号</th>
                            <th style='width:25%'>下发状态</th>
                        </tr>
                     </thead>
                    <tbody>
                        <tr v-for="(item,index) in upgradeHistory">
                            <td>{{ item.createtimeatshow }}</td>
                            <td>{{ item.accesskey }}</td>
                            <td>{{ item.appversion }}</td>
                            <td>{{ item.taskstatus=='true' ? '成功':(item.taskstatus=='successSendMqtt'?'下发中':'失败')}}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class='btn_div'>
            <router-link to="/edge/processModule" class='btn_back'>返回</router-link>
        </div>
    </div>
</template>
<script>
    export default {
        created:function () {
            let dialog_id = this.$route.query.id;
            this.$axios.post(this.$API.pipeLineManage.createDescApp,{
                'id':dialog_id,
            }).then( (res)=>{
                if(res.data.code == 420){
                    this.$router.push({path: '/login'});
                }
                let arrDialog = res.data.actionResult;
                if(arrDialog.app.apptype == 'stream'){
                    arrDialog.app.apptype_text = '流式';
                }else if(arrDialog.app.apptype == 'shadow'){
                    arrDialog.app.apptype_text = '镜像';
                }else{
                    arrDialog.app.apptype_text = '固件';
                }
                this.dialogInfo = arrDialog;
            });
            //下发历史
            this.$axios.post(this.$API.processManage.processDetailList,{
                'appid': dialog_id
            }).then( (res)=>{
                if(res.data.code == 420){
                    this.$router.push({path: '/login'});
                }
                this.upgradeHistory=res.data.actionResult;
            });
        },
        name: 'processDetail',
        data () {
            return {
                dialogInfo:{
                    app:{
                        appname:''
                    }
                },
                upgradeHistory:[]
            }
        },
        methods:{
           
        }
    }
   
</script>
<style>
    .information_cont h3{
        font-size:14px;
        padding-left:10px;
        margin-bottom:20px;
        border-left:3px solid #0004FF;
    }
    .detail_basicmsg{
        margin:0 25px;
    }
</style>
