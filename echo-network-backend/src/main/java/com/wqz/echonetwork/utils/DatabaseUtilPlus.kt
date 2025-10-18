package com.wqz.echonetwork.utils

import java.sql.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
class DatabaseUtilPlus private constructor(
    private val driver: String,
    private val url: String,
    private val username: String,
    private val password: String
) {
    private var connection: Connection? = null
    private var enableTransaction: Boolean = false

    companion object {
        fun create(): DatabaseUtilPlus {
            val config = YamlLoader("application.yml")
            return DatabaseUtilPlus(
                driver = config.getString("app.database.driver"),
                url = config.getString("app.database.url"),
                username = config.getString("app.database.username"),
                password = config.getString("app.database.password")
            )
        }

        fun create(driver: String, url: String, username: String, password: String): DatabaseUtilPlus {
            return DatabaseUtilPlus(driver, url, username, password)
        }
    }

    init {
        loadDriver()
    }

    private fun loadDriver() {
        try {
            Class.forName(driver)
        } catch (e: ClassNotFoundException) {
            throw RuntimeException("数据库驱动加载失败：$driver", e)
        }
    }

    public fun getConnection(): Connection {
        return connection?.takeIf { !it.isClosed } ?: run {
            DriverManager.getConnection(url, username, password).also {
                connection = it
            }
        }
    }

    @Throws(SQLException::class)
    fun beginTransaction() {
        require(!enableTransaction) { "事务已开启" }
        getConnection().autoCommit = false
        enableTransaction = true
    }

    @Throws(SQLException::class)
    fun commitTransaction() {
        require(enableTransaction) { "事务未开启" }
        connection?.commit()
        connection?.autoCommit = true
        enableTransaction = false
    }

    fun rollbackTransaction() {
        if (connection != null && enableTransaction) {
            runCatching {
                connection?.rollback()
                connection?.autoCommit = true
            }.onFailure {
                // 忽略异常
            }.also {
                enableTransaction = false
            }
        }
    }

    @Throws(SQLException::class)
    inline fun <T> transaction(operation: () -> T): T {
        return try {
            beginTransaction()
            val result = operation()
            commitTransaction()
            // println("事务提交成功！")
            result
        } catch (e: SQLException) {
            rollbackTransaction()
            // println("事务回滚：${e.message}")
            throw e
        }
    }

    @Throws(SQLException::class)
    fun update(sql: String, vararg params: Any?): Int {
        return getConnection().use { conn ->
            conn.prepareStatement(sql).use { ps ->
                setParameters(ps, params)
                ps.executeUpdate()
            }
        }
    }

    @Throws(SQLException::class)
    fun updateBatch(sql: String, paramList: List<Array<Any?>>): IntArray {
        return getConnection().use { conn ->
            conn.prepareStatement(sql).use { ps ->
                paramList.forEach { params ->
                    setParameters(ps, params)
                    ps.addBatch()
                }
                ps.executeBatch()
            }
        }
    }

    inline fun <reified T : Any> query(sql: String, vararg params: Any?): List<T> {
        return getConnection().use { conn ->
            conn.prepareStatement(sql).use { ps ->
                setParameters(ps, params)
                ps.executeQuery().use { rs ->
                    mapResultSetToList(rs, T::class)
                }
            }
        }
    }

    inline fun <reified T : Any> queryObject(sql: String, vararg params: Any?): T? {
        return query<T>(sql, *params).firstOrNull()
    }

    inline fun <reified T : Any> queryScalar(sql: String, vararg params: Any?): T? {
        return getConnection().use { conn ->
            conn.prepareStatement(sql).use { ps ->
                setParameters(ps, params)
                ps.executeQuery().use { rs ->
                    if (rs.next()) {
                        rs.getObject(1) as? T
                    } else {
                        null
                    }
                }
            }
        }
    }

    fun setParameters(ps: PreparedStatement, params: Array<out Any?>) {
        params.forEachIndexed { index, value ->
            ps.setObject(index + 1, value)
        }
    }

    fun <T : Any> mapResultSetToList(rs: ResultSet, clazz: KClass<T>): List<T> {
        val resultList = mutableListOf<T>()
        val metaData = rs.metaData
        val columnCount = metaData.columnCount

        while (rs.next()) {
            val obj = createInstance(clazz)
            for (i in 1..columnCount) {
                val columnName = metaData.getColumnLabel(i)
                val value = rs.getObject(i)
                setFieldValue(obj, columnName, value)
            }
            resultList.add(obj)
        }

        return resultList
    }

    private fun <T : Any> createInstance(clazz: KClass<T>): T {
        return clazz.primaryConstructor?.call()
            ?: throw IllegalArgumentException("类 ${clazz.simpleName} 没有无参构造函数")
    }

    private fun <T : Any> setFieldValue(obj: T, fieldName: String, value: Any?) {
        try {
            val field = obj::class.java.getDeclaredField(fieldName).apply {
                isAccessible = true
            }
            field.set(obj, value)
        } catch (e: NoSuchFieldException) {
            // 忽略大小写和下划线
            obj::class.java.declaredFields
                .find { it.name.equals(fieldName.replace("_", ""), ignoreCase = true) }
                ?.apply {
                    isAccessible = true
                    set(obj, value)
                }
        } catch (e: IllegalAccessException) {
            // 忽略
        }
    }

    fun closeConnection() {
        if (enableTransaction) {
            rollbackTransaction()
        }
        runCatching {
            connection?.close()
        }
        connection = null
    }
}
