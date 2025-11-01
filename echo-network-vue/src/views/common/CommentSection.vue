<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.31
-->
<template>
  <div class="comment-section dark-mode">
    <!-- 评论标题和统计 -->
    <div class="comment-header">
      <h3 class="comment-title">评论</h3>
      <span class="comment-count">{{ totalComments }} 条评论</span>
    </div>

    <!-- 发表评论区域 -->
    <div v-if="isLoggedIn" class="comment-input-section">
      <div class="input-header">
        <el-avatar
            :size="40"
            :src="currentUser.avatarUrl || '/res/ic_avatar_default.svg'"
            class="user-avatar"
        />
        <span class="user-name">{{ currentUser.nickname }}</span>
      </div>
      <div class="input-area">
        <el-input
            v-model="newComment"
            type="textarea"
            :rows="4"
            placeholder="写下你的评论"
            maxlength="1000"
            show-word-limit
            class="comment-textarea"
            resize="none"
        />
        <div class="input-actions">
          <div
              v-if="newComment.trim()"
              @click="handleSubmitComment"
              class="btn-m theme"
          >
            <p class="zh">发表评论</p>
          </div>
          <div
              v-if="!newComment.trim()"
              class="btn-m disable"
          >
            <p class="zh">发表评论</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 未登录提示 -->
    <div v-else class="login-prompt">
      <p>请登录后发表评论</p>
      <XSpacer height="20px"/>
      <div class="btn-l theme" @click="handleLogin">
        <p class="zh">立即登录</p>
      </div>
    </div>

    <!-- 评论列表 -->
    <div class="comment-list">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-comments">
        <el-skeleton
            v-for="i in 3"
            :key="i"
            animated
            class="comment-skeleton"
        >
          <template #template>
            <div class="skeleton-avatar">
              <el-skeleton-item variant="circle"/>
            </div>
            <div class="skeleton-content">
              <el-skeleton-item variant="h3" class="skeleton-name"/>
              <el-skeleton-item variant="text" class="skeleton-text"/>
              <el-skeleton-item variant="text" class="skeleton-text short"/>
              <div class="skeleton-actions">
                <el-skeleton-item variant="text" class="skeleton-action"/>
              </div>
            </div>
          </template>
        </el-skeleton>
      </div>

      <!-- 评论列表内容 -->
      <div v-else-if="comments.length > 0" class="comments-container">
        <div
            v-for="comment in comments"
            :key="comment.id"
            class="comment-item"
        >
          <!-- 用户头像 -->
          <el-avatar
              :size="40"
              :src="comment.user.avatarUrl || '/res/ic_avatar_default.svg'"
              class="comment-avatar"
              @click="handleUserClick(comment.user.id)"
          />

          <!-- 评论内容 -->
          <div class="comment-content">
            <!-- 评论头部 -->
            <div class="comment-header-info">
              <span
                  class="comment-author"
                  @click="handleUserClick(comment.user.id)"
              >
                {{ comment.user.nickname }}
              </span>
              <span class="comment-time">
                {{ formatTime(comment.createTime) }}
              </span>
            </div>

            <!-- 评论正文 -->
            <div class="comment-text">
              {{ comment.content }}
            </div>

            <!-- 评论操作 -->
            <div class="comment-actions">
              <el-button
                  :type="comment.liked ? 'primary' : 'default'"
                  size="small"
                  @click="handleLikeComment(comment)"
                  class="like-btn"
              >
                <el-icon>
                  <Coin/>
                </el-icon>
                <span class="like-count">{{ comment.likeCount || 0 }}</span>
              </el-button>

              <!-- 删除按钮（仅评论作者或文章作者可见） -->
              <el-button
                  v-if="canDeleteComment(comment)"
                  type="default"
                  size="small"
                  @click="handleDeleteComment(comment)"
                  class="delete-btn"
              >
                删除
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-comments">
        <el-empty description="暂无评论">
          <p class="empty-tip">成为第一个评论的人吧！</p>
        </el-empty>
      </div>

      <!-- 分页 -->
      <div v-if="totalComments > pageSize" class="comment-pagination">
        <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :total="totalComments"
            layout="prev, pager, next"
            background
            @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 删除确认对话框 -->
    <el-dialog
        v-model="deleteDialogVisible"
        title="确认删除"
        width="500px"
        align-center
    >
      <span>确定要删除这条评论吗？此操作不可撤销！</span>
      <template #footer>
        <div class="dialog-footer">
          <div
              class="btn-m theme"
              @click="deleteDialogVisible = false"
          >
            <p class="zh">取消</p>
          </div>
          <div
              class="btn-m red"
              @click="confirmDelete"
          >
            <p class="zh">确定删除</p>
          </div>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, computed, onMounted, watch} from 'vue'
import {ElMessage} from 'element-plus'
import {Coin} from '@element-plus/icons-vue'
import {isAuthorized, getUserInfo} from '@/net/index.js'
import {
  getArticleComments,
  submitComment,
  deleteComment,
  likeComment,
  unlikeComment
} from '@/net/request.js'
import router from '@/router/index.js'
import XSpacer from "@/aethex/components/XSpacer.vue";

const props = defineProps({
  articleId: {
    type: Number,
    required: true
  },
  articleAuthorId: {
    type: Number,
    default: null
  }
})

// 响应式数据
const comments = ref([])
const loading = ref(false)
const newComment = ref('')
const currentPage = ref(1)
const pageSize = ref(5)
const totalComments = ref(0)
const deleteDialogVisible = ref(false)
const commentToDelete = ref(null)

// 计算属性
const isLoggedIn = computed(() => isAuthorized())
const currentUser = computed(() => getUserInfo() || {})
// const totalPages = computed(() => Math.ceil(totalComments.value / pageSize.value))

// 检查是否有权限删除评论
const canDeleteComment = (comment) => {
  if (!isLoggedIn.value) return false
  const currentUserId = currentUser.value.id
  // return currentUserId === comment.user.id || currentUserId === props.articleAuthorId
  return currentUserId === comment.user.id
}

// 获取评论列表
const fetchComments = async () => {
  if (!props.articleId) return

  try {
    loading.value = true
    // 记录开始时间
    const startTime = Date.now()

    await new Promise((resolve, reject) => {
      getArticleComments(
          props.articleId,
          currentPage.value,
          pageSize.value,
          (data) => {
            comments.value = data.list || []
            totalComments.value = data.total || 0
            resolve(data)
          },
          (error) => {
            reject(error)
          }
      )
    })

    // 确保至少显示 1 秒骨架屏
    const elapsed = Date.now() - startTime
    if (elapsed < 1000) {
      await new Promise(resolve => setTimeout(resolve, 1000 - elapsed))
    }
  } catch (error) {
    // 即使出错也要确保显示至少 1 秒
    const elapsed = Date.now() - startTime
    if (elapsed < 1000) {
      await new Promise(resolve => setTimeout(resolve, 1000 - elapsed))
    }
    ElMessage.error('获取评论失败')
    console.error('获取评论失败：', error)
  } finally {
    loading.value = false
  }
}

/*const fetchComments = async () => {
  if (!props.articleId) return

  try {
    loading.value = true
    await new Promise((resolve, reject) => {
      getArticleComments(
          props.articleId,
          currentPage.value,
          pageSize.value,
          (data) => {
            comments.value = data.list || []
            totalComments.value = data.total || 0
            resolve(data)
          },
          (error) => {
            reject(error)
          }
      )
    })
  } catch (error) {
    ElMessage.error('获取评论失败')
    console.error('获取评论失败：', error)
  } finally {
    loading.value = false
  }
}*/

// 发表评论
const handleSubmitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('评论内容不能为空')
    return
  }

  try {
    await new Promise((resolve, reject) => {
      submitComment(
          {
            content: newComment.value.trim(),
            articleId: props.articleId
          },
          (data) => {
            ElMessage.success('评论发表成功')
            newComment.value = ''
            // 重新获取评论列表，显示最新评论
            currentPage.value = 1
            fetchComments()
            resolve(data)
          },
          (error) => {
            reject(error)
          }
      )
    })
  } catch (error) {
    ElMessage.error('发表评论失败')
    console.error('发表评论失败：', error)
  }
}

// 点赞、取消点赞评论
const handleLikeComment = async (comment) => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    if (comment.liked) {
      // 取消点赞
      await new Promise((resolve, reject) => {
        unlikeComment(
            comment.id,
            (data) => {
              comment.likeCount = data.likeCount
              comment.liked = data.liked
              resolve(data)
              ElMessage.warning('已取消点赞')
            },
            (error) => {
              reject(error)
            }
        )
      })
    } else {
      // 点赞
      await new Promise((resolve, reject) => {
        likeComment(
            comment.id,
            (data) => {
              comment.likeCount = data.likeCount
              comment.liked = data.liked
              resolve(data)
              ElMessage.success('点赞成功')
            },
            (error) => {
              reject(error)
            }
        )
      })
    }
  } catch (error) {
    ElMessage.error('操作失败')
    console.error('点赞操作失败：', error)
  }
}

// 删除评论
const handleDeleteComment = (comment) => {
  commentToDelete.value = comment
  deleteDialogVisible.value = true
}

const confirmDelete = async () => {
  if (!commentToDelete.value) return

  try {
    await new Promise((resolve, reject) => {
      deleteComment(
          commentToDelete.value.id,
          () => {
            ElMessage.success('评论删除成功')
            // 从列表中移除已删除的评论
            const index = comments.value.findIndex(c => c.id === commentToDelete.value.id)
            if (index !== -1) {
              comments.value.splice(index, 1)
              totalComments.value -= 1
            }
            resolve()
          },
          (error) => {
            reject(error)
          }
      )
    })
  } catch (error) {
    ElMessage.error('评论删除失败')
    console.error('评论删除失败：', error)
  } finally {
    deleteDialogVisible.value = false
    commentToDelete.value = null
  }
}

// 用户点击处理
const handleUserClick = (userId) => {
  window.open(`/user/${userId}`, '_blank')
}

const handleLogin = () => {
  router.push('/auth/login')
}

// 分页处理
const handlePageChange = (page) => {
  currentPage.value = page
  fetchComments()
}

// 时间格式化
const formatTime = (timeString) => {
  const date = new Date(timeString)
  const now = new Date()
  const diffMs = now - date
  const diffMins = Math.floor(diffMs / 60000)
  const diffHours = Math.floor(diffMs / 3600000)
  const diffDays = Math.floor(diffMs / 86400000)

  if (diffMins < 1) {
    return '刚刚'
  } else if (diffMins < 60) {
    return `${diffMins} 分钟前`
  } else if (diffHours < 24) {
    return `${diffHours} 小时前`
  } else if (diffDays < 7) {
    return `${diffDays} 天前`
  } else {
    return date.toLocaleDateString()
  }
}

// 监听文章 ID 变化
watch(() => props.articleId, () => {
  if (props.articleId) {
    currentPage.value = 1
    fetchComments()
  }
})

onMounted(() => {
  if (props.articleId) {
    fetchComments()
  }
})
</script>

<style scoped>
.comment-section {
  width: 100%;
  padding: 0;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
}

.dark-mode {
  background-color: rgba(30, 35, 47, 0.6);
  color: var(--dark-content-m);
}

/* 评论头部 */
.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 30px;
  border-bottom: 1px solid var(--dark-line-m);
}

.comment-title {
  margin: 0;
}

.comment-count {
  font-size: 14px;
  color: var(--dark-content-m);
}

/* 发表评论区域 */
.comment-input-section {
  padding: 20px 30px;
  border-bottom: 1px solid var(--dark-line-m);
}

.input-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.user-avatar {
  background-color: var(--dark-bg-l);
  margin-right: 16px;
}

.user-name {
  font-size: 16px;
  font-weight: 500;
  color: white;
}

.input-area {
  margin-left: 56px; /* 对齐头像 */
}

.comment-textarea {
  margin-bottom: 20px;
}

:deep(.comment-textarea .el-textarea__inner) {
  background-color: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: white;
  border-radius: 10px;
  font-size: 14px;
  line-height: 1.6;
}

:deep(.comment-textarea .el-textarea__inner:focus) {
  border-color: var(--theme-color-lighten);
}

:deep(.comment-textarea .el-input__count) {
  background: transparent;
  color: var(--dark-content-m);
}

.input-actions {
  display: flex;
  justify-content: flex-end;
}

/* 未登录提示 */
.login-prompt {
  text-align: center;
  padding: 30px;
  border-bottom: 1px solid var(--dark-line-m);
}

/* 评论列表 */
/* .comment-list {
  min-height: 200px;
} */

/* 加载状态 */
/* .loading-comments {
} */

.comment-skeleton {
  display: flex;
  padding: 20px 30px;
  border-bottom: 1px solid var(--dark-line-m);
}

.comment-skeleton:last-child {
  border-bottom: none;
}

.skeleton-avatar {
  margin-right: 16px;
}

.skeleton-avatar :deep(.el-skeleton__circle) {
  width: 40px;
  height: 40px;
}

:deep(.el-skeleton) {
  --el-skeleton-color: var(--dark-bg-l);
  --el-skeleton-to-color: var(--dark-bg-xl);
}

.skeleton-content {
  flex: 1;
}

.skeleton-name {
  width: 120px;
  height: 20px;
  margin-bottom: 10px;
}

.skeleton-text {
  width: 100%;
  height: 16px;
  margin-bottom: 5px;
}

.skeleton-text.short {
  width: 80%;
}

.skeleton-actions {
  margin-top: 10px;
}

.skeleton-action {
  width: 60px;
  height: 20px;
}

/* 评论项 */
.comment-item {
  display: flex;
  padding: 20px 30px;
  border-bottom: 1px solid var(--dark-line-s);
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-avatar {
  margin-right: 16px;
  cursor: pointer;
  transition: transform 0.2s;
}

.comment-avatar:hover {
  transform: scale(1.05);
}

.comment-content {
  flex: 1;
}

.comment-header-info {
  height: 40px;
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.comment-author {
  font-size: 16px;
  font-weight: 600;
  color: white;
  margin-right: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.comment-author:hover {
  color: var(--theme-color-lighten);
  transform: translateY(-2px);
}

.comment-time {
  font-size: 14px;
  color: var(--dark-content-m);
}

.comment-text {
  font-size: 14px;
  line-height: 1.6;
  color: var(--dark-content-l);
  margin-bottom: 10px;
  word-break: break-word;
}

.comment-actions {
  display: flex;
  align-items: center;
}

.like-btn {
  margin-right: 10px;
  padding: 6px 12px;
  border-radius: 16px;
}

.like-count {
  margin-left: 3px;
  font-size: 14px;
}

.delete-btn {
  color: var(--red);
  padding: 6px 12px;
  border-radius: 16px;
  margin-left: 0;
}

.delete-btn:hover {
  border-color: var(--red);
  background-color: rgba(var(--red-rgb), 0.2);
}

/* 空状态 */
.empty-comments {
  padding: 0;
  text-align: center;
}

.empty-tip {
  margin-top: 0;
  color: var(--dark-content-m);
  font-size: 14px;
}

/* 分页 */
.comment-pagination {
  display: flex;
  justify-content: center;
  margin-top: 10px;
  padding: 20px 30px;
  border-top: 1px solid var(--dark-line-m);
}

/* :deep(.el-pagination) {
  --el-pagination-bg-color: transparent;
  --el-pagination-text-color: var(--dark-content-m);
  --el-pagination-button-disabled-bg-color: transparent;
  --el-pagination-hover-color: var(--theme-color-lighten);
} */

:deep(.el-pagination.is-background .btn-prev),
:deep(.el-pagination.is-background .btn-next),
:deep(.el-pagination.is-background .el-pager li) {
  background-color: var(--dark-bg-l);
  color: var(--dark-content-m);
  border-radius: 10px;
  margin: 0 5px;
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background-color: var(--theme-color);
  color: #fff;
}

:deep(.el-pagination.is-background .btn-prev:not(.is-disabled):hover),
:deep(.el-pagination.is-background .btn-next:not(.is-disabled):hover),
:deep(.el-pagination.is-background .el-pager li:not(.is-disabled):hover) {
  color: #fff;
}

/* 对话框样式 */
:deep(.el-dialog) {
  --el-dialog-bg-color: var(--dark-bg-s);
  --el-dialog-title-text-color: var(--dark-content-l);
  --el-text-color-primary: var(--dark-content-l);
  border-radius: 20px;
  border: 1px solid var(--dark-line-m);
}

:deep(.el-dialog__header) {
  margin-right: 0;
  padding: 0;
  margin-bottom: 20px;
}

:deep(.el-dialog__body) {
  padding: 0;
}

:deep(.el-dialog__footer) {
  margin-top: 20px;
  padding: 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .input-area {
    margin-left: 0;
  }

  .comment-input-section {
    padding: 15px 20px;
  }

  .comment-skeleton {
    padding: 15px 20px;
  }

  .comment-item {
    padding: 15px 20px;
  }

  .comment-header {
    padding: 15px 20px;
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .comment-pagination {
    padding: 15px 20px;
  }
}
</style>
