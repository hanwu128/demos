package devicemanager;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

public class JsonTest extends TestCase {

    public void testStrToJson() {
        String str = "{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}";
        JSONObject json = JSONObject.parseObject(str);
        System.out.println(json);
    }
}
