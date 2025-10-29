<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.27
-->
<template>
  <div class="article-display-container dark-mode">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton animated>
        <template #template>
          <div class="skeleton-header">
            <el-skeleton-item variant="circle" class="avatar-skeleton"/>
            <div class="skeleton-header-text">
              <el-skeleton-item variant="h3" class="title-skeleton-s"/>
              <el-skeleton-item variant="text" class="meta-skeleton"/>
            </div>
          </div>
          <el-skeleton-item variant="h3" class="title-skeleton"/>
          <el-skeleton-item variant="text" class="content-skeleton"/>
          <el-skeleton-item variant="text" class="content-skeleton"/>
          <el-skeleton-item variant="text" class="content-skeleton short"/>
          <div class="skeleton-footer">
            <el-skeleton-item variant="text" class="action-skeleton"/>
          </div>
        </template>
      </el-skeleton>
    </div>

    <!-- 文章详情 -->
    <div v-else-if="article" class="article-detail">
      <!-- 文章头部信息 -->
      <div class="article-header">
        <div class="author-section">
          <el-avatar
              :size="48"
              :src="article.author.avatarUrl || '/res/ic_avatar_default.svg'"
              style="background-color: var(--dark-bg-l);"
              class="author-avatar"
              @click="handleAuthorClick"
          />
          <div class="author-info">
            <div class="author-name" @click="handleAuthorClick">{{ article.author.nickname }}</div>
            <div class="article-meta">
              <span class="publish-time">{{ formatTime(article.publishTime || article.createTime) }}</span>
              <span class="word-count">{{ calculateWordCount(article.content) }} 字 </span>
              <span class="read-count">{{ formatViewCount(article.viewCount) }} 浏览</span>
            </div>
          </div>
        </div>

        <!-- 圈子标签 -->
        <div class="circle-tag" v-if="article.circle">
          <el-tag type="info" size="large">{{ article.circle.name }}</el-tag>
        </div>
      </div>

      <!-- 文章标题 -->
      <h1 class="article-title">{{ article.title }}</h1>

      <!-- 文章标签 -->
      <div class="article-tags" v-if="article.tags && article.tags.length > 0">
        <el-tag
            v-for="tag in article.tags"
            :key="tag.id"
            size="small"
            class="tag-item"
        >
          {{ tag.name }}
        </el-tag>
      </div>

      <!-- 文章内容 -->
      <div class="article-content" v-html="article.content"></div>

      <!-- 文章操作区域 -->
      <div class="article-actions">
        <div class="action-group">
          <el-button
              :type="article.liked ? 'primary' : 'default'"
              circle
              @click="handleLike"
              class="action-btn"
          >
            <template #default>
              <div class="action-btn-inner">
                <el-icon>
                  <Coin/>
                </el-icon>
                <div class="action-count">{{ article.likeCount || 0 }}</div>
              </div>
            </template>
          </el-button>
          <span class="action-label">点赞</span>
        </div>

        <div class="action-group">
          <el-button
              :type="article.starred ? 'warning' : 'default'"
              circle
              @click="handleCollect"
              class="action-btn"
          >
            <template #default>
              <div class="action-btn-inner">
                <el-icon>
                  <Star/>
                </el-icon>
                <div class="action-count">{{ article.starCount || 0 }}</div>
              </div>
            </template>
          </el-button>
          <span class="action-label">收藏</span>
        </div>
      </div>
    </div>

    <!-- 文章不存在 -->
    <div v-else class="article-not-found">
      <el-empty description="文章不存在或已被删除">
        <div class="btn theme" @click="router.back()"><p class="zh">返回</p></div>
      </el-empty>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, watch} from 'vue'
import {useRoute} from 'vue-router'
import {ElMessage} from 'element-plus'
import {Star, Coin} from '@element-plus/icons-vue'
import router from "@/router/index.js";
import {getArticle, likeArticle, starArticle, unlikeArticle} from "@/net/request.js";

/*const props = defineProps({
  articleId: {
    type: String,
    required: true
  }
})*/

// 模拟文章数据获取函数
/*const mockGetArticleDetail = (id) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      // 模拟文章数据
      const mockArticle = {
        id: parseInt(id),
        title: '深入理解Vue 3响应式原理',
        content: `
          <h2>什么是响应式系统</h2>
          <p>Vue 3 的响应式系统是其核心特性之一，它能够自动追踪数据变化并更新相关的视图。</p>
          <p>在 Vue 3 中，响应式系统经过了重写，使用 Proxy 替代了 Object.defineProperty，带来了更好的性能和更强大的功能。</p>

          <h3>Proxy 的优势</h3>
          <ul>
            <li>可以直接监听对象而非属性</li>
            <li>可以直接监听数组的变化</li>
            <li>具有多达13种拦截方法</li>
            <li>返回一个新对象，只操作新对象达到目的</li>
          </ul>

          <pre><code>const reactiveHandler = {
  get(target, key, receiver) {
    const result = Reflect.get(target, key, receiver)
    track(target, key)
    return result
  },
  set(target, key, value, receiver) {
    const oldValue = target[key]
    const result = Reflect.set(target, key, value, receiver)
    if (result && oldValue !== value) {
      trigger(target, key)
    }
    return result
  }
}</code></pre>

          <p>通过 Proxy，Vue 3 能够更精确地追踪数据变化，并提供更优秀的性能表现。</p>
        `,
        author: {
          id: 1,
          nickname: '前端开发者',
          avatarUrl: '/res/ic_avatar_default.svg'
        },
        publishTime: '2025-10-10T10:00:00Z',
        createTime: '2025-10-10T10:00:00Z',
        viewCount: 1520,
        starCount: 45,
        collectCount: 12,
        tags: [
          {id: 1, name: 'Vue.js'},
          {id: 2, name: '前端'},
          {id: 3, name: '响应式'}
        ],
        circle: {
          id: 1,
          name: '前端技术圈'
        }
      }
      resolve(mockArticle)
    }, 800)
  })
}*/

// 响应式数据
const article = ref(null)
const loading = ref(true)
/*const liked = ref(false)
const isCollected = ref(false)*/

const route = useRoute()

// 获取文章详情
/*const fetchArticleDetail = async () => {
  const articleId = route.params.id
  if (!articleId) {
    loading.value = false
    return
  }

  try {
    loading.value = true
    await getArticle(articleId, (data) => {
      article.value = data
    })
  } catch (error) {
    ElMessage.error('获取文章详情失败')
    console.error('获取文章详情失败：', error)
  } finally {
    loading.value = false
  }
}*/

// 修改 fetchArticleDetail 函数
/*const fetchArticleDetail = async () => {
  const articleId = route.params.id
  if (!articleId) {
    loading.value = false
    return
  }

  try {
    loading.value = true
    // 使用 Promise 包装 getArticle 调用
    await new Promise((resolve, reject) => {
      getArticle(articleId, (data) => {
        article.value = data
        resolve(data)
      }, (error) => {
        reject(error)
      })
    })
  } catch (error) {
    ElMessage.error('获取文章详情失败')
    console.error('获取文章详情失败：', error)
  } finally {
    loading.value = false
  }
}*/

const fetchArticleDetail = async () => {
  const articleId = route.params.id
  if (!articleId) {
    loading.value = false
    return
  }

  try {
    loading.value = true
    // 创建一个 1 秒的延迟 Promise
    const delayPromise = new Promise(resolve => setTimeout(resolve, 1000))

    // 包装 getArticle 调用为 Promise
    const fetchPromise = new Promise((resolve, reject) => {
      getArticle(articleId, (data) => {
        article.value = data
        resolve(data)
      }, (error) => {
        reject(error)
      })
    })

    // 等待数据获取和 1 秒延迟同时完成
    await Promise.all([fetchPromise, delayPromise])
  } catch (error) {
    ElMessage.error('获取文章详情失败')
    console.error('获取文章详情失败：', error)

    // 即使出错也等待 1 秒
    await new Promise(resolve => setTimeout(resolve, 1000))
  } finally {
    loading.value = false
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

// 计算文章字数
const calculateWordCount = (content) => {
  if (!content) return 0
  // 移除 HTML 标签后计算纯文本字数
  const text = content.replace(/<[^>]*>/g, '')
  return text.length
}

// 处理作者点击
const handleAuthorClick = () => {
  window.open(
      '/user/' + article.value.author.id,
  )
}

// 处理点赞
const handleLike = () => {
  article.value.liked = !article.value.liked
  if (article.value.liked) {
    likeArticle(article.value.id, () => {
      article.value.likeCount = (article.value.likeCount || 0) + 1
      ElMessage.success('点赞成功')
    })
  } else {
    unlikeArticle(article.value.id, () => {
      article.value.likeCount = (article.value.likeCount || 0) - 1
      ElMessage.error('已取消点赞')
    })
  }
}

// 处理收藏
const handleCollect = () => {
  article.value.starred = !article.value.starred
  if (article.value.starred) {
    starArticle(article.value.id, () => {
          article.value.starCount = (article.value.starCount || 0) + 1
          ElMessage.success('收藏成功')
        }
    )
  } else {
    unlikeArticle(article.value.id, () => {
      article.value.starCount = (article.value.starCount || 0) - 1
      ElMessage.error('已取消收藏')
    })
  }
}

// 监听路由变化
watch(() => route.params.id, () => {
  fetchArticleDetail()
})

onMounted(() => {
  fetchArticleDetail()
})
</script>

<style scoped>
.article-display-container {
  width: 100%;
  margin: 0 auto;
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  min-height: 60vh;
}

/* 深色模式样式 */
.dark-mode {
  background-color: rgba(30, 35, 47, 0.6);
  color: var(--dark-content-m);
}

/* .dark-card {
  background-color: transparent;
  border: none;
  border-radius: 20px;
  padding: 30px;
  width: 100%;
  margin: 0 auto;
} */

/* .dark-card :deep(.el-card__body) {
  padding: 0;
} */

/* 加载骨架屏样式 */
.loading-container {
  width: 100%;
}

/* .article-skeleton {
  padding: 0;
  background-color: transparent;
  border-radius: 20px;
} */

.skeleton-header {
  display: flex;
  align-items: start;
  margin-bottom: 24px;
}

.avatar-skeleton {
  width: 48px;
  height: 48px;
  margin-right: 16px;
}

.skeleton-header-text {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.title-skeleton-s {
  width: 20%;
  height: 24px;
  margin-bottom: 8px;
}

.title-skeleton {
  width: 60%;
  height: 48px;
  margin-bottom: 16px;
}

.meta-skeleton {
  width: 40%;
  height: 16px;
  margin-bottom: 16px;
}

.content-skeleton {
  width: 100%;
  height: 20px;
  margin-bottom: 12px;
}

.content-skeleton.short {
  width: 80%;
}

.skeleton-footer {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

.action-skeleton {
  width: 150px;
  height: 50px;
  border-radius: 25px;
  margin: 0 12px;
}

/* 文章头部样式 */
.article-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.author-section {
  display: flex;
  align-items: flex-start;
}

.author-avatar {
  margin-right: 16px;
  cursor: pointer;
  transition: transform 0.2s;
}

.author-avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 10px 20px rgb(255, 255, 255, 0.1);
}

.author-info {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-size: 16px;
  font-weight: 600;
  color: white;
  margin-bottom: 4px;
  cursor: pointer;
  transition: all 0.2s;
}

.author-name:hover {
  color: var(--dark-content-m);
  transform: translateY(-2px);
}

.article-meta {
  font-size: 14px;
  color: var(--dark-content-m);
}

.article-meta span {
  margin-right: 16px;
}

.circle-tag {
  margin-left: 16px;
}

/* 文章标题样式 */
.article-title {
  font-size: 28px;
  font-weight: 600;
  color: white;
  line-height: 1.4;
  margin: 0 0 12px 0;
  word-break: break-word;
}

/* 文章标签样式 */
.article-tags {
  margin-bottom: 24px;
}

.tag-item {
  background-color: var(--dark-bg-l);
  border-color: var(--dark-line-m);
  color: var(--dark-content-m);
  margin-right: 5px;
  margin-bottom: 8px;
}

/* 文章内容样式 */
.article-content {
  line-height: 1.6;
  font-size: 16px;
  margin-bottom: 32px;
  word-break: break-word;
}

/* 文章内容富文本样式 */
:deep(.article-content) h1,
:deep(.article-content) h2,
:deep(.article-content) h3,
:deep(.article-content) h4,
:deep(.article-content) h5,
:deep(.article-content) h6 {
  color: white;
  margin-top: 24px;
  margin-bottom: 16px;
  font-weight: 600;
}

:deep(.article-content) h1 {
  font-size: 24px;
  border-bottom: 1px solid var(--dark-line-m);
  padding-bottom: 8px;
}

:deep(.article-content) h2 {
  font-size: 20px;
}

:deep(.article-content) h3 {
  font-size: 18px;
}

:deep(.article-content) p {
  margin-bottom: 16px;
  color: var(--dark-content-l);
}

:deep(.article-content) ul,
:deep(.article-content) ol {
  margin-bottom: 16px;
  padding-left: 24px;
}

:deep(.article-content) li {
  margin-bottom: 8px;
  color: var(--dark-content-l);
}

:deep(.article-content) blockquote {
  border-left: 4px solid var(--theme-color-lighten);
  background-color: rgba(64, 158, 255, 0.1);
  padding: 12px 16px;
  margin: 16px 0;
  border-radius: 0 4px 4px 0;
}

:deep(.article-content) pre {
  background-color: var(--dark-bg-s);
  border: 1px solid var(--dark-line-m);
  border-radius: 6px;
  padding: 16px;
  overflow-x: auto;
  margin: 16px 0;
}

:deep(.article-content) code {
  background-color: var(--dark-bg-s);
  padding: 2px 6px;
  border-radius: 3px;
  font-family: 'Courier New', monospace;
  font-size: 14px;
}

:deep(.article-content) pre code {
  background: none;
  padding: 0;
}

:deep(.article-content) a {
  color: var(--theme-color-lighten);
  text-decoration: none;
}

:deep(.article-content) a:hover {
  text-decoration: underline;
}

:deep(.article-content) img {
  max-width: 100%;
  height: auto;
  border-radius: 6px;
  margin: 16px 0;
}

/* 文章操作区域样式 */
.article-actions {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.action-group {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: 0 20px;
}

.action-btn {
  width: 64px;
  height: 64px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  margin-bottom: 10px;
}

.action-btn-inner {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.action-count {
  font-size: 12px;
  text-align: center;
  margin-top: 5px;
}

.action-label {
  font-size: 14px;
  color: var(--dark-content-m);
}

/* 文章不存在样式 */
.article-not-found {
  text-align: center;
  padding: 60px 20px;
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

:deep(.el-button) {
  transition: all 0.3s ease;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .article-display-container {
    padding: 16px;
  }

  .dark-card {
    padding: 16px;
  }

  .article-title {
    font-size: 24px;
  }

  .article-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .circle-tag {
    margin-left: 0;
    margin-top: 12px;
  }

  .article-actions {
    flex-direction: column;
    gap: 16px;
  }

  .action-group {
    margin: 0;
  }
}
</style>
