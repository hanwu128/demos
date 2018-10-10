package com.lenovo.iot.devicemanager.service;

import com.lenovo.iot.devicemanager.model.bo.SensorTemperatureDayVO;
import com.lenovo.iot.devicemanager.model.bo.SensorTemperatureHistory;
import com.lenovo.iot.devicemanager.model.bo.SensorTemperatureMinitue;

import java.util.List;

/**
 * Created by root on 2017/9/7.
 */
public interface ISensorTemperatureDayVOService {


	public Integer findSensorTemperatureDayCountByParameter(SensorTemperatureDayVO _SensorTemperatureDayVO);
	public List<SensorTemperatureDayVO> findSensorTemperatureDayVOByParameter(SensorTemperatureDayVO _SensorTemperatureDayVO);

    public List<SensorTemperatureDayVO>  getReportDataSource(SensorTemperatureDayVO _SensorTemperatureDayVO);


    public List<SensorTemperatureHistory> findSensorTemperatureHistoryByParameters(SensorTemperatureHistory _SensorTemperatureHistory);

    public List<SensorTemperatureHistory> findSensorTemperatureHistoryByParametersDesc(SensorTemperatureHistory _SensorTemperatureHistory);

    public List<SensorTemperatureMinitue> findSensorTemperatureMinitueByParametersReport(SensorTemperatureMinitue _SensorTemperatureMinitue);

    public List<SensorTemperatureHistory> findSensorTemperatureHistoryByParametersPieChart(SensorTemperatureHistory _SensorTemperatureHistory);

    public List<SensorTemperatureHistory> findSensorTemperatureHistoryByParametersZLineChart(SensorTemperatureHistory _SensorTemperatureHistory);


}
