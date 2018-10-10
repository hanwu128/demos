<template>
  <div class="x-bar">
    <div :id="id" :categories_data="categories_data" :series_data="series_data"></div>
  </div>
</template>
<script>
  import HighCharts from 'highcharts'
  export default {
    // 验证类型
    data () {
        return {
          line_options: {
                  chart: {
                    type: 'spline',
                  },
                  credits:{
                      enabled:false
                  },
                  colors:['#fbc32e','#00a6c0','#917ae4','#fb9170','#7adb78'],
                  title: {
                      text: ''
                  },
                  tooltip: {  
                    valueSuffix: '°C'  
                  },  
                  xAxis: {
                      categories: [],
                      tickmarkPlacement: 'on'
                  },
                  yAxis: {
                      title: {
                          text: ''
                      },
                      labels: {
                          style: {
                              fontWeight: 'normal',
                              fontFamily: 'Microsoft YaHei',
                              fontSize: '12px'
                          },
                          formatter: function () {
                              return this.value + '℃';//y轴加上℃
                          }
                      },
                      allowDecimals: false,
                      max: 30
                  },
                  series:[]
              }
        }
    },
    props:['id', 'categories_data' , 'series_data'],
    watch: {
        series_data:function() {
            console.log(this.series_data)
            this.line_options.xAxis.categories = this.categories_data;
            this.line_options.series = this.series_data;
            this.line_options.series.smooth = true;
            HighCharts.chart(this.id, this.line_options)
        }
    },
    mounted() {
      //this.line_options.xAxis.categories = this.categories_data;
      //this.line_options.series = this.series_data;
      //HighCharts.chart(this.id, this.line_options)
    }
  }
</script>