<template>
  <div>
    <div class="reset_pwd_title_content">
      <span class="page_title"><router-link to="/login" class="btn_color">登录</router-link>&nbsp;&gt;&nbsp;找回密码</span>
    </div>
    <div class="right_content reset_pwd_content">
      <ul class="label_ul reset_pwd_label_ul" v-if='!btn_show'>
        <li>
          <label class="label_title" style="width: 80px;">账号：</label><span  class="li_content"><input type="text" placeholder="请输入邮箱" class="inputText account_name" v-model="account_name" maxlength="40"></span>
          <p class='tip_text' style='margin-left:136px;'>{{account_name_text}}</p>
        </li>
        <li>
          <label class="label_title" style="width: 80px;">验证码：</label><span  class="li_content"><input type="text" placeholder="请输入验证码" class="inputText" id="inputVerifi" :class="{'input_readoly':!company_setting_show}" v-model="verifi_code" maxlength="20" :readonly='!company_setting_show'>
          <a class="btn_save" v-show="show_code" @click='getCodeFn'>{{get_code_text}}</a>
          <a v-show="!show_code" class="btn_save disabled">{{count}} s</a>
          </span>
          <p class='tip_text' style='margin-left:136px;'>{{verifi_code_text}}</p>
        </li>
      </ul>
      <ul class="label_ul reset_pwd_label_ul" v-if='btn_show'>
        <li>
          <label class="label_title" style="width: 80px;">新密码：</label><span  class="li_content"><input type="text"  onfocus='this.type="password"' autocomplete="off" placeholder="请输入密码" class="inputText pwd_code"v-model="pwd_code" maxlength="20"></span>
          <p class='tip_text normal' style='margin-left:136px;'>密码长度8-20位，数字、字母、字符至少包含两种</p>
          <p class='tip_text' style='margin-left:136px;'>{{pwd_new_text}}</p>
        </li>
        <li>
          <label class="label_title" style="width: 80px;">再次确认：</label><span  class="li_content"><input type="text"  onfocus='this.type="password"' autocomplete="off" placeholder="请输入密码" class="inputText pwd_code_check" :class="{'input_readoly':!company_setting_show}" v-model="pwd_code_check" maxlength="20">
          </span>
          <p class='tip_text' style='margin-left:136px;'>{{pwd_code_text}}</p>
        </li>
      </ul>
      <div class="btn_div reset_pwd_btn_div">
        <span class="btn_save" @click="nextFn" v-if='!btn_show'>下一步</span>
        <span class="btn_save" @click="save" v-if='btn_show'>确定</span>
      </div>
    </div>
  </div>
</template>
<script>
  import timerComponent from './common/timer.vue'
  export default {
    name: 'company_info',
    data () {
      return {
        //表单内容
        account_name:'',
        verifi_code:'',
        //提示内容
        verifi_code_text:'',
        account_name_text:'',
        btn_show:false,
        company_setting_show:true,
        get_code_text:'获取验证码',
        pwd_new_text:'',
        pwd_code_text:'',
        //密码
        pwd_code:'',
        pwd_code_check:'',
        tip_show:true,
        show_code: true,
        time_count:60,
        count: 60,
        timer: null,
        click_allow:true//可点击
      }
    },
    components: {
      timerComponent
    },
    methods: {
      nextFn:function(){
        let regMail=/^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,5}$/;
        if(this.account_name == ''){
          this.account_name_text = '请输入邮箱';
          this. verifi_code_text = '';
          $('.account_name').focus();
          return;
        }
        if(!regMail.test(this.account_name)){
          this.account_name_text = '请输入正确的邮箱';
          this. verifi_code_text = '';
          $('.account_name').focus();
          return;
        }
        if(this.verifi_code == ''){
          this.account_name_text = '';
          this.verifi_code_text = '请输入验证码';
          $('#inputVerifi').focus();
          return;
        }
        this.$axios({
          url: this.$API.infoManage.resetCheckPwd,
          method: 'post',
          data: {
            'loginName':this.account_name,
            'authCode':this.verifi_code
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
          if(res.data.code == 404){
            this.account_name_text = '账号不存在';
            this. verifi_code_text = '';
            $('.account_name').focus();
          }else if(res.data.code == 418){
            this.account_name_text = '';
            this.verifi_code_text = '验证码不正确';
            $('#inputVerifi').focus();
            return;
          }else if(res.data.code == 200){
            this.btn_show = true;
          }
        });
      },
      callbackTimer:function(){
      },
      getCodeFn:function(){
        let regMail=/^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,5}$/;
        if(this.account_name == ''){
          this.account_name_text = '请输入邮箱';
          this. verifi_code_text = '';
          $('.account_name').focus();
          return;
        }
        if(!regMail.test(this.account_name)){
          this.account_name_text = '请输入正确的邮箱';
          this. verifi_code_text = '';
          $('.account_name').focus();
          return;
        }
        if (!this.timer) {
          if(this.click_allow == false){
            return;  
          }
          this.$axios.post(this.$API.infoManage.getCode,{
            'loginName':this.account_name
          }).then( (res)=> {
            this.click_allow == true;
            if(res.data.code == 404){
              this.account_name_text = '账号不存在';
              this. verifi_code_text = '';
              $('.account_name').focus();
              return;
            }else if(res.data.code == 500){
              this.$message.error('获取验证码失败！');
              return;
            }else if(res.data.code == 200){
              //this.$message.success('保存成功！');
              this.count = this.time_count;
              this.show_code = false;
              //清空错误提示
              this.account_name_text = '';
              this.verifi_code_text = '';
              this.timer = setInterval(() => {
                if (this.count > 0 && this.count <= this.time_count) {
                  this.count--;
                } else {
                  this.show_code = true;
                  clearInterval(this.timer);
                  this.timer = null;
                  this.get_code_text = '再次获取';
                }
              }, 1000);
            }
          });
        }
      },
      save:function(){
        let regCheckPwd = /^(?![0-9]+$)(?![a-zA-Z]+$)(?!([^(0-9a-zA-Z)]|[\(\)])+$)([^(0-9a-zA-Z)]|[\(\)]|[a-zA-Z]|[0-9]){8,20}$/;
        if(this.pwd_code == ''){
          this.pwd_new_text = '请输入密码';
          this.pwd_code_text = '';
          $('.pwd_code').focus();
          return;
        }else if(!regCheckPwd.test(this.pwd_code)){
          this.pwd_new_text = '密码格式不正确';
          this.pwd_code_text = '';
          $('.pwd_code').focus();
          return;
        }else if(this.pwd_code_check == ''){
          this.pwd_new_text = '';
          this.pwd_code_text = '请再次输入密码';
          $('.pwd_code_check').focus();
          return;
        }else if(this.pwd_code_check != this.pwd_code){
          this.pwd_new_text = '';
          this.pwd_code_text = '两次输入密码不一致';
          $('.pwd_code_check').focus();
          return;
        }
        let md5_password = this.$crypto.createHash("md5").update(this.pwd_code_check).digest('hex');
        this.$axios({
          url: this.$API.infoManage.resetPwd,
          method: 'post',
          data: {
            'loginName':this.account_name,
            'authCode':this.verifi_code,
            'password':md5_password
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
            this.$message.error('密码设置失败！');
          }else if(res.data.code == 200){
            this.$message.success('密码设置成功！');
            this.$router.push({path: '/login'});
          }
        });
      },
      modify_pwd:function(){
      }
    }
  }
</script>
<style scoped>
  #inputVerifi{width:302px;}
  .reset_pwd_content{margin:0 20px;height:600px;}
  .reset_pwd_title_content{padding:60px 20px 0 0;}
  .reset_pwd_label_ul{margin-top:70px;}
  .reset_pwd_btn_div{margin: 80px 0 20px;}
</style>