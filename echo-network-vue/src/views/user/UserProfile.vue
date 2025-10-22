<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.22
-->
<template>
  <div class="user-profile">
    <el-card class="profile-card">
      <div class="profile-header">
        <div class="avatar-container">
          <img
              :src="userData.avatarUrl"
              :alt="userData.nickname"
              class="user-avatar"
          />
        </div>
        <div class="user-info">
          <h1 class="username">{{ userData.nickname }}</h1>
          <p class="user-login">{{ userData.username }}</p>
          <p class="user-bio" v-if="userData.bio">{{ userData.bio }}</p>
          <div class="profile-meta">
            <div class="meta-item">
              <el-icon>
                <Message/>
              </el-icon>
              <span>{{ userData.email }}</span>
            </div>
            <div class="meta-item" v-if="userData.phone">
              <el-icon>
                <Iphone/>
              </el-icon>
              <span>{{ userData.phone }}</span>
            </div>
            <div class="meta-item">
              <el-icon>
                <Calendar/>
              </el-icon>
              <span>加入于 {{ formatDate(userData.createTime) }}</span>
            </div>
            <div class="meta-item">
              <el-icon>
                <Clock/>
              </el-icon>
              <span>最后登录 {{ formatRelativeTime(userData.lastLoginTime) }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {defineProps} from 'vue'
import {ElCard, ElIcon} from 'element-plus'
import {Message, Iphone, Calendar, Clock} from '@element-plus/icons-vue'

// 定义组件属性
const props = defineProps({
  userData: {
    type: Object,
    required: true,
    default: () => ({})
  }
})

// 格式化日期
const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
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
</script>

<style scoped>
.user-profile {
  margin-bottom: 24px;
}

.profile-card {
  border-radius: 6px;
  border: 1px solid #e1e4e8;
  box-shadow: none;
}

.profile-header {
  display: flex;
  gap: 24px;
  padding: 16px;
}

.avatar-container {
  flex-shrink: 0;
}

.user-avatar {
  border: 1px solid #e1e4e8;
  border-radius: 50%;
  object-fit: cover;
  height: 200px;
  width: 200px;
}

.user-info {
  flex: 1;
}

.username {
  font-size: 26px;
  font-weight: 600;
  line-height: 1.25;
  margin: 0 0 4px;
  color: #24292f;
}

.user-login {
  font-size: 20px;
  font-weight: 300;
  line-height: 24px;
  color: #57606a;
  margin: 0 0 16px;
}

.user-bio {
  font-size: 16px;
  line-height: 1.5;
  color: #24292f;
  margin: 0 0 16px;
}

.profile-meta {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #57606a;
}

.meta-item .el-icon {
  color: #656d76;
}

@media (max-width: 768px) {
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
}
</style>
