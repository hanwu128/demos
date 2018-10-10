SET NAMES 'utf8';

-- 创建管理员账户表
drop table if exists `account`;
create table `account` (
	`id` bigint(20) not null auto_increment comment 'id',
	`name` varchar(50) default '' comment '姓名',
	`login_name` varchar(50) default '' comment '登录账户名',
	`password` varchar(32) default null comment '密码,md5加密',
	`email` varchar(50) default '' comment '邮箱',
	`phone` varchar(30) default '' comment '联系方式', 
	`enable` tinyint(1) default 1 comment '是否启用,0:禁用,1:启用',
	`company_id` bigint(20) not null comment '公司id',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
  	`update_time` timestamp comment '更新时间',
  	primary key (`id`)
)engine=innodb default charset=utf8;
create unique index loginname_unique_index on account(login_name);
-- create unique index email_unique_index on account(email);


-- 创建角色表
-- 超级管理员,普通管理员
drop table if exists `role`;
create table `role` (
	`id` bigint(20) not null auto_increment comment 'id',
	`name` varchar(50) not null comment '可读名称',
	`role_name` varchar(30) not null comment '角色名称',
	`priority` int not null comment '排序优先级',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
  	`update_time` timestamp comment '更新时间',
  	primary key (`id`)
)engine=innodb default charset=utf8;
create unique index rolename_unique_index on role(role_name);


-- 创建权限表
drop table if exists `permission`;
create table `permission` (
	`id` bigint(20) not null auto_increment comment 'id',
	`name` varchar(50) not null comment '权限名称',
	`code` varchar(30) not null comment '权限编码',
	`parent` varchar(30) not null comment '父节点权限编码,顶级节点值与code值相同',
	`val` varchar(50) not null comment '权限值',
	`editable` tinyint not null default 0 comment '类别,0:不可编辑的权限,1:可编辑的权限',
	`initial` tinyint not null default 0 comment '0:二级账户默认值',
	`priority` int not null comment '排序优先级',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
  	`update_time` timestamp comment '更新时间',
  	primary key (`id`)
)engine=innodb default charset=utf8;
create unique index code_unique_index on permission(code);
create unique index permission_unique_index on permission(val);

-- 创建账户权限表
drop table if exists `account_permission`;
create table `account_permission` (
	`id` bigint(20) not null auto_increment comment 'id',
	`login_name` varchar(50) not null comment '账户登录名',
	`perm_code` varchar(30) not null comment '权限编码',
	`editable` tinyint not null default 0 comment '类别,0:不可编辑的默认权限,1:可编辑的权限', 
	`create_time` timestamp not null default current_timestamp comment '创建时间',
  	`update_time` timestamp comment '更新时间',
  	primary key (`id`)
)engine=innodb default charset=utf8;
create unique index loginname_perm_unique_index on account_permission(login_name, perm_code);


-- 创建角色权限表
-- drop table if exists `role_permission`;
-- create table `role_permission` (
-- 	`id` bigint(20) not null auto_increment comment 'id',
-- 	`role_name` varchar(50) not null comment '角色名',
-- 	`perm_code` varchar(30) not null comment '权限编码',
-- 	`create_time` timestamp not null default current_timestamp comment '创建时间',
--  	`update_time` timestamp comment '更新时间',
--  	primary key (`id`)
-- )engine=innodb default charset=utf8;


-- 创建管理员账户角色表
drop table if exists `account_role`;
create table `account_role` (
	`id` bigint(20) not null auto_increment comment 'id',
	`login_name` varchar(50) not null comment '管理员登录账户名',
	`role_name` varchar(50) not null comment '角色名称',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	`update_time` timestamp comment '更新时间',
  	primary key (`id`)
)engine=innodb default charset=utf8;
create unique index account_role_unique_index on account_role(login_name,role_name);


