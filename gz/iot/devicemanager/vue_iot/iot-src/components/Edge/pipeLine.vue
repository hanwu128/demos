<template>
    <div>
        <div class="page_title_content">
          <span class="page_title">边缘分析&nbsp;&gt;&nbsp;<router-link to="/edge/pipeLine" class="btn_color">数据流管理</router-link></span>
        </div>
        <div class="right_content">
            <div class="header">
                <input class="search_text" type="text" v-model="searchText" placeholder="请输入数据流名称或网关设备名称">
                <span class='btn_save' @click="searchClick()">搜索</span>
                <span class="btn_create fr" @click="createClick()">创建数据流</span>
            </div>
            <div class='pipeline_list'>
                <!-- <table class="list">
                    <thead>
                    <tr>
                        <th style='width:5%'>编号</th>
                        <th style='width:15%'>数据流名称</th>
                        <th style='width:15%'>网关设备名称</th>
                        <th style='width:20%'>处理模块名称</th>
                        <th style='width:8%'>状态</th>
                        <th style='width:12%'>数据流描述</th>
                        <th style='width:15%'>创建时间</th>
                        <th style='width:10%'>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(task,index) in taskInfo">
                        <td>{{ index + 1 }}</td>
                        <td>{{ task.streamingname }}</td>
                        <td>{{ task.accesskey }}</td>
                        <td>{{ task.appname }}</td>
                        <td>{{task.pipestatus_text}}</td>
                        <td>{{ task.streamingdesc }}</td>
                        <td>{{ task.createtimeatshow }}</td>
                        <td :class="'text-center'"><span class="btn_color" :class="{'disabled':task.classStatus}" @click="controlClick(index)">{{task.status_text}}</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="detailClick(index)">详情</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="deleteClick(index)">删除</span></td>
                    </tr>
                    </tbody>
                </table> -->
                <!-- 表格组件开始 -->
                <el-table
                    :data="taskInfo"
                    border
                    empty-text=' '
                    style="width: 100%;border-left: 1px solid #d6d7dd;border-top: 1px solid #d6d7dd;"
                    :default-sort = "{prop: 'createtimeatshow', order: 'descending'}"
                    header-row-class-name = 'header-row'
                    @sort-change="sortChangeFn"
                    >
                    <el-table-column
                        align="center"
                        type="index"
                        label="编号"
                        width="80"
                    >
                    <template slot-scope="scope">
                        {{scope.$index+1+(currentpage-1)*rowcount}}
                    </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="streamingname"
                        min-width="130"
                        label="数据流名称"
                        sortable="custom"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="accesskey"
                        min-width="130"
                        label="网关设备名称"
                        sortable="custom"
                      >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="appname"
                        sortable="custom"
                        min-width="200"
                        label="处理模块名称"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="pipestatus_text"
                        width="100"
                        label="状态"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        min-width="130"
                        prop="streamingdesc"
                        label="数据流描述"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        min-width="160"
                        prop="createtimeatshow"
                        sortable="custom"
                        label="创建时间"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        min-width="150"
                        label="操作"
                    >
                    <template slot-scope="scope">
                        <span class="btn_color" :class="{'disabled':scope.row.classStatus}" @click="controlClick(scope.$index)">{{scope.row.status_text}}</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="detailClick(scope.$index)">详情</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="deleteClick(scope.$index)">删除</span>
                    </template>
                    </el-table-column>
                </el-table>
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentpage" :page-sizes="[10,20,50,80,100]" :page-size="rowcount" :total="total_num" layout="total,sizes, prev, pager, next, jumper" style='margin-top:10px;'></el-pagination>
                <div class="no_data" style="display: block;" v-if="show_no_data"><i :class="{'active':search_status,'loading':show_loading}"></i></div>
                <!-- 表格组件结束 -->
            </div>
        </div>
    </div>
</template>
<script>
    export default {
        created:function () {
            //this.listFn();  
        },
        name: 'taskList',
        data () {
            return{
                taskInfo:[],
                dialogInfo:{},
                dialogShow:false,
                searchText:'',
                btn_disabled:false,
                // 暂无数据开始
                sort_orderby:'',
                sort_rule:'descending',
                rowcount: 10,
                currentpage: 1,
                total_num:0,
                show_no_data:true,
                search_status:false,
                show_loading:true,
                // 暂无数据结束
                search_flag:0
            }
        },
        methods: {
            listFn(status){//2为自动刷新
                if(status != 2){
                    this.taskInfo = [];
                    this.show_loading = true;
                    this.search_status = false;
                    this.show_no_data = true;
                }
                //判断是否为搜索
                if(status == 1){
                  this.currentpage = 1;
                }
                this.$axios.post(this.$API.pipeLineManage.pipeLineList,{
                    'accesskey': this.searchText,
                    'pagesize':this.rowcount,
                    'current':this.currentpage,
                    'sort_orderby':this.sort_orderby,
                    'sort_rule':this.sort_rule
                }).then( (res)=> {
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                        return;
                    }
                    let arrTask = res.data.actionResult;
                    if(arrTask == undefined || arrTask.length == 0){
                        if(this.search_flag==1){
                          this.search_status = true;
                        }else{
                          this.search_status = false;
                        }
                        this.show_loading = false;
                        this.show_no_data = true;
                    }else{
                        this.show_no_data = false;
                        this.total_num = res.data.total;
                        this.taskInfo=[];
                        for(var i=0;i<arrTask.length;i++) {
                            arrTask[i].classStatus = false;
                            if(arrTask[i].status == 'true'){
                                arrTask[i].status_text = '暂停';
                                arrTask[i].pipestatus_text = '运行中';
                            }else if(arrTask[i].status == 'false'){
                                arrTask[i].status_text = '启动';
                                arrTask[i].pipestatus_text = '已暂停';         
                            }else{
                                arrTask[i].classStatus = true;
                                arrTask[i].status_text = '等待';
                                arrTask[i].pipestatus_text = '下发中';
                            }
                            this.taskInfo.push(arrTask[i]);
                        }
                    }
                });
            },
            deleteClick:function (index) {
                let id = parseInt(this.taskInfo[index].topid,10);
                this.$confirm('确定要删除数据流“'+ this.taskInfo[index].streamingname +'”吗？删除后设备“'+ this.taskInfo[index].accesskey +'”将无法上报数据。', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                    this.$axios.post(this.$API.pipeLineManage.pipeLineDelete,{
                        'id': id
                    }).then( (res)=> {
                        if(res.data.code == 420){
                            this.$router.push({path: '/login'});
                        }
                        this.taskInfo.splice(index,1);
                        this.total_num = this.total_num-1;
                        this.listFn();
                    })
                }).catch(() => {
                   return      
                });
            },
            handleSizeChange(val) {
                this.rowcount = val;
                this.listFn();
            },
            handleCurrentChange(val) {
                this.currentpage = val;
                this.listFn();
            },
            sortChangeFn:function(res){
                //{ column, prop, order }
                this.sort_orderby = res.prop;
                this.sort_rule = res.order;
                this.listFn();
            },
            createClick:function () {
                this.$router.push({path:'/edge/pipeLineCreate'});
            },
            detailClick:function (index) {
           		let id = this.taskInfo[index].topid;
                let deviceid = this.taskInfo[index].deviceidcode;
                let appname = this.taskInfo[index].appname;
                this.$router.push({path:'/edge/pipeLineDetail',query: {id:id}})
            },
            searchClick:function(){
                this.search_flag = 1;
                this.listFn(1);
            },
            //启动暂停
            controlClick:function(index){
                let old_status = this.taskInfo[index].status;
                let device_id = this.taskInfo[index].deviceidcode;
                let app_name = this.taskInfo[index].appname;
                let handle_status='';
                if(old_status == 'true'){
                    handle_status = 'false';
                }else if(old_status == 'false'){
                    handle_status = 'true';
                }else{
                    return;
                }
                this.$axios.post("http://223.203.218.93/devicemanager/TaskController/run_app.do",{'device_id':device_id,'app_name':app_name,'status':handle_status}).then( (res)=>{
                    // if(res.data.result == true && old_status == 'true'){
                    //     this.taskInfo[index].status_text = '启动';
                    //     this.taskInfo[index].status = 'false';
                    //     this.taskInfo[index].pipestatus_text = '已暂停';

                    // }else if(res.data.result == true && old_status == 'false'){
                    //     this.taskInfo[index].status_text = '暂停';
                    //     this.taskInfo[index].status = 'true';
                    //     this.taskInfo[index].pipestatus_text = '运行中';         
                    // }else{
                    //     alert('操作失败');
                    // }
                    if(res.data.result == true){
                        this.taskInfo[index].classStatus = true;
                        this.taskInfo[index].status_text = '等待';
                        this.taskInfo[index].pipestatus_text = '下发中';
                        let time_list = setTimeout(()=>{
                            this.listFn(2);
                        },5000);
                    }else{
                        this.$message.error('操作失败！');
                    }
                });
            }
        }
    }
</script>
<style scoped>
    .header,.pipeline_list{
        margin:0 20px 20px 20px;
    }
    .searchSelect {
        position: relative;
        left:50px;
    }
    .pipeLinedialogTask{
        width: 600px;
        min-height: 300px;
        margin-left: -300px;
    }
</style>