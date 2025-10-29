<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.13
-->
<template>
  <div class="blog-container dark-mode">
    <!-- 初始加载骨架屏 -->
    <div v-if="initialLoading" class="skeleton-container">
      <el-skeleton
          v-for="i in 3"
          :key="i"
          animated
          class="article-skeleton"
      >
        <template #template>
          <div class="skeleton-header">
            <el-skeleton-item variant="circle" class="avatar-skeleton"/>
            <div class="skeleton-header-text">
              <el-skeleton-item variant="h3" class="title-skeleton"/>
              <!--<el-skeleton-item variant="text" class="meta-skeleton"/>-->
            </div>
          </div>
          <el-skeleton-item variant="h3" class="title-skeleton"/>
          <el-skeleton-item variant="text" class="content-skeleton"/>
          <el-skeleton-item variant="text" class="content-skeleton short"/>
          <div class="skeleton-footer">
            <el-skeleton-item variant="text" class="tag-skeleton"/>
            <el-skeleton-item variant="text" class="stats-skeleton"/>
          </div>
        </template>
      </el-skeleton>
    </div>

    <!-- 文章列表 -->
    <div v-else>
      <!-- 空状态 -->
      <div v-if="!loading && articleList.length === 0" class="empty-state">
        <el-empty description="这里什么也没有"/>
      </div>

      <!-- 文章卡片 -->
      <div
          v-for="article in articleList"
          :key="article.id"
          class="article-card"
          @click="goToArticle(article.id)"
      >
        <el-card class="dark-card">
          <!-- 文章头部 -->
          <div class="article-header">
            <div class="author-info">
              <el-avatar
                  :size="40"
                  :src="article.author.avatarUrl || '/res/ic_avatar_default.svg'"
                  style="background-color: var(--dark-bg-l);"
                  class="author-avatar"
              />
              <div class="author-details">
                <div class="author-name">{{ article.author.nickname }}</div>
                <div class="publish-time">
                  {{ formatTime(article.publishTime || article.createTime) }}
                </div>
              </div>
            </div>
            <div class="circle-tag" v-if="article.circle">
              <el-tag type="info" size="small">{{ article.circle.name }}</el-tag>
            </div>
          </div>

          <!-- 文章内容 -->
          <div class="article-content">
            <h3 class="article-title">{{ article.title }}</h3>
            <p class="article-summary">{{ getArticleSummary(article.content) }}</p>
          </div>

          <!-- 文章底部 -->
          <div class="article-footer">
            <div class="article-tags">
              <el-tag
                  v-for="tag in article.tags"
                  :key="tag.id"
                  size="small"
                  class="tag-item"
              >
                {{ tag.name }}
              </el-tag>
            </div>
            <div class="article-stats">
              <span class="stat-item">
                <el-icon><View/></el-icon>
                {{ formatViewCount(article.viewCount) }}
              </span>
              <span class="stat-item">
                <el-icon><Star/></el-icon>
                {{ article.starCount }}
              </span>
              <span class="stat-item">
                <el-icon><ChatDotRound/></el-icon>
                {{ article.commentCount }}
              </span>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 加载更多骨架屏 -->
      <div v-if="enablePagination && loading && articleList.length > 0" class="loading-more">
        <el-skeleton
            v-for="i in 2"
            :key="`loading-${i}`"
            animated
            class="article-skeleton"
        >
          <template #template>
            <div class="skeleton-header">
              <el-skeleton-item variant="circle" class="avatar-skeleton"/>
              <div class="skeleton-header-text">
                <el-skeleton-item variant="h3" class="title-skeleton"/>
              </div>
            </div>
            <el-skeleton-item variant="h3" class="title-skeleton"/>
            <el-skeleton-item variant="text" class="content-skeleton"/>
            <el-skeleton-item variant="text" class="content-skeleton short"/>
            <div class="skeleton-footer">
              <el-skeleton-item variant="text" class="tag-skeleton"/>
              <el-skeleton-item variant="text" class="stats-skeleton"/>
            </div>
          </template>
        </el-skeleton>
      </div>

      <!-- 没有更多数据提示 -->
      <div v-if="!hasMore && articleList.length > 0" class="no-more">
        <XDivider :label="emptyText"></XDivider>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, onUnmounted, ref, watch} from 'vue'
import {ElMessage} from 'element-plus'
import {ChatDotRound, Star, View} from '@element-plus/icons-vue'
import {getArticles, getLikedArticles, getStarredArticles} from "@/net/request.js";
import XDivider from "@/aethex/components/XDivider.vue";
import {extractSummary} from "@/utils/htmlToText.js";

/*const props = defineProps({
  // 请求参数配置
  requestConfig: {
    type: Object,
    default: () => ({})
  },
  // 是否启用分页
  enablePagination: {
    type: Boolean,
    default: true
  },
  // 没有文章时显示的文字
  emptyText: {
    type: String,
    default: '暂无文章'
  }
})*/

const props = defineProps({
  // 请求参数配置
  requestConfig: {
    type: Object,
    default: () => ({})
  },
  // 是否启用分页
  enablePagination: {
    type: Boolean,
    default: true
  },
  // 没有文章时显示的文字
  emptyText: {
    type: String,
    default: '暂无文章'
  },
  // 文章类型：normal-普通文章，liked-点赞文章，starred-收藏文章
  articleType: {
    type: String,
    default: 'normal'
  }
})

// 响应式数据
const articleList = ref([])
const loading = ref(false)
const initialLoading = ref(true)
const currentPage = ref(1)
const hasMore = ref(true)
const totalPages = ref(0)

// 默认请求参数
const defaultParams = {
  page: 1,
  size: 5,
  status: 1 // 默认只获取已发布的文章
}

// 获取完整的请求参数
const getRequestParams = (page = currentPage.value) => {
  return {
    ...defaultParams,
    ...props.requestConfig,
    page: props.enablePagination ? page : 1,
    size: props.enablePagination ? (props.requestConfig.size || 10) : (props.requestConfig.limit || 10)
  }
}

// API 请求
/*const fetchArticles = async (isLoadMore = false) => {
  if (loading.value) return

  loading.value = true

  try {
    // 构建请求参数
    const params = getRequestParams(isLoadMore ? currentPage.value : 1)

    await getArticles(params, (data) => {
      if (isLoadMore) {
        articleList.value.push(...data.list)
      } else {
        articleList.value = data.list
      }

      // 如果启用分页，处理分页逻辑
      if (props.enablePagination) {
        totalPages.value = data.totalPages
        currentPage.value = data.page
        hasMore.value = currentPage.value < totalPages.value
      } else {
        // 不启用分页时，直接标记没有更多数据
        hasMore.value = false
      }
    })
  } catch (error) {
    ElMessage.error('获取文章失败，请稍后重试')
    console.error('获取文章失败：', error)
  } finally {
    loading.value = false
    // initialLoading.value = false 强制显示骨架屏
  }
}*/

// API 请求
const fetchArticles = async (isLoadMore = false) => {
  if (loading.value) return

  loading.value = true

  try {
    switch (props.articleType) {
      case 'liked':
        await getLikedArticles((data) => {
          articleList.value = data
          hasMore.value = false
          totalPages.value = 1
          currentPage.value = 1
        })
        loading.value = false
        initialLoading.value = false
        break
      case 'starred':
        await getStarredArticles((data) => {
          articleList.value = data
          hasMore.value = false  // 收藏列表通常不分页
          totalPages.value = 1   // 设置总页数
          currentPage.value = 1  // 重置当前页
        })
        loading.value = false  // 关键：手动设置加载完成
        initialLoading.value = false  // 确保初始加载也结束
        break
      default:
        const params = getRequestParams(isLoadMore ? currentPage.value : 1)

        await getArticles(params, (data) => {
          if (isLoadMore) {
            articleList.value.push(...data.list)
          } else {
            articleList.value = data.list
          }

          // 如果启用分页，处理分页逻辑
          if (props.enablePagination) {
            totalPages.value = data.totalPages
            currentPage.value = data.page
            hasMore.value = currentPage.value < totalPages.value
          } else {
            // 不启用分页时，直接标记没有更多数据
            hasMore.value = false
          }
        })
    }
  } catch (error) {
    ElMessage.error('获取文章失败，请稍后重试')
    console.error('获取文章失败：', error)
  } finally {
    loading.value = false
  }
}

// 监听请求配置变化，重新加载数据
/*watch(() => props.requestConfig, () => {
  currentPage.value = 1
  articleList.value = []
  initialLoading.value = true
  fetchArticles()
}, {deep: true})*/

// 监听请求配置和文章类型变化，重新加载数据
watch([() => props.requestConfig, () => props.articleType], () => {
  currentPage.value = 1
  articleList.value = []
  initialLoading.value = true
  fetchArticles()
}, {deep: true})

// 初始加载
const initLoad = async () => {
  // 强制显示 1 秒骨架屏
  const skeletonTimer = setTimeout(() => {
    initialLoading.value = false
    clearTimeout(skeletonTimer)
  }, 1000)

  // 同时发起数据请求
  await fetchArticles()
}

// 加载更多
const loadMore = async () => {
  if (!props.enablePagination || loading.value || !hasMore.value) return

  initialLoading.value = true

  // 强制显示加载骨架屏至少 1 秒
  const loadingTimer = setTimeout(() => {
    initialLoading.value = false
    clearTimeout(loadingTimer)
  }, 1000)

  currentPage.value += 1
  // requestParams.value.page = currentPage.value
  // 并行执行数据加载和定时器
  await Promise.all([fetchArticles(true), loadingTimer])
}

// 滚动监听
const handleScroll = () => {
  if (!props.enablePagination) return

  const scrollTop = document.documentElement.scrollTop || document.body.scrollTop
  const windowHeight = window.innerHeight
  const scrollHeight = document.documentElement.scrollHeight

  // 距离底部 100px 时触发加载更多
  if (scrollTop + windowHeight >= scrollHeight - 100) {
    loadMore()
  }
}

// 时间格式化
const formatTime = (timeString) => {
  const date = new Date(timeString)
  const now = new Date()
  const diffMs = now - date
  const diffMins = Math.floor(diffMs / 60000)
  const diffHours = Math.floor(diffMs / 3600000)
  const diffDays = Math.floor(diffMs / 86400000)

  if (diffMins < 60) {
    return `${diffMins} 分钟前`
  } else if (diffHours < 24) {
    return `${diffHours} 小时前`
  } else if (diffDays < 7) {
    return `${diffDays} 天前`
  } else {
    return date.toLocaleDateString()
  }
}

// 格式化浏览量
const formatViewCount = (count) => {
  if (count >= 10000) {
    return (count / 10000).toFixed(1) + 'W'
  }
  return count
}

const getArticleSummary = (content) => {
  return extractSummary(content, 50) // 限制 50 个字符
}

const goToArticle = (articleId) => {
  /*router.push({
    name: 'article',
    params: {id: articleId}
  });*/
  window.open(`/article/${articleId}`, '_blank')
}

onMounted(() => {
  initLoad()
  fetchArticles()
  if (props.enablePagination) {
    window.addEventListener('scroll', handleScroll)
  }
})

onUnmounted(() => {
  if (props.enablePagination) {
    window.removeEventListener('scroll', handleScroll)
  }
})
</script>

<style scoped>
.blog-container {
  width: 100%;
  margin: 0 auto;
  padding: 20px;
  min-height: 60vh;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
}

/* 深色模式样式 */
.dark-mode {
  background-color: rgba(30, 35, 47, 0.6);
  color: var(--dark-content-m);
  min-height: 60vh;
}

.dark-card {
  /* height: 200px; */
  background-color: rgba(45, 45, 45, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  margin-bottom: 20px;
  cursor: pointer;
  box-shadow: none;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  transform-origin: center;
}

.dark-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgb(255, 255, 255, 0.1);
}

.dark-card :deep(.el-card__body) {
  padding: 20px;
}

/* 骨架屏样式 */
.skeleton-container, .loading-more {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.article-skeleton {
  padding: 20px;
  background-color: rgba(45, 45, 45, 0.7);
  border-radius: 10px;
}

.skeleton-header {
  display: flex;
  flex: 1;
  align-items: center;
  margin-bottom: 16px;
}

.avatar-skeleton {
  width: 40px;
  height: 40px;
  margin-right: 12px;
}

.skeleton-header-text {
  flex: 1;
  justify-content: space-between;
  /* width: 100%; */
}

.title-skeleton {
  width: 20%;
  height: 24px;
  margin-bottom: 8px;
}

/* .meta-skeleton {
  width: 10%;
  height: 16px;
  margin-bottom: 16px;
} */

.content-skeleton {
  width: 100%;
  height: 18px;
  margin-bottom: 8px;
}

.content-skeleton.short {
  width: 80%;
}

.skeleton-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.tag-skeleton {
  width: 60px;
  height: 20px;
  margin-right: 8px;
}

.stats-skeleton {
  width: 100px;
  height: 20px;
}

/* 文章卡片样式 */
.article-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.author-info {
  display: flex;
  align-items: center;
}

.author-avatar {
  margin-right: 12px;
}

.author-details {
  display: flex;
  flex-direction: column;
}

.author-name {
  color: white;
  font-weight: 600;
  margin-bottom: 4px;
}

.publish-time {
  font-size: 12px;
  color: var(--dark-content-m);
}

.circle-tag {
  margin-left: 10px;
}

.article-content {
  margin-bottom: 16px;
}

.article-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 12px 0;
  color: #fff;
  line-height: 1.4;

  /* 确保标题单行显示 */
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-summary {
  color: var(--dark-content-m);
  line-height: 1.6;
  margin: 0;
  font-size: 14px;
}

.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
}

.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-item {
  background-color: var(--dark-bg-l);
  border-color: var(--dark-line-m);
  color: var(--dark-content-m);
}

.article-stats {
  display: flex;
  gap: 16px;
  font-size: 14px;
  color: var(--dark-content-m);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

/* 空状态和没有更多数据样式 */
.empty-state, .no-more {
  text-align: center;
  padding: 10px 0;
}

/* Element Plus 组件深色模式样式覆盖 */
:deep(.el-skeleton) {
  --el-skeleton-color: var(--dark-bg-l);
  --el-skeleton-to-color: var(--dark-bg-xl);
}

:deep(.el-tag) {
  --el-tag-bg-color: var(--dark-bg-l);
  --el-tag-border-color: var(--dark-line-m);
  --el-tag-text-color: var(--dark-content-m);
}

:deep(.el-card) {
  --el-card-bg-color: var(--dark-bg-s);
  --el-card-border-color: var(--dark-line-s);
}
</style>
