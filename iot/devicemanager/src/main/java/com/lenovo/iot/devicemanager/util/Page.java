package com.lenovo.iot.devicemanager.util;


/**
 * 分页工具类
 * @author blatter
 * @date 2015年7月28日 下午2:30:08
 */
public class Page {

    /** 页码 */
    private int pageNo = 1;

    /** 每页数量 */
    private int pageSize = 5;

    /** 记录开始索引 */
    private int pageOffset;

    /** 总页数 */
    private int pageCount;

    /** 总记录数 */
    private long pageResultCount;

    /** 是否显示sql */
    private boolean pageShowSql = false;

    public int getPageNo() {
        return pageNo;
    }


    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
        this.setPageOffset((this.getPageNo() - 1) * this.getPageSize());
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(int pageOffset) {
        this.pageOffset = pageOffset;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public long getPageResultCount() {
        return pageResultCount;
    }

    public void setPageResultCount(long pageResultCount) {
        this.pageResultCount = pageResultCount;
    }

    public boolean isPageShowSql() {
        return pageShowSql;
    }

    public void setPageShowSql(boolean pageShowSql) {
        this.pageShowSql = pageShowSql;
    }
}
