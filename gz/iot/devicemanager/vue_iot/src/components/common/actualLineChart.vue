<template>
  <div class="x-animate-bar">
    <div :id='id_animate' :label_ids='label_ids' :label_datas='label_datas'></div>
  </div>
</template>
<script>
  import Highcharts from 'highcharts'
  Highcharts.setOptions({
    global: {
      useUTC: false
    }
  });
  export default {
    // 验证类型
    data () {
        return {
          interval_id: 0, 
          line_options: {}
        }
    },
    props:['id_animate','label_ids','label_datas'],
    mounted: function () {
      console.log(this.label_ids);
       // this.$nextTick(() => {
       //   this.lineFn();
       //   Highcharts.chart('actual_line', this.line_options);
       // })
    },
    watch: {
      label_ids:function() {
         this.$nextTick(() => {
           clearInterval(this.interval_id);
           this.lineFn();
           Highcharts.chart(this.id_animate, this.line_options);
         })
      }
    },
    beforeDestroy:function(){
      clearInterval(this.interval_id);
    },
    methods:{
      lineFn:function(){
        let _self = this;
        let line_series_data = this.label_datas.series_data;
        let line_x = this.label_datas.categories_data;
        let labelidarray = this.label_ids;
        let line_data={
                  chart: {
                    type: 'spline',
                    animation: Highcharts.svg, // don't animate in old IE
                    marginRight: 10,
                    events: {
                      load: function () {
                        // set up the updating of the chart each second
                        var chart = this;
                        var timer_id = setInterval(function () {
                            //实时刷新折线图调用
                            _self.$axios.post(_self.$API.labelManage.labelLine,{'flagTimeBatch': 1,'labelidarray':labelidarray}).then(function(res){
                                let option_data = res.data.actionResultData;
                                let x_data = option_data.listX_TimeAt[0];
                                let y_data = option_data.listY_oC;
                                chart.xAxis[0].categories.push(x_data);
                                for(let i=0;i<labelidarray.length;i++){
                                  chart.series[i].addPoint([x_data, y_data[i].data[0]], true, true);
                                }
                            });
                        }, 5000);
                        _self.interval_id = timer_id;
                      }
                    }
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
                    categories:line_x,
                    labels: {
                     //step: 3,
                     //staggerLines: 1
                    },
                    tickmarkPlacement: 'on'
                  },
                  yAxis: {
                      title: {
                          text: ''
                      },
                      labels: {
                          style: {
                              fontWeight: 'normal',
                              fontFamily: '微软雅黑',
                              fontSize: '12px'
                          },
                          formatter: function () {
                              return this.value + '℃';//y轴加上℃
                          }
                      },
                      allowDecimals: false,
                      max: 30
                  },
                  tooltip: {
                      formatter: function () {
                        return '<b>' + '实时温度' + '</b><br/>' +
                          Highcharts.numberFormat(this.y, 2);
                      }
                  },
                  legend: {
                    enabled: true
                  },
                  exporting: {
                    enabled: false
                  },
                  series: line_series_data
              }
            this.line_options = line_data;
      }
    }
  }
</script>