package com.wqz.echonetwork.service;

import com.wqz.echonetwork.entity.dto.*;
import com.wqz.echonetwork.entity.vo.UserVO;

import java.util.List;

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

    UserProfileResponse getProfile(Long userId, Long currentUserId);

    String updateProfile(UserProfileRequest userProfileRequest, Long userId);

    FollowInteractionResponse followUser(Long targetUserId, Long currentUserId);

    FollowInteractionResponse unfollowUser(Long targetUserId, Long currentUserId);

    List<UserVO> getFollowers(Long userId, Long currentUserId);

    List<UserVO> getFollowing(Long userId, Long currentUserId);
}
