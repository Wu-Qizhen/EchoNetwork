package com.wqz.echonetwork.entity.vo

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.24
 */
data class PageResult<T>(
    var list: List<T> = emptyList(),
    var total: Long = 0L,
    var page: Int = 1,
    var size: Int = 10
) {
    val totalPages: Int
        get() = if (size > 0) kotlin.math.ceil(total.toDouble() / size).toInt() else 0
}
