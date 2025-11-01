<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.31
-->
<template>
  <div class="profile-edit-page">
    <!-- 骨架屏 -->
    <div v-if="loading" class="skeleton-container">
      <el-skeleton animated class="profile-skeleton">
        <template #template>
          <div class="skeleton-avatar-edit">
            <el-skeleton-item variant="circle" class="avatar-edit-skeleton"/>
          </div>
          <div class="skeleton-content">
            <el-skeleton-item variant="h3" class="name-skeleton"/>
            <el-skeleton-item variant="text" class="field-skeleton"/>
            <el-skeleton-item variant="text" class="field-skeleton"/>
            <el-skeleton-item variant="text" class="field-skeleton long"/>
            <div class="skeleton-buttons">
              <el-skeleton-item variant="button" class="button-skeleton"/>
              <el-skeleton-item variant="button" class="button-skeleton secondary"/>
            </div>
          </div>
        </template>
      </el-skeleton>
    </div>

    <!-- 编辑表单 -->
    <div v-else class="edit-content">
      <div class="edit-header">
        <h1 class="edit-title">编辑资料</h1>
        <p class="edit-subtitle">更新您的个人资料信息</p>
      </div>

      <div>
        <!-- 头像编辑区域 -->
        <h2>头像编辑</h2>
        <XSpacer height="10px"/>
        <div class="avatar-edit-section">
          <div class="avatar-preview">
            <img
                :src="form.avatarUrl || '/res/ic_avatar_default.svg'"
                alt=" "
                class="avatar-preview-image"
            />
          </div>
          <div class="avatar-controls">
            <el-input
                v-model="form.avatarUrl"
                placeholder="输入头像图片 URL，支持 JPG、PNG 格式的图片链接"
                class="avatar-url-input"
                :clearable="true"
            >
              <!--<template #prepend>
                    <el-icon>
                      <Link/>
                    </el-icon>
                  </template>-->
            </el-input>
          </div>
        </div>

        <!-- 基本信息表单 -->
        <div>
          <div class="form-item">
            <h2>昵称</h2>
            <XSpacer height="10px"/>
            <el-input
                v-model="form.nickname"
                placeholder="请输入您的昵称"
                :maxlength="20"
                show-word-limit
                class="form-input"
                :clearable="true"
            />
            <p class="form-tip">昵称将在文章中显示，最多 20 个字符</p>
          </div>

          <div class="form-item">
            <h2>用户名</h2>
            <XSpacer height="10px"/>
            <el-input
                v-model="userData.username"
                placeholder="用户名"
                class="form-input"
                disabled
            />
            <p class="form-tip">用户名不可修改</p>
          </div>

          <div class="form-item">
            <h2>个人简介</h2>
            <XSpacer height="10px"/>
            <el-input
                v-model="form.bio"
                type="textarea"
                :rows="4"
                placeholder="介绍一下自己"
                :maxlength="200"
                show-word-limit
                resize="none"
                class="form-textarea"
            />
            <p class="form-tip">用一段简短的文字介绍自己，最多 200 个字符</p>
          </div>
        </div>

        <!-- 统计信息 -->
        <div>
          <h2>账户统计</h2>
          <XSpacer height="10px"/>
          <div class="stats-grid">
            <div class="stat-item">
              <div class="stat-value">{{ userData.articleCount || 0 }}</div>
              <div class="stat-label">文章数</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userData.followerCount || 0 }}</div>
              <div class="stat-label">粉丝数</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userData.followingCount || 0 }}</div>
              <div class="stat-label">关注数</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ formattedCreateTime }}</div>
              <div class="stat-label">加入时间</div>
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div
            class="action-buttons"
        >
          <div
              class="btn-m red"
              @click="handleCancel"
              v-if="!saving"
          >
            <p class="zh">取消</p>
          </div>
          <div
              class="btn-m theme"
              @click="handleSave"
              v-if="!saving && isFormChanged"
          >
            <p class="zh">保存更改</p>
          </div>
          <div
              class="btn-m disable"
              v-if="!isFormChanged"
          >
            <p class="zh">保存更改</p>
          </div>
          <div
              class="btn-m disable"
              v-if="saving"
          >
            <p class="zh">取消</p>
          </div>
          <div
              class="btn-m disable"
              v-if="saving"
          >
            <p class="zh">保存中</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive, computed, onMounted} from 'vue'
import {ElMessage} from 'element-plus'
import {getUser, updateUser} from '@/net/request.js'
import {getUserInfo} from '@/net/index.js'
import XSpacer from "@/aethex/components/XSpacer.vue";
import router from "@/router/index.js";

// 加载状态
const loading = ref(true)
const saving = ref(false)

// 用户数据
const userData = ref({
  id: -1,
  username: '',
  nickname: '',
  bio: '',
  avatarUrl: '',
  articleCount: 0,
  followerCount: 0,
  followingCount: 0,
  createTime: ''
})

// 表单数据
const form = reactive({
  nickname: '',
  bio: '',
  avatarUrl: ''
})

// 原始表单数据（用于比较是否修改）
const originalForm = reactive({
  nickname: '',
  bio: '',
  avatarUrl: ''
})

// 计算属性：表单是否有修改
const isFormChanged = computed(() => {
  return form.nickname !== originalForm.nickname ||
      form.bio !== originalForm.bio ||
      form.avatarUrl !== originalForm.avatarUrl
})

// 计算属性：格式化创建时间
const formattedCreateTime = computed(() => {
  return formatDate(userData.value.createTime)
})

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '未知'
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1)
  const day = String(date.getDate())
  return `${year} 年 ${month} 月 ${day} 日`
}

// 获取用户数据
const fetchUserData = async () => {
  loading.value = true

  try {
    const currentUser = getUserInfo()
    if (!currentUser || !currentUser.id) {
      ElMessage.error('未登录')
      await router.push('/')
      return
    }

    await getUser(`${currentUser.id}`, (data) => {
      if (data && data.user) {
        userData.value = data.user

        // 初始化表单数据
        form.nickname = data.user.nickname || ''
        form.bio = data.user.bio || ''
        form.avatarUrl = data.user.avatarUrl || ''

        // 保存原始数据用于比较
        originalForm.nickname = form.nickname
        originalForm.bio = form.bio
        originalForm.avatarUrl = form.avatarUrl
      }
    })
  } catch (error) {
    console.error('获取用户数据失败：', error)
    ElMessage.error('获取用户信息失败')
  } finally {
    setTimeout(() => {
      loading.value = false
    }, 800)
  }
}

// 保存更改
const handleSave = async () => {
  // 表单验证
  if (!form.nickname.trim()) {
    ElMessage.error('请输入昵称')
    return
  }

  if (form.nickname.length > 20) {
    ElMessage.error('昵称不能超过 20 个字符')
    return
  }

  if (form.bio.length > 200) {
    ElMessage.error('个人简介不能超过 200 个字符')
    return
  }

  saving.value = true

  try {
    const updateData = {
      nickname: form.nickname.trim(),
      bio: form.bio.trim(),
      avatarUrl: form.avatarUrl.trim()
    }

    await updateUser(userData.value.id, updateData, (data) => {
      // 更新原始数据
      originalForm.nickname = form.nickname
      originalForm.bio = form.bio
      originalForm.avatarUrl = form.avatarUrl

      ElMessage.success('资料更新成功')

      // 更新本地用户信息
      const currentUser = getUserInfo()
      if (currentUser) {
        currentUser.nickname = form.nickname
        currentUser.avatarUrl = form.avatarUrl
        // 这里可能需要更新本地存储的用户信息
      }

      // window.close()
    })
  } catch (error) {
    console.error('更新用户资料失败：', error)
    ElMessage.error(error.message || '更新资料失败，请重试')
  } finally {
    saving.value = false
  }
}

// 取消编辑
const handleCancel = async () => {
  if (!isFormChanged.value) {
    window.close()
    return
  }

  try {
    // 恢复原始数据
    form.nickname = originalForm.nickname
    form.bio = originalForm.bio
    form.avatarUrl = originalForm.avatarUrl

    window.close()
  } catch {
    // 用户选择继续编辑，什么都不做
  }
}

// 监听表单变化，实时更新预览
/*watch(
    () => form.avatarUrl,
    (newUrl) => {
      // URL 字符串验证
    }
)*/

// 初始化加载
onMounted(() => {
  fetchUserData()
})
</script>

<style scoped>
.profile-edit-page {
  width: 100%;
  margin: 0 auto;
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  background-color: rgba(30, 35, 47, 0.6);
  color: var(--dark-content-m);
  animation: fadeIn 0.5s ease-in;
}

/* 骨架屏样式 */
.skeleton-container {
  width: 100%;
}

.profile-skeleton {
  width: 100%;
  padding: 0;
}

.skeleton-avatar-edit {
  display: flex;
  justify-content: start;
  margin-bottom: 30px;
}

.avatar-edit-skeleton {
  width: 120px;
  height: 120px;
}

.skeleton-content {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.name-skeleton {
  width: 40%;
  height: 30px;
  margin-bottom: 30px;
}

.field-skeleton {
  width: 100%;
  height: 40px;
  margin-bottom: 30px;
}

.field-skeleton.long {
  width: 100%;
  height: 80px;
  margin-bottom: 30px;
}

.skeleton-buttons {
  display: flex;
  gap: 15px;
  margin-top: 40px;
}

.button-skeleton {
  width: 120px;
  height: 40px;
  border-radius: 8px;
}

.button-skeleton.secondary {
  width: 100px;
}

/* 编辑内容样式 */
.edit-header {
  text-align: start;
  margin-bottom: 20px;
}

.edit-subtitle {
  font-size: 14px;
  color: var(--dark-content-m);
  margin: 0;
}

/* 头像编辑区域 */
.avatar-edit-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  margin-bottom: 20px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.avatar-preview {
  display: flex;
  flex-shrink: 0;
}

.avatar-preview-image {
  display: flex;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(255, 255, 255, 0.1);
  background-color: var(--dark-bg-s);
  /* text-align: center;
  align-items: center;
  justify-content: center; */
}

.avatar-controls {
  flex: 1;
}

/* 表单区域 */
.form-item {
  margin-bottom: 20px;
}

.form-input, .form-textarea {
  width: 100%;
}

.form-tip {
  font-size: 12px;
  color: var(--dark-content-s);
  margin: 8px 0 0;
}

/* 统计信息区域 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}

.stat-item {
  text-align: center;
  padding: 15px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: var(--dark-content-m);
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 20px;
  margin-top: 30px;
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Element Plus 样式覆盖 */
:deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  box-shadow: none;
}

:deep(.el-input__wrapper:hover) {
  border-color: rgba(255, 255, 255, 0.2);
}

:deep(.el-input__wrapper.is-focus) {
  border-color: var(--theme-color-lighten);
  box-shadow: 0 0 0 1px var(--theme-color-lighten);
}

:deep(.el-textarea__inner) {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  resize: none;
}

:deep(.el-textarea__inner:hover) {
  border-color: rgba(255, 255, 255, 0.2);
}

:deep(.el-textarea__inner:focus) {
  border-color: var(--theme-color-lighten);
}

:deep(.el-input.is-disabled .el-input__wrapper) {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.1);
}

:deep(.el-input__count) {
  background: transparent;
  color: var(--dark-content-s);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-edit-page {
    padding: 20px;
  }

  .stat-value {
    font-size: 16px;
  }

  .stat-item {
    padding: 10px;
  }

  .avatar-controls {
    width: 100%;
  }

  .avatar-url-input {
    width: 100%;
  }

  .avatar-edit-section {
    flex-direction: column;
    text-align: center;
  }

  .action-buttons {
    flex-direction: column;
  }
}
</style>
