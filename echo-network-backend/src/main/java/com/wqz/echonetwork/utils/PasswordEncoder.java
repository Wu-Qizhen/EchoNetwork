package com.wqz.echonetwork.utils;

import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public class PasswordEncoder {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
    private final int strength;
    private final SecureRandom random;

    public PasswordEncoder() {
        this(10);
    }

    public PasswordEncoder(int strength) {
        this(strength, null);
    }

    public PasswordEncoder(int strength, SecureRandom random) {
        if (strength != -1 && (strength < 4 || strength > 31)) {
            throw new IllegalArgumentException("强度必须在 4 到 31 之间");
        }
        this.strength = strength;
        this.random = random;
    }

    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("原始密码不能为空");
        }
        String salt;
        if (strength > 0) {
            if (random != null) {
                salt = BCrypt.genSalt(strength, random);
            } else {
                salt = BCrypt.genSalt(strength);
            }
        } else {
            salt = BCrypt.genSalt();
        }
        return BCrypt.hash(rawPassword.toString(), salt);
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            return false;
        }
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            return false;
        }
        if (!BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
            return false;
        }
        return BCrypt.check(rawPassword.toString(), encodedPassword);
    }

    private static class BCrypt {
        private static final String BASE64_CHARS = "./ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        private static final int ROUNDS_DEFAULT = 10;

        public static String genSalt() {
            return genSalt(ROUNDS_DEFAULT);
        }

        public static String genSalt(int strength) {
            return genSalt(strength, new SecureRandom());
        }

        public static String genSalt(int strength, SecureRandom random) {
            StringBuilder rs = new StringBuilder();
            rs.append("$2a$");
            if (strength < 10) {
                rs.append("0");
            }
            rs.append(strength);
            rs.append("$");

            byte[] salt = new byte[16];
            random.nextBytes(salt);

            int off = 0;
            while (off < salt.length) {
                int c1 = salt[off++] & 0xff;
                rs.append(BASE64_CHARS.charAt(c1 & 0x3f));
                if (off >= salt.length) {
                    break;
                }
                c1 = (c1 << 8) | (salt[off] & 0xff);
                rs.append(BASE64_CHARS.charAt((c1 >> 6) & 0x3f));
                if (off++ >= salt.length) {
                    break;
                }
                c1 = (c1 << 8) | (salt[off] & 0xff);
                rs.append(BASE64_CHARS.charAt((c1 >> 12) & 0x3f));
                rs.append(BASE64_CHARS.charAt((c1 >> 6) & 0x3f));
            }
            return rs.toString();
        }

        public static String hash(String password, String salt) {
            if (salt.startsWith("$2a$") && salt.length() >= 29) {
                String saltPart = salt.substring(0, 29);
                String pseudoHash = pseudoBcrypt(password, saltPart);
                return saltPart + pseudoHash;
            }
            throw new IllegalArgumentException("无效的盐值格式");
        }

        private static String pseudoBcrypt(String password, String salt) {
            String combined = password + salt;
            int hash = combined.hashCode();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 31; i++) {
                char c = BASE64_CHARS.charAt(Math.abs(hash + i) % BASE64_CHARS.length());
                sb.append(c);
            }
            return sb.toString();
        }

        public static boolean check(String plaintext, String hashed) {
            if (hashed.startsWith("$2a$") && hashed.length() >= 60) {
                String salt = hashed.substring(0, 29);
                String expectedHash = hash(plaintext, salt);
                return expectedHash.equals(hashed);
            }
            return false;
        }
    }

    /* public static void main(String[] args) {
        PasswordEncoder encoder = new PasswordEncoder(10);

        String rawPassword = "MySecretPassword";
        System.out.println("原始密码：" + rawPassword);

        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("编码后密码：" + encodedPassword);

        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("验证匹配：" + matches);

        boolean matchesWrong = encoder.matches("WrongPassword", encodedPassword);
        System.out.println("错误密码验证：" + matchesWrong);
    } */
}
