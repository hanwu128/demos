
#etcd版subscriber说明文档

----------------------------------------------------------------------------------------------------------
#依赖 
etcd 集群  
配置文件 etcd.conf
<dependency>
			<groupId>etcdgroup</groupId>
			<artifactId>etcdartifactid</artifactId>
			<version>1.0-SNAPSHOT</version>
			<!--<scope>system</scope>-->
			<!-- <systemPath>${project.basedir}/libs/smartbi-SDK-6.0.jar</systemPath> -->
			<!--<systemPath>${project.basedir}/src/main/java/com/lenovo/libappend/etcdartifactid-1.0-SNAPSHOT-jar-with-dependencies.jar</systemPath>-->
</dependency>

*************************************etcd.conf****************************************************************
etcdcontext={
    "_JSONArrayMasterKV": [
        {
            "urladdress": {
                "valueArray": [
                    "192.168.1.07",
                    "8080",
                    "topname/device/iot1",
                    "1000010000019CD67C5C126A04484963@@@@ha1@@@@483D1688DB60477CA6E3C9824B29DA37@@@@$SYS/brokers/emqttd@172.17.199.108/clients/#;$SYS/brokers/emqttd@172.17.199.109/clients/#"
                ],
                "selectedIndex": 3
            },
            "ttlinterval": "10",
            "keyRangEnd": "fooz",
            "standbyinvokeshell": "",
            "alianamekey": "foo",
            "keyRangStart": "foo",
            "ttl": "20",
            "masterinvokeshell": ""
        },
        {
            "urladdress": {
                "valueArray": [
                    "192.168.2.77",
                    "8080",
                    "topname/devicetslt/iot2",
                    "10000100000C9EE14F7F2DED54E86016@@@@ha2@@@@B2CF7D1BF58B4A069C9D03D1A517C8CF@@@@$MOC/device/#"
                ],
                "selectedIndex": 3
            },
            "ttlinterval": "10",
            "keyRangEnd": "fooz",
            "standbyinvokeshell": "",
            "alianamekey": "foo2",
            "keyRangStart": "foo",
            "ttl": "20",
            "masterinvokeshell": ""
        }
    ],
    "_JSONArrayMasterETCDClusterNode": [
        {
            "etcipporturl": "http://172.17.202.50:2379"
        }
    ]
}

**************************************etcd.conf***************************************************************
--------------------------------------------------------------------------------------------------------------


#command startup
nohup java -jar mqttsubscriber-0.0.1-SNAPSHOT-jar-with-dependencies.jar /opt/allinonemqttsubscripter/etcd.conf &
----------------------------------------------------------------------------------------------------------------
#command watching log
tail -f nohup.out | perl -pe 's/(ThreadLetter)/\e[1;31m$1\e[0m/g'

key is show at next line:
tail -f nohup.out | perl -pe 's/(key is show at next line:)/\e[1;31m$1\e[0m/g'
----------------------------------------------------------------------------------------------------------------
#此次参数

$SYS/brokers/emqttd@172.17.199.108/clients/#;$SYS/brokers/emqttd@172.17.199.109/clients/#
$MOC/device/#
-----------------------------------------------------------------------------------------------------------------
#部署节点
172.17.200.190
172.17.200.61
172.17.200.62


