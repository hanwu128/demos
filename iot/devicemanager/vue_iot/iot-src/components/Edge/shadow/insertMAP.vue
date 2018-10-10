<template>
    <div class="insertMAPBg">
    	<header-viewers></header-viewers>
        <div  class="right_content tc">
            <img src="../../../assets/images/shadowStep3.png">
        </div>
        <div  class="right_content">
            <div class="configTable">
                <div class="map_left">
                    <span class="map_title">属性列表:</span>
                    <table class="list">
                        <thead>
                            <th style='width:25%'>属性名称</th>
                            <th style='width:25%'>显示名称</th>
                            <th style='width:25%'>默认值</th>
                            <th style='width:10%'>类型</th>
                            <th style='width:15%'><input type="checkbox" v-model='map_all.state'  @click="changeAllMap" class="checkall">全选</th>
                        </thead>
                        <tbody>
        	            <tr v-for="(shadowapp,index) in shadowapplist">
        	                <td>{{ shadowapp.sourcename }}</td>
        	                <td>{{ shadowapp.sourcedisplayname }}</td>
        	                <td>{{ shadowapp.sourcedefault }}</td>
        	                <td>{{ shadowapp.sourcedatatype }}</td>
        	                <td><input type="checkbox" v-model='shadowapp.state' @click="changeStyle(index)" ></td>
        	            </tr>
        	            </tbody>
                    </table>
                </div>
                <div class="MAP_h3">
                	<a class="btn_symbol changeMAP" href="javascript:;" @click="gtlistFn">></a>
                    <span>MAP操作</span>

                </div>
                <div class="map_right">
                    <span class="map_title">MAP列表:</span>
                    <table class="list">
                        <thead>
        	                <th style='width:25%'>属性名称</th>
                            <th style='width:25%'>显示名称</th>
                            <th style='width:30%'>MAP</th>
                            <th style='width:20%'>操作</th>
                        </thead>
                        <tbody>
        		            <tr v-for="(shadowapp,index) in subShadowapplist">
        		                <td>{{ shadowapp.sourcename }}</td>
        		                <td><input type="text" v-model="shadowapp.displayVal"></td>
        		                <td><input type="text" v-model="shadowapp.inputVal"></td>
        		                <td><a href="javascript:;" @click="delshashow_list(index)" class='btn_color'>删除</a></td>
        		            </tr>
        	            </tbody>
                    </table>
                    <div class="no_data" style="display: block;margin:0 auto;" v-if="map.show_no_data"><i :class="{'loading':map.show_loading}"></i></div>
                </div>
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
        name: 'insertMAP',
        data () {
            return {
                items:[],
                selectStatus:[],
                subShadowapplist:[],
                shadowapplist : [],
                item:{},
                map_all:{//全选按钮
                    state:false,
                    checkone_num:0,
                    select_all:0
                },
                //map暂无数据
                map:{
                    show_no_data:true,
                    show_loading:false
                }
            }
        },
        components: {
      		headerViewers : headerViewers
    	},
        mounted:function () {
        	let storage = window.localStorage;
        	let datas = storage.shadow || {}
        	let shadowName = this.$route.query.shadowName || {}
        	let shadowDesc = this.$route.query.shadowDesc || {}
        	let deviceID = this.$route.query.deviceID || {}
        	this.shadowapplist = JSON.parse(datas);
        	for (var i = 0; i<this.shadowapplist.length; i++) {
                this.shadowapplist[i].state=false;
            }
            this.map_all.select_all = this.shadowapplist.length;
        },
        methods: {
            back:function () {
            	let shadowName = this.$route.query.shadowName || {}
	        	let shadowDesc = this.$route.query.shadowDesc || {}
	        	let deviceID = this.$route.query.deviceID || {}
                this.$router.push({path:'/edge/shadow/module?shadowDesc=' + shadowDesc + '&shadowName=' + shadowName + '&deviceID=' + deviceID})
            },
            changeStyle:function(index) {
                this.shadowapplist[index].state = !this.shadowapplist[index].state;
                if(this.shadowapplist[index].state == true){
                    this.map_all.checkone_num++;
                    if(this.map_all.select_all == this.map_all.checkone_num){
                       this.map_all.state = true;     
                    }
                }else{
                    this.map_all.checkone_num--;
                    this.map_all.state = false;
                }
            },
            changeAllMap:function(){
                this.map_all.state = !this.map_all.state;
                if(this.map_all.state == true){
                    if(this.map_all.select_all == 0){
                        this.map_all.state == false;
                        return;
                    }
                    for (var i = 0; i<this.shadowapplist.length; i++) {
                        this.shadowapplist[i].state = true;
                    }
                    this.map_all.checkone_num = this.shadowapplist.length;
                }else{
                    for (var i = 0; i<this.shadowapplist.length; i++) {
                        this.shadowapplist[i].state = false;
                    }
                    this.map_all.checkone_num = 0;
                }
            },
            delshashow_list: function(index){
                this.$confirm('确定要删除吗？', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                    this.subShadowapplist.splice(index,1);
                    if(this.subShadowapplist.length == 0){
                        this.map = {
                            show_no_data:true,
                            show_loading:false
                        };
                    }
                }).catch(() => {
                   return      
                });
            },
            next:function () {
                this.item.item_name = "itemname"
                this.item.item_display_name = "item_display_name"
                this.item.item_map_name = "item_map_name"
                this.item.item_datatype = "string" //长度8位
                this.item.item_unit = ""
                this.items.push(this.item)
                let shadowName = this.$route.query.shadowName || {}
	        	let shadowDesc = this.$route.query.shadowDesc || {}
	        	let deviceID = this.$route.query.deviceID || {}
                let appId = this.$route.query.appId;
	        	let appName = this.$route.query.appName || ''

                if(this.subShadowapplist.length<1){
                    this.$message.info('请选择数据。');
                    return
                }
                for (var i=0; i < this.subShadowapplist.length;i++) {
                    if(!!!this.subShadowapplist[i].displayVal){
                        alert('请输入显示名称');
                        this.$message.info('请输入显示名称。');
                        return;
                    }
                    if(!!!this.subShadowapplist[i].inputVal){
                        this.$message.info('请输入MAP值。');
                        return
                    }
                }               
                this.$axios.post(this.$API.shadowManage.shadowAdd,{
                    shadow_name:shadowName,
                    device_id:deviceID,
                    app_id:appId,
                    app_name:appName,
                    shadow_desc:shadowDesc,
                    items: this.subShadowapplist
                }).then( (res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    if(res.data.result == true){
                        this.$router.push({path:'/edge/shadow/finish?shadowName='+shadowName+'&shadowDesc='+shadowDesc+'&deviceID='+deviceID})
                    }else{
                        this.$message.error('创建失败\n'+res.data.error);
                    }
                },function(res){
                    console.log(res)
                });
            },
            gtlistFn: function () {
                let tempArr = [];
                for (var i = 0; i<this.shadowapplist.length; i++) {
                    if(this.shadowapplist[i].state == true){
                        this.shadowapplist[i].inputVal = this.shadowapplist[i].sourcename;
                        this.shadowapplist[i].displayVal = this.shadowapplist[i].sourcedisplayname;
                        tempArr.push(this.shadowapplist[i]);
                    }
                }
                if(tempArr.length == 0){
                    this.$message.info('请选择属性');
                    return;
                }
                this.map = {
                    show_no_data:false,
                    show_loading:false
                };
                this.subShadowapplist = tempArr;
            }
        }
    }
</script>

<style>
    .insertMAPBg {
        height: 100%;
    }
    .btnBack {
        width: 80px;
        height: 30px;
        border: groove 1px solid;
        background-color: deepskyblue;
        cursor: pointer;
    }
    .btnNext {
        width: 80px;
        height: 30px;
        border: groove 1px solid;
        background-color: deepskyblue;
        cursor: pointer;
    }
    .canclebutton {
        position: relative;
        text-align: center;
        width: 100%;

    }
</style>