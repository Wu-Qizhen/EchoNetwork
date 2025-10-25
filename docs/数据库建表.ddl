-- 用户表
CREATE TABLE `user` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                        `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
                        `email` VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
                        `phone` VARCHAR(20) COMMENT '手机号',
                        `password` VARCHAR(255) NOT NULL COMMENT '密码',
                        `nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
                        `bio` VARCHAR(500) COMMENT '个人简介或签名',
                        `avatar_url` VARCHAR(500) COMMENT '头像 URL',
                        `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `last_login_time` DATETIME COMMENT '最后登录时间',
                        `status` TINYINT NOT NULL DEFAULT 0 COMMENT '账号状态 0 正常 1 禁用 2 删除',
                        `role` TINYINT NOT NULL DEFAULT 0 COMMENT '账号角色 0 普通用户 1 超级用户 2 管理员',
                        `follower_count` INT NOT NULL DEFAULT 0 COMMENT '粉丝数',
                        INDEX `idx_username` (`username`),
                        INDEX `idx_email` (`email`),
                        INDEX `idx_create_time` (`create_time`),
                        INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 圈子表
CREATE TABLE `circle` (
                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                          `name` VARCHAR(100) NOT NULL UNIQUE COMMENT '圈子名称',
                          `description` TEXT COMMENT '圈子描述',
                          `creator_id` BIGINT NOT NULL COMMENT '创建者ID',
                          `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          INDEX `idx_creator_id` (`creator_id`),
                          INDEX `idx_create_time` (`create_time`),
                          FOREIGN KEY (`creator_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='圈子表';

-- 文章表
CREATE TABLE `article` (
                           `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                           `title` VARCHAR(200) NOT NULL COMMENT '文章标题',
                           `content` LONGTEXT NOT NULL COMMENT '文章内容',
                           `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` DATETIME COMMENT '更新时间',
                           `publish_time` DATETIME COMMENT '发布时间',
                           `status` TINYINT NOT NULL DEFAULT 0 COMMENT '文章状态 0草稿 1发布 2删除',
                           `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览次数',
                           `like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
                           `star_count` INT NOT NULL DEFAULT 0 COMMENT '收藏数',
                           `comment_count` INT NOT NULL DEFAULT 0 COMMENT '评论数',
                           `author_id` BIGINT NOT NULL COMMENT '作者ID',
                           `circle_id` BIGINT COMMENT '圈子ID',
                           `tag_ids` JSON COMMENT '标签ID列表', -- 存储为JSON数组
                           INDEX `idx_author_id` (`author_id`),
                           INDEX `idx_circle_id` (`circle_id`),
                           INDEX `idx_status` (`status`),
                           INDEX `idx_publish_time` (`publish_time`),
                           INDEX `idx_create_time` (`create_time`),
                           FULLTEXT INDEX `idx_title_content` (`title`, `content`),
                           FOREIGN KEY (`author_id`) REFERENCES `user`(`id`),
                           FOREIGN KEY (`circle_id`) REFERENCES `circle`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 标签表
CREATE TABLE `tag` (
                       `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                       `name` VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名称',
                       INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章标签表';

-- 文章标签关联表
CREATE TABLE `article_tag` (
                               `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                               `article_id` BIGINT NOT NULL COMMENT '文章ID',
                               `tag_id` BIGINT NOT NULL COMMENT '标签ID',
                               `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关联时间',
                               UNIQUE KEY `uk_article_tag` (`article_id`, `tag_id`),
                               INDEX `idx_article_id` (`article_id`),
                               INDEX `idx_tag_id` (`tag_id`),
                               FOREIGN KEY (`article_id`) REFERENCES `article`(`id`),
                               FOREIGN KEY (`tag_id`) REFERENCES `tag`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章标签关联表';

-- 评论表
CREATE TABLE `comment` (
                           `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                           `content` TEXT NOT NULL COMMENT '评论内容',
                           `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
                           `article_id` BIGINT NOT NULL COMMENT '所属文章ID',
                           `user_id` BIGINT NOT NULL COMMENT '评论者ID',
                           INDEX `idx_article_id` (`article_id`),
                           INDEX `idx_user_id` (`user_id`),
                           INDEX `idx_create_time` (`create_time`),
                           FOREIGN KEY (`article_id`) REFERENCES `article`(`id`),
                           FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 文章点赞表
CREATE TABLE `article_like` (
                                `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
                                `user_id` BIGINT NOT NULL COMMENT '点赞用户ID',
                                `article_id` BIGINT NOT NULL COMMENT '点赞文章ID',
                                UNIQUE KEY `uk_user_article` (`user_id`, `article_id`),
                                INDEX `idx_user_id` (`user_id`),
                                INDEX `idx_article_id` (`article_id`),
                                INDEX `idx_create_time` (`create_time`),
                                FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
                                FOREIGN KEY (`article_id`) REFERENCES `article`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章点赞表';

-- 评论点赞表
CREATE TABLE `comment_like` (
                                `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
                                `user_id` BIGINT NOT NULL COMMENT '点赞用户ID',
                                `comment_id` BIGINT NOT NULL COMMENT '点赞评论ID',
                                UNIQUE KEY `uk_user_comment` (`user_id`, `comment_id`),
                                INDEX `idx_user_id` (`user_id`),
                                INDEX `idx_comment_id` (`comment_id`),
                                INDEX `idx_create_time` (`create_time`),
                                FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
                                FOREIGN KEY (`comment_id`) REFERENCES `comment`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论点赞表';

-- 关注表
CREATE TABLE `follow` (
                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                          `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `follower_user_id` BIGINT NOT NULL COMMENT '关注者ID',
                          `following_user_id` BIGINT NOT NULL COMMENT '被关注者ID',
                          UNIQUE KEY `uk_follower_following` (`follower_user_id`, `following_user_id`),
                          INDEX `idx_follower_id` (`follower_user_id`),
                          INDEX `idx_following_id` (`following_user_id`),
                          INDEX `idx_create_time` (`create_time`),
                          FOREIGN KEY (`follower_user_id`) REFERENCES `user`(`id`),
                          FOREIGN KEY (`following_user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关注表';

-- 收藏表
CREATE TABLE `article_star` (
                                `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
                                `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                `article_id` BIGINT NOT NULL COMMENT '文章ID',
                                UNIQUE KEY `uk_user_article` (`user_id`, `article_id`),
                                INDEX `idx_user_id` (`user_id`),
                                INDEX `idx_article_id` (`article_id`),
                                INDEX `idx_create_time` (`create_time`),
                                FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
                                FOREIGN KEY (`article_id`) REFERENCES `article`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章收藏表';

-- 圈子成员表
CREATE TABLE `circle_member` (
                                 `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 `circle_id` BIGINT NOT NULL COMMENT '圈子 ID',
                                 `user_id` BIGINT NOT NULL COMMENT '用户 ID',
                                 `join_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
                                 `role` TINYINT NOT NULL DEFAULT 0 COMMENT '成员角色 0 普通成员 1 管理员',
                                 UNIQUE KEY `uk_circle_user` (`circle_id`, `user_id`),
                                 INDEX `idx_circle_id` (`circle_id`),
                                 INDEX `idx_user_id` (`user_id`),
                                 KEY `idx_join_time` (join_time),
                                 FOREIGN KEY (`circle_id`) REFERENCES `circle`(`id`),
                                 FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='圈子成员表';

-- 添加 member_count 字段
ALTER TABLE `circle`
    ADD COLUMN `member_count` INT DEFAULT 0 COMMENT '成员数量';

-- 添加 article_count 字段
ALTER TABLE `circle`
    ADD COLUMN `article_count` INT DEFAULT 0 COMMENT '文章数量';
