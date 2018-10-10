<template>
<div class="page">
  <!-- <span class="pop-span" v-show="total>=200">{{$t('moment')}}</span>
  <span class="pop-span" v-show="total<200">{{$t('Atotal')}}  {{total}} {{$t('records')}}</span> -->
  <div class="right">
    <div class="select-s">
         <select v-model="v" name="label" @change="selectIpt(v)" style="float: right;min-width: 100px;height: 30px;padding-left: 3px;font-size: 15px;">
           <option
             :value="item.pageSize"
             :label="(item.label+$t('pages.strip')+'/'+$t('pages.page'))"
             :key="item.pageSize"
             v-for="item in options"></option>
      </select>
    </div>
    <div class="navtable">
      <ul class="pagination">
        <li :class="{'disabled': current === 1}"><a href="javascript:;" @click="setCurrent(1)"> « </a></li>

        <li :class="{'disabled': current === 1}"><a href="javascript:;" @click="setCurrent(current - 1)"> ‹ </a></li>
        <li v-for="p in grouplist" :class="{'active': current == p.val}"><a href="javascript:;"
                                                                            @click="setCurrent(p.val)"> {{ p.text }} </a>
        </li>
        <li :class="{'disabled': current === page}"><a href="javascript:;" @click="setCurrent(current + 1)"> › </a></li>
        <li :class="{'disabled': current === page}"><a href="javascript:;" @click="setCurrent(page)"> » </a></li>

      </ul>
    </div>
    <div class="locapage">
      <span>{{$t('pages.total')}} {{page}} {{$t('pages.page')}}</span>&nbsp;&nbsp;&nbsp;
      <span>{{$t('pages.to')}}</span><input size="mini" class="pageNum" type="number" @keyup.13="setCurrent(currents)" v-model="currents" min="1" :max="page"><span>{{$t('pages.page')}}</span>
      &nbsp;&nbsp;<el-button size="mini" type="primary" @click="setCurrent(currents)">Go</el-button>
    </div>
  </div>
</div>
</template>

<script type="text/ecmascript-6">
  export default{
    data(){
      return {
        v: 7,
        options: [{
          label: '7',
          pageSize: 7
        }, {
          label: '11',
          pageSize: 11
        }, {
          label: '15',
          pageSize: 15
        }],
        currents: '',
      }
    },
    props: {
      total: {// 数据总条数
        type: Number,
        default: 0
      },
      pageSize: {// 每页显示条数
        type: Number,
        default: 10
      },
      current: {// 当前页码
        type: Number,
        default: 1
      },
      pagegroup: {// 分页条数
        type: Number,
        default: 5,
        coerce: function (v) {
          v = v > 0 ? v : 5;
          return v % 2 === 1 ? v : v + 1;
        }
      }
    },
    watch:{
      current(val) {
        this.currents = val;
      }
    },
    computed: {
      page: function () { // 总页数
        return Math.ceil(this.total / this.pageSize);
      },
      grouplist: function () { // 获取分页页码
        var len = this.page, temp = [], list = [], count = Math.floor(this.pagegroup / 2), center = this.current;
        if (len <= this.pagegroup) {
          while (len--) {
            temp.push({text: this.page - len, val: this.page - len});
          }
          ;
          return temp;
        }
        while (len--) {
          temp.push(this.page - len);
        }
        ;
        var idx = temp.indexOf(center);
        (idx < count) && ( center = center + count - idx);
        (this.current > this.page - count) && ( center = this.page - count);
        temp = temp.splice(center - count - 1, this.pagegroup);
        do {
          var t = temp.shift();
          list.push({
            text: t,
            val: t
          });
        } while (temp.length);
        if (this.page > this.pagegroup) {
          (this.current > count + 1) && list.unshift({text: '...', val: list[0].val - 1});
          (this.current < this.page - count) && list.push({text: '...', val: list[list.length - 1].val + 1});
        }
        return list;
      }
    },
    methods: {
      setCurrent: function (idx) {
        idx = Number(idx);
        this.currents = idx;
        if(idx<1) {
          this.currents = 1;
        }
        if(idx>this.page) {
          this.currents = this.page;
        }
        this.$emit('pagechange', this.currents);
/*       if (this.current != idx && idx > 0 && idx < this.page + 1) {
          this.current = idx;
         console.log(this.grouplist);
         this.$emit('pagechange', this.current);
        }*/
      },
      selectIpt: function(p) {
        this.currents = 1;
        this.$emit('pagesizechange', p);
      }
    },
    created(){
      this.value = this.options[0].label;
    }
  }
</script>

<style type="text/css" scoped>
  .pageNum {
    width: 45px;
    height: 26px;
    border-radius: 3px;
    border: 1px solid #ddd;
    padding-left: 5px;
    margin: 0 3px;
  }
  .pop-span {
    display: inline-block;
    line-height: 30px;
    color: #333;
    font-size: 14px;
    font-weight: 400;
  }
  .page {
    padding-top: 10px;
  }
  .right {
    float: right;
  }
  .select-s {
    display: inline-block;
    width: 115px;
    vertical-align: bottom;
  }
  .navtable {
    display: inline-block;
    height: 30px;
    margin-left: 5px;
    vertical-align: bottom;
  }
  .locapage {
    display: inline-block;
    height: 30px;
    margin-left: 5px;
    vertical-align: bottom;
  }
  .locapage  {
    font-size: 14px;
    color: #333;
  }

  .pagination {
    overflow: hidden;
    margin: 0 auto;
    /*width: 100%;*/
    height: 38px;
  }
  .pagination li {
    float: left;
    border-radius: 5px;
    margin: 0 5px;
    color: #666;
    font-size: 14px;
    line-height: 1.42857143;
    color: #333;
    background-color: #fff;
    border: 1px solid #ddd;
  }
  .pagination li a:hover{
    background-color:  #ddd;
  }
  .pagination .active a,
  .pagination .active a:hover{
    z-index: 3;
    color: #fff;
    cursor: default;
    background-color: #3b8cff;
    border-radius: 3px;
  }
  .pagination a {
    position: relative;
    float: left;
    padding: 0px 12px;
    margin-left: -1px;
    line-height: 28px;
    color: #3b8cff;
    text-decoration: none;
  }
  .pagination .disabled a,
  .pagination .disabled a:hover {
    color: #777;
    cursor: not-allowed;
  }
  .el-select-dropdown__item:hover {
    background-color: #3b8cff;
    color: #fff;
  }
  .el-select-dropdown__item {
    color: #333333;
  }
  .el-button--primary {
    background-color: #3b8cff;
  }

</style>
<style>
  .sjcx-con  .el-input__inner {
    color: #333333;
  }
  .el-input--mini .el-input__inner {
    height: 30px !important;
  }
  .sjcx-con .el-select-dropdown__item.hover,
  .sjcx-con .el-select-dropdown__item:hover {
    background-color: #3b8cff;
    color: #fff;
  }
</style>
