package com.wqz.echonetwork.service;

import com.wqz.echonetwork.entity.dto.CircleCreateRequest;
import com.wqz.echonetwork.entity.dto.CircleJoinResponse;
import com.wqz.echonetwork.entity.vo.CircleListItemVO;
import com.wqz.echonetwork.entity.vo.CircleMemberVO;
import com.wqz.echonetwork.entity.vo.CircleVO;
import com.wqz.echonetwork.entity.vo.PageResult;

import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.25
 */
public interface CircleService {

    CircleVO createCircle(CircleCreateRequest request, Long creatorId);

    PageResult<CircleListItemVO> getCircles(int page, int size, String keyword, Long currentUserId);

    CircleVO getCircleDetail(Long circleId, Long currentUserId);

    CircleJoinResponse joinCircle(Long circleId, Long userId);

    boolean exitCircle(Long circleId, Long userId);

    PageResult<CircleMemberVO> getCircleMembers(Long circleId, int page, int size, Long currentUserId);

    boolean isMember(Long circleId, Long userId);

    List<CircleListItemVO> getUserCircles(Long userId);
}
