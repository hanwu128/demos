<template>
    <div>
        <div class="page_title_content">
          <span class="page_title"><router-link to="/edge/shadowManager" class="btn_color">数字镜像</router-link>&nbsp;&gt;&nbsp;详情</span>
        </div>
        <div class="right_content">
            <!--基本信息-->
            <div class="information">
                <span class="module_title">基本信息</span>
            	<ul class="detail_basicmsg">
            		<li><label>数字镜像名称:</label><span>{{this.shadowDetail.shadow_name}}</span></li>
            		<li><label>数字镜像描述:</label><span>{{this.shadowDetail.shadow_desc}}</span></li>
                    <li><label>创建时间：</label><span>{{this.shadowDetail.create_stamp}}</span></li>
            		<li><label>网关设备名称:</label><span>{{this.shadowDetail.access_key}}</span></li>
            	</ul>
            </div>  
            <!--处理模块信息-->
            <div class="information">
                <span class="module_title">处理模块信息</span>
                <ul class="detail_basicmsg">
                    <li><label>处理模块名称：</label><span>{{this.shadowDetail.app.appname}}</span></li>
                    <li><label>处理模块描述：</label><span>{{this.shadowDetail.app.appdesc}}</span></li>
                    <li><label>处理模块版本：</label><span>{{this.shadowDetail.app.appversion}}</span></li>
                </ul>
            </div>         
            <div class="map_cont">
                <span class="module_title">MAP信息</span>
                <div class="list_div">
                	<table class="list">
        	             <thead>
        		            <tr>
        		                <th style='width:15%'>属性名称</th>
        		                <th style='width:15%'>显示名称</th>
                                <th style='width:15%'>MAP名称</th>
        		                <th style='width:15%'>类型</th>
        		                <th style='width:10%'>单位</th>
                                <th style='width:15%'>当前值</th>
                                <th style='width:15%'>更新时间</th>
        		            </tr>
        		         </thead>
        	            <tbody>
        		            <tr v-for="(shadowGW,index) in shadowGateWay">
        		                <td>{{shadowGW.item_name}}</td>
        		                <td>{{shadowGW.item_display_name}}</td>
                                <td>{{shadowGW.item_map_name}}</td>
        		                <td>{{shadowGW.item_datatype}}</td>
        		                <td>{{shadowGW.item_unit}}</td>
                                <td>{{shadowGW.item_value}}</td>
                                <td>{{shadowGW.itemvalue_stamp}}</td>
        		            </tr>
        	            </tbody>
        	        </table>
                </div>
            </div>
        </div>
        <div class='btn_div'>
            <router-link to="/edge/shadowManager" class='btn_back'>返回</router-link>
        </div>
    </div>
</template>
<script type="text/babel">


    export default {
        created:function () {
            this.$axios.get(this.$API.shadowManage.configList+"?shadow_name=" + this.$route.query.shadow_name,{}).then( (res)=>{
                if(res.data.code == 420){
                    this.$router.push({path: '/login'});
                }
                let arr = res.data.items;
                this.shadowDetail = res.data;
                this.shadowGateWay=res.data.items;
            },function(res){
            });
        },
        name: 'detail',
        data () {
            return {
               shadowDetail:{
                    app:{
                        appname:''
                    }
               },
               shadowGateWay:{}
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
