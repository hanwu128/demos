package enn.cn.dataimport;

/**
 * Created by Administrator on 2017/10/26.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class OpentsdbClientTest {
    private static Logger log = LoggerFactory.getLogger(OpentsdbClientTest.class);
    Properties prop = new Properties();

    public void testPutData() {
        OpentsdbClient client = new OpentsdbClient(prop.getProperty("opentsdb.url"));
        try {
            Map tagMap = new HashMap();
            tagMap.put("chl", "hqdApp");
            client.putData("metric-t", DateTimeUtil.parse("20160627 12:15", "yyyyMMdd HH:mm"), 210l, tagMap);
            client.putData("metric-t", DateTimeUtil.parse("20160627 12:17", "yyyyMMdd HH:mm"), 180l, tagMap);
            client.putData("metric-t", DateTimeUtil.parse("20160627 13:20", "yyyyMMdd HH:mm"), 180l, tagMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testGetData() {
        OpentsdbClient client = new OpentsdbClient(prop.getProperty("opentsdb.url"));
        try {
            Map tagMap = new HashMap();
            tagMap.put("chl", "hqdApp");
            String resContent = client.getData("metric-t", tagMap, OpentsdbClient.AGGREGATOR_SUM, "1h", "2016-06-27 12:00:00", "2016-06-30 13:00:00");
            log.info(">>>" + resContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testGetData2() {
        OpentsdbClient client = new OpentsdbClient(prop.getProperty("opentsdb.url"));
        try {
            Map tagMap = new HashMap();
            tagMap.put("chl", "hqdApp");
            Map<String,Map>tagsValuesMap = client.getData("metric-t", tagMap, OpentsdbClient.AGGREGATOR_SUM, "1h", "2016-06-27 10:00:00", "2016-06-30 11:00:00", "yyyyMMdd hh");
            for (Iterator it = tagsValuesMap.keySet().iterator(); it.hasNext(); ) {
                String tags = it.next().toString();
                System.out.println(">> tags: " + tags);
                Map<String,Object> tvMap = tagsValuesMap.get(tags);
                for (Iterator it2 = tvMap.keySet().iterator(); it2.hasNext(); ) {
                    String time = it2.next().toString();
                    System.out.println(" >> " + time + " <-> " + tvMap.get(time));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
