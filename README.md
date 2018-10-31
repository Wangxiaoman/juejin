# 表结构
```
-- 段子
CREATE TABLE `episode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `brand_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '来源',
  `origin_url` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '源url',
  `detail` varchar(8092) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '详细内容',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `utime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `type` tinyint(4) DEFAULT '1' COMMENT '1-文字，2-视频，3-动图',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `user_name` varchar(64) NOT NULL COMMENT '用户名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- 用户
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sex` tinyint(4) DEFAULT '0' COMMENT '0-未知，1-男，2-女',
  `age` tinyint(4) DEFAULT '0' COMMENT '0-未知',
  `password` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '简单加盐做下md5',
  `token` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `token_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `utime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `invitation_code` varchar(64) NOT NULL COMMENT '该用户生成的邀请码',
  `registe_invitation_code` varchar(64) NOT NULL DEFAULT '' COMMENT '用户注册使用的邀请码',
  `avatar` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户头像',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- 用户账户
CREATE TABLE `user_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `integral` int(11) DEFAULT '0' COMMENT '积分',
  `eth_token` varchar(256) DEFAULT '' COMMENT 'eth钱包地址',
  `btc_token` varchar(256) DEFAULT '' COMMENT 'btc钱包地址',
  `eos_token` varchar(256) DEFAULT '' COMMENT 'eos钱包地址',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `utime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- 用户的所有行为，可以校验用户的积分，以及限制用户的刷量行为
CREATE TABLE `user_action` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `action` tinyint(4) NOT NULL COMMENT '1-注册,2-发段子,3-邀请注册,4-喜欢,5-分享,6-抽奖,7-兑换',
  `to_user_id` int(11) DEFAULT '0' COMMENT '被作用用户id',
  `integral` int(11) DEFAULT '0' COMMENT '积分，有正有负',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `utime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

```

# 接口文档

## 拉取列表接口
接口名称： /hlg/episodes?current_id=0&page_size=2 (Get)

|  参数名称 |          描述         |
|-------|:---------------------:|
|currentId | 当前所看到的id值   |
| pageSize   | 每次拉取的条数  |
