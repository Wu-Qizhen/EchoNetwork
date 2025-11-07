package com.wqz.echonetwork.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Random;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.19
 */
public class CaptchaUtil {

    private static final Random random = new Random();
    private static final int CAPTCHA_LENGTH;
    private static final int EXPIRE_SECONDS;
    private static final int COOL_SECONDS;
    private static final int IP_LIMIT_COUNT;
    private static final int IP_LIMIT_WINDOW;
    private static final int IP_BAN_SECONDS;

    static {
        YamlLoader yamlLoader = new YamlLoader("application.yml");
        CAPTCHA_LENGTH = yamlLoader.getInt("app.captcha.length");
        EXPIRE_SECONDS = yamlLoader.getInt("app.captcha.expiration");
        COOL_SECONDS = yamlLoader.getInt("app.captcha.cool");
        IP_LIMIT_COUNT = yamlLoader.getInt("app.captcha.ip-limit-count", 10);
        IP_LIMIT_WINDOW = yamlLoader.getInt("app.captcha.ip-limit-window", 3600);
        IP_BAN_SECONDS = yamlLoader.getInt("app.captcha.ip-ban-seconds", 7200);
    }

    public static String generateCaptcha() {
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            captcha.append(random.nextInt(10));
        }
        return captcha.toString();
    }

    public static boolean isInCool(String key) {
        String coolKey = buildCoolKey(key);
        return RedisUtil.exists(coolKey);
    }

    public static long getCoolRemaining(String key) {
        String coolKey = buildCoolKey(key);
        try (Jedis jedis = RedisUtil.getJedis()) {
            return jedis.ttl(coolKey);
        }
    }

    public static boolean isIpBanned(String ip) {
        String banKey = buildIpBanKey(ip);
        return RedisUtil.exists(banKey);
    }

    public static long getIpBanRemaining(String ip) {
        String banKey = buildIpBanKey(ip);
        try (Jedis jedis = RedisUtil.getJedis()) {
            return jedis.ttl(banKey);
        }
    }

    public static boolean recordIpRequest(String ip) {
        String ipLimitKey = buildIpLimitKey(ip);

        try (Jedis jedis = RedisUtil.getJedis()) {
            jedis.watch(ipLimitKey);

            long currentTime = System.currentTimeMillis() / 1000;
            long windowStart = currentTime - IP_LIMIT_WINDOW;

            // 移除时间窗口外的记录
            jedis.zremrangeByScore(ipLimitKey, 0, windowStart);

            // 获取当前窗口内的请求次数
            long requestCount = jedis.zcard(ipLimitKey);

            if (requestCount >= IP_LIMIT_COUNT) {
                jedis.unwatch();
                return true; // 超过限制
            }

            // 开启事务并添加当前请求时间戳
            Transaction transaction = jedis.multi();
            transaction.zadd(ipLimitKey, currentTime, String.valueOf(currentTime));
            transaction.expire(ipLimitKey, (long) IP_LIMIT_WINDOW);
            transaction.exec(); // 提交事务

            return false;
        }
    }


    public static void banIp(String ip) {
        String banKey = buildIpBanKey(ip);
        RedisUtil.set(banKey, IP_BAN_SECONDS, "1");

        String ipLimitKey = buildIpLimitKey(ip);
        RedisUtil.del(ipLimitKey);
    }

    public static void unbanIp(String ip) {
        String banKey = buildIpBanKey(ip);
        RedisUtil.del(banKey);
    }

    /* public static String generateAndStoreCaptcha(String key) {
        if (isInCool(key)) {
            return null;
        }

        String captcha = generateCaptcha();
        String redisKey = buildRedisKey(key);
        String coolKey = buildCoolKey(key);

        RedisUtil.set(redisKey, EXPIRE_SECONDS, captcha);
        RedisUtil.set(coolKey, COOL_SECONDS, "1");

        return captcha;
    } */

    public static String generateAndStoreCaptcha(String key, String ip) {
        // 检查 IP 封禁
        if (isIpBanned(ip)) {
            return null;
        }

        // 检查冷却时间
        if (isInCool(key)) {
            return null;
        }

        // 记录 IP 请求并检查限制
        if (recordIpRequest(ip)) {
            banIp(ip); // 触发封禁
            return null;
        }

        String captcha = generateCaptcha();
        String redisKey = buildRedisKey(key);
        String coolKey = buildCoolKey(key);

        RedisUtil.set(redisKey, EXPIRE_SECONDS, captcha);
        RedisUtil.set(coolKey, COOL_SECONDS, "1");

        return captcha;
    }

    public static boolean verifyCaptcha(String key, String inputCaptcha) {
        if (inputCaptcha == null || inputCaptcha.trim().isEmpty()) {
            return false;
        }

        String redisKey = buildRedisKey(key);
        String storedCaptcha = RedisUtil.get(redisKey);

        if (storedCaptcha == null) {
            return false;
        }

        if (storedCaptcha.equals(inputCaptcha.trim())) {
            RedisUtil.del(redisKey);
            return true;
        }

        return false;
    }

    public static boolean existsCaptcha(String key) {
        return RedisUtil.exists(buildRedisKey(key));
    }

    public static long getCaptchaTtl(String key) {
        try (Jedis jedis = RedisUtil.getJedis()) {
            return jedis.ttl(buildRedisKey(key));
        }
    }

    public static long getIpRequestCount(String ip) {
        String ipLimitKey = buildIpLimitKey(ip);
        try (Jedis jedis = RedisUtil.getJedis()) {
            long currentTime = System.currentTimeMillis() / 1000;
            long windowStart = currentTime - IP_LIMIT_WINDOW;
            jedis.zremrangeByScore(ipLimitKey, 0, windowStart);
            return jedis.zcard(ipLimitKey);
        }
    }

    private static String buildRedisKey(String key) {
        return ConstField.CAPTCHA_DATA + key;
    }

    private static String buildCoolKey(String key) {
        return ConstField.CAPTCHA_LIMIT + key;
    }

    private static String buildIpLimitKey(String ip) {
        return ConstField.CAPTCHA_IP_LIMIT + ip;
    }

    private static String buildIpBanKey(String ip) {
        return ConstField.CAPTCHA_IP_BAN + ip;
    }
}
