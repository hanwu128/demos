<template>
    <div>
    	<header-viewers></header-viewers>
        <div  class="right_content tc">
            <img src="../../../assets/images/shadowStep1.png">
        </div>
        <div  class="right_content shadow_first_step">
            <ul class="shadowManagerUl label_ul">
                <li>
                    <label class="label_title">数字镜像名称：</label><span  class="li_content">
                    <!-- <input placeholder='请输入设备镜像名称' v-model="shadowName" class='inputText'> -->
                    <el-input
                      class="input_width"
                      placeholder= "请输入数字镜像名称"
                      :maxlength='shadowNameLength'
                      v-model='shadowName'
                    >
                    </el-input>
                    <b>*</b></span>
                    <p class='tip_text'>{{shadowname_tip}}</p>
                </li>
                <li>
                    <label style='vertical-align: top;' class="label_title">数字镜像描述：</label><span  class="li_content">
                    <!-- <textarea placeholder="请输入设备镜像描述" maxlength="100" v-model="shadowDesc"></textarea> -->
                        <el-input
                          type="textarea"
                          :autosize="{ minRows: 2, maxRows: 2}"
                          :maxlength='shadowDescLength'
                          resize="none"
                          placeholder="请输入数字镜像描述"
                          v-model="shadowDesc"
                        >
                        </el-input>
                    </span>
                </li>
                <li>
                    <label class="label_title">网关设备名称：</label><span  class="li_content">
                        <!-- <select v-model='deviceID' class='select_style' @change='changeDevice'>
                            <option v-for="(item,index) in deviceid_list" :value="item.device_id">{{ item.access_key }}</option>
                        </select> -->
                        <el-select v-model="deviceID" 
                            filterable 
                            remote
                            reserve-keyword
                            @visible-change='searchVisible'
                            :remote-method="searchList"
                            placeholder="请选择或输入网关设备名称"
                            @change="changeDevice"
                        >
                            <el-option
                              v-for="item in deviceid_list"
                              :key="item.value"
                              :label="item.label"
                              :value="item.value">
                            </el-option>
                            <!-- <el-pagination @size-change="searchHandleSizeChange" @current-change="searchHandleCurrentChange" :current-page="search.currentpage" :total="search.total_num" layout="total,prev, pager, next, jumper" style='margin: 10px 10px 0;'></el-pagination> -->
                        </el-select>
                    </span>
                    <p class='tip_text'>{{select_tip}}</p>
                </li>
            </ul>
        </div>
        <div class="btn_div_right">
            <span class="btn_back" @click="cancle">取消</span>
            <span class="btn_save" @click="next">下一步</span>
        </div>
    </div>
</template>
<script>
	import headerViewers from './header.vue'
    export default {
        name: 'insertInfo',
        data () {
            return {
                shadowName:'',
                shadowDesc:'',
                deviceID:'',
                deviceid_list:[],
                shadowname_tip:'',
                select_tip:'',
                shadowNameLength:18,
                shadowDescLength:100,
                // 关联搜索分页
                search:{
                    access_key:'',
                    rowcount: 10,
                    currentpage: 1,
                    total_num:0
                }
            }
        },
        components: {
      		headerViewers : headerViewers
    	},
        methods:{
            cancle(){
                this.$router.push({path:'/edge/shadowManager'})

            },
            next(){
            	let name = this.$data.shadowName;
            	let desc = this.$data.shadowDesc;
                let reg_check = /^[\w\s\S\u4e00-\u9fa5]{4,18}$/;
                if(name == ''){
                    this.shadowname_tip='请输入数字镜像名称';
                    return
                }
                if(!reg_check.test(name)){
                    this.shadowname_tip='请输入4-18个字符';
                    return
                }
                if(this.deviceID == ''){
                    this.streamingname_tip='';
                    this.devicename_tip = '请选择或输入网关设备名称';
                    return;
                }
                let select_data = this.deviceID;
                let select_array = select_data.split(',');
                this.$axios.post(this.$API.shadowManage.deviceSelect,{
                    'device_id':select_array[0],
                    'app_type':'shadow'
                }).then((res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    if(res.data.actionResult == undefined || res.data.actionResult.length == 0){
                        this.shadowname_tip='';
                        this.select_tip='该设备无处理模块，请更换设备';
                        return;
                    };
                    this.$router.push({path:'/edge/shadow/module',query:{shadowName:this.shadowName,shadowDesc:this.shadowDesc,deviceID:select_array[0]}});
                });
            },
            changeDevice(){
                //this.shadowname_tip='';
                this.select_tip='';
            },
            searchList(access_key){
                this.search.access_key=access_key;
                this.$axios.post(this.$API.edgeManage.deviceList,{
                  'edgent_version':'',
                  'access_key':this.search.access_key,
                  'pagesize':this.search.rowcount,
                  'current':this.search.currentpage,
                  'sort_orderby':"create_stamp",
                  'sort_rule':"descending"
                }).then( (res) =>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    this.deviceid_list = [];
                    res.data.rows.forEach((item,index)=>{
                        let obj = {};
                        obj.label = item.access_key;
                        obj.value = item.device_id+','+item.access_key;
                        this.deviceid_list.push(obj);
                    });
                    this.search.total_num=res.data.total;
                });
            },
            searchHandleSizeChange(val) {
                this.search.rowcount = val;
                this.searchList(this.search.access_key);
                this.deviceID = this.search.access_key;
            },
            searchHandleCurrentChange(val) {
                this.search.currentpage = val;
                this.searchList(this.search.access_key);
                this.deviceID = this.search.access_key;
            },
            searchVisible(status){
                // this.currentpage=1;
                // if(status == true && this.deviceID==''){
                //     this.select_check=this.deviceID;
                //     this.searchList(this.search.access_key);
                //     // this.first_step_select='';
                // }else if(status == true && this.deviceID!=''){
                //     this.select_check=this.deviceID;
                //     let select_data = this.deviceID;
                //     let select_array = select_data.split(',');
                //     this.searchList(select_array[1]);
                // }else if(status == false && this.search.access_key == this.deviceID || status == false && this.deviceID==this.select_check){ 
                //     this.deviceID=this.select_check;
                //     let select_data = this.deviceID;
                //     let select_array = select_data.split(',');
                //     this.searchList(select_array[1]);
                // }
            },
        },
        created:function () {
            this.searchList();
        }
    }
</script>

<style>
    .account {
        margin-top: 20px;
        margin-left: 5%;
    }
    .cancle {
        width: 80px;
        height: 30px;
        border: groove 1px solid;
        background-color: deepskyblue;
        position: relative;
        top: 50px;;
        left: 40%;
        cursor: pointer;
        outline: none;
    }

    .shadowdescribe {
        position: relative;
        bottom: 190px;
    }
    .shadow_first_step{text-align:center;margin-top:40px;}
    .shadow_first_step label{
        display: inline-block;
        width: 100px;
        margin-right:10px;
        font: 14px/14px 'Microsoft YaHei';
        text-align: right;
    } 
    .shadow_first_step li{height:50px;margin-top:10px;}  
    .shadow_first_step input{width:396px;}
    .shadow_first_step textarea{width:398px;}
    .shadow_first_step select{width:400px;}
</style>