import Vue from 'vue'
import Router from 'vue-router'
import MaterialTem from '@/components/MaterialTem'
import RealColumn from '@/components/RealColumn'
import addExample from '@/components/addExample'
import addTem from '@/components/addTem'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/MaterialTem',
      name: 'MaterialTem',
      component: MaterialTem
    },
    {
      path: '/addTem',
      name: 'addTem',
      component: addTem
    },
    {
      path: '/RealColumn',
      name: 'RealColumn',
      component: RealColumn
    },
    {
      path: '/addExample',
      name: 'addExample',
      component: addExample
    }
  ]
})
