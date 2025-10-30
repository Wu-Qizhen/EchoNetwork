<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.27
-->
<script setup>
import {useRoute} from "vue-router";
import {computed} from "vue";
import CircleCard from "@/views/common/CircleCard.vue";
import ArticleList from "@/views/common/ArticleList.vue";
import XSpacer from "@/aethex/components/XSpacer.vue";

const route = useRoute();
const circleId = computed(() => route.params.id);
</script>

<template>
  <div class="circle-page">
    <CircleCard
        :circle-id=circleId
    ></CircleCard>

    <XSpacer height="20px"/>

    <ArticleList
        :request-config="{
          circleId: circleId,
          enablePagination: false
        }"
    ></ArticleList>
  </div>
</template>

<style scoped>
.circle-page {
  width: 100%;
  padding: 105px 20px 100px 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: start;
}
</style>
