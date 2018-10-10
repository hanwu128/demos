SET NAMES 'utf8';

drop table if exists `digital_twin`;
create table `digital_twin` (
  `id` bigint(20) not null auto_increment comment 'id',
  `name` varchar(128) default '' comment 'dt名称',
  `desp` VARCHAR(128) DEFAULT '' COMMENT '',
  `ctime` timestamp not null default current_timestamp comment '创建时间',
  `mtime` timestamp comment '更新时间',
  primary key (`id`)
)engine=innodb default charset=utf8;

drop table if exists `digital_twin_attr`;
create table `digital_twin_attr` (
  `id` bigint(20) not null auto_increment comment 'id',
  `dt_id` bigint(20) not null comment 'digital_twin表的主键id',
  `name` varchar(128) default '' comment '属性名称',
  `desp` VARCHAR(128) DEFAULT '' COMMENT '',
  `unit` varchar(32) default '' comment '单位',
  `fixed_value` double default 0 comment '属性值',
  `expec_value` double default 0 comment '期望值',
  `metric` varchar(256) default '' comment 'Metric',
  `tagkv` varchar(1024) default '' comment 'TagKV列表',
  `ctime` timestamp not null default current_timestamp comment '创建时间',
  `mtime` timestamp comment '更新时间',
  primary key (`id`)
)engine=innodb default charset=utf8;