package com.example.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 一、搭建基本环境
 * 1、导入数据库文件，创建出department和employee表
 * 2、创建javabean封装数据
 * 3、整合MyBatis操作数据库
 *      1)配置数据源信息
 *      2)使用注解版的MyBatis
 *          1、@MapperScan指定需要扫描的mapper接口所在的包
 * 二、快速体验缓存
 *      步骤:
 *          1、开启基于注解的缓存@EnableCaching
 *          2、标注缓存注解即可
 *              @Cacheable  开启缓存
 *              @CacheEvict  清除缓存
 *              @CachePut  更新缓存
 *
 * 默认使用的是ConcurrentMapCacheManager，将数据保存在ConcurrentMap中；
 * 开发中使用缓存中间件redia
 *
 * 三、整合redia作为缓存
 * Redis 是一个开源（BSD许可）的，内存中的数据结构存储系统，它可以用作数据库、缓存和消息中间件。
 *  1、安装redis
 *  2、引入redis的starter
 *  3、配置redis
 *  4、测试缓存
 *      原理：CacheManager 缓存组件来实际给缓存中存取数据
 *      1）引入redis的starter，容器中保存的是RedisCacheManager
 *      2）RedisCacheManager，帮我们创建RedisCache来作为缓存组件，
 *      3）默认保存数据 k-V 都是Object，利用序列化保存，
 */
@MapperScan("com.example.cache.mapper")
@SpringBootApplication
@EnableCaching
public class Springboot01CacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot01CacheApplication.class, args);
    }
}
