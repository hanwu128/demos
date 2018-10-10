<template>
  <div>
    <div class="page_title_content">
      <span class="page_title">边缘分析&nbsp;&gt;&nbsp;<router-link to="/edge/processModule" class="btn_color">处理模块管理</router-link>&nbsp;&gt;&nbsp;创建处理模块</span>
    </div>
    <div class="right_content">
        <span class="module_title">创建处理模块</span>
        <form method='get'>
          <ul class="gatewayModule">
<!--             <li>
                <label>网关设备ID：</label><span><input type='text' placeholder="请输入网关设备ID" v-model='device_id'></span>
            </li> -->
            <li>
                <label>上传处理模块：</label><span style="display: inline-block;width:120px;height: 30px;line-height: 26px;margin-top: 10px; overflow: hidden;"><a href="javascript:;" class="file_prev" style="position:relative;z-index:10;">本地上传</a><input type="file" class="preFile" name="staffFile" multiple="multiple" @change="ischangeFn($event)" v-if="clearShow"></span>
            </li>
            <li>
                <label>处理模块：</label>
            </li>
        </ul>
      </form>
      <div class='handleList'>
        <table class="list">
          <thead>
            <tr>
              <th style='width:5%'>编号</th>
              <th style='width:20%'>上传文件名称</th>
              <th style='width:20%'>处理模块名称</th>
              <th style='width:10%'>处理模块类型</th>
              <th style='width:10%'>版本号</th>
              <th style='width:20%'>错误信息提示</th>
              <th style='width:15%'>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if = 'showTr'  v-for="(handleItems,index) in deviceArray">
              <td>{{index+1}}</td>
              <td>{{ handleItems.file }}</td>
              <td>{{ handleItems.appname }}</td>
              <td>{{ handleItems.apptype=='stream'?'流式' : handleItems.apptype=='shadow'?'镜像': handleItems.apptype=='agent'?'固件':''}}</td>
              <td>{{ handleItems.version }}</td>
              <td style='color: #f00;'>{{ handleItems.error }}</td>
              <td :class="'text-center'"><span class="btn_color"  :class="{'disabled': handleItems.result == false ? true : false}" @click="detailClick(index)">详情</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color" @click="deleteClick(index)">删除</span></td>
            </tr>
          </tbody>
        </table>
        <div class="no_data" style="display: block;margin-top:0px;" v-if="handle.show_no_data"><i :class="{'loading':handle.show_loading}"></i></div>
      </div>
    </div>
    <div class='btn_div'>
        <a href='javascript:;' class='btn_back' @click="cancleClick">返回</a>
        <a href='javascript:;' class='btn_save' :class="{'disabled': saveDisabled}" @click="saveClick">保存</a>
    </div>
    <!-- 配置 -->
    <el-dialog title="详情" :visible.sync="dialogShow" width="800px" v-if='dialogShow'>
      <ul class="detail_basicmsg">
        <li>
            <label>处理模块：</label><span>{{deviceArray[detailIndex].appname}}</span>
        </li>
        <li>
            <label>处理模块描述：</label><span>{{deviceArray[detailIndex].desc}}</span>
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
                <tr  v-for="(item,index) in deviceArray[detailIndex].attributes">
                    <td>{{ item.name }}</td>
                    <td>{{ item.desc }}</td>
                    <td>{{ item.default }}</td>
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
  </div>
</template>
<script>
    export default {
        name: 'handleCreate',
        data () {
          return{
            createTitle:'处理模块>创建处理模块',
            device_id:'',
            dialogShow:false,
            showTr:false,
            dialogInfo:{},
            // 暂无数据开始
            handle:{
              show_no_data:true,
              show_loading:false
            },
            // 暂无数据结束
            clearShow:true,
            deviceArray:[],
            detailIndex:0,
            saveDisabled:true
          }
        },
        watch: {
          deviceArray:{
            handler:function(newVal){
              let disabledFlag = false;
              if(this.deviceArray.length == 0){
                this.saveDisabled=true;
                return;
              }
              this.deviceArray.forEach((item)=>{
                if(item.result == false){
                  disabledFlag=true;
                  return;
                }
              });
              if(disabledFlag == true){
                this.saveDisabled=true;
              }else{
                this.saveDisabled=false;
              }

            },
            deep:true
          }
        },
        methods: {
          ischangeFn(e){
            this.deviceArray = [];
            this.handle={
              show_no_data:true,
              show_loading:true
            }
            let deviceFile = e.target.files;
            let formData = new FormData();
            for(let i=0;i<deviceFile.length;i++){
              formData.append('file', deviceFile[i]);
            }
            this.clearShow = false;
            let config = {
              headers: {
                'Content-Type': 'multipart/form-data'
              }
            }
            this.$axios.post(this.$API.processManage.processAddCheck,
              formData, config
            ).then((res)=>{
              if(res.data.code == 420){
                this.$router.push({path: '/login'});
              }
              if(res.data.result == false && res.data.apps == undefined){
                this.$message.error(res.data.error);
                this.importListShow = false;
              }else if(res.data.apps.length > 0){
                this.importListShow = true;
                this.deviceArray = res.data.apps;
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
          deleteClick(index) {
            this.$confirm('确定要删除吗？', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.deviceArray.splice(index,1);
              if(this.deviceArray.length == 0){
                this.showTr = false;
                this.handle={
                  show_no_data:true,
                  show_loading:false
                }
              }
            }).catch(() => {
               return      
            });
          },
          detailClick(index) {
            if(this.deviceArray[index].result == false){
              return;
            }else{
              this.detailIndex = index;
              this.dialogShow = true;
            }
          },
          cancleDetailClick(){
              this.dialogShow = false;
          },
          cancleClick(){
            this.$router.push({path:'/edge/processModule'});
          },
          saveClick(){
            if(this.saveDisabled == true){
              return;
            }
            this.$axios.post(this.$API.processManage.processAdd,{
              apps:this.deviceArray
            }).then((res)=>{
              if(res.data.code == 420){
                  this.$router.push({path: '/login'});
              }
              let msg = res.data.result;
              if (msg == true) {
                this.$message.success('保存成功！');
                this.$router.push({path:'/edge/processModule'});
              }else {
                this.$message.error('保存失败！');
              }
            });
          }
        }
    }
</script>
<style scoped>
    .handle_create_title {
       cursor:pointer;
    }
    .header {
        position: relative;
        top: 20px;
    }
    .create {
        position: absolute;
        right: 20px;
    }
    .gateWayInfo {
      position: relative;
      left: 20px;
      bottom:5px;
    }
    .gateWayFlag {
      position: relative;
      left: 20px;
    }
    .type {
      margin-top: 30px;
      margin-left: 5%;
      width: 80%;
    }
    .gatewayModule li{
        height: 50px;
        line-height:50px;
        padding: 0 20px;
        list-style: none;
    }
    .gatewayModule label{
        display: inline-block;
        width: 120px;
        font: 14px/50px 'Microsoft YaHei';
        text-align: left;
        vertical-align: top;
        z-index: 200;
    }
</style>