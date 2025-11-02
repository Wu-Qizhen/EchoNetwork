package com.wqz.echonetwork.service.impl;

import com.wqz.echonetwork.entity.dto.CircleCreateRequest;
import com.wqz.echonetwork.entity.dto.CircleJoinResponse;
import com.wqz.echonetwork.entity.dto.CircleQueryRequest;
import com.wqz.echonetwork.entity.dto.UserProfileResponse;
import com.wqz.echonetwork.entity.po.Circle;
import com.wqz.echonetwork.entity.po.CircleMember;
import com.wqz.echonetwork.entity.vo.*;
import com.wqz.echonetwork.mapper.ArticleMapper;
import com.wqz.echonetwork.mapper.CircleMapper;
import com.wqz.echonetwork.mapper.CircleMemberMapper;
import com.wqz.echonetwork.service.CircleService;
import com.wqz.echonetwork.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.25
 */
public class CircleServiceImpl implements CircleService {

    private final CircleMapper circleMapper = new CircleMapper();
    private final CircleMemberMapper circleMemberMapper = new CircleMemberMapper();
    private final UserService userService = new UserServiceImpl();
    private final ArticleMapper articleMapper = new ArticleMapper();

    @Override
    public CircleVO createCircle(CircleCreateRequest request, Long creatorId) {
        // 检查圈子名称是否已存在
        // 这里可以添加名称唯一性检查

        Circle circle = new Circle();
        circle.setName(Objects.requireNonNull(request.getName()));
        circle.setDescription(Objects.requireNonNull(request.getDescription()));
        circle.setCreatorId(creatorId);

        Circle circleByName = circleMapper.findByName(circle.getName());
        if (circleByName != null) {
            return null;
        }

        long circleId = circleMapper.insert(circle);
        if (circleId > 0) {
            // 创建者自动加入圈子，角色为创建者（2）
            CircleMember creatorMember = new CircleMember();
            creatorMember.setCircleId(circleId);
            creatorMember.setUserId(creatorId);
            creatorMember.setRole(2); // 创建者角色

            circleMemberMapper.insert(creatorMember);

            // 更新圈子成员计数
            int updated = circleMapper.updateMemberCount(circleId, 1);
            if (updated > 0) {
                return getCircleDetail(circleId, creatorId);
            }
            return null;
        }

        return null;
    }

    @Override
    public PageResult<CircleListItemVO> getCircles(int page, int size, String keyword, Long currentUserId) {
        int offset = (page - 1) * size;

        List<Circle> circles;
        int total;

        if (keyword != null && !keyword.trim().isEmpty()) {
            circles = circleMapper.findByName(keyword.trim(), offset, size);
            total = circleMapper.countByName(keyword.trim());
        } else {
            circles = circleMapper.findAll(offset, size);
            total = circleMapper.countAll();
        }

        List<CircleListItemVO> circleVOs = convertToCircleListItemVOList(circles, currentUserId);

        return new PageResult<>(circleVOs, total, page, size);
    }

    @Override
    public PageResult<CircleListItemVO> getCirclesByConditions(CircleQueryRequest queryRequest, Long currentUserId) {
        // 构建查询条件
        Map<String, Object> conditions = buildQueryConditions(queryRequest);

        // 计算分页
        int page = Optional.ofNullable(queryRequest.getPage()).orElse(1);
        int size = Optional.ofNullable(queryRequest.getSize()).orElse(10);
        int offset = (page - 1) * size;

        // 查询数据
        List<Circle> circles = circleMapper.findByConditions(conditions, offset, size);
        int total = circleMapper.countByConditions(conditions);

        // 转换为 VO
        List<CircleListItemVO> circleVOs = convertToCircleListItemVOList(circles, currentUserId);

        return new PageResult<>(circleVOs, total, page, size);
    }

    private Map<String, Object> buildQueryConditions(CircleQueryRequest queryRequest) {
        Map<String, Object> conditions = new HashMap<>();

        if (queryRequest.getKeyword() != null) {
            conditions.put("keyword", queryRequest.getKeyword());
        }
        if (queryRequest.getCreatorId() != null) {
            conditions.put("creatorId", queryRequest.getCreatorId());
        }
        if (queryRequest.getMemberId() != null) {
            conditions.put("memberId", queryRequest.getMemberId());
        }

        conditions.put("sortBy", queryRequest.getSortBy());
        conditions.put("sortOrder", queryRequest.getSortOrder());

        return conditions;
    }

    @Override
    public CircleVO getCircleDetail(Long circleId, Long currentUserId) {
        Circle circle = circleMapper.findById(circleId);
        if (circle == null) {
            return null;
        }

        // 获取创建者信息
        UserProfileResponse creatorProfile = userService.getProfile(circle.getCreatorId(), circle.getCreatorId());
        if (creatorProfile == null) {
            return null;
        }

        UserVO creatorVO = creatorProfile.getUser();

        // 获取成员信息
        CircleMember memberInfo;
        boolean isMember = false;
        Integer memberRole = null;
        LocalDateTime joinTime = null;

        if (currentUserId != null) {
            memberInfo = circleMemberMapper.findByCircleAndUser(circleId, currentUserId);
            if (memberInfo != null) {
                isMember = true;
                memberRole = memberInfo.getRole();
                joinTime = memberInfo.getJoinTime();
            }
        }

        // 获取成员数量
        int memberCount = circleMemberMapper.countByCircleId(circleId);

        // 获取文章数量（这里需要实现根据圈子 ID 统计文章的方法）
        int articleCount = articleMapper.countByCircleId(circleId);

        return new CircleVO(
                circleId,
                circle.getName(),
                circle.getDescription(),
                creatorVO,
                circle.getCreateTime(),
                memberCount,
                articleCount,
                isMember,
                memberRole,
                joinTime
        );
    }

    @Override
    public CircleJoinResponse joinCircle(Long circleId, Long userId) {
        // 检查圈子是否存在
        Circle circle = circleMapper.findById(circleId);
        if (circle == null) {
            return null;
        }

        // 检查是否已经是成员
        boolean alreadyMember = circleMemberMapper.exists(circleId, userId);
        if (alreadyMember) {
            return null;
        }

        // 加入圈子
        CircleMember circleMember = new CircleMember();
        circleMember.setCircleId(circleId);
        circleMember.setUserId(userId);
        circleMember.setRole(0); // 普通成员

        long memberId = circleMemberMapper.insert(circleMember);
        if (memberId > 0) {
            // 更新圈子成员计数
            circleMapper.updateMemberCount(circleId, 1);

            return new CircleJoinResponse(
                    circleId,
                    userId,
                    circleMember.getJoinTime(),
                    circleMember.getRole()
            );
        }

        return null;
    }

    @Override
    public boolean exitCircle(Long circleId, Long userId) {
        // 检查是否是成员
        boolean isMember = circleMemberMapper.exists(circleId, userId);
        if (!isMember) {
            return false;
        }

        // 检查是否是创建者（创建者不能退出）
        Circle circle = circleMapper.findById(circleId);
        if (circle != null && Objects.equals(circle.getCreatorId(), userId)) {
            return false;
        }

        // 退出圈子
        int deleted = circleMemberMapper.delete(circleId, userId);
        if (deleted > 0) {
            // 更新圈子成员计数
            circleMapper.updateMemberCount(circleId, -1);
            return true;
        }

        return false;
    }

    @Override
    public PageResult<CircleMemberVO> getCircleMembers(Long circleId, int page, int size, Long currentUserId) {
        int offset = (page - 1) * size;

        List<CircleMember> members = circleMemberMapper.findByCircleId(circleId, offset, size);
        int total = circleMemberMapper.countByCircleId(circleId);

        List<CircleMemberVO> memberVOs = convertToCircleMemberVOList(members);

        return new PageResult<>(memberVOs, total, page, size);
    }

    @Override
    public boolean isMember(Long circleId, Long userId) {
        return circleMemberMapper.exists(circleId, userId);
    }

    @Override
    public List<CircleListItemVO> getUserCircles(Long userId) {
        List<CircleMember> memberships = circleMemberMapper.findByUserId(userId);

        List<Circle> circles = new ArrayList<>();
        for (CircleMember member : memberships) {
            Circle circle = circleMapper.findById(member.getCircleId());
            if (circle != null) {
                circles.add(circle);
            }
        }

        return convertToCircleListItemVOList(circles, userId);
    }

    @Override
    public boolean updateCircle(Long circleId, CircleCreateRequest updateRequest) {
        Circle circle = circleMapper.findById(circleId);
        if (circle == null) {
            return false;
        }

        // 更新圈子信息
        circle.setName(Objects.requireNonNull(updateRequest.getName()).trim());
        circle.setDescription(Objects.requireNonNull(updateRequest.getDescription()).trim());

        int updated = circleMapper.update(circle);
        return updated > 0;
    }

    @Override
    public Circle getCircleById(Long circleId) {
        return circleMapper.findById(circleId);
    }

    @Override
    public Circle getCircleByName(String name) {
        return circleMapper.findByName(name);
    }

    // 辅助方法：转换圈子列表为 VO 列表
    private List<CircleListItemVO> convertToCircleListItemVOList(List<Circle> circles, Long currentUserId) {
        if (circles == null || circles.isEmpty()) {
            return new ArrayList<>();
        }

        List<CircleListItemVO> result = new ArrayList<>();

        for (Circle circle : circles) {
            // 获取创建者信息
            UserProfileResponse creatorProfile = userService.getProfile(circle.getCreatorId(), circle.getCreatorId());
            if (creatorProfile == null) {
                continue;
            }

            UserVO creatorVO = creatorProfile.getUser();

            // 获取成员信息
            boolean isMember = false;
            Integer memberRole = null;

            if (currentUserId != null) {
                CircleMember memberInfo = circleMemberMapper.findByCircleAndUser(circle.getId(), currentUserId);
                if (memberInfo != null) {
                    isMember = true;
                    memberRole = memberInfo.getRole();
                }
            }

            // 获取成员数量
            int memberCount = circleMemberMapper.countByCircleId(circle.getId());

            // 获取文章数量
            int articleCount = articleMapper.countByCircleId(circle.getId());

            CircleListItemVO circleVO = new CircleListItemVO(
                    circle.getId(),
                    circle.getName(),
                    circle.getDescription(),
                    creatorVO,
                    circle.getCreateTime(),
                    memberCount,
                    articleCount,
                    isMember,
                    memberRole
            );

            result.add(circleVO);
        }

        return result;
    }

    // 辅助方法：转换圈子成员为 VO 列表
    private List<CircleMemberVO> convertToCircleMemberVOList(List<CircleMember> members) {
        if (members == null || members.isEmpty()) {
            return new ArrayList<>();
        }

        List<CircleMemberVO> result = new ArrayList<>();

        for (CircleMember member : members) {
            UserProfileResponse userProfile = userService.getProfile(member.getUserId(), member.getUserId());
            if (userProfile == null) {
                continue;
            }

            UserVO userVO = userProfile.getUser();

            CircleMemberVO memberVO = new CircleMemberVO(
                    member.getId(),
                    userVO,
                    member.getJoinTime(),
                    member.getRole()
            );

            result.add(memberVO);
        }

        return result;
    }
}
