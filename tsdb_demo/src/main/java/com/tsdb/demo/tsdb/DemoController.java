package com.tsdb.demo.tsdb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private Config config;

    /**
     * 从TSDB查询数据
     * 查询条件实例ID、设备ID、指标、开始时间、结束时间
     */
    @GetMapping("/tsdb")
    private Object queryFromTsDb(Long dtInstanceId, String deviceId, String metric, Long start, Long end) {
        String tags = new StringBuilder()
                .append("{")
                .append("device_id=").append(deviceId).append(",")
                .append("dt_id=").append(dtInstanceId)
                .append("}")
                .toString();
        tags = CommonUtil.urlEncode(tags);
        String query = new StringBuilder()
                .append(config.getTsDbUrl())
                .append(":")
                .append(config.getTsDbPort())
                .append("/api/query?")
                .append("start=").append(start)
                .append("&end=").append(end)
                .append("&m=sum:").append(metric).append(tags)
                .toString();
        String result = TsDbUtil.query(query);
        if (result == null) {
            return new JSONArray();
        }

        JSONArray arr = JSONObject.parseArray(result);
        int size = arr.size();
        for (int i = 0; i < size; i++) {
            JSONObject json = arr.getJSONObject(i);
            json.put("aggregateTags", null);
            json.put("annotations", null);
            json.put("globalAnnotations", null);
        }
        return arr;
    }

}
