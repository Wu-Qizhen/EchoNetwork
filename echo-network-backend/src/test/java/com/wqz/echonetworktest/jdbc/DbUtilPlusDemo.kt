package com.wqz.echonetworktest.jdbc

import com.wqz.echonetworktest.pojo.User
import com.wqz.echonetworktest.utils.DatabaseUtilPlus
import com.wqz.echonetworktest.utils.queryFirst
import com.wqz.echonetworktest.utils.withTransaction

class DbUtilPlusDemo {

    fun example() {
        val dbUtil = DatabaseUtilPlus.create()

        dbUtil.withTransaction {
            update("INSERT INTO users (name, age) VALUES (?, ?)", "John", 25)
            update("UPDATE accounts SET balance = balance - ? WHERE id = ?", 100.0, 1)
        }

        // 查询列表
        val users: List<User> = dbUtil.query("SELECT * FROM users WHERE age > ?", 18)
        println(users)

        // 查询单个对象
        val user: User? = dbUtil.queryFirst("SELECT * FROM users WHERE id = ?", 1)
        println(user)

        // 查询标量值
        val count: Int? = dbUtil.queryScalar("SELECT COUNT(*) FROM users")
        println(count)
    }
}
