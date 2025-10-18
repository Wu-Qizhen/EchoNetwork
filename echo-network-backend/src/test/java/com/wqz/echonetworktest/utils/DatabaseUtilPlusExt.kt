package com.wqz.echonetworktest.utils

inline fun <reified T : Any> DatabaseUtilPlus.queryList(sql: String, vararg params: Any?): List<T> = query(sql, *params)
inline fun <reified T : Any> DatabaseUtilPlus.queryFirst(sql: String, vararg params: Any?): T? =
    queryObject(sql, *params)

inline fun <T> DatabaseUtilPlus.withTransaction(block: DatabaseUtilPlus.() -> T): T {
    return transaction { block() }
}
