<template>
    <div>
        <header-viewers></header-viewers>
        <div  class="right_content tc">
            <img src="../../../assets/images/shadowStep4.png">
        </div>
        <div  class="right_content">
            <ul class="label_ul">
                <li>
                    <i class="create_done"></i>
                    <p class="insert_finish_title">数字镜像添加成功！</p>
                </li>
                <li>
                  <label class="label_title">名称：</label><span style="width:150px;"  class="li_content">{{ shadowName }}</span>
                </li>
                <li>
                  <label class="label_title">网关设备名称：</label>
                  <span style="width:150px;"  class="li_content">{{ deviceID }}</span>
                </li>
                <li>
                  <label class="label_title">设备描述：</label><span style="width:150px;"  class="li_content">{{ shadowDesc }}</span>
                </li>
            </ul>     
        </div>
        <div class="btn_div_right">
            <span class="btn_back" @click="finishback">返回列表</span>
            <span class="btn_save" @click="finishnext">继续添加</span>
        </div>
    </div>
</template>
<script>
    import headerViewers from './header.vue'
    export default {
        name: 'insertFinish',
        data () {
            return {
                shadowName:this.$route.query.shadowName || {},
                shadowDesc:this.$route.query.shadowDesc || {},
                deviceID : this.$route.query.deviceID || {}
            }
        },
        components: {
            headerViewers : headerViewers
        },
        mounted:function(){
            //let shadowName = 
            //let shadowDesc = this.$route.query.shadowDesc || {}
            //let shadowID = this.$route.query.deviceID || {}
            //this.shadowapplist = JSON.parse(datas)
        },
        methods: {
            finishback:function () {
                this.$router.push({path:'/edge/shadowManager'})

            },
            finishnext:function () {
                this.$router.push({path:'/edge/shadow/info'})
            }
        }
    }
</script>
<style>
    .insert_finish_title {
        text-align: center;
    }
    .finishTable {
        position: relative;
        left: 15%;
        width: 70%;
    }
    .finishbtnBack {
        width: 80px;
        height: 30px;
        border: groove 1px solid;
        background-color: deepskyblue;
        position: relative;
        top: 50px;
        cursor: pointer;
    }
    .finishbtnNext {
        width: 80px;
        height: 30px;
        border: groove 1px solid;
        background-color: deepskyblue;
        position: relative;
        top: 50px;
        cursor: pointer;
    }
    .finishbutton {
        position: relative;
        text-align: center;
        width: 100%;
        height: 100px;
    }
    .finishTable {
        position: relative;
        left: 45%;
        width: 55%;
    }
</style>