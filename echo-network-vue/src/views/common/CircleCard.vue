<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.29
-->
<template>
  <div class="circle-card-container">
    <!-- 骨架屏 -->
    <div v-if="internalLoading" class="skeleton-container">
      <el-skeleton animated class="circle-skeleton">
        <template #template>
          <div class="skeleton-header">
            <div class="skeleton-avatar">
              <el-skeleton-item variant="circle" class="avatar-skeleton"/>
            </div>
            <div class="skeleton-header-text">
              <el-skeleton-item variant="h3" class="name-skeleton"/>
              <el-skeleton-item variant="text" class="creator-skeleton"/>
            </div>
          </div>
          <el-skeleton-item variant="text" class="description-skeleton"/>
          <el-skeleton-item variant="text" class="description-skeleton short"/>
        </template>
      </el-skeleton>
    </div>

    <!-- 实际内容 -->
    <div v-else class="circle-content">
      <!-- 圈子头部 -->
      <div class="circle-header-container">
        <div class="circle-header">
          <div class="circle-avatar">
            <el-avatar
                :size="80"
                :src="circleData.avatarUrl || '/res/ic_circle_default.svg'"
                style="background-color: rgba(var(--theme-color-rgb), 0.3);"
            />
          </div>
          <div class="circle-info">
            <h1 class="circle-name">{{ circleData.name }}</h1>
            <div class="creator-info">
              <span>创建者：<a @click="goToUser" class="creator-link">{{ circleData.creator.nickname }}</a></span>
              <span class="create-time">创建时间：{{ formatTime(circleData.createTime) }}</span>
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="circle-actions">
          <div
              class="btn-s theme"
              @click="handleJoinCircle"
              v-if="!circleData.member"
          >
            <p class="zh">加入圈子</p>
          </div>
          <div
              class="btn-s red"
              @click="handleExitCircle(circleData.id)"
              v-if="circleData.member"
          >
            <p class="zh">退出圈子</p>
          </div>
        </div>

        <!-- 圈子统计 -->
        <div class="circle-stats">
          <div class="stat-item">
            <el-icon>
              <User/>
            </el-icon>
            <span class="stat-number">{{ circleData.memberCount }}</span>
            <span class="stat-label">成员</span>
          </div>
          <div class="stat-item">
            <el-icon>
              <Document/>
            </el-icon>
            <span class="stat-number">{{ circleData.articleCount }}</span>
            <span class="stat-label">文章</span>
          </div>
          <div class="stat-item" v-if="circleData.member">
            <el-icon>
              <Star/>
            </el-icon>
            <span class="stat-number">{{ getMemberRoleText(circleData.role) }}</span>
            <span class="stat-label">角色</span>
          </div>
        </div>
      </div>

      <!-- 圈子描述 -->
      <div class="circle-description">
        <p>圈子介绍：{{ circleData.description || '这个圈子还没有描述~' }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, watch, onMounted} from 'vue'
import {ElMessage} from 'element-plus'
import {Document, User, Star} from '@element-plus/icons-vue'
import {exitCircle, getCircle, joinCircle,} from "@/net/request.js"

// 定义组件属性
const props = defineProps({
  circleId: {
    type: [String, Number],
    required: true
  }
})

// 内部状态
const internalLoading = ref(true)
const circleData = ref({
  id: null,
  name: '',
  description: '',
  avatarUrl: '',
  createTime: '',
  memberCount: 0,
  articleCount: 0,
  member: false,
  role: null,
  creator: {
    id: null,
    nickname: '',
    avatarUrl: ''
  }
})

// 获取圈子数据
const fetchCircleData = async () => {
  internalLoading.value = true

  try {
    await getCircle(props.circleId, (data) => {
      if (data) {
        circleData.value = {...circleData.value, ...data}
      }
    })
  } catch (error) {
    console.error('获取圈子数据失败：', error)
    ElMessage.error('获取圈子信息失败')
  } finally {
    // 强制显示骨架屏至少 1 秒
    setTimeout(() => {
      internalLoading.value = false
    }, 1000)
  }
}

// 加入圈子
const handleJoinCircle = async () => {
  try {
    await joinCircle(circleData.value.id, () => {
      ElMessage.success('成功加入圈子')
      // 更新本地状态
      circleData.value.member = true
      circleData.value.role = 0 // 普通成员
      circleData.value.memberCount += 1
    })
  } catch (error) {
    ElMessage.error('加入圈子失败，请稍后重试')
    console.error('加入圈子失败：', error)
  }
}

// 退出圈子
const handleExitCircle = async (circleId) => {
  if (circleData.value.role !== 2) {
    try {
      await exitCircle(circleId, () => {
        ElMessage.success('成功退出圈子')
        // 更新本地状态
        circleData.value.member = false
        circleData.value.role = null
        circleData.value.memberCount -= 1
      })
    } catch (error) {
      ElMessage.error('退出圈子失败，请稍后重试')
    }
  } else {
    ElMessage.error('圈主不能退出圈子')
  }
}

// 获取成员角色文本
const getMemberRoleText = (role) => {
  switch (role) {
    case 2:
      return '圈主'
    case 1:
      return '管理员'
    case 0:
      return '成员'
    default:
      return '成员'
  }
}

/*const getMemberRoleType = (role) => {
  switch (role) {
    case 2:
      return 'success'
    case 1:
      return 'warning'
    case 0:
      return 'info'
    default:
      return 'info'
  }
}*/

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

// 监听 circleId 变化
watch(() => props.circleId, (newCircleId) => {
  if (newCircleId) {
    fetchCircleData()
  }
})

// 初始化加载
onMounted(() => {
  fetchCircleData()
})

function goToUser() {
  window.open(`/user/${circleData.value.creator.id}`, '_blank')
}
</script>

<style scoped>
.circle-card-container {
  width: 100%;
  margin: 0 auto;
  padding: 30px;
  background-color: rgba(30, 35, 47, 0.6);
  color: var(--dark-content-m);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  animation: fadeIn 0.5s ease-in;
}

/* 骨架屏样式 */
.skeleton-container {
  width: 100%;
}

.circle-skeleton {
  width: 100%;
}

.skeleton-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.skeleton-avatar {
  margin-right: 20px;
}

.avatar-skeleton {
  width: 80px;
  height: 80px;
}

.skeleton-header-text {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.name-skeleton {
  width: 40%;
  height: 28px;
  margin-bottom: 8px;
}

.creator-skeleton {
  width: 60%;
  height: 18px;
}

.description-skeleton {
  width: 100%;
  height: 18px;
  margin-bottom: 8px;
}

.description-skeleton.short {
  width: 80%;
}

/* 实际内容样式 */
.circle-content {
  width: 100%;
  display: flex;
  flex-direction: column;
}

.circle-header-container {
  display: flex;
  align-items: start;
  justify-content: start;
  /* margin-bottom: 10px; */
}

/* 圈子头部样式 */
.circle-header {
  display: flex;
  align-items: flex-start;
  margin-right: 20px;
}

.circle-avatar {
  margin-right: 20px;
  flex-shrink: 0;
}

.circle-info {
  flex: 1;
}

.circle-name {
  margin: 0 0 5px 0;
}

.creator-info {
  display: flex;
  flex-direction: column;
  gap: 3px;
  font-size: 14px;
  color: var(--dark-content-m);
}

.create-time {
  font-size: 12px;
}

.creator-link {
  cursor: pointer;
}

.creator-link:hover {
  color: var(--theme-color-lighten);
}

/* 圈子描述样式 */
.circle-description {
  color: var(--dark-content-m);
  line-height: 1.6;
  font-size: 16px;
}

.circle-description p {
  margin: 0;
}

/* 圈子统计样式 */
.circle-stats {
  margin-left: auto;
  display: flex;
  gap: 40px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  color: var(--dark-content-m);
}

.stat-item .el-icon {
  font-size: 28px;
  color: var(--theme-color-lighten);
}

.stat-number {
  font-size: 24px;
  font-weight: 600;
  color: #fff;
}

.stat-label {
  font-size: 14px;
}

/* 操作按钮样式 */
.circle-actions {
  margin-top: 8px;
}

/* Element Plus 深色模式样式覆盖 */
:deep(.el-skeleton) {
  --el-skeleton-color: var(--dark-bg-l);
  --el-skeleton-to-color: var(--dark-bg-xl);
}

:deep(.el-card) {
  --el-card-bg-color: var(--dark-bg-s);
  --el-card-border-color: var(--dark-line-s);
}

:deep(.el-tag) {
  --el-tag-bg-color: var(--dark-bg-l);
  --el-tag-border-color: var(--dark-line-m);
  --el-tag-text-color: var(--dark-content-m);
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
