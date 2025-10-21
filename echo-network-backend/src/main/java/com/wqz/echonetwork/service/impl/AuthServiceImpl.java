package com.wqz.echonetwork.service.impl;

import com.wqz.echonetwork.entity.User;
import com.wqz.echonetwork.mapper.UserMapper;
import com.wqz.echonetwork.service.AuthService;
import com.wqz.echonetwork.utils.CaptchaUtil;
import com.wqz.echonetwork.utils.MailSender;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.19
 */
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper = new UserMapper();

    @Override
    public String askEmailVerifyCode(String type, String email) {
        String key = type + ":" + email;

        if (CaptchaUtil.isInCool(key)) {
            return "请求频繁，请稍后再试";
        }

        User user = userMapper.findByEmail(email);
        if (type.equals("register") && user != null) {
            return "邮箱已被注册";
        }
        if (type.equals("reset") && user == null) {
            return "邮箱不存在";
        }

        String captcha = CaptchaUtil.generateAndStoreCaptcha(key);
        if (captcha == null) {
            return "验证码生成失败";
        }

        MailSender.sendVerificationCode(email, captcha, type);

        // RedisUtil.printPoolStats();
        // LogUtil.info("验证码：" + captcha);
        return null;
    }
}
