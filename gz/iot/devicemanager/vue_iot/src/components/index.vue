<template>
<div>
  <div class="login_bg">
    <div class="login_form">
      <i class="fl login_title"><span class="version_num">{{this.$API.version}}</span></i>
      <div class="form_content">
        <h3 class="font_16 margin_20">账户登录</h3>  
        <ul class="form_ul">
          <li>
            <div class="input_div"><i class="name_i"></i><input type="text" name="username" class="login_user_name" placeholder="请输入邮箱" v-model='form_login.user_name'></div>
            <p class="login_tip_text">{{login_tip.user_name_text}}</p>
          </li>
          <li>
            <div class="input_div"><i class="pwd_i"></i><input type="text" onfocus='this.type="password"' autocomplete="off" name="password" class="login_pwd" placeholder="请输入密码" v-model='form_login.password'></div>
            <p class="login_tip_text">{{login_tip.password_text}}</p>
          </li>
          <li>
            <div class="input_div verifi_code" v-if='verifi_code_show'><i class="pwd_i"></i><input type="text" name="verifi_code" class="login_verifi_code" placeholder="请输入验证码" v-model='form_login.verifi_code'></div>
            <span class="code_img" v-if='verifi_code_show' @click='codeChangeFn'><img alt="验证码" :src="code_src"/></span>
            <p class="login_tip_text" v-if='verifi_code_show'>{{login_tip.verifi_code_text}}</p>
          </li>
          <li>
            <span class="login_btn_save" @click="submitFn" @keyup.13="submitFn">登录</span>
            <router-link to="/resetPassword" class="login_btn_color">找回密码</router-link>
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>
</template>
<script>
  export default {
    name: 'index',
    data () {
      return {
        form_login: {
          user_name: '',
          password: '',
          verifi_code: ''
        },
        verifi_code_show:true,//验证码展示
        login_tip:{
          user_name_text:'',
          password_text:'',
          verifi_code_text:''
        },
        code_src: this.$API.login.imgCode + '?t='+new Date().getTime(),
        authCode:''
      }
    },
    created:function(){
      //判断是否已经登录
      if(window.localStorage.login_account != '' && window.localStorage.login_account != undefined){
        let login_permission = JSON.parse(window.localStorage.login_permission) || [];
        this.goPageFn(login_permission);
      }
      document.onkeydown = (e)=>{
        let key=window.event.keyCode;
        if(key==13){
          this.submitFn();
        }
      }
    },
    methods:{
      submitFn:function(){
        let regMail=/^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,5}$/;
        if(this.form_login.user_name == ''){
          this.login_tip = {
            user_name_text:'请输入邮箱',
            password_text:'',
            verifi_code_text:''
          }
          $('.login_user_name').focus();
          return;
        }
        if(!regMail.test(this.form_login.user_name)){
          this.login_tip = {
            user_name_text:'请输入正确的邮箱',
            password_text:'',
            verifi_code_text:''
          }
          $('.login_user_name').focus();
          return;
        }
        if(this.form_login.password == ''){
          this.login_tip = {
            user_name_text:'',
            password_text:'请输入密码',
            verifi_code_text:''
          }
          $('.login_pwd').focus();
          this.verifi_code_show = true;
          return;
        }
        if(this.verifi_code_show == true && this.form_login.verifi_code == ''){
          this.login_tip = {
            user_name_text:'',
            password_text:'',
            verifi_code_text:'请填写验证码'
          }
          $('.login_verifi_code').focus();
          return;
        }
        let md5_password = this.$crypto.createHash("md5").update(this.form_login.password).digest('hex');
        let params = {
          'loginName': this.form_login.user_name, // 登录名
          'password': md5_password, // 密码,md5加密
        }
        if(this.verifi_code_show == true){
          params = {
            'loginName': this.form_login.user_name, // 登录名
            'password': md5_password, // 密码,md5加密
            'authCode': this.form_login.verifi_code
          }
        }
        //登录
        this.$axios({
          url: this.$API.login.accountLogin,
          method: 'post',
          data: params,
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
            //本地存储权限
            let storage = window.localStorage;
            storage.removeItem("login_permission");
            storage.removeItem("login_account");
            storage["login_permission"] = JSON.stringify(res.data.data.permission) || '';
            storage["login_account"] = JSON.stringify(res.data.data.account) || '';
            storage["login_name"] = JSON.stringify(res.data.data.account.loginName) || '';
            storage["data"] = JSON.stringify(res.data.data) || '';
            let login_permission = res.data.data.permission;
            this.goPageFn(login_permission);
            this.$store.commit("login",res.data.data);
          }else if(res.data.code === 404 || res.data.code === 421){
            this.login_tip = {
              user_name_text:'',
              password_text:'账号或密码错误',
              verifi_code_text:''
            }
            $('.login_pwd').focus();
            this.verifi_code_show=true;
            return;
          }else if(res.data.code === 424){
            this.login_tip = {
              user_name_text:'账号被冻结',
              password_text:'',
              verifi_code_text:''
            }
            $('.login_pwd').focus();
            this.verifi_code_show=true;
            return;
          }else if(res.data.code === 418){
            this.login_tip = {
              user_name_text:'',
              password_text:'',
              verifi_code_text:'验证码不正确'
            }
            $('.login_verifi_code').focus();
            return;
          }
        });
      },
      codeChangeFn:function(){
        this.code_src = this.$API.login.imgCode + '?t=' + new Date().getTime();
      },
      //判断跳转
      goPageFn:function(permissionArray){
        let edge = false;
        let pipeLine = false;
        let shadow = false;
        let process = false;
        for(let i=0;i<permissionArray.length;i++){
          if(permissionArray[i] == this.$API.permission.edge_manage){
            edge = true;
          }else if(permissionArray[i] == this.$API.permission.datastream_manage){
            pipeLine = true;
          }else if(permissionArray[i] == this.$API.permission.devmirror_manage){
            shadow = true;
          }else if(permissionArray[i] == this.$API.permission.procmodule_manage){
            process = true;
          }
        }
        if(edge){
          this.$router.push({path: '/edge/manager'});
        }else if(pipeLine){
          this.$router.push({path: '/edge/pipeLine'});
        }else if(shadow){
          this.$router.push({path: '/edge/shadowManager'});
        }else if(process){
          this.$router.push({path: '/edge/processModule'});
        }else{
          this.$router.push({path: '/information/personalData'});
        }
      }
    }
      
  }
</script>
<style scoped>
.login_bg{
  position: absolute;
  /*margin-top: 60px;*/
  width: 100%;
  height: 100%;
  min-height: 600px;
  background: url(../assets/images/login_bg.png) no-repeat;
  background-size:cover;
}
.login_form{
  position: absolute;
  left: 50%;
  margin-left: -325px;
  top: 25%;
  width: 650px;
  height: 396px;
}
.login_title{
  display: inline-block;
  text-align: center;
  width: 290px;
  height: 396px;
  background: url(../assets/images/login_title.png) no-repeat;
}
.version_num{
  display: inline-block;
  width: 120px;
  height: 12px;
  clear: both;
  margin: 364px auto 0 auto;
  font: 12px/12px 'Microsoft YaHei';
  color: #fff;
}
.form_content{
  width: 360px;
  height: 396px;
  background:#fff;
  margin-left: 290px; 
}
.form_ul{
  margin: 44px auto 0 auto;
  text-align: center;
}
.form_ul li{
  height: 50px;
}
.form_ul .input_div{
  display: inline-block;
  width: 278px;
  height: 28px;
  text-align: left;
  border: 1px solid #d6d7dd;
  border-radius: 4px;
}
.form_ul .input_div i{
  display: inline-block;
  margin: 5px;
  width: 20px;
  height: 20px;
  vertical-align: middle;
  background: url(../assets/images/icon.png) no-repeat;
}
.form_ul .input_div .name_i{background-position: -75px -21px;}
.form_ul .input_div .pwd_i{background-position: -76px -45px;}
.form_ul .input_div input{
  width: 246px;
  height: 26px;
  border:none;
}
.login_btn_color{
  display: block;
  color: #0093d8;
  font: 12px/12px 'Microsoft YaHei';
  cursor: pointer;
  margin: 8px 0 0 233px;
}
.form_ul .input_div.verifi_code{
  width: 166px;
}
.form_ul .input_div.verifi_code input{
  width: 136px;
}
.code_img{
  display: inline-block;
  width:106px;
  height: 30px;
  cursor: pointer;
  vertical-align: top;
}
.code_img img{
  display: inline-block;
  width: 90px;
  height: 30px;
}
</style>
