package com.lenovo.iot.devicemanager.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lenovo.iot.devicemanager.model.Sensor_Gps;
import com.lenovo.iot.devicemanager.model.Sensor_Label;
import com.lenovo.iot.devicemanager.model.Sensor_Temperature_Alarm;
import com.lenovo.iot.devicemanager.model.Sensor_Temperature_Day;
import com.lenovo.iot.devicemanager.model.Sensor_Temperature_Device;
import com.lenovo.iot.devicemanager.model.Tuple;
import com.lenovo.iot.devicemanager.util.Pagination;

@Repository
public class SensorDao {

	private static Logger log = Logger.getLogger("fordebug");
	
	@Autowired
	private JdbcTemplate jdbcTemplateMysql;
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	public Sensor_Temperature_Device getDevice(String device_id) {
		String sql = "select id, device_id, device_name, temperature_upper, temperature_lower, duration, period, stage, create_stamp from sensor_temperature_device where device_id = ?";
		List<Sensor_Temperature_Device> list = jdbcTemplateMysql.query(sql, new Object[]{ device_id }, Sensor_Temperature_Device.MAP);
		if(list != null && list.size() > 0 ){
			return (Sensor_Temperature_Device)list.get(0);
		}
		
		return null;
	}

	public List<Sensor_Temperature_Device> getDeviceList() {
		List<Sensor_Temperature_Device> list;
		
		String sql = "select id, device_id, device_name, temperature_upper, temperature_lower, duration, period, stage, create_stamp from sensor_temperature_device";
		list = jdbcTemplateMysql.query(sql, new Object[]{ }, Sensor_Temperature_Device.MAP);

		return list;
	}
	
	public int update_device(final List<Sensor_Temperature_Device> list) {
		final String sql_insert = "update sensor_temperature_device set device_name=?, temperature_upper=?, temperature_lower=?, period=? where device_id=?";

		//Transaction
		TransactionTemplate transTemp = new TransactionTemplate();
		transTemp.setTransactionManager(transactionManager);
		return transTemp.execute(new TransactionCallback<Integer>() {
            public Integer doInTransaction(TransactionStatus ts) {
                try {
                	int rows[] = jdbcTemplateMysql.batchUpdate(sql_insert, new BatchPreparedStatementSetter() {
            			public void setValues(PreparedStatement ps, int i) throws SQLException {
            				// TODO Auto-generated method stub
            				Sensor_Temperature_Device item = list.get(i);
            				ps.setString(1, item.getDevice_name());
            				ps.setDouble(2, item.getTemperature_upper());
               				ps.setDouble(3, item.getTemperature_lower());
               				ps.setInt(4, item.getPeriod());
               				ps.setString(5, item.getDevice_id());
            			}

            			public int getBatchSize() {
            				return list.size();
            			}
            		});
            		
            		int row = 0;
            		for(int r : rows) {
            			row += r;
            		}
            		
            		return row;
                } catch (RuntimeException e) {
                    ts.setRollbackOnly();
                    log.debug(e.getMessage());
                    return -1;
                }
            }
        });
	}
	
	public int getRunningStream(String device_id, String app_name) {
		final String sql = "select s.streamingid from streaming_device_app s inner join device d on s.deviceid = d.id where s.islive = 'true' and d.device_id = ? and s.appname = ?";
		int streamingid;
		try {
			streamingid = jdbcTemplateMysql.queryForObject(sql, new Object[]{ device_id, app_name }, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			streamingid = 0;
		}
		return streamingid;
	}
	
	public Sensor_Label getSensorLabel(String label_id) {
		String sql = "select id, label_id, label_name, label_group, label_index, device_id, create_stamp, update_stamp, app_name from sensor_label where label_id = ?";
		List<Sensor_Label> list = jdbcTemplateMysql.query(sql, new Object[]{ label_id }, Sensor_Label.MAP);
		if(list != null && list.size() > 0 ){
			return (Sensor_Label)list.get(0);
		}
		
		return null;
	}
	
	public int getSensorLabelCount() {
		String sql = "select count(*) from sensor_label";
		int count = jdbcTemplateMysql.queryForObject(sql, new Object[]{ }, Integer.class);
	
		return count;
	}
	
	public int addLabels(final List<Sensor_Label> list) {
		final String sql_insert = "insert into sensor_label(label_id, label_name, label_index, label_group, device_id, create_stamp, update_stamp, app_name) values(?, ?, ?, ?, ?, ?, ?, ?)";

		//Transaction
		TransactionTemplate transTemp = new TransactionTemplate();
		transTemp.setTransactionManager(transactionManager);
		return transTemp.execute(new TransactionCallback<Integer>() {
            public Integer doInTransaction(TransactionStatus ts) {
                try {
                	int rows[] = jdbcTemplateMysql.batchUpdate(sql_insert, new BatchPreparedStatementSetter() {
            			public void setValues(PreparedStatement ps, int i) throws SQLException {
            				// TODO Auto-generated method stub
            				Sensor_Label item = list.get(i);
            				ps.setString(1, item.getLabel_id());
            				ps.setString(2, item.getLabel_name());
               				ps.setString(3, item.getLabel_index());
               				ps.setString(4, item.getLabel_group());
               				ps.setString(5, item.getDevice_id());
               			    ps.setTimestamp(6, item.getCreate_stamp());
               			    ps.setTimestamp(7, item.getUpdate_stamp());
            				ps.setString(8, item.getApp_name());
            			}

            			public int getBatchSize() {
            				return list.size();
            			}
            		});
            		
            		int row = 0;
            		for(int r : rows) {
            			row += r;
            		}
            		
            		return row;
                } catch (RuntimeException e) {
                    ts.setRollbackOnly();
                    log.debug(e.getMessage());
                    return -1;
                }
            }
        });
	}
	
	public int updateSensorLabel(final List<Sensor_Label> list) {
		final String sql_insert = "insert into sensor_label(label_id, label_name, label_index, label_group, device_id, create_stamp, update_stamp, app_name) values(?, ?, ?, ?, ?, ?, ?, ?)";
		final String sql_update = "update sensor_label set device_id = ?, app_name = ?, update_stamp = ? where label_id = ?";

		//Transaction
		TransactionTemplate transTemp = new TransactionTemplate();
		transTemp.setTransactionManager(transactionManager);
		return transTemp.execute(new TransactionCallback<Integer>() {
            public Integer doInTransaction(TransactionStatus ts) {
                try {
                	int row = 0;
        			for(Sensor_Label label : list) {
        				if(label.getId() > 0) {
        					int r = jdbcTemplateMysql.update(sql_update, new Object[]{ label.getDevice_id(), label.getApp_name(), label.getUpdate_stamp(), label.getLabel_id() });
        					row = row + r;
        				} else {
        					int r = jdbcTemplateMysql.update(sql_insert, new Object[]{ label.getLabel_id(), label.getLabel_name(), label.getLabel_index(), label.getLabel_group(), label.getDevice_id(), label.getCreate_stamp(), label.getUpdate_stamp(), label.getApp_name() });
        					row = row + r;
        				}
            		}
            		
                    return row;
                } catch (RuntimeException e) {
                    ts.setRollbackOnly();
                    log.debug(e.getMessage());
                    return -1;
                }
            }
        });
	}
	
	public int addTemperatureDay(/*final List<Sensor_Temperature> list, */final List<Sensor_Temperature_Day> list_day/*, final List<Sensor_Temperature_Minitue> list_minitue*/) {
		//final String sql1 = "insert into sensor_temperature_history(label_id, value, sensor_stamp, create_stamp, device_id) values(?, ?, ?, now(), ?)";
		final String sql2_insert = "insert into sensor_temperature_day(label_id, current_value, max_value, min_value, what_day, update_stamp, max_stamp, min_stamp, last_value, status, last_status) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		final String sql2_update = "update sensor_temperature_day set current_value = ?, max_value = ?, min_value = ?, update_stamp = ?, max_stamp = ?, min_stamp = ?, last_value = ?, status = ?, last_status = ? where label_id = ? and what_day = ?";

		//Transaction
		TransactionTemplate transTemp = new TransactionTemplate();
		transTemp.setTransactionManager(transactionManager);
		return transTemp.execute(new TransactionCallback<Integer>() {
            public Integer doInTransaction(TransactionStatus ts) {
                try {
//                	int rows[] = jdbcTemplateMysql.batchUpdate(sql1, new BatchPreparedStatementSetter() {
//            			public void setValues(PreparedStatement ps, int i) throws SQLException {
//            				// TODO Auto-generated method stub
//            				Sensor_Temperature item = list.get(i);
//            				ps.setString(1, item.getLabel_id());
//            				ps.setDouble(2, item.getValue());
//            				ps.setTimestamp(3, item.getSensor_stamp());
//            				ps.setString(4, item.getDevice_id());
//            			}
//
//            			public int getBatchSize() {
//            				return list.size();
//            			}
//            		});
//            		
            		int row = 0;
//            		for(int r : rows) {
//            			row += r;
//            		}
//            		
//            		if(row > 0) {
            			for(Sensor_Temperature_Day day : list_day) {
            				if(day.getId() > 0) {
            					row += jdbcTemplateMysql.update(sql2_update, new Object[]{ day.getCurrent_value(), day.getMax_value(), day.getMin_value(), day.getUpdate_stamp(), day.getMax_stamp(), day.getMin_stamp(), day.getLast_value(), day.getStatus(), day.getLast_status(), day.getLabel_id(), day.getWhat_day() });
            				} else {
            					row += jdbcTemplateMysql.update(sql2_insert, new Object[]{ day.getLabel_id(), day.getCurrent_value(), day.getMax_value(), day.getMin_value(), day.getWhat_day(), day.getUpdate_stamp(), day.getMax_stamp(), day.getMin_stamp(), day.getLast_value(), day.getStatus(), day.getLast_status() });
            				}
                		}
//            		}
            		
                    return row;
                } catch (RuntimeException e) {
                    ts.setRollbackOnly();
                    log.debug(e.getMessage());
                    return -1;
                }
            }
        });
	}
	
	public Sensor_Temperature_Day getSensorTemperatureDay(String label_id, String day) {
		String sql = "select id, label_id, current_value, max_value, min_value, what_day, update_stamp, max_stamp, min_stamp, last_value, status, last_status from sensor_temperature_day where label_id = ? and what_day = ?";
		List<Sensor_Temperature_Day> list = jdbcTemplateMysql.query(sql, new Object[]{ label_id, day }, Sensor_Temperature_Day.MAP);
		if(list != null && list.size() > 0 ){
			return (Sensor_Temperature_Day)list.get(0);
		}
		
		return null;
	}
	
	public int getSensor_Temperature_History_Count() {
		String sql = "select count(*) from sensor_temperature_history";
		int count = jdbcTemplateMysql.queryForObject(sql, new Object[]{ }, Integer.class);
	
		return count;
	}
	
	public int getSensor_Temperature_History_Invalid_Count() {
		String sql = "select count(*) from sensor_temperature_history where value = 0";
		int count = jdbcTemplateMysql.queryForObject(sql, new Object[]{ }, Integer.class);
	
		return count;
	}

	//报警
	public int addAlarm(Sensor_Temperature_Alarm alarm) {
		String sql = "insert into sensor_temperature_alarm(device_id, label_id, type, value, value_limit, create_stamp, status) values(?, ?, ?, ?, ?, ?, 0)";
		int row = jdbcTemplateMysql.update(sql, new Object[]{ alarm.getDevice_id(), alarm.getLabel_id(), alarm.getType(), alarm.getValue(), alarm.getValue_limit(), alarm.getCreate_stamp() });
    	
		return row;
	}
	
	public Sensor_Temperature_Alarm getAlarm(long id) {
		String sql = "select id, device_id, device_id as device_name, label_id, type, value, value_limit, create_stamp, status from sensor_temperature_alarm where id = ?";
		List<Sensor_Temperature_Alarm> list = jdbcTemplateMysql.query(sql, new Object[]{ id }, Sensor_Temperature_Alarm.MAP);
		if(list != null && list.size() > 0 ){
			return (Sensor_Temperature_Alarm)list.get(0);
		}
		
		return null;
	}
	
	public int updateAlarmStatus(final Timestamp timestamp) {
		final String sql = "update sensor_temperature_alarm set status = 1 where create_stamp <= ?";
        int row = jdbcTemplateMysql.update(sql, new Object[]{ timestamp });
        return row;
	}
	
	//实时报警
	public List<Sensor_Temperature_Alarm> get_real_alarm(Pagination page, Date time_begin) {
		String sql_where = " where a.create_stamp > ? and ifnull(a.status, 0) = 0";
		String sql_orderby = " order by a.create_stamp desc";
		String sql_list = "select a.id, a.device_id, b.device_name, a.label_id, a.type, a.value, a.value_limit, a.create_stamp, status from sensor_temperature_alarm a left join sensor_temperature_device b on a.device_id = b.device_id";
		List<Sensor_Temperature_Alarm> list;

		//query for total
		if(page != null) {
			String sql_count = "select count(*) from sensor_temperature_alarm a";
			int total = jdbcTemplateMysql.queryForObject(sql_count + sql_where, new Object[]{ time_begin }, Integer.class);
			page.setTotal(total);
		}
		
		//query for list
		if(page != null) {
			list = page.queryPage(jdbcTemplateMysql, sql_list + sql_where + sql_orderby, new Object[]{ time_begin }, Sensor_Temperature_Alarm.MAP);
		} else {
			list = jdbcTemplateMysql.query(sql_list + sql_where + sql_orderby, new Object[]{ time_begin }, Sensor_Temperature_Alarm.MAP);
		}
		
		return list;
	}
	
	//报警列表
	public List<Sensor_Temperature_Alarm> getAlarmList(Pagination page, String device_id, Date day_begin, Date day_end) {
		List<Sensor_Temperature_Alarm> list;

		if(device_id == null) device_id = "";

		String sql_list = "select a.id, a.device_id, b.device_name, a.label_id, a.type, a.value, a.value_limit, a.create_stamp, a.status from sensor_temperature_alarm a left join sensor_temperature_device b on a.device_id = b.device_id";
		
		StringBuilder sqlWhere = new StringBuilder(" where ");
		List<Object> params = new ArrayList<Object>();
		if(!device_id.isEmpty()) {
			sqlWhere.append(" a.device_id = ? and");
			params.add(device_id);
		}
		
		sqlWhere.append(" a.create_stamp >= ? and a.create_stamp < ?");
		params.add(day_begin);
		params.add(day_end);

		//query for total
		if(page != null) {
			String sql_count = "select count(*) from sensor_temperature_alarm a";
			int total = jdbcTemplateMysql.queryForObject(sql_count + sqlWhere.toString(), params.toArray(), Integer.class);
			page.setTotal(total);
		}
		
		// order by
		String order_by = "a.create_stamp";
		String asc = "desc";
		if(page != null) {
			if(page.getOrder_by() != null && !page.getOrder_by().isEmpty() && page.getAsc() != null && !page.getAsc().isEmpty()) {
				order_by = page.getOrder_by();
				asc = page.getAsc();
			}
		}
		
		sqlWhere.append(" order by ");
		sqlWhere.append(order_by);
		sqlWhere.append(" ");
		sqlWhere.append(asc);
		
		//query for list
		if(page != null) {
			list = page.queryPage(jdbcTemplateMysql, sql_list + sqlWhere.toString(), params.toArray(), Sensor_Temperature_Alarm.MAP);
		} else {
			list = jdbcTemplateMysql.query(sql_list + sqlWhere.toString(), params.toArray(), Sensor_Temperature_Alarm.MAP);
		}
		
		return list;
	}
	
	public int addGps(final Sensor_Gps gps) {
		final String sql1 = "insert into sensor_gps_history(device_id, longitude, latitude, gps_stamp, device_stamp, server_stamp) values(?, ?, ?, ?, ?, ?)";
		
		final String sql2_select = "select id from sensor_gps where device_id = ?";
		final String sql2_insert = "insert into sensor_gps(device_id, longitude, latitude, gps_stamp, device_stamp, server_stamp) values(?, ?, ?, ?, ?, ?)";
		final String sql2_update = "update sensor_gps set longitude = ?, latitude = ?, gps_stamp = ?, device_stamp = ?, server_stamp = ? where id = ?";

		//Transaction
		TransactionTemplate transTemp = new TransactionTemplate();
		transTemp.setTransactionManager(transactionManager);
		return transTemp.execute(new TransactionCallback<Integer>() {
            public Integer doInTransaction(TransactionStatus ts) {
                try {
                	int row = jdbcTemplateMysql.update(sql1, new Object[] {
                		gps.getDevice_id(), gps.getLongitude(), gps.getLatitude(), gps.getGps_stamp(), gps.getDevice_stamp(), gps.getServer_stamp()
            		});
            		
            		if(row > 0) {
            			long id;
            			try {
            				id = jdbcTemplateMysql.queryForObject(sql2_select, new Object[]{ gps.getDevice_id() }, Long.class);
            			} catch (EmptyResultDataAccessException e) {
            				id = 0;
            			}
            			
            			if(id == 0) {
            				row = jdbcTemplateMysql.update(sql2_insert, new Object[] {
                            		gps.getDevice_id(), gps.getLongitude(), gps.getLatitude(), gps.getGps_stamp(), gps.getDevice_stamp(), gps.getServer_stamp()
                        		});
            			} else {
            				row = jdbcTemplateMysql.update(sql2_update, new Object[] {
                            		gps.getLongitude(), gps.getLatitude(), gps.getGps_stamp(), gps.getDevice_stamp(), gps.getServer_stamp(), id
                        		});
            			}
            		}
            		
                    return row;
                } catch (RuntimeException e) {
                    ts.setRollbackOnly();
                    log.debug(e.getMessage());
                    return -1;
                }
            }
        });
	}
	
	public Sensor_Gps getGPS(String device_id) {
		String sql = "select id, device_id, longitude, latitude, gps_stamp, device_stamp, server_stamp from sensor_gps where device_id = ?";
		List<Sensor_Gps> list = jdbcTemplateMysql.query(sql, new Object[]{ device_id }, Sensor_Gps.MAP);
		if(list != null && list.size() > 0 ){
			return (Sensor_Gps)list.get(0);
		}
		
		return null;
	}
	
	// 得到温度记录数量
	public long get_data_count(String device_id, boolean valid) {
		
		String sql;
		if(valid) {
			sql = "select count(*) from sensor_temperature_history where value != 0 and device_id = ?";
		} else {
			sql = "select count(*) from sensor_temperature_history where value = 0 and device_id = ?";
		}
		
		long count = jdbcTemplateMysql.queryForObject(sql, new Object[]{ device_id }, Long.class);
		
		return count;
	}
	
	// 趋势
	public List<Tuple<String,Integer,Integer>> get_data_trend(String startTimeAt, String endTimeAt, double max_value, double min_value) {

		String sql = "SELECT DATE_FORMAT(create_stamp,'%H') as v1, sum(case sign(value - ?) when 1 then 1 else 0 end) as v2, sum(case sign(value - ?) when -1 then 1 else 0 end) as v3 FROM sensor_temperature_history WHERE create_stamp > TIMESTAMP(date(?)) AND create_stamp < TIMESTAMP(date(?)) GROUP BY v1 ORDER BY v1;";
		List<Tuple<String,Integer,Integer>> list = jdbcTemplateMysql.query(sql, new Object[]{ max_value, min_value, startTimeAt, endTimeAt }, Tuple.MAP_SII);
		
		return list;
	}
}