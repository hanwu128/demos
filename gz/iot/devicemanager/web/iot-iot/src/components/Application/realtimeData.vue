<template>
    <div>
        <div class="page_title_content">
          <span class="page_title">冷链应用&nbsp;&gt;&nbsp;<router-link to="/application/realtimeData" class="btn_color">实时数据</router-link></span>
        </div>
        <div class="right_content label_list_div">
            <span class="module_title">标签列表</span>
            <!-- <el-date-picker type='date' placeholder='选择日期范围' v-model='date_range' :picker-options='pickerOptions0' id='select_date' @change='dateChange'></el-date-picker> -->
            <div class="header">
            	<select class="select_style" v-model="select_device" @change=selectFn()>
                    <option value=''>全部车辆</option>
                    <option :value='task.device_id'  v-for="(task,index) in deviceArray">{{task.device_desc}}</option>
            	</select>
            	<!-- <span class="btn_create fr">标签导入</span> -->
            </div>
            <div class="handle_module_list">
                <!-- <table class="list">
                    <thead>
                    <tr>
                        <th style='width:5%'></th>
                        <th style='width:12%'>标签ID</th>
                        <th style='width:12%'>标签名称</th>
                        <th style='width:12%'>当前值</th>
                        <th style='width:12%'>最高值</th>
                        <th style='width:12%'>最低值</th>
                        <th style='width:20%'>网关设备ID</th>
                        <th style='width:5%'>操作</th>
                    </tr>
                    </thead>
                    <tbody> -->
                    <ul  class="list_content clearfix">
                        <!-- <td><input type="checkbox"  @click="labelCheck(index)" v-model='task.state'></td> -->
                        <!-- <li>{{ task.labelid }}</li>
                        <li>{{ task.labelname }}</li>
                        <li><p class="td_p"><label :class="task.td_i">{{ task.currentvalue }}</label><br><span class="td_span">{{ task.updatestampstrshow }}</span></p><i :class="task.td_i"></i></li>
                        <li>{{ task.maxvalue }}<br><span class="td_span">{{ task.maxstampshow }}</span></li>
                        <li>{{ task.minvalue }}<br><span class="td_span">{{ task.minstampshow }}</span></li>
                        <li>{{ task.deviceid }}</li>
                        <li :class="'text-center'"><span class="btn_color" @click="detailClick(index)">详情</span></li> -->
                        <li class="list_li fl" v-for="(task,index) in taskInfo" >
                        	<div class="list_nav">
                                <input type="checkbox" name="" class="check-one fl" checked='true' v-model='task.state' v-if="task.state==true" @click="labelCheck(index)">
                        		<input type="checkbox" name="" class="check-one fl"  v-model='task.state'  v-else @click="labelCheck(index)">
                        		<h3 class="fl" @click="labelCheckTitle(index)">{{ task.labelname }}</h3>

                        	</div>
                        	<div class="list_cont">
                        		<p class="list_p1">{{ task.updatestampstrshow }}</p>
                        		<p class="list_p2">{{ task.currentvalue }}<i :class="task.td_i"></i></p>
                        		<p class="list_p3"><span>最高值：{{ task.maxvalue}}</span><span>最低值：{{ task.minvalue }}</span><a href="javascript:;" @click="detailClick(index)">详情</a></p>
                        	</div>
                        </li>
                    </ul>
         <!--            </tbody>
                </table> -->
                <el-pagination @current-change="handleCurrentChange" :current-page="currentpage" :page-size="rowcount" :total="total_num" layout="total,prev, pager, next, jumper" id='label_pagination'></el-pagination>
            </div>
        </div>
        <!-- <div class='btn_div_left'>
            <a href='javascript:;' class='btn_create' @click="drawLineClick">绘制折线图</a>
        </div> -->   
        <div class="right_content">
            <div style="position: relative;">
                <span class="module_title" title='当前10分钟内实时数据，每5秒更新数据'>实时数据</span><span class="explain" @mouseover = 'remarkShowFn(0)' @mouseout = 'remarkShowFn(0)'></span>
                <span class="title_remark" v-show = 'realtimeShow'>
                    <label class="remaek_em">实时数据：</label>呈现当前选中标签中实时的数据趋势
                </span>
            </div>
            <div class = "line_show">
                <!-- <el-table v-loading="loading2" v-if="loading2" :data="loadingArray" element-loading-text="拼命加载中" style="width: 100%;height:400px;">
                </el-table> -->
                <x-animate-chart :id_animate='id_animate' :label_ids='label_ids' :label_datas='label_datas'></x-animate-chart>
            </div>
        </div>   
        <div class="right_content" v-if="history_line">
            <span class="module_title">温度历史记录</span>
            <div class = "line_show">
                <x-chart :id="id" :categories_data="categories_data" :series_data="series_data"></x-chart>
            </div>
        </div>
    </div>
</template>
<script>
    import XChart from '../Edge/chart.vue'
    import XAnimateChart from '../Edge/animateChart.vue'
    export default {
        created:function () {
            this.$nextTick(() => {
                this.deviceSelectFn();
                this.labelListFn(0);
                this.timer_id = setInterval( ()=>{
                    this.labelListFn(1);
                },5000);
            })
        },
        name: 'taskList',
        data () {
            let date = new Date();
            let Y = date.getFullYear();
            let m = date.getMonth() + 1;
            let d = date.getDate();
            if (m < 10) {
                m = '0' + m;
            }
            if (d < 10) {
                d = '0' + d;
            }
            return{
                history_line:false,
                taskInfo:[],
                dialogInfo:{},
                searchTime:'',
                checkNum:0,
                dialogShow:false,
                value: 0,
                date_range: Y+'-'+ m +'-' + d,
                // date_range:'2017-09-26',
                id: 'test',
                id_animate:'actual_line',
                label_ids:[],
                label_datas: {},
                categories_data:[],
                series_data:[],
                rowcount: 10,
                currentpage: 1,
                total_num:0,
                timer_id:0,
                loadingArray:[1],
                loading2:true,
                deviceArray:[],
                realtimeShow:false,
                select_device:'',
                cancleDrawFn:function(){},
                // pickerOptions0:{
                //     disabledDate(time) {
                //         return time.getTime() > Date.now();
                //     }
                // }

            }
        },
        components: {
            XChart,
            XAnimateChart
        },
        beforeDestroy:function(){
          clearInterval(this.timer_id);
        },
        methods: {
            labelListFn: function(status){
                let _status = status;
                let labelidarray=[];
                this.$axios.post(this.$API.realtimeData.realtimeList,{
                    'what_day': this.date_range,
                    'rowcount': this.rowcount,
                    'currentpage': this.currentpage,
                    'device_id': this.select_device
                }).then( (res)=>{
                    this.total_num = res.data.totalCount;
                    let arrTask = res.data.actionResultData;
                    if(_status==0){
                        this.taskInfo = [];
                        for(var i=0;i<arrTask.length;i++) {
                            labelidarray.push(arrTask[i].labelid);
                            if(arrTask[i].difference>0){
                                arrTask[i].td_i = 'td_i_high';
                            }else if(arrTask[i].difference<0){
                                arrTask[i].td_i = 'td_i_low';
                            }else{
                                arrTask[i].td_i = 'td_i_equal';
                            }
                            arrTask[i].state = false;
                            this.taskInfo.push(arrTask[i]);
                        }
                    }else{
                        for(var i=0;i<arrTask.length;i++) {
                            // labelidarray.push(arrTask[i].labelid);
                            if(arrTask[i].difference>0){
                                arrTask[i].td_i = 'td_i_high';
                            }else if(arrTask[i].difference<0){
                                arrTask[i].td_i = 'td_i_low';
                            }else{
                                arrTask[i].td_i = 'td_i_equal';
                            }
                            // arrTask[i].state = false;
                            // this.taskInfo.push(arrTask[i]);
                            this.taskInfo[i].minvalue= arrTask[i].minvalue;
                            this.taskInfo[i].maxvalue = arrTask[i].maxvalue;
                            this.taskInfo[i].updatestampstrshow = arrTask[i].updatestampstrshow;
                            this.taskInfo[i].currentvalue = arrTask[i].currentvalue;
                            this.taskInfo[i].td_i = arrTask[i].td_i;

                        }
                    }                    
                    if(_status == 0){
                        //实时刷新折线图调用
                        // this.$axios.post(this.$API.labelManage.labelLine,{'flagTimeBatch': 30,'labelidarray':labelidarray}).then( (res)=>{
                        //     let option_data = res.data.actionResultData;
                        //     this.label_ids = labelidarray;
                        //     this.label_datas.labelidarray = labelidarray;
                        //     this.label_datas.categories_data = option_data.listX_TimeAt;
                        //     this.label_datas.series_data = option_data.listY_oC;
                        //     this.loading2 = false;
                        // });
                    }
                });
            },
            deviceSelectFn:function(){
                this.$axios.post(this.$API.dataOverview.overviewDeviceList).then( (res)=>{
                    this.deviceArray = res.data.rows;
                });
            },
            selectFn: function(){
                this.labelListFn(0);
            },
            handleClick:function () {

            },
            detailClick:function (index) {
                this.$router.push({path:'/application/realtimeDetail',query: { 'label_id': this.taskInfo[index].label_id}})
            },
            cancleClick:function(){
                this.dialogShow = false;
            },
            searchClick:function(){
            },
            labelCheck:function(index){
            	let target = event.target;
            	// document.getElementsByClassName('list_li').className='list_li fl';
                if(this.taskInfo[index].state == true && this.checkNum<5){
                	event.target.parentElement.parentElement.className='active list_li fl';
                    this.checkNum++;
                    this.drawLineClick();
                }else if(this.taskInfo[index].state == false){
                	event.target.parentElement.parentElement.className='list_li fl';
                    this.checkNum--;
                    this.drawLineClick();
                }else{
                    this.taskInfo[index].state =false;
                    alert('最多可选5条');
                }
            },
            labelCheckTitle:function(index){
                let target = event.target;
                this.taskInfo[index].state = !this.taskInfo[index].state;
                if(this.taskInfo[index].state == true && this.checkNum<5){
                    event.target.parentElement.parentElement.className='active list_li fl';
                    this.checkNum++;
                    this.drawLineClick();
                }else if(this.taskInfo[index].state == false){
                    event.target.parentElement.parentElement.className='list_li fl';
                    this.checkNum--;
                    this.drawLineClick();
                }else{
                    this.taskInfo[index].state = false;
                    alert('最多可选5条');
                }
            },
            drawLineClick:function(){
                let what_day = this.taskInfo[0].what_day;
                let labelidarray = [];
                for(var i=0;i<this.taskInfo.length;i++){
                    if(this.taskInfo[i].state == true){
                        labelidarray.push(this.taskInfo[i].labelid);
                    }
                }
                if(labelidarray.length == 0){
                    alert('请选择标签');
                    return;
                }
                // this.$axios.post("http://223.203.218.93/devicemanager/SensorTemperatureDayVOController/bindReportDataSourceminitue.do",{'what_day': what_day,'labelidarray':labelidarray}).then(function(res){
                //     let option_data = res.data.actionResultData;
                //     _self.categories_data = option_data.listX_TimeAt;
                //     _self.series_data = option_data.listY_oC;
                // });
                //实时刷新折线图调用
                let CancelToken = this.$axios.CancelToken;
                this.cancleDrawFn();//停止上一个请求
                console.log(this.cancleDrawFn);
                this.$axios.post(this.$API.labelManage.labelLine,{'flagTimeBatch': 30,'labelidarray':labelidarray,
                cancelToken: new CancelToken( (c)=> {
                    // executor 函数接收一个 cancel 函数作为参数
                    this.cancleDrawFn = c;
                  })
                }).then( (res)=>{
                    let option_data = res.data.actionResultData;
                    this.label_ids = labelidarray;
                    this.label_datas.labelidarray = labelidarray;
                    this.label_datas.categories_data = option_data.listX_TimeAt;
                    this.label_datas.series_data = option_data.listY_oC;
                });
            },
            // dateChange:function(val){
            //     this.date_range = val;
            //     this.labelListFn();
            // }
            // handleSizeChange(val) {
            //     console.log(`每页 ${val} 条`);
            //     this.rowcount = val;
            //     this.labelListFn(1);
            // },
            handleCurrentChange(val) {
                console.log(`当前页: ${val}`);
                this.currentpage = val;
                this.labelListFn(0);
                var listLi= document.getElementsByClassName('list_li');
                for(var i=0;i<listLi.length;i++){
                    listLi[i].className='list_li fl';

                }
            },
            remarkShowFn(status){
                if(status == 0){
                    this.realtimeShow = !this.realtimeShow;
                }
            },
            // cancleDrawFn(c){
            //     let cancle = c;
            //     console.log(cancle);
            // }
        }
    }
</script>

<style scoped>
    .header,.handle_module_list {
        margin:0 20px 20px 20px;
    }
    .handle_dialogTask{
        width: 650px;
        min-height: 400px;
        margin-left: -325px;
        padding-bottom:20px;
    }
    .handle_dialogTask li{
        min-height: 50px;
        line-height:50px;
        padding: 0 20px;
        list-style: none;
    }
    .handle_dialogTask label{
        display: inline-block;
        width: 120px;
        font: 14px/50px '微软雅黑';
        text-align: left;
        vertical-align: top;
    }
    .handle_dialogTask .label_l{
        margin-left:60px;
    }
    .handle_dialogTask ul span{
        display:inline-block;
        min-width:120px;
    }
    #select_date{
        display:inline-block;
        width:300px;
        cursor:pointer;
        float:right;
        margin:0 20px 10px 0;
    }
    #label_pagination{
        margin: 10px 0 0 -5px;
    }
    .td_span{
        font-size:12px;
    }
    .handle_module_list i.td_i_high,.handle_module_list i.td_i_low,.handle_module_list i.td_i_equal{
        display: inline-block;
        vertical-align: super;
        position: absolute;
        margin: 12px 0 0 20px;
        width: 20px;
        height: 20px;
        background: url(../../assets/images/icon.png) no-repeat -48px -20px;
    }
    .handle_module_list i.td_i_low{
        background-position: -33px -20px;
    }
    .handle_module_list i.td_i_equal{
        background:#fff;
    }
    .handle_module_list label.td_i_high,.handle_module_list label.td_i_low,.handle_module_list label.td_i_equal{
        color:#f00;
    }
    .handle_module_list label.td_i_low{
        color:#2dd19c;
    }
    .handle_module_list label.td_i_equal{
        color:#333;
    }
    .td_p{
        display:inline-block;
    }
    .label_list_div{
        min-height: 360px;
    }
    .list_li{
    	width: 285px;
    	height: 146px;
    	border:1px solid #d6d7dd;
    	margin-right: 14px;
    	margin-bottom: 14px;
    }
    .list_nav{
    	height: 34px;
    	background-color: #fafafa;
    	padding-left: 10px;
    	line-height: 34px;
    }
    .list_cont{
    	padding: 0 10px;
    }
    .list_nav h3{
        cursor: pointer;
    	margin-left: 10px;
    }
    .list_nav input{
    	margin-top: 10px;
    }
    .list_p1{
    	font-size: 14px;
    	line-height: 30px;
    	color: #666;
    }
    .list_p2{
    	font:28px/50px 'Microsoft YaHei';
    	color:#333;
    	text-align: center;
    }
    .list_li.active{
    	border:1px solid #0093D8;
    }
    .list_p3 span{
    	font: 14px/30px 'Microsoft YaHei';
    	color: #666;
    	margin:0 4px;
    }
    .list_p3 a{
    	font-size: 14px;
    	color: #0093D8;
    	margin-left: 9px;
    }
    .list_content{
    	height: auto;
    }
</style>