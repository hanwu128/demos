<template>
  <div class="x-bar">
    <div :id="id" :chart_type="chart_type" :categories_data="categories_data" :series_data="series_data"></div>
  </div>
</template>
<script>
  import echarts from 'echarts'
  export default {
    // 验证类型
    data () {
        return {
          line_options: {
            chart: {
              type: pie,
            },
            credits:{
                enabled:false
            },
            colors:['#ffb134 ','#00d2dc','#917ae4','#fb9170','#7adb78'],
            title: {
                text: ''
            },
            tooltip: {    
            },  
            legend: {
              x: 'center',
              y: 'bottom',
              //orient: 'vertical',
              textStyle: {
                fontFamily: '微软雅黑',
                fontWeight: 'normal',
                fontSize: '12px',
              },
              data: []
            },
            xAxis:{
              tickmarkPlacement: 'on'
            },
            series: [{
              name: '',
              type: 'pie',
              radius: ['40%', '60%'],
              data: []
            }]
          }
        }
    },
    props:['id','chart_type','categories_data','series_data'],
    watch: {
        series_data:function() {
            console.log(this.series_data)
            this.line_options.xAxis.categories = this.categories_data;
            this.line_options.series = this.series_data;
            this.line_options.series.smooth = true;
            HighCharts.chart(this.id, this.line_options);
        }
    }
  }
</script>