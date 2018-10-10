<template>
  <div>
    <div class="page_title_content">
      <span class="page_title">信息管理&nbsp;&gt;&nbsp;<router-link to="/information/personalData" class="btn_color">个人资料</router-link>&nbsp;&gt;&nbsp;修改密码</span>
    </div>
    <div class="right_content">
      <ul class="label_ul" v-if='!btn_show'>
        <li>
          <label class="label_title" style="width: 80px;">账号：</label><span  class="li_content"><input type="text" class="inputText" :class="{'input_readoly':company_setting_show}" v-model="account_name" maxlength="40" :readonly='company_setting_show'></span>
        </li>
        <li>
          <label class="label_title" style="width: 80px;">验证码：</label><span  class="li_content"><input type="text" placeholder="请输入验证码" class="inputText" id="inputVerifi" :class="{'input_readoly':!company_setting_show}" v-model="verifi_code" maxlength="20" :readonly='!company_setting_show'>
          <a class="btn_save" v-show="show_code" @click='getCodeFn'>{{get_code_text}}</a>
          <a v-show="!show_code" class="btn_save disabled">{{count}} s</a>
          </span>
          <p class='tip_text' style='margin-left:136px;'>{{verifi_code_text}}</p>
        </li>
      </ul>
      <ul class="label_ul" v-if='btn_show'>
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
    </div>
    <div class="btn_div">
      <span class="btn_save" @click="nextFn" v-if='!btn_show'>下一步</span>
      <span class="btn_save" @click="save" v-if='btn_show'>确定</span>
    </div>
  </div>
</template>
<script>
  import timerComponent from '../common/timer.vue'
  export default {
    name: 'company_info',
    data () {
      return {
        //表单内容
        account_name:'',
        verifi_code:'',
        //提示内容
        verifi_code_text:'',
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
        click_allow:true
      }
    },
    components: {
      timerComponent
    },
    created: function(){
      this.infoFn();
    },
    methods: {
      infoFn:function(){
        this.account_name = JSON.parse(window.localStorage.login_name) || '';
      },
      nextFn:function(){
        if(this.verifi_code == ''){
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
          if(res.data.code == 420){
            this.$router.push({path: '/login'});
          }else if(res.data.code == 418){
            this.verifi_code_text = '验证码不正确';
            return;
          }else if(res.data.code == 200){
            this.btn_show = true;
          }
        });
      },
      callbackTimer:function(){
      },
      getCodeFn:function(){
        if (!this.timer) {
          if(this.click_allow == false){
            return;
          }
          this.$axios.post(this.$API.infoManage.getCode,{
            'loginName':this.account_name
          }).then( (res)=> {
            this.click_allow = true;
            if(res.data.code == 404){
              this.$router.push({path: '/login'});
            }else if(res.data.code == 500){
              this.$message.error('获取验证码失败！');
              this.infoFn();
            }else if(res.data.code == 200){
              //this.$message.success('保存成功！');
              //清空错误提示
              this.verifi_code_text = '';
              this.count = this.time_count;
              this.show_code = false;
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
            this.$message.error('修改失败！');
          }else if(res.data.code == 200){
            this.$message.success('修改成功！');
            this.$router.push({path: '/information/personalData'});
          }
        });
      },
      modify_pwd:function(){
      }
    }
  }
</script>
<style>
  #inputVerifi{width:302px;}
</style>