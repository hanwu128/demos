<template>
    <div>
        <div class="page_title_content">
          <span class="page_title">边缘分析IMGdetail&nbsp;&gt;&nbsp;<router-link to="/edge/manager" class="btn_color">网关设备管理</router-link>&nbsp;&gt;&nbsp;网关设备详情</span>
        </div>
        <div class="right_content">
            <!--网关设备信息-->
            <div class="gateWay">
                <span class="module_title">网关设备信息</span>
                <span class="btn_create gateWayKey" @click="gateway_show()">&nbsp;网关秘钥&nbsp;</span>
                <span class="btn_create gateWayKey" @click="rebootFn()">&nbsp;重 启&nbsp;</span>
            </div>
            <div class="list_detail">
                <table class="gateWayInfoTable">
                    <tr>
                        <td class='table_th' style='width:20%'>网关设备设备名称</td>
                        <td style='width:30%'>{{this.deviceDetail.access_key}}</td>
                        <td class='table_th' style='width:20%'>网关设备ID</td>
                        <td style='width:30%'>{{this.deviceDetail.device_id}}</td>
                    </tr>
                    <tr>
                        <td class='table_th'>网关设备型号</td>
                        <td>{{this.deviceDetail.meta.hardware_model}}</td>
                        <td class='table_th'>位置</td>
                        <td>{{this.deviceDetail.meta.hardware_location}}</td>
                    </tr>
                    <tr>
                        <td class='table_th'>操作系统</td>
                        <td>{{this.deviceDetail.meta.hardware_os}}</td>
                        <td class='table_th'>操作系统版本</td>
                        <td>{{this.deviceDetail.meta.hardware_os_version}}</td>
                    </tr>
                    <tr>
                        <td class='table_th'>固件版本</td>
                        <td>{{this.deviceDetail.meta.hardwareosversion}}</td>
                        <td class='table_th'>厂商</td>
                        <td>{{this.deviceDetail.meta.hardwaremanufactor}}</td>
                    </tr>
                    <tr>
                        <td class='table_th'>Agent版本</td>
                        <td>{{this.deviceDetail.meta.edgent_agent_version}}</td>
                        <td class='table_th'>IP地址</td>
                        <td>{{this.deviceDetail.meta.host_ip}}</td>
                    </tr>
                </table>
            </div>
            <!--网关设备描述-->
            <div class="gateWay">
                <span class="module_title">网关设备描述</span>
                <p class="gateWayDes">
                    {{this.deviceDetail.device_desc==''|| this.deviceDetail.device_desc==undefined?'暂无数据':this.deviceDetail.device_desc}}
                </p>
            </div>
            <!--Agent信息-->
            <!-- <div class="gateWay">
                <span class="module_title">Agent信息</span>
            </div>
            <table class="gateWayInfoTable">
                <tr>
                    <td class='table_th' style='width:20%'>Agent名称</td>
                    <td style='width:30%'>{{this.deviceDetail.meta.edgent_agent_name}}</td>
                    <td class='table_th' style='width:20%'>Agent版本</td>
                    <td style='width:30%'>{{this.deviceDetail.meta.edgent_agent_version}}</td>
                </tr>
            </table> -->
            <!--Agent描述-->
            <!--
            <div class="gateWay">
                <span class="module_title">Agent描述</span>
                <p class="gateWayDes">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean euismod bibendum laoreet. Proin gravida dolor sit amet lacus accumsan et viverra justo commodo. Proin sodales pulvinar tempor. Cum sociis
                </p>
            </div>
            -->
            <!--处理模块信息-->
            <div class="gateWay">
                <span class="module_title">处理模块信息</span>
                <span class="btn_create gateWayKey" @click="processAddFn">增加模块</span>
            </div>
            <div class="list_detail">
                <table class="gateWayInfoTable">
                    <thead>
                        <tr>
                            <th style='width:20%'>处理模块名称</th>
                            <th style='width:15%'>处理模块类型</th>
                            <th style='width:15%'>状态</th>
                            <th style='width:20%'>版本新信息</th>
                            <th style='width:20%'>数据流</th>
                            <th style='width:10%'>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(item,index) in agentApplist">
                            <td>{{ item.app_name }}</td>
                            <td>{{ item.app_type=='shadow'?'镜像':item.app_type=='stream'? '流式':'固件'}}</td>
                            <td>{{ item.status_text }}</td>
                            <td>{{ item.app_version }}</td>
                            <td>{{ item.streaming_name==''?(item.app_type=='shadow'?'N/A':'<无>'):item.streaming_name}}</td>
                            <td><span class="btn_color" :class="{'disabled':item.classStatus}" @click="startClick(index)">{{ item.status_button }}</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="detailClick(index)">详情</span></td>
                        </tr>
                    </tbody>
                </table>
                <div class="no_data no_page" style="display: block;" v-if="process.show_no_data"><i :class="{'loading':process.show_loading}"></i></div>
            </div>
            <!--数据量-->
            <div class="gateWay">
                <span class="module_title">数据量</span>
            </div>
            <div class="list_detail">
                <table class="gateWayInfoTable">
                    <thead>
                        <tr>
                            <th style='width:25%'>处理模块名称</th>
                            <th style='width:25%'>处理模块类型</th>
                            <th style='width:25%'>接收量</th>
                            <th style='width:25%'>发送量/未处理</th>
                        </tr>
                    </thead>
                    <tr v-for="(item,index) in dataList">
                        <td>{{ item.app_name }}</td>
                        <td>{{item.app_type=='shadow'?'镜像':item.app_type=='stream'? '流式':'固件'}}</td>
                        <td>{{ item.received }}</td>
                        <td>{{ item.transmitted}}/{{item.transmitted_invalid}}</td>
                    </tr>
                </table>
                <div class="no_data no_page" style="display: block;" v-if="num.show_no_data"><i :class="{'loading':num.show_loading}"></i></div>
            </div>
            <!--软件更新历史-->
            <div class="gateWay">
                <span class="module_title">软件更新历史</span>
            </div>
            <!-- <table class="gateWayInfoTable">
                <thead>
                    <tr>
                        <th style='width:20%'>处理模块名称</th>
                        <th style='width:20%'>处理模块类型</th>
                        <th style='width:20%'>更新时间</th>
                        <th style='width:20%'>版本</th>
                        <th style='width:20%'>下发状态</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(appHistory,index) in appHistoryList">
                        <td>{{ appHistory.appname }}</td>
                        <td>{{ appHistory.apptype=='shadow'?'镜像':'流式'}}</td>
                        <td>{{ appHistory.createtimeatshow }}</td>
                        <td>{{ appHistory.appversion }}</td>
                        <td>{{ item.taskstatus=='true' ? '成功':(item.taskstatus=='successSendMqtt'?'下发中':'失败')}}</td>
                    </tr>
                </tbody>
            </table> -->

            <!-- 表格组件开始 -->
            <div class="list_detail">
                <el-table
                    :data="appHistoryList"
                    border
                    empty-text=' '
                    style="width: 100%;border-left: 1px solid #d6d7dd;border-top: 1px solid #d6d7dd;"
                    :default-sort = "{prop: 'createtimeatshow', order: 'descending'}"
                    header-row-class-name = 'header-row'
                    @sort-change="softSortChangeFn"
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
                        prop="appname"
                        label="处理模块名称"
                        sortable="custom"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="apptype"
                        label="处理模块类型"
                        sortable="custom"
                    >
                    <template slot-scope="scope">
                        {{scope.row.apptype=='shadow'?'镜像':scope.row.apptype=='stream'? '流式':'固件'}}
                    </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="createtimeatshow"
                        sortable="custom"
                        label="更新时间"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="appversion"
                        label="版本"
                        sortable="custom"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="taskstatus"
                        label="下发状态"
                    >
                    <template slot-scope="scope">
                        {{ scope.row.taskstatus=='true' ? '成功':(scope.row.taskstatus=='successSendMqtt'?'下发中':'失败')}}
                    </template>
                    </el-table-column>
                </el-table>
                <el-pagination @size-change="softHandleSizeChange" @current-change="softHandleCurrentChange" :current-page="soft.currentpage" :page-sizes="[10,20,50,80,100]" :page-size="soft.rowcount" :total="soft.total_num" layout="total,sizes, prev, pager, next, jumper" style='margin-top:10px;'></el-pagination>
                <div class="no_data" style="display: block;" v-if="soft.show_no_data"><i :class="{'active':soft.search_status,'loading':soft.show_loading}"></i></div>
            </div>
            <!-- 表格组件结束 -->
            <!--状态历史-->
            <div class="gateWay">
                <span class="module_title">状态历史</span>
            </div>
            <!-- <table class="gateWayInfoTable">
                <thead>
                    <tr>
                        <th style='width:25%'>网关设备名称</th>
                        <th style='width:25%'>IP地址</th>
                        <th style='width:25%'>时间</th>
                        <th style='width:25%'>状态</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(edgeHistory,index) in edgeHistoryList">
                        <td>{{ edgeHistory.username }}</td>
                        <td>{{ edgeHistory.ip }}</td>
                        <td>{{ edgeHistory.create_stamp }}</td>
                        <td>{{ edgeHistory.is_online ? '在线':'离线' }}</td>
                    </tr>
                </tbody>
            </table> -->

            <!-- 表格组件开始 -->
            <div class="list_detail">
                <el-table
                    :data="edgeHistoryList"
                    border
                    empty-text=' '
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
                        prop="username"
                        label="网关设备名称"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="ip"
                        label="IP地址"
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
                        prop="is_online"
                        label="状态"
                    >
                    <template slot-scope="scope">
                        {{scope.row.is_online?'在线':'离线'}}
                    </template>
                    </el-table-column>
                </el-table>
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentpage" :page-sizes="[10,20,50,80,100]" :page-size="rowcount" :total="total_num" layout="total,sizes, prev, pager, next, jumper" style='margin-top:10px;'></el-pagination>
                <div class="no_data" style="display: block;" v-if="show_no_data"><i :class="{'active':search_status,'loading':show_loading}"></i></div>
            </div>
            <!-- 表格组件结束 -->
        </div>
        <div class='btn_div'>
            <router-link to="/edge/manager" class='btn_back'>返回</router-link>
        </div>
        <div v-if= "gateWayisShow" class="dialogTask">
            <div class="catlog"><h3>网关秘钥</h3><span class="cancel_btn" @click="gateway_close">×</span></div>
            <div class="keyContent">{{this.deviceDetail.secret_key}}</div>
            <div class="btn_div_dialog">
                <span class="btn_save" @click="gateway_update">更新</span>
                <span class="btn_back" @click="gateway_close">关闭</span>
            </div>
        </div>
        <!-- <div v-if= "dialogShow" class="dialogTask handle_dialogTask">
            <div class="catlog"><h3>详情</h3><span class="cancel_btn" @click="cancleClick">×</span></div>
            <ul>
                <li>
                    <label>处理模块名称：</label><span>{{agentApplist[dialogIndex].app_name}}</span>
                </li>
                <li>
                    <label>处理模块类型：</label><span>{{agentApplist[dialogIndex].app_type=='shadow'?'镜像':agentApplist[dialogIndex].app_type=='stream'? '流式':'固件'}}</span>
                </li>
                <li>
                    <label>状态：</label><span>{{agentApplist[dialogIndex].status_text}}</span>
                    <label class="label_l">版本信息：</label><span>{{agentApplist[dialogIndex].app_version}}</span>
                </li>
                <li>
                    <label>处理模块描述：</label><span>{{agentApplist[dialogIndex].desc}}</span>
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
                            <tr v-for="(item,index) in agentApplist[dialogIndex].attributes">
                                <td>{{ item.name }}</td>
                                <td>{{ item.desc }}</td>
                                <td>{{ item.sourcedefault }}</td>
                                <td>{{ item.type }}</td>
                                <td>{{ item.unit }}</td>
                            </tr>
                            </tbody>
                        </table>
                    </span>
                </li>
            </ul>
            <div class='btn_div_dialog'>
                <a href='javascript:;' class='btn_back' @click="cancleClick">关闭</a>
            </div>
        </div>        -->
        <!-- 配置 -->
        <el-dialog title="详情" :visible.sync="dialogShow" width="800px" v-if="dialogShow">
            <ul class="detail_basicmsg">
                <li>
                    <label>处理模块名称：</label><span>{{agentApplist[dialogIndex].app_name}}</span>
                </li>
                <li>
                    <label>处理模块类型：</label><span>{{agentApplist[dialogIndex].app_type=='shadow'?'镜像':agentApplist[dialogIndex].app_type=='stream'? '流式':'固件'}}</span>
                </li>
                <li>
                    <label>状态：</label><span>{{agentApplist[dialogIndex].status_text}}</span>
                    <label class="label_l" style="margin-left:160px;">版本信息：</label><span>{{agentApplist[dialogIndex].app_version}}</span>
                </li>
                <li>
                    <label>处理模块描述：</label><span>{{agentApplist[dialogIndex].desc}}</span>
                </li>
                <li>
                    <label style="margin-bottom:10px;">属性：</label>
                    <span class = 'dialog_table'>
                        <table class="list">
                            <thead>
                            <tr>
                                <th>属性名称</th>
                                <th>显示名称</th>
                                <th>默认值</th>
                                <th>类型</th>
                                <th>单位</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="(item,index) in agentApplist[dialogIndex].attributes">
                                <td>{{ item.name }}</td>
                                <td>{{ item.desc }}</td>
                                <td>{{ item.sourcedefault }}</td>
                                <td>{{ item.type }}</td>
                                <td>{{ item.unit }}</td>
                            </tr>
                            </tbody>
                        </table>
                    </span>
                </li>
            </ul>
            <div slot="footer" class="dialog-footer">
                <span @click="dialogShow = false" class="btn_back">关闭</span>
            </div>
        </el-dialog>
        <!-- 数据详情 -->
        <el-dialog title="处理模块列表" :visible.sync="processShow" width="1200px">
            <div class="header">
                <input class="search_text" type="text" placeholder="请输入处理模块名称" v-model='processAddSearchTerms.appname'>
                <select class="search_select" v-model='processAddSearchTerms.apptype'>
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
                <span class='btn_save' @click="processAddSearchClick()">搜索</span>
            </div>
            <div class="handle_module_list">
                <!-- 表格组件开始 -->
                <el-table
                    :data="processArray"
                    ref="processArray"
                    border
                    empty-text=' '
                    style="width: 100%;border-left: 1px solid #d6d7dd;border-top: 1px solid #d6d7dd;"
                    header-row-class-name = 'header-row'
                    header-cell-class-name='select-single'
                    :default-sort = "{prop: 'createtimeatshow', order: 'descending'}"
                    @sort-change="processAddSortChangeFn"
                    @selection-change="processAddHandleSelectionChange"
                    @select='processAddSelectFn'
                    @row-click="processAddRowClickFn"
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
                        {{scope.$index+1+(processAddSearchTerms.current-1)*processAddSearchTerms.pagesize}}
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
                        width="150"
                        sortable="custom"
                    >
                    <template slot-scope="scope">
                        {{scope.row.apptype_text}}
                    </template>
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="appversion"
                        width="150"
                        label="版本号"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        prop="createtimeatshow"
                        sortable="custom"
                        width="200"
                        label="创建时间"
                    >
                    </el-table-column>
                    <el-table-column
                        align="center"
                        width="150"
                        label="操作"
                    >
                    <template slot-scope="scope">
                        <span class="btn_color" @click="processDetailClick(scope.$index)">详情</span>
                    </template>
                    </el-table-column>
                </el-table>
                <el-pagination @size-change="processAddHandleSizeChange" @current-change="processAddHandleCurrentChange" :current-page="processAddSearchTerms.current" :page-sizes="[10,20,50,80,100]" :page-size="processAddSearchTerms.pagesize" :total="processAdd.total_num" layout="total,sizes, prev, pager, next, jumper" style='margin-top:10px;'></el-pagination>
                <div class="no_data" style="display: block;" v-if="processAdd.show_no_data"><i :class="{'active':processAdd.search_status,'loading':processAdd.show_loading}"></i></div>
                <!-- 表格组件结束 -->
            </div>
            <div slot="footer" class="dialog-footer">
                <span @click="processShow = false" class="btn_back">取消</span>
                <span @click="processAddSave" class="btn_save"  :class="{'disabled': processAddSaveDisabled}">确认下发</span>
            </div>
        </el-dialog>
        <!-- 添加处理模块详情 -->
        <el-dialog title="详情" :visible.sync="processAddDetailShow" width="800px" v-if="processAddDetailShow">
            <ul class="detail_basicmsg">
                <li>
                    <label>处理模块名称：</label><span>{{processAddDetailObj.app.appname}}</span>
                </li>
                <li>
                    <label>处理模块类型：</label><span>{{processAddDetailObj.app.apptype_text}}</span>
                </li>
                <li>
                    <label>状态：</label><span>{{processAddDetailObj.status}}</span>
                    <label class="label_l" style="margin-left:160px;">版本信息：</label><span>{{processAddDetailObj.app.appversion}}</span>
                </li>
                <li>
                    <label>处理模块描述：</label><span>{{processAddDetailObj.app.appdesc}}</span>
                </li>
                <li>
                    <label style="margin-bottom:10px;">属性：</label>
                    <span class = 'dialog_table'>
                        <table class="list">
                            <thead>
                            <tr>
                                <th>属性名称</th>
                                <th>显示名称</th>
                                <th>默认值</th>
                                <th>类型</th>
                                <th>单位</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="(item,index) in processAddDetailObj.appsourcearray">
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
            <div slot="footer" class="dialog-footer">
                <span @click="processAddDetailShow = false" class="btn_back">关闭</span>
            </div>
        </el-dialog>
    </div>
</template>
<script>
    export default {
        created:function () {
            this.processListFn();
            //数据量
            this.dataList = [];
            this.num={
                show_no_data:true,
                show_loading:true
            }
            this.$axios.get(this.$API.edgeManage.detailData+"?device_id="+this.$route.query.deviceid).then((res)=>{
                if(res.data.code == 420){
                    this.$router.push({path: '/login'});
                }
                if(res.data.length == 0){
                    this.num={
                        show_no_data:true,
                        show_loading:false
                    }
                }else{
                    this.num={
                        show_no_data:false,
                        show_loading:true
                    }
                }
                this.dataList = res.data;
            });
        },
        name: 'detail',
        data () {
            return {
                gateWayisShow:false,
                dialogShow:false,
                edgeHistoryList:[],
                appHistoryList:[],
                agentApplist:[],
                dataList:[],
                dialogIndex:0,
                deviceDetail:{
                    meta:{
                        'app_key':'',
                        'console_url':'',
                        'customer_id':'',
                        'edgent_agent_app_list':'',
                        'edgent_agent_version':'',
                        'firmware_version':'',
                        'hardware_device_id':'',
                        'hardware_location':'',
                        'hardware_manufactor':'',
                        'hardware_model':'',
                        'hardware_os':'',
                        'hardware_os_version':'',
                        'hardwaredeviceid':'',
                        'hardwaremanufactor':'',
                        'hardwaremodel':'',
                        'hardwareos':'',
                        'hardwareosversion':'',
                        'id':'',
                        'json_version':'',
                        'timestampat':'',
                    }
                },
                keyStatus:false,
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
                // 软件更新历史暂无数据开始
                soft:{
                    sort_orderby:'',
                    sort_rule:'descending',
                    rowcount: 10,
                    currentpage: 1,
                    total_num:0,
                    show_no_data:true,
                    search_status:false,
                    show_loading:true
                },
                //process
                process:{
                    show_no_data:true,
                    show_loading:true
                },
                //num
                num:{
                    show_no_data:true,
                    show_loading:true
                },
                // 暂无数据结束
                //增加处理模块
                processShow:false,
                processArray:[],
                processAddSearchTerms:{
                    appname:'',
                    apptype:'',
                    taskstatus:'',
                    sort_orderby:'',
                    sort_rule:'descending',
                    pagesize: 10,
                    current: 1,
                },
                // 暂无数据开始
                processAdd:{
                    total_num:0,
                    show_no_data:true,
                    search_status:false,
                    show_loading:true
                },
                processAddDetailShow:false,
                processAddDetailIndex:0,
                //选择框
                multipleSelection:[],
                //增加模块详情
                processAddDetailObj:{
                    app:{
                        appname:''
                    }
                },
                //下发按钮
                processAddSaveDisabled:true
            }
        },
        watch: {
          multipleSelection:{
            handler:function(newVal){
              if(this.multipleSelection.length == 0){
                this.processAddSaveDisabled = true;
                return;
              }else{
                this.processAddSaveDisabled = false;
              }
            },
            deep:true
          }
        },
        methods:{
            historyFn(){
                //状态历史
                this.edgeHistoryList = [];
                this.show_loading = true;
                this.search_status = false;
                this.show_no_data = true;
                this.$axios.post(this.$API.edgeManage.detailStatusHistory,{
                    'device_id':this.$route.query.deviceid,
                    'pagesize':this.rowcount,
                    'current':this.currentpage,
                    'sort_orderby':this.sort_orderby,
                    'sort_rule':this.sort_rule
                }).then((res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    if(res.data.rows.length == 0){
                        this.search_status = false;
                        this.show_loading = false;
                        this.show_no_data = true;
                    }else{
                        this.show_no_data = false;
                        this.total_num = res.data.total;
                        this.edgeHistoryList=res.data.rows;
                    }
                });
            },
            detailSoftUpdateFn(){
                //软件历史
                this.appHistoryList = [];
                this.soft.show_loading = true;
                this.soft.search_status = false;
                this.soft.show_no_data = true;
                this.$axios.post(this.$API.edgeManage.detailSoftUpdate,{
                    'device_id': this.$route.query.deviceid,
                    'pagesize':this.soft.rowcount,
                    'current':this.soft.currentpage,
                    'sort_orderby':this.soft.sort_orderby,
                    'sort_rule':this.soft.sort_rule
                }).then((res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    if(res.data.actionResult.length == 0){
                        this.soft.search_status = false;
                        this.soft.show_loading = false;
                        this.soft.show_no_data = true;
                    }else{
                        this.soft.show_no_data = false;
                        this.soft.total_num = res.data.total;
                        this.appHistoryList=res.data.actionResult;
                    }
                });
            },
            startClick(index) {
                if(this.agentApplist[index].classStatus == true){
                    return;
                }
                let old_status = this.agentApplist[index].is_running;
                let device_id = this.$route.query.deviceid;
                let app_name = this.agentApplist[index].app_name;
                let handle_status='';
                if(old_status == true){
                    handle_status = 'false';
                }else{
                    handle_status = 'true';
                }
                this.$axios.post(this.$API.edgeManage.detailRun,{'device_id':device_id,'app_name':app_name,'status':handle_status}).then((res)=>{
                    // if(res.data.result == true && old_status == true){
                    //     this.agentApplist[index].status_button = '启动';
                    //     this.agentApplist[index].is_running = false;
                    //     this.agentApplist[index].status_text = '已暂停';

                    // }else if(res.data.result == true && old_status == false){
                    //     this.agentApplist[index].status_button = '暂停';
                    //     this.agentApplist[index].is_running = true;
                    //     this.agentApplist[index].status_text = '运行中';
                    // }else{
                    //     alert('操作失败');
                    // }
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    if(res.data.result == true){
                        this.agentApplist[index].classStatus = true;
                        this.agentApplist[index].status_button = '等待';
                        this.agentApplist[index].status_text = '下发中';
                        let time_list = setTimeout(()=>{
                            this.processListFn();
                        },5000);
                    }else{
                        this.$message.error('操作失败！');
                    }
                });
            },
            detailInfo(index) {

            },
            gateway_show(){
                this.gateWayisShow = true;
            },
            gateway_close(){
                this.gateWayisShow = !this.gateWayisShow;
            },
            gateway_update(){
                let device_id = this.deviceDetail.device_id;
                let secret = this.deviceDetail.secret_key;
                if(this.keyStatus == true){
                    return;
                }
                this.keyStatus = true;
                this.$axios.post(this.$API.edgeManage.detailKeyUpdate,{
                    'device_id':device_id,
                    'secret':secret
                }).then((res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    this.deviceDetail.secret_key = res.data.error;
                    this.keyStatus = false;
                })
            },
            processListFn(){
                this.agentApplist=[];
                this.process={
                    show_no_data:true,
                    show_loading:true
                }
                //处理模块信息
                this.$axios.get(this.$API.edgeManage.detailProcess + "?device_id=" + this.$route.query.deviceid).then((res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    let obj = res.data;
                    this.deviceDetail = obj;
                    if(this.deviceDetail.meta.edgent_agent_app_list == ''){
                        this.process={
                            show_no_data:true,
                            show_loading:false
                        }
                        return;
                    }
                    let agentArray = JSON.parse(this.deviceDetail.meta.edgent_agent_app_list);
                    if(agentArray.length == 0){
                        this.process={
                            show_no_data:true,
                            show_loading:false
                        }
                        return;
                    }else{
                        this.process={
                            show_no_data:false,
                            show_loading:true
                        }
                    }
                    for (var i = 0; i<agentArray.length; i++) {
                        if(agentArray[i].is_running == true){
                            agentArray[i].classStatus = false;
                            agentArray[i].status_text = '运行中';
                            agentArray[i].status_button = '暂停';
                        }else if(agentArray[i].is_running == false){
                            agentArray[i].classStatus = false;
                            agentArray[i].status_text = '已暂停';
                            agentArray[i].status_button = '启动';
                        }else{
                            agentArray[i].classStatus = true;
                            agentArray[i].status_text = '等待';
                            agentArray[i].pipestatus_text = '下发中';
                        }
                        if(agentArray[i].streaming_name == ''){
                            agentArray[i].classStatus = true;
                        }
                    }
                    this.agentApplist = agentArray;
                });
            },
            // detailClick:function (index) {
            //     this.$axios.post(this.$API.edgeManage.detailProcessDialog,{
            //         deviceidcode:this.$route.query.deviceid,
            //         appid:this.agentApplist[index].appid
            //     }).then((res)=>{
            //        console.log(res);
            //     })
            //     this.dialogShow = true;
            // },
            detailClick(index) {
                this.dialogIndex = index;
                this.dialogShow = true;

            },
            cancleClick(){
                this.dialogShow = false;
            },
            //状态历史
            handleSizeChange(val) {
                this.rowcount = val;
                this.historyFn();
            },
            handleCurrentChange(val) {
                this.currentpage = val;
                this.historyFn();
            },
            sortChangeFn(res){
                //{ column, prop, order }
                this.sort_orderby = res.prop;
                this.sort_rule = res.order;
                this.historyFn();
            },
            //软件历史
            softHandleSizeChange(val) {
                this.soft.rowcount = val;
                this.detailSoftUpdateFn();
            },
            softHandleCurrentChange(val) {
                this.soft.currentpage = val;
                this.detailSoftUpdateFn();
            },
            softSortChangeFn(res){
                //{ column, prop, order }
                this.soft.sort_orderby = res.prop;
                this.soft.sort_rule = res.order;
                this.detailSoftUpdateFn();
            },
            //重启设备
            rebootFn(){
                let device_id = this.$route.query.deviceid;
                this.$axios.post(this.$API.edgeManage.deviceReboot,{'device_id':device_id}).then((res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    if(res.data.result == true){
                        this.$message.success('重启成功！');
                    }else{
                        this.$message.error('重启失败！');
                    }
                });
            },
            //增加模块
            processAddFn(){
                this.processAddListFn();
            },
            processAddSave(){
                if(this.multipleSelection.length == 0){
                    // this.$message.info('请选择设备。');
                    return;
                }
                this.$axios.post(this.$API.processManage.upgradeSave,{
                    'appid': this.multipleSelection[0].id,
                    'deviceidArray':[this.$route.query.deviceid]
                }).then( (res) =>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    if(res.data.actionResult == undefined || res.data.actionResult.length == 0){
                        this.$message.error('下发失败!');
                    }else if(res.data.actionResult[0].result == false){
                        this.$message.error('下发失败!'+ res.data.actionResult[0].error);
                    }else if(res.data.actionResult[0].result == true){
                        this.$message.success('下发成功!');
                        this.processShow = false;
                        this.processListFn();
                        this.detailSoftUpdateFn();
                    }
                    this.$refs.processArray.clearSelection();
                });
            },
            processDetailClick(index){
                this.processAddDetailShow=true;
                this.$axios.post(this.$API.pipeLineManage.createDescApp,{
                    'id':this.processArray[index].id,
                }).then( (res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    let arrDialog = res.data.actionResult;
                    if(arrDialog.app.apptype == 'stream'){
                        arrDialog.app.apptype_text = '流式';
                    }else if(arrDialog.app.apptype == 'shadow'){
                        arrDialog.app.apptype_text = '镜像';
                    }else{
                        arrDialog.app.apptype_text = '固件';
                    }
                    this.processAddDetailObj = arrDialog;
                });
            },
            processAddListFn(status){
                this.processArray = [];
                this.processAdd.search_status = false;
                this.processAdd.show_loading = true;
                this.processAdd.show_no_data = true;
                this.processShow = true;
                //判断是否为搜索
                if(status == 1){
                  this.processAddSearchTerms.current = 1;
                }
                this.$axios.post(this.$API.processManage.processList,this.processAddSearchTerms).then( (res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    let arrTask = res.data.actionResult;
                    if(arrTask.length == 0){
                        if(status==1){
                          this.processAdd.search_status = true;
                        }else{
                          this.processAdd.search_status = false;
                        }
                        this.processAdd.show_loading = false;
                        this.processAdd.show_no_data = true;
                    }else{
                        this.processAdd.show_no_data = false;
                        this.processAdd.total_num = res.data.total;
                        this.processArray = [];
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
                            this.processArray.push(arrTask[i]);
                        }
                    }
                });

            },
            processAddSelectFn(selection,row){
                if(this.multipleSelection.length>=1){
                    this.$refs.processArray.clearSelection();
                    this.$refs.processArray.toggleRowSelection(row);
                }
            },
            processAddHandleSizeChange(val) {
                this.processAddSearchTerms.pagesize = val;
                this.processAddListFn();
            },
            processAddHandleCurrentChange(val) {
                this.processAddSearchTerms.current = val;
                this.processAddListFn();
            },
            processAddSortChangeFn(res){
                //{ column, prop, order }
                this.processAddSearchTerms.sort_orderby = res.prop;
                this.processAddSearchTerms.sort_rule = res.order;
                this.processAddListFn();
            },
            processAddHandleSelectionChange(val) {
                this.multipleSelection = val;
                console.log(this.multipleSelection);
            },
            processAddRowClickFn(row){
                //this.selectFn(row,row);
                // this.$refs.taskInfo.clearSelection();
                // this.$refs.taskInfo.toggleRowSelection(row);
            },
            processAddSearchClick(){
                this.processAddListFn(1);
            },
        }
    }
</script>

<style>
    .gateWayInfo {
        position: relative;
        left: 20px;
        bottom:5px;
    }
    .gateWayFlag {
        position: relative;
        left: 20px;
    }
    .gateWayKey {
        float:right;
        margin-right:20px;
    }
    .gateWayInfoTable {
        position: relative;
        width: 100%;
        text-align: center;
        border-collapse: collapse;
    }
   .gateWayInfoTable thead{
        height:40px;
        background:#efefef;
    }
    .gateWayInfoTable th,.gateWayInfoTable td{
      border:1px solid #d6d7dd;
    }
    .gateWayDes {
        position: relative;
        margin: 10px 20px;
        height: 30px;
        font: 14px/14px 'Microsoft YaHei';
    }
    .keyContent{
        height:100px;
        line-height:100px;
        text-align:center;
    }
</style>
