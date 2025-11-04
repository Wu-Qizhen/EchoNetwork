<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.9
-->
<script setup>

import {computed, reactive, ref} from "vue";
import XDivider from "@/aethex/components/XDivider.vue";
import {Check, EditPen, Hide, Lock, Message, User, View} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {get, post} from "@/net";
import {ElMessage} from "element-plus";
import XSpacer from "@/aethex/components/XSpacer.vue";

const form = reactive({
  username: '',
  password: '',
  password_confirm: '',
  email: '',
  code: '',
  agree: false
})

const validatePassword = (rule, value, callback) => {
  if (value === '')
    callback(new Error('请再次输入密码'));
  else if (value !== form.password)
    callback(new Error('两次输入的密码不一致'));
  else callback()
}

let timer;
const coldTime = ref(0)
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
    },
    {
      validator: (rule, value, callback) => {
        // 至少包含数字和字母
        if (!/(?=.*[0-9])(?=.*[a-zA-Z])/.test(value)) {
          callback(new Error('至少包含数字和字母'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ],
  password_confirm: [
    /*{required: true, message: '请再次输入密码', trigger: 'blur'}*/
    {validator: validatePassword, trigger: 'blur'}
  ],
  email: [
    {required: true, message: '请输入邮箱', trigger: 'blur'},
    {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
  ],
  code: [
    {required: true, message: '请输入验证码', trigger: 'blur'},
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

function askCode() {
  if (isEmailValid) {
    coldTime.value = 60
    get(`/api/auth/ask-code?type=register&email=${form.email}`, () => {
      ElMessage.success(`验证码已发送至邮箱 ${form.email}，请注意查收`)
      timer = setInterval(() => {
        if (coldTime.value > 0) {
          coldTime.value--;
        } else {
          clearInterval(timer);
        }
      }, 1000);
    }, (errorMessage) => {
      ElMessage.error(errorMessage || '验证码发送失败');
      coldTime.value = 0;
    })
  } else {
    ElMessage.warning('请输入正确的邮箱地址')
  }
}

const isEmailValid = computed(() => {
  return /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(form.email)
})

function register() {
  if (!form.agree) {
    ElMessage.warning("请先阅读并同意回声网络使用协议和隐私政策")
    return
  }
  formRef.value.validate((valid) => {
    if (valid) {
      const requestData = {
        username: form.username,
        email: form.email,
        password: form.password,
        captcha: form.code
      }
      post('/api/users/register', requestData, () => {
        ElMessage.success('注册成功，欢迎加入回声网络')
        router.push('/auth')
      })
    } else {
      ElMessage.warning('请填写完整信息')
    }
  })
}
</script>

<template>
  <div class="page-container">
    <div class="register-form">
      <p class="theme" style="font-size: 28px">注册</p>
      <XSpacer height="20px"/>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username"
                    maxlength="50"
                    type="text"
                    class="register-input"
                    placeholder="用户名">
            <template #prefix>
              <XSpacer type="horizontal" width="5px"/>
              <el-icon>
                <User/>
              </el-icon>
              <XSpacer type="horizontal" width="5px"/>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="form.password"
                    maxlength="20"
                    :type="showPassword ? 'text' : 'password'"
                    class="register-input spacer-top-ss"
                    placeholder="密码"
                    ref="passwordInput">
            <template #prefix>
              <XSpacer type="horizontal" width="5px"/>
              <el-icon>
                <Lock/>
              </el-icon>
              <XSpacer type="horizontal" width="5px"/>
            </template>
            <template #suffix>
              <el-icon @click="togglePassword" style="cursor: pointer; margin: 0 5px">
                <View v-if="!showPassword"/>
                <Hide v-else/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password_confirm">
          <el-input v-model="form.password_confirm"
                    maxlength="20"
                    type="password"
                    class="register-input spacer-top-ss"
                    placeholder="重复密码">
            <template #prefix>
              <XSpacer type="horizontal" width="5px"/>
              <el-icon>
                <Check/>
              </el-icon>
              <XSpacer type="horizontal" width="5px"/>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="email">
          <el-input v-model="form.email"
                    maxlength="50"
                    type="text"
                    class="register-input spacer-top-ss"
                    placeholder="邮箱">
            <template #prefix>
              <XSpacer type="horizontal" width="5px"/>
              <el-icon>
                <Message/>
              </el-icon>
              <XSpacer type="horizontal" width="5px"/>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="code">
          <el-row :gutter="20" style="width: 370px" class="spacer-top-ss">
            <el-col :span="16">
              <el-input v-model="form.code"
                        style="height: 40px"
                        maxlength="6"
                        type="text"
                        placeholder="验证码">
                <template #prefix>
                  <XSpacer type="horizontal" width="5px"/>
                  <el-icon>
                    <EditPen/>
                  </el-icon>
                  <XSpacer type="horizontal" width="5px"/>
                </template>
              </el-input>
            </el-col>
            <el-col :span="6">
              <!-- TODO 布尔值问题 -->
              <el-button style="height: 40px" type="primary" @click="askCode" :disabled="!isEmailValid||coldTime">
                {{ coldTime > 0 ? `${coldTime} 秒后再试` : "获取验证码" }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>

        <el-col>
          <el-form-item class="spacer-bottom-s">
            <el-checkbox v-model="form.agree">已阅读并同意回声网络 <a href="#">使用协议</a> 和 <a href="#">隐私政策</a>
            </el-checkbox>
          </el-form-item>
        </el-col>
      </el-form>

      <el-button class="register-button" type="primary" @click="register">注册</el-button>

      <x-divider label="已有账号" width="350px"/>

      <el-button class="register-button spacer-bottom-l" type="primary" @click="router.push('/auth')" plain>登录
      </el-button>
    </div>
  </div>
</template>

<style scoped>
a {
  color: white;
  font-size: 14px;
  font-weight: bold;
  text-decoration: none;
}

a:hover {
  color: var(--theme-color-lighten);
  text-decoration: underline;
}

.page-container {
  width: 100%;
}

.register-form {
  width: 100%;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
}

.register-input {
  width: 350px;
  height: 40px;
}

:deep(.el-input__wrapper) {
  --el-input-border-color: var(--dark-line-s);
  --el-input-bg-color: var(--dark-bg-s);
  --el-input-text-color: white;
  --el-input-placeholder-color: var(--dark-content-m);
}

:deep(.el-input__wrapper.is-focus) {
  --el-input-focus-border-color: white;
}

:deep(.el-checkbox__inner) {
  border-color: var(--dark-line-s);
  --el-checkbox-bg-color: var(--dark-bg-s);
}

:deep(.el-checkbox__label) {
  color: var(--dark-content-m);
}

.register-button {
  width: 350px;
  height: 50px;
}
</style>
