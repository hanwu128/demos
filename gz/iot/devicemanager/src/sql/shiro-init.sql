SET NAMES 'utf8';

-- 初始化角色表
insert into role(name,role_name,priority,create_time,update_time) values('超级管理员','superadmin',100,now(),now());
insert into role(name,role_name,priority,create_time,update_time) values('管理员','admin',200,now(),now());


-- 初始化权限表
insert into permission(name,code,parent,val,editable,initial,priority,create_time,update_time) values('边缘分析','edge_analyse','edge_analyse','edge:analyse',0,0,100,now(),now());
insert into permission(name,code,parent,val,editable,initial,priority,create_time,update_time) values('Edge管理','edge_manage','edge_analyse','edge:manage',1,0,150,now(),now());
insert into permission(name,code,parent,val,editable,initial,priority,create_time,update_time) values('数据流管理','datastream_manage','edge_analyse','datastream:manage',1,0,200,now(),now());
insert into permission(name,code,parent,val,editable,initial,priority,create_time,update_time) values('设备镜像管理','devmirror_manage','edge_analyse','devmirror:manage',1,0,250,now(),now());
insert into permission(name,code,parent,val,editable,initial,priority,create_time,update_time) values('处理模块','procmodule_manage','edge_analyse','procmodule:manage',1,0,300,now(),now());
insert into permission(name,code,parent,val,editable,initial,priority,create_time,update_time) values('信息管理','information_manage','information_manage','information:manage',0,0,350,now(),now());
insert into permission(name,code,parent,val,editable,initial,priority,create_time,update_time) values('企业信息','company_information','information_manage','company:information',0,0,400,now(),now());
insert into permission(name,code,parent,val,editable,initial,priority,create_time,update_time) values('企业信息设置','company_setting','information_manage','company:setting',0,1,450,now(),now());
insert into permission(name,code,parent,val,editable,initial,priority,create_time,update_time) values('账号管理','account_manage','information_manage','account:manage',0,1,500,now(),now());


-- 初始化角色权限表
-- 超级管理员
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('superadmin','edge_analyse',now(),now());        	-- 边缘分析
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('superadmin','edge_manage',now(),now());        		-- Edge管理 
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('superadmin','datastream_manage',now(),now());  		-- 数据流管理
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('superadmin','devmirror_manage',now(),now());   		-- 设备镜像管理
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('superadmin','procmodule_manage',now(),now());  		-- 处理模块
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('superadmin','information_manage',now(),now());  	-- 信息管理
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('superadmin','company_manage',now(),now());     		-- 企业信息
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('superadmin','company_manage_setting',now(),now());  -- 企业信息设置
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('superadmin','account_manage',now(),now());     		-- 账户管理
-- 普通管理员
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('admin','edge_analyse',now(),now());					-- 边缘分析
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('admin','edge_manage',now(),now());        			-- Edge管理 
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('admin','datastream_manage',now(),now());  			-- 数据流管理
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('admin','devmirror_manage',now(),now());   			-- 设备镜像管理
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('admin','procmodule_manage',now(),now());  			-- 处理模块
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('admin','information_manage',now(),now());  			-- 信息管理
-- insert into role_permission(role_name,perm_code,create_time,update_time) values('admin','company_manage',now(),now());     			-- 企业信息


-- 初始化用户信息
insert into account(name,login_name,password,email,company_id,create_time,update_time) values('陈昌辉','chench9',md5('111111'),'chench9@lenovo.com',100000,now(),now());
insert into account_role(login_name,role_name,create_time,update_time) values('chench9','superadmin',now(),now());
insert into account(name,login_name,password,email,company_id,create_time,update_time) values('测试账户','test',md5('111111'),'test@lenovo.com',100000,now(),now());
insert into account_role(login_name,role_name,create_time,update_time) values('test','admin',now(),now());

-- 初始化账户权限
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('chench9','edge_analyse',0,now(),now());          -- 边缘分析   
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('chench9','edge_manage',1,now(),now());           -- Edge管理 
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('chench9','datastream_manage',1,now(),now());     -- 数据流管理
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('chench9','devmirror_manage',1,now(),now());      -- 设备镜像管理
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('chench9','procmodule_manage',1,now(),now());     -- 处理模块
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('chench9','information_manage',0,now(),now());    -- 信息管理
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('chench9','company_information',0,now(),now());   -- 企业信息
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('chench9','company_setting',0,now(),now());       -- 企业信息设置
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('chench9','account_manage',0,now(),now());        -- 账户管理

insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('test','edge_analyse',0,now(),now());             -- 边缘分析   
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('test','edge_manage',1,now(),now());              -- Edge管理 
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('test','datastream_manage',1,now(),now());        -- 数据流管理
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('test','devmirror_manage',1,now(),now());         -- 设备镜像管理
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('test','procmodule_manage',1,now(),now());        -- 处理模块
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('test','information_manage',0,now(),now());       -- 信息管理
insert into account_permission(login_name,perm_code,editable,create_time,update_time) values('test','company_information',0,now(),now());      -- 企业信息

