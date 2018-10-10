<template>
  <div class="x-pie">
    <div :id="id" :series_data="series_data" style="height:380px;width: 100%;"></div>
  </div>
</template>
<script>
  import echarts from 'echarts'
  export default {
    // 验证类型
    data () {
      return {
        option:[],
        optionData:[]
      }
    },
    props:['id','series_data'],
    methods:{
        drawPie(id){
           this.charts = echarts.init(document.getElementById(id))
           this.charts.setOption({
             tooltip: {
               trigger: 'item',
               formatter: '{a}<br/>{b}:{c} ({d}%)'
             },
             legend: {
                x: 'center',
                bottom:'60px',
                //orient: 'vertical',
                textStyle: {
                  fontFamily: '微软雅黑',
                  fontWeight: 'normal',
                  fontSize: '12px',
                },
                data:this.option
             },
             color:['#ffb134','#00d2dc'],
             series: [
                {
                 name:'采集数据',
                 type:'pie',
                 radius:['40%','60%'],
                 center: ['50%', '40%'],
                 avoidLabelOverlap: false,
                 label: {
                   normal: {
                     show: false,
                     position: 'center'
                   },
                   // emphasis: {
                   //    show: true,
                   //    textStyle: {
                   //     fontSize: '30',
                   //     fontWeight: 'blod'
                   //   }
                   // }
                 },
                 labelLine: {
                   normal: {
                     show: false
                   }
                 },
                 data:this.optionData
               }
             ]
           })
        }
    },
    watch: {
        series_data:function() {
            let pieNames = [];
            for (var i=0;i<this.series_data.length;i++){
              pieNames.push(this.series_data[i].name);
            }
            console.log(pieNames);
            this.option = pieNames;
            this.optionData = this.series_data;
            this.drawPie(this.id);
        }
    },
    // mounted(){
    //   this.$nextTick(function() {
    //       this.drawPie(this.id);
    //   })
    // }

  }
</script>