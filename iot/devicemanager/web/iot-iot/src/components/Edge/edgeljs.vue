<template>
  <div class="sjdc-con">
    <iframe onscroll="no" :src="ifurl" frameborder="0"></iframe>
  </div>
</template>
<script>
  export default {
    created:function () {
      this.ifurl = this.$API.edgeljs.ljsurl;
    },
    name: 'edgesjdc',
    data () {
      return {
        ifurl: ''
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
