<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.22
-->
<template>
  <div class="profile-card">
    <div class="avatar-container">
      <img
          :src="avatarUrl || '../res/ic_avatar_default.svg'"
          :alt="username"
          class="user-avatar"
      />
    </div>

    <div class="user-info">
      <h1 class="user-nickname">{{ nickname }}</h1>
      <p class="user-username">{{ username }}</p>
      <p class="user-bio" v-if="bio">{{ bio }}</p>

      <div
          class="user-settings btn theme"
          @click="settings"
          v-if="isCurrentUser"
      ><p class="zh">编辑资料</p></div>

      <!-- TODO 关注、取消关注 -->

      <XSpacer type="vertical"
               height="20px"
               v-if="isCurrentUser"/>

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
</template>

<script setup>
import {computed, defineProps} from 'vue'
import {ElIcon} from 'element-plus'
import {Calendar, Clock, User, MessageBox} from '@element-plus/icons-vue'
import XSpacer from "@/aethex/components/XSpacer.vue";
import router from "@/router/index.js";

// 定义组件属性
const props = defineProps({
  userData: {
    type: Object,
    required: true,
    default: () => ({})
  },
  currentUserId: {
    type: Number,
    required: true,
    default: -1
  }
})

// 使用计算属性提供默认值，避免直接访问 null 的属性
const userDataSafe = computed(() => props.userData || {})

// 计算属性：用户基本信息
const avatarUrl = computed(() => userDataSafe.value.avatarUrl || '../res/ic_avatar_default.svg')
const nickname = computed(() => userDataSafe.value.nickname || '未知用户')
const username = computed(() => userDataSafe.value.username || '')
const bio = computed(() => userDataSafe.value.bio || '')
const articleCount = computed(() => userDataSafe.value.articleCount || 0)
const followerCount = computed(() => userDataSafe.value.followerCount || 0)
const followingCount = computed(() => userDataSafe.value.followingCount || 0)
const userId = computed(() => userDataSafe.value.id)

// 计算属性：检查是否为当前用户
const isCurrentUser = computed(() => {
  return props.currentUserId && userId.value && props.currentUserId === userId.value
})

// 计算属性：格式化时间
const formattedCreateTime = computed(() => {
  return formatDate(userDataSafe.value.createTime)
})

const formattedLastLoginTime = computed(() => {
  return formatRelativeTime(userDataSafe.value.lastLoginTime)
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

function settings() {
  router.push('/settings')
}
</script>

<style scoped>
.profile-card {
  width: 100%;
  padding: 30px;
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  align-items: start;
  justify-content: flex-start;
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
</style>
