package com.hw.provider;

import com.hw.provider.send.Sender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRabbitmqDirectProviderApplicationTests {

    @Autowired
    Sender sender;

    /**
     * 测试消息队列
     */
    @Test
    public void test1() {
        while (true){
            this.sender.send("Hello RabbitMQ !");
        }
    }

}
