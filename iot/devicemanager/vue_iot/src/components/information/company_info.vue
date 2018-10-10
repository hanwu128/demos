<template>
  <div>
    <div class="page_title_content">
      <span class="page_title">信息管理&nbsp;&gt;&nbsp;<router-link to="/information/companyInfo" class="btn_color">企业信息</router-link></span>
    </div>
    <div class="right_content">
      <ul class="label_ul info_ul">
        <li>
          <label class="label_title">企业名称：</label><span  class="li_content"><input type="text" :placeholder="company_placeholder[0]" class="inputText infoInput" :class="{'input_readoly':!company_setting_show}" v-model="company_name" maxlength="50" :readonly='!company_setting_show'><b v-if='company_setting_show'>*</b></span>
          <p class='tip_text'>{{company_tip[0]}}</p>
        </li>
        <li>
          <label class="label_title">企业地址：</label><span  class="li_content"><input type="text" :placeholder="company_placeholder[1]" class="inputText" :class="{'input_readoly':!company_setting_show}" v-model="company_address" maxlength="50" :readonly='!company_setting_show'></span>
          <p class='tip_text'>{{company_tip[1]}}</p>
        </li>
        <li>
          <label class="label_title">联系方式：</label><span  class="li_content"><input type="text" :placeholder="company_placeholder[2]" class="inputText" :class="{'input_readoly':!company_setting_show}" v-model="company_phone" maxlength="50" :readonly='!company_setting_show'></span>
          <p class='tip_text'>{{company_tip[2]}}</p>
        </li>
        <li>
          <label class="label_title">企业邮箱：</label><span  class="li_content"><input type="text" :placeholder="company_placeholder[3]" class="inputText" :class="{'input_readoly':!company_setting_show}" v-model="company_mail" maxlength="50" :readonly='!company_setting_show'></span>
          <p class='tip_text'>{{company_tip[3]}}</p>
        </li>
      </ul>
    </div>
    <div class="btn_div">
      <span class="btn_save" :class="{'disabled': edit_show}" @click="save" v-if='company_setting_show'>保存</span>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'company_info',
    data () {
      return {
        //存储表单内容
        old_company_name:'',
        old_company_address:'',
        old_company_phone:'',
        old_company_mail:'',
        //表单内容
        company_name:'',
        company_address:'',
        company_phone:'',
        company_mail:'',
        //提示内容
        company_tip:['','','','',''],
        company_placeholder:['','','','',''],
        company_setting_show:false,
        edit_show:true
      }
    },
    created: function(){
      this.infoFn();
    },
    watch: {
      company_name:function(){
        this.checkFn();
      },
      company_address:function(){
        this.checkFn();
      },
      company_phone:function(){
        this.checkFn();
      },
      company_mail:function(){
        this.checkFn();
      }
    },
    methods: {
      infoFn:function(){
        let storage = window.localStorage;
        let login_account = JSON.parse(storage.login_account) || {};
        let company_setting = storage.company_setting;
        this.edit_show = true;
        if(company_setting == 'true'){
          this.company_setting_show = true;
          this.company_placeholder = ['请输入企业名称','请输入企业地址','请输入联系方式','请输入企业邮箱'];
        }
        this.$axios.get(this.$API.infoManage.getInfo).then( (res)=> {
          this.company_name = res.data.company_name;
          this.company_address = res.data.address;
          this.company_phone = res.data.contact;
          this.company_mail = res.data.email;
          //存储
          this.old_company_name = res.data.company_name;
          this.old_company_address = res.data.address;
          this.old_company_phone = res.data.contact;
          this.old_company_mail = res.data.email;
        });
      },
      checkFn:function(){
        if(this.company_name == this.old_company_name && this.company_address == this.old_company_address && this.company_phone == this.old_company_phone && this.company_mail == this.old_company_mail){
          this.edit_show = true;
        }else{
          this.edit_show = false;
        }
      },
      save:function(){
        if(this.edit_show == true){
          return false;
        }
        if(this.company_name == ''){
          $('.infoInput').focus();
          this.company_tip=['请输入企业名称','','','',''];
          return;
        }
        this.$axios.post(this.$API.infoManage.updateInfo,{
          'company_name':this.company_name,
          'address':this.company_address,
          'contact':this.company_phone,
          'email':this.company_mail,
          'remark':''
        }).then( (res)=> {
          this.company_tip=['','','','',''];
          if(res.data.result == true){
            this.$message.success('保存成功！');
            this.infoFn();
          }else {
            this.$message.error('保存失败！');
            this.infoFn();
          }
        }, function (res) {
          let obj = res.data;
          this.$message.error('请求失败');
        });
      }
    }
  }
</script>