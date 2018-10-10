# digitaltwin-api


## 技术架构

Spring Boot + MyBatis + JWT
MySQL版本：5.6.40


## 部署架构

DT基于HTTP协议，本身无状态，可以水平扩展。



## 功能架构

### 前端接口

前端接口是为DT前端设计的，用于Web界面与DT后端的交互，实现DT模板和DT实例的管理。
前端接口放在包：com.lenovo.iot.digitaltwin.controller.front下。


### Stream接口

该接口提供给StreamStudio访问DT物实例数据。
这部分接口放在包：com.lenovo.iot.digitaltwin.controller.stream下。


### 开放接口

开放接口为上层应用提供访问DT的能力，通过DT提供的外部接口查询DT实例数据。
开放接口放在包：com.lenovo.iot.digitaltwin.controller.open下。



## 研发调试

在集成开发环境中调试时添加参数：

1.VM参数
-Dlog.dir=.

2.命令行参数
--spring.profiles.active=dev


在开发环境执行编译：mvn clean package -Dmaven.test.skip=true -Pdev



## 安装部署

详见INSTALL.md



## 端口占用

本程序启动需要占用2个端口：一个服务端口，一个管理端口。
服务端口默认为：8400，管理管理默认为8000。



## 数据库自动升级

程序在启动时会自动处理数据库升级，无需手动操作。


