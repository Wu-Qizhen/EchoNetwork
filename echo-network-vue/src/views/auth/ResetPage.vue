<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.9
-->
<script setup>
import {computed, reactive, ref} from "vue";
import {Check, EditPen, Hide, Lock, Message, View} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {get, post} from "@/net";
import {ElMessage} from "element-plus";
import XSpacer from "@/aethex/components/XSpacer.vue";

const active = ref(0)
const form = reactive({
  password: '',
  password_confirm: '',
  email: '',
  code: ''
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
    get(`/api/auth/ask-code?type=reset&email=${form.email}`, () => {
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

function confirmReset() {
  formRef.value.validate((valid) => {
        if (valid) {
          post('/api/auth/reset-confirm', {
                email: form.email,
                code: form.code
              },
              () => active.value++)
        }
      }
  )
}

function doReset() {
  formRef.value.validate((valid) => {
    if (valid) {
      post('/api/auth/reset-password',
          {...form},
          () => {
            ElMessage.success('密码重置成功，请重新登录')
            router.push('/auth')
          })
    }
  })
}
</script>

<template>
  <div class="page-container">
    <div class="reset-form">
      <p class="theme" style="font-size: 28px">重置密码</p>
      <el-steps :active="active"
                finish-status="success"
                align-center
                class="steps-bar">
        <el-step title="验证电子邮件" :icon="Message"></el-step>
        <el-step title="重新设置密码" :icon="Lock"></el-step>
      </el-steps>
      <div v-if="active===0" class="spacer-top-m">
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="email">
            <el-input v-model="form.email"
                      maxlength="50"
                      type="text"
                      class="reset-input"
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
            <el-row :gutter="20" style="width: 370px" class="spacer-top-s">
              <el-col :span="16">
                <el-input v-model="form.code"
                          style="height: 50px"
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
                <el-button style="height: 50px" type="primary" @click="askCode" :disabled="!isEmailValid||coldTime">
                  {{ coldTime > 0 ? `${coldTime} 秒后再试` : "获取验证码" }}
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>

        <div>
          <el-button class="reset-button spacer-top-s" type="primary" @click="confirmReset">下一步</el-button>
        </div>

        <div>
          <el-button
              class="reset-button spacer-top-m"
              type="primary"
              @click="router.push('/auth')"
              plain>
            返回登录
          </el-button>
        </div>
      </div>

      <div v-if="active===1" class="spacer-top-m">
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="password">
            <el-input v-model="form.password"
                      maxlength="20"
                      :type="showPassword ? 'text' : 'password'"
                      class="reset-input"
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
                      class="reset-input spacer-top-s"
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
        </el-form>

        <div>
          <el-button class="reset-button spacer-top-s" type="primary" @click="doReset">立即重置</el-button>
        </div>

        <div>
          <el-button class="reset-button spacer-top-m" type="primary" @click="active--" plain>上一步</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  width: 100%;
}

.steps-bar {
  width: 350px;
}

.steps-bar :deep(.el-step__icon) {
  background-color: transparent;
}

.steps-bar :deep(.el-step__head.is-process) {
  background-color: transparent;
  border-color: transparent;
}

.steps-bar ::v-deep .el-step__title.is-process {
  color: #409eff;
  font-weight: bold;
}

.steps-bar :deep(.el-step__head.is-wait) {
  background-color: transparent;
  border-color: gray;
}

.steps-bar ::v-deep .el-step__title.is-wait {
  color: gray;
}

.steps-bar :deep(.el-step__head.is-success) {
  background-color: transparent;
  border-color: #69aa65;
}

.steps-bar ::v-deep .el-step__title.is-success {
  color: #69aa65;
}

.steps-bar :deep(.el-step__icon-inner) {
  color: #409eff;
}

.steps-bar :deep(.is-wait .el-step__icon-inner) {
  color: gray;
}

.steps-bar :deep(.is-success .el-step__icon-inner) {
  color: #69aa65;
}

.steps-bar ::v-deep .el-step__line {
  margin: 0 30px;
  background-color: gray;
}

.reset-form {
  width: 100%;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
}

.reset-input {
  width: 350px;
  height: 50px;
}

.reset-button {
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
}
</style>
