import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex);
export default new Vuex.Store({
  state: {
    	temListcont:'',//物模板
      input:'',
    	id:0,
       total_num:0,
       pageTotal:0,
       rowcount:7,
       currentpage:1,
       show_no_data:false,
    	listIndexText:{
        name:'',
        dec:'',
        attr:[]
      },
      editname:{
        name:''
      },
  },
  mutations: {
  	//物模板列表
  	temlistFn:function(state,that){
      // if(state.currentpage==)
		that.$axios.post(that.$API.temmodel.temList,{
            page:state.currentpage,
            offset:state.rowcount,
            name:state.input || ''
        }).then((res)=>{
            state.temListcont = res.data.data.rows;
            state.total_num = res.data.data.total;
            state.pageTotal = res.data.data.pageTotal;
            if(state.temListcont==''){
              that.$axios.post(that.$API.temmodel.temList,{
                  page:1,
                  offset:state.rowcount,
                  name:state.input || ''
              }).then((res)=>{
                state.temListcont = res.data.data.rows;
                state.total_num = res.data.data.total;
                state.pageTotal = res.data.data.pageTotal;
                })
            }
            // state.currentpage = res.data.data.pageTotal;
		});
  	},
  	//物模板详情列表
  	temlistDecFn:function(state,that){
  		state.id = location.hash.split('?')[1].split('&')[1].split('=')[1] || 0;
		that.$axios.post(that.$API.temmodel.temlistdec,{
            id:state.id
        }).then((res)=>{

        	 state.listIndexText = res.data.data;
          state.editname.name = state.listIndexText.name
           if(that.state.listIndexText.attr.length==0){
              that.show_no_data=true;
             }else{
              that.show_no_data=false;
             }
          function FormatDate (inputTime) {
            var date = new Date(inputTime);
            var y = date.getFullYear();  
            var m = date.getMonth() + 1;  
            m = m < 10 ? ('0' + m) : m;  
            var d = date.getDate();  
            d = d < 10 ? ('0' + d) : d;  
            var h = date.getHours();
            h = h < 10 ? ('0' + h) : h;
            var minute = date.getMinutes();
            var second = date.getSeconds();
            minute = minute < 10 ? ('0' + minute) : minute;  
            second = second < 10 ? ('0' + second) : second; 
            return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second; 
        }
        state.listIndexText.mtime = FormatDate(state.listIndexText.mtime);


		});
  	},
  	clearTemAttr:function(state){
       	var status=location.hash.split('?')[1].split('&')[0].split('=')[1];
  		 if(status==1){
        	 state.listIndexText.attr =[];
        }
  	}
  },
  actions:{
	  temlistFn: ({
	    commit
	    },that) => {
	    commit('temlistFn',that)
	  },
	  temlistDecFn: ({
	    commit
	    },that) => {
	    commit('temlistDecFn',that)
	  },
	  clearTemAttr: ({
	    commit
	    },that) => {
	    commit('clearTemAttr')
	  },
  },
  getters:{
  	state(state) {
    	return state;
  	}
  }
})


