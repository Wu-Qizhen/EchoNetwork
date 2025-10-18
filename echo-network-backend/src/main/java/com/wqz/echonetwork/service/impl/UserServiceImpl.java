package com.wqz.echonetwork.service.impl;

import com.wqz.echonetwork.entity.User;
import com.wqz.echonetwork.mapper.UserMapper;
import com.wqz.echonetwork.service.UserService;
import com.wqz.echonetwork.utils.PasswordEncoder;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public class UserServiceImpl implements UserService {

    @Override
    public User login(String username, String password) {
        UserMapper userMapper = new UserMapper();
        User user = userMapper.findByUsername(username);
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else return null;
    }
}
