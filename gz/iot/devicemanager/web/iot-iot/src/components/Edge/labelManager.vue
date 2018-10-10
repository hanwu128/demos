<template>
    <div>
        <div class="page_title_content">
          <span class="page_title">边缘分析&nbsp;&gt;&nbsp;<router-link to="/edge/labelManager" class="btn_color">标签管理</router-link></span>
        </div>
        <div class="right_content label_list_div">
            <span class="module_title">标签列表</span>
            <!-- <el-date-picker type='date' placeholder='选择日期范围' v-model='date_range' :picker-options='pickerOptions0' id='select_date' @change='dateChange'></el-date-picker> -->
            <div class="handle_module_list">
                <table class="list">
                    <thead>
                    <tr>
                       <!--  <th style='width:5%'></th> -->
                        <th style='width:12%'>标签ID</th>
                        <th style='width:12%'>标签名称</th>
                        <th style='width:12%'>当前值</th>
                        <th style='width:12%'>最高值</th>
                        <th style='width:12%'>最低值</th>
                        <th style='width:20%'>网关设备ID</th>
                        <th style='width:5%'>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(task,index) in taskInfo">
                        <!-- <td><input type="checkbox"  @click="labelCheck(index)" v-model='task.state'></td> -->
                        <td>{{ task.labelid }}</td>
                        <td>{{ task.labelname }}</td>
                        <td><p class="td_p"><label :class="task.td_i">{{ task.currentvalue }}</label><br><span class="td_span">{{ task.updatestampstrshow }}</span></p><i :class="task.td_i"></i></td>
                        <td>{{ task.maxvalue }}<br><span class="td_span">{{ task.maxstampshow }}</span></td>
                        <td>{{ task.minvalue }}<br><span class="td_span">{{ task.minstampshow }}</span></td>
                        <td>{{ task.deviceid }}</td>
                        <td :class="'text-center'"><span class="btn_color" @click="detailClick(index)">详情</span></td>
                    </tr>
                    </tbody>
                </table>
                <el-pagination @current-change="handleCurrentChange" :current-page="currentpage" :page-size="rowcount" :total="total_num" layout="total,prev, pager, next, jumper" id='label_pagination'></el-pagination>
            </div>
        </div>
        <!-- <div class='btn_div_left'>
            <a href='javascript:;' class='btn_create' @click="drawLineClick">绘制折线图</a>
        </div>  -->  
        <div class="right_content">
            <span class="module_title">实时数据</span><span >（当前10分钟内实时数据，每5秒更新数据）</span>
            <div class = "line_show">
                <el-table v-loading="loading2" v-if="loading2" :data="loadingArray" element-loading-text="拼命加载中" style="width: 100%;height:400px;">
                </el-table>
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
    import XChart from './chart.vue'
    import XAnimateChart from './animateChart.vue'
    export default {
        created:function () {
            this.$nextTick(() => {
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
                id: 'test',
                id_animate:'actual_line',
                label_ids:[],
                label_datas: {},
                categories_data:[],
                series_data:[],
                rowcount: 5,
                currentpage: 1,
                total_num:0,
                timer_id:0,
                loadingArray:[1],
                loading2:true
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
                this.$axios.post(this.$API.labelManage.labelList,{'what_day': this.date_range,'rowcount': this.rowcount,'currentpage': this.currentpage}).then( (res)=>{
                    this.total_num = res.data.totalCount;
                    let arrTask = res.data.actionResultData;
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
                        //arrTask[i].state = false;
                        this.taskInfo.push(arrTask[i]);
                    }
                    if(_status == 0){
                        //实时刷新折线图调用
                        this.$axios.post(this.$API.labelManage.labelLine,{'flagTimeBatch': 30,'labelidarray':labelidarray}).then( (res)=>{
                            let option_data = res.data.actionResultData;
                            this.label_ids = labelidarray;
                            this.label_datas.labelidarray = labelidarray;
                            this.label_datas.categories_data = option_data.listX_TimeAt;
                            this.label_datas.series_data = option_data.listY_oC;
                            this.loading2 = false;
                        });
                    }
                });
            },
            selectFn: function(value){
                console.log(value);

            },
            handleClick:function () {

            },
            detailClick:function (index) {
                this.$router.push({path:'/edge/labelDetail',query: { 'label_id': this.taskInfo[index].label_id}})
            },
            cancleClick:function(){
                this.dialogShow = false;
            },
            searchClick:function(){
            },
            labelCheck:function(index){
                if(this.taskInfo[index].state == true && this.checkNum<5){
                    this.checkNum++;
                }else if(this.taskInfo[index].state == false){
                    this.checkNum--;
                }else{
                    this.taskInfo[index].state =false;
                    alert('最多可选5条');
                }
            },
            drawLineClick:function(){
                //let _self = this;
                //let what_day = this.taskInfo[0].what_day;
                //let labelidarray = [];
                // for(var i=0;i<this.taskInfo.length;i++){
                //     if(this.taskInfo[i].state == true){
                //         labelidarray.push(this.taskInfo[i].labelid);
                //     }
                // }
                // if(labelidarray.length == 0){
                //     alert('请选择标签');
                //     return;
                // }
                // this.$axios.post("http://223.203.218.93/devicemanager/SensorTemperatureDayVOController/bindReportDataSourceminitue.do",{'what_day': what_day,'labelidarray':labelidarray}).then(function(res){
                //     let option_data = res.data.actionResultData;
                //     _self.categories_data = option_data.listX_TimeAt;
                //     _self.series_data = option_data.listY_oC;
                // });
                //实时刷新折线图调用
                // this.$axios.post("http://223.203.218.93/devicemanager/SensorTemperatureDayVOController/listSensorTemperatureHistoryBySensorstampwithInterval.do",{'flagTimeBatch': 30,'labelidarray':labelidarray}).then(function(res){
                //     let option_data = res.data.actionResultData;
                //     _self.label_ids = labelidarray;
                //     _self.label_datas.labelidarray = labelidarray;
                //     _self.label_datas.categories_data = option_data.listX_TimeAt;
                //     _self.label_datas.series_data = option_data.listY_oC;
                //     console.log(res);
                // });
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
            }
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
</style>