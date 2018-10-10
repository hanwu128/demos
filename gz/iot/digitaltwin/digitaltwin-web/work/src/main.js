// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import Vuex from 'vuex'
import router from './router'
import axios from 'axios'
import API from './assets/api/api.config.js'
import ElementUI from 'element-ui'
import store from './store/index.js'
import 'element-ui/lib/theme-chalk/index.css'
import echarts from 'echarts'
import VueI18n from 'vue-i18n'

Vue.use(ElementUI)
Vue.use(Vuex)
Vue.use(VueI18n)
Vue.config.productionTip = false;
Vue.prototype.$axios = axios;
axios.defaults.withCredentials=true;//让ajax携带cookie
Vue.prototype.$API = API;
Vue.prototype.$echarts = echarts

var language = location.hash.split('language=')[1];
if(language==null){
  language='zh';
}
const i18n = new VueI18n({
  locale: language,
  messages: {
    'zh': require('@/assets/languages/zh.json'),
    'en': require('@/assets/languages/en.json')
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store, 
  i18n,
  template: '<App/>',
  components: { App }
})

