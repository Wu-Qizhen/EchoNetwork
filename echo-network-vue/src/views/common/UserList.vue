<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.27
-->
<template>
  <div class="user-container dark-mode">
    <!-- 初始加载骨架屏 -->
    <div v-if="initialLoading" class="skeleton-container">
      <el-skeleton
          v-for="i in 5"
          :key="i"
          animated
          class="user-skeleton"
      >
        <template #template>
          <div class="skeleton-content">
            <el-skeleton-item variant="circle" class="avatar-skeleton"/>
            <div class="skeleton-info">
              <el-skeleton-item variant="h3" class="name-skeleton"/>
              <el-skeleton-item variant="text" class="bio-skeleton"/>
            </div>
          </div>
        </template>
      </el-skeleton>
    </div>

    <!-- 用户列表 -->
    <div v-else>
      <!-- 空状态 -->
      <div v-if="!loading && userList.length === 0" class="empty-state">
        <el-empty :description="emptyText"/>
      </div>

      <!-- 用户卡片 -->
      <div
          v-for="user in userList"
          :key="user.id"
          class="user-card"
          @click="goToUser(user.id)"
      >
        <el-card class="dark-card">
          <div class="user-info">
            <el-avatar
                :size="48"
                :src="user.avatarUrl || '/res/ic_avatar_default.svg'"
                style="background-color: var(--dark-bg-l);"
                class="user-avatar"
            />
            <div class="user-details">
              <div class="user-name">{{ user.nickname }}</div>
              <div class="user-bio" v-if="user.bio">{{ user.bio }}</div>
              <div class="user-stats" v-if="showStats">
                <span class="stat-item">
                  <el-icon><Document/></el-icon>
                  {{ user.articleCount || 0 }} 文章
                </span>
                <span class="stat-item">
                  <el-icon><User/></el-icon>
                  {{ user.followerCount || 0 }} 粉丝
                </span>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 加载更多骨架屏 -->
      <div v-if="enablePagination && loading && userList.length > 0" class="loading-more">
        <el-skeleton
            v-for="i in 3"
            :key="`loading-${i}`"
            animated
            class="user-skeleton"
        >
          <template #template>
            <div class="skeleton-content">
              <el-skeleton-item variant="circle" class="avatar-skeleton"/>
              <div class="skeleton-info">
                <el-skeleton-item variant="h3" class="name-skeleton"/>
                <el-skeleton-item variant="text" class="bio-skeleton"/>
              </div>
            </div>
          </template>
        </el-skeleton>
      </div>

      <!-- 没有更多数据提示 -->
      <div v-if="!hasMore && userList.length > 0" class="no-more">
        <XDivider :label="noMoreText"></XDivider>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, onUnmounted, ref, watch} from 'vue'
import {ElMessage} from 'element-plus'
import {Document, User} from '@element-plus/icons-vue'
import XDivider from "@/aethex/components/XDivider.vue";

const props = defineProps({
  // 请求配置
  requestConfig: {
    type: Object,
    default: () => ({})
  },
  // 自定义获取用户的函数
  fetchUsersFunction: {
    type: Function,
    required: true
  },
  // 是否启用分页
  enablePagination: {
    type: Boolean,
    default: true
  },
  // 空状态文本
  emptyText: {
    type: String,
    default: '暂无用户'
  },
  // 没有更多数据时的文本
  noMoreText: {
    type: String,
    default: '没有更多用户了'
  },
  // 是否显示用户统计信息
  showStats: {
    type: Boolean,
    default: false
  }
})

// 响应式数据
const userList = ref([])
const loading = ref(false)
const initialLoading = ref(true)
const currentPage = ref(1)
const hasMore = ref(true)
const totalPages = ref(0)

// 默认请求参数
const defaultParams = {
  /*page: 1,
  size: 10,*/
  state: 1
}

// 获取完整的请求参数
const getRequestParams = (page = currentPage.value) => {
  return {
    ...defaultParams,
    ...props.requestConfig,
    page: props.enablePagination ? page : 1,
    size: props.enablePagination ? (props.requestConfig.size || 10) : (props.requestConfig.limit || 20)
  }
}

// API 请求
const fetchUsers = async (isLoadMore = false) => {
  if (loading.value) return

  loading.value = true

  try {
    const params = getRequestParams(isLoadMore ? currentPage.value : 1)

    await props.fetchUsersFunction(params, (data) => {
      if (isLoadMore) {
        userList.value.push(...data.list || data)
      } else {
        userList.value = data.list || data
      }

      // 处理分页逻辑
      if (props.enablePagination && data.totalPages !== undefined) {
        totalPages.value = data.totalPages
        currentPage.value = data.page
        hasMore.value = currentPage.value < totalPages.value
      } else {
        // 如果没有分页信息，根据返回数据长度判断
        const items = data.list || data
        hasMore.value = props.enablePagination && items.length >= (params.size || 10)
      }
    })
  } catch (error) {
    ElMessage.error('获取用户列表失败，请稍后重试')
    console.error('获取用户列表失败：', error)
  } finally {
    loading.value = false
  }
}

// 监听请求配置变化，重新加载数据
watch(() => props.requestConfig, () => {
  currentPage.value = 1
  userList.value = []
  initialLoading.value = true
  fetchUsers()
}, {deep: true})

// 初始加载
const initLoad = async () => {
  // 强制显示 1 秒骨架屏
  const skeletonTimer = setTimeout(() => {
    initialLoading.value = false
    clearTimeout(skeletonTimer)
  }, 1000)

  // 同时发起数据请求
  await fetchUsers()
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
  await Promise.all([fetchUsers(true), loadingTimer])
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

// 跳转到用户详情页
const goToUser = (userId) => {
  /*router.push({
    name: 'user',
    params: {id: userId}
  });*/
  /*router.push(`/user/${userId}`)*/
  window.open(`/user/${userId}`, '_blank')
}

onMounted(() => {
  initLoad()
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
.user-container {
  width: 100%;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
}

/* 深色模式样式 */
.dark-mode {
  background-color: rgba(30, 35, 47, 0.6);
  color: var(--dark-content-m);
}

.dark-card {
  background-color: rgba(45, 45, 45, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  transform-origin: center;
}

.dark-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgb(255, 255, 255, 0.1);
}

.dark-card :deep(.el-card__body) {
  padding: 15px 20px;
}

/* 骨架屏样式 */
.skeleton-container, .loading-more {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.user-skeleton {
  padding: 16px 20px;
  background-color: rgba(45, 45, 45, 0.7);
  border-radius: 10px;
}

.skeleton-content {
  display: flex;
  align-items: center;
}

.avatar-skeleton {
  width: 48px;
  height: 48px;
  margin-right: 16px;
}

.skeleton-info {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.name-skeleton {
  width: 30%;
  height: 20px;
  margin-bottom: 8px;
}

.bio-skeleton {
  width: 60%;
  height: 16px;
}

/* 用户列表样式 */
.user-info {
  display: flex;
  align-items: center;
}

.user-avatar {
  margin-right: 16px;
  flex-shrink: 0;
}

.user-details {
  flex: 1;
  min-width: 0; /* 防止文本溢出 */
}

.user-name {
  color: white;
  font-weight: 600;
  font-size: 16px;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-bio {
  color: var(--dark-content-m);
  font-size: 14px;
  line-height: 1.4;
  margin-bottom: 6px;

  /* 最多显示一行 */
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.user-stats {
  display: flex;
  gap: 16px;
  font-size: 12px;
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

:deep(.el-card) {
  --el-card-bg-color: var(--dark-bg-s);
  --el-card-border-color: var(--dark-line-s);
}
</style>
