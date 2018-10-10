<template>
    <div>
        <!--{{this.$route.query.shadowName}} shadowDesc deviceID-->
        <header-viewers></header-viewers>
        <div  class="right_content tc">
            <img src="../../../assets/images/shadowStep2.png">
        </div>
        <div  class="right_content">
            <div class="choiceModule">
                <span class="label_title">选择处理模块:</span>
                <select  v-on:change="descApp(indexVal)" v-model='indexVal' class='select_style'>
                    <option v-for="(shadowoption,index) in shadowoptionlist" v-bind:value="index">{{ shadowoption.appname }}</option>
                </select>
            </div>
            <p class="choiceModule">
                <span class="choiceModuleTitle">处理模块描述:</span>
                <span class="moduleDesc">
                    {{appDesc}}
                </span>
            </p>
            <div class="choiceModule">属性:</div>
            <div class="insertmodule_list">
                <table class="list">
                    <thead>
                        <th>属性名称</th>
                        <th>显示名称</th>
                        <th>默认值</th>
                        <th>类型</th>
                        <th>单位</th>
                    </thead>
                    <tbody>
                    <tr v-for="(shadowapp,index) in shadowapplist">
                        <td>{{ shadowapp.sourcename }}</td>
                        <td>{{ shadowapp.sourcedisplayname }}</td>
                        <td>{{ shadowapp.sourcedefault }}</td>
                        <td>{{ shadowapp.sourcedatatype }}</td>
                        <td>{{ shadowapp.sourceunit }}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="btn_div_right">
            <span class="btn_back" @click="back">上一步</span>
            <span class="btn_save" @click="next">下一步</span>
        </div>
    </div>
</template>
<script>
	import headerViewers from './header.vue'

    export default {
        name: 'insertModule',
        data () {
            return {
            	shadowoptionlist:[],
            	indexVal:0,
            	shadowapplist:[],
                shadowappText:{},
            	appName:'',
                appDesc:''
            }
        },
        components: {
      		headerViewers : headerViewers
    	},
        methods:{
            back:function () {
                this.$router.push({path:'/edge/shadow/info'})
            },
            next:function () {
            	let shadowName = this.$route.query.shadowName || '';
            	let shadowDesc = this.$route.query.shadowDesc || '';
            	let deviceID = this.$route.query.deviceID || '';
            	let storage = window.localStorage;
                //判断处理模块是否存在
                this.$axios.post(this.$API.shadowManage.shadowExist,{
                    'device_id':deviceID,
                    'app_name':this.shadowoptionlist[this.indexVal].appname
                }).then( (res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    if(res.data.count > 0){
                        this.$message.error('无法创建数字镜像，当前选定的处理模块已存在数字镜像。');
                        return;
                    }else{
                        storage.removeItem("shadow");
                        storage["shadow"] = JSON.stringify(this.shadowapplist) || [];
                        this.$router.push({path:'/edge/shadow/MAP?datas=1111&shadowDesc=' + shadowDesc + '&shadowName=' + shadowName + '&deviceID=' + deviceID + '&appName=' + this.appName});
                    }
                });   
            },
            descApp:function (index) {
            	this.appName = this.shadowoptionlist[index].appname;
                this.appDesc = this.shadowoptionlist[index].appdesc;
                this.shadowapplist = this.shadowoptionlist[index].appsourcearray;
                
            }

        },
        created:function () {
            let device_id = this.$route.query.deviceID || '';
            this.$axios.post(this.$API.shadowManage.deviceSelect,{
                'device_id':device_id,
                'app_type':'shadow'
            }).then((res)=>{
                if(res.data.code == 420){
                    this.$router.push({path: '/login'});
                }
                this.shadowoptionlist = res.data.actionResult;
                this.descApp(0);
            });
        },
    }
</script>

<style>
    .insertmodule_list{
        width: 90%;
        margin: 0 auto;
    }
    .moduleDesc {
        width: 70%;
        height: 10%;
    }
    .moduleTable {
        position: relative;
        margin-left: 10%;
        width: 80%;
    }
    .moduleTitle {
        display: inline-block;
        width: 120px;
        float: left;
    }
</style>