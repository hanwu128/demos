<template>
  <div>
    <div class="page_title_content">
      <span class="page_title">边缘分析&nbsp;&gt;&nbsp;<router-link to="/edge/pipeLine" class="btn_color">数据流管理</router-link>&nbsp;&gt;&nbsp;详情</span>
    </div>
    <div class="right_content">
       <!--基本信息-->
       <div class="line_infor">
       		<span class="module_title">基本信息</span>
       		<ul class="detail_basicmsg">
       			<li><label>数据流名称：</label><span>{{this.deviceDetail.streamingname}}</span></li>
       			<li><label>数据流描述：</label><span>{{this.deviceDetail.streamingdesc}}</span></li>
            <li><label>创建时间：</label><span>{{this.deviceDetail.createtimeatshow}}</span></li>
            <li><label>数据流状态：</label><span>{{this.deviceDetail.status ? '运行中':'已暂停'}}</span></li>
            <li><label>网关设备名称：</label><span>{{this.deviceDetail.accesskey}}</span></li>
            <li><label>数据处理量(条)：</label><span>{{this.dataNum}}</span><span class="btn_color" @click='detailIsShow = true;' style="display: inline;">详情>>></span></li>
       		</ul>
       </div>
       <!--处理模块信息-->
       <div class="line_infor">
          <span class="module_title">处理模块信息</span>
          <ul class="detail_basicmsg">
            <li><label>处理模块名称：</label><span>{{this.deviceDetail.appname}}</span></li>
            <li><label>处理模块描述：</label><span>{{this.deviceDetail.appdesc}}</span></li>
            <li><label>处理模块版本：</label><span>{{this.deviceDetail.appversion}}</span></li>
          </ul>
       </div>
       <!--MAP-->
       <div class="line_infor">
          <span class="module_title">MAP信息</span>
          <div class="pipe_line_detail_list">
         		<table class="list">
  	             <thead>
  		            <tr>
  		                <th>属性名称</th>
  		                <th>显示名称</th>
  		                <th>类型</th>
  		                <th>单位</th>
  		                <th>MAP名称</th>
  		            </tr>
  		         </thead>
  	            <tbody>
  		            <tr v-for="(MAP,index) in MAPGateWay">
  		                <td>{{MAP.sourcename}}</td>
  		                <td>{{MAP.mapdisplayname}}</td>
  		                <td>{{MAP.sourcedatatype}}</td>
  		                <td>{{MAP.sourceunit}}</td>
  		                <td>{{MAP.mapname}}</td>
  		            </tr>
  	            </tbody>
  	        </table>
          </div>
       </div>
          <!--MAP-->
       <div class="line_infor">
          <span class="module_title">filter列表</span>
          <div class="pipe_line_detail_list">
         		<table class="list">
  	             <thead>
  		            <tr>
  		                <th>属性</th>
  		                <th>条件</th>
  		                <th>规则</th>
  		                <th>关联</th>
  		            </tr>
  		         </thead>
  	            <tbody>
  		            <tr v-for="(ruler,index) in rulerGateWay">
  		                <td>{{ruler.attributename}}</td>
  		                <td>{{ruler.mathematicalsymbol}}</td>
  		                <td>{{ruler.rightvalue}}</td>
  		                <td>{{ruler.logicalsymbol}}</td>
  		            </tr>
  	            </tbody>
  	        </table>
          </div>
        </div>
    </div>
    <div class='btn_div'>
      <router-link to="/edge/pipeLine" class='btn_back'>返回</router-link>
    </div>
    <!-- 数据详情 -->
    <el-dialog title="数据详情" :visible.sync="detailIsShow" width="1200px">
      <div style="max-height:600px;overflow: auto;">
        <!-- 表格组件开始 -->
        <el-table
            :data="numInfo"
            border
            empty-text=' '
            style="width: 100%;border-left: 1px solid #d6d7dd;border-top: 1px solid #d6d7dd;"
            :default-sort = "{prop: 'create_stamp', order: 'descending'}"
            header-row-class-name = 'header-row'
            @sort-change="sortChangeFn"
            >
            <el-table-column
                align="center"
                type="index"
                label="编号"
                width="80"
            >
            <template slot-scope="scope">
                {{scope.$index+1+(currentpage-1)*rowcount}}
            </template>
            </el-table-column>
            <el-table-column
                align="center"
                prop="messageStr"
                label="信息名称"
            >
            </el-table-column>
            <el-table-column
              align="center"
              prop="create_stamp"
              label="时间"
              sortable="custom"
              width="200"
            >
            </el-table-column>
        </el-table>
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentpage" :page-sizes="[10,20,50,80,100]" :page-size="rowcount" :total="total_num" layout="total,sizes, prev, pager, next, jumper" style='margin-top:10px;'></el-pagination>
          <div class="no_data" style="display: block;" v-if="show_no_data"><i :class="{'active':search_status,'loading':show_loading}"></i></div>
        <!-- 表格组件结束 -->
      </div>
    </el-dialog>
  </div>
</template>
<script>
    export default {
        created:function () {
          let id = this.$route.query.id || '';
          this.$axios.post(this.$API.pipeLineManage.pipeLineDetail,{
              'id':id
          }).then( (res)=> {
            if(res.data.code == 420){
              this.$router.push({path: '/login'});
            }
            this.deviceDetail = res.data.streaming;
            this.MAPGateWay =  res.data.map;
            this.rulerGateWay = res.data.ruler[0];
            //数据发送量
            this.$axios.get(this.$API.pipeLineManage.pipeLineDataNum+'?app_name='+this.deviceDetail.appname+'&device_id='+this.deviceDetail.deviceidcode).then( (res)=> {
              if(res.data.code == 420){
                this.$router.push({path: '/login'});
              }
              this.dataNum = res.data[0].transmitted_valid;
            });
          });
        },
        name: 'detail',
        data () {
            return {
                gateWayisShow:false,
                deviceDetail:{},
                MAPGateWay:[],
                rulerGateWay:{},
                dataNum:'',
                // 暂无数据开始
                detailIsShow:false,
                numInfo:[],
                emptyText:' ',
                show_no_data:true,
                search_status:false,
                show_loading:true,
                rowcount: 10,
                currentpage: 1,
                total_num:0,
                sort_orderby:'create_stamp',
                sort_rule:'descending'
                // 暂无数据结束
            }
        },
        methods:{
          listFn:function(status){
            this.numInfo = [];
            this.search_status = false;
            this.show_loading = true;
            this.show_no_data = true;
            this.$axios.post(this.$API.pipeLineManage.pipeLineDataNumDetail,{
                'device_id':this.deviceDetail.deviceidcode,
                'app_name':this.deviceDetail.appname,
                'pagesize':this.rowcount,
                'current':this.currentpage,
                'sort_orderby':this.sort_orderby,
                'sort_rule':this.sort_rule
            }).then( (res)=> {
                if(res.data.code == 420){
                    this.$router.push({path: '/login'});
                }
                if(res.data.rows.length == 0){
                  this.show_loading = false;
                  this.show_no_data = true;
                  this.emptyText = '暂无数据';
                }else{
                  this.show_no_data = false;
                  this.total_num = res.data.total;
                  this.numInfo = res.data.rows;
                  for(let i=0;i<this.numInfo.length;i++){
                    this.numInfo[i].messageStr = JSON.stringify(this.numInfo[i].message);
                  }
                }
            });
          },
          handleSizeChange(val) {
              this.rowcount = val;
              this.listFn();
          },
          handleCurrentChange(val) {
              this.currentpage = val;
              this.listFn();
          },
          sortChangeFn:function(res){
              //{ column, prop, order }
              this.sort_orderby = res.prop;
              this.sort_rule = res.order;
              this.listFn();
          },
          updateInfo:function (index) {
              
          },
          detailInfo:function (index) {
              
          },
          gateWayKey:function () {
              this.gateWayisShow = !this.gateWayisShow
          }
        }
    }
</script>

<style>
	.detailBg h3{
		font-size:14px;
		padding-left:20px;
		border-left:2px solid #3000FF;
	}
	.line_infor{
		margin-bottom:20px;
	}
  .manager_title {
      cursor:pointer;
  }
  .detail_basicmsg{
    margin:0 25px;
  }
  .pipe_line_detail_list{
    margin:0 20px;
  }
</style>
