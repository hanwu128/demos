package com.lenovo.iot.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

//Get page
public class Pagination {
    protected static final Log log = LogFactory.getLog(Pagination.class);

    private int pagesize = 0;
	private int current  = 0;
	private int total= 0;
	private String order_by;
	private String asc;

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		if(pagesize == 0) {
			pagesize = 20;
		}
		this.pagesize = pagesize;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		if(current == 0) {
			current = 1;
		}
		this.current = current;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotal() {
		return this.total;
	}
	
	public String getOrder_by() {
		return order_by;
	}

	public void setOrder_by(String order_by) {
		this.order_by = order_by;
	}

	public String getAsc() {
		return asc;
	}

	public void setAsc(String asc) {
		if(asc == null || asc.isEmpty() || asc.equalsIgnoreCase("descending")) {
			asc = "desc";
		} else {
			asc = "asc";
		}
		
		this.asc = asc;
	}
	
	public Pagination() { }
	
//	public Pagination(int current) {
//		this.pageSize = 20;
//		this.current = current;
//	}
	
//	public Pagination(int pageSize, int current) {
//		this.pageSize = pageSize;
//		this.current = current;
//	}
	
	//Run query
	public <T> List<T> queryPage(final JdbcTemplate jt, final String sql1, Object[] arg, RowMapper<T> resultType){
    	int end = pagesize;
		int start = (current - 1) * pagesize;
		String queryStr = sql1 + " limit " + start + " , " + end;
		
		if(arg == null){
			return jt.query(queryStr, resultType);
		} else {
			return jt.query(queryStr, arg, resultType);
		}
	}
}
