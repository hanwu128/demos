package com.example.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot 默认支持两种技术来和ES交互
 * 1、Jest（默认不生效），生效需要导入jest的工具包（io.searchbox.client.JestClient）
 * 2、SpringData Elas    ticSearch
 *      1)Client节点信息  clusterNodes clusterName
 *      2）ElasticsearchTemplate操作ES
 *      3）编写一个ElasticsearchRepository的子接口来操作ES
 *
*  两种用法
 *  1、
 */
@SpringBootApplication
public class Springboot03ElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot03ElasticsearchApplication.class, args);
    }
}
