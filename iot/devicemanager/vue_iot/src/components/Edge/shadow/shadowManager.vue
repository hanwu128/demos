<template>
    <div>
        <div class="page_title_content">
          <span class="page_title"><router-link to="/edge/shadowManager" class="btn_color">数字镜像</router-link></span>
        </div>
        <div class="right_content">
            <div class="header">
                <input class="search_text" type="text" placeholder="请输入数字镜像名称或网关设备名称" v-model="search_access_key">
                <span class='btn_save' @click="searchClick()">搜索</span>
                <span class="btn_create fr" @click="createShadowClick()">创建数字镜像</span>
            </div>
            <div class="shadowmanager_list">
                <!-- <table class="list">
                    <thead>
                    <tr>
                        <th style='width:5%'>编号</th>
                        <th style='width:16%'>设备镜像名称</th>
                        <th style='width:16%'>网关设备名称</th>
                        <th style='width:16%'>处理模块名称</th>
                        <th style='width:16%'>设备镜像描述</th>
                        <th style='width:16%'>创建时间</th>
                        <th style='width:15%'>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(shadow,index) in shadowInfo">
                        <td>{{ index + 1 }}</td>
                        <td>{{ shadow.shadow_name }}</td>
                        <td>{{ shadow.access_key }}</td>
                        <td>{{ shadow.app_name }}</td>
                        <td>{{ shadow.shadow_desc }}</td>
                        <td>{{shadow.create_stamp}}</td>
                        <td :class="'text-center'"><span class="btn_color" @click="handleClick(index)">配置</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="detailsClick(index)">详情</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="deleteClick(index)">删除</span></td>
                    </tr>
                    </tbody>
                </table> -->
                <!-- 表格组件开始 -->
                <el-table
                    :data="shadowInfo"
                    border
                    :empty-text='emptyText'
                    style="width: 100%;border-left: 1px solid #d6d7dd;border-top: 1px solid #d6d7dd;"
                    :default-sort = "{prop: 'create_stamp', order: 'descending'}"
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
                        prop="shadow_name"
                        label="数字镜像名称"
                        sortable="custom"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="access_key"
                        label="网关设备名称"
                        sortable="custom"
                      >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="app_name"
                        label="处理模块名称"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="shadow_desc"
                        label="数字镜像描述"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="create_stamp"
                        sortable="custom"
                        label="创建时间"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        label="操作"
                    >
                    <template slot-scope="scope">
                        <span class="btn_color" @click="handleClick(scope.$index)">配置</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="detailsClick(scope.$index)">详情</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="deleteClick(scope.$index)">删除</span>
                    </template>
                    </el-table-column>
                </el-table>
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentpage" :page-sizes="[10,20,50,80,100]" :page-size="rowcount" :total="total_num" layout="total,sizes, prev, pager, next, jumper" style='margin-top:10px;'></el-pagination>
                <div class="no_data" style="display: block;" v-if="show_no_data"><i :class="{'active':search_status,'loading':show_loading}"></i></div>
                <!-- 表格组件结束 -->
            </div>
            </div>
            <!-- <div v-if="shadowIsShow"  class="dialogTask shadowManagerDialog">
                <div class="catlog"><h3>配置网关设备镜像</h3><span class="cancel_btn" @click="cancleAlert">×</span></div>
                    <div class="shadowManagerDialogTable">
                        <table class="list">
                            <thead>
                                <th>显示名称</th>
                                <th>类型</th>
                                <th>单位</th>
                                <th>当前值</th>
                                <th>更新时间</th>
                                <th>期望值</th>
                            </thead>
                            <tbody>
                            <tr v-for="(shadowGW,index) in shadowGateWay">
                                <td>{{ shadowGW.item_display_name }}</td>
                                <td>{{ shadowGW.item_datatype }}</td>
                                <td>{{ shadowGW.item_unit }}</td>
                                <td>{{ shadowGW.item_value }}</td>
                                <td>{{ shadowGW.expectvalue_stamp }}</td>
                                <td><input type="text" v-model="shadowGW.inputVal"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="btn_div_dialog">
                        <span class="btn_back" @click="cancleAlert">取消</span>
                        <span class="btn_save" :class="{'disabled': edit_show}"  @click="doneAlert()">确定</span>
                    </div>
                </div>
            </div> -->
            <!-- 配置 -->
            <el-dialog title="配置网关数字镜像" :visible.sync="shadowIsShow" width="800px">
                <div class="shadowManagerDialogTable">
                    <table class="list">
                        <thead>
                            <th>显示名称</th>
                            <th>类型</th>
                            <th>单位</th>
                            <th>当前值</th>
                            <th>更新时间</th>
                            <th>期望值</th>
                        </thead>
                        <tbody>
                        <tr v-for="(shadowGW,index) in shadowGateWay">
                            <td>{{ shadowGW.item_display_name }}</td>
                            <td>{{ shadowGW.item_datatype }}</td>
                            <td>{{ shadowGW.item_unit }}</td>
                            <td>{{ shadowGW.item_value }}</td>
                            <td>{{ shadowGW.expectvalue_stamp }}</td>
                            <td><input type="text" v-model="shadowGW.inputVal"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
              <div slot="footer" class="dialog-footer">
                <span @click="shadowIsShow = false" class="btn_back">取消</span>
                <span type="primary" class="btn_save" :class="{'disabled': edit_show}" @click="doneAlert">保存</span>
              </div>
            </el-dialog>
        </div>
    </div>
</template>
<script>
    export default {
        created:function () {
            //this.listFn();
        },
        name: 'shadowManager',
        data () {
            return {
                //列表
                shadowInfo: [],
                rowcount: 10,
                currentpage: 1,
                total_num:0,
                sort_orderby:'create_stamp',
                sort_rule:'descending',
                shadowIsShow:false,
                shadowGateWay:[],
                shadowdeviceid:'',
                shadowAppName:'',
                jsonEx:{},
                inputVal:[],
                search_access_key:'',
                edit_show:true,
                // 暂无数据开始
                emptyText:' ',
                show_no_data:true,
                search_status:false,
                show_loading:true,
                // 暂无数据结束
                search_flag:0
            }
        },
        watch: {
            shadowGateWay:{
                handler:function(newVal){
                    if(this.shadowGateWay[0].inputVal == '' || this.shadowGateWay[0].inputVal == undefined){
                        this.edit_show = true;
                    }else{
                        this.edit_show = false;
                    }
                },
                deep:true
            }
        },
        methods: {
            listFn(status){
                this.shadowInfo = [];
                this.search_status = false;
                this.show_loading = true;
                this.show_no_data = true;
                //判断是否为搜索
                if(status == 1){
                  this.currentpage = 1;
                }
                this.$axios.post(this.$API.shadowManage.shadowList,{
                    'edgent_version':'',
                    'access_key':this.search_access_key,
                    'pagesize':this.rowcount,
                    'current':this.currentpage,
                    'sort_orderby':this.sort_orderby,
                    'sort_rule':this.sort_rule
                }).then( (res)=> {
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    if(res.data.items.length == 0){
                        if(this.search_flag==1){
                          this.search_status = true;
                        }else{
                          this.search_status = false;
                        }
                        this.show_loading = false;
                        this.show_no_data = true;
                        this.emptyText = '暂无数据';
                    }else{
                        this.show_no_data = false;
                        this.total_num = res.data.total;
                        this.shadowInfo = res.data.items;
                    }
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
            handleClick:function (index) {
                let _self = this;
                this.shadowIsShow = !this.shadowIsShow
                if (this.shadowIsShow == true) {
                    this.$axios.get(this.$API.shadowManage.configList+"?shadow_name=" + this.shadowInfo[index].shadow_name,{}).then( (res)=> {
                        if(res.data.code == 420){
                            this.$router.push({path: '/login'});
                        }
                        let arr = res.data.items;
                        this.shadowGateWay = []
                        for(var i=0;i<arr.length;i++) {
                            this.shadowGateWay.push(arr[i])
                        }
                        this.shadowdeviceid = res.data.device_id;
                        this.shadowAppName = res.data.app_name; 
                    },function(res){
                        console.log(res)
                    });
                }
            },
            deleteClick:function (index) {
                this.$confirm('确定要删除数字镜像“'+ this.shadowInfo[index].shadow_name +'”吗？删除后将无法获取和控制设备“'+ this.shadowInfo[index].access_key +'”的状态。', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                    this.$axios.post(this.$API.shadowManage.shadowDelete,{
                    'shadow_name':this.shadowInfo[index].shadow_name
                    }).then( (res)=> {
                        if(res.data.code == 420){
                            this.$router.push({path: '/login'});
                        }
                        let obj = res.data.error;
                        if(obj == 'success') {
                            this.shadowInfo.splice(index,1);
                            this.total_num = this.total_num-1;
                            this.listFn();
                        }else {
                            this.$message.error('删除失败!');
                        }
                    },function(res){
                        console.log(res);
                    });
                }).catch(() => {
                   return      
                });
            },
            detailsClick:function (index) {
            	this.$router.push({path:'/edge/shadow/Detail',query: { shadow_name: this.shadowInfo[index].shadow_name}})
            	return false;
            },
            cancleAlert:function () {
                this.shadowIsShow = false
            },
            doneAlert:function () {
            	let desired_inputVal={};
                let _self= this;
                if(this.edit_show == true){
                    return;
                }
                for (var i=0; i < this.shadowGateWay.length;i++) {
                    if(!!!_self.shadowGateWay[i].inputVal){
                        this.$message.info('请输入期望值。');
                        return
                    }
                    desired_inputVal[ this.shadowGateWay[i].item_name ]=  this.shadowGateWay[i].inputVal
                }

                this.$axios.post(this.$API.shadowManage.configSave,{
                        device_id: this.shadowdeviceid,
                        app_name: this.shadowAppName,
                        desired: desired_inputVal
                }).then(function(res){
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    let msg = res.data.result;
                    if (msg == true) {
                        _self.shadowIsShow = false
                    }
                    else {
                        this.$message.error('配置失败！');
                    }
                },function(res){
                    console.log(res)
                });
            },
            createShadowClick:function () {
                this.$router.push({path:'/edge/shadow/info'})
            },
            searchClick:function(){
                this.listFn(1);
            }
        }
    }
</script>

<style scoped>
    .header,.shadowmanager_list{
        margin:0 20px 20px 20px;
    }
    .configShadow {
        background-color: white;
        position: relative;
        width: 60%;
        height: 460px;
        left: 20%;
        top:10px;
        box-shadow:0 0 5px #000 inset;
    }
    .shadowManagerDialog{
        width:800px;
        margin-left:-400px;
        height:330px;
    }
</style>
