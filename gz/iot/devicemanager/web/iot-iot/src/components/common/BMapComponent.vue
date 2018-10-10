<template>
  <div id="allmap" v-bind:style="mapStyle" :longitude='longitude' :latitude='latitude' :description='description' :check_deviceid="check_deviceid"></div>
</template>
<script>
export default{
  data:function(){
    return{
      mapStyle:{
        height:this.mapHeight +'px',
        margin:'0 20px 20px 20px'
      },
      timer_id:0
    }
  },
  props:{
    // 地图在该视图上的高度
    mapHeight:{
      type:Number,
      default:530
    },
    // 经度
    longitude:{
      type:Number,
      default:116.404
    },
    // 纬度
    latitude:{
      type:Number,
      default:39.915
    },
    description:{
      type:String,
      default:'天安门'
    },
    check_deviceid:{
      type:String,
      default:''
    }
  },
  methods:{
    ready:function(){
      var map =new BMap.Map("allmap",{enableMapClick:false,minZoom:4});
      var point =new BMap.Point(this.longitude,this.latitude);
      var markerIcon = new BMap.Icon("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAB5klEQVRYR+1WTU7CUBCeeeTVjVGWJgrKRstKOYF4AvUE4gnUE1hvACfQI+gJ1BMIqxI3NgUTd4KubOGNeSWvllIpaWxlQXdt5+d73zdvZhD++cF/zg8LAPi8zvcoh4dJpSCgnm67jaT+aBY1CwE2kwaQfiTEQbk7eEgSA9tFjZI4Bn3mAgAMBhZyfv37YbC39OmclnrQC9r8GQMyKDJ2P5VNQQ29655nBID6RNBUyRBxP6peUmOAiB7LHbeqAJhFzUCASwCwlj6cipIiMwASSLvAm4C4CwEpMgUge47I4VNQikwByMRhKTIHEJYiNQCzNrcFgAUDc8wAUYsJqMlqHjKoq14eVd1yHEcPI+ojiRoKZgmEGjA8C/ujWeA3iHgS/sGGVNl+db1h8pKH/NeK9h59tahPjruXy/G86nLKjgCuyrZjqHe/FfsG1JpYSpUROU6p/AZWGIBuO7GLbHuD10enpQvdduv+QCrwB8lkcIGZCOYzQnCrFojAtzu94xzFNRmzwGuIKJcTiw3pWDL58436uu3m/TEdDjaim1sAuDpNlhlAeKedtBtnJZJOcw22kGt1QKp6QAjumCBD1URccl+2ZW4QwyNv6SWptzB2OsPbsY1olmBp2sQWVJrJvaubdoK4+AsA35gJSV+M0CrkAAAAAElFTkSuQmCC",new BMap.Size(32, 32));
      var marker =new BMap.Marker(point,{icon:markerIcon});
      map.addOverlay(marker);
      map.centerAndZoom(point,15);
      //map.addControl(new BMap.ScaleControl({anchor:BMAP_ANCHOR_TOP_LEFT}));//比例尺
      //map.addControl(new BMap.NavigationControl());//放大缩小工具
      //map.addControl(new BMap.NavigationControl({anchor:BMAP_ANCHOR_BOTTOM_RIGHT,type:BMAP_NAVIGATION_CONTROL_SMALL}));//右下角放大缩小工具
      // 信息窗的配置信息
      var opts ={
        width :250,
        height:75,
        title :"地址：",
        enableMessage:false,//设置允许信息窗发送短息
      }
      var infoWindow =new BMap.InfoWindow(this.description, opts);// 创建信息窗口对象
      this.timer_id = setInterval(()=>{
        this.$axios.post(this.$API.dataOverview.mapData,{
            device_id: this.check_deviceid
        }).then( (res) =>{
          let newPoint = new BMap.Point(res.data.longitude, res.data.latitude);
          map.clearOverlays(marker);
          map.addOverlay(new BMap.Marker(newPoint,{icon:markerIcon}));
          map.centerAndZoom(newPoint,15);
        });
      },5000);
      //var newPoint = new BMap.Point(116.404, 39.915);
      // marker.addEventListener("click",function(){
      //   map.openInfoWindow(infoWindow,point);
      // });
      //map.enableScrollWheelZoom(this.enableScrollWheelZoom);//鼠标滚轮事件
      //map.openInfoWindow(infoWindow,point);//开启信息窗口
    }
  },
  beforeDestroy:function(){
      clearInterval(this.timer_id);
    },
  mounted () {  
    // this.$nextTick(() => {
    //   this.ready();  
    // });
  },
  watch: {
    longitude:function() {
      this.$nextTick(() => {
          clearInterval(this.timer_id);
          this.ready();
      })
    }
  }
}
</script>
<style scoped>
</style>