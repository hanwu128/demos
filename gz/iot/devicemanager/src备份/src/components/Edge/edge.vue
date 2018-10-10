<template>
    <div class="body_content">
        <div class="menu_list">
            <ul class="operate_nav">
                <li class="li2" v-if='permission_list[0]'>
                    <label class="operate_nav_a" :class="{'active':menu_li_show[0]}" @click="menu_li_click(0)"><i></i><span>边缘分析</span><strong :class="{'active':menu_strong_show[0]}"></strong></label>
                    <el-collapse-transition>
                        <div class="operate_dd" v-if="menu_strong_show[0]">
                            <dl>
                                <dd @click="menuClick('dd1')" class="dd1" v-if='permission_list[1]'><router-link to="/edge/manager"><em></em>网关设备管理</router-link></dd>
                                <dd @click="menuClick('dd2')" class="dd2" v-if='permission_list[2]'><router-link to="/edge/pipeLine"><em></em>数据流管理</router-link></dd>
                                <!--<dd @click="menuClick('dd3')" class="dd3" v-if='permission_list[3]'><router-link to="/edge/shadowManager"><em></em>数字镜像管理</router-link></dd>-->
                                <dd @click="menuClick('dd4')" class="dd4" v-if='permission_list[4]'><router-link to="/edge/processModule"><em></em>处理模块管理</router-link></dd>
                                <!-- <dd @click="menuClick('dd5')" class="dd5" :class="{'active':menu_dd_active[4]}"><router-link to="/edge/labelManager"><em></em>标签管理</router-link></dd> -->
                            </dl>
                        </div>
                    </el-collapse-transition>
                </li>
                <!-- <li class="li5">
                    <label class="operate_nav_a" :class="{'active':menu_li_show[1]}" @click="menu_li_click(1)"><i></i><span>冷链应用</span><strong :class="{'active':menu_strong_show[1]}"></strong></label>
                    <el-collapse-transition>
                        <div class="operate_dd" v-if="menu_strong_show[1]">
                            <dl>
                                <dd @click="menuClick('li5_dd1')" class="li5_dd1"><router-link to="/application/dataOverview"><em></em>数据概述</router-link></dd>
                                <dd @click="menuClick('li5_dd2')" class="li5_dd2"><router-link to="/application/realtimeData"><em></em>实时数据</router-link></dd>
                                <dd @click="menuClick('li5_dd3')" class="li5_dd3" :class="{'active':menu_dd_active[7]}"><router-link to=""><em></em>设备管理</router-link></dd>
                            </dl>
                        </div>
                    </el-collapse-transition>
                </li> -->
                <!--2018.3.7新增链接-->
                <li class="li4">
                	<router-link to="/edge/shadowManager"><label class="operate_nav_a" :class="{'active':menu_li_show[3]}" @click="menu_li_click(3)"><i></i><span>数字镜像</span></label></router-link>
                </li>
                <li class="li5">
                    <label class="operate_nav_a" :class="{'active':menu_li_show[4]}" @click="menu_li_click(4)"><i></i><span>流计算</span><strong :class="{'active':menu_strong_show[4]}"></strong></label>
                    <el-collapse-transition>
                        <div class="operate_dd" v-if="menu_strong_show[4]">
                            <dl>
                                <dd @click="menuClick('li5_dd1')" class="li5_dd1"><router-link to="/Pipelines"><em></em>Pipelines</router-link></dd>
                                <dd @click="menuClick('li5_dd2')" class="li5_dd2"><router-link to="/Canalines"><em></em>Canalines</router-link></dd>
                            </dl>
                        </div>
                    </el-collapse-transition>
                </li>
                <li class="li6">
                    <label class="operate_nav_a" :class="{'active':menu_li_show[1]}" @click="menu_li_click(1)"><i></i><span>License</span><strong :class="{'active':menu_strong_show[1]}"></strong></label>
                    <el-collapse-transition>
                        <div class="operate_dd" v-if="menu_strong_show[1]">
                            <dl>
                                <dd @click="menuClick('li6_dd1')" class="li6_dd1"><router-link to="/License"><em></em>License管理</router-link></dd>
                            </dl>
                        </div>
                    </el-collapse-transition>
                </li>
                <li class="li3" v-if='permission_list[5]'>
                    <label class="operate_nav_a" :class="{'active':menu_li_show[2]}" @click="menu_li_click(2)"><i></i><span>信息管理</span><strong :class="{'active':menu_strong_show[2]}"></strong></label>
                    <el-collapse-transition>
                        <div class="operate_dd" v-if="menu_strong_show[2]">
                            <dl>
                                <dd @click="menuClick('li3_dd1')" class="li3_dd1" v-if='permission_list[6]'><router-link to="/information/companyInfo"><em></em>企业信息</router-link></dd>
                                <dd @click="menuClick('li3_dd2')" class="li3_dd2" v-if='permission_list[7]'><router-link to="/information/personalData"><em></em>个人资料</router-link></dd>
                                <dd @click="menuClick('li3_dd3')" class="li3_dd3" v-if='permission_list[8]'><router-link to="/information/accountManagement"><em></em>账号管理</router-link></dd>
                            </dl>
                        </div>
                    </el-collapse-transition>
                </li>
                <!-- 新增链接 -->
            </ul>
        </div>
        <router-view :backgroundFn="showFn"></router-view>
        <div class='background_shadow' v-if="background_show" style="position: fixed; left: 0px; top: 0px; bottom: 0px; background: rgba(0, 0, 0, 0.298039); height: 100%; width: 100%; z-index: 4100;" ></div>
    </div>
</template>
<script>
    export default {
        name: 'menu',
        data () {
            return {
                menu_li_show:[false,false,false,false,false],
                menu_strong_show:[false,false,false,false,false],
                background_show:false,
                permission_list:[false,false,false,false,false,false,false,true,false]
            }
        },
        methods:{
            menuClick:function (class_name) {
                $('.operate_nav_a , .operate_nav dd').removeClass('active');
                $(event.currentTarget).addClass('active');
                $(event.currentTarget).parents('li').children('.operate_nav_a').addClass('active');

            },
            menu_li_click:function(status){
                if(status == 0){
                    this.menu_strong_show = [!this.menu_strong_show[status],false];
                    this.set_menu(0);
                }else if(status == 1){
                    this.menu_strong_show = [false,!this.menu_strong_show[status],false,false,false];
                    this.set_menu(0);
                }else if(status == 2){
                    this.menu_strong_show = [false,false,!this.menu_strong_show[status],false,false];
                    this.set_menu(0);
                }else if(status == 3){
                	this.menu_strong_show = [false,false,false,true,false];
                    this.set_menu(1);
                }else if(status == 4){
                	this.menu_strong_show = [false,false,false,false,!this.menu_strong_show[status]];
                    this.set_menu(0);
                }
            },
            showFn:function(data){
                console.log(data);
            },
            set_menu:function(status){
                let page_path = window.location.hash;
                let module_name = page_path.split('/').pop().split('?')[0];
                if(module_name == 'edge' || module_name == 'manager' || module_name == 'detail' || module_name == 'insert'){
                    if(status == 1){
                        this.menu_li_show=[true,false,false,false,false];
                        this.menu_strong_show = this.menu_li_show;
                    }
                    setTimeout( ()=>{
                        $('.operate_nav dd').removeClass('active');
                        $('.dd1').addClass('active');
                    },50);
                }else if(module_name == 'pipeLine' || module_name == 'pipeLineDetail' || module_name == 'pipeLineCreate'){
                    if(status == 1){
                        this.menu_li_show=[true,false,false,false,false];
                        this.menu_strong_show = this.menu_li_show;
                    }
                    setTimeout( ()=>{
                        $('.operate_nav dd').removeClass('active');
                        $('.dd2').addClass('active');
                    },50);
                }else if(module_name == 'shadowManager' || module_name == 'Detail' || module_name == 'info' || module_name == 'module' || module_name == 'MAP' || module_name == 'finish'){
                    if(status == 1){
                        this.menu_li_show=[false,false,false,true,false];
                        this.menu_strong_show = this.menu_li_show;
                    }
                    setTimeout( ()=>{
                        $('.operate_nav dd').removeClass('active');
                        $('.dd3').addClass('active');
                    },50);
                }else if(module_name == 'processModule' || module_name == 'processDetail' ||module_name == 'handleCreate' || module_name == 'upgradeModule'){
                    if(status == 1){
                        this.menu_li_show=[true,false,false,false,false];
                        this.menu_strong_show = this.menu_li_show;
                    }
                    setTimeout( ()=>{
                        $('.operate_nav dd').removeClass('active');
                        $('.dd4').addClass('active');
                    },50);
                }else if(module_name == 'Pipelines'){
                    if(status == 1){
                        this.menu_li_show=[false,false,false,false,true];
                        this.menu_strong_show = this.menu_li_show;
                    }
                    setTimeout( ()=>{
                        $('.operate_nav dd').removeClass('active');
                        $('.li5_dd1').addClass('active');
                    },50);
                }else if(module_name == 'Canalines'){
                    if(status == 1){
                        this.menu_li_show=[false,false,false,false,true];
                        this.menu_strong_show = this.menu_li_show;
                    }
                    setTimeout( ()=>{
                        $('.operate_nav dd').removeClass('active');
                        $('.li5_dd2').addClass('active');
                    },50);
                }else if(module_name == 'License'){
                    if(status == 1){
                        this.menu_li_show=[false,true,false,false,false];
                        this.menu_strong_show = this.menu_li_show;
                    }
                    setTimeout( ()=>{
                        $('.operate_nav dd').removeClass('active');
                        $('.li6_dd1').addClass('active');
                    },50);
                }else if(module_name == 'companyInfo'){
                    if(status == 1){
                        this.menu_li_show=[false,false,true,false,false];
                        this.menu_strong_show = this.menu_li_show;
                    }
                    setTimeout( ()=>{
                        $('.operate_nav dd').removeClass('active');
                        $('.li3_dd1').addClass('active');
                    },50);
                }else if(module_name == 'personalData' || module_name == 'modifyPwd'){
                    if(status == 1){
                        this.menu_li_show=[false,false,true,false,false];
                        this.menu_strong_show = this.menu_li_show;
                    }
                    setTimeout( ()=>{
                        $('.operate_nav dd').removeClass('active');
                        $('.li3_dd2').addClass('active');
                    },50);
                }else if(module_name == 'accountManagement'){
                    if(status == 1){
                        this.menu_li_show=[false,false,true,false,false];
                        this.menu_strong_show = this.menu_li_show;
                    }
                    setTimeout( ()=>{
                        $('.operate_nav dd').removeClass('active');
                        $('.li3_dd3').addClass('active');
                    },50);
                }
            },
            permissionFn:function(){
                //获取权限
                let storage = window.localStorage;
                if(storage.login_account == '' || storage.login_account == undefined){
                    //this.$router.push({path: '/login'});
                }else{
                    let login_account = JSON.parse(storage.login_account) || {};
                    //let login_permission = JSON.parse(storage.login_permission) || [];
                    let login_data = JSON.parse(storage.data) || {};
                    this.$store.commit("login",login_data);
                    let login_permission = this.$store.state.login_permission || [];
                    for(let i=0;i<login_permission.length;i++){
                        if(login_permission[i] == this.$API.permission.edge_analyse){
                            this.permission_list[0] = true;
                        }else if(login_permission[i] == this.$API.permission.edge_manage){
                            this.permission_list[1] = true;
                        }else if(login_permission[i] == this.$API.permission.datastream_manage){
                            this.permission_list[2] = true;
                        }else if(login_permission[i] == this.$API.permission.devmirror_manage){
                            this.permission_list[3] = true;
                        }else if(login_permission[i] == this.$API.permission.procmodule_manage){
                            this.permission_list[4] = true;
                        }else if(login_permission[i] == this.$API.permission.information_manage){
                            this.permission_list[5] = true;
                        }else if(login_permission[i] == this.$API.permission.company_information){
                            this.permission_list[6] = true;
                        }else if(login_permission[i] == this.$API.permission.company_setting){
                            storage.removeItem("company_setting");
                            storage["company_setting"] = true;
                        }else if(login_permission[i] == this.$API.permission.account_manage){
                            this.permission_list[8] = true;
                        }
                    }
                }
            }
        },
        watch: {
          '$route':function(){
            this.permissionFn();
            setTimeout( ()=>{
                this.set_menu(1);
            },60);
          }
        },
        created: function(){
            this.permissionFn();
            this.set_menu(1);
            document.onkeydown = (e)=>{
                let key=window.event.keyCode;
                if(key==13){
                  return false;
                }
            }

        }
    }
</script>
