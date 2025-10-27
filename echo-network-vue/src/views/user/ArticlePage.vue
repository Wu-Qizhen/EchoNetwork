<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.27
-->
<script setup>
import ArticleList from "@/views/common/ArticleList.vue";
import {useRoute} from "vue-router";
import {computed} from "vue";

const route = useRoute();
const authorId = computed(() => route.params.id);
</script>

<template>
  <div class="article-page">
    <ArticleList
        :request-config="{
            authorId: authorId
        }"
        :enable-pagination="false"
        :empty-text="'已经到底了'"
    ></ArticleList>
  </div>
</template>

<style scoped>
.article-page {
  width: 100%;
  padding-right: 30px;
  padding-bottom: 100px;
}
</style>
