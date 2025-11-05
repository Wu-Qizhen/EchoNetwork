package com.wqz.echonetwork.service;

import com.wqz.echonetwork.entity.dto.ArticleInteractionResponse;
import com.wqz.echonetwork.entity.dto.ArticleQueryRequest;
import com.wqz.echonetwork.entity.dto.ArticleUpdateRequest;
import com.wqz.echonetwork.entity.vo.ArticleVO;
import com.wqz.echonetwork.entity.vo.PageResult;

import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public interface ArticleService {

    ArticleVO create(ArticleUpdateRequest articleUpdateRequest, Long authorId);

    ArticleVO update(ArticleUpdateRequest articleUpdateRequest, Long authorId, Long articleId);

    String delete(Long articleId, Long authorId);

    ArticleVO getArticle(Long articleId);

    ArticleVO getArticle(Long articleId, Long userId);

    List<ArticleVO> getRecommend(Integer limit);

    PageResult<ArticleVO> getArticlesByConditions(ArticleQueryRequest queryRequest);

    List<ArticleVO> getArticlesByUserId(Long userId);

    List<ArticleVO> getArticlesByCircleId(Long circleId);

    List<ArticleVO> getArticlesByTagId(Long tagId);

    ArticleInteractionResponse likeArticle(Long articleId, Long userId);

    ArticleInteractionResponse unlikeArticle(Long articleId, Long userId);

    ArticleInteractionResponse starArticle(Long articleId, Long userId);

    ArticleInteractionResponse unstarArticle(Long articleId, Long userId);

    ArticleInteractionResponse getArticleInteractionStatus(Long articleId, Long userId);

    List<ArticleVO> getLikedArticles(Long userId);

    List<ArticleVO> getStarredArticles(Long userId);

    String increaseViewCount(Long articleId);
}
