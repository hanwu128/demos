package com.hw.blog.util;

import com.alibaba.fastjson.JSONObject;

public class JsonList {
    private int total = 0;  //总记录数
    private int pageTotal = 0;  //总页数
    private int page = 1;   //页数
    private int offset = 10; //每页记录数
    private Object rows = new JSONObject();

    public JsonList(int total, int pageTotal, int page, int offset, Object rows) {
        this.total = total;
        this.pageTotal = pageTotal;
        this.page = page;
        this.offset = offset;
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pagetotal) {
        this.pageTotal = pagetotal;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }
}
