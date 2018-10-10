<template>
    <div class="right_content content_iframe">
		<v-iframe :id='id_data' :src="url_data" :width="width_data" :height="height_data"></v-iframe>
    </div>
</template>

<script>
import vIframe from '../externalLinks/iframe.vue'
export default {
	name: 'hello',
	data () {
    	return {
    		id_data:'canalines_iframe',
    		url_data:'https://www.baidu.com/',
    		width_data:'',
    		height_data:'3000'
    	}
  	},
	mounted(){
	    this.width_data = '100%';
	    this.height_data = window.screen.height-230;
  	},
	methods:{

	},
	components: {
		vIframe
	}
}
</script>
<style scoped>
</style>