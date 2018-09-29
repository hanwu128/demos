package com.example.demo.tsdb1;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.opentsdb.client.ExpectResponse;
import org.opentsdb.client.HttpClient;
import org.opentsdb.client.HttpClientImpl;
import org.opentsdb.client.builder.MetricBuilder;
import org.opentsdb.client.request.Filter;
import org.opentsdb.client.request.Query;
import org.opentsdb.client.request.QueryBuilder;
import org.opentsdb.client.request.SubQueries;
import org.opentsdb.client.response.Response;
import org.opentsdb.client.response.SimpleHttpResponse;
import org.opentsdb.client.util.Aggregator;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.*;

public class OpenTSDBUtil {
    private Logger LOG = LoggerFactory.getLogger(OpenTSDBUtil.class);
    private static String openTSDBUrl = "http://10.100.156.66:4242";
    private HttpClient httpClient;

    /**
     * 构造函数
     */
    public OpenTSDBUtil() {
        System.out.println("======================" + openTSDBUrl);
        this.httpClient = new HttpClientImpl(openTSDBUrl);

    }

    /**
     * 写入数据
     */
    public boolean putData(String metric, Date timestamp, Long value, Map<String, String> tagMap) throws IOException {
        long timeSecs = timestamp.getTime() / 1000;
        return this.putData(metric, timeSecs, value, tagMap);
    }

    public boolean pubData(String metric, Date timestamp, Double value, Map<String, String> tagMap) throws IOException {
        long timeSecs = timestamp.getTime() / 1000;
        return this.putData(metric, timeSecs, value, tagMap);
    }

    /**
     * 写入数据
     */
    public boolean putData(String metric, long timestamp, Long value, Map<String, String> tagMap) throws IOException {
        MetricBuilder builder = MetricBuilder.getInstance();
        builder.addMetric(metric).setDataPoint(timestamp, value).addTags(tagMap);
        try {
            LOG.debug("write quest: {}", builder.build());
            Response response = httpClient.pushMetrics(builder, ExpectResponse.SUMMARY);
            LOG.debug("response.statusCode: {}", response.getStatusCode());
            return response.isSuccess();
        } catch (IOException e) {
            LOG.error("put data to opentsdb error: ", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 写入数据
     */
    public boolean putData(String metric, long timestamp, Double value, Map<String, String> tagMap) throws IOException {
        MetricBuilder builder = MetricBuilder.getInstance();
        builder.addMetric(metric).setDataPoint(timestamp, value).addTags(tagMap);
        try {
            LOG.debug("write quest: {} ", builder.build());
            Response response = httpClient.pushMetrics(builder, ExpectResponse.SUMMARY);
            LOG.debug("response.statueCpde: {}", response.getStatusCode());
            return response.isSuccess();
        } catch (Exception error) {
            LOG.error("put data to opentsdb error: ", error);
            LOG.error(error.getMessage());
            error.printStackTrace();
            throw error;
        }
    }

    /**
     * 查询数据返回的数据为JSON格式
     *
     * @param metric     查询的指标
     * @param tagk       tagk
     * @param tagvFtype  tagv分过滤规则
     * @param tagvFilter tagv的匹配规则
     * @param aggregator 查询的聚合类型
     * @param downsample 采样的时间粒度
     * @param startTime  查询开始时间
     * @param endTime    查询结束时间
     * @return
     * @throws IOException
     */
    public String getData(String metric, String tagk, String tagvFtype, String tagvFilter, String aggregator, String downsample, String startTime, String endTime) throws IOException {
        QueryBuilder queryBuilder = QueryBuilder.getInstance();
        Query query = queryBuilder.getQuery();
        query.setStart(DateTimeUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss") / 1000);
        query.setEnd(DateTimeUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss") / 1000);
        List<SubQueries> sqList = new ArrayList<>();
        SubQueries sq = new SubQueries();
        sq.setMetric(metric);
        sq.setAggregator(aggregator);
        List<Filter> filters = new ArrayList<>();
        Filter filter = new Filter();
        filter.setTagk(tagk);
        filter.setType(tagvFtype);
        filter.setFilter(tagvFilter);
        filter.setGroupBy(Boolean.TRUE);
        filters.add(filter);
        sq.setFilters(filters);
        sq.setDownsample(downsample + "-" + aggregator);
        sqList.add(sq);
        query.setQueries(sqList);
        try {
            LOG.debug("query rqeust: {}", queryBuilder.build()); // 查询校验
            SimpleHttpResponse spHttpResponse = httpClient.pushQueries(queryBuilder, ExpectResponse.DETAIL);
            LOG.debug("response.content: {}", spHttpResponse.getContent());

            if (spHttpResponse.isSuccess()) {
                return spHttpResponse.getContent();
            }
            return null;
        } catch (IOException ioe) {
            LOG.error("get data from opentsdb error: ", ioe);
            throw ioe;
        }
    }

    /**
     * 查询数据，返回的数据为json格式
     *
     * @param metric     要查询的指标
     * @param filter     查询过滤的条件，原来使用的tags在v2.2后已不适用
     *                   filter.setType(): 设置过滤类型, 如: wildcard, regexp
     *                   filter.setTagk(): 设置tag
     *                   filter.setFilter(): 根据type设置tagv的过滤表达式, 如: hqdApp|hqdWechat
     *                   filter.setGroupBy():设置成true, 不设置或设置成false会导致读超时
     * @param aggregator 查询的聚合类型， 如： OpentsdbClient.AGGREGATOR_AVG, OpentsdbClient.AGGREGATOR_SUM
     * @param downsample 采样的时间粒度， 如： 1s, 2m, 1h, 1d, 2d
     * @param startTime  查询开始时间，时间格式为：yyyy-MM-dd HH:mm:ss
     * @param endTime    查询结束时间，时间格式为: yyyy-MM-dd HH:mm:ss
     * @return
     * @throws IOException
     */
    public String getData(String metric, Filter filter, String aggregator, String downsample, String startTime, String endTime) throws IOException {
        QueryBuilder queryBuilder = QueryBuilder.getInstance();
        Query query = queryBuilder.getQuery();
        query.setStart(DateTimeUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss") / 1000);
        query.setEnd(DateTimeUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss") / 1000);
        List<SubQueries> sqList = new ArrayList<>();
        SubQueries sq = new SubQueries();
        sq.addMetric(metric);
        sq.addAggregator(aggregator);

        List<Filter> filters = new ArrayList<>();
        filters.add(filter);
        sq.setFilters(filters);

        sq.setDownsample(downsample + "-" + aggregator);
        sqList.add(sq);
        query.setQueries(sqList);
        try {
            LOG.debug("query request: {}", queryBuilder.build());
            SimpleHttpResponse spHttpResponse = httpClient.pushQueries(queryBuilder, ExpectResponse.DETAIL);
            LOG.debug("response.content: {}", spHttpResponse.getContent());
            if (spHttpResponse.isSuccess()) {
                return spHttpResponse.getContent();
            }
            return null;
        } catch (IOException e) {
            LOG.error("get data from opentsdb error: ", e);
            throw e;
        }
    }

    /**
     * 查询时间，返回tags与时序值的映射：Map<tags, Map<时间点，value>>
     *
     * @param metric     查询指标
     * @param tagk       tagk
     * @param tagvFtype  tagv的过滤规则
     * @param tagvFilter tagv的匹配自字符
     * @param aggregator 查询的聚合类型，如： OpentsdbClient.AGGREGATOR_AVG, OpentsdbClient.AGGREGATOR_SUM
     * @param downsample 采样的时间粒度，如：1s, 2m, 1h, 1d, 2d
     * @param startTime  查询开始时间，时间格式为yyyy-MM-dd HH:mm:ss
     * @param endTime    查询结束时间，时间格式为yyyy-MM-dd HH:mm:ss
     * @param retTimeFmt 返回的结果集中，时间点的格式，如：yyyy-MM-dd HH:mm:ss 或 yyyyMMddHH 等
     * @return Map<tags               ,                               Map               <               时间点               ,                               value>>
     * @throws IOException
     */
    public Map<String, Map<String, Object>> getData(String metric, String tagk, String tagvFtype, String tagvFilter,
                                                    String aggregator, String downsample, String startTime,
                                                    String endTime, String retTimeFmt) throws IOException {
        String resContent = this.getData(metric, tagk, tagvFtype, tagvFilter, aggregator, downsample, startTime, endTime);
        return this.convertContentToMap(resContent, retTimeFmt);
    }

    /**
     * 查询数据，返回tags与时序值的映射： Map<tags, Map<时间点，value>>
     *
     * @param metric     要查询的指标
     * @param filter     查询过滤的条件，原来使用的tags在v2.2后已不适用
     *                   filter.setType(): 设置过滤类型, 如: wildcard, regexp
     *                   filter.setTagk(): 设置tag
     *                   filter.setFilter(): 根据type设置tagv的过滤表达式, 如: hqdApp|hqdWechat
     *                   filter.setGroupBy():设置成true, 不设置或设置成false会导致读超时
     * @param aggregator 查询的聚合类型， 如： OpentsdbClient.AGGREGATOR_AVG, OpentsdbClient.AGGREGATOR_SUM
     * @param downsample 采样的时间粒度， 如：1s, 2m, 1h, 1d, 2d
     * @param startTime  查询开始时间，时间格式为：yyyy-MM-dd HH:mm:ss
     * @param endTime    查询结束时间，时间格式为：yyyy-MM-dd HH:mm:ss
     * @param retTimeFmt 返回的结果集中，时间点的格式，如：yyyy-MM-dd HH:mm:ss 或 yyyyMMddHH 等
     * @return Map<String               ,                               Map               <               String               ,                               Object>>
     * @throws IOException
     */
    public Map<String, Map<String, Object>> getData(String metric, Filter filter, String aggregator, String downsample,
                                                    String startTime, String endTime, String retTimeFmt) throws IOException {
        String resContent = this.getData(metric, filter, aggregator, downsample, startTime, endTime);
        return this.convertContentToMap(resContent, retTimeFmt);
    }


    public Map<String, Map<String, Object>> convertContentToMap(String resContent, String retTimeFmt) {
        Map<String, Map<String, Object>> tagsValuesMap = new HashMap<>();

        if (resContent == null || "".equals(resContent.trim())) {
            return tagsValuesMap;
        }

        JSONArray array = (JSONArray) JSONObject.parse(resContent);
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                JSONObject tags = (JSONObject) obj.get("tags");
                JSONObject dps = (JSONObject) obj.get("dps");

                Map<String, Object> timeValueMap = new HashMap<>();
                for (Iterator<String> it = dps.keySet().iterator(); it.hasNext(); ) {
                    String timestamp = it.next();
                    Date datetime = new Date(Long.parseLong(timestamp) * 1000);
                    timeValueMap.put(DateTimeUtil.format(datetime, retTimeFmt), dps.get(timestamp));
                }
                tagsValuesMap.put(tags.toString(), timeValueMap);
            }
        }
        return tagsValuesMap;
    }

    public static void main(String[] args) {
        System.out.println(new Date().getTime());
        OpenTSDBUtil openTSDBUtil = new OpenTSDBUtil();
        try {
            // write
            //Map<String, String> tagMap = new HashMap<>();
            //tagMap.put("ch2", "ofo-App");
            //openTSDBUtil.putData("sys.cpu.user", 1530588074535L, 210l, tagMap);
            // read
            Filter filter = new Filter();
            filter.setType("regexp");
            filter.setTagk("ch2");
            filter.setFilter("ofo-App");
            filter.setGroupBy(Boolean.TRUE);
            String resContent = openTSDBUtil.getData("sys.cpu.user", filter, Aggregator.avg.name(), "1h",
                    "2016-07-03 09:20:00", "2018-07-03 11:22:00");
            System.out.println(resContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
