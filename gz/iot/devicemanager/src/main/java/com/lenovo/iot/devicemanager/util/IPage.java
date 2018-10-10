package com.lenovo.iot.devicemanager.util;

import java.util.List;


public interface IPage<Entity> extends List<Entity> {

    public List<Entity> getPageResult();

    public int getPageNo();

    public int getPageSize();

    public int getPageCount();

    public long getPageResultCount();

    public boolean hasPreviousPage();

    public boolean isFirstPage();

    public boolean hasNextPage();

    public boolean isLastPage();

    public boolean hasPageResult();

}
