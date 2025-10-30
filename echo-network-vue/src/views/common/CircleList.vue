<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.29
-->
<template>
  <div class="circle-list-container dark-mode">
    <!-- 初始加载骨架屏 -->
    <div v-if="initialLoading" class="skeleton-container">
      <el-skeleton
          v-for="i in 3"
          :key="i"
          animated
          class="circle-skeleton"
      >
        <template #template>
          <div class="skeleton-header">
            <div class="skeleton-header-text">
              <el-skeleton-item variant="h3" class="title-skeleton"/>
              <el-skeleton-item variant="text" class="meta-skeleton"/>
            </div>
          </div>
          <el-skeleton-item variant="text" class="description-skeleton"/>
          <div class="skeleton-footer">
            <el-skeleton-item variant="text" class="stat-skeleton"/>
            <el-skeleton-item variant="text" class="stat-skeleton"/>
            <el-skeleton-item variant="text" class="action-skeleton"/>
          </div>
        </template>
      </el-skeleton>
    </div>

    <!-- 圈子列表 -->
    <div v-else>
      <!-- 空状态 -->
      <div v-if="!loading && circleList.length === 0" class="empty-state">
        <el-empty description="暂无圈子"/>
      </div>

      <!-- 圈子卡片 -->
      <div
          v-for="circle in circleList"
          :key="circle.id"
          class="circle-card"
          @click="goToCircle(circle.id)"
      >
        <el-card class="dark-card">
          <!-- 圈子头部 -->
          <div class="circle-header">
            <div class="circle-info">
              <!--<div class="circle-avatar">
                    <el-avatar
                        :size="50"
                        :src="'/res/ic_circle_default.svg'"
                        style="background-color: var(&#45;&#45;dark-bg-l);"
                    />
                  </div>-->
              <div class="circle-details">
                <div class="circle-name">
                  <el-tag type="success"
                          size="large"
                          style="
                          background-color: rgba(var(--theme-color-rgb), 0.2);
                          border: 1px solid var(--theme-color);
                          color: #fff;
                          font-weight: normal;
                          font-size: 16px"
                  >{{ circle.name }}
                  </el-tag>
                </div>
                <div class="creator-info">
                  <span>创建者：{{ circle.creator.nickname }}</span>
                  <span class="create-time">创建时间：{{ formatTime(circle.createTime) }}</span>
                </div>
              </div>
            </div>
            <div class="member-status" v-if="circle.member">
              <el-tag type="success" size="small" v-if="circle.memberRole === 2">圈主</el-tag>
              <el-tag type="warning" size="small" v-else-if="circle.memberRole === 1">管理员</el-tag>
              <el-tag type="info" size="small" v-else>成员</el-tag>
            </div>
            <div class="member-status" v-else>
              <div class="btn-s theme" @click.stop="joinCircle(circle.id)">
                <p class="zh">加入圈子</p>
              </div>
            </div>
          </div>

          <!-- 圈子描述 -->
          <div class="circle-description">
            <p>{{ circle.description || '这个圈子还没有描述~' }}</p>
          </div>

          <!-- 圈子底部 -->
          <div class="circle-footer">
            <div class="circle-stats">
              <span class="stat-item">
                <el-icon><User/></el-icon>
                {{ circle.memberCount }} 成员
              </span>
              <span class="stat-item">
                <el-icon><Document/></el-icon>
                {{ circle.articleCount }} 文章
              </span>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 加载更多骨架屏 -->
      <div v-if="enablePagination && loading && circleList.length > 0" class="loading-more">
        <el-skeleton
            v-for="i in 2"
            :key="`loading-${i}`"
            animated
            class="circle-skeleton"
        >
          <template #template>
            <div class="skeleton-header">
              <div class="skeleton-header-text">
                <el-skeleton-item variant="h3" class="title-skeleton"/>
                <el-skeleton-item variant="text" class="meta-skeleton"/>
              </div>
            </div>
            <el-skeleton-item variant="text" class="description-skeleton"/>
            <div class="skeleton-footer">
              <el-skeleton-item variant="text" class="stat-skeleton"/>
              <el-skeleton-item variant="text" class="stat-skeleton"/>
              <el-skeleton-item variant="text" class="action-skeleton"/>
            </div>
          </template>
        </el-skeleton>
      </div>

      <!-- 没有更多数据提示 -->
      <div v-if="!hasMore && circleList.length > 0" class="no-more">
        <XDivider :label="emptyText"></XDivider>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, onUnmounted, ref, watch} from 'vue'
import {ElMessage} from 'element-plus'
import {Document, User} from '@element-plus/icons-vue'
import {getCircles, joinCircle as joinCircleApi} from "@/net/request.js"
import XDivider from "@/aethex/components/XDivider.vue"

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
  // 没有圈子时显示的文字
  emptyText: {
    type: String,
    default: '暂无更多圈子'
  }
})

// 响应式数据
const circleList = ref([])
const loading = ref(false)
const initialLoading = ref(true)
const currentPage = ref(1)
const hasMore = ref(true)
const totalPages = ref(0)

// 默认请求参数
const defaultParams = {
  page: 1,
  size: 10
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
const fetchCircles = async (isLoadMore = false) => {
  if (loading.value) return

  loading.value = true

  try {
    // 构建请求参数
    const params = getRequestParams(isLoadMore ? currentPage.value : 1)

    await getCircles(params, (data) => {
      if (isLoadMore) {
        circleList.value.push(...data.list)
      } else {
        circleList.value = data.list
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
    ElMessage.error('获取圈子列表失败，请稍后重试')
    console.error('获取圈子列表失败：', error)
  } finally {
    loading.value = false
  }
}

// 加入圈子
const joinCircle = async (circleId) => {
  try {
    await joinCircleApi(circleId, () => {
      ElMessage.success('成功加入圈子')
      // 更新当前圈子的成员状态
      const circle = circleList.value.find(item => item.id === circleId)
      if (circle) {
        circle.member = true
        circle.memberRole = 0 // 普通成员
        circle.memberCount += 1
      }
    })
  } catch (error) {
    ElMessage.error('加入圈子失败，请稍后重试')
    console.error('加入圈子失败：', error)
  }
}

// 监听请求配置变化，重新加载数据
watch(() => props.requestConfig, () => {
  currentPage.value = 1
  circleList.value = []
  initialLoading.value = true
  fetchCircles()
}, {deep: true})

// 初始加载
const initLoad = async () => {
  // 强制显示 1 秒骨架屏
  const skeletonTimer = setTimeout(() => {
    initialLoading.value = false
    clearTimeout(skeletonTimer)
  }, 1000)

  // 同时发起数据请求
  await fetchCircles()
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
  await Promise.all([fetchCircles(true), loadingTimer])
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

// 进入圈子详情
const goToCircle = (circleId) => {
  window.open(`/circle/${circleId}`, '_blank')
}

onMounted(() => {
  initLoad()
  fetchCircles()
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
.circle-list-container {
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

.circle-skeleton {
  padding: 20px;
  background-color: rgba(45, 45, 45, 0.7);
  border-radius: 10px;
}

.skeleton-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.skeleton-header-text {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.title-skeleton {
  width: 30%;
  height: 24px;
  margin-bottom: 8px;
}

.meta-skeleton {
  width: 50%;
  height: 16px;
}

.description-skeleton {
  width: 100%;
  height: 18px;
}

.skeleton-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
}

.stat-skeleton {
  width: 80px;
  height: 20px;
  margin-right: 16px;
}

.action-skeleton {
  width: 60px;
  height: 20px;
}

/* 圈子卡片样式 */
.circle-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.circle-info {
  display: flex;
  align-items: flex-start;
}

.circle-details {
  display: flex;
  align-items: center;
  gap: 16px;
}

.circle-name {
  color: white;
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
}

.creator-info {
  font-size: 14px;
  color: var(--dark-content-m);
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.create-time {
  font-size: 12px;
}

.member-status {
  margin-left: 10px;
}

.circle-description {
  margin-bottom: 16px;
  color: var(--dark-content-m);
  line-height: 1.6;
  font-size: 14px;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.circle-description p {
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.circle-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
}

.circle-stats {
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

:deep(.el-button) {
  --el-button-bg-color: var(--dark-bg-l);
  --el-button-border-color: var(--dark-line-m);
  --el-button-text-color: var(--dark-content-m);
}

:deep(.el-button--primary) {
  --el-button-bg-color: var(--theme-color-lighten);
  --el-button-border-color: var(--theme-color-lighten);
  --el-button-text-color: white;
}
</style>
