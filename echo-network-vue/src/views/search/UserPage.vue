<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.27
-->
<script setup>
/*import UserList from "@/views/common/UserList.vue";
import {getUsers} from "@/net/request.js";
import {useRoute} from "vue-router";
import {computed} from "vue";

const route = useRoute();
const query = route.query;*/
import XAlert from "@/aethex/components/XAlert.vue";
</script>

<template>
  <div class="user-page">
    <XAlert
        title="暂不支持搜索用户"
        type="warning"
    />
  </div>
</template>

<style scoped>
.user-page {
  width: 100%;
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  background-color: rgba(30, 35, 47, 0.6);
  color: var(--dark-content-m);
  -webkit-backdrop-filter: blur(10px);
  backdrop-filter: blur(10px);
}

@media (max-width: 768px) {
  .user-page {
    padding: 20px;
  }
}
</style>
