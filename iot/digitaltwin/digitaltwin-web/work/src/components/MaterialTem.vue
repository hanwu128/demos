<template>
  <div class="main_cont" :class="{'activelist':hidden}">
    <div class="current_place clearfix">
      <div class="icon_dw fl"></div>
      <div class="fl">
        <!-- <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item style="color:#777;">你当前所在位置：</el-breadcrumb-item>
        <el-breadcrumb-item style="color:#333;">数字镜像</el-breadcrumb-item>
        <el-breadcrumb-item>物模板</el-breadcrumb-item>
      </el-breadcrumb> -->
        <div class="common-breadcrumb">    
            <span><span class="positionImg"></span>{{ $t('common.location') }}</span>    
            <a ><strong> {{ $t('common.navh3') }}</strong> > </a>    <span class="first-level">{{ $t('common.template') }}</span> 
         </div>
      </div>
    </div>
    <div class="temListCont">
      <h1>{{ $t('common.templateList') }}</h1>
      <div class="temCont">
        <div class="condition clearfix">
           <el-row class="addTem">
            <el-button size="mini" class="add_icon" @click="createClick(1)"><em></em>{{ $t('common.addTemplate') }}</el-button>
          </el-row>
          <div class="searchTem">
            <input :placeholder="$t('common.templateName')" id="search_input" class="el-input__inner"  v-model="state.input" @input="keyUpdataDevices()" />
            <i class="el-icon-circle-close-outline" @click="clearFn()"></i>
          </div>
            
        </div>
        <ul class="temListUl clearfix">
          <li class="fl" v-for="temlist in state.temListcont">
            <a @click="createClick(2,temlist.id)">
              <h2 class="listname">{{temlist.name}}</h2>
              <p class="pBottom"><span v-show="temlist.desp!==''">{{ $t('common.description') }}</span><span>{{temlist.desp}}</span></p>
              <div style="margin:15px 85px;"><em class="icon_em"></em></div>
              <p style="padding-bottom:0;"><span>{{ $t('common.statusNumber') }}</span><span>{{temlist.attrnum}}</span>{{ $t('pages.Number') }}</p>
            </a>
              <i class="icon-delete fr" @click="deleteFn(temlist.id,temlist.istnum)" style=""></i>
          </li>
           <li class="fl">
            <a @click="createClick(1)" class="addicon metericon">
              <i></i>
              <p class="add_p">{{ $t('common.addTemplate') }}</p>
            </a>
          </li>
         
          <el-button :plain="true" style="display:none;"></el-button>
        </ul>
        <div style="padding:20px;" class="clearfix">
        <div style="float:right">
          <!-- <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="state.currentpage" :total="state.total_num" :page-sizes="[7, 11, 15]" :page-size="state.rowcount"  layout="sizes, prev, pager, next" style="float:left;"></el-pagination>
          <p class="total">{{$t('pages.total')}} {{state.pageTotal}} {{$t('pages.page')}} </p>
          <div class="go_num">
            <span>{{$t('pages.to')}}</span><input type="" v-model="input_num"><span>{{$t('pages.page')}}</span>
          </div>
          <a class="go" @click="searchPage()">GO</a> -->
           <pagination :total="state.total_num"
                    :current ='state.currentpage'
                    :pageSize ='state.rowcount'
                     @pagesizechange="handleSizeChange"
                    @pagechange="handleCurrentChange"
                   ></pagination>

          </div>
          </div>
      </div>
    </div>

  <el-dialog :title="$t('common.deleteOk')" :visible.sync="shadowIsShow" width="400px" :close-on-click-modal="false" :close-on-press-escape="false">
    <div class="addTemSetDialog">
        <i class="el-icon-question icon_th" ></i>
        <div class="TA">{{$t("message.sureDelectTemplate")}}</div>
        <div class="bt_icon" style="margin:20px 65px;">
           <el-button size="mini"  type="primary" @click="sureFn()">{{$t("common.yes")}}</el-button>
            <el-button size="mini" @click="cancelFn()">{{$t("common.no")}}</el-button>
        </div>     
    </div>
  </el-dialog>

  <el-dialog :title="$t('common.deleteOk')" :visible.sync="shadowIsShowIsnum" width="400px" :close-on-click-modal="false" :close-on-press-escape="false">
    <div class="addTemSetDialog">
        <i class="el-icon-warning icon_th" ></i>
        <div class="TA">{{$t("message.noDelect")}}</div>
        <div class="bt_icon" style="margin:20px 100px;">
           <el-button size="mini"  type="primary" @click="cancelFn()" >{{$t("common.yes")}}</el-button>
        </div>     
    </div>
  </el-dialog>

  </div>
</template>
<script>
  import {mapGetters, mapActions} from 'vuex'
  import pagination from '@/components/pagenum'
 export default {
        computed: mapGetters(['state']),
        components: {pagination},
        methods: {
          ...mapActions(['temlistFn']),
         createClick:function(status,id){
            if(status==1){
              if(this.hidden){
                this.$router.push({path:'/addTem?status='+status +'&hidden=hidden'});
              }else{
                this.$router.push({path:'/addTem?status='+status});
              }
              
            }else{
              if(this.hidden){
                 this.$router.push({path:'/addTem?status='+status + '&id=' +id + '&hidden=hidden'});
               }else{
                 this.$router.push({path:'/addTem?status='+status + '&id=' +id});
               }
            }
          },
          deleteFn:function(id,istnum){
            if(istnum>0){
              this.shadowIsShowIsnum=true;
            }else{
            this.shadowIsShow = true;
            }
            this.id = id;
            this.istnum = istnum;

          },
          sureFn:function(){
            var _this = this;
            this.$axios.post(this.$API.temmodel.deletetem,{
                ids:this.id
            }).then((res)=>{
                if(res.data.code==200){
                  this.shadowIsShow=false;
                  this.$message({
                    message: _this.$t('message.delOk'),
                    type: 'success'
                  });
                _this.temlistFn(_this);
                  
                }else{
                  this.$message.error(_this.$t('message.delNo'));
                }
               
            });
          },
          cancelFn:function(){
            this.shadowIsShow=false;
            this.shadowIsShowIsnum=false;
          },
          ishsow:function(){
              // this.search_show= false;
          },
          searchPage:function(){
            this.state.currentpage=Number(this.input_num);
            this.$axios.post(this.$API.temmodel.temList,{
                page:Number(this.input_num),
                offset:this.state.rowcount,
                name:document.getElementById('search_input').value
            }).then((res)=>{
              // this.colListcont = res.data.data.rows; 
               this.state.temListcont = res.data.data.rows; 
               this.state.total_num = res.data.data.total; 
               this.state.pageTotal = res.data.data.pageTotal;      
            }); 
          },
           keyUpdataDevices:function(){
              this.$axios.post(this.$API.temmodel.temList,{
                page:1,
                offset:this.state.rowcount,
                name:document.getElementById('search_input').value
            }).then((res)=>{
              // this.colListcont = res.data.data.rows; 
               this.state.temListcont = res.data.data.rows; 
               this.state.total_num = res.data.data.total;   
               this.state.pageTotal = res.data.data.pageTotal;    
            }); 
          },
          clearFn:function(){
            this.$axios.post(this.$API.temmodel.temList,{
                page:1,
                offset:this.state.rowcount,
                name:''
            }).then((res)=>{
               // this.search_show= true;
               // document.getElementById('search_input').blur();
               this.state.temListcont = res.data.data.rows; 
               this.state.total_num = res.data.data.total;  
               this.state.pageTotal = res.data.data.pageTotal;
            }); 
            document.getElementById('search_input').value = '';
            this.state.input='';
          },
          handleSizeChange:function(val){
            var _this =this;
            this.state.rowcount = val;
            this.temlistFn(_this);
          },
          handleCurrentChange:function(val){
            var _this =this;
            this.state.currentpage = val;
            this.temlistFn(_this);
          },
        }, 
        data(){
          return{
            currentPage2: 1,
            shadowIsShow:false,
            id:'',
            hidden:null,
            // search_show:true,
            istnum:'',
            input_num:'',
            shadowIsShowIsnum:false
           
          }
        },
        created:function () {
            if(location.hash.split('?')[1]){
              this.hidden = location.hash.split('?')[1].split('=')[1]=="hidden";
            }else{
              this.hidden=false;

            }
                this.state.input='';
                // this.keyUpdataDevices();
               var _this =this;
               this.temlistFn(_this);
                localStorage.setItem('parentUrl', window.location.href);
                var href = {
                    parentUrl:window.location.href,
                    childrenUrl:''
                }
               window.parent.postMessage(href,"*");
              this.$axios.post(this.$API.temmodel.temList,{
                page:1,
                offset:this.state.rowcount,
                name:''
            }).then((res)=>{
               // this.search_show= true;
               // document.getElementById('search_input').blur();
               this.state.temListcont = res.data.data.rows; 
               this.state.total_num = res.data.data.total;  
               this.state.pageTotal = res.data.data.pageTotal;
               this.state.currentpage = 1;
            }); 
        },
        name: ''
      }
</script>