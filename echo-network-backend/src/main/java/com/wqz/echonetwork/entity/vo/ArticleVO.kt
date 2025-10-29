package com.wqz.echonetwork.entity.vo

import com.wqz.echonetwork.entity.po.Circle
import com.wqz.echonetwork.entity.po.Tag
import java.time.LocalDateTime

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.23
 */
data class ArticleVO(
    var id: Long, // 文章 ID
    var title: String = "", // 文章标题
    var content: String = "", // 文章内容
    var createTime: LocalDateTime, // 创建时间
    var updateTime: LocalDateTime, // 更新时间
    var publishTime: LocalDateTime, // 发布时间
    var status: Int, // 文章状态 0 草稿 1 发布 2 删除
    var viewCount: Int, // 浏览次数
    var likeCount: Int, // 点赞数
    var starCount: Int, // 收藏数
    var commentCount: Int, // 评论数
    var author: UserVO, // 作者
    var circle: Circle? = null, // 圈子
    var tags: MutableSet<Tag> = mutableSetOf(), // 标签
    var isLiked: Boolean = false,
    var isStarred: Boolean = false
)
