<template>
  <div>
    <div class="page_title_content">
      <span class="page_title">边缘分析&nbsp;&gt;&nbsp;<router-link to="/edge/manager" class="btn_color">设备管理</router-link></span>
    </div>
    <div class="right_content">
      <div class="header">
        <input class="search_text" type="text" placeholder="请输入设备名称" v-model="search_data.access_key">
        <!-- <select class="search_select" v-model="search_data.edgent_version">
          <option value="">所有版本</option>
          <option v-for="(item,index) in deviceVersion" :value="item">{{ item }}</option>
        </select> -->
        <!-- <el-select v-model="search_data.edgent_version" placeholder="请选择">
          <el-option
            v-for="item in deviceVersion"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select> -->
        <span class='btn_save' @click="searchClick()">搜索</span>
        <span class="btn_create fr" @click="newDevice">创建设备</span>
        <!-- <span class="btn_create fr agaent">Agent批量更新</span> -->
      </div>
      <div class="edge_manager_list">
        <!-- 表格组件开始 -->
        <el-table
          :data="deviceInfo"
          border
          empty-text=' '
          style="width: 100%;border-left: 1px solid #d6d7dd;border-top: 1px solid #d6d7dd;"
          header-row-class-name = 'header-row'
          :default-sort = "{prop: 'create_stamp', order: 'descending'}"
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
              prop="access_key"
              label="设备名称"
              sortable="custom"
          >
          </el-table-column>
          <el-table-column
              align="center"
              prop="device_desc"
              label="设备描述"
            >
          </el-table-column>
          <el-table-column
              align="center"
              prop="status"
              width="100"
              label="状态"
          >
          <template slot-scope="scope">
              {{scope.row.status==1?'在线':'离线'}}
          </template>
          </el-table-column>
          <!-- <el-table-column
              align="center"
              prop="edgent_version"
              width="100"
              label="Agent版本"
          >
          </el-table-column> -->
          <el-table-column
              align="center"
              prop="create_stamp"
              sortable="custom"
              label="创建时间"
          >
          </el-table-column>
          <el-table-column
              align="center"
              label="操作"
          >
            <template slot-scope="scope">
                <span class="btn_color" @click="detailInfo(scope.$index)">详情</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span class="btn_color"  :class="{'disabled':scope.row.status == 1 ? true : false}"  @click="deleteInfo(scope.$index)">删除</span>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentpage" :page-sizes="[10,20,50,80,100]" :page-size="rowcount" :total="total_num" layout="total,sizes, prev, pager, next, jumper" style='margin-top:10px;'></el-pagination>
        <div class="no_data" style="display: block;" v-if="show_no_data"><i :class="{'active':search_status,'loading':show_loading}"></i></div>
        <!-- 表格组件结束 -->
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    created:function () {
      //this.listFn();
      this.$axios.get(this.$API.edgeManage.deviceGetVersion).then( (res)=>{
        if(res.data.code == 420){
          this.$router.push({path: '/login'});
        }
        this.deviceVersion = res.data;
        // this.deviceVersion=[];
        // res.data.forEach((item,index)=>{
        //   let obj = {};
        //   obj.label = item;
        //   obj.value = item;
        //   this.deviceVersion.push(obj);
        // });
      });
    },
    name: 'edgeManage',
    data () {
      return {
        //列表
        deviceInfo: [],
        rowcount: 10,
        currentpage: 1,
        total_num:0,
        sort_orderby:'create_stamp',
        sort_rule:'descending',
        search_data:{
          access_key:"",
          edgent_version:""
        },
        // 暂无数据开始
        show_no_data:true,
        search_status:false,
        show_loading:true,
        // 暂无数据结束
        search_flag:0,
        deviceVersion:[]
      }
    },
    methods: {
      listFn(status){
        this.deviceInfo = [];
        this.search_status = false;
        this.show_loading = true;
        this.show_no_data = true;
        //判断是否为搜索
        if(status == 1){
          this.currentpage = 1;
        }
        this.$axios.post(this.$API.edgeManage.deviceList,{
          'edgent_version':this.search_data.edgent_version,
          'access_key':this.search_data.access_key,
          'pagesize':this.rowcount,
          'current':this.currentpage,
          'sort_orderby':this.sort_orderby,
          'sort_rule':this.sort_rule
        }).then( (res) =>{
          if(res.data.code == 420){
            this.$router.push({path: '/login'});
          }
          this.deviceInfo = res.data.rows;
          if( this.deviceInfo == undefined || this.deviceInfo.length == 0){
            if(this.search_flag==1){
              this.search_status = true;
            }else{
              this.search_status = false;
            }
            this.show_loading = false;
            this.show_no_data = true;
            this.emptyText = '暂无数据';
          }else{
            this.show_no_data = false;
            this.total_num = res.data.total;
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
      detailInfo:function (index) {
        this.$router.push({path:'/edge/detail',query: { deviceid: this.deviceInfo[index].device_id}});
      },
      deleteInfo:function (index) {
        if(this.deviceInfo[index].status == 1){
          return
        }
        this.$confirm('确定要删除设备“'+ this.deviceInfo[index].access_key +'”吗？删除后设备将无法接入系统。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.post(this.$API.edgeManage.deviceDelete,{
            'device_id':this.deviceInfo[index].device_id
          }).then((res)=>{
            if(res.data.code == 420){
              this.$router.push({path: '/login'});
            }
            let obj = res.data;
            if(obj.result == true) {
              this.deviceInfo.splice(index,1);
              this.total_num = this.total_num-1;
              this.listFn();
            }else {
              this.$message.error('删除失败!');
            }
          });
        }).catch(() => {
           return
        });
      },
      newDevice:function () {
        this.$router.push({path:'/edge/insert'})
      },
      searchClick:function(){
        this.search_flag = 1;
        this.listFn(1);
      }
    }
  }
  //扩展数组
  //替代函数的apply方法
</script>
<style scoped>
  .edge_manager_list {
    margin:0 20px 20px 20px;
  }
  .searchSelect {
    margin-left:20px;
  }
  .agaent {
    margin-right:20px;
  }
</style>
