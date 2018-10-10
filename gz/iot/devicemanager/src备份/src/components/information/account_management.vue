<template>
    <div>
        <div class="page_title_content">
          <span class="page_title">信息管理&nbsp;&gt;&nbsp;<router-link to="/information/accountManagement" class="btn_color">账号管理</router-link></span>
        </div>
        <div class="right_content">
            <div class="header">
                <span class="btn_create fr" @click="addAccountFn()">新增账号</span>
            </div>
            <div class='pipeline_list'>
                <!-- <table class="list">
                    <thead>
                    <tr>
                        <th style='width:5%'>编号</th>
                        <th style='width:20%'>账号信息</th>
                        <th style='width:15%'>姓名</th>
                        <th style='width:10%'>状态</th>
                        <th style='width:15%'>联系方式</th>
                        <th style='width:20%'>创建时间</th>
                        <th style='width:15%'>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(task,index) in taskInfo">
                        <td>{{ index + 1 }}</td>
                        <td>{{ task.loginName }}</td>
                        <td>{{ task.name }}</td>
                        <td>{{task.enable == 1 ? '正常':'冻结'}}</td>
                        <td>{{ task.phone }}</td>
                        <td>{{ task.createTime | formatDate}}</td>
                        <td :class="'text-center'"><span class="btn_color" @click="controlClick(index)">{{task.enable==1?'冻结':'解冻'}}</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="setPermissionFn(index)">权限</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="deleteClick(index)">删除</span></td>
                    </tr>
                    </tbody>
                </table> -->
                <!-- 表格组件开始 -->
                <el-table
                    :data="taskInfo"
                    border
                    :empty-text='emptyText'
                    style="width: 100%;border-left: 1px solid #d6d7dd;border-top: 1px solid #d6d7dd;"
                    :default-sort = "{prop: 'create_time', order: 'descending'}"
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
                        prop="loginName"
                        label="账号信息"
                        sortable="custom"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="name"
                        label="姓名"
                        sortable="custom"
                      >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="enable"
                        label="状态"
                    >
                    <template slot-scope="scope">
                        {{scope.row.enable==1?'正常':'冻结'}}
                    </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="phone"
                        label="联系方式"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="create_time"
                        sortable="custom"
                        label="创建时间"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        label="操作"
                    >
                    <template slot-scope="scope">
                        <span class="btn_color" @click="controlClick(scope.row)">{{scope.row.enable==1?'冻结':'解冻'}}</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="setPermissionFn(scope.row)">权限</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="deleteClick(scope.$index,scope.row)">删除</span>
                    </template>
                    </el-table-column>
                </el-table>
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentpage" :page-sizes="[10,20,50,80,100]" :page-size="rowcount" :total="total_num" layout="total,sizes, prev, pager, next, jumper" style='margin-top:10px;'></el-pagination>
                <div class="no_data" style="display: block;" v-if="show_no_data"><i :class="{'active':search_status,'loading':show_loading}"></i></div>
                <!-- 表格组件结束 -->
            </div>
        </div>
        <!-- Form -->
        <el-dialog title="新增账号" :visible.sync="dialogFormVisible" width="550px">
          <ul class="label_ul_dialog">
            <li>
              <label>账号信息：</label><span><input type="text" placeholder="请输入邮箱" class="inputText account_input" v-model="form.login_name"><b>*</b></span>
              <p class='tip_text'>{{account_tip.login_text}}</p>
            </li>
            <li>
              <label>密码：</label><span><input type="text" autocomplete="off" name="password"  placeholder="请输入密码" class="inputText pwd_input" v-model="form.password" minlength="8" maxlength="20"><b>*</b></span>
              <p class='tip_text normal'>密码长度8-20位，数字、字母、字符至少包含两种</p>
              <p class='tip_text'>{{account_tip.pwd_text}}</p>
            </li>
            <li>
              <label>姓名：</label><span><input type="text" placeholder="请输入姓名" class="inputText user_input" v-model="form.user_name" maxlength="16"></span>
              <p class='tip_text'>{{account_tip.user_text}}</p>
            </li>
          </ul>
          <div slot="footer" class="dialog-footer">
            <span @click="dialogFormVisible = false" class="btn_back">取消</span>
            <span type="primary" @click="accountSaveFn" class="btn_save">保存</span>
          </div>
        </el-dialog>
        <!-- 权限 -->
        <el-dialog title="权限列表" :visible.sync="dialogPermission" width="550px">
            <form class="permission_form" method="post" accept-charset="UTF-8">
            <table style="width: 100%;text-align: center;" cellpadding="0" cellspacing="0">
                <thead>
                    <tr>
                       <td style="width: 50%;height: 50px;border-bottom:1px solid #d6d7dd;">名称</td> 
                       <td style="width: 50%;height: 50px;border-bottom:1px solid #d6d7dd;">状态</td>
                    </tr>
                </thead>
                <tbody>
                        <tr style="height: 38px;">
                            <td>网关设备管理</td>
                            <td><input type="checkbox" v-model="permission" value= 'edge_manage'></td>
                        </tr>
                        <tr style="height: 38px;">
                            <td>数据流管理</td>
                            <td><input type="checkbox" v-model="permission" value='datastream_manage'></td>
                        </tr>
                        <tr style="height: 38px;">
                            <td>数字镜像</td>
                            <td><input type="checkbox" v-model="permission" value='devmirror_manage'></td>
                        </tr> 
                        <tr style="height: 38px;">
                            <td>处理模块</td>
                            <td><input type="checkbox" v-model="permission" value='procmodule_manage'></td>
                        </tr>
                </tbody>
            </table>
            </form>
            <p class='permission_tip_text'>{{permission_tip}}</p>
          <div slot="footer" class="dialog-footer">
            <span @click="dialogPermission = false" class="btn_back">取消</span>
            <span type="primary" @click="permissionSaveFn" class="btn_save">保存</span>
          </div>
        </el-dialog>
    </div>
</template>
<script>
    import {formatDate} from '../../assets/common/date.js';
    export default {
        mounted:function () {
            //this.listFn();
        },
        name: 'taskList',
        data () {
            return{
                taskInfo:[],
                emptyText:' ',
                dialogInfo:{},
                dialogShow:false,
                searchText:'',
                btn_disabled:false,
                rowcount: 10,
                currentpage: 1,
                total_num:0,
                dialogFormVisible: false,
                //新增账号
                form: {
                    login_name: '',
                    password:'',
                    user_name:''
                },
                account_tip:{
                  login_text:'',
                  pwd_text:'',
                  user_text:''
                },
                formLabelWidth: '100px',
                tip_show:true,
                //权限
                setPermissionLoginName:'',
                dialogPermission:false,
                permission_tip:'',
                permission:[],
                // 暂无数据开始
                sort_orderby:'',
                sort_rule:'descending',
                show_no_data:true,
                search_status:false,
                show_loading:true
                // 暂无数据结束
            }
        },
        filters: {
            formatDate(time) {
                let date = new Date(time);
                return formatDate(date, 'yyyy-MM-dd hh:mm:ss');
            }
        },
        methods: {
            listFn:function(){
                this.taskInfo = [];
                this.search_status = false;
                this.show_loading = true;
                this.show_no_data = true;
                this.$axios.post(this.$API.infoManage.accountList,{
                    'pagesize':this.rowcount,
                    'current':this.currentpage,
                    'sort_orderby':this.sort_orderby,
                    'sort_rule':this.sort_rule
                }).then( (res)=> {
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }else if(res.data.code == 200){
                        this.total_num = res.data.data.total;
                        this.taskInfo = res.data.data.list;
                        if(this.taskInfo.length == 0){
                            if(status==1){
                              this.search_status = true;
                            }else{
                              this.search_status = false;
                            }
                            this.show_loading = false;
                            this.show_no_data = true;
                            this.emptyText = '暂无数据';
                        }else{
                            this.show_no_data = false;
                        }
                    }
                });
            },
            handleClick(colData){
                console.log(index);

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
            addAccountFn:function(){
                this.form ={
                    login_name: '',
                    password:'',
                    user_name:''
                };
                this.account_tip ={
                  login_text:'',
                  pwd_text:'',
                  user_text:''
                };
                this.dialogFormVisible = true;
            },
            setPermissionFn:function(colData){
                this.setPermissionLoginName = colData.loginName;
                this.permission=[];
                this.permission_tip='';
                this.$axios.get(this.$API.infoManage.searchPermission+'?loginName='+colData.loginName).then( (res)=> {
                    let permissionArray = res.data.data;
                    if(res.data.code === 200){
                        for(let i=0; i<permissionArray.length;i++){
                            if(permissionArray[i] == 'edge:manage'){
                                this.permission.push('edge_manage');
                            }else if(permissionArray[i] == 'datastream:manage'){
                                this.permission.push('datastream_manage');
                            }else if(permissionArray[i] == 'devmirror:manage'){
                                this.permission.push('devmirror_manage');
                            }else if(permissionArray[i] == 'procmodule:manage'){
                                this.permission.push('procmodule_manage');
                            }
                        }
                    }else if(res.data.code === 420){
                        this.$router.push({path: '/login'});
                    }
                });
                this.dialogPermission = true;
            },
            deleteClick:function (index,colData) {
                let id = parseInt(colData.topid,10);
                this.$confirm('请慎重操作，删除后该账号将不能正常登录平台！', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                    this.$axios({
                      url: this.$API.infoManage.accountDelete,
                      method: 'post',
                      data: {'loginName': colData.loginName},
                      transformRequest: [function (data) {
                        // Do whatever you want to transform the data
                        let ret = ''
                        for (let it in data) {
                          ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                        }
                        return ret
                      }],
                      headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                      }
                    }).then((res)=>{
                        if(res.data.code === 200){
                            this.taskInfo.splice(index,1);
                            this.total_num = this.total_num-1;
                            if(this.taskInfo.length==0){
                                this.search_status = false;
                                this.show_loading = false;
                                this.show_no_data = true;
                            }
                        }else if(res.data.code === 500){
                            this.$message.error('删除失败！');
                        }else if(res.data.code === 420){
                            this.$router.push({path: '/login'});
                        }
                    });
                }).catch(() => {
                   return      
                });
            },
            accountSaveFn:function(){
                let regMail=/^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,5}$/;
                let regCheckPwd = /^(?![0-9]+$)(?![a-zA-Z]+$)(?!([^(0-9a-zA-Z)]|[\(\)])+$)([^(0-9a-zA-Z)]|[\(\)]|[a-zA-Z]|[0-9]){8,20}$/;
                let reg_name = /^[\w\s\u4e00-\u9fa5]{1,16}$/;
                if(this.form.login_name == ''){
                  this.account_tip = {
                    login_text:'请输入邮箱',
                    pwd_text:'',
                    user_text:''
                  }
                  this.tip_show = true;
                  $('.account_input').focus();
                  return;
                }
                if(!regMail.test(this.form.login_name)){
                  this.account_tip = {
                    login_text:'请输入正确的邮箱',
                    pwd_text:'',
                    user_text:''
                  }
                  $('.account_input').focus();
                  return;
                }
                if(this.form.password == ''){
                  this.account_tip = {
                    login_text:'',
                    pwd_text:'请输入密码',
                    user_text:''
                  }
                  $('.pwd_input').focus();
                  return;
                }
                if(!regCheckPwd.test(this.form.password)){
                  this.account_tip = {
                    login_text:'',
                    pwd_text:'密码格式不正确',
                    user_text:''
                  }
                  $('.pwd_input').focus();
                  return;
                }
                let md5_password = this.$crypto.createHash("md5").update(this.form.password).digest('hex');
                //提交
                this.$axios({
                  url: this.$API.infoManage.accountAdd,
                  method: 'post',
                  data: {'loginName':this.form.login_name,'password':md5_password,'name':this.form.user_name},
                  transformRequest: [function (data) {
                    // Do whatever you want to transform the data
                    let ret = ''
                    for (let it in data) {
                      ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                    }
                    return ret
                  }],
                  headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                  }
                }).then((res)=>{
                    if(res.data.code === 200){
                        this.dialogFormVisible = false;
                        this.$message.success('保存成功！');
                        this.listFn();
                    }else if(res.data.code === 409){
                        this.account_tip = {
                            login_text:'账户已存在',
                            user_text:''
                        }
                        this.tip_show = true;
                    }else if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                });
            },
            //启动暂停
            controlClick:function(colData){
                this.$axios({
                  url: this.$API.infoManage.accountEnabled,
                  method: 'post',
                  data: {'loginName': colData.loginName,'enable': colData.enable == 1 ? 0:1},
                  transformRequest: [function (data) {
                    // Do whatever you want to transform the data
                    let ret = ''
                    for (let it in data) {
                      ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                    }
                    return ret
                  }],
                  headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                  }
                }).then((res)=>{
                    if(res.data.code === 200){
                        colData.enable = colData.enable == 1 ? 0:1;
                    }else if(res.data.code === 500){
                        this.$message.error('操作失败！');
                    }else if(res.data.code === 420){
                        this.$router.push({path: '/login'});
                    }
                });
            },
            //权限
            permissionSaveFn:function(index){
                if(this.permission.length == 0){
                    this.permission_tip = '至少选择一个权限。';
                    return;
                }
                this.$axios({
                  url: this.$API.infoManage.setPermission,
                  method: 'post',
                  data: {'loginName':this.setPermissionLoginName,'permission':this.permission},
                  transformRequest: [function (data) {
                    // Do whatever you want to transform the data
                    let ret = ''
                    for (let it in data) {
                      ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                    }
                    return ret
                  }],
                  headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                  }
                }).then((res)=>{
                    if(res.data.code === 200){
                        this.$message.success('权限设置成功！');
                        this.dialogPermission = false;
                    }else if(res.data.code === 500){
                        this.$message.error('权限设置失败！');
                    }else if(res.data.code === 420){
                        this.$router.push({path: '/login'});
                    }
            
                });
            }
        }
    }
</script>
<style scoped>
    .pipeline_list{
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
    /*针对elementui*/
</style>