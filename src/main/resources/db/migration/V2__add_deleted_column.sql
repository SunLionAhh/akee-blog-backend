-- 为所有表添加deleted字段
ALTER TABLE user ADD COLUMN deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除';
ALTER TABLE post ADD COLUMN deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除';
ALTER TABLE category ADD COLUMN deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除';
ALTER TABLE tag ADD COLUMN deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除';
ALTER TABLE comment ADD COLUMN deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除';
ALTER TABLE post_tag ADD COLUMN deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'; 