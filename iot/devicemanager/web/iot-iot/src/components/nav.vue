<template>
  <div class="nav">
    <img src="../assets/images/logo.png" class="logo">
    <div class='nav_r' v-if='logout_show'>
      <div class='account_div'>
        <router-link to="/information/personalData">
          <i class="account_icon"></i>
          <span class="span_14">{{account_name}}</span>
        </router-link>
      </div>
      <span class='btn_logout' @click='logoutFn'>退出登录</span>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'nav',
    data () {
      return {
        account_name:'',
        logout_show:false,
      }
    },
    created: function(){
      this.logoutShowFn();
    },
    watch: {
      '$route':function(){
        this.logoutShowFn();
      }
    },
    methods:{
      logoutShowFn:function(){
        let page_path = window.location.hash;
        let module_name = page_path.split('/').pop().split('?')[0];
        if(module_name == 'login' || module_name == 'resetPassword' || module_name == 'noPermission'){
          this.logout_show = false;        
        }else{
          this.logout_show = true;  
          this.account_name = JSON.parse(window.localStorage.login_name);
        }
      },
      logoutFn:function(){
        //登出
        this.$axios.post(this.$API.login.logOut).then((res)=>{
          window.localStorage.clear();
          this.logout_show = false;
          this.$store.commit("logout");
          this.$router.push({path: '/login'});
        });
      }
    }
      
  }
</script>
<style>
  @import "../assets/common/common.css";
  @import "../assets/common/btn_style.css";
  @import "../assets/common/table_style.css";
  .logo {
    margin-top: 7px;
    margin-left:10px;
  }
  .nav_r{
    display:inline-block;
    float:right;
  }
  .account_icon{
    display: inline-block;
    cursor: pointer;
    width: 20px;
    height: 20px;
    vertical-align: middle;
    background: url(../assets/images/icon.png) no-repeat;
    background-position: 0 0;
  }
  .account_div{
    display: inline-block;
  }
</style>
