package com.lenovo.iot.digitaltwin.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.digitaltwin.config.Config;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Desc: TSDB工具类
 * Name: com.lenovo.iot.digitaltwin.util.TsDbUtil
 * Author: hanwu
 * Date: 2018/7/6 11:12
 **/
@Component
public class TsDbUtil {
    private static final Logger logger = LoggerFactory.getLogger(TsDbUtil.class);

    @Autowired
    private Config config;

    private static TsDbUtil tsDbUtil;

    @PostConstruct
    public void init() {
        tsDbUtil = this;
    }

    /**
     * 查询TSDB数据，GET方式
     *
     * @param url
     * @return
     */
    public static String getQuery(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseBody = null;
        try {
            HttpGet httpget = new HttpGet(url);
            httpget.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
            // 创建一个自定义响应处理程序
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(
                        final HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (HttpServletResponse.SC_BAD_REQUEST == status) {
                        logger.error("tsdb query is failed:{}", EntityUtils.toString(response.getEntity()));
                        //throw new IllegalArgumentException("tsdb query is failed");
                        //tsdb状态码为400时返回null
                        return null;
                    }
                    if (HttpServletResponse.SC_OK == status) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        logger.warn("Not found in opentsdb!");
                        return null;
                    }
                }
            };
            responseBody = client.execute(httpget, responseHandler);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                logger.error("tsdb query is failed:{}", e.getMessage());
                throw (IllegalArgumentException) e;
            }
            logger.error("failed to create opentsdb connection:{}", e.getMessage());
            e.printStackTrace();
        } finally {
            IOUtil.closeCloseableHttpClient(client);
        }
        return responseBody;
    }

    /**
     * 查询TSDB数据，POST方式
     *
     * @param params
     * @return
     */
    public static String postQuery(String params) {
        String url = new StringBuilder()
                .append(tsDbUtil.config.getTsDbUrl())
                .append(":")
                .append(tsDbUtil.config.getTsDbPort())
                .append("/api/query").toString();
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            post.setEntity(new ByteArrayEntity(params.getBytes("UTF-8"), ContentType.APPLICATION_JSON));
            response = httpClient.execute(post);
            if (response != null) {
                int status = response.getStatusLine().getStatusCode();
                if (status == HttpServletResponse.SC_BAD_REQUEST) {
                    logger.error("tsdb query is failed:{}", EntityUtils.toString(response.getEntity()));
                    //throw new IllegalArgumentException("query is failed");
                    //tsdb状态码为400时返回null
                    return null;
                }
                if (status == HttpServletResponse.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        result = EntityUtils.toString(entity);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                logger.error("tsdb query is failed:{}", e.getMessage());
                throw (IllegalArgumentException) e;
            }
            logger.error("tsdb query failed: {},{}", params, e.getMessage());
            e.printStackTrace();
        } finally {
            IOUtil.closeCloseableHttpResponse(response);
        }
        return null;
    }

    /**
     * 从TSDB查询数据，GET方式
     * * @param id
     * * @param dtInstanceId
     * * @param deviceId
     * * @param metric
     * * @param start
     * * @param end
     * * @return
     */
    public static Object getQueryFromTsDb(Long id, String dtInstanceName, String deviceId, String metric,
                                          Object start, Object end, String downsample, String aggregator) {
        if (logger.isDebugEnabled()) {
            logger.debug("tsdb parameter : {},{},{},{},{},{},{},{}", id, dtInstanceName, deviceId, metric, start, end, downsample, aggregator);
        }
        boolean msResolution = false;
        if (CommonUtil.isNumeric(start.toString())) {
            //默认安全时间，大于或等于12位为毫秒级时间戳
            if (String.valueOf(start).length() >= 12) {
                msResolution = true;
            }
        }
        if (CommonUtil.isNumeric(start.toString()) && (end == null || "".equals(String.valueOf(end)))) {
            end = new Date().getTime();
        }
        String tags = new StringBuilder()
                .append("{")
                .append("device_id=").append(deviceId).append(",")
                //.append("dt_id=").append(dtInstanceId)
                .append("dt_name=").append(CommonUtil.formatDtName(dtInstanceName))
                .append("}")
                .toString();
        tags = CommonUtil.urlEncode(tags);
        StringBuilder query = null;
        query = new StringBuilder()
                .append(tsDbUtil.config.getTsDbUrl())
                .append(":")
                .append(tsDbUtil.config.getTsDbPort())
                .append("/api/query?")
                .append("start=").append(CommonUtil.urlEncode(String.valueOf(start)));
        if (null != end && !"".equals(String.valueOf(end))) {
            query.append("&end=").append(CommonUtil.urlEncode(String.valueOf(end)));
        }
        if (msResolution) {
            query.append("&ms=").append(msResolution);
        }
        if (!StringUtils.isEmpty(aggregator)) {
            query.append("&m=").append(aggregator);
        } else {
            query.append("&m=none");
        }
        if (!StringUtils.isEmpty(downsample)) {
            query.append(":").append(downsample);
        }
        query.append(":").append(metric).append(tags);
        String result = getQuery(query.toString());
        if (StringUtils.isEmpty(result)) {
            logger.warn("tsdb query result is null: {},{},{},{},{},{},{},{}", id, dtInstanceName, deviceId, metric, start, end, downsample, aggregator);
            return null;
        }
        /*JSONArray arr = JSONObject.parseArray(result);
        int size = arr.size();
        for (int i = 0; i < size; i++) {
            JSONObject json = arr.getJSONObject(i);
            json.put("aggregateTags", null);
            json.put("annotations", null);
            json.put("globalAnnotations", null);
            // 如下方法取出的map不是有序的
            Map<String, Object> map = json.getJSONObject("dps").getInnerMap();

            // 如下是根据fast json官网的说法设置排序，并未生效
            //json.put("dps", JSONObject.parseObject(JSONObject.toJSONString(map, SerializerFeature.SortField), Feature.OrderedField));

            // 通过tree map进行排序
            map = CommonUtil.sortMapByKey(map);
            json.put("dps", map);
        }
        return arr;*/
        return formatQueryTsdbResult(result);
    }

    /**
     * 从TSDB查询数据，POST方式
     * * @param id
     * * @param dtInstanceId
     * * @param deviceId
     * * @param metric
     * * @param start
     * * @param end
     * * @return
     */
    public static Object postQueryFromTsDb(Long id, String dtInstanceName, String deviceId, String metric,
                                           Object start, Object end, String downsample, String aggregator) {
        if (logger.isDebugEnabled()) {
            logger.debug("tsdb parameter : {},{},{},{},{},{},{},{}", id, dtInstanceName, deviceId, metric, start, end, downsample, aggregator);
        }
        boolean msResolution = false;
        if (CommonUtil.isNumeric(start.toString())) {
            //默认安全时间，大于或等于12位为毫秒级时间戳
            if (String.valueOf(start).length() >= 12) {
                msResolution = true;
            }
        }
        if (CommonUtil.isNumeric(start.toString()) && (end == null || "".equals(String.valueOf(end)))) {
            end = new Date().getTime();
        }
        if (StringUtils.isEmpty(aggregator)) {
            aggregator = "none";
        }
        //filter过滤参数
        JSONObject filter = new JSONObject();
        filter.put("device_id", deviceId);
        filter.put("dt_name", dtInstanceName);
        //queries参数
        JSONObject objQueries = new JSONObject();
        objQueries.put("aggregator", aggregator);
        objQueries.put("metric", metric);
        objQueries.put("downsample", downsample);
        objQueries.put("filter", filter);
        JSONArray queries = new JSONArray();
        queries.add(objQueries);
        //post请求参数
        JSONObject objParams = new JSONObject();
        objParams.put("start", start);
        objParams.put("end", end);
        objParams.put("msResolution", msResolution);
        objParams.put("queries", queries);
        String result = postQuery(objParams.toJSONString());
        if (StringUtils.isEmpty(result)) {
            logger.warn("tsdb query result is null: {},{},{},{},{},{},{},{}", id, dtInstanceName, deviceId, metric, start, end, downsample, aggregator);
            return new JSONArray();
        }
        /*JSONArray arr = JSONObject.parseArray(result);
        int size = arr.size();
        for (int i = 0; i < size; i++) {
            JSONObject json = arr.getJSONObject(i);
            json.put("aggregateTags", null);
            json.put("annotations", null);
            json.put("globalAnnotations", null);
            // 如下方法取出的map不是有序的
            Map<String, Object> map = json.getJSONObject("dps").getInnerMap();
            // 通过tree map进行排序
            map = CommonUtil.sortMapByKey(map);
            json.put("dps", map);
        }
        return arr;*/
        return formatQueryTsdbResult(result);
    }

    private static Object formatQueryTsdbResult(String result) {
        JSONArray arr = JSONObject.parseArray(result);
        int size = arr.size();
        for (int i = 0; i < size; i++) {
            JSONObject json = arr.getJSONObject(i);
            json.put("aggregateTags", null);
            json.put("annotations", null);
            json.put("globalAnnotations", null);
            // 如下方法取出的map不是有序的
            Map<String, Object> map = json.getJSONObject("dps").getInnerMap();

            // 如下是根据fast json官网的说法设置排序，并未生效
            //json.put("dps", JSONObject.parseObject(JSONObject.toJSONString(map, SerializerFeature.SortField), Feature.OrderedField));

            // 通过tree map进行排序
            map = CommonUtil.sortMapByKey(map);
            json.put("dps", map);
        }
        return arr;
    }

}
