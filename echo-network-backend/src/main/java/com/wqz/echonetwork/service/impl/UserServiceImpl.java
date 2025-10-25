package com.wqz.echonetwork.service.impl;

import com.wqz.echonetwork.entity.dto.*;
import com.wqz.echonetwork.entity.enums.UserStatus;
import com.wqz.echonetwork.entity.po.Follow;
import com.wqz.echonetwork.entity.po.User;
import com.wqz.echonetwork.entity.vo.UserVO;
import com.wqz.echonetwork.mapper.ArticleMapper;
import com.wqz.echonetwork.mapper.FollowMapper;
import com.wqz.echonetwork.mapper.UserMapper;
import com.wqz.echonetwork.service.UserService;
import com.wqz.echonetwork.utils.ConstField;
import com.wqz.echonetwork.utils.JwtUtil;
import com.wqz.echonetwork.utils.PasswordEncoder;
import com.wqz.echonetwork.utils.RedisUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public class UserServiceImpl implements UserService {

    UserMapper userMapper = new UserMapper();

    FollowMapper followMapper = new FollowMapper();

    ArticleMapper articleMapper = new ArticleMapper();

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
        UserProfileResponse profile = this.getProfile(user.getId(), user.getId());

        UserVO userVO = profile.getUser();

        return new UserLoginResponse(token, userVO, JwtUtil.getExpirationDateFromToken(token));
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

    @Override
    public String resetVerify(ResetVerifyRequest resetVerifyRequest) {
        String email = resetVerifyRequest.getEmail();
        String rightCaptcha = RedisUtil.get(ConstField.CAPTCHA_DATA + "reset:" + email);

        if (rightCaptcha == null || rightCaptcha.isEmpty()) {
            return "请先获取验证码";
        }

        if (!rightCaptcha.equals(resetVerifyRequest.getCaptcha())) {
            return "验证码错误";
        }

        return null;
    }

    @Override
    public String resetPassword(ResetPasswordRequest resetPasswordRequest) {
        String email = resetPasswordRequest.getEmail();
        String verify = this.resetVerify(new ResetVerifyRequest(email, resetPasswordRequest.getCaptcha()));
        if (verify != null) {
            return verify;
        }
        String password = PasswordEncoder.encode(resetPasswordRequest.getPassword());
        int updated = userMapper.updatePasswordByEmail(email, password);

        if (updated == 0) {
            return "重置密码失败，请联系客服";
        } else {
            RedisUtil.del(ConstField.CAPTCHA_DATA + "reset:" + email);
        }

        return null;
    }

    @Override
    public UserProfileResponse getProfile(Long userId, Long currentUserId) {
        // LogUtil.info("getProfile");
        User user = userMapper.findById(userId);

        if (user == null || user.getId() == null || user.getStatus() != UserStatus.NORMAL.getId()) {
            return null;
        }

        int countFollowing = followMapper.countFollowing(userId);
        // LogUtil.info(String.valueOf(countFollowing));
        int countArticles = articleMapper.countByUserId(userId);
        // LogUtil.info(String.valueOf(countArticles));
        boolean existsed = followMapper.existsByUserIds(currentUserId, userId);
        // LogUtil.info("existsed");

        return new UserProfileResponse(
                new UserVO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getNickname(),
                        user.getBio(),
                        user.getAvatarUrl(),
                        user.getFollowerCount(),
                        countFollowing,
                        countArticles,
                        Objects.requireNonNull(user.getCreateTime()),
                        Objects.requireNonNull(user.getLastLoginTime()),
                        user.getRole(),
                        user.getStatus()
                ),
                existsed
        );
    }

    @Override
    public String updateProfile(UserProfileRequest userProfileRequest, Long userId) {
        User user = userMapper.findById(userId);

        if (user == null || user.getId() == null) {
            return "用户不存在";
        }

        int result = userMapper.updateProfile(userId, userProfileRequest.getNickname(), userProfileRequest.getBio(), userProfileRequest.getAvatarUrl());
        if (result > 0) {
            return null;
        }

        return "更新失败";
    }

    @Override
    public FollowInteractionResponse followUser(Long targetUserId, Long currentUserId) {
        // 不能关注自己
        if (targetUserId.equals(currentUserId)) {
            return null;
        }

        // 检查目标用户是否存在
        User targetUser = userMapper.findById(targetUserId);
        if (targetUser == null || targetUser.getStatus() != UserStatus.NORMAL.getId()) {
            return null;
        }

        // 检查是否已经关注
        boolean alreadyFollowing = followMapper.existsByUserIds(currentUserId, targetUserId);
        if (alreadyFollowing) {
            return getFollowInteractionStatus(targetUserId, currentUserId);
        }

        // 创建关注记录
        Follow follow = new Follow();
        follow.setFollowerUserId(currentUserId);
        follow.setFollowingUserId(targetUserId);

        long followId = followMapper.insert(follow);
        if (followId > 0) {
            // 更新目标用户的粉丝数
            int result = userMapper.updateFollowerCount(targetUserId, 1);
            if (result > 0) {
                return getFollowInteractionStatus(targetUserId, currentUserId);
            }
            return null;
        }

        return null;
    }

    @Override
    public FollowInteractionResponse unfollowUser(Long targetUserId, Long currentUserId) {
        // 检查是否已经关注
        boolean alreadyFollowing = followMapper.existsByUserIds(currentUserId, targetUserId);
        if (!alreadyFollowing) {
            return getFollowInteractionStatus(targetUserId, currentUserId);
        }

        // 删除关注记录
        int deleted = followMapper.delete(currentUserId, targetUserId);
        if (deleted > 0) {
            // 更新目标用户的粉丝数
            int result = userMapper.updateFollowerCount(targetUserId, -1);
            if (result > 0) {
                return getFollowInteractionStatus(targetUserId, currentUserId);
            }
            return null;
        }

        return null;
    }

    @Override
    public List<UserVO> getFollowers(Long userId, Long currentUserId) {
        List<Follow> follows = followMapper.findFollowers(userId);
        List<Long> followerIds = follows.stream()
                .map(Follow::getFollowerUserId)
                .collect(Collectors.toList());

        if (followerIds.isEmpty()) {
            return new ArrayList<>();
        }

        return convertToUserVOList(followerIds, currentUserId);
    }

    @Override
    public List<UserVO> getFollowing(Long userId, Long currentUserId) {
        List<Follow> follows = followMapper.findFollowing(userId);
        List<Long> followingIds = follows.stream()
                .map(Follow::getFollowingUserId)
                .collect(Collectors.toList());

        if (followingIds.isEmpty()) {
            return new ArrayList<>();
        }

        return convertToUserVOList(followingIds, currentUserId);
    }

    // 辅助方法：获取关注互动状态
    private FollowInteractionResponse getFollowInteractionStatus(Long targetUserId, Long currentUserId) {
        User targetUser = userMapper.findById(targetUserId);
        if (targetUser == null) {
            return null;
        }

        boolean isFollowing = followMapper.existsByUserIds(currentUserId, targetUserId);

        return new FollowInteractionResponse(
                targetUserId,
                isFollowing,
                targetUser.getFollowerCount()
        );
    }

    // 辅助方法：批量转换用户 ID 列表为 UserVO 列表
    private List<UserVO> convertToUserVOList(List<Long> userIds, Long currentUserId) {
        if (userIds == null || userIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<UserVO> userVOs = new ArrayList<>();
        for (Long userId : userIds) {
            UserProfileResponse profile = getProfile(userId, currentUserId);
            if (profile != null) {
                userVOs.add(profile.getUser());
            }
        }

        return userVOs;
    }
}
