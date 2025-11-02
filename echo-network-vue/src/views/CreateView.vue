<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.11.02
-->
<script setup>
import {getUserInfo, isAuthorized, logout} from "@/net/index.js";
import router from "@/router/index.js";
import {onMounted, reactive, ref} from "vue";
import XMultiFunBar from "@/aethex/components/XMultiFunBar.vue";
import XBackgroundSpace from "@/aethex/components/XBackgroundSpace.vue";
import {ElMessage} from "element-plus";
import XSpacer from "@/aethex/components/XSpacer.vue";
import {useRoute} from "vue-router";
import XAlert from "@/aethex/components/XAlert.vue";
import {createCircle, getCircle, updateCircle} from "@/net/request.js";

const isLoggedIn = ref(false)
const userInfo = ref(null)

const navItems = ref([
  {text: '创建圈子', id: 'create'},
  {text: '返回首页', id: 'home'}
])

const navButtons = ref([
  {
    type: 'text',
    content: '创建',
    title: '创建圈子',
    dropdownItems: []
  },
  {
    type: 'image',
    imageUrl: userInfo.value?.avatarUrl || '/res/ic_avatar_default.svg',
    alt: '用户头像',
    title: userInfo.value?.nickname || userInfo.value?.username || '用户菜单',
    dropdownItems: [
      {text: '个人资料', id: 'profile'},
      {text: '账户设置', id: 'settings'},
      {text: '退出登录', id: 'logout'}
    ]
  }
])

function checkLoginStatus() {
  isLoggedIn.value = isAuthorized()
  if (isLoggedIn.value) {
    userInfo.value = getUserInfo()
  } else {
    router.push("/")
  }
}

const handleNavItemClick = (data) => {
  const item = data.item;
  if (item.id === 'home') {
    router.push('/')
  }
}

const handleLogoClick = () => {
  router.push('/welcome')
}

const handleButtonClick = (data) => {
  const button = data.button

  if (button.type === 'text') {
    if (button.content === '创建' || button.content === '更新') {
      handleSubmit()
    } /*else if (button.content === '删除') {
      deleteCircleConfirm()
    }*/
  }
}

const handleDropdownItemClick = (data) => {
  const dropdownItems = data.item

  switch (dropdownItems.id) {
    case 'logout':
      userLogout()
      break
    default:
      ElMessage.warning("功能正在施工中")
  }
}

function userLogout() {
  logout(() => {
    isLoggedIn.value = false
    userInfo.value = null
    router.push("/")
  })
}

// 圈子表单数据
const route = useRoute()
const circleData = ref(null)
const circleForm = reactive({
  name: '',
  description: ''
})
const circleFormRef = ref()
const circleId = ref(null)
const submitting = ref(false)
const isEditMode = ref(false)
const canEdit = ref(false)
// const deleteClicked = ref(false)

// 加载圈子数据
const loadCircle = async () => {
  if (!circleId.value) return

  try {
    await getCircle(circleId.value, (response) => {
          circleData.value = response

          // 检查编辑权限：当前用户是否为圈子创建者
          canEdit.value = userInfo.value && userInfo.value.id === response.creator.id

          if (!canEdit.value) {
            ElMessage.error('您没有编辑此圈子的权限')
            router.push('/')
            return
          }

          // 填充表单数据
          circleForm.name = response.name
          circleForm.description = response.description

          // console.log('加载圈子成功：', response)
        },
        (message, code, url) => {
          console.warn(`请求地址：${url}，状态码：${code}，错误信息：${message || '未知错误'}`)
          ElMessage.error(message || '未知错误')
          router.push('/')
        }
    )
  } catch (error) {
    console.error('加载圈子失败：', error)
    ElMessage.error('加载圈子失败')
    await router.push('/')
  }
}

// 更新圈子
const updateCircleData = async () => {
  if (!circleForm.name.trim()) {
    ElMessage.warning('请输入圈子名称')
    return
  }

  if (!circleForm.description.trim()) {
    ElMessage.warning('请输入圈子描述')
    return
  }

  submitting.value = true

  try {
    await updateCircle(
        circleId.value,
        {
          name: circleForm.name.trim(),
          description: circleForm.description.trim()
        },
        () => {
          ElMessage.success('圈子更新成功！')
          router.push(`/circle/${circleId.value}`)
        }
    )
  } catch (error) {
    ElMessage.error('更新失败，请重试')
    console.error('更新圈子失败：', error)
  } finally {
    submitting.value = false
  }
}

// 删除圈子
/*const deleteCircleConfirm = () => {
  // 检查是否已经点击过一次删除按钮
  if (!deleteClicked.value) {
    deleteClicked.value = true
    ElMessage.warning('再次点击删除按钮以确认删除')

    // 3 秒后重置状态
    setTimeout(() => {
      deleteClicked.value = false
    }, 3000)
    return
  }

  // 双击确认删除
  deleteClicked.value = false
  performDelete()
}*/

// 实际执行删除操作
/*const performDelete = async () => {
  try {
    await deleteCircle(
        circleId.value,
        () => {
          ElMessage.success('圈子删除成功')
          router.push('/')
        })
  } catch (error) {
    console.error('删除圈子失败：', error)
    ElMessage.error('删除失败，请重试')
  }
}*/

// 创建圈子
const createCircleData = async () => {
  if (!circleForm.name.trim()) {
    ElMessage.warning('请输入圈子名称')
    return
  }

  if (!circleForm.description.trim()) {
    ElMessage.warning('请输入圈子描述')
    return
  }

  submitting.value = true

  try {
    await createCircle({
      name: circleForm.name.trim(),
      description: circleForm.description.trim()
    }, (response) => {
      ElMessage.success('圈子创建成功！')
      router.push(`/circle/${response.id}`)
    })
  } catch (error) {
    ElMessage.error('创建失败，请重试')
    console.error('创建圈子失败：', error)
  } finally {
    submitting.value = false
  }
}

// 提交表单
const handleSubmit = async () => {
  if (isEditMode.value) {
    await updateCircleData()
  } else {
    await createCircleData()
  }
}

// 根据模式动态设置导航栏
const updateNavBar = () => {
  if (isEditMode.value) {
    navItems.value = [
      {text: '编辑圈子', id: 'edit'},
      {text: '返回首页', id: 'home'}
    ]

    navButtons.value = [
      {
        type: 'text',
        content: '更新',
        title: '更新圈子',
        dropdownItems: []
      },
      /*{
        type: 'text',
        content: '删除',
        title: '删除圈子',
        style: 'danger',
        dropdownItems: []
      },*/
      {
        type: 'image',
        imageUrl: userInfo.value?.avatarUrl || '/res/ic_avatar_default.svg',
        alt: '用户头像',
        title: userInfo.value?.nickname || userInfo.value?.username || '用户菜单',
        dropdownItems: [
          {text: '个人资料', id: 'profile'},
          {text: '账户设置', id: 'settings'},
          {text: '退出登录', id: 'logout'}
        ]
      }
    ]
  } else {
    // 保持原有的创建模式导航栏
    navItems.value = [
      {text: '创建圈子', id: 'create'},
      {text: '返回首页', id: 'home'}
    ]

    navButtons.value = [
      {
        type: 'text',
        content: '创建',
        title: '创建圈子',
        dropdownItems: []
      },
      {
        type: 'image',
        imageUrl: userInfo.value?.avatarUrl || '/res/ic_avatar_default.svg',
        alt: '用户头像',
        title: userInfo.value?.nickname || userInfo.value?.username || '用户菜单',
        dropdownItems: [
          {text: '个人资料', id: 'profile'},
          {text: '账户设置', id: 'settings'},
          {text: '退出登录', id: 'logout'}
        ]
      }
    ]
  }
}

onMounted(() => {
  checkLoginStatus()

  // 检查路由参数，判断是编辑模式还是创建模式
  circleId.value = route.params.id
  isEditMode.value = !!circleId.value

  if (isEditMode.value) {
    // 编辑模式：加载圈子数据
    loadCircle()
  }

  // 更新导航栏
  updateNavBar()
})
</script>

<template>
  <XMultiFunBar
      :nav-items="navItems"
      :nav-buttons="navButtons"
      logo-image="/res/logo_echo_network_with_text.svg"
      :logo-title="'回声网络'"
      :logo-alt="'回声网络'"
      @nav-item-click="handleNavItemClick"
      @logo-click="handleLogoClick"
      @button-click="handleButtonClick"
      @dropdown-item-click="handleDropdownItemClick"
      :show-search="false"
  />

  <XBackgroundSpace>
    <div class="root">
      <div class="form-container frosted-glass">
        <!-- 编辑模式提示 -->
        <div class="page-header">
          <h1 class="page-title">{{ isEditMode ? '编辑圈子' : '创建圈子' }}</h1>
          <p class="page-subtitle">创建圈子，让知识分享更简单</p>
        </div>

        <div>
          <XAlert
              v-if="isEditMode"
              title="编辑模式"
              description="您正在编辑已创建的圈子"
              type="warning"/>
          <XAlert
              v-if="!isEditMode"
              title="创建模式"
              description="创建的圈子不支持删除"
              type="warning"/>
          <XSpacer type="vertical" height="20px"/>
        </div>

        <el-form :model="circleForm" ref="circleFormRef" class="circle-form">
          <!-- 圈子名称 -->
          <div class="form-item">
            <h2>圈子名称</h2>
            <XSpacer height="10px"/>
            <el-input
                v-model="circleForm.name"
                placeholder="请输入圈子名称"
                clearable
                maxlength="20"
                show-word-limit
                class="name-input"
            />
          </div>

          <!-- 圈子描述 -->
          <div class="form-item">
            <h2>圈子描述</h2>
            <XSpacer height="10px"/>
            <el-input
                v-model="circleForm.description"
                type="textarea"
                :rows="6"
                placeholder="请输入圈子描述"
                maxlength="200"
                show-word-limit
                resize="none"
                class="description-input"
            />
          </div>

          <!-- 提交按钮 -->
          <div class="form-actions">
            <div
                @click="router.push('/')"
                class="btn-m red"
            >
              <p class="zh">取消</p>
            </div>

            <div
                v-if="!submitting"
                @click="handleSubmit"
                class="btn-m theme"
            >
              <p class="zh">{{ isEditMode ? '更新圈子' : '创建圈子' }}</p>
            </div>

            <div
                v-if="submitting"
                @click="handleSubmit"
                class="btn-l disable"
            >
              <p class="zh">处理中</p>
            </div>
          </div>
        </el-form>
      </div>
    </div>
  </XBackgroundSpace>
</template>

<style scoped>
.root {
  width: 100%;
  padding: 105px 20px 100px 20px;
  display: flex;
  justify-content: center;
  align-items: start;
}

.form-container {
  width: 100%;
  padding: 30px;
}

/* .page-title {
  color: var(--dark-content-l);
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 30px;
  text-align: center;
} */

.circle-form {
  width: 100%;
}

.page-header {
  text-align: start;
  margin-bottom: 20px;
}

.page-subtitle {
  font-size: 14px;
  color: var(--dark-content-m);
  margin: 0;
}

.form-item {
  margin-bottom: 20px;
}

:deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  box-shadow: none;
}

:deep(.el-input__wrapper:hover) {
  border-color: rgba(255, 255, 255, 0.2);
}

:deep(.el-input__wrapper.is-focus) {
  border-color: var(--theme-color-lighten);
  box-shadow: 0 0 0 1px var(--theme-color-lighten);
}

:deep(.el-textarea__inner) {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  resize: none;
}

:deep(.el-textarea__inner:hover) {
  border-color: rgba(255, 255, 255, 0.2);
}

:deep(.el-textarea__inner:focus) {
  border-color: var(--theme-color-lighten);
}

:deep(.el-input.is-disabled .el-input__wrapper) {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.1);
}

:deep(.el-input__count) {
  background: transparent;
  color: var(--dark-content-s);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 20px;
  margin-top: 30px;
}

@media (max-width: 768px) {
  .form-container {
    padding: 20px;
  }

  .page-title {
    font-size: 24px;
  }

  .form-actions {
    flex-direction: column;
  }
}
</style>
