package com.lenovo.iot.device.dao;

import com.lenovo.iot.device.model.Device;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@Repository
public class DeviceDao {
	@Autowired
	private JdbcTemplate jdbcTemplateMysql;
	
//	@Autowired
//	private DataSourceTransactionManager transactionManager;

	public Device getDevice(String device_id) {
		String sql = "select id, device_id, company_id, policy_name, access_key, secret_key, topic_group, device_desc, create_stamp, update_stamp, online from device where device_id = ?";
		List<Device> list = this.jdbcTemplateMysql.query(sql, new Object[] { device_id }, Device.MAP);
		if ((list != null) && (list.size() > 0)) {
			return (Device) list.get(0);
		}
		return null;
	}

	public Device getDeviceByAccessKey(long company_id, String access_key) {
		String sql = "select id, device_id, company_id, policy_name, access_key, secret_key, topic_group, device_desc, create_stamp, update_stamp, online from device where company_id = ? and access_key = ?";
		List<Device> list = this.jdbcTemplateMysql.query(sql, new Object[] { Long.valueOf(company_id), access_key }, Device.MAP);
		if ((list != null) && (list.size() > 0)) {
			return (Device) list.get(0);
		}
		return null;
	}

	public int addDevice(Device device) {
		String sql = "insert into device(device_id, company_id, policy_name, access_key, secret_key, topic_group, device_desc, create_stamp, update_stamp, online) values(?, ?, ?, ?, ?, ?, ?, now(), now(), 0)";
		int row = this.jdbcTemplateMysql.update(sql, new Object[] { device.getDevice_id(), Long.valueOf(device.getCompany_id()), device.getPolicy_name(), device.getAccess_key(), device.getSecret_key(), device.getGroup(), device.getDevice_desc() });

		return row;
	}

	public int updateDeviceDesc(String device_id, String device_desc) {
		String sql = "update device set device_desc = ?, update_stamp=now() where device_id = ?";
		int row = this.jdbcTemplateMysql.update(sql, new Object[] { device_desc, device_id });

		return row;
	}

//	public int setOnlineStatus(final String device_id, final String username, final int status, long ts, final String ipaddress) {
//		final String sql1 = "update device set online = ? where device_id = ?";
//		final String sql2 = "insert into device_online_history(device_id, is_online, ip, username, broker, online_stamp, create_stamp) values(?, ?, ?, ?, ?, ?, now())";
//
//		String tsString = String.format("%1$-13s", new Object[] { Long.valueOf(ts) }).replace(' ', '0');
//		final Timestamp timestamp = new Timestamp(Long.valueOf(tsString).longValue());
//
//		TransactionTemplate transTemp = new TransactionTemplate();
//		transTemp.setTransactionManager(this.transactionManager);
//		return transTemp.execute(new TransactionCallback<Integer>() {
//			public Integer doInTransaction(TransactionStatus ts) {
//				try {
//					int row = DeviceDao.this.jdbcTemplateMysql.update(sql1, new Object[] { Integer.valueOf(status), device_id });
//					if (row > 0) {
//						row = DeviceDao.this.jdbcTemplateMysql.update(sql2, new Object[] { device_id, Integer.valueOf(status), ipaddress, username, "", timestamp });
//					}
//					return Integer.valueOf(row);
//				} catch (RuntimeException e) {
//					ts.setRollbackOnly();
//				}
//				return Integer.valueOf(-1);
//			}
//		});
//	}
}
