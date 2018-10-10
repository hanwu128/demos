# README


## 是什么

这是一个基于NanoHTTPD实现的轻量级WEB服务器，用于部署DT前端静态页面。

关于NanoHTTPD，详见：https://github.com/NanoHttpd/nanohttpd


## 编译打包

1. 开发环境
```shell
mvn clean package -Pdev -Dmaven.test.skip=true
```

2. 测试环境
```shell
mvn clean package -Ptest -Dmaven.test.skip=true
```

3. 生产环境
```shell
mvn clean package -Pprod -Dmaven.test.skip=true
```
