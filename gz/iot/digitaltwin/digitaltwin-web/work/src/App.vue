<template>
  <div id="app" class="clearfix">
    <div class="nav_top" v-show="hidden">
        <img src="./assets/images/logo.png" class="logo">
     </div>
    <nav-category v-show="hidden"></nav-category>
    <router-view></router-view>
  </div>
</template>

<script>
  import Nav from './components/nav.vue'
  
  export default {
    name: 'app',
    data(){
      return{
        hidden:null
      }
    },
    components: {
      navCategory : Nav
    },
    created:function(){
      
      if(location.hash.split('?')[1]){
              this.hidden = location.hash.split('?')[1].split('=')[location.hash.split('?')[1].split('=').length-1]=="hidden";
            }else{
              this.hidden=false;
            }
    }
  }
</script>


