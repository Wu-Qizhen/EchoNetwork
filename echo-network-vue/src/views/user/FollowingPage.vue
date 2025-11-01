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

const getFollowings = async (params, callback) => {
  try {
    const userId = params.userId
    await getUsers(
        `${userId}/following`,
        (data) => {
          const responseData = {
            list: data,
            page: params.page || 1,
            totalPages: Math.ceil(data.length / (params.size || 10))
          }
          callback(responseData)
        }
    )
  } catch (error) {
    console.error('获取关注列表失败：', error)
    throw error
  }
}
</script>

<template>
  <div class="following-page">
    <UserList
        :fetch-users-function="getFollowings"
        :request-config="{ userId: userId }"
        empty-text="暂无关注"
        no-more-text="没有更多关注了"
        :show-stats="true"
    />
  </div>
</template>

<style scoped>
.following-page {
  width: 100%;
  padding-right: 30px;
  padding-bottom: 100px;
}

@media (max-width: 768px) {
  .following-page {
    padding-right: 0;
  }
}
</style>
