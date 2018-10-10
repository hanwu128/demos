package com.lenovo.iot.devicemanager.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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

import com.lenovo.iot.devicemanager.model.DeviceEx;
import com.lenovo.iot.devicemanager.model.DeviceShadow;
import com.lenovo.iot.devicemanager.model.DeviceShadowEx;
import com.lenovo.iot.devicemanager.model.DeviceShadowItem;
import com.lenovo.iot.devicemanager.util.Pagination;

@Repository
public class DeviceShadowDao {

	private static final Logger log = LoggerFactory.getLogger(DeviceShadowDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplateMysql;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;

	public DeviceShadow getDeviceShadow(long company_id, String shadow_name) {
		String sql = "select id, shadow_name, device_id, company_id, app_name, shadow_desc, app_version, app_desc, create_stamp, update_stamp from device_shadow where company_id = ? and shadow_name = ?";
		List<DeviceShadow> list = jdbcTemplateMysql.query(sql, new Object[]{ company_id, shadow_name }, DeviceShadow.MAP);
		if(list != null && list.size() > 0 ){
			return (DeviceShadow)list.get(0);
		}
		
		return null;
	}
	
	public int addDeviceShadow(final DeviceShadow deviceShadow, final List<DeviceShadowItem> deviceShadowItems) {
		final String sql1 = "insert into device_shadow(shadow_name, device_id, company_id, app_name, shadow_desc, app_version, app_desc, create_stamp, update_stamp) values(?, ?, ?, ?, ?, ?, ?, now(), now())";
		final String sql2 = "insert into device_shadow_item(shadow_name, item_name, item_display_name, item_map_name, item_default, item_datatype, item_unit, create_stamp) values(?, ?, ?, ?, ?, ?, ?, now())";

		//Transaction
		TransactionTemplate transTemp = new TransactionTemplate();
		transTemp.setTransactionManager(transactionManager);
		return transTemp.execute(new TransactionCallback<Integer>() {
            public Integer doInTransaction(TransactionStatus ts) {
                try {
            		int row = jdbcTemplateMysql.update(sql1, new Object[]{ deviceShadow.getShadow_name(), deviceShadow.getDevice_id(), deviceShadow.getCompany_id(), deviceShadow.getApp_name(), deviceShadow.getShadow_desc(), deviceShadow.getApp_version(), deviceShadow.getApp_desc() });
            		if(row > 0 && deviceShadowItems != null) {
                		int rows[] = jdbcTemplateMysql.batchUpdate(sql2, new BatchPreparedStatementSetter() {
                			public void setValues(PreparedStatement ps, int i) throws SQLException {
                				// TODO Auto-generated method stub
                				DeviceShadowItem item = deviceShadowItems.get(i);
                				ps.setString(1, item.getShadow_name());
                				ps.setString(2, item.getItem_name());
                				ps.setString(3, item.getItem_display_name());
                				ps.setString(4, item.getItem_map_name());
                				ps.setString(5, item.getItem_default());
                				ps.setString(6, item.getItem_datatype());
                				ps.setString(7, item.getItem_unit());
                			}

                			public int getBatchSize() {
                				return deviceShadowItems.size();
                			}
                		});
                		
                		row = 0;
                		for(int r : rows) {
                			row += r;
                		}
                		return row;
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
	
	public int deleteDeviceShadow(final long company_id, final String shadow_name) {
		final String sql1 = "delete from device_shadow_item where shadow_name in (select shadow_name from device_shadow where company_id = ? and shadow_name = ?);";
		final String sql2 = "delete from device_shadow where company_id = ? and shadow_name = ?";
		
		//Transaction
		TransactionTemplate transTemp = new TransactionTemplate();
		transTemp.setTransactionManager(transactionManager);
		return transTemp.execute(new TransactionCallback<Integer>() {
            public Integer doInTransaction(TransactionStatus ts) {
                try {
            		int row = jdbcTemplateMysql.update(sql1, new Object[]{ company_id, shadow_name });
            		row = jdbcTemplateMysql.update(sql2, new Object[]{ company_id, shadow_name });
            		
                    return row;
                } catch (RuntimeException e) {
                    ts.setRollbackOnly();
                    log.debug(e.getMessage());
                    return -1;
                }
            }
        });
	}
	
	public int updateDeviceShadow(DeviceShadow deviceShadow) {
		String sql = "update device_shadow set device_id = ?, app_name = ?, shadow_desc = ?, app_version = ?, app_desc = ?, update_stamp = now() where shadow_name = ? and company_id = ?";
		int row = jdbcTemplateMysql.update(sql, new Object[]{ deviceShadow.getDevice_id(), deviceShadow.getApp_name(), deviceShadow.getShadow_desc(), deviceShadow.getApp_version(), deviceShadow.getApp_desc(), deviceShadow.getShadow_name(), deviceShadow.getCompany_id() });
    	
		return row;
	}
	
	public List<DeviceShadowEx> getDeviceShadowList(Pagination page, long company_id, String access_key) {
		List<DeviceShadowEx> list;
		
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql_where = new StringBuilder();
		
		String sql_list = "select a.id, a.shadow_name, a.device_id, a.company_id, a.app_name, a.shadow_desc, a.app_version, a.app_desc, a.create_stamp, a.update_stamp, b.access_key from device_shadow a left join device b on a.device_id = b.device_id";
		sql_where.append(" where 1 = 1");
		if(company_id > 0) {
			sql_where.append(" and a.company_id = ?");
			params.add(company_id);
		}
		if(access_key != null && !access_key.isEmpty()) {
			sql_where.append(" and (a.shadow_name like ? or b.access_key like ?)");
			params.add("%" + access_key + "%");
			params.add("%" + access_key + "%");
		}

		//query for total
		if(page != null) {
			String sql_count = "select count(a.id) from device_shadow a inner join device b on a.device_id = b.device_id";
			int total = jdbcTemplateMysql.queryForObject(sql_count + sql_where.toString(), params.toArray(), Integer.class);
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
		
		sql_where.append(" order by ");
		sql_where.append(order_by);
		sql_where.append(" ");
		sql_where.append(asc);

		//query for list
		if(page != null) {
			list = page.queryPage(jdbcTemplateMysql, sql_list + sql_where.toString(), params.toArray(), DeviceShadowEx.MAP);
		} else {
			list = jdbcTemplateMysql.query(sql_list + sql_where.toString(), params.toArray(), DeviceShadowEx.MAP);
		}
		
		return list;
	}
	
	public List<DeviceShadow> getDeviceShadowListByDeviceId(long company_id, String device_id) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		
		sql.append("select id, shadow_name, device_id, company_id, app_name, shadow_desc, app_version, app_desc, create_stamp, update_stamp from device_shadow");
		sql.append(" where 1 = 1");
		if(company_id > 0) {
			sql.append(" and company_id = ?");
			params.add(company_id);
		}
		if(device_id != null && !device_id.isEmpty()) {
			sql.append(" and device_id = ?");
			params.add(device_id);
		}

		List<DeviceShadow> list = jdbcTemplateMysql.query(sql.toString(), params.toArray(), DeviceShadow.MAP);
		
		return list;
	}

	public int getDeviceShadowCount(long company_id, String device_id, String app_name) {
		String sql = "select count(*) from device_shadow where company_id = ? and device_id = ? and app_name = ?";
		int count = jdbcTemplateMysql.queryForObject(sql, new Object[]{ company_id, device_id, app_name }, Integer.class);
		return count;
	}
}
