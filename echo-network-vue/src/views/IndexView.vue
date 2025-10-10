<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.9
-->
<script setup>
import {logout} from "@/net/index.js";
import router from "@/router/index.js";

function userLogout() {
  logout(() => {
    router.push("/")
  })
}
</script>

<template>
  <div class="root">
    <el-button @click="userLogout">退出登录</el-button>
  </div>
</template>

<style scoped>
.root {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
