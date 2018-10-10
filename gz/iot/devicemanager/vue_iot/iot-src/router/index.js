import Vue from 'vue'
import Router from 'vue-router'
import axios from 'axios'
import API from '../assets/api/api.config.js'
import store from '../store/index.js'

import Nav from '@/components/nav.vue'
import login from '@/components/index.vue'
import resetPassword from '@/components/modify_pwd.vue'
import noPermission from '@/components/no_permission.vue'
import Application from '@/components/Application/application.vue'
import Calculate from '@/components/Calculate/calculate.vue'
import Device from '@/components/Device/device.vue'
import Edge from '@/components/Edge/edge.vue'
import Listen from '@/components/Listening/listen.vue'
import Manager from '@/components/Edge/edgeManager.vue'
import deImage from '@/components/Edge/edgeImage.vue'
import desjdc from '@/components/Edge/edgesjdc.vue'
import deljs from '@/components/Edge/edgeljs.vue'

import Insert from '@/components/Edge/deviceInsert.vue'
import insertImg from '@/components/Edge/deviceInsertImg.vue'
import processModule from '@/components/Edge/handleModule.vue'
import processDetail from '@/components/Edge/processDetail.vue'
import upgradeModule from '@/components/Edge/upgradeModule.vue'
import Detail from '@/components/Edge/edgeDetail.vue'
/*import ImgDetail from '@/components/Edge/edgeImage.vue'*/
import Shadow from '@/components/Edge/shadow/shadowManager.vue'
import handleCreate from '@/components/Edge/handleCreate.vue'
import ShadowInsert from '@/components/Edge/shadow/shadowInsert.vue'
import insertInfo from '@/components/Edge/shadow/insertinfo.vue'
import insertModule from '@/components/Edge/shadow/insertModule.vue'
import insertMAP from '@/components/Edge/shadow/insertMAP.vue'
import insertFinish from '@/components/Edge/shadow/insertFinish.vue'
import pipeLine from '@/components/Edge/pipeLine.vue'
import pipeLineDetail from '@/components/Edge/pipeLineDetail.vue'
import pipeLineCreate from '@/components/Edge/pipeLineCreate.vue'
import labelManager from '@/components/Edge/labelManager.vue'
import labelDetail from '@/components/Edge/labelDetail.vue'
import ShadowDetail from '@/components/Edge/shadow/shadowDetail.vue'
import Blank from '@/components/Edge/blank.vue'

//2017-9-26新增冷链管理
import dataOverview from '@/components/Application/dataOverview.vue'
import realtimeData from '@/components/Application/realtimeData.vue'
import realtimeDetail from '@/components/Application/realtimeDetail.vue'

//2017-11-7新增信息管理
import companyInfo from '@/components/information/company_info.vue'
import personalData from '@/components/information/personal_data.vue'
import modifyPwd from '@/components/information/personal_modify_pwd.vue'
import accountManagement from '@/components/information/account_management.vue'

//2018-3-2新增外部链接
import Pipelines from '@/components/externalLinks/Pipelines.vue'
import Canalines from '@/components/externalLinks/Canalines.vue'
import License from '@/components/externalLinks/License.vue'
Vue.use(Router)

//export default new Router({
const router = new Router({
  routes: [
    {
      path: '/nav',
      name: 'nav',
      component: Nav
    },
    {
      path: '/',
      name: '登录',
      component: login,
      redirect:'/login'
    },
    {
      path: '/login',
      name: '登录',
      component: login
    },
    {
      path: '/resetPassword',
      name: '找回密码',
      component: resetPassword
    },
    {
      path: '/noPermission',
      name: '无权限',
      component: noPermission
    },
    {
      path: '/application',
      meta: { requiresAuth: true },//需要判断登录权限
      name: 'application',
      component: Application
    },
    {
      path: '/calculate',
      meta: { requiresAuth: true },//需要判断登录权限
      name: 'calculate',
      component: Calculate
    },
    {
      path: '/edge',
      meta: { requiresAuth: true },//需要判断登录权限
      name: 'edge',
      component: Edge,
      children:[
        {
          path: '/edge/manager',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'mangaer',
          component: Manager
        },
        {
          path: '/edge/deImage',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'deImage',
          component: deImage
        },
        {
          path: '/edge/desjdc',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'desjdc',
          component: desjdc
        },
        {
          path: '/edge/deljs',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'deljs',
          component: deljs
        },
        {
          path: '/edge/insert',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'insert',
          component: Insert
        },
        {
          path: '/edge/insertImg',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'insertImg',
          component: insertImg
        },
        {
          path: '/edge/processModule',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'module',
          component: processModule
        },
        {
          path: '/edge/processDetail',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'processDetail',
          component: processDetail
        },
        {
          path: '/edge/upgradeModule',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'upgradeModule',
          component: upgradeModule
        },
        {
          path: '/edge/pipeLine',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'pipeline',
          component: pipeLine
        },
        {
          path: '/edge/detail',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'detail',
          component: Detail
        },
        {
          path: '/edge/pipeLineDetail',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'pipeLineDetail',
          component: pipeLineDetail
        },
        {
          path: '/edge/pipeLineCreate',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'pipeLineCreate',
          component: pipeLineCreate
        },
        {
          path: '/edge/shadowManager',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'shadow',
          component: Shadow
        },
        {
          path: '/edge/handleCreate',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'handleCreate',
          component: handleCreate
        },
        {
          path: '/edge/labelManager',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'labelManager',
          component: labelManager
        },
        {
          path: '/edge/labelDetail',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'labelDetail',
          component: labelDetail
        },
        {
          path: '/Pipelines',
          //meta: { requiresAuth: true },//需要判断登录权限
          name: 'Pipelines',
          component: Pipelines
        },
        {
          path: '/Canalines',
          //meta: { requiresAuth: true },//需要判断登录权限
          name: 'Canalines',
          component: Canalines
        },
        {
          path: '/License',
          //meta: { requiresAuth: true },//需要判断登录权限
          name: 'License',
          component: License
        },
        //2017-9-26新增冷链管理路由
        // {
        //  path: '/application/dataOverview',
        //  name: 'dataOverview',
        //  component: dataOverview
        // },
        // {
        //  path: '/application/realtimeData',
        //  name: 'realtimeData',
        //  component: realtimeData
        // },
        // {
        //  path: '/application/realtimeDetail',
        //  name: 'realtimeDetail',
        //  component: realtimeDetail
        // },
        //2017-11-7新增信息管理路由
        {
         path: '/information/companyInfo',
         meta: { requiresAuth: true },//需要判断登录权限
         name: '企业信息',
         component: companyInfo
        },
        {
         path: '/information/personalData',
         meta: { requiresAuth: true },//需要判断登录权限
         name: '个人资料',
         component: personalData
        },
        {
         path: '/information/modifyPwd',
         meta: { requiresAuth: true },//需要判断登录权限
         name: '个人资料修改密码',
         component: modifyPwd
        },
        {
         path: '/information/accountManagement',
         meta: { requiresAuth: true },//需要判断登录权限
         name: '账号管理',
         component: accountManagement
        },

        {
          path: '/edge/shadowInsert',
          meta: { requiresAuth: true },//需要判断登录权限
          name: 'shadowInsert',
          component: ShadowInsert,
          children:[
            {
              path: '/edge/shadow/info',
              meta: { requiresAuth: true },//需要判断登录权限
              name: 'shadowInsertInfo',
              component: insertInfo
            },
            {
              path: '/edge/shadow/module',
              meta: { requiresAuth: true },//需要判断登录权限
              name: 'shadowInsertModule',
              component: insertModule
            },
            {
              path: '/edge/shadow/MAP',
              meta: { requiresAuth: true },//需要判断登录权限
              name: 'shadowInsertMAP',
              component: insertMAP
            },
            {
              path: '/edge/shadow/finish',
              meta: { requiresAuth: true },//需要判断登录权限
              name: 'shadowInsertFinish',
              component: insertFinish
            },
            {
             path: '/edge/shadow/Detail',
             meta: { requiresAuth: true },//需要判断登录权限
             name: 'shadowDetail',
             component: ShadowDetail,
            }
          ]
        },
      ]
    },
    {
      path: '/device',
      meta: { requiresAuth: true },//需要判断登录权限
      name: 'device',
      component: Device
    },
    {
      path: '/listen',
      meta: { requiresAuth: true },//需要判断登录权限
      name: 'listen',
      component: Listen
    },
    {
     path: '/edge/blank',
     meta: { requiresAuth: true },//需要判断登录权限
     name: 'blank',
     component: Blank
    }
  ]
})
//判断登录权限
router.beforeEach((to, from, next) => {
  // 可以在路由元信息指定哪些页面需要登录权限
  let checklogin = true // 假设没有登录（这里应从接口获取）
  //获取权限
  let storage = window.localStorage;
  if(storage.login_account != '' && storage.login_account != undefined){
    //判断是否具有权限
    let login_permission = JSON.parse(storage.login_permission) || [];
    for(let i=0;i<login_permission.length;i++){
      if(login_permission[i] == API.permission.edge_manage){
        if(to.path=='/edge'||to.path=='/edge/manager'||to.path=='/edge/deImage'||to.path=='/edge/insert'||to.path=='/edge/detail'||to.path=='/edge/blank'){
          checklogin = true;
        }
      }else if(login_permission[i] == API.permission.datastream_manage){
        if(to.path=='/edge/pipeLine'||to.path=='/edge/pipeLineDetail'||to.path=='/edge/pipeLineCreate'){
          checklogin = true;
        }
      }else if(login_permission[i] == API.permission.devmirror_manage){
        if(to.path=='/edge/shadowManager'||to.path=='/edge/shadowInsert'||to.path=='/edge/shadow/info'||to.path=='/edge/shadow/module'||to.path=='/edge/shadow/MAP'||to.path=='/edge/shadow/finish'||to.path=='/edge/shadow/Detail'){
          checklogin = true;
        }
      }else if(login_permission[i] == API.permission.procmodule_manage){
        if(to.path=='/edge/processModule'||to.path=='/edge/processDetail'||to.path=='/edge/upgradeModule'||to.path=='/edge/handleCreate'){
          checklogin = true;
        }
      }else if(login_permission[i] == API.permission.company_information){
        if(to.path=='/information/companyInfo'){
          checklogin = true;
        }
      }else if(login_permission[i] == API.permission.account_manage){
        if(to.path=='/information/accountManagement'){
          checklogin = true;
        }
      }else if(to.path=='/information/personalData'|| to.path=='/information/modifyPwd'){
        checklogin = true;
      }
    }
    //checklogin = true;
  }
  const islogin = checklogin; // 假设没有登录（这里应从接口获取）
  if(to.meta.requiresAuth && islogin == false) { // 需要登录授权，这里还需要判断是否登录
    //登出
    axios.post(API.login.logOut).then((res)=>{});
    window.localStorage.clear();
    store.commit("logout");
    next('/noPermission') // 跳转到无权限页面
    return
  }
  next()
})
export default router;
