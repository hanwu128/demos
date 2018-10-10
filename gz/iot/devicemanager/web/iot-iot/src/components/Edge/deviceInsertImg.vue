<template>
  <div>
    <div class="page_title_content">
      <span class="page_title"><router-link to="/edge/deImage" class="btn_color">数字镜像</router-link>&nbsp;&gt;&nbsp;数字镜像详情</span>
    </div>
    <div class="right_content">
      <!--<span class="module_title">创建数字镜像</span>-->
      <ul class="label_ul">
        <li>
          <label class="label_title">数字镜像名称：</label><span class="li_content">
          <el-input
            class="input_width"
            placeholder="请输入数字镜像"
            :maxlength='accesskeyLength'
            v-model='deviceRes.digitaltwinname'
            clearable>
          </el-input>
          <b>*</b></span>
        </li>
        <li>
          <label style='vertical-align: top;' class="label_title">数字镜像描述：</label><span class="li_content">
            <el-input
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 2}"
              :maxlength='descLength'
              resize="none"
              placeholder="请输入描述"
              v-model="deviceRes.describemessage"
              clearable>
            </el-input>
          </span>
        </li>
      </ul>
      <div class="gateWay">
        <span class="module_title">数字镜像属性</span>
        <span class="btn_create gateWayKey" @click="processAddFn">添加属性</span>
      </div>
      <div class='handleList'>
        <table class="list">
          <thead>
          <tr>
            <th style='width:5%'>编号</th>
            <th style='width:13%'>属性名称</th>
            <th style='width:5%'>单位</th>
            <th style='width:20%'>属性描述</th>
            <th style='width:6%'>属性值</th>
            <th style='width:13%'>更新时间</th>
        <!--    <th style='width:7%'>期望值</th> -->
            <th style='width:32%'>数据源信息</th>
            <th style='width:10%'>操作</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item,index) in deviceRes.attrList">
            <td>{{index+1}}</td>
            <td>{{item.digitaltwinname}}</td>
            <td>{{item.unit}}</td>
            <td>{{item.describemessage}}</td>
            <td>{{item.value}}</td>
            <td>{{item.valuetimestamp}}</td>
        <!--    <td>{{item.expectedvalue}}</td>-->
            <td>{{item.metric}}&{{JSON.stringify(item.tagkv)}}</td>
            <td style='color: #f00;'>
              <span class="det" @click="cikbj(item,index)">编辑</span>
              <span class="det"  @click="cikDelete(item,index)">删除</span>
            </td>
          </tr>
          </tbody>
        </table>
        <div class="no_data" style="display: block;margin-top:0px;" v-if="handle.show_no_data"><i
          :class="{'loading':handle.show_loading}"></i></div>
      </div>
    </div>

    <div class="el-dialog__wrapper" :class="{'show': showPop}" style="z-index: 2001;">
      <div class="el-dialog" style="margin-top: 15vh;">
        <div class="el-dialog__header">
          <span class="el-dialog__title">添加属性</span>
          <button type="button" aria-label="Close" class="el-dialog__headerbtn">
            <i class="el-dialog__close el-icon el-icon-close" @click="cikclose"></i></button>
        </div>
        <div class="el-dialog__body">
          <div class="title">
            <span class="san">属性名称：</span>
            <input type="text" placeholder="请输入名称" v-model="addatbute.digitaltwinname" class="inputText name_input el-input__inner">
            <span class="san sandw">单位：</span>
            <input type="text" placeholder="单位" v-model="addatbute.unit" class="inputText Company_input el-input__inner">
          </div>
          <div class="title">
            <span class="san">描述：</span>
            <input type="text" placeholder="请输入描述" v-model="addatbute.describemessage" class="inputText account_input el-input__inner">
          </div>
    <!--      <div class="title titl_qwz">
            <span class="san">属性值：</span>
            <span class="san san-val">{{addatbute.value}}</span>
          </div>
          <div class="title titl_qwz">
            <span class="san">期望值：</span>
            <span class="san san-val">{{addatbute.expectedvalue}}</span>
          </div>-->

          <div class="title title-zb">
            <span class="san">指标：</span>
            <!-- <input  type="text" placeholder="请输入指标" v-model="addatbute.metric" class="inputText account_input">-->
            <el-autocomplete
              v-model="addatbute.metric"
              :fetch-suggestions="querysbjxmc"
              placeholder="请输入指标"
            ></el-autocomplete>
          </div>
          <div class="title titles">
            <span class="san">标签：</span>
            <ul class="biaoq">
              <li v-for="(items,index) in addatbute.attrList">
                <!--<input type="text" placeholder="标签" v-model="items[0]" class="inputText bqian_input">-->
                <el-autocomplete
                  v-model="items[0]"
                  :fetch-suggestions="querySxbq"
                  placeholder="标签"
                ></el-autocomplete>
                &nbsp;&nbsp; = &nbsp;&nbsp;
                <el-autocomplete
                  v-model="items[1]"
                  :fetch-suggestions="querySxbqv"
                  placeholder="标签"
                ></el-autocomplete>
                <span class="delet" :class="{'disable': addatbute.attrList.length<2}"
                      @click="removeClick(index)">—</span>
              </li>
            </ul>
          </div>
          <div class="add">
            <span class="add_span" :class="{'disable': addatbute.attrList.length>7}" @click="addClick">+</span>
          </div>
        </div>
        <div class="el-dialog__footer">
          <div class="dialog-footer">
            <span class="btn_back" @click="cikclose">取消</span>
            <span type="primary" class="btn_save" @click="btnSave">确认</span>
          </div>
        </div>
      </div>
    </div>
    <!--处理模块信息-->
    <div class="btn_div">
      <router-link to="/edge/deImage" class="btn_back">取消</router-link>
      <span class="btn_save" @click="save">保存</span>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'insertImg',
    data () {
      return {
        restaurants: [],
        state4: '',
        timeout: null,
        urlInput: '',
        deviceid: '',   //设备id
        deviceRes: {
          id: '',
          /* name: '',*/
          describemessage: '',
          attrList: []
        },  //给后台参数
        // 暂无数据开始
        showTr: false,   //控制表格是否显示
        handle: {
          show_no_data: true,
          show_loading: false
        },
        idex: -1,
        accesskeyLength: 18,
        descLength: 100,
        showPop: false,
        addatbute: {
          name: "",
          describemessage: "",
          unit: "",
          metric: "",
          attrList: [
            []
          ]
        }
      }
    },
    created () {
      let device_id = this.$route.query.deviceid;
      if (device_id) {
        this.deviceRes.id = device_id;
        this.getDetail();
      }
    },
    watch: {},
    mounted() {
    },
    methods: {
      loadAll() {
        return [];
      },
      querySearchAsync(queryString, cb, type) {
          if(!queryString) {
            queryString = '';
          }
          var _this = this;
          var results = []
          var list = [];
          var url = this.$API.edgeImg.deviceSxzb+'?type='+type+'&max=20&q='+queryString;
          clearTimeout(this.timeout);
          this.timeout = setTimeout(() => {
              _this.$axios.get(url, {}).then((res) => {
              if(res.data.length>0){
            for(var i = 0; i<res.data.length; i++) {
              results.push({
                value: res.data[i]
              })
            }
          }
          cb(results);
        }).catch(function (error) {});
        },200);
      },
      //属性指标
      querysbjxmc(queryString, cb){
        this.type = 'metrics'
        this.querySearchAsync(queryString, cb, 'metrics');
      },
      //属性标签K
      querySxbq(queryString, cb){
        this.type = 'tagk'
        this.querySearchAsync(queryString, cb, 'tagk');
      },
      //属性标签K
      querySxbqv(queryString, cb){
        this.type = 'tagv'
        this.querySearchAsync(queryString, cb, 'tagv');
      },
      createStateFilter(queryString) {
        return (state) => {
          return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        }
        ;
      },
      //点击编辑
      cikbj(item, s) {
        this.idex = s;
        this.addatbute.attrList = [];
        for (var key in item.tagkv) {
          var arr = [];
          arr[0] = key;
          arr[1] = item.tagkv[key];
          this.addatbute.attrList.push(arr)
        }
        this.addatbute.digitaltwinname = item.digitaltwinname,
          this.addatbute.describemessage = item.describemessage,
          this.addatbute.metric = item.metric,
          this.addatbute.unit = item.unit
          this.addatbute.id = item.id
      /*    this.addatbute.expectedvalue = item.expectedvalue
          this.addatbute.value = item.value*/
          this.showPop = true;
      },
      //点击删除
      cikDelete(item, s) {
        this.deviceRes.attrList.splice(s, 1);
      },
      //点击取消
      cikclose() {
        this.addatbute.digitaltwinname = '';
        this.addatbute.unit = '';
        this.addatbute.describemessage = '';
        this.addatbute.metric = '';
        this.addatbute.attrList = [[]];
        this.addatbute.id = '';
    /*    this.addatbute.expectedvalue = '';
        this.addatbute.value = '';*/
        this.idex = -1;
        this.showPop = false;
        this.showPop = false;
      },
      //点击确定
      btnSave() {
        if (!this.addatbute.digitaltwinname) {
          alert('属性名称不能为空');
          return;
        }
        if (!this.addatbute.metric) {
          alert('指标不能为空');
          return;
        }
        let attrArray = {};
        for (let i = 0; i < this.addatbute.attrList.length; i++) {
          if (!this.addatbute.attrList[i][0] || !this.addatbute.attrList[i][1]) {
            alert('标签不能为空');
            return;
          }
          attrArray[this.addatbute.attrList[i][0]] = this.addatbute.attrList[i][1];
        }
        if (this.idex > -1) {
          this.deviceRes.attrList[this.idex].digitaltwinname = this.addatbute.digitaltwinname;
          this.deviceRes.attrList[this.idex].describemessage = this.addatbute.describemessage;
          this.deviceRes.attrList[this.idex].metric = this.addatbute.metric;
          this.deviceRes.attrList[this.idex].unit = this.addatbute.unit;
          this.deviceRes.attrList[this.idex].tagkv = attrArray;
        } else {
          this.deviceRes.attrList.push({
            digitaltwinname: this.addatbute.digitaltwinname,
            describemessage: this.addatbute.describemessage,
            metric: this.addatbute.metric,
            unit: this.addatbute.unit,
            tagkv: attrArray
          })
        }
        this.cikclose();
      },
      //点击添加属性
      addClick() {
        if (this.addatbute.attrList.length > 7) {
          return
        }
        this.addatbute.attrList.push(['', '']);
      },
      //点击移除
      removeClick(index) {
        if (this.addatbute.attrList.length < 2) {
          return
        }
        this.addatbute.attrList.splice(index, 1);
      },
      getDetail: function () {
        var _this = this;
        _this.$axios.post(_this.$API.edgeImg.devicedetail,
          {'id': this.deviceRes.id})
          .then((res) => {
              if(res.data.code == 420){
              this.$router.push({path: '/login'});
              }
              function add0(m){return m<10?'0'+m:m };
              function format(shijianchuo){
                //shijianchuo是整数，否则要parseInt转换
                var time = new Date(shijianchuo);
                var y = time.getFullYear();
                var m = time.getMonth()+1;
                var d = time.getDate();
                var h = time.getHours();
                var mm = time.getMinutes();
                var s = time.getSeconds();
                return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
              }
              let lists = res.data.data.attrList;
              if(lists.length>0){
                this.handle.show_no_data = false
                for(var i=0; i<lists.length;i++){
                  if(lists[i].valuetimestamp.toString().length<11){
                    lists[i].valuetimestamp = format(lists[i].valuetimestamp*1000)
                  }else{
                    lists[i].valuetimestamp = format(lists[i].valuetimestamp)
                  }
                }
              }else{
                this.handle.show_no_data = true
              }
              _this.deviceRes.digitaltwinname = res.data.data.digitaltwinname;
              _this.deviceRes.describemessage = res.data.data.describemessage;
              _this.deviceRes.attrList = lists;

       }).catch(function (error) {
        });
      },
      save(){
        if (!this.deviceRes.digitaltwinname) {
          alert("设备镜像名称不能为空");
          return;
        }
        if (this.deviceRes.attrList.length < 1) {
          alert("最少添加一条属性");
          return;
        }
        var _this = this;
        _this.$axios.post(_this.$API.edgeImg.deviceSave, _this.deviceRes).then((res) => {
          alert("保存成功"
      )
        this.$router.push('/edge/deImage');
      }).
        catch(function (error) {
        });
      },
      //增加模块
      processAddFn(){
        this.showPop = true;
      },
    }
  }
</script>
<style scoped type="text/css">
  .label_ul, .label_ul_dialog {
    margin: 0;
    width: 650px;
    padding-left: 50px;
    box-sizing: border-box;
  }

  .label_ul .label_title {
    display: inline-block;
    width: 130px;
    font: 14px/40px 'Microsoft YaHei';
    text-align: left;
    /* margin-right: 10px; */
    vertical-align: top;
  }

  .btn_create {
    float: right;
    margin-right: 20px;
  }

  .handleList .list .det {
    display: inline-block;
    padding: 0 5px;
    color: #1253f9;
    cursor: pointer;
    line-height: 30px;
  }

  .handleList .list .det:hover {
    color: #0000ff;
  }

  .list tbody tr:hover, .gateWayInfoTable tbody tr:hover {
    background: #f5f7fa;
    /* background: #edf2fc; */
  }

  .el-dialog__wrapper {
    display: none;
    background-color: rgba(0, 0, 0, 0.5);
  }

  .el-dialog__wrapper.show {
    display: block;
  }

  .el-dialog__wrapper .el-dialog {
    width: 860px;
  }

  .el-dialog__wrapper .label_ul_dialog {
    width: 100%;
    padding-left: 0;
  }

  .el-dialog__body {
    padding: 40px 65px;
    color: #333;
  }

  .el-dialog__body .title {
    width: 100%;
    padding-bottom: 15px;
    height: 48px;
    box-sizing: border-box;
  }
  .el-dialog__body .titl_qwz {
    padding-bottom: 0;
  }
  .el-dialog__body .titles {
    width: 100%;
    padding-bottom: 15px;
    height: auto;
    box-sizing: border-box;
  }

  .el-dialog__body .title span.san {
    display: inline-block;
    vertical-align: top;
    line-height: 32px;
    width: 80px;
    margin-left: 40px;
  }
  .el-dialog__body .title span.sandw {
    width: 50px;
  }
  .el-dialog__body .title span.san-val{
    color: #797979;
  }
  .el-dialog__body .inputText {
    -webkit-appearance: none;
    background-color: #fff;
    border-radius: 4px;
    border: 1px solid #d8dce5;
    box-sizing: border-box;
    color: #5a5e66;
    display: inline-block;
    font: 14px/21px 'Microsoft YaHei';
    height: 32px;
    line-height: 1;
    outline: 0;
    padding: 0 15px;
    -webkit-transition: border-color .2s cubic-bezier(.645, .045, .355, 1);
    transition: border-color .2s cubic-bezier(.645, .045, .355, 1);
  }

  .el-dialog__body .account_input {
    width: 228px;
  }

  .el-dialog__body .biaoq {
    display: inline-block;
  }

  .el-dialog__body .bqian_input {
    width: 170px;
  }

  .el-dialog__body .name_input {
    width: 228px;
  }

  .el-dialog__body .biaoq li {
    padding-bottom: 8px;
  }

  .el-dialog__body .biaoq li span.delet {
    padding: 3px 10px;
    margin-left: 10px;
    background-color: #d7d7d7;
    cursor: pointer;
  }

  .el-dialog__body .add .add_span {
    padding: 0px 10px;
    margin-left: 50px;
    font-size: 20px;
    background-color: #d7d7d7;
    cursor: pointer;
  }

  .el-dialog__body .biaoq li span.disable,
  .el-dialog__body .add .disable {
    color: #a4aab0;
    cursor: default;
    background-color: #f0f0f0;
  }

  .el-dialog__body .Company_input {
    width: 100px;
  }

  .el-dialog__footer {
    padding: 0 15px 30px;
  }

  .el-input__inner {
    height: 32px;
    line-height: 32px;
  }
</style>
<style>
  .el-dialog__wrapper .el-input__inner {
    height: 32px;
    line-height: 32px;
    width: 228px;
  }

  .title-zb .el-input__inner {
    width: 228px;
  }
</style>
