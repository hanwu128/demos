<template>
  <div>
    <div class="page_title_content">
      <span class="page_title"><router-link to="/edge/deImage" class="btn_color"><!--数字镜像--></router-link></span>
    </div>
    <div class="right_content">
      <div class="header">
        <input class="search_text" type="text" placeholder="请输入数字镜像名称" v-model="search_data.access_key">
        <!-- <select class="search_select" v-model="search_data.edgent_version">
           <option value="">所有版本</option>
           <option v-for="(item,index) in deviceVersion" :value="item">{{ item }}</option>
         </select>-->
        <!-- <el-select v-model="search_data.edgent_version" placeholder="请选择">
          <el-option
            v-for="item in deviceVersion"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select> -->
        <span class='btn_save' @click="searchClick()">搜索</span>
        <span class="btn_create fr" @click="newDevice">创建数字镜像</span>
        <!-- <span class="btn_create fr agaent">Agent批量更新</span>-->
      </div>
      <div class="edge_manager_list">
        <!-- 表格组件开始 -->
        <el-table
          :data="deviceInfo"
          border
          empty-text=' '
          style="width: 100%;border-left: 1px solid #d6d7dd;border-top: 1px solid #d6d7dd;"
          header-row-class-name = 'header-row'
          :default-sort = "{prop: 'createtimestamp', order: 'descending'}"
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
            prop="digitaltwinname"
            label="数字镜像名称"
            sortable="custom"
          >
          </el-table-column>
          <el-table-column
            align="center"
            prop="describemessage"
            label="数字镜像描述"
          >
          </el-table-column>
          <el-table-column
            align="center"
            prop="createtimestamp"
            label="创建时间"
            sortable="custom"
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
    },
    name: 'edgeManage',
    data () {
      return {
        //列表
        deviceInfo: [],
        rowcount: 10,
        currentpage: 1,
        total_num:0,
        sort_orderby:'createtimestamp',
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
        var _this = this;
        _this.deviceInfo = [];
        _this.search_status = false;
        _this.show_loading = true;
        _this.show_no_data = true;
        //判断是否为搜索
        if(status == 1){
          _this.currentpage = 1;
        }
        _this.$axios.post(_this.$API.edgeImg.devicedetaiList,{
          'name':_this.search_data.access_key,
          'pagesize':_this.rowcount,
          'current':_this.currentpage
        }).then( (res) =>{
          if(res.data.code == 420){
          _this.$router.push({path: '/login'});
        }
        function add0(m){return m<10?'0'+m:m };
        function format(shijianchuo){
        //shijianchuo是整数，否则要parseInt转换
          var time = new Date(shijianchuo);
          var y = time.getFullYear();
          var m = time.getMonth()+1;
          var d = time.getDate();
          var h = time.getHours();
          var mm = time.getMinutes();
          var s = time.getSeconds();
          return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
        }
        for(var j=0; j<res.data.data.rows.length;j++) {
          res.data.data.rows[j].createtimestamp = format(res.data.data.rows[j].createtimestamp)
        }
        _this.deviceInfo = res.data.data.rows;
        if( _this.deviceInfo == undefined || _this.deviceInfo.length == 0){
          if(_this.search_flag==1){
            _this.search_status = true;
          }else{
            _this.search_status = false;
          }
          _this.show_loading = false;
          _this.show_no_data = true;
          _this.emptyText = '暂无数据';
        }else{
          _this.show_no_data = false;
          _this.total_num = res.data.data.total;
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
        this.$router.push({path:'/edge/insertImg',query: { deviceid: this.deviceInfo[index].id}});
      },
      deleteInfo:function (index) {
        if(this.deviceInfo[index].status == 1){
          return
        }
        this.$confirm('确定要删除数字镜像“'+ this.deviceInfo[index].digitaltwinname +'”吗？。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.post(this.$API.edgeImg.devicedetailDelete,{
          'id':this.deviceInfo[index].id
        }).then((res)=>{
          console.log(res)
          if(res.data.code == 420){
            this.$router.push({path: '/login'});
          }
        let obj = res.data;
        if(obj.code === 200) {
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
        this.$router.push({path:'/edge/insertImg'})
      },
      searchClick:function(){
        this.search_flag = 1;
        this.listFn(1);
      }
    }
  }
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
