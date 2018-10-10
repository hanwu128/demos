<template>
  <div>
    <div class="nothing_cont">
      <i class="nothing_img"></i>
      <p>暂无该页面的访问权限</p>
      <span class="back_login btn_color" @click="backLogin">返回登录>></span>
    </div>
  </div>
</template>
<script>
  import timerComponent from './common/timer.vue'
  export default {
    name: 'company_info',
    data () {
      return {
       
      }
    },
    methods: {
      backLogin:function(){
        window.localStorage.clear();
        this.$router.push({path: '/login'});
      }
    }
  }
</script>
<style scoped>
.nothing_img{
  display: inline-block;
  width: 230px;
  height:120px;
  background: url(../assets/images/nothing.png) no-repeat;
  background-position: center center;
}
.nothing_cont {
  width: 230px;
  height: 240px;
  position: absolute;
  left: 50%;
  top: 50%;
  margin-left: -115px;
  margin-top: -180px;
}
.nothing_cont p {
  text-align: center;
  font: 18px/18px 'Microsoft YaHei';
  margin:36px 0;
  color: #999;
}
.back_login{
  width: 80px;
  height: 20px;
  margin-left: 75px;
}
</style>