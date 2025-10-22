package com.wqz.echonetwork.service;

import com.wqz.echonetwork.entity.dto.*;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public interface UserService {

    UserLoginResponse login(String username, String password);

    String register(UserRegisterRequest userRegisterRequest);

    String resetVerify(ResetVerifyRequest resetVerifyRequest);

    String resetPassword(ResetPasswordRequest resetPasswordRequest);

    UserProfileResponse getProfile(Long userId);
}
