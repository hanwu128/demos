<template>
  <div class="main_cont" :class="{'activelist':hidden}">
    <div class="current_place clearfix">
      

    <div class="icon_dw fl"></div>
      <div class="fl">
        <div class="common-breadcrumb">    
            <span><span class="positionImg"></span>{{$t('common.location')}}</span>    
            <span> {{$t('common.navh3')}}</span> >   
            <a @click="childclearFn()">
              <strong class="first-level rotlink" v-if="hidden==false" style="color:#337ab7;" @click="addtoken"> {{$t('common.template')}}</strong>
              <strong class="first-level" v-if="hidden==true"><router-link to="/MaterialTem?hidden=hidden"  class="rotlink" style="color:#337ab7;">{{$t('common.template')}}</router-link></strong>
            </a> > 
            <span v-if="status==1"> {{$t('common.addTemplate')}}</span>
            <span v-if="status==2 || status==3 || status==4"> {{$t('common.templateDetailsValue')}}</span>
         </div>


      </div>

    </div>
    <div class="temListCont ">
		<div class="temH3 clearfix">
			<h1 v-if="status==1">{{$t('common.addTemplate')}}</h1>
      <h1 v-if="status==3">{{$t('common.editTemplate')}}</h1>
	  		<h1 v-else-if="status==2" class="fl">{{$t('common.templateDetailsValue')}}</h1> 
         <el-row class="addTem fr" v-if="status==2">
            <el-button size="medium" class="edit_icon" icon="el-icon-edit-outline" @click="editTemFn(state.listIndexText)">{{$t('common.edit')}}</el-button>
          </el-row>


		</div>
      <div class="temCont">
      <div class="temfrom">

      <!-- 查看物模板详情 -->
	     <el-form :model="state.listIndexText" label-width="13%" class="demo-ruleForm" v-if="status==2" size="small">
			  <el-form-item :label="$t('common.TemName')" prop="name" >
			    <el-input v-model="state.listIndexText.name" v-if="status==2" readonly="readonly" class="readonliInput"></el-input>
			  </el-form-item>
			  <el-form-item :label="$t('common.templateDetails')">
			    <el-input type="text" v-model="state.listIndexText.desp" v-if="status==2" readonly="readonly" class="readonliInput"></el-input>
			  </el-form-item>
			  <el-form-item :label="$t('common.updata')"  v-if="status==2">
			    <el-input  v-model="state.listIndexText.mtime" class="readonliInput"></el-input>
			  </el-form-item>
			  <el-form-item :label="$t('common.attribute')" >
			    <div style="border:1px solid rgb(241, 241, 241)">
          <!-- 查看物模板详情 属性列表-->
			   		<el-table :data="state.listIndexText.attr"  style="width: 100%" size="small" v-if="status==2">
					    <el-table-column prop="name" :label="$t('common.attrName')" ></el-table-column>
					    <el-table-column prop="displayname" :label="$t('common.displayname')"></el-table-column>
					    <el-table-column prop="datatype" :label="$t('common.datatype')" ></el-table-column>
              <el-table-column prop="attrtype" :label="$t('common.attrtype')" ></el-table-column>
					    <el-table-column prop="value" :label="$t('common.value')" ></el-table-column>
					    <el-table-column prop="unit" :label="$t('common.unit')" ></el-table-column>
					    
				  </el-table>
              <div class="no_data" style="display: block;" v-if="show_no_data"><i></i><span>{{$t('message.nodata')}}</span></div>
				</div>
			  </el-form-item>
			</el-form>


      <!-- 编辑物模板详情 -->
      <!--<el-form :model="state.listIndexText" ref="state.listIndexText" :rules="rules4" label-width="100px" class="demo-ruleForm edittem" v-if="status==3" size="small"> -->
      <el-form :model="state.listIndexText" ref="state.listIndexText" :rules="rules4" label-width="13%" class="demo-ruleForm edittem" v-show="status==3" size="small">
        <el-form-item :label="$t('common.TemName')" prop="name"  >
          <el-input v-model="state.listIndexText.name" maxlength="50" :placeholder="$t('common.inputtemName')" style="width:580px;margin-left:10px;"></el-input>
        </el-form-item>
        <el-form-item :label="$t('common.templateDetails')"  prop="desp">
          <el-input type="textarea"  v-if="status==3" maxlength="200" v-model="state.listIndexText.desp" :placeholder="$t('common.inputtemdec')" style="width:580px;"></el-input>
        </el-form-item>
        <el-form-item :label="$t('common.updata')"  v-if="status==3">
          <el-input  v-model="state.listIndexText.mtime" class="readonliInput"></el-input>
        </el-form-item>
        <el-form-item :label="$t('common.attribute')" prop="attr"  >
          <div style="border:1px solid rgb(241, 241, 241);">
          <!-- 编辑物模板详情 和编辑属性列表-->
          <el-table :data="state.listIndexText.attr" style="width: 100%" size="small" v-if="status==3">
              <!-- <el-table-column prop="name" :label="$t('common.attrName')" ></el-table-column> -->
              <el-table-column :label="$t('common.attrName')" v-if="status==3">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.name" ></el-input>
                  </template>
              </el-table-column>

              <el-table-column :label="$t('common.displayname')" v-if="status==3">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.displayname" :placeholder="$t('common.inputcont')"></el-input>
                  </template>
              </el-table-column>

              <!-- <el-table-column prop="datatype" :label="$t('common.datatype')" ></el-table-column> -->
              <el-table-column :label="$t('common.datatype')"  v-if="status==3">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.datatype" :placeholder="$t('message.changeDatatype')" style="width:130px;">
                      <el-option label="String" value="String"></el-option>
                      <el-option label="Boolean" value="Boolean"></el-option>
                      <el-option label="Number" value="Number"></el-option>
                      <el-option label="Object" value="Object"></el-option>
                      <el-option label="Array" value="Array"></el-option>

                    </el-select>
                  </template>
              </el-table-column>

              <el-table-column :label="$t('common.value')" v-if="status==3">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.value" :placeholder="$t('common.inputcont')" ></el-input>
                  </template>
              </el-table-column>

              <el-table-column :label="$t('common.unit')" v-if="status==3">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.unit" :placeholder="$t('common.inputcont')"></el-input>
                  </template> 
              </el-table-column>

              <!-- <el-table-column prop="attrtype" :label="$t('common.attrtype')" ></el-table-column> -->

             <el-table-column :label="$t('common.attrtype')" v-if="status==3">
                  <template slot-scope="scope">
                     <el-select v-model="scope.row.attrtype" :placeholder="$t('message.changeAttrtype')" style="width:130px;">
                      <el-option label="State" value="State"></el-option>
                      <el-option label="Stream" value="Stream"></el-option>
                      <el-option label="Description" value="Description"></el-option>
                </el-select>
                  </template>
              </el-table-column>

              <el-table-column  :label="$t('common.operation')" >
                 <template slot-scope="scope">
                  <a><i class="el-icon-delete fr"  style="font-size:18px;color:#333;position:absolute;left:10px;top:25%;" @click="delectSetFn(scope.$index,scope.row.id)"></i></a>
                </template>
              </el-table-column>

          </el-table>
              <div class="no_data" style="display: block;" v-if="show_no_data"><i></i><span>{{$t('message.nodata')}}</span></div>
 
          <el-button type="primary" size="medium" v-if="status==3" icon="el-icon-circle-plus-outline" style="margin:160px 0 20px 20px;padding:10px 12px;color:#fff;background:#3b8cFF;" @click="creatSet()">{{$t('common.addAttr')}}</el-button>
        </div>
        </el-form-item>
        <el-form-item v-if="status==3" class="MT">
          <el-button type="primary" @click="submitForm('state.listIndexText')" v-if="status==3" style="background:#3b8cFF;width:70px;height:34px;padding:7px 20px;">{{ $t('common.yes') }}</el-button>
          <el-button @click="resetForm" style="width:70px;height:34px;padding:7px 20px;">{{ $t('common.no') }}</el-button>
        </el-form-item>
      </el-form>


<!-- 添加物模板 -->
    <el-form :model="addteminfor" ref="addteminfor" :rules="rules2" label-width="13%" class="demo-ruleForm addtem" v-if="status==1" size="small">
        <el-form-item :label="$t('common.TemName')" prop="name"  >
          <el-input v-if="status==1" v-model="addteminfor.name" maxlength="50" :placeholder="$t('message.validationHintsThree')" style="width:580px;"></el-input>
        </el-form-item>
        <el-form-item :label="$t('common.templateDetails')" prop="dec">
          <el-input type="textarea"  v-if="status==1 || status==3" v-model="addteminfor.dec" maxlength="200" :placeholder="$t('message.validationHintsTwo')" style="width:580px;"></el-input>
        </el-form-item>
        <el-form-item :label="$t('common.attribute')" prop="attrstring"  >
          <el-input v-model="addteminfor.attrstring" style="display: none;"></el-input>
          <div style="border:1px solid rgb(241, 241, 241);margin-top:5px;">
            <el-table :data="state.listIndexText.attr"  style="width: 100%" size="small" v-if="status==2">
              <el-table-column prop="name" :label="$t('common.attrName')" ></el-table-column>
              <el-table-column prop="displayname" :label="$t('common.displayname')"></el-table-column>
              <el-table-column prop="datatype" :label="$t('common.datatype')" ></el-table-column>
              <el-table-column prop="value" :label="$t('common.value')" ></el-table-column>
              <el-table-column prop="unit" :label="$t('common.unit')" ></el-table-column>
              <el-table-column prop="attrtype" :label="$t('common.attrtype')" ></el-table-column>
              <el-table-column prop="name" :label="$t('common.operation')" ></el-table-column>
          </el-table>
          <el-table :data="addteminfor.attr" style="width: 100%" size="small" v-else-if="status==1">
              <el-table-column prop="name" :label="$t('common.attrName')" ></el-table-column>
              <el-table-column prop="displayname" :label="$t('common.displayname')"  v-if="status==1"></el-table-column>

              <el-table-column prop="datatype" :label="$t('common.datatype')" ></el-table-column>
              <el-table-column prop="value" :label="$t('common.value')" v-if="status==2 || status==1"></el-table-column>


              <el-table-column prop="unit" :label="$t('common.unit')" v-if="status==2 || status==1"></el-table-column>


              <el-table-column prop="attrtype" :label="$t('common.attrName')" ></el-table-column>

              <el-table-column  :label="$t('common.operation')" >
                <template slot-scope="scope">
                  <a><i class="el-icon-delete fr"  style="font-size:18px;color:#333;margin-right:80%;" @click="delectSetFn(scope.$index)"></i></a>
                </template>
              </el-table-column>

          </el-table>
              <div class="no_data" style="display: block;" v-if="show_no_data"><i></i><span>{{$t('message.nodata')}}</span></div>
 
          <el-button type="primary" size="medium" v-if="status==1" icon="el-icon-circle-plus-outline" style="margin:160px 0 20px 20px;padding:10px 12px;color:#fff;background:#3b8cFF;" @click="creatSet()">{{$t('common.addAttr')}}</el-button>
        </div>
        </el-form-item>
        <el-form-item v-if="status==1" class="MT">
          <el-button type="primary" @click="submitForm('addteminfor')" v-if="status==1 || status==3" style="background:#3b8cFF;width:70px;height:34px;padding:7px 20px;">{{ $t('common.yes') }}</el-button>
          <el-button @click="resetForm" style="width:70px;height:34px;padding:7px 20px;">{{ $t('common.no') }}</el-button>
        </el-form-item>
<!--         <el-form-item v-if="status==3" class="MT">
          <el-button type="primary" @click="submitForm()" v-if="status==1 || status==3" style="background:#3b8cFF;width:100px;height:34px;padding:7px 20px;">保存</el-button>
          <el-button @click="resetForm" style="width:100px;height:34px;padding:7px 20px;">取消</el-button>
        </el-form-item> -->
      </el-form>

			</div>
      </div>
    </div>

                <!-- 添加属性配置 -->
<el-dialog :title="$t('common.addAttr')" :visible.sync="shadowIsShow"  width="570px" :close-on-click-modal="false" :close-on-press-escape="false">
    <div class="addTemSetDialog addsetcont">
        <el-form :model="addsetinfor"  ref="addsetinfor" label-width="130px" :rules="rules3" class="demo-ruleForm">
        	  <el-form-item :label="$t('common.attrName')" prop="name" >
						    <el-input v-model="addsetinfor.name" maxlength="30" :placeholder="$t('message.validationHintsThree2')"></el-input>
						</el-form-item>
						<el-form-item :label="$t('common.displayname')" prop="displayname" >
						    <el-input v-model="addsetinfor.displayname" maxlength="30" :placeholder="$t('message.validationHintsThree')"></el-input>
						</el-form-item>
						<el-form-item :label="$t('common.attrtype')" prop="attrtype" :rules="{ required: true, message: $t('message.attrNotEmpty')}">
						    <el-select v-model="addsetinfor.attrtype" :placeholder="$t('message.changeAttrtype')" style="width:332px;">
						      <el-option label="State" value="State"></el-option>
						      <el-option label="Stream" value="Stream"></el-option>
						      <el-option label="Description" value="Description"></el-option>
						    </el-select>
						</el-form-item>
						<el-form-item :label="$t('common.datatype')" prop="datatype" :rules="{ required: true, message: $t('message.dataNOtEmpty')}">
						    <el-select v-model="addsetinfor.datatype" :placeholder="$t('message.changeDatatype')" style="width:332px;">
						      <el-option label="String" value="String"></el-option>
						      <el-option label="Boolean" value="Boolean"></el-option>
						      <el-option label="Number" value="Number"></el-option>
                  <el-option label="Object" value="Object"></el-option>
                  <el-option label="Array" value="Array"></el-option>
						    </el-select>
						</el-form-item>
						<el-form-item  :label="$t('common.value')" prop="value">
						    <el-input v-model="addsetinfor.value"></el-input>
						</el-form-item>
						<el-form-item :label="$t('common.unit')" >
						    <el-input v-model="addsetinfor.unit"></el-input>
						</el-form-item>
						<el-form-item>
					    <el-button type="primary" @click="addsetFn('addsetinfor')"  style="background:#3b8cFF;margin-left:40px;">{{ $t('common.yes') }}</el-button>
					    <el-button @click="cancelFn">{{ $t('common.no') }}</el-button>
					 </el-form-item>
        </el-form>
         
    </div>
</el-dialog>
  <el-button :plain="true" style="display:none;"></el-button>
   <el-dialog :title="$t('common.edit')" :visible.sync="shadowIsShowIsnumEdit" width="400px" :close-on-click-modal="false" :close-on-press-escape="false">
    <div class="addTemSetDialog">
        <i class="el-icon-warning icon_th" ></i>
        <div class="TA">{{$t("message.noEdit")}}</div>
        <div class="bt_icon" style="margin:20px 100px;">
           <el-button size="mini"  type="primary" @click="cancelFn()" >{{ $t('common.yes') }}</el-button>
        </div>     
    </div>
  </el-dialog>

  </div>
</template>
<script type="text/javascript">
  import {mapGetters, mapActions} from 'vuex'
   export default {
   	computed: mapGetters(['state']),
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
        this.$axios.post(this.$API.temmodel.isRename,{
              name:value
          }).then((res)=>{
             if(res.data.code==409){
              callback(new Error(_this.$t('message.temalready')));
              }else{
                  callback();
              }
        });
      };
      var validateNam4 = (rule, value, callback) => {
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
          if(value==_this.state.editname.name){
            callback();
          }

           this.$axios.post(this.$API.temmodel.isRename,{
              name:value
          }).then((res)=>{
             if(res.data.code==409){
              callback(new Error(_this.$t('message.temalready')));
              }else{
                  callback();
              }
        });

          // callback();
      };
// 物模板添加属性的属性名称校验规则
      var validateName3 = (rule, value, callback) => {
      var _this = this;
      var regEn =    /^[a-zA-Z_]{3,30}$/;
        if( 3>value.length || value.length>30){
          callback(new Error(_this.$t('message.validationHintsThree2')));
        }
        var value = (value).replace(/\s+/g,"");
          if (!regEn.test(value)  ) {
            callback(new Error(_this.$t('message.validationHintsThree2')));
          }

          var nameAttr = _this.addteminfor.attr;
          if(nameAttr.length==0){
             callback();
           }else{
            for(var i=0;i<nameAttr.length;i++){
              if(nameAttr[i].name==value){
                callback(new Error(_this.$t('message.PropertyExist')));
              }else{
                callback();
              }
            }
           }

      };
      var addsetvalue = (rule, value, callback) => {
        var _this=this;
        var datatype = this.addsetinfor.datatype;
        console.log(datatype);
        if(datatype=='Array'){
          if( Object.prototype.toString.call([value]) == '[object Array]'){
            callback();
          }else{
            callback(new Error(_this.$t('message.valueAtypism')));
          }
        }else if(datatype=='Boolean'){
          if( typeof eval(value.toLowerCase()) === 'boolean'){
            callback();
          }else{
            callback(new Error(_this.$t('message.valueAtypism')));
          }
        }else if(datatype=='Number'){
          if( typeof eval(value.toLowerCase()) === 'number'){
            callback();
          }else{
            callback(new Error(_this.$t('message.valueAtypism')));
          }
        }else if(datatype=='Object'){
          if( Object.prototype.toString.call([value]) === '[object Array]'){
            callback();
          }else{
            callback(new Error(_this.$t('message.valueAtypism')));
          }
        }else{
          callback();
        }
      };
      // 物模板添加属性的显示名称校验规则
      var validateDisplayname = (rule, value, callback) => {
        var _this = this;
      var regEn = /[`~!@#$%^&*()\-+=<>?:"{}|,.\/;'\\[\]·~！@#￥%……&*（）\-+={}|《》？：“”【】、；‘’，。、]/im;
      var regCn = /[·！#￥（——）：；“”‘、，|《。》？、【】[\]]/im;
      var nullname = (value).replace(/\s+/g,"");
      if(nullname==''){
        callback(new Error(_this.$t('message.validationHintsThree3')));
        return;
      }
        if( 3>value.length || value.length>30){
          callback(new Error(_this.$t('message.validationHintsThree3')));
        }
          if (regEn.test(value) || regCn.test(value) ) {
            callback(new Error(_this.$t('message.validationHintsThree3')));
          }
          callback();
      };
      function validatePass(rule, value, callback) {
        alert();
      }
      return {
      	shadowIsShow:false,
      	status:'',
        token:'',
        hidden:null,
        delectSetNum:[],
        shadowIsShowIsnumEdit:false,
        show_no_data:false,
        tableList : [],
        rules2: {
          name: [
            { required: true, message: this.$t('message.notemName'), trigger: 'change' },
            { validator: validateName, trigger: 'blur' }
          ],
           dec: [
            { min: 0, max: 200, message: this.$t('message.validationHintsTwo'), trigger: 'blur' }
          ],
          attrstring:[
            { required: true, message: this.$t('message.notEmpty'), trigger: 'change' },
          ]
        },
        rules4: {
          name: [
            { required: true, message: this.$t('message.notemName'), trigger: 'blur' },
            { validator: validateNam4, trigger: 'blur' }
            // { validator: validatePass, trigger: 'blur' }
          ],
           desp: [
            { min: 0, max: 200, message: this.$t('message.validationHintsTwo'), trigger: 'blur' }
          ],
          attr:[
            { required: true, message: this.$t('message.notEmpty'), trigger: 'blur' },
          ]
        },
        rules3: {
          name: [
            { required: true, message: this.$t('message.nodataEmpty'), trigger: 'blur' },
            { validator: validateName3, trigger: 'blur' }
          ],
           displayname: [
            { required: true, message: this.$t('message.noAttrEmpty'), trigger: 'blur' },
            { min: 0, max: 200, message: this.$t('message.validationHintsTwo'), trigger: 'blur' },
            { validator: validateDisplayname, trigger: 'blur' }
          ],
          value:[
             { validator: addsetvalue, trigger: 'blur' }
          ]
        },
        addteminfor:{
          name:'',
          dec:'',
          attrstring:'',
          attr:[]
        },

        addsetinfor:{
            name:'',
            displayname:'',
            datatype:'',
            value:'',
            unit:'',
            ctime:'',
            attrtype:'',
        }

      };
    },
    methods: {
      ...mapActions(['temlistDecFn','clearTemAttr']),

      submitForm(formName) {
        var _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(this.status==3){
                for(var i=0;i<this.state.listIndexText.attr.length;i++){
                if(this.state.listIndexText.attr[i].displayname==''){
                  this.$message.error( _this.$t('message.tpdateFaild'));
                  return;
                }
              }
                var deletenum = JSON.stringify(this.delectSetNum).split('[')[1].split(']')[0];
                  this.$axios.post(this.$API.temmodel.updatetem,{
                        id:this.addteminfor.id,
                        name:this.state.listIndexText.name,
                        desp:this.state.listIndexText.desp,
                        attr:this.state.listIndexText.attr,
                        delete:deletenum,
                    }).then((res)=>{
                       if(res.data.code==200){
                        this.delectSetNum=[];
                              this.$message({
                                message: _this.$t('message.tpdateSuccess'),
                                type: 'success'
                              });
                            }else{
                              this.$message.error( _this.$t('message.tpdateFaild'));
                        }
                    });
            }else if(this.status==1){
               var name = (this.addteminfor.name).replace(/\s+/g,"");
                 this.$axios.post(this.$API.temmodel.addteminfor,{
                    name:name,
                    desp:this.addteminfor.dec,
                    attr:this.addteminfor.attr
                }).then((res)=>{
                   if(res.data.code==200){
                      this.$message({
                        message: _this.$t('message.addSuccess'),
                        type: 'success'
                      });
                      this.resetForm();
                    }else if(res.data.code==409){
                      this.$message.error(_this.$t('message.temalready'));
                    }else if(res.data.code==400){
                      this.$message.error(_this.$t('message.parameterErr'));
                    }else if(res.data.code==404){
                      this.$message.error(_this.$t('message.notexit'));
                    }else if(res.data.code==419){
                      this.$message.error(_this.$t('message.pronameexit'));
                    }else if(res.data.code==420){
                      this.$message.error(_this.$t('message.valueAtypism'));
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
      childclearFn(){
        var href = {
            parentUrl:localStorage.parentUrl,
            childrenUrl:''
        }
       window.parent.postMessage(href,"*");
      },
      addtoken(){
        var token = localStorage.parentUrl.split('?')[1]
        this.$router.push({path:'/MaterialTem?'+token});
      },
      delectSetFn(index,id){
        if(id!=null){
          this.delectSetNum.push(id);
        }
        if(this.status=='1'){
          this.addteminfor.attr.splice(index,1); 
        }else{
          this.state.listIndexText.attr.splice(index,1);
        }
        if(this.addteminfor.attr.length==0 && this.state.listIndexText.attr.length==0){
          this.show_no_data=true;
          this.addteminfor.attrstring = '';
        }
      },
      creatSet(){
        this.addsetinfor={};
      	this.shadowIsShow=true;
      },
      addsetFn(formName){
        this.$refs[formName].validate((valid) => {
          if (valid) {
            debugger
            this.shadowIsShow=false;
            this.show_no_data=false;
            if(this.status=='1'){
              this.addteminfor.attr.push(this.addsetinfor);
            }else{
              this.state.listIndexText.attr.push(this.addsetinfor);
            }
            this.addteminfor.attrstring = '1';
          } else {
            return false;
          }
        });
      },
      cancelFn(){
      	this.shadowIsShow=false;
        this.shadowIsShowIsnumEdit  = false;
      },
      resetForm(){
        var token = localStorage.parentUrl.split('?')[1]
        

        if(this.hidden){
      	 this.$router.push({path:'/MaterialTem?hidden=hidden'});
        }else{
           // this.$router.push({path:'/MaterialTem'});
           this.$router.push({path:'/MaterialTem?'+token});
        }
      },
      editTemFn(val){
        if(val.istnum>0){
          this.shadowIsShowIsnumEdit=true;
          return;
        }
        this.status=3;
        // this.addteminfor.name = val.name;
        // this.addteminfor.dec = val.desp;
        // this.addteminfor.attr = val.attr;
        this.addteminfor.id = val.id
      }
    },
    created:function(){
        var _this =this;
        var href = {
            parentUrl:localStorage.parentUrl,
            childrenUrl:window.location.href
        }
       window.parent.postMessage(href,"*");
        this.status=location.hash.split('?')[1].split('&')[0].split('=')[1];
        if(this.status==1){
            this.show_no_data = true;
        if(location.hash.split('?')[1]){
              this.hidden = location.hash.split('?')[1].split('=')[2]=="hidden";
          }else{
              this.hidden=false;
          }

          this.clearTemAttr();
        }else if(this.status==2){
          if(location.hash.split('?')[1]){
              this.hidden = location.hash.split('?')[1].split('=')[3]=="hidden";
          }else{
              this.hidden=false;
          }
             this.temlistDecFn(_this);
             
        };
    }
  }
</script>