<template>
  <div>
    <div class="page_title_content">
      <span class="page_title">信息管理&nbsp;&gt;&nbsp;<router-link to="/information/personalData" class="btn_color">个人资料</router-link></span>
    </div>
    <div class="right_content">
      <div class="header">
        <router-link to="/information/modifyPwd" class="btn_create fr">修改密码</router-link>
      </div>
      <ul class="label_ul">
        <li>
          <label class="label_title">账号：</label><span  class="li_content"><input type="text" class="inputText" :class="{'input_readoly':company_setting_show}" v-model="account_info" maxlength="20" :readonly='company_setting_show'></span>
          <p class='tip_text'>{{company_tip[0]}}</p>
        </li>
        <li>
          <label class="label_title">姓名：</label><span  class="li_content"><input type="text" placeholder="请输入姓名" class="inputText" :class="{'input_readoly':!company_setting_show}" v-model="account_name" maxlength="20" :readonly='!company_setting_show'></span>
          <p class='tip_text'>{{company_tip[1]}}</p>
        </li>
        <li>
          <label class="label_title">联系方式：</label><span  class="li_content"><input type="text" placeholder="请输入联系方式" class="inputText" :class="{'input_readoly':!company_setting_show}" v-model="account_num" maxlength="20" :readonly='!company_setting_show'></span>
          <p class='tip_text'>{{company_tip[1]}}</p>
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
        //存储
        old_account_name:'',
        old_account_num:'',
        //表单内容
        account_info:'',
        account_name:'',
        account_num:'',
        //提示内容
        company_tip:['','','',''],
        company_setting_show:true,
        edit_show:true
      }
    },
    created: function(){
      this.infoFn();
    },
    watch: {
      account_name:function(){
        this.checkFn();
      },
      account_num:function(){
        this.checkFn();
      }
    },
    methods: {
      infoFn:function(){
        this.edit_show = true;
        this.$axios.get(this.$API.infoManage.personalData).then((res)=>{
          if(res.data.code == 420){
            this.$router.push({path: '/login'});
          }else if(res.data.code == 200){
            //存储
            this.old_account_name = res.data.data.name;
            this.old_account_num = res.data.data.phone;
            this.account_info = res.data.data.loginName;
            this.account_name = res.data.data.name;
            this.account_num = res.data.data.phone;
          }
        });
      },
      checkFn:function(){
        if(this.account_name == this.old_account_name && this.account_num == this.old_account_num){
          this.edit_show = true;
        }else{
          this.edit_show = false;
        }
      },
      save:function(){
        if(this.edit_show == true){
          return false;
        }
        this.$axios({
          url: this.$API.infoManage.editPersonalData,
          method: 'post',
          data: {
            'name':this.account_name,
            'phone':this.account_num
          },
          transformRequest: [function (data) {
            let ret = ''
            for (let it in data) {
              ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
            }
            return ret
          }],
          headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).then((res)=>{
          if(res.data.code == 420){
            this.$router.push({path: '/login'});
          }else if(res.data.code == 500){
            this.$message.error('保存失败！');
            this.infoFn();
          }else if(res.data.code == 200){
            this.$message.success('保存成功！');
            this.infoFn();
          }
        });
      }
    }
  }
</script>