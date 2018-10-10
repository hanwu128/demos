<template>
    <div>
        <div class="page_title_content">
          <span class="page_title">冷链应用&nbsp;&gt;&nbsp;<router-link to="/edge/labelManager" class="btn_color">数据概述</router-link></span>
        </div>
        <div class="right_content">
            <div style="position: relative;">
                <span class="module_title">概览</span><span class="explain" @mouseover = 'remarkShowFn(0)' @mouseout = 'remarkShowFn(0)'></span>
                <span class="title_remark" v-show = 'overviewShow'>
                    <label class="remaek_em">车辆数：</label>截止当前使用终端统计设备的车辆数<br>
                    <label class="remaek_em">标签数：</label>截止当前系统中添加的标签累计数量<br>
                    <label class="remaek_em">标签采集数：</label>截止当前系统中标签所采集到数据的累计次数<br>
                    <label class="remaek_em">异常次数：</label>截止当前系统中标签所采集到数据的异常数据累计次数
                </span>
            </div>
            <el-row class="overview_module">
                <el-col :span="6" class="label_content">
                    <div class="label_div">
                        <i class="overview_i1"></i>
                        <span class="overview_text">车辆数</span>
                        <span>{{overviewData.device_count}}</span>
                    </div>
                </el-col>
                <el-col :span="6" class="label_content">
                    <div class="label_div">
                        <i class="overview_i2"></i>
                        <span class="overview_text">标签数</span>
                        <span>{{overviewData.label_count}}</span>
                    </div>
                </el-col>
                <el-col :span="6" class="label_content">
                    <div class="label_div">
                        <i class="overview_i3"></i>
                        <span class="overview_text">标签采集数</span>
                        <span>{{overviewData.label_data_count}}</span>
                    </div>
                </el-col>
                <el-col :span="6" class="label_content">
                    <div class="label_div">
                        <i class="overview_i4"></i>
                        <span class="overview_text">异常次数</span>
                        <span>{{overviewData.label_data_invalid_count}}</span>
                    </div>
                </el-col>
            </el-row>
        </div>
        <div class="right_content">
            <div style="position: relative;">
                <span class="module_title">车辆状态</span><span class="explain" @mouseover = 'remarkShowFn(1)' @mouseout = 'remarkShowFn(1)'></span>
                <span class="title_remark" v-show = 'statusShow'>
                    <label class="remaek_em">位置信息：</label>呈现当前车辆实施的地图位置，通过列表选择查看任意车辆位置信息<br>
                    <label class="remaek_em">车辆数据采集：</label>截止当前选中车辆所采集到的数据信息占比，包含异常和正常两种数据<br>
                    <label class="remaek_em">车辆实时数据：</label>显示当前车辆设备实时返回采集次数的趋势变化
                </span>
            </div>
            <span class="module_subtitle">位置信息</span>
            <!-- 地图 -->
            <el-row>
              <el-col :span="17">
                  <b-map-component :longitude='longitude' :latitude='latitude' :description='description' :check_deviceid="check_deviceid"></b-map-component>         
              </el-col>
              <el-col :span="7">
                <div class="list_div location_list">
                    <table class="list">
                        <thead>
                        <tr>
                            <th style='width:100%'>车辆名称</th>
                        </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(device,index) in deviceInfo" class="tr_click">
                                <td :class="{'active':device.classStatus}" @click="deviceClick(index)">{{ device.device_desc }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
              </el-col>
            </el-row>
            <el-row class='mapText'>
                <el-col :span="3"><span>{{check_device_desc}}</span></el-col>
                <el-col :span="4"><span>{{mapObj.gpstime}}</span></el-col>
                <el-col :span="4"><span>状态：{{mapObj.status_text}}</span></el-col>
                <el-col :span="4"></el-col>
            </el-row>
            <el-row class="groupStatusData">
                <el-col :span="10">
                    <span class="module_subtitle">车辆数据采集</span>
                    <div class = "line_show">
                        <x-pie-component  :id="id_pie" :series_data="series_data"></x-pie-component>
                    </div>
                </el-col>
                <el-col :span="14">
                    <span class="module_subtitle" title="当前10分钟内实时数据，每10秒更新数据">车辆实时数据</span>
                    <div class = "line_show">
                        <x-line-component :id="id_line" :series_data="actual_series_data" :legendShow ='legendShowActual'></x-line-component>
                    </div>
                </el-col>
            </el-row>
        </div>
        <div class="right_content">
            <div style="position: relative;">
                <span class="module_title">数据风险趋势</span><span class="explain" @mouseover = 'remarkShowFn(2)' @mouseout = 'remarkShowFn(2)'></span>
                <span class="title_remark" v-show = 'trendShow'>
                    <label class="remaek_em">数据风险趋势：</label>通过设置预警值将数据进行简单过滤，呈现对应数据结果
                </span>
            </div>            
            <div class="info_div fr">
                <span class="span_color">预警值：</span>
                <span class="span_color trend_text">最高</span>
                <input type="number" class="inputText trend_input" v-model='max_value'>
                <span class="span_color trend_text">最低</span>
                <input type="number" class="inputText trend_input" v-model='min_value'>
                <span class="btn_save trend_set" @click="setClick()">配置</span>
                <el-date-picker type='date' placeholder='选择日期' v-model='date_range' :picker-options='pickerOptions0' id='select_date' @change='dateChange'></el-date-picker>
            </div>
            <div class = "line_show">
                <x-line-component :id="trend_line" :series_data="line_series_data"></x-line-component>
            </div>
        </div>
    </div>
</template>
<script>
    import bMapComponent from '../common/BMapComponent.vue'
    import xPieComponent from '../common/pieChart.vue'
    import xLineComponent from '../common/lineChart.vue'
    //import xActualComponent from '../common/actualLineChart.vue'
    export default {
        name: 'application',
        created:function(){
            this.$nextTick(() => {
                this.overviewNumFn();
                this.listFn();
                this.drawTrendLineFn();
            });
        },
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
            return {
                date_range: Y+'-'+ m +'-' + d,
                pickerOptions0:{
                    disabledDate(time) {
                        return time.getTime() > Date.now();
                    }
                },
                longitude:116.404,
                latitude:39.915,
                description:'天安门',
                deviceInfo: [],
                check_deviceid:'',
                check_device_desc:'',
                id_line:'actual_line',
                trend_line:'trend_line',
                id_pie:'actual_pie',
                mapObj:{
                    device_desc:'',
                    gpstime:'',
                    status_text:''
                },
                overviewData:{
                    device_count:'',
                    label_count:'',
                    label_data_count:'',
                    label_data_invalid_count:'',
                },
                series_data:[],
                label_ids:[],
                label_datas: {},
                timer_id:0,//调用定时器
                timer_line_id:0,//调用定时器
                actual_series_data:{},//实时数据折线图
                legendShowActual:false,
                line_series_data:{},//风险趋势折线图
                max_value:'26.5',//最高值
                min_value:'20',//最低值
                overviewShow:false,
                statusShow:false,
                trendShow:false
            }
        },
        methods:{
            listFn:function(){
                this.$axios.post(this.$API.dataOverview.overviewDeviceList,{}).then( (res) =>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    let arr = res.data.rows;
                    this.deviceInfo = [];
                    for(var i=0;i<arr.length;i++) {
                        if(i == 0){
                            arr[i].classStatus = true;
                            this.check_deviceid = arr[i].device_id;
                            this.check_device_desc = arr[i].device_desc;
                            this.mapFn(0);
                            this.drawLineFn();
                            this.drawPieFn();
                            this.timer_id = setInterval(()=>{
                                this.mapFn(1);
                            },5000);
                            this.timer_line_id = setInterval(()=>{
                                this.drawLineFn();
                            },10000);
                        }else{
                            arr[i].classStatus = false;
                        }
                        this.deviceInfo.push(arr[i]);
                    }
                },function(res){
                    console.log(res)
                });
            },
            overviewNumFn:function(){
                this.$axios.get(this.$API.dataOverview.overviewNum).then( (res) =>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    this.overviewData.device_count = res.data.device_count;
                    this.overviewData.label_count = res.data.label_count;
                    this.overviewData.label_data_count = res.data.label_data_count;
                    this.overviewData.label_data_invalid_count = res.data.label_data_invalid_count;
                },function(res){
                    console.log(res)
                });
            },
            //调用地图接口
            mapFn:function(status){
                this.$axios.post(this.$API.dataOverview.mapData,{
                    device_id: this.check_deviceid
                }).then( (res) =>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    if(status == 0){
                        this.longitude = res.data.longitude;
                        this.latitude = res.data.latitude;
                    }
                    this.mapObj.gpstime = res.data.gpstime;
                    if(res.data.status == 0){
                        this.mapObj.status_text = '离线';
                    }else{
                        this.mapObj.status_text = '正常';
                    }
                },function(res){
                    console.log(res)
                });
            },
            //调用饼图
            drawPieFn:function(){
                let labelidarray = [this.check_deviceid];
                this.$axios.post(this.$API.dataOverview.overviewPie,{'device_id': this.check_deviceid}).then( (res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    this.series_data = res.data;
                });
            },
            //调用实时刷新折线图
            drawLineFn:function(){
                //实时刷新折线图调用
                this.$axios.post(this.$API.dataOverview.actualLine,{'device_id': this.check_deviceid}).then( (res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    this.actual_series_data = res.data.actionResult;
                });
            },
            //调用风险趋势折线图
            drawTrendLineFn:function(){
                Number(this.max_value)
                if(this.max_value == '' || this.min_value == ''){
                    alert('请填写预警值');
                    return;
                }else if(Number(this.max_value) < Number(this.min_value)){
                    alert('最大值不能小于最小值');
                    return;
                }
                this.$axios.post(this.$API.dataOverview.trendLine,{
                    "startTimeAt": this.date_range, 
                    "endTimeAt": this.date_range, 
                    "device_id": this.check_deviceid, 
                    "max_value": this.max_value, 
                    "min_value": this.min_value
                }).then( (res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    this.line_series_data = res.data;
                    this.line_series_data.type = 'area';
                });
            },
            //点击设备
            deviceClick:function(index){
                for(var i=0;i<this.deviceInfo.length;i++) {
                    if(i == index){
                        this.deviceInfo[i].classStatus = true;
                        this.check_deviceid = this.deviceInfo[i].device_id;
                        this.check_device_desc = this.deviceInfo[i].device_desc;
                        this.mapFn();
                        this.drawPieFn();
                        this.drawLineFn();
                    }else{
                        this.deviceInfo[i].classStatus = false;
                    }
                }
            },
            setClick(){
                this.drawTrendLineFn();
            },
            //日期选择
            dateChange:function(val){
                this.date_range = val;
                this.drawTrendLineFn();
            },
            //提示语
            remarkShowFn:function(status){
                if(status == 0){
                    this.overviewShow = !this.overviewShow;
                }else if(status == 1){
                    this.statusShow = !this.statusShow;
                }else if(status == 2){
                    this.trendShow = !this.trendShow;
                }
            }
        },
        components: {
            bMapComponent,
            xPieComponent,
            xLineComponent
        },
        beforeDestroy:function(){
          clearInterval(this.timer_id);
          clearInterval(this.timer_line_id);
        }
    }
</script>
<style>
    .location_list{
        height: 530px;
        overflow: auto;
    }
    .mapText{
        margin: 0 20px 40px 20px;
    }
    .mapText span{
        display: inline-block;
        font-size: 16px;
        color: #333;
    }
    .overview_module{
        margin:0 10px 20px 10px;
    }
    .overview_module .label_content{
        padding: 0 10px;
    }
    .overview_module .label_div{
        display: inline-block;
        width: 100%;
        height: 100px;
        border:1px solid #d6d7dd;
    }
    .overview_module .label_div i{
        display: inline-block;
        background: url(../../assets/images/overviewIcon.png) no-repeat;
        width: 72px;
        height: 74px;
        margin: 13px 20px;
        float: left;
    }
    .overview_module .label_div .overview_i2{
        background-position: -72px 0;
    }
    .overview_module .label_div .overview_i3{
        background-position: -144px 0;
    }
    .overview_module .label_div .overview_i4{
        background-position: -216px 0;
    }
    .overview_module .label_div span{
        display: block;
        font: bold 36px/36px 'Microsoft YaHei';
        color: #333;
    }
    .overview_module .label_div .overview_text{
        margin: 20px 0 10px 0;
        font: 18px/18px 'Microsoft YaHei';
    }
    .subtitle_text{
        font-weight: normal;
    }
    .groupStatusData{
        margin-bottom: 20px;
    }
    .trend_set{
        margin: 0 60px 0 20px;
    }
    .trend_input{
        width: 80px;
    }
    .trend_text{
        margin: 0 5px 0 20px;
    }
    .info_div{
        margin-right:20px;
    }
</style>
