package com.lenovo.iot.devicemanager.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.javatuples.Pair;
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

import com.lenovo.iot.devicemanager.model.DeviceShadowItem;

@Repository
public class DeviceShadowItemDao {

	private static final Logger log = LoggerFactory.getLogger(DeviceShadowItemDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplateMysql;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	public DeviceShadowItem getDeviceShadowItem(String shadow_name, String item_name) {
		String sql = "select id, shadow_name, item_name, item_display_name, item_map_name, item_default, item_datatype, item_unit, create_stamp, item_value, itemvalue_stamp, expect_value, expectvalue_stamp from device_shadow_item where shadow_name = ? and item_name = ?";
		List<DeviceShadowItem> list = jdbcTemplateMysql.query(sql, new Object[]{ shadow_name, item_name }, DeviceShadowItem.MAP);
		if(list != null && list.size() > 0 ){
			return (DeviceShadowItem)list.get(0);
		}
		
		return null;
	}
	
	public int addDeviceShadowItem(DeviceShadowItem deviceShadowItem) {
		String sql = "insert into device_shadow_item(shadow_name, item_name, item_display_name, item_map_name, item_default, item_datatype, item_unit, create_stamp) values(?, ?, ?, ?, ?, ?, ?, now())";
		int row = jdbcTemplateMysql.update(sql, new Object[]{ deviceShadowItem.getShadow_name(), deviceShadowItem.getItem_name(), deviceShadowItem.getItem_display_name(), deviceShadowItem.getItem_map_name(), deviceShadowItem.getItem_default(), deviceShadowItem.getItem_datatype(), deviceShadowItem.getItem_unit() });
    	
		return row;
	}
	
	public int deleteDeviceShadowItem(String shadow_name, String item_name) {
		String sql = "delete from device_shadow_item where shadow_name = ? and item_name = ?";
		int row = jdbcTemplateMysql.update(sql, new Object[]{ shadow_name, item_name });
    	
		return row;
	}
	
	public int updateDeviceShadowItemMapName(DeviceShadowItem deviceShadowItem) {
		String sql = "update device_shadow_item set item_map_name = ? where shadow_name = ? and item_name = ?";
		int row = jdbcTemplateMysql.update(sql, new Object[]{ deviceShadowItem.getItem_map_name(), deviceShadowItem.getShadow_name(), deviceShadowItem.getItem_name() });
    	
		return row;
	}
	
	public int updateDeviceShadowValue(final List<String> shadow_names, final List<Pair<String, String>> pairs) {
		//Transaction
		TransactionTemplate transTemp = new TransactionTemplate();
		transTemp.setTransactionManager(transactionManager);
		return transTemp.execute(new TransactionCallback<Integer>() {
            public Integer doInTransaction(TransactionStatus ts) {
                try {
                	StringBuilder shadow_name_where = new StringBuilder();
                	shadow_name_where.append("(");
                	for(int i = 0; i < shadow_names.size(); i++) {
                		shadow_name_where.append("shadow_name = ?");
                    	if(i < shadow_names.size() - 1) {
                    		shadow_name_where.append(" or ");
                    	}
                	}
                	shadow_name_where.append(")");
                	String sql = "update device_shadow_item set item_value = ?, itemvalue_stamp = now() where item_map_name = ? and " + shadow_name_where.toString();
            		int rows[] = jdbcTemplateMysql.batchUpdate(sql, new BatchPreparedStatementSetter() {
            			public void setValues(PreparedStatement ps, int i) throws SQLException {
            				// TODO Auto-generated method stub
            				Pair<String, String> pair = pairs.get(i);
            				ps.setString(1, pair.getValue1());
            				ps.setString(2, pair.getValue0());
            				for(int ii = 0;ii < shadow_names.size(); ii++) {
            					ps.setString(ii + 3, shadow_names.get(ii));
            				}
            			}

            			public int getBatchSize() {
            				return pairs.size();
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
	
	public int updateDeviceShadowExpectValue(final List<String> shadow_names, final List<Pair<String, String>> pairs) {
		//Transaction
		TransactionTemplate transTemp = new TransactionTemplate();
		transTemp.setTransactionManager(transactionManager);
		return transTemp.execute(new TransactionCallback<Integer>() {
            public Integer doInTransaction(TransactionStatus ts) {
                try {                	
           			StringBuilder shadow_name_where = new StringBuilder();
                	shadow_name_where.append("(");
                	for(int i = 0; i < shadow_names.size(); i++) {
                    	shadow_name_where.append("shadow_name = ?");
                    	if(i < shadow_names.size() - 1) {
                    		shadow_name_where.append(" or ");
                    	}
                	}
                	shadow_name_where.append(")");
                	
            		String sql1 = "update device_shadow set update_stamp = now() where " + shadow_name_where.toString();
            		int row = jdbcTemplateMysql.update(sql1, shadow_names.toArray());
            		
            		
                	String sql2 = "update device_shadow_item set expect_value = ?, expectvalue_stamp = now() where item_name = ? and " + shadow_name_where.toString();
            		int rows[] = jdbcTemplateMysql.batchUpdate(sql2, new BatchPreparedStatementSetter() {
            			public void setValues(PreparedStatement ps, int i) throws SQLException {
            				// TODO Auto-generated method stub
            				Pair<String, String> pair = pairs.get(i);
            				ps.setString(1, pair.getValue1());
            				ps.setString(2, pair.getValue0());
            				for(int ii = 0;ii < shadow_names.size(); ii++) {
            					ps.setString(ii + 3, shadow_names.get(ii));
            				}
            			}

            			public int getBatchSize() {
            				return pairs.size();
            			}
            		});
            		
            		row = 0;
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
	
	public List<DeviceShadowItem> getDeviceShadowItemList(long company_id, String shadow_name) {
		String sql = "select id, shadow_name, item_name, item_display_name, item_map_name, item_default, item_datatype, item_unit, create_stamp, item_value, itemvalue_stamp, expect_value, expectvalue_stamp from device_shadow_item where shadow_name = ?";
		List<DeviceShadowItem> list = jdbcTemplateMysql.query(sql, new Object[]{ shadow_name }, DeviceShadowItem.MAP);
		return list;
	}
}
