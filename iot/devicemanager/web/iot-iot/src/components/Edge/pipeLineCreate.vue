<template>
    <div>
        <div class="page_title_content">
          <span class="page_title">边缘分析&nbsp;&gt;&nbsp;<router-link to="/edge/pipeLine" class="btn_color">数据流管理</router-link>&nbsp;&gt;&nbsp;创建数据流</span>
        </div>
        <div  class="right_content tc">
            <img src="../../assets/images/pipeStep1.png" v-if='first_div'>
            <img src="../../assets/images/pipeStep2.png" v-if='second_div'>
            <img src="../../assets/images/pipeStep3.png" v-if='third_div'>
            <img src="../../assets/images/pipeStep4.png" v-if='forth_div'>
            <img src="../../assets/images/pipeStep5.png" v-if='fifth_div'>
        </div>
        <div class="right_content">
            <div class='first_step' v-if='first_div'>
                <ul class="label_ul">
                    <li>
                        <label class="label_title">数据流名称：</label><span class="li_content"><!-- <input type='text' class='inputText' placeholder='请输入数据流名称' maxlength="18" v-model='create_data.stepFirstCreateStreaming.streamingname'> -->
                        <el-input
                            class="input_width"
                            placeholder= "请输入数据流名称"
                            :maxlength='streamingnameLength'
                            v-model='create_data.stepFirstCreateStreaming.streamingname'
                        >
                        </el-input>
                        <b>*</b></span>
                        <p class='tip_text'>{{streamingname_tip}}</p>
                    </li>
                    <li>
                        <label style='vertical-align: top;'class="label_title">数据流描述：</label><span class="li_content"><!-- <textarea placeholder="请输入数据流描述" maxlength="100" v-model='create_data.stepFirstCreateStreaming.streamingdesc'></textarea> -->
                        <el-input
                          type="textarea"
                          :autosize="{ minRows: 2, maxRows: 2}"
                          :maxlength='streamingdescLength'
                          resize="none"
                          placeholder="请输入数据流描述"
                          v-model="create_data.stepFirstCreateStreaming.streamingdesc"
                        >
                        </el-input>
                        </span>
                    </li>
                    <li>
                        <label class="label_title">网关设备名称：</label><span class="li_content">
                            <!-- <select v-model='first_step_select' class='select_style' v-on:change="deviceChange">
                                <option v-for="(first_step_item,index) in first_step_list" v-bind:value="index">{{ first_step_item.access_key }}</option>
                            </select> -->
                            <el-select v-model="first_step_select" 
                                filterable 
                                remote
                                @visible-change='searchVisible'
                                :remote-method="searchCheck"
                                placeholder="请选择或输入网关设备名称"
                                @change="deviceChange"
                            >
                                <el-option
                                  v-for="item in first_step_list"
                                  :key="item.value"
                                  :label="item.label"
                                  :value="item.value">
                                </el-option>
                                <!-- <el-pagination @size-change="searchHandleSizeChange" @current-change="searchHandleCurrentChange" :current-page="search.currentpage" :total="search.total_num" layout="total,prev, pager, next, jumper" style='margin: 10px 10px 0;'></el-pagination> -->
                            </el-select>
                            <!-- <el-cascader
                                placeholder="请选择或输入网关设备名称"
                                :options="first_step_list"
                                filterable
                                :debounce="debounceNum"
                                :before-filter="searchList"
                                @change="deviceChange"
                            ></el-cascader> -->
                        </span>
                        <p class='tip_text'>{{devicename_tip}}</p>
                    </li>
                </ul>  
            </div>
            <div class='secend_step' v-if='second_div'>
                <div class="choiceModule">
                    <span>选择处理模块:</span>
                    <select  v-on:change="descApp(indexVal)" v-model='indexVal' class='select_style'>
                        <option v-for="(second_step_item,index) in second_step_list" v-bind:value="index">{{ second_step_item.appname }}</option>
                    </select>
                </div>
                <p class="choiceModule">
                    <span class="choiceModuleTitle">处理模块描述:</span>
                    <span class="moduleDesc">
                        {{appDesc}}
                    </span>
                </p>
                <div class="choiceModule" style="margin-bottom:10px;">
                    <span>采样频率设置:</span>
                    <span class="moduleDesc">
                        <input type='number' class='choiceModuleInput' placeholder="请输入采样时段" v-model='create_data.stepSecondStreamingBindingApp.samplingperiod' @keydown='keypressFn()' @paste='pasteFn()' style="ime-mode:disabled;">&nbsp;秒
                    </span>
                    <p class="create_tip_text">{{samplingperiod_tip}}</p>
                </div>
                <div class="choiceModule">属性:</div>
                <table class="list">
                    <thead>
                        <th style='width:25%'>属性名称</th>
                        <th style='width:25%'>显示名称</th>
                        <th style='width:25%'>默认值</th>
                        <th style='width:10%'>类型</th>
                        <th style='width:15%'>单位</th>
                    </thead>
                    <tbody>
                    <tr v-for="(shadowapp,index) in shadowapplist">
                        <td>{{ shadowapp.sourcename }}</td>
                        <td>{{ shadowapp.sourcedisplayname }}</td>
                        <td>{{ shadowapp.sourcedefault }}</td>
                        <td>{{ shadowapp.sourcedatatype }}</td>
                        <td>{{ shadowapp.sourceunit }}</td>
                    </tr>
                    </tbody>
                </table>    
            </div>    
            <div class='third_step' v-if='third_div'>
                <div class="configTable">
                    <div class="map_left">
                        <span class="map_title">属性列表:</span>
                        <table class="list">
                            <thead>
                                <th style='width:25%'>属性名称</th>
                                <th style='width:25%'>显示名称</th>
                                <th style='width:25%'>默认值</th>
                                <th style='width:10%'>类型</th>
                                <th style='width:15%'><input type="checkbox" v-model='third_all.state'  @click="changeAllMap" class="checkall">全选</th>
                            </thead>
                            <tbody>
                            <tr v-for="(shadowapp,index) in shadowapplist">
                                <td>{{ shadowapp.sourcename }}</td>
                                <td>{{ shadowapp.sourcedisplayname }}</td>
                                <td>{{ shadowapp.sourcedefault }}</td>
                                <td>{{ shadowapp.sourcedatatype }}</td>
                                <td><input type="checkbox" v-model='shadowapp.state'  @click="changeStyle(index)" ></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="MAP_h3">
                        <a class="btn_symbol changeMAP" href="javascript:;" @click="gtlistFn">></a>
                        <span>MAP操作</span>
                    </div>
                    <div class="map_right">
                        <span class="map_title">MAP列表:</span>
                        <table class="list">
                            <thead>
                                <th style='width:25%'>属性名称</th>
                                <th style='width:25%'>显示名称</th>
                                <th style='width:30%'>MAP</th>
                                <th style='width:20%'>操作</th>
                            </thead>
                            <tbody>
                                <tr v-for="(shadowapp,index) in subShadowapplist" :data-orderId="index">
                                    <td>{{ shadowapp.sourcename }}</td>
                                    <td><input type="text" v-model="shadowapp.displayVal"></td>
                                    <td><input type="text" v-model="shadowapp.inputVal"></td>
                                    <td><a href="javascript:;" class="btn_color" @click="deleteMapClick(index)">删除</a></td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="no_data" style="display: block;margin:0 auto;" v-if="map.show_no_data"><i :class="{'loading':map.show_loading}"></i></div>
                    </div>
                </div>
            </div>
            <div class='forth_step' v-if='forth_div'>
                <div class="choiceModule">属性列表:</div>
                <table class="list">
                    <thead>
                        <th style='width:5%'><input type="checkbox" v-model='forth_all.state'  @click="changeAllFilter" class="checkall">全选</th>
                        <th style='width:25%'>属性名称</th>
                        <th style='width:25%'>显示名称</th>
                        <th style='width:25%'>默认值</th>
                        <th style='width:10%'>类型</th>
                        <th style='width:10%'>单位</th>
                    </thead>
                    <tbody>
                    <tr v-for="(shadowapp,index) in subShadowapplist" :class="shadowapp.styleName" :forthOrderId="index">
                        <td><input type="checkbox" v-model='shadowapp.state' @click="forthCheck(index)"></td>
                        <td>{{ shadowapp.inputVal }}</td>
                        <td>{{ shadowapp.displayVal }}</td>
                        <td>{{ shadowapp.sourcedefault }}</td>
                        <td>{{ shadowapp.sourcedatatype }}</td>
                        <td>{{ shadowapp.sourceunit }}</td>
                    </tr>
                    </tbody>
                </table>
                <div class='addBtn'>
                    <a class='btn_create fr' href='javascript:;' @click='addFilterClick'>添加Filter</a>
                </div>
                <div class="choiceModule">Filter列表:(规则内容暂时只支持数字)</div>
                <table class="list">
                    <thead>
                        <th style='width:20%'>属性名称</th>
                        <th style='width:10%'>条件</th>
                        <th style='width:50%'>规则</th>
                        <th style='width:10%'>关联</th>
                        <th style='width:10%'>操作</th>
                    </thead>
                    <tbody>
                    <tr v-for="(shadowapp,index) in filterApplist">
                        <td>{{ shadowapp.inputVal }}</td>
                        <td>
                            <select v-model='shadowapp.mathematicalsymbol'>
                                <option value='>'>大于</option>
                                <option value='<'>小于</option>
                            </select>
                        </td>
                        <td><input type='number' class='filter_input' v-model='shadowapp.attributename' @keydown='keypressFn()' @paste='pasteFn()'></td>
                        <td>
                            <select v-model='shadowapp.logicalsymbol' v-if='index == filterApplist.length-1?false:true'>
                                <option value=''>无</option>
                                <option value='&amp;&amp;'>且</option>
                                <option value='||'>或</option>
                            </select>
                        </td>
                        <td><a href='javascript:;' class="btn_color" @click="deleteFilterClick(index)">删除</a></td>
                    </tr>
                    </tbody>
                </table>
                <div class="no_data" style="display: block;margin:0 auto;" v-if="filter.show_no_data"><i :class="{'loading':filter.show_loading}"></i></div>
            </div>
            <div class='fifth_step' v-if='fifth_div'>
                <ul class="label_ul">
                    <li>
                        <i class="create_done"></i>
                        <p class="insert_finish_title">数据流添加成功！</p>
                    </li>
                    <li>
                      <label class="label_title">名称：</label><span style="width:150px;" class="li_content">{{ create_data.stepFirstCreateStreaming.streamingname }}</span>
                    </li>
                    <li>
                      <label class="label_title">网关设备名称：</label>
                      <span style="width:150px;"  class="li_content">
                          {{ create_data.stepFirstCreateStreaming.access_key }}
                      </span>
                    </li>
                    <li>
                      <label class="label_title">设备描述：</label><span style="width:150px;"  class="li_content">{{ create_data.stepFirstCreateStreaming.streamingdesc }}</span>
                    </li>
                </ul>
            </div>
        </div>
        <div class='btn_div_right' v-if='first_div'>
            <a href='javascript:;' class='btn_back' @click="cancleClick">取消</a>
            <a href='javascript:;' class='btn_save' @click="firstClick">下一步</a>
        </div>
        <div class='btn_div_right' v-if='second_div'>
            <a href='javascript:;' class='btn_back' @click="backFirstClick">上一步</a>
            <a href='javascript:;' class='btn_save' @click="secondClick">下一步</a>
        </div>
        <div class='btn_div_right' v-if='third_div'>
            <a href='javascript:;' class='btn_back' @click="backSecondClick">上一步</a>
            <a href='javascript:;' class='btn_save' @click="thirdClick">下一步</a>
        </div>   
        <div class='btn_div_right' v-if='forth_div'>
            <a href='javascript:;' class='btn_back' @click="backThirdClick">上一步</a>
            <a href='javascript:;' class='btn_save' @click="forthClick">下一步</a>
        </div>
        <div class='btn_div_right' v-if='fifth_div'>
            <a href='javascript:;' class='btn_back' @click="backListClick">返回列表</a>
            <a href='javascript:;' class='btn_save' @click="goonClick">继续添加</a>
        </div>  
    </div>
</template>
<script>
    export default {
        name: 'pipeLineCreate',
        data () {
            return{
                pipe_title:'数据流管理>创建流式模块',
                first_div: true,
                second_div: false,
                third_div: false,
                forth_div: false,
                fifth_div: false,
                first_step_list:[],
                first_step_select:'',
                check_select:'',
                second_step_list: [],
                appDesc:'',
                shadowapplist:{},
                indexVal: 0,
                items:[],
                selectStatus:[],
                subShadowapplist:[],
                filterApplist:[],
                item:{},
                inputVal:[],
                streamingnameLength:18,
                streamingdescLength:100,
                debounceNum:500,
                third_all:{//第三步全选按钮
                    state:false,
                    checkone_num:0,
                    select_all:0
                },
                forth_all:{//第四步全选按钮
                    state:false,
                    checkone_num:0,
                    select_all:0
                },
                //filter暂无数据
                filter:{
                    show_no_data:true,
                    show_loading:false
                },
                //map暂无数据
                map:{
                    show_no_data:true,
                    show_loading:false
                },
                create_data:{
                    "stepFirstCreateStreaming": {
                        "streamingname": "",
                        "streamingdesc": "",
                        "deviceid": ""
                    },
                    "stepSecondStreamingBindingApp": {
                        "appname": '',
                        "samplingunit":'SECONDS',
                        "samplingperiod":'',
                    },
                    "stepThirdStreamingBindAppSource": [],
                    "stepForthStreamingBindRuler": []
                },
                streamingname_tip:'',//数据流名称提示语
                devicename_tip:'',//设备名称下拉框提示语
                samplingperiod_tip:'',
                loading:false,
                // 关联搜索分页
                search:{
                    access_key:'',
                    rowcount: 10,
                    currentpage: 1,
                    total_num:0
                }
            }
        },
        created:function () {
            // this.$axios.post(this.$API.pipeLineManage.createDeviceId,{
            // }).then( (res)=>{
            //     if(res.data.code == 420){
            //         this.$router.push({path: '/login'});
            //     }
            //     res.data.rows.forEach((item,index)=>{
            //         let obj = {};
            //         obj.label = item.access_key;
            //         obj.value = item;
            //         this.first_step_list.push(obj);
            //     });
            // });
            this.searchList();
        },
        methods: {
            searchList(access_key){
                this.search.access_key=access_key;
                this.$axios.post(this.$API.edgeManage.deviceList,{
                  'edgent_version':'',
                  'access_key':access_key,
                  'pagesize':this.search.rowcount,
                  'current':this.search.currentpage,
                  'sort_orderby':"create_stamp",
                  'sort_rule':"descending"
                }).then( (res) =>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    this.first_step_list=[];
                    res.data.rows.forEach((item,index)=>{
                        let obj = {};
                        obj.label = item.access_key;
                        obj.value = item.device_id+','+item.access_key+','+item.id;
                        this.first_step_list.push(obj);
                    });
                    this.search.total_num=res.data.total;
                });
            },
            searchCheck(access_key){
                // if(access_key == ''){
                //     access_key = this.first_step_select.split(',')[1];
                // }
                //this.first_step_select = access_key;
                this.searchList(access_key);
            },
            deviceChange(val){
                //let access_key = val.split(',')[1];
                //this.search.currentpage=1;
                //this.searchList('');
                //this.first_step_select = val;
                this.devicename_tip = '';
            },
            searchHandleSizeChange(val) {
                this.search.rowcount = val;
                this.searchList(this.search.access_key);
                this.first_step_select = this.search.access_key;
            },
            searchHandleCurrentChange(val) {
                this.search.currentpage = val;
                this.searchList(this.search.access_key);
                this.first_step_select = this.search.access_key;
            },
            searchVisible(status){
                // this.currentpage=1;
                // if(status == true && this.first_step_select==''){
                //     this.select_check=this.first_step_select;
                //     this.searchList(this.search.access_key);
                //     // this.first_step_select='';
                // }else if(status == true && this.first_step_select!=''){
                //     let select_data = this.first_step_select;
                //     let select_array = select_data.split(',');
                //     this.select_check=this.first_step_select;
                //     this.searchList(select_array[1]);
                // }else if(status == false && this.search.access_key == this.first_step_select){ 
                //     let select_data = this.first_step_select;
                //     let select_array = select_data.split(',');
                //     this.first_step_select=this.select_check;
                //     this.searchList(select_array[1]);
                // }
            },
            clearSearch(status){
                this.currentpage=1;
                this.searchList('');
            },
            searchSortChangeFn(res){
                //{ column, prop, order }
                this.search.sort_orderby = res.prop;
                this.search.sort_rule = res.order;
                this.searchList(this.search.access_key);
            },
            cancleClick(){
               this.$router.push({path:'/edge/pipeLine'});
            },
            backListClick(){
               this.$router.push({path:'/edge/pipeLine'});
            },
            goonClick(){
               this.$router.push({path:'/edge/blank'});
            },
            backFirstClick(){
                this.streamingname_tip='';
                this.devicename_tip='';
                this.first_div = true;
                this.second_div = false;
            },
            backSecondClick(){
                this.second_div = true;
                this.third_div = false;
                this.create_data.stepThirdStreamingBindAppSource = [];
            },
            backThirdClick(){
                this.third_div = true;
                this.forth_div = false;
                console.log(this.shadowapplist);
            },
            firstClick(){
                let streamingname = this.create_data.stepFirstCreateStreaming.streamingname;
                let streamingdesc = this.create_data.stepFirstCreateStreaming.streamingdesc;
                let reg_check = /^[\w\s\S\u4e00-\u9fa5]{4,18}$/;
                if(streamingname == ''){
                    this.streamingname_tip='请输入数据流名称';
                    this.devicename_tip='';
                    return
                }
                if(!reg_check.test(streamingname)){
                    this.streamingname_tip='请输入4-18个字符';
                    this.devicename_tip='';
                    return
                }
                if(this.first_step_select == ''){
                    this.streamingname_tip='';
                    this.devicename_tip = '请选择或输入网关设备名称';
                    return;
                }
                let select_data = this.first_step_select;
                let select_array = select_data.split(',');
                let device_id = select_array[0];
                let id = select_array[2];
                this.create_data.stepFirstCreateStreaming.access_key = select_array[1];
                this.create_data.stepFirstCreateStreaming.deviceid = parseInt(id, 10);
                this.$axios.post(this.$API.shadowManage.deviceSelect,{
                    'device_id':device_id,
                    'app_type':'stream'
                }).then( (res)=> {
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    if(res.data.actionResult == undefined || res.data.actionResult.length == 0){
                        this.streamingname_tip='';
                        this.devicename_tip = '该设备无处理模块，请更换设备';
                        return;
                    }else{
                        this.second_step_list = res.data.actionResult;
                        this.descApp(0);
                        this.samplingperiod_tip='';
                        this.first_div = false;
                        this.second_div = true;
                    }
                });
            },
            descApp(index){
                console.log(this.second_step_list);
                this.appDesc = this.second_step_list[index].appdesc;
                this.shadowapplist = this.second_step_list[index].appsourcearray;
                for (let i = 0; i<this.shadowapplist.length; i++) {
                    this.shadowapplist[i].state=false;
                }
                this.third_all.select_all = this.shadowapplist.length;
            },
            secondClick(){
                // let appid = this.second_step_list[this.indexVal].id;
                let appname = this.second_step_list[this.indexVal].appname;
                let deviceid = this.create_data.stepFirstCreateStreaming.deviceid;
                //let reg_check_num = /^([1-9]\d*|0)(\.\d{1,2})?$/;
                let reg_check_num = /^\+?[1-9][0-9]*$/;
                this.$axios.post(this.$API.pipeLineManage.createRepeatCheck,{
                    'deviceid':deviceid,
                    'appname':appname
                }).then( (res)=>{
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                    this.samplingperiod_tip='';
                    this.devicename_tip='';
                    if(res.data.actionResult.length > 0){
                        this.$message.error('无法创建数据流，当前选定的处理模块已存在数据流。');
                        return;
                    }else if(!reg_check_num.test(this.create_data.stepSecondStreamingBindingApp.samplingperiod)){
                        this.samplingperiod_tip = '请输入大于0的整数。';
                        return;
                    }else{
                        this.create_data.stepSecondStreamingBindingApp.appname = appname;
                        this.third_all.state = false;  
                        for (let i = 0; i<this.shadowapplist.length; i++) {
                            this.shadowapplist[i].state=false;
                        } 
                        this.subShadowapplist = [];
                        this.map = {
                            show_no_data:true,
                            show_loading:false
                        };
                        this.second_div = false;
                        this.third_div = true;
                    }
                })   
            },
            // mounted:function () {
            //     let storage = window.localStorage;
            //     let datas = storage.shadow || {}
            //     let shadowName = this.$route.query.shadowName || {}
            //     let shadowDesc = this.$route.query.shadowDesc || {}
            //     let shadowID = this.$route.query.shadowID || {}
            //     this.shadowapplist = JSON.parse(datas)
            //     for (var i = 0, lens = this.shadowapplist.length; i < lens; i++) {
            //         this.shadowapplist[i].styleName = ''
            //     }
            //     console.log(this.shadowapplist)
            // },
            changeStyle(index) {
                this.shadowapplist[index].state = !this.shadowapplist[index].state;
                if(this.shadowapplist[index].state == true){
                    this.third_all.checkone_num++;
                    if(this.third_all.select_all == this.third_all.checkone_num){
                       this.third_all.state = true;     
                    }
                }else{
                    this.third_all.checkone_num--;
                    this.third_all.state = false;
                }
            },
            changeAllMap(event){
                this.third_all.state = !this.third_all.state;
                if(this.third_all.state == true){
                    if(this.third_all.select_all == 0){
                        this.third_all.state == false;
                        return;
                    }
                    for (var i = 0; i<this.shadowapplist.length; i++) {
                        this.shadowapplist[i].state = true;
                    }
                    this.third_all.checkone_num = this.shadowapplist.length;
                }else{
                    for (var i = 0; i<this.shadowapplist.length; i++) {
                        this.shadowapplist[i].state = false;
                    }
                    this.third_all.checkone_num = 0;
                } 
            },
            gtlistFn() {
                let tempArr = [];
                for (var i = 0; i<this.shadowapplist.length; i++) {
                    if(this.shadowapplist[i].state == true){
                        this.shadowapplist[i].inputVal = this.shadowapplist[i].sourcename;
                        this.shadowapplist[i].displayVal = this.shadowapplist[i].sourcedisplayname;
                        tempArr.push(this.shadowapplist[i]);
                    }
                }
                if(tempArr.length == 0){
                    this.$message.info('请选择属性。');
                    return;
                }
                this.map = {
                    show_no_data:false,
                    show_loading:false
                };
                this.subShadowapplist = tempArr;
                //清空选中
                this.shadowapplist.forEach((item,index)=>{
                    this.shadowapplist[index].state = false;
                });
                this.third_all.checkone_num = 0;
                this.third_all.state = false;
            },
            thirdClick(){
                if(this.subShadowapplist.length<1){
                    this.$message.info('请选择数据。');
                    return;
                }
                for (var i=0; i < this.subShadowapplist.length;i++) {
                    if(!!!this.subShadowapplist[i].displayVal){
                        this.$message.info('请输入显示名称。');
                        return;
                    }
                    if(!!!this.subShadowapplist[i].inputVal){
                        this.$message.info('请输入MAP值。');
                        return;
                    }
                }
                this.create_data.stepThirdStreamingBindAppSource = [];
                for(var i = 0, lens = this.subShadowapplist.length; i < lens;i++){
                    let forthJson = {};
                    forthJson.sourcename = this.subShadowapplist[i].sourcename;
                    forthJson.mapname = this.subShadowapplist[i].inputVal;
                    forthJson.mapdisplayname = this.subShadowapplist[i].displayVal;
                    forthJson.sourcedefault = this.subShadowapplist[i].sourcedefault;
                    forthJson.sourcedatatype = this.subShadowapplist[i].sourcedatatype;
                    forthJson.sourceunit = this.subShadowapplist[i].sourceunit;
                    this.create_data.stepThirdStreamingBindAppSource.push(forthJson);
                    //初始化第四步数据
                    this.filterApplist=[];
                    this.forth_all.state = false; 
                    this.subShadowapplist[i].state=false;
                    this.filter = {
                        show_no_data:true,
                        show_loading:false
                    };
                }
                this.forth_all.select_all = this.subShadowapplist.length;
                this.third_div = false;
                this.forth_div = true;
            },
            forthClick(){
                for (var i=0; i < this.filterApplist.length;i++) {
                    if(!!!this.filterApplist[i].mathematicalsymbol){
                        this.$message.info('请选择条件。');
                        return;
                    }else if(!!!this.filterApplist[i].attributename ){
                        this.$message.info('请输入规则。');
                        return;
                    }else if(!!!this.filterApplist[i].logicalsymbol && i!=this.filterApplist.length-1){
                        this.$message.info('请选择关联。');
                        return;
                    }
                }
                let statusFlag = -1,filterArray=[];
                for (var i = 0, lens = this.filterApplist.length; i < lens; i++ ) {
                    let filterObj={};
                    filterObj.rightvalue = this.filterApplist[i].attributename;
                    filterObj.logicalsymbol = this.filterApplist[i].logicalsymbol;
                    filterObj.mathematicalsymbol = this.filterApplist[i].mathematicalsymbol;
                    filterObj.appsourceid = 0;
                    filterObj.attributename = this.filterApplist[i].inputVal;
                    filterObj.attributevaluetype = this.filterApplist[i].sourcedatatype;
                    if(i == lens-1){
                        this.filterApplist[i].logicalsymbol = '';
                    }
                    if(this.filterApplist[i].logicalsymbol == '' && filterArray.length >= 0){
                        filterArray.push(filterObj);
                        this.create_data.stepForthStreamingBindRuler.push(filterArray);
                        filterArray = [];
                    }else{
                        filterArray.push(filterObj);
                    }
                }
                this.forth_div = false;
                this.fifth_div = true;
                this.$axios.post(this.$API.pipeLineManage.createPipeLine,this.create_data).then(function(res){
                    if(res.data.code == 420){
                        this.$router.push({path: '/login'});
                    }
                });  
            },
            forthCheck(index){
                this.subShadowapplist[index].state = !this.subShadowapplist[index].state;
                if(this.subShadowapplist[index].state == true){
                    this.forth_all.checkone_num++;
                    if(this.forth_all.select_all == this.forth_all.checkone_num){
                       this.forth_all.state = true;     
                    }
                }else{
                    this.forth_all.checkone_num--;
                    this.forth_all.state = false;
                }

            },
            addFilterClick(){
                let check_flag = false;
                let filterLength = this.filterApplist.length;
                let addFilterArray = [];
                for (var i = 0; i<this.subShadowapplist.length; i++) {
                    if(this.subShadowapplist[i].state == true){
                        check_flag = true;
                        let filterAddObj = {};
                        filterAddObj.inputVal = this.subShadowapplist[i].inputVal;
                        filterAddObj.mathematicalsymbol = '>';
                        filterAddObj.attributename = '';
                        filterAddObj.logicalsymbol = '';
                        filterAddObj.sourcedatatype = this.subShadowapplist[i].sourcedatatype;
                        this.filterApplist.push(filterAddObj);
                    }
                }
                if(check_flag == false){
                    this.$message.info('请选择属性。');
                    return;
                };
                this.filter = {
                    show_no_data:false,
                    show_loading:false
                };
                //清空选中
                this.subShadowapplist.forEach((item,index)=>{
                    this.subShadowapplist[index].state = false;
                });
                this.forth_all.checkone_num = 0;
                this.forth_all.state = false;
            },
            changeAllFilter(index){
                this.forth_all.state = !this.forth_all.state;
                if(this.forth_all.state == true){
                    if(this.forth_all.select_all == 0){
                        this.forth_all.state == false;
                        return;
                    }
                    for (var i = 0; i<this.subShadowapplist.length; i++) {
                        this.subShadowapplist[i].state = true;
                    }
                    this.forth_all.checkone_num = this.subShadowapplist.length;
                }else{
                    for (var i = 0; i<this.subShadowapplist.length; i++) {
                        this.subShadowapplist[i].state = false;
                    }
                    this.forth_all.checkone_num = 0;
                }
            },
            deleteFilterClick(index){
                this.$confirm('确定要删除吗？', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                  this.filterApplist.splice(index,1);
                  if(this.filterApplist.length == 0){
                    this.filter = {
                        show_no_data:true,
                        show_loading:false
                    };
                  }
                }).catch(() => {
                   return      
                });
            },
            deleteMapClick(index){
                this.$confirm('确定要删除吗？', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                    this.subShadowapplist.splice(index,1);
                    if(this.subShadowapplist.length == 0){
                        this.map = {
                            show_no_data:true,
                            show_loading:false
                        };
                    }
                }).catch(() => {
                   return      
                });
            },
            keypressFn(){
                if(event.keyCode == 69 || event.keyCode == 229){
                    event.returnValue=false;
                }
            },
            pasteFn(){
                event.returnValue=false;
            }
            
        }
    }
</script>

<style scoped>
    .managerBg {
        background-color: white;
        height: 100%;
        width: 89%;
        text-align: left;
        position: relative;
        left: 10%;
        right: 10px;
    }
    .searchSelect {
        position: relative;
        left:50px;
    }
    .pipe_line_nav{
        height:60px;
        text-align:center;
    }
    .pipe_line_flag{
        display:inline-block;
        padding:0;
        text-align:center;
    }
    .pipe_line_flag li{
        height: 50px;
        line-height:50px;
        padding: 0 30px;
        float:left;
    }
    .first_step{text-align:center;margin-top:40px;}
    .forth_step .list,.secend_step .list{
        width:90%;
    }
    .titleBg{margin-top:40px;height:50px;}
    .titleBg span{display:inline-block;margin: 0 40% 0 40px;}
    .addBtn{height:50px;margin:30px 5% 0 0;}
    .filter_input{width:80%}
    .insert_finish_title {
        text-align: center;
    }
    .fifth_step .finishTable {
        position: relative;
        left: 45%;
        width: 55%;
    }
    .choiceModuleInput{
        width:206px;
        height:26px;
    }
</style>