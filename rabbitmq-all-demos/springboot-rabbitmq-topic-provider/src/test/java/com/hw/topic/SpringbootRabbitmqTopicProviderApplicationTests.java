package com.hw.topic;

import com.hw.topic.send.OrderSender;
import com.hw.topic.send.ProductSender;
import com.hw.topic.send.UserSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRabbitmqTopicProviderApplicationTests {

    @Autowired
    UserSender userSender;

    @Autowired
    ProductSender productSender;

    @Autowired
    OrderSender orderSender;

    /**
     * 测试消息队列
     */
    @Test
    public void test1() {
        this.userSender.send("user send .......");
        this.productSender.send("product send .......");
        this.orderSender.send("order send .......");
    }

}
