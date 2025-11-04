<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.13
-->
<template>
  <div class="ranking-list-container dark-mode">
    <el-card class="ranking-card">
      <template #header>
        <div class="card-header">
          <div class="header-title">
            <el-icon class="title-icon">
              <Trophy/>
            </el-icon>
            <span>热门榜</span>
          </div>
          <el-tag size="small"
                  style="
                  background-color: #4a2a2a;
                  border-color: #e6a23c;
                  color: #e6a23c;"
          >TOP 10
          </el-tag>
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
      <div v-else class="ranking-list">
        <div
            v-for="(article, index) in rankingList"
            :key="article.id"
            class="ranking-item"
            :class="{
            'top-1': index === 0,
            'top-2': index === 1,
            'top-3': index === 2
          }"
            @click="handleArticleClick(article)"
        >
          <div class="rank-number">
            <span class="number">{{ index + 1 }}</span>
            <el-icon v-if="index < 3" class="crown-icon">
              <GoldMedal v-if="index === 0"/>
              <Medal v-else-if="index === 1"/>
              <Aim v-else/>
            </el-icon>
          </div>

          <div class="article-info">
            <div class="article-title">{{ article.title }}</div>
            <div class="heat-info">
              <span class="heat-value">
                <el-icon><Sunny/></el-icon>
                {{ formatHeat(article.likeCount) }}
              </span>
              <span class="view-count">
                <el-icon><View/></el-icon>
                {{ formatViewCount(article.viewCount) }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="!loading && rankingList.length === 0" class="empty-state">
        <el-empty description="暂无排行数据" :image-size="80"/>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {Aim, GoldMedal, Loading, Medal, Sunny, Trophy, View} from '@element-plus/icons-vue'
import {getArticles} from "@/net/request.js";

// 响应式数据
const rankingList = ref([])
const loading = ref(true)

// 模拟 API 请求
/*const fetchHotRanking = async () => {
  try {
    // 模拟 API 请求延迟
    await new Promise(resolve => setTimeout(resolve, 800))

    // 模拟 API 响应数据
    const mockResponse = {
      code: 200,
      message: "成功",
      data: [
        {id: 1, title: "Vue 3 组合式 API 最佳实践与性能优化指南", heat: 9850, viewCount: 12500},
        {id: 2, title: "深入理解 TypeScript 泛型与类型编程", heat: 8760, viewCount: 11000},
        {id: 3, title: "微前端架构实战：从零搭建企业级应用", heat: 7650, viewCount: 9800},
        {id: 4, title: "Node.js 高并发场景下的性能调优策略", heat: 6540, viewCount: 8500},
        {id: 5, title: "Webpack 5 模块联邦与构建优化实战", heat: 5430, viewCount: 7200},
        {id: 6, title: "CSS Grid 与 Flexbox 现代布局完全指南", heat: 4320, viewCount: 6100},
        {id: 7, title: "Docker 容器化部署与 DevOps 实践", heat: 3210, viewCount: 4900},
        {id: 8, title: "React 18 新特性与并发渲染原理", heat: 2980, viewCount: 3800},
        {id: 9, title: "GraphQL 在前端项目中的工程化实践", heat: 2650, viewCount: 3200},
        {id: 10, title: "前端监控体系搭建与性能分析实战", heat: 2340, viewCount: 2800}
      ],
      timestamp: new Date().toISOString()
    }

    if (mockResponse.code === 200) {
      rankingList.value = mockResponse.data
    } else {
      console.error('获取热度排行失败：', mockResponse.message)
    }
  } catch (error) {
    console.error('API 请求错误：', error)
  } finally {
    loading.value = false
  }
}*/

const fetchHotRanking = async () => {
  try {
    await getArticles({
      sortBy: 'likeCount',
      sortOrder: 'DESC',
      page: 1,
      size: 10
    }, (data) => {
      if (data && data.list) {
        rankingList.value = data.list
      } else {
        console.warn('返回的数据结构不符合预期：', data)
        rankingList.value = [] // 设置为空数组避免后续错误
      }
    })
  } catch (error) {
    console.error('API 请求错误：', error)
  } finally {
    loading.value = false
  }
}

// 处理文章点击
const handleArticleClick = (article) => {
  window.open(
      '/article/' + article.id,
      '_blank'
  )
}

// 格式化热度值
const formatHeat = (heat) => {
  if (heat >= 10000) {
    return (heat / 10000).toFixed(1) + 'W'
  }
  return heat.toString()
}

// 格式化阅读量
const formatViewCount = (count) => {
  if (count >= 10000) {
    return (count / 10000).toFixed(1) + 'W'
  }
  return count.toString()
}

// 组件挂载时获取数据
onMounted(() => {
  fetchHotRanking()
})
</script>

<style scoped>
.ranking-list-container {
  width: 100%;
}

.ranking-card {
  background-color: rgba(30, 35, 47, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  box-shadow: none;
  /* backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px); */
}

.ranking-card:hover {
  box-shadow: 0 10px 20px rgba(255, 255, 255, 0.2);
}

:deep(.ranking-card .el-card__header) {
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
  color: var(--dark-content-m);
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
.ranking-list {
  padding: 0;
}

.ranking-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  margin: 5px 0;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.ranking-item:hover {
  background-color: rgba(45, 45, 45, 0.7);
  border-color: rgba(255, 255, 255, 0.2);
  transform: translateX(4px);
}

/* 前三名特殊样式 */
.ranking-item.top-1 {
  background: linear-gradient(135deg, rgba(249, 176, 148, 0.2) 0%, rgba(255, 223, 124, 0.6) 100%);
  border-color: #e6a23c;
}

.ranking-item.top-2 {
  background: linear-gradient(135deg, #3a3a3a 0%, #2a3a4a 100%);
  border-color: #909399;
}

.ranking-item.top-3 {
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
  color: white;
}

.top-2 .rank-number {
  background: linear-gradient(135deg, #909399 0%, #73767a 100%);
  color: white;
}

.top-3 .rank-number {
  background: linear-gradient(135deg, #b88230 0%, #966c25 100%);
  color: white;
}

/* 其他排名的数字样式 */
.ranking-item:not(.top-1):not(.top-2):not(.top-3) .rank-number {
  background-color: var(--dark-line-s);
  color: var(--dark-content-m);
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

.article-info {
  flex: 1;
  min-width: 0;
}

.article-title {
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
.top-1 .article-title {
  color: #ffdf7c;
}

.top-2 .article-title {
  color: #c0c0c0;
}

.top-3 .article-title {
  color: #cd7f32;
}

.heat-info {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #888;
}

.heat-value, .view-count {
  display: flex;
  align-items: center;
  gap: 4px;
}

.heat-value .el-icon {
  color: white;
}

.view-count .el-icon {
  color: white;
}

/* 空状态 */
.empty-state {
  padding: 10px 0;
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
