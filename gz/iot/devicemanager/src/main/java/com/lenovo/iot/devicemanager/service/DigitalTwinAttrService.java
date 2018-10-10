package com.lenovo.iot.devicemanager.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenovo.iot.devicemanager.config.TsDb;
import com.lenovo.iot.devicemanager.dao.DigitalTwinAttrDao;
import com.lenovo.iot.devicemanager.model.DigitalTwinAttrParam;
import com.lenovo.iot.devicemanager.model.DigitalTwinAttrValueMeta;
import com.lenovo.iot.devicemanager.util.Md5;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
public class DigitalTwinAttrService {
    private static final Logger logger = LoggerFactory.getLogger(DigitalTwinAttrService.class);

    @Autowired
    private TsDb tsDb;

    @Autowired
    private DigitalTwinAttrDao digitalTwinAttrDao;

    @Autowired
    private SqlSessionTemplate sqlSession;

    public Integer addDigitalTwinAttr(List<DigitalTwinAttrParam> list) {
        return digitalTwinAttrDao.addDigitalTwinAttr(list);
        // 使用xml映射器实现批量添加并返回各记录主键字段值
        //return this.sqlSession.insert("com.lenovo.iot.devicemanager.dao.DigitalTwinAttrDao.addDigitalTwinAttr", list);
    }

    public Integer updateDigitalTwinAttr(List<DigitalTwinAttrParam> list) {
        List<DigitalTwinAttrParam> addDigitalTwinAttrList = new LinkedList<DigitalTwinAttrParam>();
        int size = list.size();
        for(int i = 0; i < size; i++) {
            DigitalTwinAttrParam digitalTwinAttr = list.get(i);
            if(digitalTwinAttr.getId() != null && digitalTwinAttr.getId() > 0) {
                digitalTwinAttrDao.updateDigitalTwinAttr(digitalTwinAttr);
            }else {
                addDigitalTwinAttrList.add(digitalTwinAttr);
            }
        }
        if(!addDigitalTwinAttrList.isEmpty()) {
            //digitalTwinAttrDao.addDigitalTwinAttr(addDigitalTwinAttrList);
            addDigitalTwinAttr(addDigitalTwinAttrList);
            updateMetricValue(addDigitalTwinAttrList);
        }
        return 1;
    }

    /**
     * 异步更新Metric值
     * @param addDigitalTwinAttrList
     */
    private void updateMetricValue(List<DigitalTwinAttrParam> addDigitalTwinAttrList) {
        if(addDigitalTwinAttrList == null) {
            return;
        }

        Map<String, DigitalTwinAttrValueMeta> metaMap = new HashMap<String, DigitalTwinAttrValueMeta>();
        JSONArray metricArr = new JSONArray();
        for(DigitalTwinAttrParam dtp : addDigitalTwinAttrList) {
            JSONObject metric = new JSONObject();
            metric.put("metric", dtp.getMetric().trim());
            metric.put("tags", JSONObject.parseObject(dtp.getTagkv()));
            metricArr.add(metric);
            metaMap.put(buildMetricKey(dtp.getMetric(), dtp.getTagkv()), new DigitalTwinAttrValueMeta(dtp.getId()));
        }

        JSONObject queryJson = new JSONObject();
        queryJson.put("resolveNames", true);
        queryJson.put("queries", metricArr);
        queryJson.put("backScan", 240);

        CloseableHttpResponse response = null;
        HttpEntity respEntity = null;
        try {
            HttpPost post = null;
            CloseableHttpClient httpclient = HttpClients.createDefault();
            post = new HttpPost(tsDb.getUrlQueryMetric());
            ByteArrayEntity reqEntity = new ByteArrayEntity(queryJson.toJSONString().getBytes("UTF-8"), ContentType.APPLICATION_JSON);
            post.setEntity(reqEntity);
            response = httpclient.execute(post);

            if(response.getStatusLine().getStatusCode() == HttpServletResponse.SC_BAD_REQUEST) {
                logger.error(String.format("metric tags value not found! query json: %s", queryJson));
                return;
            }

            respEntity = response.getEntity();
            String body = EntityUtils.toString(respEntity);
            if(body == null || "".equals(body.trim())) {
                logger.warn(String.format("metric tags value not found! query json: %s", queryJson));
                return;
            }

            JSONArray jsonArr = JSONObject.parseArray(body);
            int size = jsonArr.size();
            for(int i = 0; i < size; i++) {
                JSONObject json = jsonArr.getJSONObject(i);
                String key = buildMetricKey(json.getString("metric"), json.getJSONObject("tags").toJSONString());
                DigitalTwinAttrValueMeta dtvm = metaMap.get(key);
                if(dtvm == null) {
                    continue;
                }
                dtvm.setValue(json.getDoubleValue("value"));
                dtvm.setValuetimestamp(json.getLong("timestamp"));
            }
            updateDigitalTwinAttrValue(metaMap);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(respEntity != null) {
                    EntityUtils.consume(respEntity);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String buildMetricKey(String metric, String tagkv) {
        String str = new StringBuilder().append(metric).append(tagkv).toString();
        return Md5.encryption(str);
    }

    private void updateDigitalTwinAttrValue(Map<String, DigitalTwinAttrValueMeta> metaMap) {
        Iterator<String> it = metaMap.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next();
            DigitalTwinAttrValueMeta meta = metaMap.get(key);
            digitalTwinAttrDao.updateDigitalTwinAttrValue(meta);
        }
    }

    public List<DigitalTwinAttrParam> getDigitalTwinAttrList(long daddyid) {
        return digitalTwinAttrDao.getDigitalTwinAttrList(daddyid);
    }

}
