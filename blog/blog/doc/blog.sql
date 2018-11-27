/*
Navicat MySQL Data Transfer

Source Server         : tenxunyun
Source Server Version : 50724
Source Host           : 192.144.161.18:3306
Source Database       : blog

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2018-11-27 09:48:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `hw_
permiss`
-- ----------------------------
DROP TABLE IF EXISTS `hw_
permiss`;
CREATE TABLE `hw_
permiss` (
  `per_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `per_name` varchar(60) NOT NULL COMMENT '权限名称',
  `per_menu` varchar(60) NOT NULL COMMENT '权限菜单',
  PRIMARY KEY (`per_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_
permiss
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_articles`
-- ----------------------------
DROP TABLE IF EXISTS `hw_articles`;
CREATE TABLE `hw_articles` (
  `article_id` bigint(255) NOT NULL AUTO_INCREMENT COMMENT '博文ID',
  `user_id` bigint(20) NOT NULL COMMENT '发表用户ID',
  `article_title` text NOT NULL COMMENT '博文标题',
  `article_content` longtext NOT NULL COMMENT '博文内容',
  `article_views` bigint(20) NOT NULL COMMENT '浏览量',
  `article_comment_count` bigint(20) NOT NULL COMMENT '评论总数',
  `article_date` datetime NOT NULL COMMENT '发表日期',
  `article_like_count` bigint(20) NOT NULL COMMENT '点赞数',
  PRIMARY KEY (`article_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_articles
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_comments`
-- ----------------------------
DROP TABLE IF EXISTS `hw_comments`;
CREATE TABLE `hw_comments` (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` bigint(20) NOT NULL COMMENT '发表用户ID',
  `article_id` bigint(20) NOT NULL COMMENT '评论文字ID',
  `comment_like_count` bigint(20) NOT NULL COMMENT '点赞数',
  `comment_date` datetime NOT NULL COMMENT '评论日期',
  `comment_content` text NOT NULL COMMENT '评论内容',
  `parent_comment_id` bigint(20) NOT NULL COMMENT '父评论',
  PRIMARY KEY (`comment_id`),
  KEY `article_id` (`article_id`),
  KEY `comment_date` (`comment_date`),
  KEY `parent_comment_id` (`parent_comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_comments
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_forums`
-- ----------------------------
DROP TABLE IF EXISTS `hw_forums`;
CREATE TABLE `hw_forums` (
  `forum_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '论坛ID',
  `forum_name` varchar(20) NOT NULL COMMENT '论坛名称',
  `forum_description` text NOT NULL COMMENT '论坛描述',
  `forum_logo` varchar(255) NOT NULL COMMENT '论坛Logo',
  `forum_post_count` bigint(20) NOT NULL COMMENT '论坛帖子数',
  `parent_forum_id` bigint(20) NOT NULL COMMENT '父论坛ID',
  PRIMARY KEY (`forum_id`),
  KEY `forum_name` (`forum_name`),
  KEY `parent_forum_id` (`parent_forum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_forums
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_friend_links`
-- ----------------------------
DROP TABLE IF EXISTS `hw_friend_links`;
CREATE TABLE `hw_friend_links` (
  `friend_link_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '友链ID',
  `friend_links` varchar(255) NOT NULL COMMENT '友链链接',
  `friend_link_name` varchar(20) NOT NULL COMMENT '友链名称',
  `friend_link_description` text NOT NULL COMMENT '友链描述',
  `friend_link_logo` varchar(255) NOT NULL COMMENT '友链Logo',
  PRIMARY KEY (`friend_link_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_friend_links
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_labels`
-- ----------------------------
DROP TABLE IF EXISTS `hw_labels`;
CREATE TABLE `hw_labels` (
  `label_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `label_name` varchar(20) NOT NULL COMMENT '标签名称',
  `label_alias` varchar(15) NOT NULL COMMENT '标签别名',
  `label_description` text NOT NULL COMMENT '标签描述',
  PRIMARY KEY (`label_id`),
  KEY `label_name` (`label_name`),
  KEY `label_alias` (`label_alias`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_labels
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_menus`
-- ----------------------------
DROP TABLE IF EXISTS `hw_menus`;
CREATE TABLE `hw_menus` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '总菜单',
  `menu_name` varchar(20) NOT NULL COMMENT '总菜单名称',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_menus
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_moderator`
-- ----------------------------
DROP TABLE IF EXISTS `hw_moderator`;
CREATE TABLE `hw_moderator` (
  `moderator_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '版主ID',
  `forum_id` bigint(20) NOT NULL COMMENT '论坛ID',
  `moderator_level` varchar(20) NOT NULL COMMENT '版主级别',
  PRIMARY KEY (`moderator_id`,`forum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_moderator
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_options`
-- ----------------------------
DROP TABLE IF EXISTS `hw_options`;
CREATE TABLE `hw_options` (
  `option_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '选项ID',
  `option_name` varchar(255) NOT NULL COMMENT '选项名称',
  `option_values` longtext NOT NULL COMMENT '选项值',
  PRIMARY KEY (`option_id`),
  KEY `option_name` (`option_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_options
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_posts`
-- ----------------------------
DROP TABLE IF EXISTS `hw_posts`;
CREATE TABLE `hw_posts` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主题ID',
  `forum_id` bigint(20) NOT NULL COMMENT '论坛ID',
  `user_id` bigint(20) NOT NULL COMMENT '回帖用户ID',
  `post_title` text NOT NULL COMMENT '帖子标题',
  `post_views` bigint(20) NOT NULL COMMENT '帖子浏览量',
  `post_content` longtext NOT NULL COMMENT '帖子内容',
  `post_date` datetime NOT NULL COMMENT '发帖时间',
  `post_status` varchar(20) NOT NULL COMMENT '帖子状态',
  `post_comment_count` bigint(20) NOT NULL COMMENT '回帖个数',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_posts
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_reply`
-- ----------------------------
DROP TABLE IF EXISTS `hw_reply`;
CREATE TABLE `hw_reply` (
  `reply_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '回帖ID',
  `user_id` bigint(20) NOT NULL COMMENT '回帖用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '所属主贴ID',
  `reply_content` longtext NOT NULL COMMENT '回帖内容',
  `reply_date` datetime NOT NULL COMMENT '回帖时间',
  `parent_reply_id` bigint(20) NOT NULL COMMENT '父回帖ID',
  PRIMARY KEY (`reply_id`),
  KEY `parent_floor_id` (`parent_reply_id`),
  KEY `user_id` (`user_id`,`post_id`),
  KEY `floor_date` (`reply_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_reply
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_set_artitle_label`
-- ----------------------------
DROP TABLE IF EXISTS `hw_set_artitle_label`;
CREATE TABLE `hw_set_artitle_label` (
  `article_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `label_id` bigint(20) NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`article_id`),
  KEY `label_id` (`label_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_set_artitle_label
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_set_artitle_sort`
-- ----------------------------
DROP TABLE IF EXISTS `hw_set_artitle_sort`;
CREATE TABLE `hw_set_artitle_sort` (
  `article_id` bigint(20) NOT NULL COMMENT '文章ID',
  `sort_id` bigint(20) NOT NULL COMMENT '分类ID',
  PRIMARY KEY (`article_id`,`sort_id`),
  KEY `sort_id` (`sort_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_set_artitle_sort
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_sorts`
-- ----------------------------
DROP TABLE IF EXISTS `hw_sorts`;
CREATE TABLE `hw_sorts` (
  `sort_id` bigint(20) NOT NULL COMMENT '分类ID',
  `sort_name` varchar(50) NOT NULL COMMENT '分类名称',
  `sort_alias` varchar(15) NOT NULL COMMENT '分类别名',
  `sort_description` text NOT NULL COMMENT '分类描述',
  `parent_sort_id` bigint(20) NOT NULL COMMENT '父分类ID',
  PRIMARY KEY (`sort_id`),
  KEY `sort_name` (`sort_name`),
  KEY `sort_alias` (`sort_alias`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_sorts
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_submenus`
-- ----------------------------
DROP TABLE IF EXISTS `hw_submenus`;
CREATE TABLE `hw_submenus` (
  `link_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '子菜单ID',
  `menu_id` bigint(20) NOT NULL COMMENT '总菜单ID',
  `link_name` varchar(255) NOT NULL COMMENT '子菜单名称',
  `link_target` varchar(255) NOT NULL COMMENT '子菜单链接',
  `link_open_way` varchar(20) NOT NULL COMMENT '子菜单打开方式',
  `parent_link_id` bigint(20) NOT NULL COMMENT '父菜单',
  PRIMARY KEY (`link_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_submenus
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_user_friends`
-- ----------------------------
DROP TABLE IF EXISTS `hw_user_friends`;
CREATE TABLE `hw_user_friends` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '标识ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `user_friends_id` bigint(20) NOT NULL COMMENT '好友ID',
  `user_note` varchar(20) NOT NULL COMMENT '好友备注',
  `user_status` varchar(20) NOT NULL COMMENT '好友状态',
  PRIMARY KEY (`Id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_user_friends
-- ----------------------------

-- ----------------------------
-- Table structure for `hw_users`
-- ----------------------------
DROP TABLE IF EXISTS `hw_users`;
CREATE TABLE `hw_users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_ip` varchar(20) DEFAULT NULL COMMENT '用户IP',
  `user_name` varchar(20) NOT NULL COMMENT '用户名',
  `user_password` varchar(15) NOT NULL COMMENT '用户密码',
  `user_email` varchar(30) NOT NULL COMMENT '用户邮箱',
  `user_photo` varchar(255) NOT NULL COMMENT '用户头像',
  `user_level` varchar(20) NOT NULL COMMENT '用户等级',
  `user_permission` varchar(20) NOT NULL COMMENT '用户权限',
  `user_regist_time` datetime NOT NULL COMMENT '注册时间',
  `user_birth` date DEFAULT NULL COMMENT '用户生日',
  `user_age` tinyint(4) DEFAULT NULL COMMENT '用户年龄',
  `user_phone` varchar(11) NOT NULL COMMENT '用户手机号',
  `user_nickname` varchar(20) NOT NULL COMMENT '用户昵称',
  `user_activate` int(11) NOT NULL DEFAULT '0' COMMENT '激活状态',
  `user_code` varchar(64) NOT NULL COMMENT '激活码',
  `user_del` int(11) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`user_id`),
  KEY `user_name` (`user_name`),
  KEY `user_nickname` (`user_nickname`),
  KEY `user_email` (`user_email`),
  KEY `user_telephone_number` (`user_phone`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hw_users
-- ----------------------------
INSERT INTO `hw_users` VALUES ('4', '123', 'aaa时代大厦', 'a', 'asa@qq.com', 'aa', 'aa', 'aa', '2018-11-09 19:43:38', '2018-11-09', '12', '13521234987', 'aa', '0', '', '0');
INSERT INTO `hw_users` VALUES ('5', '12', 'bbb啊啊', 'bb', 'bb@qq.com', 'bbb', 'bbbb', 'bbbb', '2018-11-09 19:44:12', '2018-11-09', '32', '15012341234', 'bbbb', '0', '', '0');
INSERT INTO `hw_users` VALUES ('6', '0:0:0:0:0:0:0:1', 'sadfasfa', '123', '123', 'aa', '菜鸟', '普通用户', '2018-11-14 16:07:47', '2018-11-30', null, '123', 'sadfasfa', '0', '', '0');
INSERT INTO `hw_users` VALUES ('7', '0:0:0:0:0:0:0:1', '的撒的发生', '123', '123', 'aa', '菜鸟', '普通用户', '2018-11-15 09:45:15', '2018-11-30', '0', '123', '的撒的发生', '0', '0c61819db1c74ff4abb075a83e29bbe0', '0');
INSERT INTO `hw_users` VALUES ('8', '0:0:0:0:0:0:0:1', '的说法是的范德萨水电费', '123', '123', 'aa', '菜鸟', '普通用户', '2018-11-15 20:30:43', '2012-11-15', '6', '123', '的说法是的范德萨水电费', '0', 'b1a2a948cef047d2879da7b33474c630', '0');
INSERT INTO `hw_users` VALUES ('9', '0:0:0:0:0:0:0:1', '是的发送到发送到', '123', '123', 'aa', '菜鸟', '普通用户', '2018-11-15 20:40:51', '2011-11-15', '7', '312321', '是的发送到发送到', '1', '79f6e7aace5c43ae8222541a08b263e4', '0');

-- ----------------------------
-- View structure for `Articles`
-- ----------------------------
DROP VIEW IF EXISTS `Articles`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `Articles` AS select `zj_articles`.`user_id` AS `user_id`,`zj_users`.`user_name` AS `user_name`,`zj_articles`.`article_id` AS `article_id`,`zj_articles`.`article_title` AS `article_title`,`zj_articles`.`article_content` AS `article_content` from (`zj_articles` join `zj_users`) where (`zj_articles`.`user_id` = `zj_users`.`user_id`) ;

-- ----------------------------
-- View structure for `Forums`
-- ----------------------------
DROP VIEW IF EXISTS `Forums`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `Forums` AS select `zj_forums`.`forum_id` AS `forum_id`,`zj_forums`.`forum_name` AS `forum_name`,`zj_posts`.`post_id` AS `post_id`,`zj_posts`.`post_title` AS `post_title` from (`zj_forums` join `zj_posts`) where (`zj_forums`.`forum_id` = `zj_posts`.`forum_id`) ;

-- ----------------------------
-- View structure for `Friends`
-- ----------------------------
DROP VIEW IF EXISTS `Friends`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `Friends` AS select `zj_user_friends`.`user_id` AS `user_id`,`zj_users`.`user_name` AS `user_name`,`zj_user_friends`.`user_friends_id` AS `user_friends_id`,`zj_user_friends`.`user_note` AS `user_note` from (`zj_users` join `zj_user_friends`) where (`zj_users`.`user_id` = `zj_user_friends`.`user_id`) ;

-- ----------------------------
-- View structure for `Label`
-- ----------------------------
DROP VIEW IF EXISTS `Label`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `Label` AS select `zj_set_artitle_label`.`label_id` AS `label_id`,`zj_labels`.`label_name` AS `label_name`,`zj_set_artitle_label`.`article_id` AS `article_id`,`zj_articles`.`article_title` AS `article_title` from ((`zj_labels` join `zj_articles`) join `zj_set_artitle_label`) where ((`zj_set_artitle_label`.`article_id` = `zj_articles`.`article_id`) and (`zj_set_artitle_label`.`label_id` = `zj_labels`.`label_id`)) ;

-- ----------------------------
-- View structure for `Posts`
-- ----------------------------
DROP VIEW IF EXISTS `Posts`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `Posts` AS select `zj_posts`.`user_id` AS `user_id`,`zj_users`.`user_name` AS `user_name`,`zj_posts`.`post_id` AS `post_id`,`zj_posts`.`post_title` AS `post_title`,`zj_posts`.`post_content` AS `post_content`,`zj_posts`.`forum_id` AS `forum_id`,`zj_forums`.`forum_name` AS `forum_name` from ((`zj_posts` join `zj_users`) join `zj_forums`) where ((`zj_posts`.`user_id` = `zj_users`.`user_id`) and (`zj_posts`.`forum_id` = `zj_forums`.`forum_id`)) ;

-- ----------------------------
-- View structure for `Sort`
-- ----------------------------
DROP VIEW IF EXISTS `Sort`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `Sort` AS select `zj_set_artitle_sort`.`sort_id` AS `sort_id`,`zj_sorts`.`sort_name` AS `sort_name`,`zj_set_artitle_sort`.`article_id` AS `article_id`,`zj_articles`.`article_title` AS `article_title` from ((`zj_articles` join `zj_set_artitle_sort`) join `zj_sorts`) where ((`zj_articles`.`article_id` = `zj_set_artitle_sort`.`article_id`) and (`zj_sorts`.`sort_id` = `zj_set_artitle_sort`.`sort_id`)) ;
