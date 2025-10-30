<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.27
-->
<script setup>
import RankingList from "@/views/common/RankingList.vue";
import ArticleList from "@/views/common/ArticleList.vue";
</script>

<template>
  <div class="home-page">
    <div class="article-list">
      <ArticleList
          :request-config="{
              sortBy: 'publishTime',
              sortOrder: 'DESC',
              size: 5
          }"
          :enable-pagination="true"
          :empty-text="'暂无更多文章，快去发布一篇吧！'"
      />
    </div>

    <div class="ranking-list">
      <RankingList/>
    </div>
  </div>
</template>

<style scoped>
.home-page {
  width: 100%;
  padding: 105px 80px 100px 80px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
  align-items: start;
}

.article-list {
  width: 60%;
  margin-right: 20px;
}

.ranking-list {
  width: 40%;
}
</style>
