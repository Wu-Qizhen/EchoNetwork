<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.30
-->
<template>
  <div class="recommend-list-container dark-mode">
    <el-card class="recommend-card">
      <template #header>
        <div class="card-header">
          <div class="header-title">
            <el-icon class="title-icon">
              <Trophy/>
            </el-icon>
            <span>推荐圈子</span>
          </div>
        </div>
      </template>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <el-icon class="loading-icon">
          <Loading/>
        </el-icon>
        <span>加载中</span>
      </div>

      <!-- 排行榜列表 -->
      <div v-else class="recommend-list">
        <div
            v-for="(circle, index) in recommendList"
            :key="circle.id"
            class="recommend-item"
            :class="{
            'top-1': index === 0,
            'top-2': index === 1,
            'top-3': index === 2
          }"
            @click="handleCircleClick(circle)"
        >
          <div class="rank-number">
            <span class="number">{{ index + 1 }}</span>
            <el-icon v-if="index < 3" class="crown-icon">
              <GoldMedal v-if="index === 0"/>
              <Medal v-else-if="index === 1"/>
              <Aim v-else/>
            </el-icon>
          </div>

          <div class="circle-info">
            <div class="circle-title">{{ circle.title }}</div>
            <div class="heat-info">
              <span class="article-count">
                <el-icon><Files/></el-icon>
                {{ formatArticleCount(circle.articleCount) }}
              </span>
              <span class="member-count">
                <el-icon><User/></el-icon>
                {{ formatMemberCount(circle.memberCount) }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="!loading && recommendList.length === 0" class="empty-state">
        <el-empty description="暂无推荐数据" :image-size="80"/>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {Aim, Files, GoldMedal, Loading, Medal, Trophy, User} from '@element-plus/icons-vue'
import {getCircles} from "@/net/request.js";

// 响应式数据
const recommendList = ref([])
const loading = ref(true)

const fetchHotRecommend = async () => {
  try {
    await getCircles({
      sortBy: 'articleCount',
      sortOrder: 'DESC',
      page: 1,
      size: 10
    }, (data) => {
      if (data && data.list) {
        recommendList.value = data.list
      } else {
        console.warn('返回的数据结构不符合预期：', data)
        recommendList.value = []
      }
    })
  } catch (error) {
    console.error('API 请求错误：', error)
  } finally {
    loading.value = false
  }
}

// 处理圈子点击
const handleCircleClick = (circle) => {
  window.open(
      '/circle/' + circle.id,
      '_blank'
  )
}

const formatArticleCount = (articleCount) => {
  if (articleCount >= 10000) {
    return (articleCount / 10000).toFixed(1) + 'W'
  }
  return articleCount.toString()
}

const formatMemberCount = (count) => {
  if (count >= 10000) {
    return (count / 10000).toFixed(1) + 'W'
  }
  return count.toString()
}

// 组件挂载时获取数据
onMounted(() => {
  fetchHotRecommend()
})
</script>

<style scoped>
.recommend-list-container {
  width: 100%;
}

.recommend-card {
  background-color: rgba(30, 35, 47, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  box-shadow: none;
  /* backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px); */
}

.recommend-card:hover {
  box-shadow: 0 10px 20px rgba(255, 255, 255, 0.2);
}

:deep(.recommend-card .el-card__header) {
  background-color: rgba(45, 45, 45, 0.7);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  padding: 16px 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  display: flex;
  align-items: center;
  font-weight: 600;
  color: #e0e0e0;
}

.title-icon {
  margin-right: 8px;
  color: white;
  font-size: 18px;
}

/* 加载状态 */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 20px;
  color: #a8abb2;
}

.loading-icon {
  font-size: 24px;
  margin-bottom: 12px;
  animation: rotate 2s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 排行榜列表 */
.recommend-list {
  padding: 0;
}

.recommend-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  margin: 5px 0;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.recommend-item:hover {
  background-color: rgba(45, 45, 45, 0.7);
  border-color: rgba(255, 255, 255, 0.2);
  transform: translateX(4px);
}

/* 前三名特殊样式 */
.recommend-item.top-1 {
  background: linear-gradient(135deg, rgba(249, 176, 148, 0.2) 0%, rgba(255, 223, 124, 0.6) 100%);
  border-color: #e6a23c;
}

.recommend-item.top-2 {
  background: linear-gradient(135deg, #3a3a3a 0%, #2a3a4a 100%);
  border-color: #909399;
}

.recommend-item.top-3 {
  /* background: linear-gradient(135deg, #3a3a3a 0%, #4a462a 100%);
  border-color: #b88230; */
  background: linear-gradient(135deg, #3a3a3a 0%, #4a2a00 100%);
  border-color: #e6a23c;
}

.rank-number {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  margin-right: 16px;
  border-radius: 6px;
  font-weight: bold;
  font-size: 14px;
}

/* 前三名数字样式 */
.top-1 .rank-number {
  background: linear-gradient(135deg, #ffdf7c 0%, #f99a18 100%);
  color: #fff;
}

.top-2 .rank-number {
  background: linear-gradient(135deg, #909399 0%, #73767a 100%);
  color: #fff;
}

.top-3 .rank-number {
  background: linear-gradient(135deg, #b88230 0%, #966c25 100%);
  color: #fff;
}

/* 其他排名的数字样式 */
.recommend-item:not(.top-1):not(.top-2):not(.top-3) .rank-number {
  background-color: #444;
  color: #a8abb2;
}

.crown-icon {
  position: absolute;
  top: -8px;
  right: -8px;
  font-size: 16px;
}

.top-1 .crown-icon {
  color: #e6a23c;
}

.top-2 .crown-icon {
  color: #c0c0c0;
}

.top-3 .crown-icon {
  color: #cd7f32;
}

.circle-info {
  flex: 1;
  min-width: 0;
}

.circle-title {
  font-size: 14px;
  font-weight: 500;
  color: var(--dark-content-l);
  line-height: 1.4;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}

/* 前三名标题特殊样式 */
.top-1 .circle-title {
  color: #ffdf7c;
}

.top-2 .circle-title {
  color: #c0c0c0;
}

.top-3 .circle-title {
  color: #cd7f32;
}

.heat-info {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #888;
}

.article-count, .member-count {
  display: flex;
  align-items: center;
  gap: 4px;
}

.heat-value .el-icon {
  color: white;
}

.member-count .el-icon {
  color: white;
}

/* 空状态 */
.empty-state {
  padding: 40px 20px;
}

/* Element Plus 组件深色模式样式覆盖 */
:deep(.el-card) {
  --el-card-bg-color: var(--dark-bg-m);
  --el-card-border-color: var(--dark-line-s);
}

:deep(.el-tag) {
  --el-tag-bg-color: var(--dark-bg-m);
  --el-tag-border-color: var(--dark-line-m);
  --el-tag-text-color: var(--dark-content-m);
}

/* :deep(.el-tag.el-tag--danger) {
  --el-tag-bg-color: #4a2a2a;
  --el-tag-border-color: #e6a23c;
  --el-tag-text-color: #e6a23c;
} */

:deep(.el-empty__description p) {
  color: var(--dark-content-m);
}
</style>
