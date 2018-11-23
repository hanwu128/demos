package com.hw.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 菜单操作
 */
@Controller
public class MenuController {

    private static final String PAGE = "admin/index/";

    /**
     * 主页
     *
     * @return
     */
    @RequestMapping("/index.html")
    public String index() {
        return "index";
    }

    /**
     * 邮件管理
     *
     * @return
     */
    @RequestMapping("/email.html")
    public String email() {
        return PAGE + "email";
    }

    /**
     * 写邮件
     *
     * @return
     */
    @RequestMapping("/email/write.html")
    public String emailWrite() {
        return PAGE + "email-write";
    }

    //控制台
    @RequestMapping("/home/console.html")
    public String console() {
        return "home/console";
    }

    //主页一
    @RequestMapping("/home/homepage1.html")
    public String homepage1() {
        return "home/homepage1";
    }

    //主页二
    @RequestMapping("/home/homepage2.html")
    public String homepage2() {
        return "home/homepage2";
    }

    //搜索
    @RequestMapping("/template/search.html")
    public String templateSearch(String keywords) {
        return "template/search";
    }

    //栅格    等比例列表排列
    @RequestMapping("/component/grid/list.html")

    public String componentGridList() {
        return "component/grid/list";
    }

    //栅格    按移动端排列
    @RequestMapping("/component/grid/mobile.html")
    public String componentGridMobile() {
        return "component/grid/mobile";
    }

    //栅格    移动桌面端组合
    @RequestMapping("/component/grid/mobile-pc.html")
    public String componentGridMobilePc() {
        return "component/grid/mobile-pc";
    }

    //栅格    全端复杂组合
    @RequestMapping("/component/grid/all.html")
    public String componentGridAll() {
        return "component/grid/all";
    }

    //栅格    低于桌面堆叠排列
    @RequestMapping("/component/grid/stack.html")
    public String componentGridStack() {
        return "component/grid/stack";
    }

    //栅格    九宫格
    @RequestMapping("/component/grid/speed-dial.html")
    public String componentGridSpeedDial() {
        return "component/grid/speed-dial";
    }

    //按钮
    @RequestMapping("/component/button/index.html")
    public String componentButtonIndex() {
        return "component/button/index";
    }

    //表单    表单元素
    @RequestMapping("/component/form/element.html")
    public String componentFormElement() {
        return "component/form/element";
    }

    //按钮    表单组合
    @RequestMapping("/component/form/group.html")
    public String componentFormGroup() {
        return "component/form/group";
    }

    //导航
    @RequestMapping("/component/nav/index.html")
    public String componentNavIndex() {
        return "component/nav/index";
    }

    //选项卡
    @RequestMapping("/component/tabs/index.html")
    public String componentTabsIndex() {
        return "component/tabs/index";
    }

    //进度条
    @RequestMapping("/component/progress/index.html")
    public String componentProgressIndex() {
        return "component/progress/index";
    }

    //面板
    @RequestMapping("/component/panel/index.html")
    public String componentPanelIndex() {
        return "component/panel/index";
    }

    //徽章
    @RequestMapping("/component/badge/index.html")
    public String componentBadgeIndex() {
        return "component/badge/index";
    }

    //时间线
    @RequestMapping("/component/timeline/index.html")
    public String componentTimelineIndex() {
        return "component/timeline/index";
    }

    //动画
    @RequestMapping("/component/anim/index.html")
    public String componentAnimIndex() {
        return "component/anim/index";
    }

    //辅助
    @RequestMapping("/component/auxiliar/index.html")
    public String componentAuxiliarIndex() {
        return "component/auxiliar/index";
    }

    //通用弹层  功能演示
    @RequestMapping("/component/layer/list.html")
    public String componentLayerList() {
        return "component/layer/list";
    }

    //通用弹层  特殊示例
    @RequestMapping("/component/layer/special-demo.html")
    public String componentLayerSpecialDemo() {
        return "component/layer/special-demo";
    }

    //通用弹层  风格定制
    @RequestMapping("/component/layer/theme.html")
    public String componentLayerTheme() {
        return "component/layer/theme";
    }

    //日期时间  功能演示一
    @RequestMapping("/component/laydate/demo1.html")
    public String componentLaydateDemo1() {
        return "component/laydate/demo1";
    }

    //日期时间  功能演示二
    @RequestMapping("/component/laydate/demo2.html")
    public String componentLaydateDemo2() {
        return "component/laydate/demo2";
    }

    //日期时间  设定主题
    @RequestMapping("/component/laydate/theme.html")
    public String componentLaydateTheme() {
        return "component/laydate/theme";
    }

    //日期时间  特殊示例
    @RequestMapping("/component/laydate/special-demo.html")
    public String componentLaydateSpecialDemo() {
        return "component/laydate/special-demo";
    }

    //静态表格
    @RequestMapping("/component/table/static.html")
    public String componentTableStatic() {
        return "component/table/static";
    }

    //数据表格  简单数据表格
    @RequestMapping("/component/table/simple.html")
    public String componentTableSimple() {
        return "component/table/simple";
    }

    //数据表格  列宽自动分配
    @RequestMapping("/component/table/auto.html")
    public String componentTableAuto() {
        return "component/table/auto";
    }

    //数据表格  赋值已知数据
    @RequestMapping("/component/table/data.html")
    public String componentTableData() {
        return "component/table/data";
    }

    //数据表格  转化静态表格
    @RequestMapping("/component/table/tostatic.html")
    public String componentTableTostatic() {
        return "component/table/tostatic";
    }

    //数据表格  开启分页
    @RequestMapping("/component/table/page.html")
    public String componentTablePage() {
        return "component/table/page";
    }

    //数据表格  自定义分页
    @RequestMapping("/component/table/resetPage.html")
    public String componentTableResetPage() {
        return "component/table/resetPage";
    }

    //数据表格  开启头部工具栏
    @RequestMapping("/component/table/toolbar.html")
    public String componentTableToolbar() {
        return "component/table/toolbar";
    }

    //数据表格  开启合计行
    @RequestMapping("/component/table/totalRow.html")
    public String componentTableTotalRow() {
        return "component/table/totalRow";
    }

    //数据表格  高度最大适应
    @RequestMapping("/component/table/height.html")
    public String componentTableHeight() {
        return "component/table/height";
    }

    //数据表格  开启复选框
    @RequestMapping("/component/table/checkbox.html")
    public String componentTableCheckbox() {
        return "component/table/checkbox";
    }

    //数据表格  开启单选框
    @RequestMapping("/component/table/radio.html")
    public String componentTableRadio() {
        return "component/table/radio";
    }

    //数据表格  开启单元格编辑
    @RequestMapping("/component/table/cellEdit.html")
    public String componentTableCellEdit() {
        return "component/table/cellEdit";
    }

    //数据表格  加入表单元素
    @RequestMapping("/component/table/form.html")
    public String componentTableForm() {
        return "component/table/form";
    }

    //数据表格  设置单元格样式
    @RequestMapping("/component/table/style.html")
    public String componentTableStyle() {
        return "component/table/style";
    }

    //数据表格  固定列
    @RequestMapping("/component/table/fixed.html")
    public String componentTableFixed() {
        return "component/table/fixed";
    }

    //数据表格  数据操作
    @RequestMapping("/component/table/operate.html")
    public String componentTableOperate() {
        return "component/table/operate";
    }

    //数据表格  解析任意数据格式
    @RequestMapping("/component/table/parseData.html")
    public String componentTableParseData() {
        return "component/table/parseData";
    }

    //数据表格  监听行事件
    @RequestMapping("/component/table/onrow.html")
    public String componentTableOnrow() {
        return "component/table/onrow";
    }

    //数据表格  数据表格的重载
    @RequestMapping("/component/table/reload.html")
    public String componentTableReload() {
        return "component/table/reload";
    }

    //数据表格  设置初始排序
    @RequestMapping("/component/table/initSort.html")
    public String componentTableInitSort() {
        return "component/table/initSort";
    }

    //数据表格  监听单元格事件
    @RequestMapping("/component/table/cellEvent.html")
    public String componentTableCellEvent() {
        return "component/table/cellEvent";
    }

    //数据表格  复杂表头
    @RequestMapping("/component/table/thead.html")
    public String componentTableThead() {
        return "component/table/thead";
    }

    //分页  功能演示一
    @RequestMapping("/component/laypage/demo1.html")
    public String componentLayageDemo1() {
        return "component/laypage/demo1";
    }

    //分页  功能演示二
    @RequestMapping("/component/laypage/demo2.html")
    public String componentLayageDemo2() {
        return "component/laypage/demo2";
    }

    //上传  功能演示一
    @RequestMapping("/component/upload/demo1.html")
    public String componentUploadDemo1() {
        return "component/upload/demo1";
    }

    //上传  功能演示二
    @RequestMapping("/component/upload/demo2.html")
    public String componentUploadDemo2() {
        return "component/upload/demo2";
    }

    //颜色选择器
    @RequestMapping("/component/colorpicker/index.html")
    public String componentColorpickerIndex() {
        return "component/colorpicker/index";
    }

    //滑块组件
    @RequestMapping("/component/slider/index.html")
    public String componentSliderIndex() {
        return "component/slider/index";
    }

    //评分
    @RequestMapping("/component/rate/index.html")
    public String componentRateIndex() {
        return "component/rate/index";
    }

    //轮播
    @RequestMapping("/component/carousel/index.html")
    public String componentCarouselIndex() {
        return "component/carousel/index";
    }

    //流加载
    @RequestMapping("/component/flow/index.html")
    public String componentFlowIndex() {
        return "component/flow/index";
    }

    //工具
    @RequestMapping("/component/util/index.html")
    public String componentUtilIndex() {
        return "component/util/index";
    }

    //代码修饰
    @RequestMapping("/component/code/index.html")
    public String componentCodeIndex() {
        return "component/code/index";
    }

    //页面    个人主页
    @RequestMapping("/template/personalpage.html")
    public String templatePersonalpage() {
        return "template/personalpage";
    }

    //页面    通讯录
    @RequestMapping("/template/addresslist.html")
    public String templateAddresslist() {
        return "template/addresslist";
    }

    //页面    客户列表
    @RequestMapping("/template/caller.html")
    public String templateCaller() {
        return "template/caller";
    }

    //页面    商品列表
    @RequestMapping("/template/goodslist.html")
    public String templateGoodslist() {
        return "template/goodslist";
    }

    //页面    留言板
    @RequestMapping("/template/msgboard.html")
    public String templateMsgboard() {
        return "template/msgboard";
    }

    //页面    注册
    @RequestMapping("/user/reg.html")
    public String userReg() {
        return "user/reg";
    }

    //页面    忘记密码
    @RequestMapping("/user/forget.html")
    public String userForget() {
        return "user/forget";
    }

    //页面    404页面不存在
    @RequestMapping("/template/tips/404.html")
    public String templateTips404() {
        return "template/tips/404";
    }

    //页面    错误提示
    @RequestMapping("/template/tips/error.html")
    public String templateTipsError() {
        return "template/tips/error";
    }

    //应用    内容系统    文章列表
    @RequestMapping("/app/content/list.html")
    public String appContentList() {
        return "app/content/list";
    }

    //应用    内容系统    分类管理
    @RequestMapping("/app/content/tags.html")
    public String appContentTags() {
        return "app/content/tags";
    }

    //应用    内容系统    评论管理
    @RequestMapping("/app/content/comment.html")
    public String appContentComment() {
        return "app/content/comment";
    }

    //应用    社区系统    帖子列表
    @RequestMapping("/app/forum/list.html")
    public String appForumList() {
        return "app/forum/list";
    }

    //应用    社区系统    回帖列表
    @RequestMapping("/app/forum/replys.html")
    public String appForumReplys() {
        return "app/forum/replys";
    }

    //应用    消息中心
    @RequestMapping("/app/message/index.html")
    public String appMessageIndex() {
        return "app/message/index";
    }

    //应用    工单系统
    @RequestMapping("/app/workorder/list.html")
    public String appWorkorderList() {
        return "app/workorder/list";
    }

    //高级    Echarts集成   折线图
    @RequestMapping("/senior/echarts/line.html")
    public String seniorEchartsLine() {
        return "senior/echarts/line";
    }

    //高级    Echarts集成   柱状图
    @RequestMapping("/senior/echarts/bar.html")
    public String seniorEchartsBar() {
        return "senior/echarts/bar";
    }

    //高级    Echarts集成   地图
    @RequestMapping("/senior/echarts/map.html")
    public String seniorEchartsMap() {
        return "senior/echarts/map";
    }

    //用户    编辑用户
    @RequestMapping("/user/user/list.html")
    public String userUserList() {
        return "user/user/list";
    }

    //用户    网站用户
    @RequestMapping("/user/user/update.html")
    public String userUserUpdate() {
        return "user/user/update_user";
    }

    //用户    后台管理员
    @RequestMapping("/user/administrators/list.html")
    public String userAdministratorsList() {
        return "user/administrators/list";
    }

    //用户    角色管理
    @RequestMapping("/user/administrators/role.html")
    public String userAdministratorsRole() {
        return "user/administrators/role";
    }

    //设置    系统设置    网站设置
    @RequestMapping("/set/system/website.html")
    public String setSystemWebsite() {
        return "set/system/website";
    }

    //设置    系统设置    邮件服务
    @RequestMapping("/set/system/email.html")
    public String setSystemEmail() {
        return "set/system/email";
    }

    //设置    我的设置    基本资料
    @RequestMapping("/set/user/info.html")
    public String setUserInfo() {
        return "set/user/info";
    }

    //设置    我的设置    修改密码
    @RequestMapping("/set/user/password.html")
    public String setUserPassword() {
        return "set/user/password";
    }

}
