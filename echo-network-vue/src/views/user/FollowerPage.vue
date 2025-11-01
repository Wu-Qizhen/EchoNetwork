<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.27
-->
<script setup>
import UserList from "@/views/common/UserList.vue";
import {getUsers} from "@/net/request.js";
import {useRoute} from "vue-router";
import {computed} from "vue";

const route = useRoute();
const userId = computed(() => route.params.id);

const getFollowers = async (params, callback) => {
  try {
    const userId = params.userId
    await getUsers(
        `${userId}/followers`,
        (data) => {
          // 适配组件期望的数据结构
          const responseData = {
            list: data,
            page: params.page || 1,
            totalPages: Math.ceil(data.length / (params.size || 10))
          }
          callback(responseData)
        }
    )
  } catch (error) {
    console.error('获取粉丝列表失败：', error)
    throw error
  }
}
</script>

<template>
  <div class="follower-page">
    <UserList
        :fetch-users-function="getFollowers"
        :request-config="{ userId: userId }"
        empty-text="暂无粉丝"
        no-more-text="没有更多粉丝了"
        :show-stats="true"
    />
  </div>
</template>

<style scoped>
.follower-page {
  width: 100%;
  padding-right: 30px;
  padding-bottom: 100px;
}

@media (max-width: 768px) {
  .follower-page {
    padding-right: 0;
  }
}
</style>
