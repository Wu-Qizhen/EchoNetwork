<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.22
-->
<template>
  <div>
    <UserProfile :userData="userData"/>
  </div>
</template>

<script setup>
import UserProfile from "@/views/user/UserProfile.vue";

const userData = {
  id: 123,
  username: "Test User",
  email: "test@example.com",
  phone: "13800138000",
  nickname: "测试用户",
  bio: "这是一个个人简介",
  avatarUrl: "../../res/logo_code_intellix.png",
  createTime: "2024-01-01T00:00:00Z",
  lastLoginTime: "2024-01-15T10:30:00Z",
  status: 0,
  role: 0,
  followerCount: 150,
  followingCount: 85,
  articleCount: 42,
  isFollowing: false
}
</script>

<style scoped>

</style>
