<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.27
-->
<script setup>
import {useRoute} from "vue-router";
import {computed} from "vue";
import CircleList from "@/views/common/CircleList.vue";

const route = useRoute();
const userId = computed(() => route.params.id);
</script>

<template>
  <div class="circle-page">
    <CircleList
        :request-config="{
           userId: userId
        }"
        :enable-pagination="true"
        :empty-text="'已经到底了'"
    ></CircleList>
  </div>
</template>

<style scoped>
.circle-page {
  width: 100%;
  padding-right: 30px;
  padding-bottom: 100px;
}

@media (max-width: 768px) {
  .circle-page {
    padding-right: 0;
  }
}
</style>
