package com.lenovo.iot.devicemanager.util;


import java.util.ArrayList;
import java.util.List;


public class PageImpl<Entity> extends ArrayList<Entity> implements IPage<Entity> {

    private static final long serialVersionUID = -5707283913969397010L;

    private List<Entity> pageResult;

    private int pageOffset = 0;

    private int pageNo;

    private int pageSize;

    private int pageCount;

    private long pageResultCount;

    public PageImpl(List<Entity> pageResult, int pageNo, int pageSize, int pageCount, long pageResultCount) {
        this.pageResult = pageResult;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.pageResultCount = pageResultCount;
    }

    public List<Entity> getPageResult() {
        return pageResult;
    }

    public void setPageResult(List<Entity> pageResult) {
        this.pageResult = pageResult;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public boolean hasPreviousPage() {
        return 1 != pageNo;
    }

    public boolean isFirstPage() {
        return 1 == pageNo;
    }

    public boolean hasNextPage() {
        return pageCount != pageNo;
    }

    public boolean isLastPage() {
        return pageCount == pageNo;
    }

    public boolean hasPageResult() {
        if (pageResult == null || pageResult.isEmpty()) {
            return false;
        }
        return true;
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

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public String toString() {
        return "page {\n" +
                "\tpageResult=" + pageResult +
                ", \n\tpageOffset=" + pageOffset +
                ", \n\tpageNo=" + pageNo +
                ", \n\tpageSize=" + pageSize +
                ", \n\tpageCount=" + pageCount +
                ", \n\tpageResultCount=" + pageResultCount +
                "\n}";
    }
}

