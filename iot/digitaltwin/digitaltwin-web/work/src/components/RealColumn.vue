<template>
  <div class="main_cont" :class="{'activelist':hidden}">
    <div class="current_place clearfix">
    <div class="icon_dw fl"></div>
      <div class="fl">
       <!--  <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item>你当前所在位置：</el-breadcrumb-item>
        <el-breadcrumb-item>数字镜像</el-breadcrumb-item>
        <el-breadcrumb-item>物实例</el-breadcrumb-item>
      </el-breadcrumb> -->

        <div class="common-breadcrumb">    
            <span><span class="positionImg"></span>{{ $t('common.location') }}</span>    
            <a ><strong>  {{ $t('common.navh3') }}</strong> > </a>    <span class="first-level">{{ $t('common.example')}}</span> 
         </div>

      </div>
    </div>
    <div class="temListCont">
      <h1>{{$t('common.exampleList')}}</h1>
      <div class="temCont">
        <div class="condition clearfix">
           <el-row class="addTem">
            <el-button size="mini" class="add_icon" @click="createClick(1)"><em></em>{{$t('common.addExample')}}</el-button>
          </el-row>
          <div class="searchTem">
            <!-- <el-input placeholder="请输入内容" v-model="input" @change="search(input)" @focus="ishsow()" clearable @clear="clearFn()" style="width:200px;">
            </el-input>
            <i class="el-icon-search" v-if="search_show"></i> -->
            <input :placeholder="$t('common.wuExampleName')" id="search_input" class="el-input__inner"  v-model="input" @input="search()" />
            <i class="el-icon-circle-close-outline" @click="clearFn()"></i>
          </div>
        </div>
        <ul class="temListUl clearfix">
          <li class="fl" v-for="temlist in colListcont">
            <a @click="createClick(2,temlist.id)">
              <h2  class="listname">{{temlist.name}}</h2>
              <!-- <strong :class="{'active':temlist.state=='1'}" >{{temlist.state ? '在线':'离线'}} </strong> -->
              <p class="pBottom"><span  v-show="temlist.desp!==''">{{$t('common.description')}}</span><span>{{temlist.desp}}</span></p>
              <div style="margin:15px 85px;"><em class="icon_em expm_em"></em></div>
              <p style="padding:0;margin-bottom:16px;background:#efefef;border:1px solid #ddd;"><span>{{$t('common.statusNumber')}}</span><span>{{temlist.attrnum}}</span>{{ $t('pages.Number') }}</p>
              <p class="temName" style="border-bottom:1px solid #ddd;"><span>{{$t('common.belongTem')}}</span><span>{{temlist.tplname}}</span></p>
              <p class="temName" style="margin-top:18px;padding-bottom:0;"><span>{{$t('common.updata')}}</span><span>{{temlist.mtime}}</span></p>
              </a>
              <i class="icon-delete fr" @click="deleteFn(temlist.id)"></i>
          </li>
           <li class="fl">
            <a @click="createClick(1)" class="addicon expmicon">
              <i></i>
              <p  class="add_p" style="margin-bottom:76px;">{{$t('common.addExample')}}</p>
            </a>
          </li>
          <el-button :plain="true" style="display:none;"></el-button>
        </ul>
        <div style="padding:20px;" class="clearfix">
        <div style="float:right;">
         <!--  <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentpage" :page-sizes="[7, 11, 15]" :total="total_num" :page-size="rowcount"  layout="sizes, prev, pager, next" style="float:left;"></el-pagination>
          <p class="total">{{$t('pages.total')}} {{pageTotal}}{{$t('pages.page')}}</p>
          <div class="go_num">
            <span>{{$t('pages.to')}}</span><input type="" v-model="input_num"><span>{{$t('pages.page')}}</span>
          </div>
          <a class="go" @click="searchPage()">GO</a> -->
          <pagination :total="total_num"
                    :current ='currentpage'
                    :pageSize ='rowcount'
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
        <div class="TA">{{$t('message.sureDelectExample')}}</div>
        <div class="bt_icon"  style="margin:20px 65px;">
           <el-button size="mini"  type="primary" @click="sureFn()">{{$t("common.yes")}}</el-button>
            <el-button size="mini" @click="cancelFn()">{{$t("common.no")}}</el-button>
        </div>     
    </div>
  </el-dialog>

  </div>
</template>
<script>
import pagination from '@/components/pagenum'
 export default {
  components: {pagination},
        methods: {
         createClick:function(status,id){
            if(status==1){
              if(this.hidden){
                this.$router.push({path:'/addExample?status='+status +'&hidden=hidden'});
              }else{
                this.$router.push({path:'/addExample?status='+status});
              }
            }else{
              if(this.hidden){
                 this.$router.push({path:'/addExample?status='+status + '&id=' +id + '&hidden=hidden'});
               }else{
                 this.$router.push({path:'/addExample?status='+status + '&id=' +id});
               }
            }
          },
          deleteFn:function(id){
            var that = this;
            this.id = id;
            this.shadowIsShow=true;
          },
          searchPage:function(){
            this.currentpage=Number(this.input_num);
            this.$axios.post(this.$API.RealColumn.colList,{
                page:Number(this.input_num),
                offset:this.rowcount,
                name:document.getElementById('search_input').value
            }).then((res)=>{
              this.colListcont = res.data.data.rows;        
              this.total_num = res.data.data.total;
              this.pageTotal  = res.data.data.pageTotal;
              function FormatDate (strTime) {
                    var date = new Date(strTime);
                  return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate() +" "+date.getHours() +":"+date.getMinutes();
                }
                for(var i=0; i<this.colListcont.length; i++){
                   this.colListcont[i].mtime = FormatDate(this.colListcont[i].mtime);

                }

            }); 
          },
          sureFn:function(){
            var _this =this;
             this.$axios.post(this.$API.RealColumn.deleteExp,{
                ids:this.id
            }).then((res)=>{
                if(res.data.code==200){
                  this.shadowIsShow=false;
                  this.$message({
                    message: _this.$t('message.delOk'),
                    type: 'success'
                  });
                  this.colListFn();
                }else if(res.data.code==510){
                  this.shadowIsShow=false;
                  this.$message.error(_this.$t('message.BeingUser'));
                }else{
                  this.shadowIsShow=false;
                  this.$message.error(_this.$t('message.delNo'));
                }
               
            });
          },
          cancelFn:function(){
            this.shadowIsShow=false;
          },
          ishsow:function(){
              this.search_show= false;
          },
          colListFn:function(){
            this.$axios.post(this.$API.RealColumn.colList,{
                page:this.currentpage,
                offset:this.rowcount,
                name: this.input
            }).then((res)=>{
              this.colListcont = res.data.data.rows;        
              this.total_num = res.data.data.total;
              this.pageTotal = res.data.data.pageTotal;

              if(this.colListcont==''){
              this.$axios.post(this.$API.RealColumn.colList,{
                  page:1,
                  offset:this.rowcount,
                  name:document.getElementById('search_input').value
              }).then((res)=>{
                this.colListcont = res.data.data.rows;  
                this.total_num = res.data.data.total;
                this.pageTotal = res.data.data.pageTotal;    
              });
            }

            
              function FormatDate (inputTime) {
                  var date = new Date(inputTime);
                  var y = date.getFullYear();  
                  var m = date.getMonth() + 1;  
                  m = m < 10 ? ('0' + m) : m;  
                  var d = date.getDate();  
                  d = d < 10 ? ('0' + d) : d;  
                  var h = date.getHours();
                  h = h < 10 ? ('0' + h) : h;
                  var minute = date.getMinutes();
                  var second = date.getSeconds();
                  minute = minute < 10 ? ('0' + minute) : minute;  
                  second = second < 10 ? ('0' + second) : second; 
                  return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second; 
                }
                for(var i=0; i<this.colListcont.length; i++){
                   this.colListcont[i].mtime = FormatDate(this.colListcont[i].mtime);

                }

            }); 
          },
          search:function(){
              this.$axios.post(this.$API.RealColumn.colList,{
                page:1,
                offset:this.rowcount,
                name:document.getElementById('search_input').value
            }).then((res)=>{
              this.colListcont = res.data.data.rows;  
              this.total_num = res.data.data.total;
              this.pageTotal = res.data.data.pageTotal;    
            }); 
          },
          handleSizeChange:function(val){
            this.rowcount = val;
            this.colListFn();
          },
          handleCurrentChange:function(val){
            this.currentpage = val;
            this.colListFn();
          },
          clearFn:function(){
            this.$axios.post(this.$API.RealColumn.colList,{
                page:1,
                offset:this.rowcount,
                name:''
            }).then((res)=>{
              this.search_show=true;
              document.getElementById('search_input').value='';
              this.colListcont = res.data.data.rows;   
              this.total_num = res.data.data.total;
              this.pageTotal = res.data.data.pageTotal;
              this.input='';       
            }); 
          }
        }, 
        data(){
          return{
              colListcont:'',
              input:'',
              id:'',
              shadowIsShow:false,
              hidden:null,
              search_show:true,
              total_num:0,
              pageTotal:0,
              rowcount:7,
              currentpage:1,
              input_num:''
          }
        },
        created:function () {
           if(location.hash.split('?')[1]){
              this.hidden = location.hash.split('?')[1].split('=')[1]=="hidden";
            }else{
              this.hidden=false;
            }
            this.colListFn();
            this.input='';
            // this.search();
            localStorage.setItem('parentUrlR', window.location.href);
             var href = {
                    parentUrl:window.location.href,
                    childrenUrl:''
                }
               window.parent.postMessage(href,"*");
        },
      }
</script>