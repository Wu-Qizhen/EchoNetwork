package com.wqz.echonetwork.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.19
 */
public class MailSender { // TODO RabbitMQ 优化
    private static final String SMTP_HOST;
    private static final String USERNAME;
    private static final String PASSWORD;
    private static final boolean SSL_ENABLE;
    private static final int SMTP_PORT;

    static {
        YamlLoader yamlLoader = new YamlLoader("application.yml");
        SMTP_HOST = yamlLoader.getString("app.mail.host");
        USERNAME = yamlLoader.getString("app.mail.username");
        PASSWORD = yamlLoader.getString("app.mail.password");
        SSL_ENABLE = yamlLoader.getBoolean("app.mail.ssl");
        SMTP_PORT = yamlLoader.getInt("app.mail.port");
    }

    public static void sendMail(String toEmail, String subject, String content) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.enable", SSL_ENABLE);
            props.put("mail.smtp.auth", "true");

            // 创建会话
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            // 创建邮件
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(content);
            message.setSentDate(new java.util.Date());

            // 发送邮件
            Transport.send(message);
            // LogUtil.log("邮件发送成功至 " + toEmail);

        } catch (Exception e) {
            LogUtil.error("邮件发送失败：" + e.getMessage());
            throw new RuntimeException("邮件发送失败", e);
        }
    }

    public static void sendVerificationCode(String email, String code, String type) {
        String subject;
        String content;
        // LogUtil.info("已发送验证码：" + code);

        switch (type) {
            case "register":
                subject = "欢迎注册回声网络";
                content = "您的邮件注册验证码为：" + code + "，有效时间 3 分钟，为了保障您的账户安全，请勿向他人泄露验证码信息";
                break;
            case "reset":
                subject = "回声网络密码重置";
                content = "您正在执行重置密码操作，验证码：" + code + "，有效时间 3 分钟，如非本人操作，请无视";
                break;
            default:
                throw new IllegalArgumentException("不支持的邮件类型：" + type);
        }

        sendMail(email, subject, content);
    }
}
