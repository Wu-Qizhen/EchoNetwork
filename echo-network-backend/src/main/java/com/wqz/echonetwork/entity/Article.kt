package com.wqz.echonetwork.entity

import com.wqz.echonetwork.entity.enums.ArticleStatus
import java.time.LocalDateTime

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
data class Article(
    var id: Long? = null, // 文章 ID
    var title: String = "", // 文章标题
    var content: String = "", // 文章内容
    var createTime: LocalDateTime? = null, // 创建时间
    var updateTime: LocalDateTime? = null, // 更新时间
    var publishTime: LocalDateTime? = null, // 发布时间
    var status: Int = ArticleStatus.DRAFT.id, // 文章状态 0 草稿 1 发布 2 删除
    var viewCount: Int = 0, // 浏览次数
    var likeCount: Int = 0, // 点赞数
    var starCount: Int = 0, // 收藏数
    var commentCount: Int = 0, // 评论数
    var authorId: Long? = null, // 作者 ID
    var circleId: Long? = null, // 圈子 ID
    var tagIds: MutableSet<Long> = mutableSetOf() // 标签 ID
) {

    init {
        onCreate()
    }

    fun onCreate() {
        createTime = LocalDateTime.now()
        if (status == ArticleStatus.PUBLISHED.id && publishTime == null) {
            publishTime = LocalDateTime.now()
        }
    }

    fun onUpdate() {
        updateTime = LocalDateTime.now()
    }
}
