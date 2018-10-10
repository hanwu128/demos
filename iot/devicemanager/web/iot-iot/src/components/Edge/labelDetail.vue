<template>
    <div>
        <div class="page_title_content">
          <span class="page_title">边缘分析&nbsp;&gt;&nbsp;<router-link to="/edge/labelManager" class="btn_color">标签管理</router-link>&nbsp;&gt;&nbsp;详情</span>
        </div>
        <div class="right_content">
            <span class="module_title">实时数据</span><span >（当前10分钟内实时数据，每5秒更新数据）</span>
            <div class = "line_show line_show_detail">
                <el-table v-loading="loading2" v-if="loading2" :data="loadingArray" element-loading-text="拼命加载中" style="width: 100%;height:400px;">
                </el-table>
                <x-animate-chart :id_animate='id_animate' :label_ids='label_ids' :label_datas='label_datas'></x-animate-chart>
            </div>
        </div>   
        <div class="right_content">
            <span class="module_title">标签详情列表</span>
            <!-- <el-date-picker type="daterange" align="right" placeholder="选择日期范围"  v-model='date_range_detail' :picker-options="pickerOptions2" id="select_date_detail" @change='dateDetailChange'></el-date-picker> -->
            <el-date-picker type='date' placeholder='选择日期' v-model='date_range' :picker-options='pickerOptions0' id='select_date_detail' @change='dateDetailChange'></el-date-picker>
            <div class="handle_module_list">
                <table class="list">
                    <thead>
                    <tr>
                        <th style='width:5%'>编号</th>
                        <th style='width:45%'>监测值</th>
                        <th style='width:50%'>采集时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(task,index) in taskInfo">
                        <td>{{ rowcount * (currentpage-1) + index + 1 }}</td>
                        <td>{{ task.value }}</td>
                        <td>{{ task.createstampshow }}</td>
                    </tr>
                    </tbody>
                </table>
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentpage" :page-sizes="[10,20,50,80,100]" :page-size="rowcount" :total="total_num" layout="total,sizes, prev, pager, next, jumper" id='labeldetail_pagination'></el-pagination>
            </div>
        </div>
        <div class='btn_div'>
            <router-link to="/edge/labelManager" class='btn_back'>返回</router-link>
        </div>
    </div>
</template>
<script>
    import XAnimateChart from './animateChart.vue'
    export default {
        created:function () {
            let labelidarray = [];
            labelidarray.push(this.$route.query.label_id);
            this.$nextTick(() => {
                this.labelDetailListFn();
                //实时刷新折线图调用
                this.$axios.post(this.$API.labelManage.labelLine,{'flagTimeBatch': 30,'labelidarray':labelidarray}).then( (res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    let option_data = res.data.actionResultData;
                    this.label_ids = labelidarray;
                    this.label_datas.labelidarray = labelidarray;
                    this.label_datas.categories_data = option_data.listX_TimeAt;
                    this.label_datas.series_data = option_data.listY_oC;
                    this.loading2 = false;
                });
            });
        },
        name: 'labelDetail',
        data () {
            //let what_day = this.$route.query.what_day;
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
                taskInfo:[],
                searchTime: '',
                date_range: Y+'-'+ m +'-' + d,
                rowcount: 10,
                currentpage: 1,
                total_num:0,
                pickerOptions0:{
                    disabledDate(time) {
                        return time.getTime() > Date.now();
                    }
                },
                id_animate:'detail_line',
                label_ids:[],
                label_datas: {},
                loadingArray:[1],
                loading2:true
                //startTime: what_day,
                //endTime: what_day,
                // date_range_detail: [what_day,what_day],
                // pickerOptions2: {
                //     disabledDate(time) {
                //         return time.getTime() > Date.now();
                //     },
                //     shortcuts: [{
                //         text: '最近一周',
                //         onClick(picker) {
                //           const end = new Date();
                //           const start = new Date();
                //           start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                //           picker.$emit('pick', [start, end]);
                //         }
                //       }, {
                //         text: '最近一个月',
                //         onClick(picker) {
                //           const end = new Date();
                //           const start = new Date();
                //           start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                //           picker.$emit('pick', [start, end]);
                //         }
                //       }, {
                //         text: '最近三个月',
                //         onClick(picker) {
                //           const end = new Date();
                //           const start = new Date();
                //           start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                //           picker.$emit('pick', [start, end]);
                //         }
                //     }]
                // }
            }
        },
        components: {
            XAnimateChart
        },
        mounted() {
            console.log(this.startTime);
        },
        methods: {
            labelDetailListFn: function(){
                let label_id = this.$route.query.label_id;
                this.$axios.post(this.$API.labelManage.labelDetailList,{'label_id':label_id,'startTimeAt': this.date_range,'endTimeAt': this.date_range,'rowcount': this.rowcount,'currentpage': this.currentpage}).then( (res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    let arrTask = res.data.actionResultData;
                    this.taskInfo = [];
                    this.total_num = res.data.totalCount;
                    for(var i=0;i<arrTask.length;i++) {
                        this.taskInfo.push(arrTask[i]);
                    }
                });
            },
            handleClick:function () {
            },
            // dateDetailChange:function(val){
            //     this.startTime = val.split(' - ')[0];
            //     this.endTime = val.split(' - ')[1];
            //     this.labelDetailListFn();
            // },
            handleSizeChange(val) {
                console.log(`每页 ${val} 条`);
                this.rowcount = val;
                this.labelDetailListFn();
            },
            handleCurrentChange(val) {
                console.log(`当前页: ${val}`);
                this.currentpage = val;
                this.labelDetailListFn();
            },
            dateDetailChange:function(val){
                this.date_range = val;
                this.labelDetailListFn();
            }
        }
    }
</script>

<style scoped>
    .header,.handle_module_list {
        margin:0 20px 20px 20px;
    }
    #select_date_detail{
        display:inline-block;
        width:300px;
        float:right;
        margin:0 20px 20px 0;
    }
    #labeldetail_pagination{
        margin: 10px 0 0 -5px;
    }
    .line_show_detail{
        width:98.5%;
    }
</style>