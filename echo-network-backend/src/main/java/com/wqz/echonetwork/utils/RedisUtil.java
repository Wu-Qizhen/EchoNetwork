package com.wqz.echonetwork.utils;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.19
 */
public class RedisUtil {

    private static final JedisPool jedisPool;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        YamlLoader yamlLoader = new YamlLoader("application.yml");
        String redisHost = yamlLoader.getString("app.redis.host");
        int redisPort = yamlLoader.getInt("app.redis.port");
        // String redisPassword = yamlLoader.getString("app.redis.password"); TODO 密码设置不生效
        int redisDatabase = yamlLoader.getInt("app.redis.database");
        int redisTimeout = yamlLoader.getInt("app.redis.timeout");
        int redisMaxActive = yamlLoader.getInt("app.redis.lettuce.pool.max-active");
        int redisMaxIdle = yamlLoader.getInt("app.redis.lettuce.pool.max-idle");
        int redisMinIdle = yamlLoader.getInt("app.redis.lettuce.pool.min-idle");
        // int redisMaxWait = yamlLoader.getInt("app.redis.lettuce.pool.max-wait");

        config.setMaxTotal(redisMaxActive);
        config.setMaxIdle(redisMaxIdle);
        config.setMinIdle(redisMinIdle);
        // config.setMaxWaitMillis(redisMaxWait);
        config.setTestOnBorrow(true);

        // jedisPool = new JedisPool(config, redisHost, redisPort, redisTimeout, redisPassword, Integer.parseInt(redisDatabase));

        // 使用自定义禁用客户端设置
        jedisPool = new JedisPool(
                config,
                redisHost,
                redisPort,
                redisTimeout,
                null /* redisPassword */,
                redisDatabase /* Protocol.DEFAULT_DATABASE */,
                null,
                false,
                null,
                null,
                null) {
            @Override
            public void returnBrokenResource(Jedis resource) {
                if (resource != null) {
                    try {
                        resource.close();
                    } catch (Exception e) {
                        // 忽略关闭异常
                    }
                }
            }
        };
    }

    /* public static Jedis getJedis() {
        return jedisPool.getResource();
    } */

    public static Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.ping();
            return jedis;
        } catch (Exception e) {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (Exception ex) {
                    // 忽略关闭异常
                }
            }
            return jedisPool.getResource();
        }
    }

    /* public static void set(String key, int seconds, String value) {
        try (Jedis jedis = getJedis()) {
            jedis.setex(key, seconds, value);
        }
    } */

    public static void set(String key, int seconds, String value) {
        try (Jedis jedis = getJedis()) {
            jedis.setex(key, (long) seconds, value);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /* public static String get(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(key);
        }
    } */

    public static String get(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(key);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /* public static void del(String key) {
        try (Jedis jedis = getJedis()) {
            jedis.del(key);
        }
    } */

    public static void del(String key) {
        try (Jedis jedis = getJedis()) {
            jedis.del(key);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /* public static boolean exists(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.exists(key);
        }
    } */

    public static boolean exists(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.exists(key);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void printPoolStats() {
        if (jedisPool != null) {
            System.out.println("Active: " + jedisPool.getNumActive());
            System.out.println("Idle: " + jedisPool.getNumIdle());
            System.out.println("Waiters: " + jedisPool.getNumWaiters());
        }
    }

    public static void close() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }
}
