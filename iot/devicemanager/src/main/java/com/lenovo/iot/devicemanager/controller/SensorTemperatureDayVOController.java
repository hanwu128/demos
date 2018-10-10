package com.lenovo.iot.devicemanager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.devicemanager.dao.SensorDao;
import com.lenovo.iot.devicemanager.model.Sensor_Label;
import com.lenovo.iot.devicemanager.model.Sensor_Temperature_Device;
import com.lenovo.iot.devicemanager.model.bo.SensorTemperatureDayVO;
import com.lenovo.iot.devicemanager.model.bo.SensorTemperatureHistory;
import com.lenovo.iot.devicemanager.service.ISensorTemperatureDayVOService;
import com.lenovo.iot.devicemanager.util.OpenTSDBClient;
import com.lenovo.iot.devicemanager.util.OpenTSDBResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by root on 2017/9/7.
 */
@RequestMapping("/SensorTemperatureDayVOController")
@Controller
public class SensorTemperatureDayVOController {

	private static final String TSD_URL = "http://172.17.201.239:4242/api";
    protected static final Log LOG = LogFactory.getLog(SensorTemperatureDayVOController.class);

    @Autowired
    private ISensorTemperatureDayVOService _SensorTemperatureDayVOServiceImpl;
	
	@Autowired
	private SensorDao sensorDao;
	
	private static  SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
	private static  SimpleDateFormat HHMMSS = new SimpleDateFormat("HH:mm:ss");
	private static  SimpleDateFormat HHMM = new SimpleDateFormat("HH:mm");
	private static  SimpleDateFormat YYYYMMDD_HHMM = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * http://localhost:8080/devicemanager/SensorTemperatureDayVOController/findSensorTemperatureDayVOByParameter.do
     *
     * Content-Type: application/json
       Host: localhost:8080
       Content-Length: 28

     {
     "what_day":"2017-09-06"
     }

     *
     *
     * @param _SensorTemperatureDayVO
     * @param _HttpServletRequest
     * @param _HttpServletResponse
     * @return
     */
    @RequestMapping(value = "/findSensorTemperatureDayVOByParameter.url")
    @ResponseBody
    @CrossOrigin(origins="*")
    public JSONObject findSensorTemperatureDayVOByParameter(
            @RequestBody SensorTemperatureDayVO _SensorTemperatureDayVO,
            HttpServletRequest _HttpServletRequest,
            HttpServletResponse _HttpServletResponse){
        JSONObject _JSONObjectResult = new JSONObject();

        String whichDay = _SensorTemperatureDayVO.getWhatday();

        if(null == whichDay || whichDay.trim().length()<=0){
            return null;
        }


        String[] whichDayArray = whichDay.split("-");

        StringBuffer sb = new StringBuffer();

        for(int i = 0 ; i < whichDayArray.length; i++){
            sb.append(whichDayArray[i]);
        }

        _SensorTemperatureDayVO.setWhatday(sb.toString());

        Integer count = _SensorTemperatureDayVOServiceImpl.findSensorTemperatureDayCountByParameter(_SensorTemperatureDayVO);

        int limitStart = (_SensorTemperatureDayVO.getCurrentpage()-1)*_SensorTemperatureDayVO.getRowcount();

        int limitEnd = _SensorTemperatureDayVO.getRowcount();

        _SensorTemperatureDayVO.setLimitStart(limitStart);

        _SensorTemperatureDayVO.setLimitEnd(limitEnd);

        List<SensorTemperatureDayVO> list_SensorTemperatureDayVO = _SensorTemperatureDayVOServiceImpl.findSensorTemperatureDayVOByParameter(_SensorTemperatureDayVO);

        _JSONObjectResult.put("actionFlag",false);
        _JSONObjectResult.put("actionResultData",list_SensorTemperatureDayVO);
        _JSONObjectResult.put("totalCount",count);
        return _JSONObjectResult;


    }

    /**
     *
     *
     * http://localhost:8080/devicemanager/SensorTemperatureDayVOController/bindReportDataSource.do
     *
     *   Content-Type: application/json
         Host: localhost:8080
         Content-Length: 51

         {
         "what_day":"2017-09-06",
         "label_id":"0916"
         }
     *
     *
     *
     *
     *
     *
     SELECT
     *
     FROM
     sensor_temperature_history
     WHERE
     unix_timestamp(create_stamp) > unix_timestamp(
     TIMESTAMP (date('2017-09-06'))
     )
     AND unix_timestamp(create_stamp) < unix_timestamp(
     TIMESTAMP (
     adddate(date('2017-09-06'), 1)
     )
     )
     AND label_id = '0916'

     select unix_timestamp(timestamp(date('2017-09-07'))),unix_timestamp(timestamp(adddate(date('2017-09-07'),1)));

     * @param _SensorTemperatureHistory
     * @param _HttpServletRequest
     * @param _HttpServletResponse
     * @return
     */
    @RequestMapping(value = "/bindReportDataSource.url")
    @ResponseBody
    @CrossOrigin(origins="*")
    public JSONObject getReportDataSource(
            @RequestBody SensorTemperatureHistory _SensorTemperatureHistory,
            HttpServletRequest _HttpServletRequest,
            HttpServletResponse _HttpServletResponse){
        JSONObject _JSONObjectResult = new JSONObject();


        List<SensorTemperatureHistory> list_SensorTemperatureHistory = _SensorTemperatureDayVOServiceImpl.
                findSensorTemperatureHistoryByParameters(_SensorTemperatureHistory);

        //List<Timestamp> listX_TimeAt = new ArrayList<Timestamp>();
        List<String> listX_TimeAt = new ArrayList<String>();
        List<Double> listY_oC = new ArrayList<Double>();
        for(int i = 0 ; i < list_SensorTemperatureHistory.size() ; i++){

            SensorTemperatureHistory current_SensorTemperatureHistory = list_SensorTemperatureHistory.get(i);

            Timestamp create_stamp = current_SensorTemperatureHistory.getCreatestamp();

            Date date = new Date(create_stamp.getTime());

            DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            String dateStr = sdf.format(date);

           // listX_TimeAt.add(create_stamp);

            listX_TimeAt.add(dateStr);

            Double currentValue = current_SensorTemperatureHistory.getValue();
            listY_oC.add(currentValue);
        }

        JSONObject jsonObject_DataSourcePackage = new JSONObject();

        jsonObject_DataSourcePackage.put("listX_TimeAt",listX_TimeAt);
        jsonObject_DataSourcePackage.put("listY_oC",listY_oC);


        _JSONObjectResult.put("actionFlag",false);
        _JSONObjectResult.put("actionResultData",jsonObject_DataSourcePackage);

        return _JSONObjectResult;


    }

    /**
     *
     * http://localhost:8080/devicemanager/SensorTemperatureDayVOController/descByPrimarykeyAndTimeat.do
     *   Content-Type: application/json
         Host: localhost:8080
         Content-Length: 83
     {
     "startTimeAt":"2017-09-07",
     "endTimeAt":"2017-09-08",
     "label_id":"0764"
     }
     *
     *
     *
     SELECT
     t.id,
     t.label_id,
     t.value,
     t.create_stamp,
     tt.label_name,
     tt.device_id,
     ttt.group_name
     FROM
     sensor_temperature_history t
     LEFT JOIN sensor_label tt
     ON t.label_id = tt.label_id
     LEFT JOIN sensor_group ttt ON tt.label_group = ttt.group_id

     WHERE
     1=1

     AND unix_timestamp(t.create_stamp) > unix_timestamp(TIMESTAMP(date('2017-09-07')))
     AND unix_timestamp(t.create_stamp) < unix_timestamp(TIMESTAMP(date('2017-09-09')))


     * @param _SensorTemperatureHistory
     * @param _HttpServletRequest
     * @param _HttpServletResponse
     * @return
     */
    @RequestMapping(value = "/descByPrimarykeyAndTimeat.url")
    @ResponseBody
    @CrossOrigin(origins="*")
    public JSONObject descByPrimarykeyAndTimeat(
            @RequestBody SensorTemperatureHistory _SensorTemperatureHistory,
            HttpServletRequest _HttpServletRequest,
            HttpServletResponse _HttpServletResponse){
        JSONObject _JSONObjectResult = new JSONObject();

        //private int totalnumcount;//共多少页

        //private int rowcount;    //每页多少条

        //private int currentpage; //当前第几页

        int rowcountFromPage = _SensorTemperatureHistory.getRowcount();

        int currentpageFromPage = _SensorTemperatureHistory.getCurrentpage();

        if(rowcountFromPage <= 0 || currentpageFromPage<=0){
            return null;
        }

        String startTimeAt = _SensorTemperatureHistory.getStartTimeAt();

        if(null == startTimeAt || startTimeAt.trim().length()<=0){
            return null;
        }

        String endTimeAt = _SensorTemperatureHistory.getEndTimeAt();
        if(null == endTimeAt || endTimeAt.trim().length()<=0){
            endTimeAt = startTimeAt;
        }

        DateFormat format__yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date _Date_EndTimeAt = format__yyyy_MM_dd.parse(endTimeAt);
            Calendar cal = Calendar.getInstance();
            cal.setTime(_Date_EndTimeAt);
            cal.add(Calendar.DATE, 1);//+1 day
            //cal.add(Calendar.MILLISECOND,-10);
            //System.out.println(cal.getTime());
            Date dateTommorry_EndTimeAt = cal.getTime();
            String dateTommorry_EndTimeAtStr = format__yyyy_MM_dd.format(dateTommorry_EndTimeAt);
            //System.out.println("dateTommorry_EndTimeAtStr-->>"+dateTommorry_EndTimeAtStr);
            _SensorTemperatureHistory.setEndTimeAt(dateTommorry_EndTimeAtStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        List<SensorTemperatureHistory> list_SensorTemperatureHistory = _SensorTemperatureDayVOServiceImpl.findSensorTemperatureHistoryByParametersDesc(_SensorTemperatureHistory);


        Integer total = list_SensorTemperatureHistory.size();
        _SensorTemperatureHistory.setTotalnumcount(total);


        int limitStart = (_SensorTemperatureHistory.getCurrentpage()-1)*_SensorTemperatureHistory.getRowcount();

        int limitEnd = rowcountFromPage;

        _SensorTemperatureHistory.setLimitStart(limitStart);
        _SensorTemperatureHistory.setLimitEnd(limitEnd);

        List<SensorTemperatureHistory> list_SensorTemperatureHistorylimit =
                _SensorTemperatureDayVOServiceImpl.findSensorTemperatureHistoryByParametersDesc(_SensorTemperatureHistory);


        _JSONObjectResult.put("actionFlag",true);
        _JSONObjectResult.put("actionResultData",list_SensorTemperatureHistorylimit);
        _JSONObjectResult.put("totalCount",total);


        return _JSONObjectResult;


    }


    //Pie chart

    /**
     *
     http://localhost:8080/devicemanager/SensorTemperatureDayVOController/getPiechar.do
     Content-Type: application/json
     Host: localhost:8080
     Content-Length: 32
     {device_id:"test_deviceid_02"}
     *
     *
     *
     SELECT count(*) as total ,t.create_stamp ,FROM_UNIXTIME(unix_timestamp(t.create_stamp),'%H:%i') as updatestampstrshow
     from sensor_temperature_history t WHERE  unix_timestamp(t.create_stamp) >  unix_timestamp(TIMESTAMP('2017-09-28 10:00:00')) and
     unix_timestamp(t.create_stamp) <  unix_timestamp(TIMESTAMP('2017-09-28 10:30:00'))
     GROUP BY updatestampstrshow

     SELECT count(*) as total ,t.create_stamp ,FROM_UNIXTIME(unix_timestamp(t.create_stamp),'%H:%i') as updatestampstrshow
     from sensor_temperature_history t WHERE  unix_timestamp(t.create_stamp) >  unix_timestamp(TIMESTAMP('2017-09-28 10:00')) and
     unix_timestamp(t.create_stamp) <  unix_timestamp(TIMESTAMP('2017-09-28 10:15'))
     GROUP BY updatestampstrshow


     * @param _SensorTemperatureHistory
     * @param _HttpServletRequest
     * @param _HttpServletResponse
     * @return
     */
    @RequestMapping(value = "/getPiechar.url")
    @ResponseBody
    @CrossOrigin(origins="*")
    public JSONObject getPiechar(
            @RequestBody SensorTemperatureHistory _SensorTemperatureHistory,
            HttpServletRequest _HttpServletRequest,
            HttpServletResponse _HttpServletResponse){
        JSONObject _JSONObjectResult = new JSONObject();

        DateFormat format__yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String startTimeAt = "";

        String endTimeAt = "";


        Date date_StartTimeAt = new Date();

        try {

             startTimeAt = format__yyyy_MM_dd.format(date_StartTimeAt);

            Date _Date_EndTimeAt = format__yyyy_MM_dd.parse(startTimeAt);
            Calendar cal = Calendar.getInstance();
            cal.setTime(_Date_EndTimeAt);
            cal.add(Calendar.MINUTE, -10);//+1 day
            //cal.add(Calendar.MILLISECOND,-10);
            System.out.println(cal.getTime());
            Date dateTommorry_EndTimeAt = cal.getTime();
            String dateTommorry_EndTimeAtStr = format__yyyy_MM_dd.format(dateTommorry_EndTimeAt);
            endTimeAt = dateTommorry_EndTimeAtStr;
            //System.out.println("dateTommorry_EndTimeAtStr-->>"+dateTommorry_EndTimeAtStr);
            _SensorTemperatureHistory.setEndTimeAt(dateTommorry_EndTimeAtStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        _SensorTemperatureHistory.setStartTimeAt(endTimeAt);

        _SensorTemperatureHistory.setEndTimeAt(startTimeAt);

        List<SensorTemperatureHistory> list_SensorTemperatureHistory = _SensorTemperatureDayVOServiceImpl.findSensorTemperatureHistoryByParametersPieChart(_SensorTemperatureHistory);

        List<Integer> listXInt_Total = new ArrayList<Integer>();

        List<String> listYString_Time = new ArrayList<String>();

        for(int i = 0 ; i < list_SensorTemperatureHistory.size() ; i++){

            SensorTemperatureHistory _SensorTemperatureHistoryCurrent = list_SensorTemperatureHistory.get(i);

            Integer total = _SensorTemperatureHistoryCurrent.getTotal();

            listXInt_Total.add(total);

            listYString_Time.add(_SensorTemperatureHistoryCurrent.getCreatestampshow());
        }

        JSONObject _JSONObjectMap = new JSONObject();

        _JSONObjectMap.put("X",listYString_Time);
        
        JSONArray y = new JSONArray();
        JSONObject y1 = new JSONObject();
        y1.put("data", listXInt_Total);
        y1.put("name", "采集次数");
        y.add(y1);
        
        _JSONObjectMap.put("Y",y);
        
        
        
        _JSONObjectResult.put("actionFlag",true);
        _JSONObjectResult.put("actionResult",_JSONObjectMap);

        return _JSONObjectResult;
    }

    /**
     {
     	"labelidarray":["0930","0754"],
     	"flagTimeBatch":1
     }
     */
    @RequestMapping(value = "/listSensorTemperatureHistoryBySensorstampwithInterval.url")
    @ResponseBody
    @CrossOrigin(origins="*")
    public net.sf.json.JSONObject getLatest(@RequestBody JSONObject params, HttpServletRequest _HttpServletRequest, HttpServletResponse _HttpServletResponse) {
		JSONArray labelArray = params.getJSONArray("labelidarray");
		
		String[] labels = new String[labelArray.size()];
		for(int i = 0; i < labelArray.size(); i++) {
			labels[i] = labelArray.get(i).toString();
		}
		
		int batch = params.getIntValue("flagTimeBatch");
		
		Sensor_Label label = sensorDao.getSensorLabel(labels[0]);
		String device_id = label.getDevice_id();
		String app_name = label.getApp_name();
		
		Sensor_Temperature_Device device = sensorDao.getDevice(device_id);
		if(device == null) {
			net.sf.json.JSONObject _JSONObjectResult = new net.sf.json.JSONObject();
			_JSONObjectResult.put("actionFlag", false);
			_JSONObjectResult.put("actionResult", "no device was found");
			
			return _JSONObjectResult;
		}

		/*
		{
		    "start": "60s-ago",
		    "queries": [
		        {
		            "aggregator": "zimsum",
		            "metric": "app.topology.services.rfid",
		            "filters": [
		                {
		                   "type":"literal_or",
		                   "tagk":"device",
		                   "filter":"10000100000D5193D55BAC0002972537",
		                   "groupBy":true
		                },
		                {
		                   "type":"literal_or",
		                   "tagk":"label",
		                   "filter":"8001|8002",
		                   "groupBy":true
		                }
		            ]
		        }
		    ]
		}
		*/
		
		// Filter[]
		net.sf.json.JSONObject filter_1 = new net.sf.json.JSONObject();
		filter_1.put("type", "literal_or");
		filter_1.put("tagk", "device");
		filter_1.put("filter", device_id);
		filter_1.put("groupBy", true);
		
		net.sf.json.JSONObject filter_2 = new net.sf.json.JSONObject();
		filter_2.put("type", "literal_or");
		filter_2.put("tagk", "label");
		StringBuilder label_sb = new StringBuilder(labels[0]);
		for(int j = 1 ; j < labels.length ; j++) {
			label_sb.append("|");
			label_sb.append(labels[j]);
		}
		filter_2.put("filter", label_sb.toString());
		filter_2.put("groupBy", true);

		net.sf.json.JSONArray filters = new net.sf.json.JSONArray();
		filters.add(filter_1);
		filters.add(filter_2);
		
		// Query
		net.sf.json.JSONObject query = new net.sf.json.JSONObject();
		query.put("aggregator", "zimsum");
		query.put("metric", app_name.toLowerCase());
		query.put("filters", filters);

		// Queries
		net.sf.json.JSONArray queries = new net.sf.json.JSONArray();
		queries.add(query);
		
		net.sf.json.JSONObject request = new net.sf.json.JSONObject();
		request.put("start", device.getPeriod() * batch + "s-ago");
		request.put("queries", queries);
	
		OpenTSDBResult response = OpenTSDBClient.httpPost(TSD_URL + "/query", request.toString());
		if(response.getStatus_code() != 200) {
			return null;
		}
		
		/*
		[
		    {
		        "metric": "app.topology.services.rfid",
		        "tags": {
		            "device": "10000100000D5193D55BAC0002972537",
		            "label": "8002"
		        },
		        "aggregateTags": [
		            "status"
		        ],
		        "dps": {
		            "1524571562": 27.761297273733874,
		            "1524571572": 44.761297273733874
		        }
		    }
		]
		 */
		net.sf.json.JSONArray objResponse = net.sf.json.JSONArray.fromObject(response.getResponse());
		
		net.sf.json.JSONArray _JSONArrayPackage = new net.sf.json.JSONArray();
		//首先得到合并的时间戳集合
		List<Long> listX_time = new ArrayList<Long>();
		for(Object obj : objResponse) {
			net.sf.json.JSONObject obj_i = (net.sf.json.JSONObject)obj;
			net.sf.json.JSONObject dps = obj_i.getJSONObject("dps");
			Iterator<String> keys = dps.keys();
			while(keys.hasNext()) {
				String key = keys.next();
				long x = Long.parseLong(key);
				boolean inserted = false;
				for(int i = 0; i < listX_time.size(); i++) {
					Long time = listX_time.get(i);
					if(x == time) {
						inserted = true;
						break;
					} else if(x < time) {
						listX_time.add(i, x);
						inserted = true;
						break;
					}
				}
				
				if(!inserted) {
					listX_time.add(x);
				}
			}
		}
		
		//时间戳跳跃的地方填空
		
		//基于时间戳集合生成每个时序数据		
		for(Object obj : objResponse) {
			net.sf.json.JSONObject obj_i = (net.sf.json.JSONObject)obj;
			
			List<Double> data = new ArrayList<Double>();
			net.sf.json.JSONObject dps = obj_i.getJSONObject("dps");
			for(int i = 0; i < listX_time.size(); i++) {
				Long time = listX_time.get(i);
				Double value = dps.optDouble(time.toString(), 99999);
				if(value == 99999) {
					data.add(null);
				} else {
					data.add(value);
				}
			}
			
			// 结果集
			String label_id_temp = obj_i.getJSONObject("tags").optString("label");
			Sensor_Label label_temp = sensorDao.getSensorLabel(label_id_temp);
			String label_name_temp = label_temp.getLabel_name();

			JSONObject _JSONObject4CurrentPackage = new JSONObject();
			_JSONObject4CurrentPackage.put("label_name", label_name_temp);
			_JSONObject4CurrentPackage.put("name", label_name_temp);
			_JSONObject4CurrentPackage.put("data", data);
			_JSONArrayPackage.add(_JSONObject4CurrentPackage);
		}
		
		//修正时间显示
		String[] strArrayTemp = new String[listX_time.size()];
		int listX_TimeAtgloablesize = listX_time.size();
		for (int j = 0 ; j < listX_TimeAtgloablesize ; j++){
			Long d = listX_time.get(j);
			Date dd = new Date(d * 1000);
			String time_temp = HHMMSS.format(dd);
			//String sec = x.substring(7, 8);
			//if(sec.compareToIgnoreCase("5") >= 0) {
			//	strArrayTemp[j] = "";
			//} else {
				strArrayTemp[j] = time_temp;
			//}
		}
		
		JSONObject _JSONObject_X = new JSONObject();
		_JSONObject_X.put("listY_oC", _JSONArrayPackage);
		_JSONObject_X.put("listX_TimeAt", strArrayTemp);
		
		net.sf.json.JSONObject _JSONObjectResult = new net.sf.json.JSONObject();
		_JSONObjectResult.put("actionFlag", true);
		_JSONObjectResult.put("actionResultData", _JSONObject_X);
		
		return _JSONObjectResult;
	}
   
   /**
    * 得到某天的按小时计数的温度数据
   {
   	"label_id": "0930",
   	"day": "2017-4-25"
   }
   */
  @RequestMapping(value = "/getlist_day.url")
  @ResponseBody
  @CrossOrigin(origins="*")
  public JSONObject getlist_day(@RequestBody JSONObject params, HttpServletRequest _HttpServletRequest, HttpServletResponse _HttpServletResponse) {
		String label_id = params.getString("label_id");
		String dayString = params.getString("day");
		
		Sensor_Label label = sensorDao.getSensorLabel(label_id);
		if(label == null) {
			JSONObject result = new JSONObject();
			result.put("result", false);
			result.put("error", "no label was found:" + label_id);
			return result;
		}
		
		String app_name = label.getApp_name().toLowerCase();
		
		Date day_begin;
		try {
			day_begin = YYYYMMDD.parse(dayString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			JSONObject result = new JSONObject();
			result.put("result", false);
			result.put("error", "invalid date format:" + dayString);
			return result;
		}

		
		//+1 day -1 second
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day_begin);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		calendar.add(Calendar.SECOND, -1);
		Date day_end = calendar.getTime();

		/*
		{
		    "start": 1524625200,
		    "end": 1524628799,
		    "queries": [
		        {
		            "aggregator": "sum",
		            "metric": "app.topology.services.rfid",
		            "downsample": "1h-count-zero",
		            "filters": [
		                {
		                   "type":"literal_or",
		                   "tagk":"label",
		                   "filter":"82060228",
		                   "groupBy":true
		                }
		            ]
		        }
		    ]
		}
		*/
		// Filter[]
		net.sf.json.JSONObject filter_2 = new net.sf.json.JSONObject();
		filter_2.put("type", "literal_or");
		filter_2.put("tagk", "label");
		filter_2.put("filter", label_id);
		filter_2.put("groupBy", true);

		net.sf.json.JSONArray filters = new net.sf.json.JSONArray();
		filters.add(filter_2);
		
		// Query
		net.sf.json.JSONObject query = new net.sf.json.JSONObject();
		query.put("aggregator", "sum");
		query.put("metric", app_name);
		query.put("downsample", "1h-count-zero");
		query.put("filters", filters);

		// Queries
		net.sf.json.JSONArray queries = new net.sf.json.JSONArray();
		queries.add(query);
		
		net.sf.json.JSONObject request = new net.sf.json.JSONObject();
		request.put("start", day_begin.getTime() / 1000);
		request.put("end", day_end.getTime() / 1000);
		request.put("queries", queries);
	
		OpenTSDBResult response = OpenTSDBClient.httpPost(TSD_URL + "/query", request.toString());
		if(response.getStatus_code() != 200) {
			return null;
		}
		
		/*
		[
		    {
		        "metric": "app.topology.services.rfid",
		        "tags": {
		            "device": "10000100000D5193D55BAC0002972537",
		            "label": "8002"
		        },
		        "aggregateTags": [
		            "status"
		        ],
		        "dps": {
		            "1524571562": 27.761297273733874,
		            "1524571572": 44.761297273733874
		        }
		    }
		]
		 */
		net.sf.json.JSONArray objResponse = net.sf.json.JSONArray.fromObject(response.getResponse());
		
		JSONArray dps_list = new JSONArray();
		if(objResponse != null && objResponse.size() > 0) {
			net.sf.json.JSONObject objResponse_0 = objResponse.getJSONObject(0);
			net.sf.json.JSONObject dps = objResponse_0.getJSONObject("dps");
			
			Iterator<String> keys = dps.keys();
			while(keys.hasNext()) {
				String key = keys.next();
				long d = Long.parseLong(key);
				Date dd = new Date(d * 1000);
				String time_temp = HHMM.format(dd);
				
				int count = dps.getInt(key);
				
				JSONObject o = new JSONObject();
				o.put("hour", time_temp);
				o.put("count", count);
				
				dps_list.add(o);
			}
		}
		
		JSONObject result = new JSONObject();
		result.put("result", true);
		result.put("error", "");
		result.put("rows", dps_list);
		
		return result;
  }
  
  /**
   * 得到某时间段内的温度数据
  {
  	"label_id": "0930",
  	"day": "2018-4-25",
  	"hour": "16:00"
  }
  */
 @RequestMapping(value = "/getlist_detail.url")
 @ResponseBody
 @CrossOrigin(origins="*")
 public JSONObject getlist_detail(@RequestBody JSONObject params, HttpServletRequest _HttpServletRequest, HttpServletResponse _HttpServletResponse) {
		String label_id = params.getString("label_id");
		String dayString = params.getString("day");
		String hourString = params.getString("hour");
		
		Sensor_Label label = sensorDao.getSensorLabel(label_id);
		if(label == null) {
			JSONObject result = new JSONObject();
			result.put("result", false);
			result.put("error", "no label was found:" + label_id);
			return result;
		}
		
		String app_name = label.getApp_name().toLowerCase();
		
		Date hour_begin;
		try {
			hour_begin = YYYYMMDD_HHMM.parse(dayString + " " + hourString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			JSONObject result = new JSONObject();
			result.put("result", false);
			result.put("error", "invalid date format:" + dayString);
			return result;
		}

		
		//+1 hour -1 second
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(hour_begin);
		calendar.add(Calendar.HOUR, 1);
		calendar.add(Calendar.SECOND, -1);
		Date hour_end = calendar.getTime();

		/*
		{
		    "start": 1524625200,
		    "end": 1524628799,
		    "queries": [
		        {
		            "aggregator": "zimsum",
		            "metric": "app.topology.services.rfid",
		            "filters": [
		                {
		                   "type":"literal_or",
		                   "tagk":"label",
		                   "filter":"82060228",
		                   "groupBy":true
		                }
		            ]
		        }
		    ]
		}
		*/
		// Filter[]
		net.sf.json.JSONObject filter_2 = new net.sf.json.JSONObject();
		filter_2.put("type", "literal_or");
		filter_2.put("tagk", "label");
		filter_2.put("filter", label_id);
		filter_2.put("groupBy", true);

		net.sf.json.JSONArray filters = new net.sf.json.JSONArray();
		filters.add(filter_2);
		
		// Query
		net.sf.json.JSONObject query = new net.sf.json.JSONObject();
		query.put("aggregator", "zimsum");
		query.put("metric", app_name);
		query.put("filters", filters);

		// Queries
		net.sf.json.JSONArray queries = new net.sf.json.JSONArray();
		queries.add(query);
		
		net.sf.json.JSONObject request = new net.sf.json.JSONObject();
		request.put("start", hour_begin.getTime() / 1000);
		request.put("end", hour_end.getTime() / 1000);
		request.put("queries", queries);
	
		OpenTSDBResult response = OpenTSDBClient.httpPost(TSD_URL + "/query", request.toString());
		if(response.getStatus_code() != 200) {
			return null;
		}
		
		/*
		[
		    {
		        "metric": "app.topology.services.rfid",
		        "tags": {
		            "device": "10000100000D5193D55BAC0002972537",
		            "label": "8002"
		        },
		        "aggregateTags": [
		            "status"
		        ],
		        "dps": {
		            "1524571562": 27.761297273733874,
		            "1524571572": 44.761297273733874
		        }
		    }
		]
		 */
		net.sf.json.JSONArray objResponse = net.sf.json.JSONArray.fromObject(response.getResponse());
		
		JSONArray dps_list = new JSONArray();
		if(objResponse != null && objResponse.size() > 0) {
			net.sf.json.JSONObject objResponse_0 = objResponse.getJSONObject(0);
			net.sf.json.JSONObject dps = objResponse_0.getJSONObject("dps");
			
			Iterator<String> keys = dps.keys();
			while(keys.hasNext()) {
				String key = keys.next();
				long d = Long.parseLong(key);
				Date dd = new Date(d * 1000);
				String time_temp = HHMMSS.format(dd);
				
				double value = dps.getDouble(key);
				
				JSONObject o = new JSONObject();
				o.put("time", time_temp);
				o.put("value", value);				
				dps_list.add(o);
			}
		}
		
		JSONObject result = new JSONObject();
		result.put("result", true);
		result.put("error", "");
		result.put("rows", dps_list);
		
		return result;
 	}
}
