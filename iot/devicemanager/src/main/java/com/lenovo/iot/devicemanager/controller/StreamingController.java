package com.lenovo.iot.devicemanager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.devicemanager.model.Device;
import com.lenovo.iot.devicemanager.model.DeviceShadowItem;
import com.lenovo.iot.devicemanager.model.Filter;
import com.lenovo.iot.devicemanager.model.MqttMessage;
import com.lenovo.iot.devicemanager.model.bo.*;
import com.lenovo.iot.devicemanager.model.vo.Messenger;
import com.lenovo.iot.devicemanager.model.vo.StreamingPipeLineMapVO;
import com.lenovo.iot.devicemanager.mqtt.EmqttInstance;
import com.lenovo.iot.devicemanager.service.*;
import com.lenovo.iot.devicemanager.service.impl.StreamingMirrorRulerServiceImpl;
import com.lenovo.iot.devicemanager.util.LoginedAccountHolder;
import com.lenovo.iot.devicemanager.util.ReadProperties;
import com.lenovo.iot.devicemanager.util.SessionUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static com.lenovo.iot.devicemanager.mqtt.EmqttInstance.buildFilter;

@RequestMapping("/StreamingPipeLineController")
@Controller
public class StreamingController {

    protected static final Log log = LogFactory.getLog(StreamingController.class);

    @Autowired
    private IStreamingService _StreamingServiceImpl;

    @Autowired
    private IStreamingDeviceAppService _StreamingDeviceAppServiceImpl;

    @Autowired
    private IStreamingMirrorAppsourceVOService _StreamingMirrorAppsourceVOServiceImpl;

    @Autowired
    private IRulerService _RulerServiceImpl;


    @Autowired
    private IStreamingMirrorRulerService _StreamingMirrorRulerServiceImpl;


    @Autowired
    private IAppService _AppServiceImpl;

    @Autowired
    private DeviceService deviceService;
    
    @Autowired
    private ITaskService _TaskServiceImpl;
    
    @Autowired
    private EmqttInstance emqttInstance;
    
    // 新增数据流，包括Map信息集合和Filter集合集合
    @RequestMapping(value = "/nurturedStreamingPipeLine.do")
    @ResponseBody
    @CrossOrigin(origins="*")
    @RequiresPermissions({"datastream:manage"})
    public JSONObject nurturedStreamingPipeLine(
            @RequestBody String jsonString,
            HttpServletRequest _HttpServletRequest,
            HttpServletResponse _HttpServletResponse){

        JSONObject _JSONObjectResult =  new JSONObject();
        JSONObject _CreatestreamingpipelinemapjsonFinal = (JSONObject) JSONObject.parse(jsonString);

        //insert db
        // step_1 streaming
        // step_2streaming_device_app
        JSONObject _JSONObjectstepFirstCreateStreaming = _CreatestreamingpipelinemapjsonFinal.getJSONObject("stepFirstCreateStreaming");

        String _StepFirst__streamingname = _JSONObjectstepFirstCreateStreaming.getString("streamingname");
        String _StepFirst__streamingdesc = _JSONObjectstepFirstCreateStreaming.getString("streamingdesc");
        Long _StepFirst__deviceid = _JSONObjectstepFirstCreateStreaming.getLong("deviceid");
        if(_StepFirst__deviceid == 0){
            return null;
        }
        //_StepFirst__deviceid
        Device _DeviceGloable = deviceService.getDeviceByPrimaryKey(Long.valueOf(_StepFirst__deviceid));
        if(null == _DeviceGloable){
            return null;
        }

        String device_id__Gloable = _DeviceGloable.getDevice_id();
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();

        // 插入 streaming
        StreamingVO _StreamingVO = new StreamingVO();
        _StreamingVO.setStreamingname(_StepFirst__streamingname);
        _StreamingVO.setStreamingdesc(_StepFirst__streamingdesc);
        _StreamingVO.setCreatetimeat(System.currentTimeMillis());
        StreamingVO _stepFirstCallback__StreamingVO = _StreamingServiceImpl.doInsertStreaming(_StreamingVO);

        JSONObject _JSONObject_stepSecondStreamingBindingApp = _CreatestreamingpipelinemapjsonFinal.getJSONObject("stepSecondStreamingBindingApp");

//        Long appid_willBindStreaming = _JSONObject_stepSecondStreamingBindingApp.getLong("appid");
        String app_name = _JSONObject_stepSecondStreamingBindingApp.getString("appname");
        if(null == app_name){
            return null;
        }

        //采样频率
        Long samplingperiodLong = _JSONObject_stepSecondStreamingBindingApp.getLong("samplingperiod");
        //采样频率单位 默认 seconds
        String samplingunit = _JSONObject_stepSecondStreamingBindingApp.getString("samplingunit");

        StreamingDeviceAppVO _StreamingDeviceAppVO = new StreamingDeviceAppVO();
        _StreamingDeviceAppVO.setStreamingid(_stepFirstCallback__StreamingVO.getId());
        _StreamingDeviceAppVO.setDeviceid(_StepFirst__deviceid);
        _StreamingDeviceAppVO.setCompanyid(company_id);
//        _StreamingDeviceAppVO.setAppid(appid_willBindStreaming);
        _StreamingDeviceAppVO.setAppname(app_name);
        _StreamingDeviceAppVO.setIslive("true");
        _StreamingDeviceAppVO.setPeriod(samplingperiodLong);
        _StreamingDeviceAppVO.setPeriodunit(samplingunit);
        _StreamingDeviceAppVO.setCreatetimeat(System.currentTimeMillis());//添加创建时间
        
        net.sf.json.JSONObject app = deviceService.get_app(device_id__Gloable, app_name);
		if(app != null) {
			_StreamingDeviceAppVO.setAppversion(app.optString("app_version"));
			_StreamingDeviceAppVO.setAppdesc(app.optString("desc"));
		}

        //check streamingid + deviceid
//        StreamingCallbackFlight _StreamingCallbackFlightParameter = new StreamingCallbackFlight();
//        _StreamingCallbackFlightParameter.setAppid(String.valueOf(_stepFirstCallback__StreamingVO.getId()));
//        _StreamingCallbackFlightParameter.setDeviceid(_StepFirst__deviceid);
//        List<StreamingCallbackFlight> List_StreamingCallbackFlight__check =  _StreamingDeviceAppServiceImpl.findStreamingDeviceAppVOBaseById(_StreamingCallbackFlightParameter);
//        if(null != List_StreamingCallbackFlight__check && List_StreamingCallbackFlight__check.size()>0){
//            _JSONObjectResult.put("actionFlag",false);
//            _JSONObjectResult.put("actionMessage","this app already runing on this deviceid");
//            return _JSONObjectResult;
//        }

        // 插入 streaming_device_app
        StreamingDeviceAppVO _stepFirstCallback__StreamingDeviceApp = _StreamingDeviceAppServiceImpl.doInsertStreamingDeviceApp(_StreamingDeviceAppVO);

//        StreamingDeviceAppVO _StreamingDeviceAppVOUpdate = new StreamingDeviceAppVO();
//        _StreamingDeviceAppVOUpdate.setId(_stepFirstCallback__StreamingDeviceApp.getId());
//        _StreamingDeviceAppVOUpdate.setAppid(appid_willBindStreaming);
//        _StreamingDeviceAppServiceImpl.doUpdateStreamingDeviceAppVO(_StreamingDeviceAppVOUpdate);

        //streamingmirrorappsource

        JSONArray _JSONArraystepThirdStreamingBindAppSource = _CreatestreamingpipelinemapjsonFinal.getJSONArray("stepThirdStreamingBindAppSource");
        // 插入 streamingmirrorappsource
        for(int i = 0 ; i < _JSONArraystepThirdStreamingBindAppSource.size() ; i++){
            JSONObject currentJSONObject_APPSource = _JSONArraystepThirdStreamingBindAppSource.getJSONObject(i);

//            Long current_appsourceid = currentJSONObject_APPSource.getLong("appsourceid");
            String current_sourcename = currentJSONObject_APPSource.getString("sourcename");
            String current_mapname = currentJSONObject_APPSource.getString("mapname");
            String current_mapdisplayname = currentJSONObject_APPSource.getString("mapdisplayname");
            String current_sourcedefault = currentJSONObject_APPSource.getString("current_sourcedefault");
            String current_sourcedatatype = currentJSONObject_APPSource.getString("current_sourcedatatype");
            String current_sourceunit = currentJSONObject_APPSource.getString("current_sourceunit");

            StreamingMirrorAppsourceVO _StreamingMirrorAppsourceVO = new StreamingMirrorAppsourceVO();
            _StreamingMirrorAppsourceVO.setStreamingdevappbriprimarykey(_stepFirstCallback__StreamingDeviceApp.getId());
//            _StreamingMirrorAppsourceVO.setAppsourceid(current_appsourceid);
            _StreamingMirrorAppsourceVO.setSourcename(current_sourcename);
            _StreamingMirrorAppsourceVO.setMapname(current_mapname);
            _StreamingMirrorAppsourceVO.setMapdisplayname(current_mapdisplayname);
            _StreamingMirrorAppsourceVO.setSourcedefault(current_sourcedefault);
            _StreamingMirrorAppsourceVO.setSourcedatatype(current_sourcedatatype);
            _StreamingMirrorAppsourceVO.setSourceunit(current_sourceunit);

            _StreamingMirrorAppsourceVOServiceImpl.doInsertStreamingMirrorAppsourceVO(_StreamingMirrorAppsourceVO);
        }

        // 插入 streamingmirrorruler
        JSONArray _JSONArrayOneTranscation = _CreatestreamingpipelinemapjsonFinal.getJSONArray("stepForthStreamingBindRuler");

        for(int i = 0 ; i < _JSONArrayOneTranscation.size() ; i++){

            JSONArray currentRuler_JSONObject = _JSONArrayOneTranscation.getJSONArray(i);

            String seed = String.valueOf(Math.random());
            String groupId = String.valueOf(System.currentTimeMillis())+seed;

            for(int j = 0 ; j < currentRuler_JSONObject.size() ; j++){
                JSONObject _CurrentJSONObject__Ruler = currentRuler_JSONObject.getJSONObject(j);
                _CurrentJSONObject__Ruler.put("groupid",groupId);
                _CurrentJSONObject__Ruler.put("groupinnerindex",j);

                RulerVO currentRulerVO = JSONObject.toJavaObject(_CurrentJSONObject__Ruler,RulerVO.class);

                RulerVO currentRulerVOCallBack = _RulerServiceImpl.doInsertruler(currentRulerVO);

                log.info("currentRulerVOCallBack.getId()--->>>"+currentRulerVOCallBack.getId());

            }

            //streamingbindruler
            StreamingMirrorRulerVO _StreamingMirrorRulerVO = new StreamingMirrorRulerVO();
            _StreamingMirrorRulerVO.setStreamingdevappbriprimarykey(_stepFirstCallback__StreamingDeviceApp.getId());
            _StreamingMirrorRulerVO.setRulergroupid(groupId);
            StreamingMirrorRulerVO StreamingMirrorRulerVOCallBack =_StreamingMirrorRulerServiceImpl.doInsertStreamingMirrorRuler(_StreamingMirrorRulerVO);
            log.info("StreamingMirrorRulerVOCallBack.getId()--->>>"+StreamingMirrorRulerVOCallBack.getId());
        }

        //build map
//        SELECT t.id ,t.streamingdevappbriprimarykey ,tt.appname,tt.sourcename,t.mapname from streamingmirrorappsource t
//        LEFT JOIN app_source tt ON t.appsourceid = tt.id
//        LEFT JOIN app ttt ON t.appid=ttt.id WHERE t.streamingdevappbriprimarykey = '26'
        //bridgekey
        //getAppName

        StreamingDeviceAppVO _StreamingDeviceAppVOToFixed = new StreamingDeviceAppVO();
        _StreamingDeviceAppVOToFixed.setId(_stepFirstCallback__StreamingDeviceApp.getId());
        List<StreamingDeviceAppVO> listForApacheAdgentMetaData = _StreamingDeviceAppServiceImpl.findStreamingDeviceAppVOByParameter(_StreamingDeviceAppVOToFixed);

        StreamingDeviceAppVO _StreamingDeviceAppVOGloable = listForApacheAdgentMetaData.get(0);

//        long appidToFixed =_StreamingDeviceAppVOGloable.getAppid();
//
//        App _AppToFixed = new App();
//        _AppToFixed.setId(appidToFixed);
//        List<App> listAppToFixed = _AppServiceImpl.findAppByParameter(_AppToFixed);
//        App appFixed = listAppToFixed.get(0);
//        String appName_Rogerthat = appFixed.getAppname();
//        String appType = appFixed.getApptype();

        //samplingperiod send to mqtt
//        StreamingMirrorAppsourceVO _StreamingMirrorAppsourceVO = new StreamingMirrorAppsourceVO();
//        _StreamingMirrorAppsourceVO.setStreamingdevappbriprimarykey(_stepFirstCallback__StreamingDeviceApp.getId());
//        List<StreamingMirrorAppsourceVO> list4StreamingMirrorAppsourceVOToCreateMapMqtt = _StreamingMirrorAppsourceVOServiceImpl.findStreamingMirrorAppsourceVOByParameter(_StreamingMirrorAppsourceVO);

        String _Topic__map = "$MOC/control/" + device_id__Gloable + "/map/update";
        String _Topic__filter = "$MOC/control/" + device_id__Gloable + "/filter/update";
        String _Topic__samplingperiod = "$MOC/control/" + device_id__Gloable + "/app/config";

        //构建mapclassstr 并且  send mqtt
        JSONObject JSONObject_Map__keyAppsourcename__valueMapname = new JSONObject();
        for(int i = 0 ; i < _JSONArraystepThirdStreamingBindAppSource.size() ; i++){
            JSONObject currentJSONObject_APPSource = _JSONArraystepThirdStreamingBindAppSource.getJSONObject(i);

            String current_sourcename = currentJSONObject_APPSource.getString("sourcename");
            String current_mapname = currentJSONObject_APPSource.getString("mapname");
            JSONObject_Map__keyAppsourcename__valueMapname.put(current_sourcename, current_mapname);
        }
        String message4MapClassCodeStr = EmqttInstance.buildMap(app_name, JSONObject_Map__keyAppsourcename__valueMapname.toJSONString());

        //发送map
		MqttMessage map_msg = new MqttMessage();
		map_msg.setTopic(_Topic__map);
		map_msg.setMessage(message4MapClassCodeStr);
		map_msg.setQos(0);
		map_msg.setDevice_id(device_id__Gloable);
		map_msg.setApp_name(app_name);
		map_msg.setApp_type("stream");
		boolean result4pulishMessage_Map = emqttInstance.pulishMessage(map_msg);

        //构建filterclassstr 并且  send mqtt
        //sendmessage to apacheEdgentMetadata by mqtt
        Filter[] _FilterArray = null;
        List<Filter> _FilterList = new ArrayList<Filter>();

        StreamingMirrorRulerVO _StreamingMirrorRulerVO = new StreamingMirrorRulerVO();
        _StreamingMirrorRulerVO.setStreamingdevappbriprimarykey(_StreamingDeviceAppVOGloable.getId());
        List<StreamingMirrorRulerVO> listForStreamingMirrorRulerVO = _StreamingMirrorRulerServiceImpl.findStreamingMirrorRulerVOByParameter(_StreamingMirrorRulerVO);

        for(int i = 0 ; i < listForStreamingMirrorRulerVO.size(); i++){
            StreamingMirrorRulerVO currentStreamingMirrorRulerVO = listForStreamingMirrorRulerVO.get(i);
            String currentGroupid = currentStreamingMirrorRulerVO.getRulergroupid();
            RulerVO _RulerVOForParameter = new RulerVO();
            _RulerVOForParameter.setGroupid(currentGroupid);
            List<RulerVO> listForRulerVO = _RulerServiceImpl.findRulerByParameter(_RulerVOForParameter);

            for(int j = 0 ; j < listForRulerVO.size(); j++ ){
                RulerVO currentRulerVO = listForRulerVO.get(j);
                String propertyName = currentRulerVO.getAttributename();
                String dataType =  currentRulerVO.getAttributevaluetype();
                String operator = currentRulerVO.getMathematicalsymbol();
                String value = currentRulerVO.getRightvalue();
                String conjunction = currentRulerVO.getLogicalsymbol();
                Filter currentFilter = null;
//                if(listForRulerVO.size()>1){
//                    currentFilter = new Filter(propertyName,dataType,operator,value,conjunction);
//
//                }else{
//                    currentFilter = new Filter(propertyName,dataType,operator,value);
//                }
                if(i == listForStreamingMirrorRulerVO.size()-1 && j== listForRulerVO.size()-1){
                    currentFilter = new Filter(propertyName,dataType,operator,value);
                }else{
                    currentFilter = new Filter(propertyName,dataType,operator,value,conjunction);
                }

                _FilterList.add(currentFilter);
            }

        }

        _FilterArray =  _FilterList.toArray(new Filter[0]);

        String message4FilterClassCodeStr = EmqttInstance.buildFilter(app_name, _FilterArray);

		MqttMessage filter_msg = new MqttMessage();
		filter_msg.setTopic(_Topic__filter);
		filter_msg.setMessage(message4FilterClassCodeStr);
		filter_msg.setQos(0);
		filter_msg.setDevice_id(device_id__Gloable);
		filter_msg.setApp_name(app_name);
		filter_msg.setApp_type("stream");
		boolean result4pulishMessage_Filter = emqttInstance.pulishMessage(filter_msg);

		//samplingperiod
        //"app_name": "app.topology.services.AdlinkStreamApp","config": {"period": 1,"unit": "SECONDS"}}
        JSONObject JSONObject__samplingperiod = new JSONObject();
        JSONObject _JSONObject_config = new JSONObject();
        _JSONObject_config.put("period",samplingperiodLong);
        _JSONObject_config.put("unit",samplingunit);
        JSONObject__samplingperiod.put("app_name",app_name);
        JSONObject__samplingperiod.put("config",_JSONObject_config);

		String messageSamplingperiod = JSONObject__samplingperiod.toJSONString();
		MqttMessage samplingperiod_msg = new MqttMessage();
		samplingperiod_msg.setTopic(_Topic__samplingperiod);
		samplingperiod_msg.setMessage(messageSamplingperiod);
		samplingperiod_msg.setQos(0);
		samplingperiod_msg.setDevice_id(device_id__Gloable);
		samplingperiod_msg.setApp_name(app_name);
		samplingperiod_msg.setApp_type("stream");
		boolean result4pulishMessage_samplingperiod = emqttInstance.pulishMessage(map_msg);

        //启动应用
        if(_TaskServiceImpl.run_app(device_id__Gloable, app_name, true)) {
        	StreamingDeviceAppVO _StreamingDeviceAppVOUpdate = new StreamingDeviceAppVO();
        	_StreamingDeviceAppVOUpdate.setId(_stepFirstCallback__StreamingDeviceApp.getId());
	        _StreamingDeviceAppVOUpdate.setStatus("successSendMessageToMqtt");
	        _StreamingDeviceAppServiceImpl.doUpdateStreamingDeviceAppVO(_StreamingDeviceAppVOUpdate);
        }


        _JSONObjectResult.put("result4pulishMessage_Map",result4pulishMessage_Map);
        _JSONObjectResult.put("result4pulishMessage_Filter",result4pulishMessage_Filter);
        _JSONObjectResult.put("result4pulishMessage_samplingperiod",result4pulishMessage_samplingperiod);
        _JSONObjectResult.put("streamingid",_stepFirstCallback__StreamingDeviceApp.getId());

        _JSONObjectResult.put("actionFlag",true);

        return _JSONObjectResult;
    }

    // 查询数据流列表
    @RequestMapping(value = "/findStreamingDeviceAppVOBaseByParameters.do")
    @ResponseBody
    @CrossOrigin(origins="*")
    @RequiresPermissions({"datastream:manage"})
    public JSONObject findStreamingDeviceAppVOBaseByParameters(@RequestBody StreamingCallbackFlight _StreamingCallbackFlight,
                                                        HttpServletRequest _HttpServletRequest,
                                                        HttpServletResponse _HttpServletResponse) {
    	List<StreamingCallbackFlight> listForStreamingCallbackFlight;
    	Integer total_count;
    	
		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		if(company_id > 0) {
			_StreamingCallbackFlight.setCompanyid(company_id);
	   	
	        _StreamingCallbackFlight.setIslive("false");
	        if(_StreamingCallbackFlight.getAccesskey() != null && !_StreamingCallbackFlight.getAccesskey().isEmpty()) {
	        	_StreamingCallbackFlight.setAccesskey("%" + _StreamingCallbackFlight.getAccesskey().trim() + "%");
	        }
	        
	        //总记录数
	        total_count = _StreamingDeviceAppServiceImpl.findStreamingDeviceAppVOBaseCountByParameters(_StreamingCallbackFlight);
	        
	        //排序
	        if(_StreamingCallbackFlight.getSort_orderby() == null || _StreamingCallbackFlight.getSort_orderby().isEmpty()) {
	        	_StreamingCallbackFlight.setSort_orderby("t.createtimeat");
	        } else if(_StreamingCallbackFlight.getSort_orderby().equalsIgnoreCase("createtimeatshow")) {
	        	_StreamingCallbackFlight.setSort_orderby("t.createtimeat");
	        } else if(_StreamingCallbackFlight.getSort_orderby().equalsIgnoreCase("streamingname")) {
	        	_StreamingCallbackFlight.setSort_orderby("tt.streamingname");
	        } else if(_StreamingCallbackFlight.getSort_orderby().equalsIgnoreCase("accesskey")) {
	        	_StreamingCallbackFlight.setSort_orderby("ttt.access_key");
	        } else if(_StreamingCallbackFlight.getSort_orderby().equalsIgnoreCase("appname")) {
	        	_StreamingCallbackFlight.setSort_orderby("t.appname");
	        } else {
	        	_StreamingCallbackFlight.setSort_orderby("t.createtimeat");
	        }
	        
	        if(_StreamingCallbackFlight.getSort_rule() == null || _StreamingCallbackFlight.getSort_rule().isEmpty() || _StreamingCallbackFlight.getSort_rule().equalsIgnoreCase("descending")) {
	        	_StreamingCallbackFlight.setSort_rule("desc");
	        } else {
	        	_StreamingCallbackFlight.setSort_rule("asc");
	        }
	        
	        //分页
		    if(_StreamingCallbackFlight.getCurrent() == 0) {
		    	_StreamingCallbackFlight.setCurrent(1);
		    }
		    if(_StreamingCallbackFlight.getPagesize() == 0) {
		    	_StreamingCallbackFlight.setPagesize(20);
		    }
	        _StreamingCallbackFlight.setLimitStart((_StreamingCallbackFlight.getCurrent() - 1) * _StreamingCallbackFlight.getPagesize());
	        _StreamingCallbackFlight.setLimitEnd(_StreamingCallbackFlight.getPagesize());

	        listForStreamingCallbackFlight = _StreamingDeviceAppServiceImpl.findStreamingDeviceAppVOBaseByParameters(_StreamingCallbackFlight);
		} else {
			listForStreamingCallbackFlight = new ArrayList<StreamingCallbackFlight>();
			total_count = 0;
		}
		
        JSONObject _JSONObjectResult = new JSONObject();
        _JSONObjectResult.put("actionResult",true);
        _JSONObjectResult.put("actionResult",listForStreamingCallbackFlight);

        _JSONObjectResult.put("pagesize", _StreamingCallbackFlight.getPagesize());
        _JSONObjectResult.put("current", _StreamingCallbackFlight.getCurrent());
        _JSONObjectResult.put("total", total_count);

        return _JSONObjectResult;
    }

    // 删除数据流
    @RequestMapping(value = "/deleteStreaming.do")
    @ResponseBody
    @CrossOrigin(origins="*")
    @RequiresPermissions({"datastream:manage"})
    public JSONObject deleteStreaming(@RequestBody StreamingDeviceAppVO _StreamingDeviceAppVO,
                                                        HttpServletRequest _HttpServletRequest,
                                                        HttpServletResponse _HttpServletResponse) {
        JSONObject _JSONObjectResult = new JSONObject();

        if(null == _StreamingDeviceAppVO || _StreamingDeviceAppVO.getId() == 0 ){
            _JSONObjectResult.put("actionResult", false);
            return _JSONObjectResult;
        }

		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
        _StreamingDeviceAppServiceImpl.deleteStreamingDeviceApp(company_id, _StreamingDeviceAppVO.getId());
        _JSONObjectResult.put("actionResult", true);

        return _JSONObjectResult;

    }

    // 查询(单个)数据流
    @RequestMapping(value = "/findStreamingDeviceAppVOBaseById.do")
    @ResponseBody
    @CrossOrigin(origins="*")
    @RequiresPermissions({"datastream:manage"})
    public JSONObject findStreamingDeviceAppVOBaseById(@RequestBody StreamingCallbackFlight _StreamingCallbackFlight, HttpServletRequest _HttpServletRequest, HttpServletResponse _HttpServletResponse){

        JSONObject _JSONObjectResultFinal = new JSONObject();

		long company_id = LoginedAccountHolder.getLoginedAccount().getCompanyId();
		_StreamingCallbackFlight.setCompanyid(company_id);
		
        List<StreamingCallbackFlight> list4StreamingCallbackFlight =  _StreamingDeviceAppServiceImpl.findStreamingDeviceAppVOBaseById(_StreamingCallbackFlight);

        StreamingCallbackFlight _StreamingCallbackFlightCallback = list4StreamingCallbackFlight.get(0);
        _JSONObjectResultFinal.put("streaming",_StreamingCallbackFlightCallback);
        _StreamingCallbackFlight.setId(_StreamingCallbackFlightCallback.getTopid());

        //streamingmirrorappsource  map
        StreamingMirrorAppsourceVO _StreamingMirrorAppsourceVO = new StreamingMirrorAppsourceVO();
        _StreamingMirrorAppsourceVO.setStreamingdevappbriprimarykey(_StreamingCallbackFlight.getId());

        List<StreamingMirrorAppsourceVO> list4_StreamingMirrorAppsourceVO = _StreamingMirrorAppsourceVOServiceImpl.findStreamingMirrorAppsourceVOByParameter(_StreamingMirrorAppsourceVO);
        _JSONObjectResultFinal.put("map",list4_StreamingMirrorAppsourceVO);

        //streamingmirrorruler
        StreamingMirrorRulerVO _StreamingMirrorRulerVO = new StreamingMirrorRulerVO();
        _StreamingMirrorRulerVO.setStreamingdevappbriprimarykey(_StreamingCallbackFlight.getId());
        List<StreamingMirrorRulerVO> list4StreamingMirrorRulerVO = _StreamingMirrorRulerServiceImpl.findStreamingMirrorRulerVOByParameter(_StreamingMirrorRulerVO);

        List<List<RulerVO>> list_list__RulerVO = new ArrayList<List<RulerVO>>();

        for(int i = 0 ; i < list4StreamingMirrorRulerVO.size(); i++){

            StreamingMirrorRulerVO currentStreamingMirrorRulerVO = list4StreamingMirrorRulerVO.get(i);

            String groupId = currentStreamingMirrorRulerVO.getRulergroupid();

            RulerVO _RulerVO = new RulerVO();

            _RulerVO.setGroupid(groupId);

            List<RulerVO> list_RulerVO = _RulerServiceImpl.findRulerByParameter(_RulerVO);

            list_list__RulerVO.add(list_RulerVO);
        }

        _JSONObjectResultFinal.put("ruler",list_list__RulerVO);

        log.info("_JSONObjectResultFinal.toJSONString()--->>>\n"+_JSONObjectResultFinal.toJSONString());

        return _JSONObjectResultFinal;

    }

    
    
    
    
    
/*
    private JSONObject generateTestParameterJSON(JSONObject _CreatestreamingpipelinemapjsonFinal){

        JSONObject _JSONObject4FirstStepCreateStreaming = new JSONObject();

        _JSONObject4FirstStepCreateStreaming.put("streamingname","streamingnameIAMMYSELF20170831");//streaming 名称
        _JSONObject4FirstStepCreateStreaming.put("streamingdesc","streamingdescIAMMYSELF20170831");//streaming 描述
        _JSONObject4FirstStepCreateStreaming.put("deviceid",37);

        JSONObject _JSONObject4SecondStepCreateStreaming = new JSONObject();

        _JSONObject4SecondStepCreateStreaming.put("appid",85);



        _CreatestreamingpipelinemapjsonFinal.put("stepFirstCreateStreaming",_JSONObject4FirstStepCreateStreaming);

        //_JSONObject4SecondStepCreateStreaming
        _CreatestreamingpipelinemapjsonFinal.put("stepSecondStreamingBindingApp",_JSONObject4SecondStepCreateStreaming);


        //{"stepThirdBindingAppSource":[{appsourceid:"","showname":"","mapname","appsourcename":""}]}
        JSONArray _JSONArray4MapStreamingBindAppSource = new JSONArray();
        JSONObject _JSONObjectAppSourceMap_1 = new JSONObject();
        _JSONObjectAppSourceMap_1.put("appsourceid",70);
        _JSONObjectAppSourceMap_1.put("showname","showname_humidity");
        _JSONObjectAppSourceMap_1.put("mapname","mapname_humidity");

        JSONObject _JSONObjectAppSourceMap_2 = new JSONObject();
        _JSONObjectAppSourceMap_2.put("appsourceid",71);
        _JSONObjectAppSourceMap_2.put("showname","showname_temperature");
        _JSONObjectAppSourceMap_2.put("mapname","mapname_temperature");

        _JSONArray4MapStreamingBindAppSource.add(_JSONObjectAppSourceMap_1);
        _JSONArray4MapStreamingBindAppSource.add(_JSONObjectAppSourceMap_2);

        _CreatestreamingpipelinemapjsonFinal.put("stepThirdStreamingBindAppSource",_JSONArray4MapStreamingBindAppSource);


        //{"stepfourthstreamingbindruler":[[{}],[{},{}]]}
        JSONArray _JSONArray_Ruler = new JSONArray();

        JSONArray _JSONArray_Ruler_InnerElement_1 = new JSONArray();
        JSONObject _JSONObjectRuler_1 = new JSONObject();
        _JSONObjectRuler_1.put("attributename","mapname_humidity");
        _JSONObjectRuler_1.put("mathematicalsymbol","<");
        _JSONObjectRuler_1.put("rightvalue",50);
        _JSONObjectRuler_1.put("logicalsymbol","&&");
        _JSONObjectRuler_1.put("attributevaluetype","double");
        _JSONObjectRuler_1.put("appsourceid",70);//humidity湿度

        _JSONArray_Ruler_InnerElement_1.add(_JSONObjectRuler_1);

        JSONArray _JSONArray_Ruler_InnerElement_2 = new JSONArray();
        JSONObject _JSONObjectRuler_2 = new JSONObject();
        _JSONObjectRuler_2.put("attributename","mapname_temperature");
        _JSONObjectRuler_2.put("mathematicalsymbol",">");
        _JSONObjectRuler_2.put("rightvalue",20);
        _JSONObjectRuler_2.put("logicalsymbol","||");
        _JSONObjectRuler_2.put("attributevaluetype","double");
        _JSONObjectRuler_2.put("appsourceid",71);


        JSONObject _JSONObjectRuler_3 = new JSONObject();
        _JSONObjectRuler_3.put("attributename","mapname2");
        _JSONObjectRuler_3.put("mathematicalsymbol","<");
        _JSONObjectRuler_3.put("rightvalue",100);
        _JSONObjectRuler_3.put("logicalsymbol","||");
        _JSONObjectRuler_3.put("attributevaluetype","int");
        _JSONObjectRuler_3.put("appsourceid",58);

        _JSONArray_Ruler_InnerElement_2.add(_JSONObjectRuler_2);
       // _JSONArray_Ruler_InnerElement_2.add(_JSONObjectRuler_3);

        _JSONArray_Ruler.add(_JSONArray_Ruler_InnerElement_1);
        _JSONArray_Ruler.add(_JSONArray_Ruler_InnerElement_2);

        _CreatestreamingpipelinemapjsonFinal.put("stepForthStreamingBindRuler",_JSONArray_Ruler);

        log.info("_Createstreamingpipelinemapjson--->>>\n"+_CreatestreamingpipelinemapjsonFinal.toJSONString());

        return _CreatestreamingpipelinemapjsonFinal;


    }
*/

//
//    @RequestMapping(value = "/findAllStreamingDeviceAppVOBaseBy.do")
//    @ResponseBody
//    @CrossOrigin(origins="*")
//    public JSONObject findAllStreamingDeviceAppVOBaseBy(@RequestBody StreamingCallbackFlight _StreamingCallbackFlight,
//                                                    HttpServletRequest _HttpServletRequest,
//                                                    HttpServletResponse _HttpServletResponse) {
//        JSONObject _JSONObjectResult = new JSONObject();
//
//        List<StreamingCallbackFlight> listForStreamingCallbackFlight = _StreamingDeviceAppServiceImpl.findAllStreamingDeviceAppVOBaseBy(new StreamingCallbackFlight());
//
//        _JSONObjectResult.put("actionResult",true);
//        _JSONObjectResult.put("actionResult",listForStreamingCallbackFlight);
//
//        return _JSONObjectResult;
//
//    }













}
