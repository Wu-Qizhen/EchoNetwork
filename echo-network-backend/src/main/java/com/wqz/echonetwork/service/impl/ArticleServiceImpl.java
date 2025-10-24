package com.wqz.echonetwork.service.impl;

import com.wqz.echonetwork.entity.dto.ArticleInteractionResponse;
import com.wqz.echonetwork.entity.dto.ArticleQueryRequest;
import com.wqz.echonetwork.entity.dto.ArticleUpdateRequest;
import com.wqz.echonetwork.entity.dto.UserProfileResponse;
import com.wqz.echonetwork.entity.po.*;
import com.wqz.echonetwork.entity.vo.ArticleVO;
import com.wqz.echonetwork.entity.vo.PageResult;
import com.wqz.echonetwork.entity.vo.UserVO;
import com.wqz.echonetwork.mapper.*;
import com.wqz.echonetwork.service.ArticleService;
import com.wqz.echonetwork.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.23
 */
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper = new ArticleMapper();
    private final CircleMapper circleMapper = new CircleMapper();
    private final TagMapper tagMapper = new TagMapper();
    // private final UserMapper userMapper = new UserMapper();
    private final UserService userService = new UserServiceImpl();
    private final ArticleLikeMapper articleLikeMapper = new ArticleLikeMapper();
    private final ArticleStarMapper articleStarMapper = new ArticleStarMapper();

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
    public List<ArticleVO> getRecommend(Integer limit) {
        List<Article> articles = articleMapper.findRecommend(limit != null ? limit : 5);
        return convertToArticleVOList(articles);
    }

    @Override
    public PageResult<ArticleVO> getArticlesByConditions(ArticleQueryRequest queryRequest) {
        // LogUtil.info("QueryRequest: " + queryRequest);

        // 构建查询条件
        Map<String, Object> conditions = buildQueryConditions(queryRequest);
        // LogUtil.info("Conditions: " + conditions);

        // 计算分页
        // int offset = (queryRequest.getPage() - 1) * queryRequest.getSize();
        // int limit = queryRequest.getSize();
        int page = Optional.ofNullable(queryRequest.getPage()).orElse(1);
        int limit = Optional.ofNullable(queryRequest.getSize()).orElse(10);
        int offset = (page - 1) * limit;
        // LogUtil.info("Offset: " + offset);
        // LogUtil.info("Limit: " + limit);

        // 查询数据
        List<Article> articles = articleMapper.findByConditions(conditions, offset, limit);
        // LogUtil.info("Articles: " + articles);
        int total = articleMapper.countByConditions(conditions);
        // LogUtil.info("Total: " + total);

        // 转换为 VO
        List<ArticleVO> articleVOs = convertToArticleVOList(articles);
        // LogUtil.info("ArticleVOs: " + articleVOs);

        // 返回分页结果
        return new PageResult<>(
                articleVOs,
                total,
                queryRequest.getPage(),
                queryRequest.getSize()
        );
    }

    @Override
    public List<ArticleVO> getArticlesByUserId(Long userId) {
        List<Article> articles = articleMapper.findByAuthorIdPublished(userId);
        return convertToArticleVOList(articles);
    }

    @Override
    public List<ArticleVO> getArticlesByCircleId(Long circleId) {
        List<Article> articles = articleMapper.findByCircleId(circleId);
        return convertToArticleVOList(articles);
    }

    @Override
    public List<ArticleVO> getArticlesByTagId(Long tagId) {
        List<Article> articles = articleMapper.findByTagId(tagId);
        return convertToArticleVOList(articles);
    }

    // 辅助方法：构建查询条件
    private Map<String, Object> buildQueryConditions(ArticleQueryRequest queryRequest) {
        Map<String, Object> conditions = new HashMap<>();

        if (queryRequest.getAuthorId() != null) {
            conditions.put("authorId", queryRequest.getAuthorId());
        }
        if (queryRequest.getCircleId() != null) {
            conditions.put("circleId", queryRequest.getCircleId());
        }
        if (queryRequest.getTagId() != null) {
            conditions.put("tagId", queryRequest.getTagId());
        }
        if (queryRequest.getStatus() != null) {
            conditions.put("status", queryRequest.getStatus());
        }
        if (queryRequest.getKeyword() != null && !queryRequest.getKeyword().trim().isEmpty()) {
            conditions.put("keyword", "%" + queryRequest.getKeyword().trim() + "%");
        }
        if (queryRequest.getSortBy() != null) {
            conditions.put("sortBy", queryRequest.getSortBy());
        }
        if (queryRequest.getSortOrder() != null) {
            conditions.put("sortOrder", queryRequest.getSortOrder());
        }

        return conditions;
    }

    // 辅助方法：批量转换 Article 为 ArticleVO
    private List<ArticleVO> convertToArticleVOList(List<Article> articles) {
        if (articles == null || articles.isEmpty()) {
            return new ArrayList<>();
        }

        // 批量获取用户信息，避免 N+1 查询
        Set<Long> authorIds = articles.stream()
                .map(Article::getAuthorId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, UserProfileResponse> userProfiles = new HashMap<>();
        for (Long authorId : authorIds) {
            UserProfileResponse profile = userService.getProfile(authorId, authorId);
            if (profile != null) {
                userProfiles.put(authorId, profile);
            }
        }

        // 批量获取圈子信息
        Set<Long> circleIds = articles.stream()
                .map(Article::getCircleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, Circle> circles = new HashMap<>();
        for (Long circleId : circleIds) {
            Circle circle = circleMapper.findById(circleId);
            if (circle != null && circle.getId() != null) {
                circles.put(circleId, circle);
            }
        }

        // 批量获取标签信息
        Set<Long> allTagIds = articles.stream()
                .flatMap(article -> article.getTagIds().stream())
                .collect(Collectors.toSet());

        Map<Long, Tag> tags = new HashMap<>();
        for (Long tagId : allTagIds) {
            Tag tag = tagMapper.findById(tagId);
            if (tag != null) {
                tags.put(tagId, tag);
            }
        }

        // 构建 VO 列表
        return articles.stream().map(article -> {
            UserProfileResponse profile = userProfiles.get(article.getAuthorId());
            if (profile == null) {
                return null;
            }

            UserVO userVO = profile.getUser();
            Circle circle = circles.get(article.getCircleId());

            Set<Tag> articleTags = article.getTagIds().stream()
                    .map(tags::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (article.getId() == null) {
                return null;
            }

            return new ArticleVO(
                    article.getId(),
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
                    articleTags
            );
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public ArticleInteractionResponse likeArticle(Long articleId, Long userId) {
        // 检查文章是否存在
        Article article = articleMapper.findById(articleId);
        if (article == null || article.getStatus() != 1) {
            return null;
        }

        // 检查是否已经点赞
        boolean alreadyLiked = articleLikeMapper.exists(userId, articleId);
        if (alreadyLiked) {
            // 已经点赞，直接返回当前状态
            return getArticleInteractionStatus(articleId, userId);
        }

        // 创建点赞记录
        ArticleLike articleLike = new ArticleLike();
        articleLike.setUserId(userId);
        articleLike.setArticleId(articleId);

        long likeId = articleLikeMapper.insert(articleLike);
        if (likeId > 0) {
            // 更新文章的点赞计数
            articleMapper.updateLikeCount(articleId, 1);

            // 返回更新后的状态
            return getArticleInteractionStatus(articleId, userId);
        }

        return null;
    }

    @Override
    public ArticleInteractionResponse unlikeArticle(Long articleId, Long userId) {
        // 检查是否已经点赞
        boolean alreadyLiked = articleLikeMapper.exists(userId, articleId);
        if (!alreadyLiked) {
            // 没有点赞，直接返回当前状态
            return getArticleInteractionStatus(articleId, userId);
        }

        // 删除点赞记录
        int deleted = articleLikeMapper.delete(userId, articleId);
        if (deleted > 0) {
            // 更新文章的点赞计数
            articleMapper.updateLikeCount(articleId, -1);

            // 返回更新后的状态
            return getArticleInteractionStatus(articleId, userId);
        }

        return null;
    }

    @Override
    public ArticleInteractionResponse starArticle(Long articleId, Long userId) {
        // 检查文章是否存在
        Article article = articleMapper.findById(articleId);
        if (article == null || article.getStatus() != 1) {
            return null;
        }

        // 检查是否已经收藏
        boolean alreadyStarred = articleStarMapper.exists(userId, articleId);
        if (alreadyStarred) {
            // 已经收藏，直接返回当前状态
            return getArticleInteractionStatus(articleId, userId);
        }

        // 创建收藏记录
        ArticleStar articleStar = new ArticleStar();
        articleStar.setUserId(userId);
        articleStar.setArticleId(articleId);

        long starId = articleStarMapper.insert(articleStar);
        if (starId > 0) {
            // 更新文章的收藏计数
            articleMapper.updateStarCount(articleId, 1);

            // 返回更新后的状态
            return getArticleInteractionStatus(articleId, userId);
        }

        return null;
    }

    @Override
    public ArticleInteractionResponse unstarArticle(Long articleId, Long userId) {
        // 检查是否已经收藏
        boolean alreadyStarred = articleStarMapper.exists(userId, articleId);
        if (!alreadyStarred) {
            // 没有收藏，直接返回当前状态
            return getArticleInteractionStatus(articleId, userId);
        }

        // 删除收藏记录
        int deleted = articleStarMapper.delete(userId, articleId);
        if (deleted > 0) {
            // 更新文章的收藏计数
            articleMapper.updateStarCount(articleId, -1);

            // 返回更新后的状态
            return getArticleInteractionStatus(articleId, userId);
        }

        return null;
    }

    @Override
    public ArticleInteractionResponse getArticleInteractionStatus(Long articleId, Long userId) {
        Article article = articleMapper.findById(articleId);
        if (article == null) {
            return null;
        }

        boolean isLiked = articleLikeMapper.exists(userId, articleId);
        boolean isStarred = articleStarMapper.exists(userId, articleId);

        return new ArticleInteractionResponse(
                articleId,
                article.getLikeCount(),
                article.getStarCount(),
                isLiked,
                isStarred
        );
    }

    @Override
    public List<ArticleVO> getLikedArticles(Long userId) {
        List<ArticleLike> likes = articleLikeMapper.findByUserId(userId);
        List<Long> articleIds = likes.stream()
                .map(ArticleLike::getArticleId)
                .collect(Collectors.toList());

        if (articleIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 批量获取文章详情
        List<Article> articles = articleMapper.findByIds(articleIds);
        return convertToArticleVOList(articles);
    }

    @Override
    public List<ArticleVO> getStarredArticles(Long userId) {
        List<ArticleStar> stars = articleStarMapper.findByUserId(userId);
        List<Long> articleIds = stars.stream()
                .map(ArticleStar::getArticleId)
                .collect(Collectors.toList());

        if (articleIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 批量获取文章详情
        List<Article> articles = articleMapper.findByIds(articleIds);
        return convertToArticleVOList(articles);
    }
}
