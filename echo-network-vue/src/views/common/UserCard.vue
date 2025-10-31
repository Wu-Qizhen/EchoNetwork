<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.22
-->
<template>
  <div class="user-card-container">
    <!-- 骨架屏 -->
    <div v-if="internalLoading" class="skeleton-container">
      <el-skeleton animated class="profile-skeleton">
        <template #template>
          <div class="skeleton-avatar">
            <el-skeleton-item variant="circle" class="avatar-skeleton"/>
          </div>
          <div class="skeleton-content">
            <el-skeleton-item variant="h3" class="name-skeleton"/>
            <el-skeleton-item variant="text" class="username-skeleton"/>
            <el-skeleton-item variant="text" class="bio-skeleton"/>
            <el-skeleton-item variant="text" class="bio-skeleton short"/>
            <div class="skeleton-button">
              <el-skeleton-item variant="button" class="button-skeleton"/>
            </div>
            <div class="skeleton-meta">
              <el-skeleton-item variant="text" class="meta-skeleton"/>
              <el-skeleton-item variant="text" class="meta-skeleton"/>
              <el-skeleton-item variant="text" class="meta-skeleton"/>
              <el-skeleton-item variant="text" class="meta-skeleton"/>
              <el-skeleton-item variant="text" class="meta-skeleton"/>
            </div>
          </div>
        </template>
      </el-skeleton>
    </div>

    <!-- 实际内容 -->
    <div v-else class="profile-content">
      <div class="avatar-container">
        <img
            :src="avatarUrl || '../res/ic_avatar_default.svg'"
            :alt="username"
            class="user-avatar"
        />
      </div>

      <div class="user-info protected-content">
        <h1 class="user-nickname">{{ nickname }}</h1>
        <p class="user-username">{{ username }}</p>
        <p class="user-bio" v-if="bio">{{ bio }}</p>

        <div
            class="user-settings btn-l theme"
            @click="settings"
            v-if="isCurrentUser"
        ><p class="zh">编辑资料</p></div>

        <div
            class="user-settings btn-l theme"
            @click="toggleFollow"
            v-if="!isCurrentUser"
        >
          <p class="zh">{{ following ? '已关注' : '关注' }}</p>
        </div>

        <XSpacer type="vertical" height="20px"/>

        <div class="profile-meta">
          <!--<div class="meta-item">
            <el-icon>
              <Message/>
            </el-icon>
            <span>{{ userData.email }}</span>
          </div>-->
          <div class="meta-item">
            <el-icon>
              <MessageBox/>
            </el-icon>
            <span>发表 {{ articleCount }} 篇文章</span>
          </div>
          <div class="meta-item">
            <el-icon>
              <User/>
            </el-icon>
            <span>拥有 {{ followerCount }} 个粉丝</span>
          </div>
          <div class="meta-item">
            <el-icon>
              <CollectionTag/>
            </el-icon>
            <span>关注了 {{ followingCount }} 个用户</span>
          </div>
          <div class="meta-item">
            <el-icon>
              <Calendar/>
            </el-icon>
            <span>加入于 {{ formattedCreateTime }}</span>
          </div>
          <div class="meta-item">
            <el-icon>
              <Clock/>
            </el-icon>
            <span>最后登录于 {{ formattedLastLoginTime }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref, watch} from 'vue'
import {ElIcon, ElMessage} from 'element-plus'
import {Calendar, Clock, User, MessageBox, CollectionTag} from '@element-plus/icons-vue'
import XSpacer from "@/aethex/components/XSpacer.vue";
import router from "@/router/index.js";
import {followUser, getUser, unfollowUser} from "@/net/request.js";

// 定义组件属性
const props = defineProps({
  userId: {
    type: [String, Number],
    required: true
  },
  currentUserId: {
    type: [Number, String],
    required: false,
    default: -1
  }
})

// 内部状态
const internalLoading = ref(true)
const userData = ref({
  id: -1,
  username: '',
  email: '',
  nickname: '',
  bio: '',
  avatarUrl: '',
  role: 1,
  articleCount: 0,
  followerCount: 0,
  followingCount: 0,
  createTime: '',
  lastLoginTime: '',
  following: false
})

// 计算属性：用户基本信息
const avatarUrl = computed(() => userData.value.avatarUrl || '/res/ic_avatar_default.svg')
const nickname = computed(() => userData.value.nickname || '未知用户')
const username = computed(() => userData.value.username || '')
const bio = computed(() => userData.value.bio || '')
const articleCount = computed(() => userData.value.articleCount || 0)
const followerCount = computed(() => userData.value.followerCount || 0)
const followingCount = computed(() => userData.value.followingCount || 0)
const userIdComputed = computed(() => userData.value.id)
const following = computed(() => userData.value.following)

// 计算属性：检查是否为当前用户
const isCurrentUser = computed(() => {
  return props.currentUserId && userIdComputed.value && props.currentUserId === userIdComputed.value
})

// 计算属性：格式化时间
const formattedCreateTime = computed(() => {
  return formatDate(userData.value.createTime)
})

const formattedLastLoginTime = computed(() => {
  return formatRelativeTime(userData.value.lastLoginTime)
})

// 格式化日期
const formatDate = (dateString) => {
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1)/*.padStart(2, '0')*/
  const day = String(date.getDate())/*.padStart(2, '0')*/
  return `${year} 年 ${month} 月 ${day} 日`
}

// 格式化相对时间
const formatRelativeTime = (dateString) => {
  const date = new Date(dateString)
  const now = new Date()
  const diffInMs = now - date
  const diffInDays = Math.floor(diffInMs / (1000 * 60 * 60 * 24))

  if (diffInDays === 0) {
    return '今天'
  } else if (diffInDays === 1) {
    return '昨天'
  } else if (diffInDays < 7) {
    return `${diffInDays} 天前`
  } else if (diffInDays < 30) {
    const weeks = Math.floor(diffInDays / 7)
    return `${weeks} 周前`
  } else {
    return formatDate(dateString)
  }
}

// 获取用户数据
const fetchUserData = async () => {
  internalLoading.value = true

  try {
    await getUser(`${props.userId}`, (data) => {
      if (data && data.user) {
        userData.value = {
          ...userData.value,
          ...data.user,
          following: data.following
        }
      }
    })
  } catch (error) {
    console.error('获取用户数据失败：', error)
    ElMessage.error('获取用户信息失败')
  } finally {
    // 强制显示骨架屏至少 1 秒
    setTimeout(() => {
      internalLoading.value = false
    }, 1000)
  }
}

// 监听 userId 变化
watch(() => props.userId, (newUserId) => {
  if (newUserId) {
    fetchUserData()
  }
})

// 初始化加载
onMounted(() => {
  fetchUserData()
})

function settings() {
  window.open(
      '/settings',
      '_blank'
  )
}

/*function follow() {
  followUser(userData.value.id, () => {
    ElMessage.success('关注成功')
  })
}

function unfollow() {
  unfollowUser(userData.value.id, () => {
    ElMessage.success('取消关注成功')
  })
}*/

function toggleFollow() {
  if (following.value) {
    // 当前已关注，执行取消关注
    unfollowUser(userData.value.id, () => {
      // 更新本地状态
      userData.value.following = false
      ElMessage.error('已取消关注')
    })
  } else {
    // 当前未关注，执行关注
    followUser(userData.value.id, () => {
      // 更新本地状态
      userData.value.following = true
      ElMessage.success('关注成功')
    })
  }
}
</script>

<style scoped>
.user-card-container {
  width: 100%;
  padding: 30px;
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  align-items: start;
  justify-content: flex-start;
}

/* 骨架屏样式 */
.skeleton-container {
  width: 100%;
  animation: fadeIn 0.3s ease-in;
}

.profile-skeleton {
  width: 100%;
  padding: 0;
}

.skeleton-avatar {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 20px;
}

.avatar-skeleton {
  width: 250px;
  height: 250px;
}

.skeleton-content {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.name-skeleton {
  width: 60%;
  height: 28px;
  margin-bottom: 8px;
}

.username-skeleton {
  width: 40%;
  height: 20px;
  margin-bottom: 12px;
}

.bio-skeleton {
  width: 90%;
  height: 16px;
  margin-bottom: 8px;
}

.bio-skeleton.short {
  width: 70%;
}

.skeleton-button {
  margin: 15px 0;
}

.button-skeleton {
  width: 100%;
  height: 40px;
  border-radius: 8px;
}

.skeleton-meta {
  display: flex;
  flex-direction: column;
  gap: 8px;
  /* margin-top: 20px; */
}

.meta-skeleton {
  width: 100%;
  height: 16px;
}

/* 实际内容样式 */
.profile-content {
  width: 100%;
  animation: fadeIn 0.5s ease-in;
}

.avatar-container {
  flex-shrink: 0;
}

.user-avatar {
  border: none;
  background-color: var(--dark-bg-s);
  border-radius: 50%;
  object-fit: cover;
  width: 100%;
  height: auto;
}

.user-info {
  width: 100%;
  margin-top: 20px;
  flex: 1;
}

.user-nickname {
  font-size: 28px;
  font-weight: 600;
  line-height: 1.25;
  margin: 0 0 5px;
  color: #fff;
}

.user-username {
  font-size: 20px;
  font-weight: 300;
  color: var(--dark-content-m);
  margin: 0 0 15px;
}

.user-bio {
  font-size: 16px;
  color: var(--dark-content-s);
  margin: 0 0 15px;
}

.user-settings {
  width: 100%;
  display: flex;
  justify-content: center;
}

.profile-meta {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: var(--dark-content-m);
}

.meta-item .el-icon {
  color: var(--dark-content-l);
}

/* @media (max-width: 768px) {
  .profile-header {
    flex-direction: column;
    text-align: center;
  }

  .username {
    font-size: 22px;
  }

  .user-login {
    font-size: 18px;
  }
} */

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

/* Element Plus 深色模式样式覆盖 */
:deep(.el-skeleton) {
  --el-skeleton-color: var(--dark-bg-l);
  --el-skeleton-to-color: var(--dark-bg-xl);
}

:deep(.el-skeleton__item) {
  background: linear-gradient(90deg, var(--dark-bg-l) 25%, var(--dark-bg-xl) 37%, var(--dark-bg-l) 63%);
}
</style>
