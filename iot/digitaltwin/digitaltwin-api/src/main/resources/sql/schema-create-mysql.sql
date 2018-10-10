-- 创建数据库
-- create database digitaltwin DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
-- grant all privileges on `digitaltwin`.* to `dtapi`@`%` identified by 'qwe!@#123';

-- DT模板表
-- drop table if exists `dt_tpl`;
create table if not exists `dt_tpl` (
  `id`      BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '模板id',
  `name`    VARCHAR(80)   NOT NULL DEFAULT '' COMMENT '模板名称',
  `desp`    VARCHAR(128)  NOT NULL DEFAULT '' COMMENT '模板描述',
  `ctime`   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `mtime`   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间戳',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- DT模板属性表
-- drop table if exists `dt_tpl_attr`;
create table if not exists `dt_tpl_attr` (
  `id`          BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '属性id',
  `tpl`         BIGINT(20)    NOT NULL DEFAULT 0  COMMENT '模板id',
  `name`        VARCHAR(80)   NOT NULL DEFAULT '' COMMENT '属性名称',
  `displayname` VARCHAR(128)  NOT NULL DEFAULT '' COMMENT '属性显示名称',
  `attrtype`    VARCHAR(30)   NOT NULL DEFAULT '' COMMENT '属性类型',
  `datatype`    VARCHAR(30)   NOT NULL DEFAULT '' COMMENT '数据类型',
  `value`       VARCHAR(30)   NOT NULL DEFAULT '' COMMENT '默认值',
  `unit`        VARCHAR(30)   NOT NULL DEFAULT '' COMMENT '单位',
  `ctime`       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `mtime`       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间戳',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`tpl`) REFERENCES dt_tpl(`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- DT实例表
-- drop table if exists `dt_instance`;
create table if not exists `dt_instance` (
  `id`        BIGINT(20)     NOT NULL AUTO_INCREMENT COMMENT '实例id',
  `tpl`       BIGINT(20)     NOT NULL DEFAULT 0  COMMENT '所继承模板id',
  `name`      VARCHAR(80)   NOT NULL DEFAULT '' COMMENT '实例名称',
  `desp`      VARCHAR(128)  NOT NULL DEFAULT '' COMMENT '实例描述',
  `state`     TINYINT        NOT NULL DEFAULT 0  COMMENT '实例状态,0:离线,1:在线',
  `lockup`    TINYINT        NOT NULL DEFAULT 0  COMMENT '是否被锁定,0:未被锁定,1:被锁定,被锁定的物实例不允许删除',
  `uid`       VARCHAR(32)   NOT NULL  DEFAULT '' COMMENT '用户id',
  `gid`       VARCHAR(32)   NOT NULL DEFAULT  '' COMMENT '用户组id',
  `ctime`     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `mtime`     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间戳',
  `atime`     TIMESTAMP              DEFAULT '0000-00-00 00:00:00' COMMENT '最后活跃时间戳',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- DT实例属性表
-- 一个DT实例属性与(deviceid,metric)一一对应
-- drop table if exists `dt_instance_atrr`;
create table if not exists `dt_instance_atrr` (
  `id`          BIGINT(20)    NOT NULL  AUTO_INCREMENT COMMENT '属性id',
  `instance`    BIGINT(20)    NOT NULL  DEFAULT 0  COMMENT 'dt实例id',
  `name`        VARCHAR(80)   NOT NULL  DEFAULT '' COMMENT '属性名',
  `displayname` VARCHAR(128)  NOT NULL  DEFAULT '' COMMENT '属性显示名称',
  `attrtype`    VARCHAR(30)   NOT NULL  DEFAULT '' COMMENT '属性类型',
  `datatype`    VARCHAR(30)   NOT NULL  DEFAULT '' COMMENT '数据类型',
  `value`       VARCHAR(30)   NOT NULL  DEFAULT '' COMMENT '当前值',
  `expectvalue` VARCHAR(30)   NOT NULL  DEFAULT '' COMMENT '期望值',
  `unit`        VARCHAR(30)   NOT NULL  DEFAULT '' COMMENT '单位',
  `metric`      VARCHAR(128)  NOT NULL  DEFAULT '' COMMENT '指标',
  `deviceid`    VARCHAR(128)  NOT NULL  DEFAULT '' COMMENT '设备id',
  `ctime`       TIMESTAMP     NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `mtime`       TIMESTAMP     NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间戳',
  `atime`       TIMESTAMP               DEFAULT '0000-00-00 00:00:00' COMMENT '最后活跃时间戳',
  `stime`       TIMESTAMP               DEFAULT '0000-00-00 00:00:00' COMMENT '发送时间戳',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`instance`) REFERENCES dt_instance(`id`) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;