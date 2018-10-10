<template>
  <div id="app">
    <nav-category></nav-category>
    <router-view></router-view>
  </div>
</template>

<script>
  import Nav from './components/nav.vue'

  export default {
    name: 'app',
    components: {
      navCategory : Nav

    },
  }
</script>

<style>
</style>
