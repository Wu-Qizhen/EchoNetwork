package com.wqz.echonetwork.utils;

import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.19
 */
public class CaptchaUtil { // TODO 锁 IP

    private static final Random random = new Random();
    private static final int CAPTCHA_LENGTH;
    private static final int EXPIRE_SECONDS;
    private static final int COOL_SECONDS;

    static {
        YamlLoader yamlLoader = new YamlLoader("application.yml");
        CAPTCHA_LENGTH = yamlLoader.getInt("app.captcha.length");
        EXPIRE_SECONDS = yamlLoader.getInt("app.captcha.expiration");
        COOL_SECONDS = yamlLoader.getInt("app.captcha.cool");
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

    public static String generateAndStoreCaptcha(String key) {
        if (isInCool(key)) {
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

    private static String buildRedisKey(String key) {
        return ConstField.CAPTCHA_DATA + key;
    }

    private static String buildCoolKey(String key) {
        return ConstField.CAPTCHA_LIMIT + key;
    }
}
