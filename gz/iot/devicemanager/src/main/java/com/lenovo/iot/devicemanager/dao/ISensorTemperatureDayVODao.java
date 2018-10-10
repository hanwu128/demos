package com.lenovo.iot.devicemanager.dao;

import com.lenovo.iot.devicemanager.model.bo.ApacheAdgentMetaData;
import com.lenovo.iot.devicemanager.model.bo.SensorTemperatureDayVO;
import com.lenovo.iot.devicemanager.model.bo.SensorTemperatureHistory;
import com.lenovo.iot.devicemanager.model.bo.SensorTemperatureMinitue;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by root on 2017/7/19.
 */
@Repository
public interface ISensorTemperatureDayVODao {

	public Integer findSensorTemperatureDayCountByParameter(SensorTemperatureDayVO _SensorTemperatureDayVO);
	public List<SensorTemperatureDayVO> findSensorTemperatureDayVOByParameter(SensorTemperatureDayVO _SensorTemperatureDayVO);

    public List<SensorTemperatureHistory> findSensorTemperatureHistoryByParameters(SensorTemperatureHistory _SensorTemperatureHistory);

    //findSensorTemperatureHistoryByParametersDesc
    public List<SensorTemperatureHistory> findSensorTemperatureHistoryByParametersDesc(SensorTemperatureHistory _SensorTemperatureHistory);

    //findSensorTemperatureMinitueByParametersReport
    public List<SensorTemperatureMinitue> findSensorTemperatureMinitueByParametersReport(SensorTemperatureMinitue _SensorTemperatureMinitue);

   // findSensorTemperatureHistoryByParametersPieChart
    public List<SensorTemperatureHistory> findSensorTemperatureHistoryByParametersPieChart(SensorTemperatureHistory _SensorTemperatureHistory);

    //findSensorTemperatureHistoryByParametersZLineChart

    public List<SensorTemperatureHistory> findSensorTemperatureHistoryByParametersZLineChart(SensorTemperatureHistory _SensorTemperatureHistory);



}
