<template>
  <div class="sjdc-con">
    <iframe onscroll="no" src="http://172.17.171.17:3000/?orgId=1&panelId=3&fullscreen" frameborder="0"></iframe>
  </div>
</template>
<script>
  export default {
    created:function () {
    },
    name: 'edgesjdc',
    data () {
      return {
      }
    },
    methods: {
    }
  }
</script>
<style scoped>
 .sjdc-con {
   width: 100%;
   height: 100%;
   padding-left: 180px;
   box-sizing: border-box;
 }
 .sjdc-con iframe {
   width: 100%;
   height: 865px;
 }
</style>
