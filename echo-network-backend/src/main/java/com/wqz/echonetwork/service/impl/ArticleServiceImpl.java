package com.wqz.echonetwork.service.impl;

import com.wqz.echonetwork.entity.dto.ArticleUpdateRequest;
import com.wqz.echonetwork.entity.po.Article;
import com.wqz.echonetwork.entity.po.Circle;
import com.wqz.echonetwork.entity.po.Tag;
import com.wqz.echonetwork.mapper.ArticleMapper;
import com.wqz.echonetwork.mapper.CircleMapper;
import com.wqz.echonetwork.mapper.TagMapper;
import com.wqz.echonetwork.service.ArticleService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.23
 */
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper = new ArticleMapper();
    private final CircleMapper circleMapper = new CircleMapper();
    private final TagMapper tagMapper = new TagMapper();

    @Override
    public Article create(ArticleUpdateRequest articleUpdateRequest, Long authorId) {
        if (articleUpdateRequest.getCircleId() != null) {
            Circle circle = circleMapper.findById(articleUpdateRequest.getCircleId());
            if (circle == null) {
                return null;
            }
        }

        List<String> tags = articleUpdateRequest.getTags();
        Set<Long> tagIds = new HashSet<>();
        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                Tag byName = tagMapper.findByName(tag);
                if (byName != null && byName.getId() != null) {
                    tagIds.add(byName.getId());
                } else {
                    long insert = tagMapper.insert(new Tag(null, tag));
                    tagIds.add(insert);
                }
            }
        }

        Article article = new Article(
                null,
                articleUpdateRequest.getTitle(),
                articleUpdateRequest.getContent(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                1,
                0,
                0,
                0,
                0,
                authorId,
                articleUpdateRequest.getCircleId(),
                tagIds
        );

        long insert = articleMapper.insert(article);

        if (insert > 0) {
            article.setId(insert);
            return article;
        }

        return null;
    }

    @Override
    public Article update(ArticleUpdateRequest articleUpdateRequest) {
        return null;
    }

    @Override
    public String delete(Long articleId) {
        return null;
    }

    @Override
    public Article getArticle(Long articleId) {
        return null;
    }

    @Override
    public List<Article> getRecommend(Integer limit) {
        return null;
    }

    @Override
    public List<Article> getArticlesByUserId(Long userId) {
        return null;
    }

    @Override
    public List<Article> getArticlesByCircleId(Long circleId) {
        return null;
    }

    @Override
    public List<Article> getArticlesByTagId(Long tagId) {
        return null;
    }
}
