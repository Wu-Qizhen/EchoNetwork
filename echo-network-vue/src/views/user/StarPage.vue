<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.27
-->
<script setup>
import ArticleList from "@/views/common/ArticleList.vue";
import {useRoute} from "vue-router";

const route = useRoute();
</script>

<template>
  <div class="star-page">
    <ArticleList
        article-type='starred'
    ></ArticleList>
  </div>
</template>

<style scoped>
.star-page {
  width: 100%;
  padding-right: 30px;
  padding-bottom: 100px;
}

@media (max-width: 768px) {
  .star-page {
    padding-right: 0;
  }
}
</style>
