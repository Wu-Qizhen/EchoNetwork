<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.27
-->
<script setup>
import {useRoute} from "vue-router";
import {computed} from "vue";
import CircleList from "@/views/common/CircleList.vue";
import RecommendList from "@/views/common/RecommendList.vue";

const route = useRoute();
const circleId = computed(() => route.params.id);
</script>

<template>
  <div class="circle-page">
    <div class="circle-list">
      <CircleList
          :request-config="{
           sortBy: 'createTime',
           sortOrder: 'DESC',
           size: 5
        }"
          :enable-pagination="true"
          :empty-text="'已经到底了'"
      ></CircleList>
    </div>

    <div class="recommend-list">
      <RecommendList></RecommendList>
    </div>
  </div>
</template>

<style scoped>
.circle-page {
  width: 100%;
  padding: 105px 80px 100px 80px;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
  align-items: start;
}

.circle-list {
  width: 60%;
  margin-right: 20px;
}

.recommend-list {
  width: 40%;
}

@media (max-width: 768px) {
  .circle-page {
    width: 100%;
    padding: 105px 20px 0 20px;
    display: flex;
    flex-direction: column;
    justify-content: start;
    align-items: center;
  }

  .circle-list {
    width: 100%;
    margin-right: 0;
  }

  .recommend-list {
    display: none;
  }
}
</style>
