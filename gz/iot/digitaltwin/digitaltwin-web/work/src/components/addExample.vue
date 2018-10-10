<template>
  <div class="main_cont" :class="{'activelist':hidden}">
    <div class="current_place clearfix">
      
      <div class="icon_dw fl"></div>
      <div class="fl">
      <div class="common-breadcrumb">    
            <span><span class="positionImg"></span>{{ $t('common.location') }}</span>    
            <span> {{ $t("common.navh3") }}</span> >   
            <a @click="childclearFn()">
              
              <strong class="first-level" v-if="hidden==false" style="color:#337ab7;" @click="addtoken">{{ $t("common.example") }}</strong>
              <strong class="first-level" v-if="hidden==true"><router-link to="/RealColumn?hidden=hidden"   style="color:#337ab7;">{{ $t("common.example") }}</router-link></strong>
            </a> > 
            <span v-if="status==1"> {{ $t("common.addTemplate") }}</span>
            <span v-if="status==2 || status==3 || status==4"> {{ $t("common.templateDetailsValue") }}</span>
         </div>

      </div>

    </div>
    <div class="temListCont ">
  		<div class="temH3 clearfix">
  			<h1 v-if="status==1">{{ $t("common.addExample") }}</h1>
        <h1 v-if="status==3 || status==4">{{ $t("common.editExample") }}</h1>
  	  		<h1 v-else-if="status==2" class="fl">{{ $t("common.exampleDetailsValue") }}</h1> 
           <el-row class="addTem fr" v-if="status==2">
            <el-button size="medium" class="edit_icon" icon="el-icon-edit-outline" @click="editTemFn()">{{ $t("common.edit") }}</el-button>
          </el-row>

  		</div>
      <div class="exampleCont">
        <el-form :model="addExampleinfor"  :rules="rules2" ref="addExampleinfor" label-width="15%" class="demo-ruleForm" size="small">
          <el-form-item :label="$t('common.exampleName')" prop="name"  v-show="status==1" >
            <el-input  v-model="addExampleinfor.name" maxlength="50" v-if="status==1" style="width:580px;" :placeholder="$t('message.validationHintsThree')"></el-input>
          </el-form-item>

          <el-form-item :label="$t('common.exampleName')"  prop="name"  v-show="status==4">
            <el-input  v-model="addExampleinfor.name" maxlength="50" v-if="status==4"></el-input>
          </el-form-item>

          <el-form-item :label="$t('common.exampleName')"  v-if="status==2 || status==3">
            <el-input  v-if="status==2 || status==3" readonly="readonly" class="readonliInput" v-model="exampledetail.name"></el-input>
          </el-form-item>

          <el-form-item :label="$t('common.exampleDetails')" prop="dec" >
            <el-input type="textarea" v-model="addExampleinfor.dec" v-if="status==1" style="width:580px;" maxlength="200" :placeholder="$t('message.validationHintsTwo')"></el-input>
            <el-input type="textarea" v-model="exampledetail.desp" maxlength="200" v-if="status==4"></el-input>
            <el-input type="text" v-model="exampledetail.desp" v-else-if="status==2 || status==3" readonly="readonly" class="readonliInput"></el-input>
          </el-form-item>
             <el-form-item :label="$t('common.belongTem')" prop="tplname" v-if="status==2 || status==3">
            <el-input v-model="exampledetail.tplname" readonly="readonly" class="readonliInput"></el-input>
          </el-form-item>
          <el-form-item :label="$t('common.atime')" prop="atime" v-if="status==2 || status==3">
            <el-input v-model="exampledetail.atime" readonly="readonly" class="readonliInput"></el-input>
          </el-form-item>
           <el-form-item :label="$t('common.changetemplate')" prop="tem" v-if="status==1" :rules="{ required: true, message: $t('message.changetem')}"  >
            <el-select v-model="example_select" :placeholder="$t('message.selectTemplate')" @change='selectChangeFn' >
              <el-option :key="select.id" :label="select.name" :value="select.id" v-for="select in exampleSelect" ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('common.changetemplate')" v-if="status==4" >
            <el-select v-model="example_select" :placeholder="$t('message.selectTemplate')" readonly="readonly" disabled style ="float:left;">
              <el-option :key="select.id" :label="select.name" :value="select.id"  v-for="select in exampleSelect" ></el-option>
            </el-select>
            <!-- <el-row class="addTem" style="margin-left:20px;">
            <el-button size="mini" class="add_icon" @click="createClick(1)"><em></em>创建物模板</el-button>
          </el-row> -->
          </el-form-item>
          <el-form-item :label="$t('common.attribute')" v-if="selectid!=''" v-show="status==4 || status==1">
          <div style="border:1px solid rgb(241, 241, 241)">
            <el-table :data="example_set_list"  style="width: 100%" size="small" >
              <el-table-column prop="name" :label="$t('common.attrName')" ></el-table-column>
              <el-table-column prop="displayname" :label="$t('common.displayname')"></el-table-column>
              <el-table-column prop="datatype" :label="$t('common.datatype')" ></el-table-column>
              <!-- <el-table-column prop="value" :label="$t('common.currentValue')"></el-table-column> -->
              <el-table-column prop="attrtype" :label="$t('common.attrtype')" ></el-table-column>
              <!-- <el-table-column prop="mtime" :label="$t('common.updata')" ></el-table-column> -->
              <!-- <el-table-column prop="expectvalue" label="期望值" ></el-table-column> -->
              <!-- <el-table-column prop="stime" label="发送时间" ></el-table-column> -->
              <el-table-column prop="unit" :label="$t('common.unit')" ></el-table-column>
          </el-table>
          <div class="no_data" style="display: block;" v-if="show_no_data"><i></i><span>{{$t('message.nodata')}}</span></div>
          </div>
          </el-form-item>
           <!--  <div v-if="status==2">
            <el-button type="primary" size="mini" class="setting" style="position:absolute; right: 113px;top:140px;background:#3B8cFF;padding:8px 20px 8px 37px;"  v-if="isState==true" @click="setValueFn()" ><i class="iconSetting"></i>设置期望值</el-button>

            <el-button type="primary" size="mini" class="setting" style="position:absolute; right: 113px;top:140px;background:#e6e6e6;padding:8px 20px 8px 37px;border-color:#e6e6e6;" v-else-if="isState==false"><i class="iconSetting"></i>设置期望值</el-button>
          </div> -->
            <el-row class="addTem addTem_refresh" style="margin-left:20px;" v-if="status==2">
              <el-button size="mini" class="add_icon add_icon_refresh" @click="refreshFn()"><em></em>{{ $t('common.refresh') }}</el-button>
            </el-row>
          <el-form-item :label="$t('common.exampleStatus')" v-if="status==2 || status==3">
          <div style="border:1px solid rgb(241, 241, 241);" class="list_cont_ul">
          <ul class="theadSpan">
            <li>
            <div class="list_div" style="display:table;width:100%">
              <span>{{ $t('common.attrName') }}</span><span>{{ $t('common.displayname') }}</span><span>{{ $t('common.datatype') }}</span><span class="back"style="background:#f7f7f7;font-weight:bold;">{{ $t('common.currentValue') }}</span><span>{{ $t('common.attrtype') }}</span><span v-show="status==3">{{ $t('common.expectValue') }}</span><span>{{ $t('common.updata') }}</span><span v-if="isnodata">{{ $t('common.stime') }}</span><span v-if="isnodata">{{ $t('common.unit') }}</span>
            </div>
            <a href="javascript:;"  :class="{open:isnodata==false,shrink:isnodata==true}" @click="openFn()" v-if="setQWZ"></a>
            <!-- <a href="javascript:;" class="shrink" @click="shrink()"></a> -->
            </li>
          </ul>
          <ul class="theadSpan">
            <li v-for="list in exampledetail.attr">
              <div class="list_div list_ul_li" style="display:table;width:100%">
              <span>{{list.name}}</span><span>{{list.displayname}}</span><span>{{list.datatype}}</span><span class="back" style="background: #f7f7f7;font-weight:bold;">&nbsp;{{list.value}}</span><span>{{list.attrtype}}</span><span v-show="status==3"><div v-if="list.attrtype=='State'"><input v-model="list.expectvalue" placeholder="" ></input></div></span><span>{{list.mtime}}</span><span v-if="isnodata">{{list.stime}}</span><span v-if="isnodata">{{list.unit}}</span>
              </div>
            <div style="width:100%;height:200px;border-top:1px solid rgb(241, 241, 241);position:relative;" class="Mcharts" :id="list.id"  v-if="list.attrtype=='Stream' && status!==3">
                <img src="../assets/images/loading.gif" class="imgercharts" v-if="isloading">
              
              </div>

            </li>
          </ul>
          </div>
          </el-form-item>
          <el-form-item v-if="status==3 || status==4"  class="MT">
            <el-button type="primary" @click="submitFormupdate()" style="background:#3B8cFF; width:70px;height:34px;padding:7px 20px;">{{ $t('common.yes') }}</el-button>
            <el-button @click="resetForm()"  style="width:70px;height:34px;padding:7px 20px;">{{ $t('common.no') }}</el-button>
          </el-form-item>
          <el-form-item v-if="status==1"  class="MT">
            <el-button type="primary" @click="submitForm('addExampleinfor')" style="background:#3B8cFF;width:70px;height:34px;padding:7px 20px;">{{ $t('common.yes') }}</el-button>
            <el-button @click="resetForm()" style="width:70px;height:34px;padding:7px 20px;">{{ $t('common.no') }}</el-button>
          </el-form-item>
          
        </el-form>

      </div>
    </div> 

  <el-button :plain="true" style="display:none;"></el-button>

  </div>
</template>
<script type="text/javascript">
import echarts from 'echarts'
   export default {
	data() {
    var validateName = (rule, value, callback) => {
      var _this=this;
      var regEn = /[`~!@#$%^&*()\-+=<>?:"{}|,.\/;'\\[\]·~！@#￥%……&*（）\-+={}|《》？：“”【】、；‘’，。、]/im;
      var regCn = /[·！#￥（——）：；“”‘、，|《。》？、【】[\]]/im;
       var nullname = (value).replace(/\s+/g,"");
      if(nullname==''){
        callback(new Error(_this.$t('message.validationHintsThree')));
        return;
      }
        if( 3>value.length || value.length>50){
          callback(new Error(_this.$t('message.validationHintsThree')));
        }
          if (regEn.test(value) || regCn.test(value) ) {
            callback(new Error(_this.$t('message.validationHintsThree')));
          }
          if(value==_this.exampledetail.name){
            callback();
          }
          this.$axios.post(this.$API.RealColumn.isRename,{
              name:value
          }).then((res)=>{
             if(res.data.code==409){
              callback(new Error(_this.$t('message.examplealready')));
              }else{
                  callback();
              }
        });
      };

      return {
      	shadowIsShow:false,
      	status:'',
        show_no_data:false,
        selectid:'',
        token:'',
        hidden:null,
        isloading:true,
        isnodata:false,
        setQWZ:true,
        isshowid:'',
        isState:false,
        example_select:'',
        example_set_list:[],
        delectSetNum:[],
        chartsArr:[],
        resArry:[],
        setInterval:'',
        exampleSelect:'',
        rules2: {
          name: [
            { required: true, message: this.$t('message.noexampleEmpty'), trigger: 'blur' },
            { validator: validateName, trigger: 'blur' }
          ],
           dec: [
            { min: 0, max: 200, message: this.$t('common.validationHintsTwo'), trigger: 'blur' }
          ]
        },
        addExampleinfor:{
          name:'',
          dec:'',
          tem:'',
          attr:[]
        },
        exampledetail:{

        }

      };
    },
    methods: {
      cellStyle({row, column, rowIndex, columnIndex}){
          if(columnIndex === 3){ //指定坐标
              return 'background:#f1f1f1'
          }else{
              return ''
          }
      },
      rowClass({ row, rowIndex , columnIndex}) {
        if(rowIndex === 0 && columnIndex === 3){
          return 'background:#f1f1f1'
        }
      },
      selectChangeFn(index){
        this.selectid=index;
        this.addExampleinfor.tem = index;
        this.$axios.post(this.$API.temmodel.temlistdec,{
            id:index
        }).then((res)=>{
           this.example_set_list = res.data.data.attr;
           if(res.data.data.attr==null){
              this.show_no_data=true;
              return;
           }else{
            this.show_no_data = false;
           }
           function FormatDate (inputTime) {
                  var date = new Date(inputTime);
                  var y = date.getFullYear();  
                  var m = date.getMonth() + 1;  
                  m = m < 10 ? ('0' + m) : m;  
                  var d = date.getDate();  
                  d = d < 10 ? ('0' + d) : d;  
                  var h = date.getHours();
                  h = h < 10 ? ('0' + h) : h;
                  var minute = date.getMinutes();
                  var second = date.getSeconds();
                  minute = minute < 10 ? ('0' + minute) : minute;  
                  second = second < 10 ? ('0' + second) : second; 
                  return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second; 
              }
              for(var i=0; i<this.example_set_list.length; i++){
                if(this.example_set_list[i].mtime==0){
                  this.example_set_list[i].mtime='';
                  break;
                }
                // this.example_set_list[i].stime= FormatDate(this.example_set_list[i].stime)
                this.example_set_list[i].mtime= FormatDate(this.example_set_list[i].mtime)
              }
           
          
        });
      },
      refreshFn(){
        var _this = this;
        this.hidden = location.hash.split('?')[1].split('=')[3]=="hidden";
           this.$axios.post(this.$API.RealColumn.exampledec,{
                id:location.hash.split('?')[1].split('&')[1].split('=')[1] || 0
            }).then((res)=>{
              this.exampledetail = res.data.data;
              function FormatDate (inputTime) {
                  var date = new Date(inputTime);
                  var y = date.getFullYear();  
                  var m = date.getMonth() + 1;  
                  m = m < 10 ? ('0' + m) : m;  
                  var d = date.getDate();  
                  d = d < 10 ? ('0' + d) : d;  
                  var h = date.getHours();
                  h = h < 10 ? ('0' + h) : h;
                  var minute = date.getMinutes();
                  var second = date.getSeconds();
                  minute = minute < 10 ? ('0' + minute) : minute;  
                  second = second < 10 ? ('0' + second) : second; 
                  return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second; 
              }
              if(this.exampledetail.atime==0){
                this.exampledetail.atime='';
              }else{
              this.exampledetail.atime = FormatDate(this.exampledetail.atime);
              }

              if(this.exampledetail.mtime==0){
                this.exampledetail.mtime='';
              }else{
                this.exampledetail.mtime = FormatDate(this.exampledetail.mtime);

              }
              for(var i=0; i<this.exampledetail.attr.length; i++){
                if(this.exampledetail.attr[i].stime==0){
                  this.exampledetail.attr[i].stime='';
                }else{
                  this.exampledetail.attr[i].stime= FormatDate(this.exampledetail.attr[i].stime)
                }

                if(this.exampledetail.attr[i].mtime==0){
                  this.exampledetail.attr[i].mtime='';
                }else{
                  this.exampledetail.attr[i].mtime= FormatDate(this.exampledetail.attr[i].mtime)
                }
                
                
              }
              if(res.data.code==200){
                            this.$message({
                              message: _this.$t('message.refreshSuccess'),
                              type: 'success'
                            });
                          }else{
                            this.$message.error(_this.$t('message.refreshFaild'));
                      }
            });
      },
      childclearFn(){
        var href = {
            parentUrl:localStorage.parentUrlR,
            childrenUrl:''
        }
       window.parent.postMessage(href,"*");
      },
      addtoken(){
        var token = localStorage.parentUrlR.split('?')[1]
        this.$router.push({path:'/RealColumn?'+token});
      },
      submitFormupdate(){
        var _this= this;
        if(this.status==3){
                var expectvalueNew =[];
                for(var p in this.exampledetail.attr){//遍历json数组时，这么写p为索引，0,1
                  if(this.exampledetail.attr[p].expectvalue!=''){
                    if(this.exampledetail.attr[p].attrtype=='State'){
                      var obj1 = {
                        "id":this.exampledetail.attr[p].id,
                        "expectvalue":this.exampledetail.attr[p].expectvalue
                     };
                     expectvalueNew.push(obj1);
                    }
                     
                  }
                }

                this.$axios.post(this.$API.RealColumn.updateexa,{
                      id:location.hash.split('?')[1].split('&')[1].split('=')[1] || 0,
                      attr:expectvalueNew
                  }).then((res)=>{
                     if(res.data.code==200){
                            this.$message({
                              message: _this.$t('message.tpdateSuccess'),
                              type: 'success'
                            });
                            _this.status=2;
                            var id = location.hash.split('?')[1].split('&')[1].split('=')[1] || 0;
                            if(_this.hidden){
                                    _this.$router.push({path:'/addExample?status='+status +'&id='+id+'&hidden=hidden'});
                              }else{
                                    _this.$router.push({path:'/addExample?status='+status+'&id='+id});
                              }
                          }else{
                            this.$message.error(_this.$t('message.tpdateFaild'));
                      }
                  });

              }else if(this.status==4){
                this.$axios.post(this.$API.RealColumn.updatedatil,{
                      id:this.exampledetail.id,
                      name:this.addExampleinfor.name,
                      desp:this.exampledetail.desp,
                      tpl:this.selectid
                  }).then((res)=>{
                     if(res.data.code==200){
                            this.$message({
                              message: _this.$t('message.tpdateSuccess'),
                              type: 'success'
                            });
                          }else{
                            this.$message.error(_this.$t('message.tpdateFaild'));
                      }
                  });
              }
      },
      openFn(){
        if(this.isnodata==false){
            this.isnodata=true;
        var li = document.getElementsByClassName("list_cont_ul")[0].getElementsByTagName('li');
        for(var i=0;i<li.length;i++){
         var span = li[i].getElementsByTagName("span");
         for(var j=0;j<span.length;j++){
            span[j].style.width='11%';
         }
        }
        }else{
          this.isnodata=false;
        var li = document.getElementsByClassName("list_cont_ul")[0].getElementsByTagName('li');
        for(var i=0;i<li.length;i++){
         var span = li[i].getElementsByTagName("span");
         for(var j=0;j<span.length;j++){
            span[j].style.width='13%';
         }
        }
        this.isState=true;
        }
        
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
             if(this.status==1){
              var name = (this.addExampleinfor.name).replace(/\s+/g,"");
                 this.$axios.post(this.$API.RealColumn.addexample,{
                      name:name,
                      desp:this.addExampleinfor.dec,
                      tpl:this.selectid
                  }).then((res)=>{
                     if(res.data.code==200){
                      this.resetForm();
                            this.$message({
                              message: _this.$t('message.addSuccess'),
                              type: 'success'
                            });
                            var token = localStorage.parentUrl.split('?')[1]
                            if(this.hidden){
                                  this.$router.push({path:'/RealColumn?hidden=hidden'});
                            }else{
                                  // this.$router.push({path:'/RealColumn'});
                                  this.$router.push({path:'/RealColumn?'+token});
                            }
                          }else if(res.data.code==409){
                            this.$message.error(_this.$t('message.examplealready'));
                          }else if(res.data.code==400){
                            this.$message.error(_this.$t('message.parameterErr'));
                          }else if(res.data.code==404){
                            this.$message.error(_this.$t('message.notexit'));
                          }else{
                            this.$message.error(_this.$t('message.addFaild'));
                          }
                  });
              }
          } else {
            return false;
          }
        });

       
        
      },
      resetForm(){
        var _this = this;
        if(_this.status==1){
          var token = localStorage.parentUrl.split('?')[1]
          if(this.hidden){
                    this.$router.push({path:'/RealColumn?hidden=hidden'});
              }else{
                    // this.$router.push({path:'/RealColumn'});
                    this.$router.push({path:'/RealColumn?'+token});
              }
        }else{
            _this.status=2;
            var id = location.hash.split('?')[1].split('&')[1].split('=')[1] || 0;
            if(this.hidden){
                    this.$router.push({path:'/addExample?status='+status +'&id='+id+'&hidden=hidden'});
              }else{
                    this.$router.push({path:'/addExample?status='+status+'&id='+id});
              }
        }
        
        // _this.MchartsFn();
      
       
      },
      createClick(status){
         if(this.hidden){
                this.$router.push({path:'/addTem?status='+status +'&hidden=hidden'});
          }else{
                this.$router.push({path:'/addTem?status='+status});
          }
      },
      setValueFn(){
        this.status=3;
        this.setQWZ = false;
        this.isnodata=false;
      },
      editTemFn(){
        this.status=4;
        this.addExampleinfor.name=this.exampledetail.name;
        this.$axios.post(this.$API.temmodel.temList,{
                name:'',
                offset:1000000,
                page:0
            }).then((res)=>{
                this.exampleSelect = res.data.data.rows;
            });
         if(this.exampledetail.tpl != ''){
          this.example_select = this.exampledetail.tpl;
          this.selectChangeFn(this.exampledetail.tpl);
         }

      },
      MchartsFn(){
        var _self = this;
        var _this=this.exampledetail.attr;
        _self.resArry=[];
        for(var i=0;i<_this.length;i++){
          if(_self.exampledetail.attr[i].attrtype=='Stream'){
             var obj2={
              'id':_self.exampledetail.attr[i].id
            }
            _self.resArry.push(obj2);
          }
         
        }

        for(var i=0;i<_self.resArry.length;i++){
             this.$axios.post(this.$API.RealColumn.charts,{
                id:_self.resArry[i].id
            }).then((res)=>{
              _self.isloading=false;
               _self.chartsArr = [];
               if(res.data.data.time==null){
                document.getElementById(res.data.data.id).style.display='none';
               }

              for(var j=0;j<_self.resArry.length;j++){
                if(res.data.data.id==_self.resArry[j].id){
                    var time =_self.resArry[j].time = res.data.data.time
                     var value =_self.resArry[j].value = res.data.data.value;
                    var dome = document.getElementsByClassName('Mcharts');
                    // var time = _self.resArry[j].time;
                    // var value = _self.resArry[j].value;

                     for(var i=0;i<dome.length;i++){
                      var id = dome[i].getAttribute('id');
                      if(id==_self.resArry[j].id){
                        if(time==null){
                          document.getElementById(id).style.display='none';
                          break;
                        }
                         var charts =  echarts.init(dome[i]);
                        _self.setoptionFn(charts,time,value);
                        window.onresize = echarts.init(dome[i]).resize();
                        _self.setoptionFn(charts,time,value);
                        _self.exampledetail.attr[j].value = _self.resArry[j].value.pop();
                        
                      }
                     }
                }
              }
            });
        }
        // setTimeout(function(){
        //   _self.chartsArr = [];
        //   for(var j=0;j<_self.resArry.length;j++){
        //             var dome = document.getElementsByClassName('Mcharts');
        //             var time = _self.resArry[j].time;
        //             var value = _self.resArry[j].value;

        //              for(var i=0;i<dome.length;i++){
        //               var id = dome[i].getAttribute('id');
        //               if(id==_self.resArry[j].id){
        //                 if(time==null){
        //                   document.getElementById(id).style.display='none';
        //                   break;
        //                 }
        //                  var charts =  echarts.init(dome[i]);
        //                 _self.setoptionFn(charts,time,value);
        //                 window.onresize = echarts.init(dome[i]).resize();
        //                 // _this.MchartsFn();
        //               }
        //              }
        //   }
        // },500);
      },
      setoptionFn(dome,time,value){
           dome.setOption({
                          xAxis: {
                              type: 'category',
                              boundaryGap: false,
                              data: time,
                          },
                          tooltip : {
                              trigger: 'axis',
                              axisPointer: {
                                  type: 'cross',
                                  label: {
                                      backgroundColor: '#6a7985'
                                  }
                              }
                          },
                          grid: {
                            left: '6%',   //距离左边的距离
                            right: '6%', //距离右边的距离
                            top:'15%',
                            bottom:'20%'
                            },
                          yAxis: {
                              type: 'value'
                          },

                          series: [{
                              data: value,
                              type: 'line',
                              areaStyle: {},
                              itemStyle : {
                                  normal : {
                                      color:'#7eb26d30',
                                      lineStyle:{
                                          color:'#7EB26D'
                                      }
                                  }
                              },
                          }]

                        });
      }
    },

    created:function(){
        var _this =this;
        var href = {
            parentUrl:localStorage.parentUrlR,
            childrenUrl:window.location.href
        }
       window.parent.postMessage(href,"*");
        this.status=location.hash.split('?')[1].split('&')[0].split('=')[1];
        if(this.status==1){
          this.hidden = location.hash.split('?')[1].split('=')[2]=="hidden";
            this.$axios.post(this.$API.temmodel.temList,{
                name:'',
                offset:1000000,
                page:0
            }).then((res)=>{
                this.exampleSelect = res.data.data.rows;
            });
        }else if(this.status==2){
          this.hidden = location.hash.split('?')[1].split('=')[3]=="hidden";
           this.$axios.post(this.$API.RealColumn.exampledec,{
                id:location.hash.split('?')[1].split('&')[1].split('=')[1] || 0
            }).then((res)=>{
              this.exampledetail = res.data.data;
              var statelen = [];
              for(var i=0;i<this.exampledetail.attr.length;i++){
                if(res.data.data.attr[i].attrtype=='State'){
                  statelen.push(res.data.data.attr[i].attrtype);
                }
                if(statelen.length>0){
                  this.isState = true
                }else{
                  this.isState=false;
                }
                // console.log(statelen);
              }

              function FormatDate(inputTime) {  
                var date = new Date(inputTime);
                var y = date.getFullYear();  
                var m = date.getMonth() + 1;  
                m = m < 10 ? ('0' + m) : m;  
                var d = date.getDate();  
                d = d < 10 ? ('0' + d) : d;  
                var h = date.getHours();
                h = h < 10 ? ('0' + h) : h;
                var minute = date.getMinutes();
                var second = date.getSeconds();
                minute = minute < 10 ? ('0' + minute) : minute;  
                second = second < 10 ? ('0' + second) : second; 
                return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;  
              }

              if(_this.exampledetail.atime==0){
                 _this.exampledetail.atime='';

              }else{
                  _this.exampledetail.atime = FormatDate(_this.exampledetail.atime);
              }      
            
              for(var i=0; i<_this.exampledetail.attr.length; i++){
                if(_this.exampledetail.attr[i].stime==0 ){
                  _this.exampledetail.attr[i].stime='';
                }else{
                  _this.exampledetail.attr[i].stime= FormatDate(_this.exampledetail.attr[i].stime)

                }

                if(_this.exampledetail.attr[i].mtime==0){
                  _this.exampledetail.attr[i].mtime='';
                }else{
                  _this.exampledetail.attr[i].mtime= FormatDate(_this.exampledetail.attr[i].mtime)
                }
                
              }
              _this.MchartsFn();
              _this.setInterval = setInterval(function(){
                _this.MchartsFn();
              },1000)
            });
        };
    },
    destroyed:function(){
      window.clearTimeout(this.setInterval)
    }
  }
</script>





