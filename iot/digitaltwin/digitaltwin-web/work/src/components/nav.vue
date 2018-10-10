<template>
   
    <div class="nav_left_list">
      <ul>
        <li class="li2">
               <label class="operate_nav_a"><i></i><span>{{ $t('common.navh3') }}</span><strong></strong></label>
               <el-collapse-transition>
                   <div class="operate_dd">
                       <dl>
                           <dd class="dd1" :class="{'active':module_name=='MaterialTem'}" @click="linkmater()"><em>●</em>{{ $t('common.template') }}</dd>
                           <dd class="dd2" :class="{'active':module_name=='RealColumn'}"  @click="linkexp()"><em>●</em>{{ $t('common.example') }}</dd>
                       </dl>
                   </div>
               </el-collapse-transition>
          </li>
      </ul>
    </div>
</template>
<style>
  @import "../assets/css/nav.css";
  @import "../assets/css/common.css";
</style>
<script type="text/javascript">
    export default {
    name: '',
    data(){
      return{
        hidden:null,
        module_name:''
      }
    },
    methods:{
      linkmater(){
        // 
        if(this.hidden){
          this.$router.push({path:'/MaterialTem?hidden=hidden'});
        }else{
          this.$router.push({path:'/MaterialTem'});
        }
        this.module_name = location.hash.split('/').pop().split('?')[0];
      },
      linkexp(){
        // 
        if(this.hidden){
          this.$router.push({path:'/RealColumn?hidden=hidden'});
        }else{
          this.$router.push({path:'/RealColumn'});
        }
        this.module_name = location.hash.split('/').pop().split('?')[0];
      }
    },
    created:function(){
      if(location.hash.split('?')[1]){
              this.hidden = location.hash.split('?')[1].split('=')[location.hash.split('?')[1].split('=').length-1]=="hidden";
            }else{
              this.hidden=false;
            }
      
      this.module_name = location.hash.split('/').pop().split('?')[0];
    }
  }
</script>
