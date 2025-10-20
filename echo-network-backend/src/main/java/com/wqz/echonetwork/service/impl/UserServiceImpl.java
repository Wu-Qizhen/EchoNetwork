package com.wqz.echonetwork.service.impl;

import com.wqz.echonetwork.entity.User;
import com.wqz.echonetwork.entity.dto.UserLoginResponse;
import com.wqz.echonetwork.entity.dto.UserRegisterRequest;
import com.wqz.echonetwork.entity.enums.UserStatus;
import com.wqz.echonetwork.entity.vo.UserVO;
import com.wqz.echonetwork.mapper.UserMapper;
import com.wqz.echonetwork.service.UserService;
import com.wqz.echonetwork.utils.ConstField;
import com.wqz.echonetwork.utils.JwtUtil;
import com.wqz.echonetwork.utils.PasswordEncoder;
import com.wqz.echonetwork.utils.RedisUtil;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public class UserServiceImpl implements UserService {

    UserMapper userMapper = new UserMapper();

    @Override
    public UserLoginResponse login(String username, String password) {
        User user = userMapper.findByUserNameOrEmail(username);
        if (user == null || user.getId() == null) {
            return null;
        }

        if (!PasswordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        if (user.getStatus() != UserStatus.NORMAL.getId()) {
            return null;
        }

        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateLastLoginTime(user.getId(), user.getLastLoginTime());

        String token = JwtUtil.generateToken(user);

        UserVO userVO = new UserVO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getNickname(),
                user.getBio(),
                user.getAvatarUrl(),
                user.getRole(),
                user.getFollowerCount(),
                Objects.requireNonNull(user.getLastLoginTime())
        );

        return new UserLoginResponse(token, userVO);
    }

    @Override
    public String register(UserRegisterRequest userRegisterRequest) {
        String username = userRegisterRequest.getUsername();
        String email = userRegisterRequest.getEmail();
        String password = userRegisterRequest.getPassword();
        String captcha = userRegisterRequest.getCaptcha();

        String key = ConstField.CAPTCHA_DATA + "register:" + email;
        String rightCaptcha = RedisUtil.get(key);
        if (rightCaptcha == null) {
            return "请先获取验证码";
        }
        if (!rightCaptcha.equals(captcha)) {
            return "验证码错误";
        }
        if (userMapper.findByUsername(username) != null) {
            return "用户名已被注册";
        }
        if (userMapper.findByEmail(email) != null) {
            return "邮箱已被注册";
        }

        String encodePassword = PasswordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodePassword);
        user.setNickname(username);
        user.setCreateTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());
        int insert = userMapper.insert(user);
        if (insert == 0) {
            return "注册失败，请联系客服";
        } else {
            RedisUtil.del(key);
            return null;
        }
    }
}
