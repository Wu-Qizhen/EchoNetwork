package com.wqz.echonetworktest.mapper;

import com.wqz.echonetwork.entity.po.User;
import com.wqz.echonetwork.mapper.UserMapper;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.22
 */
public class MapperDemo {
    public static void main(String[] args) {
        UserMapper userMapper = new UserMapper();
        /* List<User> search = userMapper.search("1");
        System.out.println(search); */
        User user = userMapper.findById(1L);
        System.out.println(user);
    }
}
