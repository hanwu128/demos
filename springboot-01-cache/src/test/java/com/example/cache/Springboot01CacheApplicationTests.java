package com.example.cache;

import com.example.cache.bean.Employee;
import com.example.cache.mapper.EmployeeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot01CacheApplicationTests {

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * String(字符串)、List(列表)、Set(集合)、Hash(散列)、ZSet(有序集合)
     */
    @Test
    public void test01() {
        //给redis保存数据
        stringRedisTemplate.opsForValue().append("aaa","test");

        //从redis读数据
        String str = stringRedisTemplate.opsForValue().get("aaa");
        System.out.println(str);
    }

    @Test
    public void contextLoads() {

        Employee employee = employeeMapper.getEmpById(1);
        System.out.println("======" + employee);
    }

}
