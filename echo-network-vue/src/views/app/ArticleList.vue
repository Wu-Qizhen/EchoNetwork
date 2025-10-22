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
    <div v-else class="article-list">
      <!-- 空状态 -->
      <div v-if="!loading && articleList.length === 0" class="empty-state">
        <el-empty description="暂无文章"/>
      </div>

      <!-- 文章卡片 -->
      <div
          v-for="article in articleList"
          :key="article.id"
          class="article-card"
      >
        <el-card shadow="hover" class="dark-card">
          <!-- 文章头部 -->
          <div class="article-header">
            <div class="author-info">
              <el-avatar
                  :size="40"
                  :src="article.author.avatarUrl"
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
            <p class="article-summary">{{ article.summary }}</p>
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
                {{ article.viewCount }}
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
      <div v-if="loading && articleList.length > 0" class="loading-more">
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
        <el-divider>
          <span class="no-more-text">没有更多文章了</span>
        </el-divider>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, onUnmounted, ref} from 'vue'
import {ElMessage} from 'element-plus'
import {ChatDotRound, Star, View} from '@element-plus/icons-vue'

// 响应式数据
const articleList = ref([])
const loading = ref(false)
const initialLoading = ref(true)
const currentPage = ref(1)
const hasMore = ref(true)
const totalPages = ref(0)

// 请求参数
const requestParams = ref({
  page: 1,
  size: 20,
  authorId: null,
  circleId: null,
  tagId: null,
  status: 1,
  keyword: null
})

// 模拟 API 请求
const fetchArticles = async (isLoadMore = false) => {
  if (loading.value) return

  loading.value = true

  try {
    // 模拟 API 请求延迟
    await new Promise(resolve => setTimeout(resolve, 1000))

    // 模拟 API 响应数据
    const mockResponse = {
      code: 200,
      message: "成功",
      data: {
        list: Array.from({length: isLoadMore ? 5 : 10}, (_, index) => {
          const id = isLoadMore ? articleList.value.length + index + 1 : index + 1
          return {
            id,
            title: [
              '从零构建高并发服务：如何扛住百万级 QPS',
              'React 19 新特性实战：Server Components 如何重塑前端架构',
              '深入理解 Linux 内核调度器：为什么你的进程总是被抢占？',
              '用 LangChain 搭建企业级智能客服：从模型选型到上线部署',
              'Kubernetes 网络疑难杂症排查：从 CNI 到 Service 流量黑洞',
              '零信任架构在微服务中的落地实践',
              '前端性能优化实战：如何将首屏时间压缩到 800ms 以内',
              'Redis 缓存穿透与雪崩：不只是加锁那么简单',
              '用 eBPF 实现无侵入式服务监控',
              'TypeScript 高级类型实战：打造类型安全的 API 客户端']
                [Math.floor(Math.random() * 10)],
            summary:
                [
                  '在分布式系统设计中，真正的挑战不是功能实现，而是如何在流量洪峰中保持服务的稳定与低延迟……',
                  '随着 React Server Components 的正式落地，我们终于可以告别“前端过度加载”的时代……',
                  '你是否曾发现某个关键任务在高峰期莫名其妙变慢？问题可能出在 CFS 调度器的红黑树上',
                  '本文将带你一步步构建一个基于 LLM 的智能客服系统，并解决上下文过长与幻觉问题……',
                  '当你的 Pod 无法访问外部服务时，别急着重启，先看看 iptables 规则是否已被篡改……',
                  '传统的“内网即安全”思维正在失效，每一次内部调用都应被验证和加密……',
                  '通过代码分割、预加载和 SSR 三板斧，我们成功将用户跳出率降低了 40%……',
                  '缓存失效的瞬间，数据库可能已经承受了十倍于平时的请求压力……',
                  '不再依赖日志和埋点，eBPF 让你直接从内核层面观测系统行为……',
                  '利用 conditional types 和 mapped types，我们可以让编译器帮你检查接口契约……'
                ]
                    [Math.floor(Math.random() * 10)],
            createTime: new Date().toISOString(),
            publishTime: new Date(Date.now() - Math.random() * 7 * 24 * 60 * 60 * 1000).toISOString(),
            viewCount: Math.floor(Math.random() * 1000),
            likeCount: Math.floor(Math.random() * 100),
            starCount: Math.floor(Math.random() * 50),
            commentCount: Math.floor(Math.random() * 30),
            author: {
              id: 123,
              nickname: ['码上天下', '指针猎人', '栈溢出', '内核狂魔', '零号进程', '字节风暴', '死锁终结者', '汇编诗人', '内存征服者', '无锁人生']
                  [Math.floor(Math.random() * 10)],
              avatarUrl: `https://example.com/avatar${Math.floor(Math.random() * 5) + 1}.jpg`
            },
            circle: Math.random() > 0.3 ? {
              id: 789,
              name: ['技术圈', '设计圈', '产品圈'][Math.floor(Math.random() * 3)]
            } : null,
            tags: [
              {id: 1, name: 'Java'},
              {id: 2, name: 'Spring'}
            ].slice(0, Math.floor(Math.random() * 3) + 1)
          }
        }),
        total: isLoadMore ? 150 : 150,
        page: currentPage.value,
        size: 20,
        totalPages: 8
      },
      timestamp: new Date().toISOString()
    }

    if (mockResponse.code === 200) {
      if (isLoadMore) {
        articleList.value.push(...mockResponse.data.list)
      } else {
        articleList.value = mockResponse.data.list
      }

      totalPages.value = mockResponse.data.totalPages
      currentPage.value = mockResponse.data.page
      hasMore.value = currentPage.value < totalPages.value
    } else {
      ElMessage.error(`获取文章失败: ${mockResponse.message}`)
    }
  } catch (error) {
    ElMessage.error('网络错误，请稍后重试')
    console.error(' API 请求错误:', error)
  } finally {
    loading.value = false
  }
}

// 初始加载
const initLoad = async () => {
  // 强制显示3秒骨架屏
  const skeletonTimer = setTimeout(() => {
    initialLoading.value = false
    clearTimeout(skeletonTimer)
  }, 3000)

  // 同时发起数据请求
  await fetchArticles()
}

// 加载更多
const loadMore = () => {
  if (loading.value || !hasMore.value) return

  currentPage.value += 1
  requestParams.value.page = currentPage.value
  fetchArticles(true)
}

// 滚动监听
const handleScroll = () => {
  const scrollTop = document.documentElement.scrollTop || document.body.scrollTop
  const windowHeight = window.innerHeight
  const scrollHeight = document.documentElement.scrollHeight

  // 距离底部100px时触发加载更多
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
    return `${diffMins}分钟前`
  } else if (diffHours < 24) {
    return `${diffHours}小时前`
  } else if (diffDays < 7) {
    return `${diffDays}天前`
  } else {
    return date.toLocaleDateString()
  }
}

// 生命周期
onMounted(() => {
  initLoad()
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
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
  color: #e0e0e0;
  min-height: 60vh;
}

.dark-card {
  /* height: 200px; */
  background-color: rgba(45, 45, 45, 0.7);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  margin-bottom: 20px;
}

/* .dark-card :hover{
  border-color: white;
} */

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
  color: #aaa;
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
  color: #e0e0e0;
  line-height: 1.4;
}

.article-summary {
  color: #a8abb2;
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
  background-color: #3a3a3a;
  border-color: #555;
  color: #a8abb2;
}

.article-stats {
  display: flex;
  gap: 16px;
  font-size: 14px;
  color: #aaa;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 空状态和没有更多数据样式 */
.empty-state, .no-more {
  text-align: center;
  padding: 40px 0;
}

.no-more-text {
  color: #888;
  font-size: 14px;
}

/* Element Plus 组件深色模式样式覆盖 */
:deep(.el-skeleton) {
  --el-skeleton-color: #3a3a3a;
  --el-skeleton-to-color: #4a4a4a;
}

:deep(.el-tag) {
  --el-tag-bg-color: #3a3a3a;
  --el-tag-border-color: #555;
  --el-tag-text-color: #a8abb2;
}

:deep(.el-card) {
  --el-card-bg-color: #2d2d2d;
  --el-card-border-color: #444;
}

:deep(.el-divider__text) {
  background-color: #1a1a1a;
  color: #888;
  padding: 0 10px;
}
</style>
