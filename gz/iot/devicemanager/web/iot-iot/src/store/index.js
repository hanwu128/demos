import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex);
export default new Vuex.Store({
    state:{
    	login_permission:[],
    	login_account:{},
		login_name:''
    },
    mutations: {　　　　　　　　
	　　login(state,login_array){
			state.login_permission=login_array.permission;
			state.login_account=login_array.account;
			state.login_name=login_array.account.loginName;
		},
		logout(state){
			state.login_permission=[];
			state.login_account={};
			state.login_name='';
		}
	},
})