<template>
  <div>
    <div class="page_title_content">
      <span class="page_title">边缘分析&nbsp;&gt;&nbsp;<router-link to="/edge/manager" class="btn_color">设备管理</router-link>&nbsp;&gt;&nbsp;创建设备</span>
    </div>
    <div class="right_content">
      <span class="module_title">创建设备</span>
      <ul class="label_ul">
        <li>
          <label class="label_title">添加方式：</label>
          <span class="tl li_content">
            <span class="label_title" style="text-align: left;"><a @click='manualFn'><el-radio v-model="importShow" :label='!importShowRadio'>手动添加</el-radio></a></span>
            <span class="label_title" style="text-align: left;"><a @click='importFn'><el-radio v-model="importShow" :label='importShowRadio'>批量导入</el-radio></a></span>
          </span>
        </li>
        <li v-if='!importShow'>
          <label class="label_title">设备名称：</label><span  class="li_content">
          <el-input
            class="input_width"
            placeholder= "请输入设备名称"
            :maxlength='accesskeyLength'
            v-model='accesskey'
            clearable>
          </el-input>
          <b>*</b></span>
          <p class='tip_text'>{{accesskey_tip}}</p>
        </li>
        <li v-if='!importShow'>
          <label style='vertical-align: top;'class="label_title">设备描述：</label><span  class="li_content">
            <el-input
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 2}"
              :maxlength='descLength'
              resize="none"
              placeholder="请输入设备描述"
              v-model="desc"
              clearable>
            </el-input>
          </span>
        </li v-if='!importShow'>
        <!-- 批量导入 -->
        <li v-if='importShow'>
          <label class="label_title">批量导入：</label><span  class="li_content"><a href="javascript:;" class="file_prev" style="position:relative;z-index:10;">上传文件</a> <input type="file" class="preFile" name="staffFile" @change="ischangeFn($event)" v-if="clearShow"><a :href="this.$API.edgeManage.deviceBatchTemplate" class="btn_color" style="margin-left: 30px;">下载模板</a></span>
          <p class='tip_text normal' style="margin-top:10px;margin-bottom:20px;">（批量上传一次最多上传200个设备，设备名称为必填项，<br>设备名称内容不得重复，请仔细比对上传模板内的内容。）</p>
        </li>
      </ul>
      <div class='handleList' v-if='importListShow'>
        <table class="list">
          <thead>
            <tr>
              <th style='width:10%'>编号</th>
              <th style='width:30%'>设备名称</th>
              <th style='width:30%'>设备描述</th>
              <th style='width:30%'>错误信息提示</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if = 'showTr' v-for="(item,index) in deviceArray">
                <td>{{index+1}}</td>
                <td>{{item.access_key}}</td>
                <td>{{item.device_desc}}</td>
                <td style='color: #f00;'>{{item.error_message}}</td>
            </tr>
          </tbody>
        </table>
        <div class="no_data" style="display: block;margin-top:0px;" v-if="handle.show_no_data"><i :class="{'loading':handle.show_loading}"></i></div>
      </div>
    </div>
    <div class="btn_div">
      <router-link to="/edge/manager" class="btn_back">取消</router-link>
      <span class="btn_save" @click="save" v-if='!importShow'>保存</span>
      <span class="btn_save" :class="{'disabled': saveDisabled}"  @click="importSave" v-if='importShow' >保存</span>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'insert',
    data () {
      return {
        accesskey:'',
        deviceid:'',
        desc:'',
        focusDevice:[false,false],
        accesskey_tip:'',
        deviceid_tip:'',
        // 批量导入
        importShow:false,
        importShowRadio:true,
        importListShow:false,
        deviceArray:[],
        deviceRes:{},
        // 暂无数据开始
        showTr:false,
        handle:{
          show_no_data:true,
          show_loading:false
        },
        // 暂无数据结束
        clearShow:true,
        saveDisabled:true,
        accesskeyLength:18,
        descLength:100,
        //radio:'1'
      }
    },
    watch: {
      deviceArray:{
        handler:function(newVal){
          let disabledFlag = false;
          if(this.deviceArray.length == 0 || this.deviceRes.result == false){
            this.saveDisabled=true;
            return;
          }else{
            this.saveDisabled=false;
          }
        },
        deep:true
      }
    },
    methods: {
      save: function () {
        let reg_check = /^[\w\u4e00-\u9fa5]{4,18}$/;
        let reg_imei=/^[\u4e00-\u9fa5\\\%\&\@]{1,}$/;
        if (!reg_check.test(this.accesskey)){
          this.accesskey_tip = '请输入4-18位设备名称';
          this.deviceid_tip = '';
           return;
        }
        this.$axios.post(this.$API.edgeManage.deviceAdd,{
          'access_key':this.accesskey,
          'device_desc':this.desc
        }).then( (res)=> {
          if(res.data.code == 420){
            this.$router.push({path: '/login'});
          }
          let obj = res.data;
          if(obj.result == true){
            this.$message.success('保存成功！');
            this.$router.push({path: '/edge/manager'});
          }else if(res.data.error == '103'){
            this.$message.error('保存失败！设备名称已存在。');
          }else {
            this.$message.error('保存失败！');
          }
        }, function (res) {
          let obj = res.data;
          this.$message.error('请求失败');
        });
      },
      //手动添加
      manualFn:function(){
        this.importListShow = false;
        this.importShow = false;
        this.accesskey = '';
        this.deviceid = '';
        this.desc = '';
        this.accesskey_tip = '';
        this.deviceid_tip = '';
      },
      //批量导入
      importFn:function(){
        this.importListShow = false;
        this.importShow = true;
      },
      //上传
      ischangeFn(e){
        this.deviceArray = [];
        this.handle={
          show_no_data:false,
          show_loading:true
        }
        this.importListShow = false;
        let deviceFile = e.target.files[0];
        let formData = new FormData();
        formData.append('file', deviceFile);
        this.clearShow = false;
        let config = {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }
        this.$axios.post(this.$API.edgeManage.deviceUpload,
          formData, config
        ).then((res)=>{
          this.deviceRes={};
          if(res.data.code == 420){
            this.$router.push({path: '/login'});
          }
          //res.data = {"result":true, "error":"错误信息", "rows":[{"access_key":"test", "device_desc":"testtest"}]};
          if(res.data.result == false && res.data.rows == undefined){
            this.$message.error(res.data.error);
            this.importListShow = false;
          }else if(res.data.rows.length > 0){
            this.importListShow = true;
            this.deviceArray = res.data.rows;
            this.handle={
              show_no_data:false,
              show_loading:true
            }
            this.deviceRes = res.data;
            this.showTr = true;
          }
          this.clearShow = true;
        },()=>{
          this.clearShow = true;
        });
      },
      //保存
      importSave:function(){
        if(this.saveDisabled==true){
          // this.$message.error('请按错误提示修改模板后重新上传！');
          return;
        }else{
          this.$axios.post(this.$API.edgeManage.deviceImportSave,
              this.deviceRes
          ).then((res)=>{
              console.log(res);
              if(res.data.code == 420){
                this.$router.push({path: '/login'});
              }
              if(res.data.result == true){
                this.$message.success('保存成功！');
                this.$router.push({path: '/edge/manager'});
              }else{
                this.$message.error('保存失败！'+res.data.error);
              }
          });
        }
      }
    }
  }
</script>
