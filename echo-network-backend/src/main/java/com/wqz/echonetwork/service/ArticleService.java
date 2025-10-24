package com.wqz.echonetwork.service;

import com.wqz.echonetwork.entity.dto.ArticleUpdateRequest;
import com.wqz.echonetwork.entity.po.Article;
import com.wqz.echonetwork.entity.vo.ArticleVO;

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

    List<Article> getRecommend(Integer limit);

    List<Article> getArticlesByUserId(Long userId);

    List<Article> getArticlesByCircleId(Long circleId);

    List<Article> getArticlesByTagId(Long tagId);
}
