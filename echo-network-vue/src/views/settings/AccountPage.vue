<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.31
-->
<script setup>
import XAlert from "@/aethex/components/XAlert.vue";
import XSpacer from "@/aethex/components/XSpacer.vue";
import {logout} from "@/net/index.js";
import router from "@/router/index.js";
import {ref} from "vue";
import {ElMessage} from "element-plus";

function resetPassword() {
  logout(() => {
    router.push("/auth/reset")
  })
}

const resetClicked = ref(false)

const goToReset = () => {
  if (!resetClicked.value) {
    resetClicked.value = true
    ElMessage.warning('再次点击重置密码按钮以前往重置页')

    setTimeout(() => {
      resetClicked.value = false
    }, 3000)
    return
  }

  resetClicked.value = false
  resetPassword()
}
</script>

<template>
  <div class="account-page">
    <h1>账户设置</h1>
    <p style="color: var(--dark-content-m); font-size: 14px">更改您的个人账户</p>
    <XSpacer height="20px"/>
    <h2>更改用户名</h2>
    <XSpacer height="20px"/>
    <XAlert
        title="用户名暂不支持修改"
        description=""
        type="warning"
    />
    <XSpacer height="20px"/>

    <h2>更改邮箱</h2>
    <XSpacer height="20px"/>
    <XAlert
        title="邮箱暂不支持修改"
        description=""
        type="warning"
    />
    <XSpacer height="20px"/>

    <h2>更改密码</h2>
    <XSpacer height="20px"/>
    <XAlert
        title="退出登录后可通过忘记密码更改密码"
        description="点击下方按钮前往重置界面"
        type="success"
    />
    <XSpacer height="20px"/>
    <div
        class="btn-m red"
        @click="goToReset"
    >
      <p class="zh">重置密码</p>
    </div>
  </div>
</template>

<style scoped>
.account-page {
  width: 100%;
  margin: 0 auto;
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  background-color: rgba(30, 35, 47, 0.6);
  color: var(--dark-content-m);
}
</style>
