# 安装部署说明



## 运行环境

请在目标主机上安装Java 1.8+的运行时环境。


## 安装程序

程序为免安装的压缩包格式，只需要将digitaltwin-api-xxx.tar.gz解压到指定目录即可。

例如：将压缩包digitaltwin-api-1.0.0.tar.gz解压到/usr/local目录下，执行如下命令：
```shell
tar xvf digitaltwin-api-1.0.0.tar.gz -C /usr/local/
```
那么，`/usr/local/digitaltwin-api-1.0.0`即为安装目录。


## 配置参数

### 修改服务端口

默认服务端口为：8080。

进入到程序安装目录下，打开文件`conf/application-xxx.properties`，修改服务端口：

- server.port=服务端口


### 初始化数据库

对于初始化安装的环境，请先使用安装目录下`sql/digitaltwin-api.sql`文件中的sql语句新建数据库并初始化数据库表。


### 修改MySQL参数

进入到程序安装目录下，打开文件`conf/application-xxx.properties`，分别修改如下对应参数值：

- spring.datasource.url=数据库IP地址和端口
- spring.datasource.username=访问数据库用户名
- spring.datasource.password=访问数据库密码


### 修改TSDB参数

进入到程序安装目录下，打开文件`conf/application-xxx.properties`，分别修改如下对应参数值：

- tsdb.url=TSDB接口地址
- tsdb.port=TSDB服务端口


## 修改MQTT参数

进入到程序安装目录下，打开文件`conf/application-xxx.properties`，分别修改如下对应参数值：

- mqtt.broker=MQTT服务器地址
- mqtt.client_id=客户端ID
- mqtt.username=认证用户名
- mqtt.password=认证密码
- mqtt.keep_alive=保持连接时常
- mqtt.clean_session=是否清空会话


## 修改用户验证参数

token.url=用户TOKEN验证服务地址


### 修改日志文件路径

默认情况下，程序运行日志保存在安装路径下的logs目录中，并且按天进行切分保存，历史日志文件最多保存30天。

如果希望将程序日志输出到其他地方，请修改配置文件`conf/logback.xml`对应参数：

- FileNamePattern：修改程序输出日志的文件路径和名称


## 启动程序

直接执行安装路径下的启动脚本即可启动程序，例如：
```shell
sh /usr/local/digitaltwin-api-1.0.0/bin/startup.sh
```


## 停止程序

直接执行安装目录下的停止脚本即可停止程序，例如：
```shell
sh /usr/local/digitaltwin-api-1.0.0/bin/shutdown.sh
```



