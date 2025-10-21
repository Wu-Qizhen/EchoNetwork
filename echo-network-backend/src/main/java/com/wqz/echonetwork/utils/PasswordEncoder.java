package com.wqz.echonetwork.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public class PasswordEncoder {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("原始密码不能为空");
        }
        return encoder.encode(rawPassword);
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return encoder.matches(rawPassword, encodedPassword);
    }

    public static boolean upgradeEncoding(String encodedPassword) {
        if (encodedPassword == null) {
            return false;
        }
        return encoder.upgradeEncoding(encodedPassword);
    }

    public static BCryptPasswordEncoder createEncoder(int strength) {
        return new BCryptPasswordEncoder(strength);
    }

    /* public static void main(String[] args) {
        String rawPassword = "MyPassword123";

        String encodedPassword = PasswordEncoder.encode(rawPassword);
        LogUtil.info("加密后的密码：" + encodedPassword);

        boolean isMatch = PasswordEncoder.matches(rawPassword, encodedPassword);
        LogUtil.info("密码验证结果：" + isMatch);

        boolean isWrongMatch = PasswordEncoder.matches("WrongPassword", encodedPassword);
        LogUtil.info("错误密码验证结果：" + isWrongMatch);

        boolean needUpgrade = PasswordEncoder.upgradeEncoding(encodedPassword);
        LogUtil.info("是否需要重新加密：" + needUpgrade);
    } */
}
