<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.9
-->
<script setup>
import {reactive, ref} from "vue";
import {Hide, Lock, User, View} from "@element-plus/icons-vue"
import XDivider from "@/aethex/components/XDivider.vue";
import {login} from "@/net/index.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";
import XSpacer from "@/aethex/components/XSpacer.vue";

const form = reactive({
  username: '',
  password: '',
  remember: false,
  agree: false
})

const formRef = ref()
const showPassword = ref(false) // 控制密码显示状态
const passwordInput = ref(null) // 用于获取原生输入框
const rules = {
  username: [
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur'},
    {
      pattern: /^[a-zA-Z0-9\u4e00-\u9fa5\x20]+$/,
      message: '只包含数字、字母、中文和空格',
      trigger: 'blur'
    }
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 4, max: 20, message: '长度在 4 到 20 个字符', trigger: 'blur'},
    {
      pattern: /^[a-zA-Z0-9!@#$%^&*()_+{}:"<>?\-=[\];',./\\|]+$/,
      message: '只包含数字、字母和部分特殊符号',
      trigger: 'blur'
    }
  ]
}

function togglePassword() {
  // 保存当前光标位置
  const input = passwordInput.value.$refs.input;
  const start = input.selectionStart;
  const end = input.selectionEnd;

  // 切换密码显示状态
  showPassword.value = !showPassword.value;

  // 等待输入框重新渲染后恢复光标位置
  setTimeout(() => {
    if (input) {
      input.setSelectionRange(start, end);
    }
  }, 0);
}

function userLogin() {
  if (!form.agree) {
    ElMessage.warning("请先阅读并同意回声网络使用协议和隐私政策")
    return
  }
  formRef.value.validate((valid) => {
        if (valid) {
          login(form.username, form.password, form.remember, () => {
            router.push("/index")
          })
        } else {
          ElMessage.warning("请检查输入")
        }
      }
  )
}
</script>

<template>
  <!--<div class="login-card">-->
  <!--<div class="login-logo">
        <img src="../../../res/logo_echo_network_with_text.svg" alt="">
      </div>-->
  <div class="page-container">
    <div class="login-form">
      <p class="theme" style="font-size: 28px">登录</p>
      <XSpacer height="20px"/>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username"
                    maxlength="50"
                    type="text"
                    class="login-input"
                    placeholder="用户名 / 邮箱 / Echo ID">
            <template #prefix>
              <el-icon style="padding: 0 5px">
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="form.password"
                    maxlength="20"
                    :type="showPassword ? 'text' : 'password'"
                    class="login-input spacer-top-s"
                    placeholder="密码"
                    ref="passwordInput">
            <template #prefix>
              <el-icon style="padding: 0 5px">
                <Lock/>
              </el-icon>
            </template>
            <template #suffix>
              <el-icon @click="togglePassword" style="cursor: pointer; padding: 0 5px">
                <View v-if="!showPassword"/>
                <Hide v-else/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-col>
          <el-form-item style="margin-bottom: 5px">
            <el-checkbox v-model="form.agree">已阅读并同意回声网络 <a
                @click="router.push('/service-terms')">使用协议</a> 和 <a
                @click="router.push('/privacy-policy')">隐私政策</a>
            </el-checkbox>
          </el-form-item>

          <el-row>
            <el-col :span="12" style="text-align:left">
              <el-form-item prop="remember">
                <el-checkbox v-model="form.remember">记住密码</el-checkbox>
              </el-form-item>
            </el-col>

            <el-col :span="12" style="text-align:right">
              <a @click="router.push('/reset')">忘记密码？</a>
            </el-col>
          </el-row>
        </el-col>
      </el-form>

      <el-button class="login-button" type="primary" @click="userLogin">登录</el-button>

      <x-divider label="没有账号" width="350px"/>

      <el-button class="login-button spacer-bottom-l" type="primary" @click="router.push('/register')" plain>注册
      </el-button>
    </div>
  </div>
  <!--</div>-->
</template>

<style scoped>
a {
  color: #fff;
  font-size: 14px;
  font-weight: bold;
  text-decoration: none;
}

a:hover {
  color: #409eff;
  text-decoration: underline;
}

/* .login-card {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  margin: 0 0;
  text-align: center;
  justify-content: flex-start;
  align-content: center;
  align-items: center;
} */

.page-container {
  width: 100%;
}

.login-form {
  width: 100%;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
}

/* .login-logo {
  margin: 30px;
  align-self: flex-start;
}

.login-logo img {
  height: 60px;
} */

.login-input {
  width: 350px;
  height: 50px;
}

:deep(.el-input__wrapper) {
  --el-input-border-color: #43454a;
  --el-input-bg-color: #1e1f22;
  --el-input-text-color: #fff;
  --el-input-placeholder-color: #a8abb2;
}

:deep(.el-input__wrapper.is-focus) {
  --el-input-focus-border-color: #fff;
  /* --el-input-focus-bg-color: #43454a; */
}

:deep(.el-checkbox__inner) {
  border-color: #43454a;
  --el-checkbox-bg-color: #1e1f22;
  /* --el-checkbox-text-color: #fff; */
}

:deep(.el-checkbox__label) {
  color: #a8abb2;
}

/* :deep(.el-checkbox__input.is-checked) {

} */

/* :deep(.el-checkbox__input.is-checked +){

} */

/* :deep(.el-checkbox__input.is-checked + .el-checkbox__label) {
  --el-checkbox-checked-text-color: #fff;
} */

.login-button {
  width: 350px;
  height: 50px;
}
</style>
