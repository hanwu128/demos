<template>
  <div class="x-line">
    <div :id="id" :series_data="series_data" :chart_data="chart_data" :xName='xName' :yName='yName' :legendShow='legendShow' style="height:380px;width: 100%;"></div>
  </div>
</template>
<script>
  import HighCharts from 'highcharts'
  export default {
    // 验证类型
    data () {
      return {
        option:[],
        optionData:[]
      }
    },
    props:{
      id:{
        type:String,
        default:'trend_line'    
      },
      series_data:{
        type:Object,
        default:function(){
          return {}
        }
      },
      chart_data:{
        type:Object,
        default:function(){
          return {type:'spline'};
        }
      },
      titleName:{
        type:String,
        default:''
      },
      xName:{
        type:String,
        default:'X'
      },
      yName:{
        type:String,
        default:'Y'
      },
      legendShow:{
        type:Boolean,
        default:true
      },
      xUnit:{
        type:String,
        default:'' 
      },
      yUnit:{
        type:String,
        default:'' 
      },
      colors:{
        type:Array,
        default:function(){
          return ['#fbc32e','#00a6c0','#917ae4','#fb9170','#7adb78'];
        }
      },
      tooltipUnit:{
        type:String,
        default:'' 
      }
    },
    methods:{
        drawLine(){
          let _self = this;
          let line_options = {
            chart: this.chart_data,
            credits:{
              enabled:false
            },
            colors:this.colors,
            title: {
                text: this.titleName
            },
            legend: {                                                                    
              enabled: this.legendShow                                                           
            }, 
            plotOptions: {
              area: {
                fillColor: {
                    linearGradient: {
                        x1: 0,
                        y1: 0,
                        x2: 0,
                        y2: 1
                    },
                    stops: [
                        [0, HighCharts.getOptions().colors[0]],
                        [1, HighCharts.Color(HighCharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                    ]
                },
                marker: {
                    radius: 2
                },
                lineWidth: 1,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null
              }
            },
            tooltip: {
              valueSuffix: this.tooltipUnit
            },  
            xAxis: {
              categories: this.option,
              tickmarkPlacement: 'on',
              labels: {  
                style: {
                    fontWeight: 'normal',
                    fontFamily: 'Microsoft YaHei',
                    fontSize: '12px'
                },
                formatter: function (){
                  return this.value + _self.xUnit;
                }
              },
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
                    formatter: function (){
                      return this.value + _self.yUnit;
                    }
                },
                allowDecimals: false,
            },
            series: this.optionData
          }
          HighCharts.chart(this.id, line_options)
        }
    },
    watch: {
        series_data:function() {
          let X = this.xName;
          let Y = this.yName;
          this.option = this.series_data.X;
          this.optionData = this.series_data.Y;
          this.drawLine();
        }
    }
    // mounted(){
    //   this.$nextTick(function() {
    //       this.drawLine();
    //   })
    // }
  }
</script>