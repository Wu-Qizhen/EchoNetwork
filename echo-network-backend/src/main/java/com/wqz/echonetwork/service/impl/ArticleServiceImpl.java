package com.wqz.echonetwork.service.impl;

import com.wqz.echonetwork.entity.dto.ArticleUpdateRequest;
import com.wqz.echonetwork.entity.dto.UserProfileResponse;
import com.wqz.echonetwork.entity.po.Article;
import com.wqz.echonetwork.entity.po.ArticleTag;
import com.wqz.echonetwork.entity.po.Circle;
import com.wqz.echonetwork.entity.po.Tag;
import com.wqz.echonetwork.entity.vo.ArticleVO;
import com.wqz.echonetwork.entity.vo.UserVO;
import com.wqz.echonetwork.mapper.ArticleMapper;
import com.wqz.echonetwork.mapper.CircleMapper;
import com.wqz.echonetwork.mapper.TagMapper;
import com.wqz.echonetwork.mapper.UserMapper;
import com.wqz.echonetwork.service.ArticleService;
import com.wqz.echonetwork.service.UserService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
    private final UserMapper userMapper = new UserMapper();
    private final UserService userService = new UserServiceImpl();

    @Override
    public ArticleVO create(ArticleUpdateRequest articleUpdateRequest, Long authorId) {
        if (articleUpdateRequest.getCircleId() != null) {
            Circle circle = circleMapper.findById(articleUpdateRequest.getCircleId());
            if (circle == null) {
                return null;
            }
        }

        List<String> tags = articleUpdateRequest.getTags();
        Set<Long> tagIds = tagMapper.insertBatch(tags);
        /* Set<Long> tagIds = new HashSet<>();
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
        } */

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

        // TODO 事务优化
        long insert = articleMapper.insert(article);

        if (insert > 0) {
            article.setId(insert);
            article.getTagIds().forEach(
                    tagId -> {
                        ArticleTag articleTag = new ArticleTag(
                                null,
                                article.getId(),
                                tagId,
                                LocalDateTime.now()
                        );
                        articleMapper.insertArticleTag(articleTag);
                    }
            );
            return getArticle(article.getId());
        }

        return null;
    }

    @Override
    public ArticleVO update(ArticleUpdateRequest articleUpdateRequest, Long authorId, Long articleId) {
        // LogUtil.info("ArticleUpdateRequest: " + articleUpdateRequest);
        Article article = articleMapper.findById(articleId);
        // LogUtil.info("Article: " + article);

        if (article == null || article.getId() == null || article.getStatus() == 2) {
            return null;
        }
        if (!Objects.equals(article.getAuthorId(), authorId)) {
            return null;
        }
        if (articleUpdateRequest.getCircleId() != null) {
            Circle circle = circleMapper.findById(articleUpdateRequest.getCircleId());
            if (circle == null) {
                return null;
            }
        }

        article.setTitle(articleUpdateRequest.getTitle());
        article.setContent(articleUpdateRequest.getContent());
        article.setCircleId(articleUpdateRequest.getCircleId());
        article.setUpdateTime(LocalDateTime.now());
        article.setStatus(articleUpdateRequest.getStatus());

        List<String> tags = articleUpdateRequest.getTags();
        // LogUtil.info("Tags: " + tags);
        Set<Long> tagIds = tagMapper.insertBatch(tags);
        // LogUtil.info("TagIds: " + tagIds);
        article.setTagIds(tagIds);

        int update = articleMapper.update(article);
        // LogUtil.info("Update: " + update);
        if (update > 0) {
            // 删除旧的标签绑定
            articleMapper.deleteArticleTagsByArticleId(articleId);

            // 为新标签创建绑定
            for (Long tagId : tagIds) {
                ArticleTag articleTag = new ArticleTag(
                        null,
                        articleId,
                        tagId,
                        LocalDateTime.now()
                );
                articleMapper.insertArticleTag(articleTag);
            }

            // 返回更新后的文章信息
            return getArticle(articleId);
        }
        return null;
    }

    @Override
    public String delete(Long articleId, Long authorId) {
        Article article = articleMapper.findById(articleId);
        if (article == null || article.getId() == null) {
            return "文章不存在";
        }
        if (!Objects.equals(article.getAuthorId(), authorId)) {
            return "无权删除文章";
        }
        // 删除相关的标签绑定 TODO 事务优化
        articleMapper.deleteArticleTagsByArticleId(articleId);
        int delete = articleMapper.delete(articleId);
        if (delete > 0) {
            return null;
        }
        return "删除失败";
    }

    @Override
    public ArticleVO getArticle(Long articleId) {
        Article article = articleMapper.findByIdPublished(articleId);
        if (article != null) {
            /* User user = userMapper.findById(article.getAuthorId());
            if (user == null || user.getId() == null) {
                return null;
            } */

            /* UserVO userVO = new UserVO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getNickname(),
                    user.getBio(),
                    user.getAvatarUrl(),
                    user.getRole(),
                    user.getFollowerCount(),
                    Objects.requireNonNull(user.getLastLoginTime())
            ); */

            UserProfileResponse profile = userService.getProfile(article.getAuthorId(), article.getAuthorId());
            if (profile == null) {
                return null;
            }
            UserVO userVO = profile.getUser();

            Circle circle = null;
            if (article.getCircleId() != null) {
                circle = circleMapper.findById(article.getCircleId());
                if (circle.getId() == null) {
                    circle = null;
                }
            }

            Set<Tag> tags = new HashSet<>();
            article.getTagIds();
            if (!article.getTagIds().isEmpty()) {
                for (Long tagId : article.getTagIds()) {
                    Tag tag = tagMapper.findById(tagId);
                    if (tag != null) {
                        tags.add(tag);
                    }
                }
            }

            return new ArticleVO(
                    articleId,
                    article.getTitle(),
                    article.getContent(),
                    Objects.requireNonNull(article.getCreateTime()),
                    Objects.requireNonNull(article.getUpdateTime()),
                    Objects.requireNonNull(article.getPublishTime()),
                    article.getStatus(),
                    article.getViewCount(),
                    article.getLikeCount(),
                    article.getStarCount(),
                    article.getCommentCount(),
                    userVO,
                    circle,
                    tags
            );
        }
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
