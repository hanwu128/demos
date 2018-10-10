<template>
    <div>
        <div class="page_title_content">
          <span class="page_title">边缘分析&nbsp;&gt;&nbsp;<router-link to="/edge/processModule" class="btn_color">处理模块管理</router-link>&nbsp;&gt;&nbsp;安装</span>
        </div>
        <div class="right_content">
            <span class="module_title">网关设备列表</span>
            <div class="list_div">
                <!-- <table class="list">
                    <thead>
                        <tr>
                            <th style='width:10%'><input type="checkbox" v-model='select_all.state'  @click="changeAllDevice" class="checkall">全选</th>
                            <th style='width:30%'>设备名称</th>
                            <th style='width:30%'>设备描述</th>
                            <th style='width:30%'>版本号</th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(task,index) in deviceInfo">
                        <td><input type="checkbox" v-model='task.state' @click="deviceCheck(index)"></td>
                        <td>{{ task.access_key }}</td>
                        <td>{{ task.device_desc }}</td>
                        <td>{{ task.edgent_version }}</td>
                    </tr>
                    </tbody>
                </table> -->
                <!-- 表格组件开始 -->
                <el-table
                    :data="deviceInfo"
                    ref="deviceInfo"
                    border
                    empty-text=' '
                    style="width: 100%;border-left: 1px solid #d6d7dd;border-top: 1px solid #d6d7dd;"
                    header-row-class-name = 'header-row'
                    @selection-change="handleSelectionChange"
                    >
                    <el-table-column
                      align="center"
                      type="selection"
                      label="全选"
                      width="55">
                    </el-table-column>
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
                        prop="access_key"
                        label="设备名称"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="device_desc"
                        label="设备描述"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="edgent_version"
                        label="版本号"
                    >
                    </el-table-column>
                </el-table>
                <div class="no_data" style="display: block;margin-top: -60px;" v-if="upgrade.show_no_data"><i :class="{'active':upgrade.search_status,'loading':upgrade.show_loading}"></i></div>
                <!-- 表格组件结束 -->

            </div>
        </div>
        <div class='btn_div'>
            <router-link to="/edge/processModule" class='btn_back'>返回</router-link>
            <a href='javascript:;' class='btn_save' :class="{'disabled':edit_disabled}" @click="saveClick">下发</a>
        </div>
        <!-- 数据详情 -->
        <el-dialog title="下发结果" :visible.sync="processResShow" width="1200px">
            <div class="handle_module_list">
                <!-- 表格组件开始 -->
                <el-table
                    :data="errorInfo"
                    ref="errorInfo"
                    border
                    empty-text=' '
                    style="width: 100%;border-left: 1px solid #d6d7dd;border-top: 1px solid #d6d7dd;"
                    header-row-class-name = 'header-row'
                    >
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
                        prop="access_key"
                        label="设备名称"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="result"
                        label="下发状态"
                    >
                    <template slot-scope="scope">
                        <span :class="{'errorStyle': !scope.row.result}">{{scope.row.result == true?'成功':'失败'}}</span>
                    </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="error"
                        label="错误信息提示"
                    >
                    <template slot-scope="scope">
                        <span :class="{'errorStyle': !scope.row.result}">{{scope.row.error}}</span>
                    </template>
                    </el-table-column>
                </el-table>
                <!-- 表格组件结束 -->
            </div>
            <div slot="footer" class="dialog-footer">
                <span @click="processResShow = false" class="btn_back">关闭</span>
            </div>
        </el-dialog>
    </div>
</template>
<script>
    export default {
        created:function(){
            this.$nextTick(() => {
                this.listFn();
            });   
        },
        name: 'upgradeModule',
        data () {
            return{
                deviceInfo:[],
                searchTerms:{
                    sort_rule:'descending',
                    pagesize: 10,
                    current: 1,
                },
                // 暂无数据开始
                upgrade:{
                    show_no_data:true,
                    search_status:false,
                    show_loading:true
                },
                // 暂无数据结束
                multipleSelection:[],
                edit_disabled:true,
                processResShow:false,
                errorInfo:[]
                
            }
        },
        watch: {
            multipleSelection:{
                handler:function(newVal){
                    if(this.multipleSelection.length == 0){
                        this.edit_disabled = true;
                    }else{
                        this.edit_disabled = false;
                    }
                },
                deep:true
            }
        },
        methods: {
            listFn:function(){
                this.upgrade = {
                    show_no_data:true,
                    search_status:false,
                    show_loading:true
                }
                this.$axios.get(this.$API.processManage.upgradeList).then( (res) =>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    this.deviceInfo = res.data.rows;
                    if(res.data.rows.length == 0){
                        this.upgrade = {
                            show_no_data:true,
                            search_status:false,
                            show_loading:false
                        }
                    }else{
                        this.upgrade = {
                            show_no_data:false,
                            search_status:false,
                            show_loading:true
                        }
                        this.deviceInfo = res.data.rows;
                    }
                });
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            //下发
            saveClick:function(){
                let deviceArr = [];
                if(this.edit_disabled == true){
                    //this.$message.info('请选择设备。');
                    return;
                }
                this.multipleSelection.forEach((item)=>{
                    deviceArr.push(item.device_id);
                });
                this.$axios.post(this.$API.processManage.upgradeSave,{
                    'appid': this.$route.query.appid,
                    'deviceidArray':deviceArr
                }).then( (res) =>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    if(res.data.actionResult == undefined || res.data.actionResult.length == 0){
                        this.$message.error('下发失败!');
                    }else{
                        this.errorInfo = res.data.actionResult;
                        this.processResShow = true;
                    }
                    this.$refs.deviceInfo.clearSelection();
                });
            }
        }
    }
</script>