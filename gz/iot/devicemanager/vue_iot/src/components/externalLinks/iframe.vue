<template>
	<iframe :id="id_data"  frameborder=0 name="showHere" scrolling="no" :src="url_str" :width="width_data" :height="height_data"></iframe>
</template>

<script>
export default {
	name: 'hello',
	data () {
    	return {
    	}
  	},
  	props:{
  		id_data:{
  			type:String,
	        default:'show_iframe'  
  		},
	    url_str:{
	      	type:String,
	        default:''    
	    },
	    width_data:{
	    	type:String,
	        default:'2000'
	    },
	    height_data:{
	    	type:String,
	        default:'2000'
	    } 
  	},
  	mounted(){
  	},
  	methods:{

  	}
}

</script>
<style scoped>
	#show_iframe{min-height: 2000px;}
</style>