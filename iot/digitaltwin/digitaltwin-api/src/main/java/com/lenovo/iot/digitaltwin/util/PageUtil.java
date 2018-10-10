package com.lenovo.iot.digitaltwin.util;

/**
 * 分页工具
 *
 * @desc: com.lenovo.iot.digitaltwin.util.PageUtil
 * @author: chench9@lenovo.com
 * @date: 2018/6/28
 */
public class PageUtil {

    /**
     * 根据请求页数计算查询起始位置
     *
     * @param page
     * @return
     */
    public static int getStart(int page, int offset) {
        if (page <= 0) {
            return 0;
        }
        return (page - 1) * offset;
    }

    private PageUtil() {
    }

    /**
     * 计算总页数
     *
     * @param total
     * @param offset
     * @return
     */
    public static int total(int total, int offset) {
        if (total == 0) {
            return 1;
        }
        if (total % offset == 0) {
            return total / offset;
        } else {
            return total / offset + 1;
        }
    }

}
