package com.lenovo.iot.devicemanager.service.impl;

import com.lenovo.iot.devicemanager.dao.IApacheAdgentMetaDataDao;
import com.lenovo.iot.devicemanager.dao.ISensorTemperatureDayVODao;
import com.lenovo.iot.devicemanager.model.bo.SensorTemperatureDayVO;
import com.lenovo.iot.devicemanager.model.bo.SensorTemperatureHistory;
import com.lenovo.iot.devicemanager.model.bo.SensorTemperatureMinitue;
import com.lenovo.iot.devicemanager.service.ISensorTemperatureDayVOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by root on 2017/9/7.
 */
@Service
public class SensorTemperatureDayVOServiceImpl implements ISensorTemperatureDayVOService{

    private static Logger logger = LoggerFactory.getLogger(SensorTemperatureDayVOServiceImpl.class);    //日志记录

    @Autowired
    private ISensorTemperatureDayVODao _ISensorTemperatureDayVODao;

    public Integer findSensorTemperatureDayCountByParameter(SensorTemperatureDayVO _SensorTemperatureDayVO) {
    	return _ISensorTemperatureDayVODao.findSensorTemperatureDayCountByParameter(_SensorTemperatureDayVO);
    }
    public List<SensorTemperatureDayVO> findSensorTemperatureDayVOByParameter(SensorTemperatureDayVO _SensorTemperatureDayVO) {

        List list_SensorTemperatureDayVO =  _ISensorTemperatureDayVODao.findSensorTemperatureDayVOByParameter(_SensorTemperatureDayVO);

        return list_SensorTemperatureDayVO;
    }

    public List<SensorTemperatureDayVO> getReportDataSource(SensorTemperatureDayVO _SensorTemperatureDayVO) {

        return null;
    }

    public List<SensorTemperatureHistory> findSensorTemperatureHistoryByParameters(SensorTemperatureHistory _SensorTemperatureHistory) {

        List<SensorTemperatureHistory> list__SensorTemperatureHistory =  _ISensorTemperatureDayVODao.findSensorTemperatureHistoryByParameters(_SensorTemperatureHistory);

        return list__SensorTemperatureHistory;
    }

    public List<SensorTemperatureHistory> findSensorTemperatureHistoryByParametersDesc(SensorTemperatureHistory _SensorTemperatureHistory) {

        List<SensorTemperatureHistory> list__SensorTemperatureHistory = _ISensorTemperatureDayVODao.findSensorTemperatureHistoryByParametersDesc(_SensorTemperatureHistory);

        return list__SensorTemperatureHistory;
    }

    public List<SensorTemperatureMinitue> findSensorTemperatureMinitueByParametersReport(SensorTemperatureMinitue _SensorTemperatureMinitue) {

        List<SensorTemperatureMinitue> __List_SensorTemperatureMinitue =  _ISensorTemperatureDayVODao.findSensorTemperatureMinitueByParametersReport(_SensorTemperatureMinitue);

        return __List_SensorTemperatureMinitue;
    }


    public List<SensorTemperatureHistory> findSensorTemperatureHistoryByParametersPieChart(SensorTemperatureHistory _SensorTemperatureHistory) {

        if(null == _SensorTemperatureHistory){
            return null;
        }

        return _ISensorTemperatureDayVODao.findSensorTemperatureHistoryByParametersPieChart(_SensorTemperatureHistory);
    }

    public List<SensorTemperatureHistory> findSensorTemperatureHistoryByParametersZLineChart(SensorTemperatureHistory _SensorTemperatureHistory) {

        if(null == _SensorTemperatureHistory){
            return null;
        }

        return _ISensorTemperatureDayVODao.findSensorTemperatureHistoryByParametersZLineChart(_SensorTemperatureHistory);
    }


}
