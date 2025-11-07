package com.wqz.echonetwork.service;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.19
 */
public interface AuthService {
    String askEmailVerifyCode(String type, String email, String ip);
}
