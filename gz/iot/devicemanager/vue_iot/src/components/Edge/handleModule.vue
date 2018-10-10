<template>
    <div>
        <div class="page_title_content">
          <span class="page_title">边缘分析&nbsp;&gt;&nbsp;<router-link to="/edge/processModule" class="btn_color">处理模块管理</router-link></span>
        </div>
        <div class="right_content">
            <div class="header">
                <input class="search_text" type="text" placeholder="请输入处理模块名称" v-model='searchTerms.appname'>
                <select class="search_select" v-model='searchTerms.apptype'>
                    <option value=''>所有处理模块</option>
                    <option value='shadow'>镜像</option>
                    <option value='stream'>流式</option>
                    <option value='edgent'>固件</option>
                </select>
                <!-- <select class="search_select" v-model='searchTerms.taskstatus'>
                    <option value=''>状态选择</option>
                    <option value='successSendMqtt'>运行中</option>
                    <option value='2'>已卸载</option>
                </select> -->
                <span class='btn_save' @click="searchClick()">搜索</span>
                <span class="btn_create fr" @click="createClick()">创建处理模块</span>
            </div>
            <div class="handle_module_list">
                <!-- 表格组件开始 -->
                <el-table
                    :data="taskInfo"
                    ref="taskInfo"
                    border
                    empty-text=' '
                    style="width: 100%;border-left: 1px solid #d6d7dd;border-top: 1px solid #d6d7dd;"
                    header-row-class-name = 'header-row'
                    header-cell-class-name='select-single'
                    :default-sort = "{prop: 'createtimeatshow', order: 'descending'}"
                    @sort-change="sortChangeFn"
                    @selection-change="handleSelectionChange"
                    @select='selectFn'
                    @row-click="rowClickFn"
                    >
                    <!-- <el-table-column
                      align="center"
                      type="selection"
                      label="全选"
                      width="55">
                    </el-table-column> -->
                    <el-table-column
                        align="center"
                        type="index"
                        label="编号"
                        width="80"
                    >
                    <template slot-scope="scope">
                        {{scope.$index+1+(searchTerms.current-1)*searchTerms.pagesize}}
                    </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="appname"
                        label="处理模块名称"
                        sortable="custom"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="apptype"
                        label="类型"
                        width="200"
                        sortable="custom"
                    >
                    <template slot-scope="scope">
                        {{scope.row.apptype_text}}
                    </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="appversion"
                        width="200"
                        label="版本号"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="createtimeatshow"
                        sortable="custom"
                        label="创建时间"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        label="操作"
                    >
                    <template slot-scope="scope">
                        <span class="btn_color" @click="upgradeClick(scope.$index)">安装</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="detailClick(scope.$index)">详情</span>
                    </template>
                    </el-table-column>
                </el-table>
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="searchTerms.current" :page-sizes="[10,20,50,80,100]" :page-size="searchTerms.pagesize" :total="total_num" layout="total,sizes, prev, pager, next, jumper" style='margin-top:10px;'></el-pagination>
                <div class="no_data" style="display: block;" v-if="show_no_data"><i :class="{'active':search_status,'loading':show_loading}"></i></div>
                <!-- 表格组件结束 -->
            </div>
            <!-- <div v-if= "dialogShow" class="dialogTask handle_dialogTask">
                <div class="catlog"><h3>详情</h3><span class="cancel_btn" @click="cancleClick">×</span></div>
                <ul>
                    <li>
                        <label>处理模块名称：</label><span>{{dialogInfo.app.appname}}</span>
                    </li>
                    <li>
                        <label>处理模块类型：</label><span>{{dialogInfo.app.apptype_text}}</span>
                    </li>
                    <li>
                        <label>状态：</label><span></span>
                        <label class="label_l">版本信息：</label><span>{{dialogInfo.app.appversion}}</span>
                    </li>
                    <li>
                        <label>处理模块描述：</label><span>{{dialogInfo.app.appdesc}}</span>
                    </li>
                    <li>
                        <label>数据源：</label>
                        <span class = 'dialog_table'>
                            <table class="dialog_list">
                                <thead>
                                <tr>
                                    <th>数据源名称</th>
                                    <th>显示名称</th>
                                    <th>默认值</th>
                                    <th>类型</th>
                                    <th>单位</th>
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
                        </span>
                    </li>
                </ul>
                <div class='btn_div_dialog'>
                    <a href='javascript:;' class='btn_back' @click="cancleClick">关闭</a>
                </div>
            </div> -->
        </div>
    </div>
</template>
<script>
    export default {
        created:function () {
            //this.listFn();
            console.log(this.$store.state.goodsList);
        },
        name: 'taskList',
        data () {
            return{
                taskInfo:[],
                dialogInfo:{
                    app:{
                        appname:''
                    }
                },
                searchTerms:{
                    appname:'',
                    apptype:'',
                    taskstatus:'',
                    sort_orderby:'',
                    sort_rule:'descending',
                    pagesize: 10,
                    current: 1,
                },
                dialogShow:false,
                value: 0,
                // 暂无数据开始
                total_num:0,
                show_no_data:true,
                search_status:false,
                show_loading:true,
                // 暂无数据结束
                multipleSelection:[]
            }
        },
        methods: {
            listFn(status){
                this.taskInfo = [];
                this.search_status = false;
                this.show_loading = true;
                this.show_no_data = true;
                //判断是否为搜索
                if(status == 1){
                  this.current = 1;
                }
                this.$axios.post(this.$API.processManage.processList,this.searchTerms).then( (res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    let arrTask = res.data.actionResult;
                    if(arrTask == undefined || arrTask.length == 0){
                        if(status==1){
                          this.search_status = true;
                        }else{
                          this.search_status = false;
                        }
                        this.show_loading = false;
                        this.show_no_data = true;
                    }else{
                        this.show_no_data = false;
                        this.total_num = res.data.total;
                        this.taskInfo = [];
                        for(var i=0;i<arrTask.length;i++) {
                            if(arrTask[i].appstatus == 'true'){
                                arrTask[i].status_text = '暂停';
                                arrTask[i].appstatus_text = '运行中';
                            }else{
                                arrTask[i].status_text = '启动';
                                arrTask[i].appstatus_text = '已暂停';         
                            }
                            if(arrTask[i].apptype == 'stream'){
                                arrTask[i].apptype_text = '流式';
                            }else if(arrTask[i].apptype == 'shadow'){
                                arrTask[i].apptype_text = '镜像';
                            }else{
                                arrTask[i].apptype_text = '固件';
                            }
                            this.taskInfo.push(arrTask[i]);
                        }
                    }
                });

            },
            createClick() {
                this.$router.push({path:'/edge/handleCreate'});
            },
            selectFn(selection,row){
                if(this.multipleSelection.length>=1){
                    this.$refs.taskInfo.clearSelection();
                    this.$refs.taskInfo.toggleRowSelection(row);
                }
                console.log(this.multipleSelection);
            },
            handleSizeChange(val) {
                this.searchTerms.pagesize = val;
                this.listFn();
            },
            handleCurrentChange(val) {
                this.searchTerms.current = val;
                this.listFn();
            },
            sortChangeFn(res){
                //{ column, prop, order }
                this.searchTerms.sort_orderby = res.prop;
                this.searchTerms.sort_rule = res.order;
                this.listFn();
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            rowClickFn(row){
                //this.selectFn(row,row);
                // this.$refs.taskInfo.clearSelection();
                // this.$refs.taskInfo.toggleRowSelection(row);
            },
            // handleClick:function (index) {
            //     let old_status = this.taskInfo[index].appstatus;
            //     let device_id = this.taskInfo[index].deviceid;
            //     let app_name = this.taskInfo[index].appname;
            //     let handle_status='';
            //     if(old_status == 'true'){
            //         handle_status = 'false';
            //     }else{
            //         handle_status = 'true';
            //     }
            //     this.$axios.post("http://223.203.218.93/devicemanager/TaskController/run_app.do",{'device_id':device_id,'app_name':app_name,'status':handle_status}).then( (res)=>{
            //         if(res.data.result == true && old_status == 'true'){
            //             this.taskInfo[index].status_text = '启动';
            //             this.taskInfo[index].appstatus = 'false';
            //             this.taskInfo[index].appstatus_text = '已暂停';

            //         }else if(res.data.result == true && old_status == 'false'){
            //             this.taskInfo[index].status_text = '暂停';
            //             this.taskInfo[index].appstatus = 'true';
            //             this.taskInfo[index].appstatus_text = '运行中';         
            //         }else{
            //             alert('操作失败');
            //         }
            //     });
            // },
            detailClick:function (index) {
                let dialog_id = this.taskInfo[index].id;
                this.$router.push({path:'/edge/processDetail',query: { id:dialog_id}});
                // this.dialogShow = true;
                // this.$emit("backgroundFn",true);
                // this.$axios.post(this.$API.pipeLineManage.createDescApp,{
                //     'id':dialog_id,
                //     'appid':dialog_appid
                // }).then( (res)=>{
                //     let arrDialog = res.data.actionResult;
                //     if(arrDialog.app.apptype == 'stream'){
                //         arrDialog.app.apptype_text = '流式';
                //     }else{
                //         arrDialog.app.apptype_text = '镜像';
                //     }
                //     this.dialogInfo = arrDialog;
                // })
            },
            cancleClick:function(){
                this.dialogShow = false;
            },
            searchClick:function(){
                this.listFn(1);
            },
            //升级
            upgradeClick:function(index){
                this.$router.push({path:'/edge/upgradeModule',query: { appid: this.taskInfo[index].id}})
            }
        }
    }
</script>

<style scoped>
    .header,.handle_module_list {
        margin:0 20px 20px 20px;
    }
</style>