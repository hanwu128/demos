package com.lenovo.iot.devicemanager.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lenovo.iot.devicemanager.model.Device;
import com.lenovo.iot.devicemanager.model.DeviceEx;
import com.lenovo.iot.devicemanager.model.DeviceOnlineHistory;
import com.lenovo.iot.devicemanager.model.DeviceShadowItem;
import com.lenovo.iot.devicemanager.model.MqttDeviceMessages;
import com.lenovo.iot.devicemanager.model.MqttMessage;
import com.lenovo.iot.devicemanager.util.Pagination;

@Repository
public class DeviceDao {

	private static final Logger log = LoggerFactory.getLogger(DeviceDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplateMysql;
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	public Device getDevice(String device_id) {
		String sql = "select id, device_id, company_id, policy_name, access_key, secret_key, topic_group, device_desc, create_stamp, update_stamp, online from device where device_id = ?";
		List<Device> list = jdbcTemplateMysql.query(sql, new Object[]{ device_id }, Device.MAP);
		if(list != null && list.size() > 0 ){
			return (Device)list.get(0);
		}
		
		return null;
	}


	public Device getDeviceByPrimaryKey(Long id) {
		String sql = "select id, device_id, company_id, policy_name, access_key, secret_key, topic_group, device_desc, create_stamp, update_stamp, online from device where id = ?";
		List<Device> list = jdbcTemplateMysql.query(sql, new Object[]{ id }, Device.MAP);
		if(list != null && list.size() > 0 ){
			return (Device)list.get(0);
		}

		return null;
	}

	public Device getDeviceByAccessKey(long company_id, String access_key) {
		String sql = "select id, device_id, company_id, policy_name, access_key, secret_key, topic_group, device_desc, create_stamp, update_stamp, online from device where company_id = ? and access_key = ?";
		List<Device> list = jdbcTemplateMysql.query(sql, new Object[]{ company_id, access_key }, Device.MAP);
		if(list != null && list.size() > 0 ){
			return (Device)list.get(0);
		}
		
		return null;
	}
	
//	public int getDeviceCountByDeviceIdOrAccessKey(String device_id, String access_key) {
//		String sql = "select count(*) from device where device_id = ? or access_key = ?";
//		int count = jdbcTemplateMysql.queryForObject(sql, new Object[]{ device_id, access_key }, Integer.class);
//		
//		return count;
//	}
	
	public int getDeviceCount() {
		String sql = "select count(*) from device where system = 0";
		int count = jdbcTemplateMysql.queryForObject(sql, new Object[]{ }, Integer.class);
		
		return count;
	}
	
	public int addDevice(Device device) {
		String sql = "insert into device(device_id, company_id, policy_name, access_key, secret_key, topic_group, device_desc, create_stamp, update_stamp, online) values(?, ?, ?, ?, ?, ?, ?, now(), now(), 0)";
		int row = jdbcTemplateMysql.update(sql, new Object[]{ device.getDevice_id(), device.getCompany_id(), device.getPolicy_name(), device.getAccess_key(), device.getSecret_key(), device.getTopic_group(), device.getDevice_desc() });
    	
		return row;
	}
	
	public int addDevices(final List<Device> devices) {
		final String sql = "insert into device(device_id, company_id, policy_name, access_key, secret_key, topic_group, device_desc, create_stamp, update_stamp, online) values(?, ?, ?, ?, ?, ?, ?, now(), now(), 0)";
    	
		// Transaction
		TransactionTemplate transTemp = new TransactionTemplate();
		transTemp.setTransactionManager(transactionManager);
		return transTemp.execute(new TransactionCallback<Integer>() {
			public Integer doInTransaction(TransactionStatus ts) {
				try {
            		int rows[] = jdbcTemplateMysql.batchUpdate(sql, new BatchPreparedStatementSetter() {
            			public void setValues(PreparedStatement ps, int i) throws SQLException {
            				// TODO Auto-generated method stub
            				Device device = devices.get(i);
            				ps.setString(1, device.getDevice_id());
            				ps.setLong(2, device.getCompany_id());
            				ps.setString(3, device.getPolicy_name());
            				ps.setString(4, device.getAccess_key());
            				ps.setString(5, device.getSecret_key());
            				ps.setString(6, device.getTopic_group());
            				ps.setString(7, device.getDevice_desc());
            			}

            			public int getBatchSize() {
            				return devices.size();
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
	
	public int deleteDevice(String device_id) {
		String sql = "delete from device where device_id = ?";
		int row = jdbcTemplateMysql.update(sql, new Object[]{ device_id });
    	
		return row;
	}
	
//	public int updateDevice(Device device) {
//		String sql = "update device set policy_name = ?, access_key = ?, device_desc = ?, update_stamp = now() where device_id = ?";
//		int row = jdbcTemplateMysql.update(sql, new Object[]{ device.getPolicy_name(), device.getAccess_key(), device.getDevice_desc(), device.getDevice_id() });
//    	
//		return row;
//	}
	
	public int updateDeviceSecretKey(String device_id, long company_id, String secret_key) {
		String sql = "update device set secret_key = ?, update_stamp = now() where device_id = ? and company_id = ?";
		int row = jdbcTemplateMysql.update(sql, new Object[]{ secret_key, device_id, company_id });
    	
		return row;
	}

/*
	public List<Device> getDeviceList(String device_id) {
		List<Device> list;
		
		if(device_id == null || device_id.isEmpty()) {
			String sql = "select id, device_id, company_id, policy_name, access_key, secret_key, device_desc, create_stamp, update_stamp, online from device where system = 0";
			list = jdbcTemplateMysql.query(sql, new Object[]{ }, Device.MAP);
		} else {
			String sql = "select id, device_id, company_id, policy_name, access_key, secret_key, device_desc, create_stamp, update_stamp, online from device where system = 0 and device_id like ?";
			list = jdbcTemplateMysql.query(sql, new Object[]{ "%" + device_id + "%" }, Device.MAP);
		}
		return list;
	}
*/

	public List<DeviceEx> getDeviceListEx(Pagination page, long company_id, String access_key, String adgent_version, boolean online_only) {
		List<DeviceEx> list;

		if(access_key == null) access_key = "";
		if(adgent_version == null) adgent_version = "";

		String sql_list = "select device.id, device_id, company_id, policy_name, access_key, secret_key, topic_group, device_desc, create_stamp, update_stamp, online, apacheadgentmetadata.edgent_agent_version as edgent_version from device left outer join apacheadgentmetadata on device.device_id = apacheadgentmetadata.hardware_device_id";
		
		StringBuilder sqlWhere = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlWhere.append(" where system = 0 and company_id = ?");
		params.add(company_id);
		
		if(!access_key.isEmpty()) {
			sqlWhere.append(" and device.access_key like ?");
			params.add("%" + access_key + "%");
		}
		if(!adgent_version.isEmpty()) {
			sqlWhere.append(" and apacheadgentmetadata.edgent_agent_version like ?");
			params.add("%" + adgent_version + "%");
		}
		if(online_only) {
			sqlWhere.append(" and online = 1");
		}

		//query for total
		if(page != null) {
			String sql_count = "select count(device.id) from device left outer join apacheadgentmetadata on device.device_id = apacheadgentmetadata.hardware_device_id";
			int total = jdbcTemplateMysql.queryForObject(sql_count + sqlWhere.toString(), params.toArray(), Integer.class);
			page.setTotal(total);
		}
		
		// order by
		String order_by = "create_stamp";
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
			list = page.queryPage(jdbcTemplateMysql, sql_list + sqlWhere.toString(), params.toArray(), DeviceEx.MAP);
		} else {
			list = jdbcTemplateMysql.query(sql_list + sqlWhere.toString(), params.toArray(), DeviceEx.MAP);
		}
		log.debug("getDeviceListEx() company_id:" + company_id);
		
		return list;
	}
	
	public List<String> getEdgentVersionList() {
		String sql = "select distinct(edgent_agent_version) from apacheadgentmetadata order by edgent_agent_version";
		List<String> result = jdbcTemplateMysql.queryForList(sql, String.class);
		return result;
	}
	
	public int getDeviceCount_ColdChain() {
		String sql = "select count(*) from sensor_temperature_device";
		int count = jdbcTemplateMysql.queryForObject(sql, new Object[]{ }, Integer.class);
		
		return count;
	}
	
	public List<DeviceOnlineHistory> getDeviceOnlineHistory(Pagination page, String device_id) {
		String sql = "select id, device_id, is_online, ip, username, broker, create_stamp, online_stamp from device_online_history";

		StringBuilder sqlWhere = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlWhere.append(" where device_id = ?");
		params.add(device_id);

		//query for total
		String sql_count = "select count(*) from device_online_history";
		int total = jdbcTemplateMysql.queryForObject(sql_count + sqlWhere.toString(), params.toArray(), Integer.class);
		page.setTotal(total);
		
		sqlWhere.append(" order by ");
		sqlWhere.append(page.getOrder_by());
		sqlWhere.append(" ");
		sqlWhere.append(page.getAsc());
		
		//query for list
		List<DeviceOnlineHistory> list = page.queryPage(jdbcTemplateMysql, sql + sqlWhere.toString(), params.toArray(), DeviceOnlineHistory.MAP);

		return list;
	}
	
	public int setOnlineStatus(final String device_id, final String username, final int status, final long ts, final String ipaddress) {
		final String sql1 = "update device set online = ? where device_id = ?";
		final String sql2 = "insert into device_online_history(device_id, is_online, ip, username, broker, online_stamp, create_stamp) values(?, ?, ?, ?, ?, ?, now())";
		
		String tsString = String.format("%1$-13s", ts).replace(' ', '0');
		final Timestamp timestamp = new Timestamp(Long.valueOf(tsString));

		TransactionTemplate transTemp = new TransactionTemplate();
		transTemp.setTransactionManager(transactionManager);
		return transTemp.execute(new TransactionCallback<Integer>() {
            public Integer doInTransaction(TransactionStatus ts) {
                try {
            		int row = jdbcTemplateMysql.update(sql1, new Object[]{ status, device_id });
            		if(row > 0) {
            			row = jdbcTemplateMysql.update(sql2, new Object[]{ device_id, status, ipaddress, username, "", timestamp });
            		}
            		
                    return row;
                } catch (RuntimeException e) {
                    ts.setRollbackOnly();
                    return -1;
                }
            }
        });
	}
	
	public int addMqttMessage(final MqttMessage message) {
		final String sql_insert1 = "insert into message(device_id, app_name, app_type, topic, message, qos, payload, received, isvalid, message_stamp, create_stamp) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";
		final String sql_insert21 = "update device_messages set received = received + 1, update_stamp = now() where device_id = ? and app_name = ?";
		final String sql_insert221 = "update device_messages set transmitted = transmitted + 1, update_stamp = now() where device_id = ? and app_name = ?";
		final String sql_insert222 = "update device_messages set transmitted = transmitted + 1, received_invalid = ifnull(received_invalid, 0) + 1, update_stamp = now() where device_id = ? and app_name = ?";
		final String sql_insert23 = "insert device_messages(device_id, app_name, app_type, received, transmitted, received_invalid, update_stamp) values(?, ?, ?, ?, ?, ?, now())";

		// Transaction
		TransactionTemplate transTemp = new TransactionTemplate();
		transTemp.setTransactionManager(transactionManager);
		return transTemp.execute(new TransactionCallback<Integer>() {
			public Integer doInTransaction(TransactionStatus ts) {
				try {
					int row = jdbcTemplateMysql.update(sql_insert1, new Object[] { message.getDevice_id(), message.getApp_name(), message.getApp_type(), message.getTopic(), message.getMessage(), message.getQos(), message.getPayload(), (message.isReceived() ? 1 : 0), (message.isIsvalid() ? 1 : 0), message.getMessage_stamp() });
					if (row > 0) {
						if (message.isReceived()) {
							row = jdbcTemplateMysql.update(sql_insert21, new Object[] { message.getDevice_id(), message.getApp_name() });
						} else {
							if(message.isIsvalid()) {
								row = jdbcTemplateMysql.update(sql_insert221, new Object[] { message.getDevice_id(), message.getApp_name() });
							} else {
								row = jdbcTemplateMysql.update(sql_insert222, new Object[] { message.getDevice_id(), message.getApp_name() });
							}
						}
						
						if(row == 0) {
							if(message.getApp_name() != null && !message.getApp_name().isEmpty()) {
								final String sql_app = "select apptype from app where appname = ?";
								String tt = jdbcTemplateMysql.queryForObject(sql_app, new Object[]{ message.getApp_name() }, String.class);
								message.setApp_type(tt);
							}
							
							row = jdbcTemplateMysql.update(sql_insert23, new Object[] { message.getDevice_id(), message.getApp_name(), message.getApp_type(), (message.isReceived() ? 1 : 0), (message.isReceived() ? 0 : 1), (!message.isReceived() && message.isIsvalid() ? 1 : 0) });
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

	public List<MqttDeviceMessages> getDeviceMqttMessageStatistic(String device_id, String app_name) {
		List<MqttDeviceMessages> list;
		
		String sql = "select id, device_id, app_name, app_type, received, transmitted, received_invalid, update_stamp from device_messages where device_id = ?";
		if(app_name != null && !app_name.isEmpty()) {
			sql += " and app_name = ?";
			list = jdbcTemplateMysql.query(sql, new Object[]{ device_id, app_name }, MqttDeviceMessages.MAP);
		} else {
			list = jdbcTemplateMysql.query(sql, new Object[]{ device_id }, MqttDeviceMessages.MAP);
		}
			
		return list;
	}

	public List<MqttMessage> getDeviceMqttMessageList(Pagination page, String device_id, String app_name, int received) {
		List<MqttMessage> list;
		
		String sql = "select id, device_id, app_name, app_type, topic, message, qos, payload, received, isvalid, message_stamp, create_stamp from message";
		StringBuilder sqlWhere = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlWhere.append(" where received = ?");
		params.add(received);
		sqlWhere.append(" and device_id = ?");
		params.add(device_id);
		if(app_name != null && !app_name.isEmpty()) {
			sqlWhere.append(" and app_name = ?");
			params.add(app_name);
		}
		
		//query for total
		String sql_count = "select count(*) from message";
		int total = jdbcTemplateMysql.queryForObject(sql_count + sqlWhere.toString(), params.toArray(), Integer.class);
		page.setTotal(total);
		
		sqlWhere.append(" order by ");
		sqlWhere.append(page.getOrder_by());
		sqlWhere.append(" ");
		sqlWhere.append(page.getAsc());
		
		//query for list
		list = page.queryPage(jdbcTemplateMysql, sql + sqlWhere.toString(), params.toArray(), MqttMessage.MAP);

		return list;
	}
}
