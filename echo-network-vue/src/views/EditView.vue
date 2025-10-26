<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.21
-->
<script setup>
import {getUserInfo, isAuthorized, logout, post} from "@/net/index.js";
import router from "@/router/index.js";
import {onMounted, reactive, ref} from "vue";
import XMultiFunBar from "@/aethex/components/XMultiFunBar.vue";
import XBackgroundSpace from "@/aethex/components/XBackgroundSpace.vue";
import {ElMessage} from "element-plus";
import {Plus} from "@element-plus/icons-vue";
import EditorPage from "@/views/edit/EditorPage.vue";
import XSpacer from "@/aethex/components/XSpacer.vue";

const isLoggedIn = ref(false)
const userInfo = ref(null)

const navItems = ref([
  {text: '写文章', id: 'write'},
  {text: '返回首页', id: 'home'}
])

const navButtons = ref([
  {
    type: 'text',
    content: '发布',
    title: '立即发布',
    dropdownItems: [
      {text: '立即发布', id: 'publish'},
      {text: '存草稿', id: 'draft'}
    ]
  },
  {
    type: 'image',
    imageUrl: userInfo.value?.avatarUrl || './res/ic_avatar_default.svg',
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

onMounted(() => {
  checkLoginStatus()
})

const handleNavItemClick = (data) => {
  const item = data.item;
  if (item.id === 'about') {
    router.push('/welcome')
  }
}

const handleLogoClick = () => {
  router.push('/welcome')
}

const handleButtonClick = (data) => {
  const button = data.button

  if (button.type === 'text' && button.content === '发布') {
    handlePublish()
  }
}

const handleDropdownItemClick = (data) => {
  const dropdownItems = data.item;

  switch (dropdownItems.id) {
    case 'logout':
      userLogout()
      break
    default:
      console.log('点击了菜单项：', dropdownItems.id)
  }
}

function userLogout() {
  logout(() => {
    isLoggedIn.value = false
    userInfo.value = null
    router.push("/")
  })
}

// 文章表单数据
const articleForm = reactive({
  title: '',
  content: '',
  circleId: null,
  tags: [],
  status: 0
})

const tagInput = ref('')
const publishing = ref(false)
const articleFormRef = ref()

// 模拟圈子数据 - 实际应从后端获取
const circleOptions = ref([
  {id: null, name: '无'},
  {id: 1, name: '技术交流'},
  {id: 2, name: '生活分享'},
  {id: 3, name: '学习笔记'},
  {id: 4, name: '问题求助'}
])

// 添加标签
const addTag = () => {
  if (tagInput.value.trim() && !articleForm.tags.includes(tagInput.value.trim())) {
    articleForm.tags.push(tagInput.value.trim())
    tagInput.value = ''
  }
}

// 删除标签
const removeTag = (index) => {
  articleForm.tags.splice(index, 1)
}

function isContentEmpty(content) {
  // 替换所有 &nbsp; 为空格
  const contentWithSpaces = content.replace(/&nbsp;/g, ' ');

  // 移除所有 HTML 标签（包括 <br> 和 <p>）
  const plainText = contentWithSpaces.replace(/<[^>]+>/g, '');

  // 检查是否全是空白字符（空格、换行、制表符等）
  return /^\s*$/.test(plainText);
}

// 发布文章
const handlePublish = async () => {
  // console.log('发布文章：', articleForm)
  if (!articleForm.title.trim()) {
    ElMessage.warning('请输入文章标题')
    return
  }

  if (isContentEmpty(articleForm.content)) {
    ElMessage.warning('请输入文章内容');
    return;
  }

  publishing.value = true

  try {
    await publishArticle({
      title: articleForm.title.trim(),
      content: articleForm.content,
      circleId: articleForm.circleId || null,
      tags: articleForm.tags,
      status: 1 // 直接发布，设置为 1
    }, () => {
      ElMessage.success('文章发布成功！')
      router.push('/')
    })

  } catch (error) {
    ElMessage.error('发布失败，请重试')
    console.error('发布文章失败：', error)
  } finally {
    publishing.value = false
  }
}

function publishArticle(articleData, success, failure) {
  post("/api/articles", articleData, success, failure)
}
</script>

<template>
  <XMultiFunBar
      :nav-items="navItems"
      :nav-buttons="navButtons"
      logo-image="./res/logo_echo_network_with_text.svg"
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
      <div class="editor frosted-glass">
        <el-form :model="articleForm" ref="articleFormRef">
          <!-- 标题输入框 -->
          <el-form-item prop="title">
            <el-input
                v-model="articleForm.title"
                placeholder="文章标题"
                clearable
                class="title-input"
            />
          </el-form-item>

          <div class="operation">
            <!-- 圈子选择 -->
            <el-form-item label="圈子" prop="circleId" style="width: 30%">
              <el-select
                  v-model="articleForm.circleId"
                  placeholder="选择圈子（可选）"
                  filterable
                  clearable
                  popper-class="custom-select-dropdown"
              >
                <!--:teleported="false"-->
                <el-option
                    v-for="circle in circleOptions"
                    :key="circle.id"
                    :label="circle.name"
                    :value="circle.id"
                />
              </el-select>
            </el-form-item>

            <!-- 标签输入 -->
            <el-form-item label="标签" prop="tags" style="width: 30%">
              <el-input
                  v-model="tagInput"
                  placeholder="输入标签后按回车添加"
                  maxlength="20"
                  @keyup.enter="addTag"
                  clearable
              >
                <template #suffix>
                  <el-tooltip content="按回车添加标签">
                    <el-icon>
                      <Plus/>
                    </el-icon>
                  </el-tooltip>
                </template>
              </el-input>
              <div class="tags-container">
                <el-tag
                    v-for="(tag, index) in articleForm.tags"
                    :key="index"
                    closable
                    @close="removeTag(index)"
                    class="tag-item"
                >
                  {{ tag }}
                </el-tag>
              </div>
            </el-form-item>
          </div>

          <!-- 正文区域 -->
          <!-- TODO 样式栏冻结-->
          <EditorPage v-model="articleForm.content"></EditorPage>
        </el-form>
        <XSpacer height="20px"/>
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

.editor {
  width: 100%;
  padding: 20px 30px;
}

.operation {
  width: 100%;
  display: flex;
  align-items: start;
  gap: 20px;
}

.tags-container {
  margin-top: 10px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

:deep(.el-tag) {
  --el-tag-bg-color: var(--dark-bg-s);
  --el-tag-border-color: var(--dark-line-m);
  --el-tag-text-color: var(--dark-content-m);
}

:deep(.el-card) {
  --el-card-bg-color: #2d2d2d;
  --el-card-border-color: #444;
}

:deep(.el-form-item__label) {
  color: #fff;
}

/* 设置输入框 */
:deep(.el-input__inner) {
  color: #fff;
}

:deep(.el-input__inner)::placeholder {
  color: #a8abb2;
}

:deep(.title-input) {
  font-size: 28px;
  height: 50px;
}

:deep(.title-input .el-input__wrapper) {
  box-shadow: none !important;
  border-radius: 0 !important;
  padding-left: 0 !important;
  background: transparent !important;
}

:deep(.title-input .el-input__inner) {
  height: 50px;
}

:deep(.title-input .el-input__clear) {
  font-size: 20px; /* 清除按钮图标大小 */
  width: 20px; /* 清除按钮宽度 */
  height: 20px; /* 清除按钮高度 */
}

/* 设置标签 */
:deep(.el-tag .el-tag__close:hover) {
  background-color: #555 !important;
}

:deep(.el-tag .el-tag__close:focus) {
  background-color: #555 !important;
}

:deep(.el-tag .el-tag__close:active) {
  background-color: #666 !important;
}

/* 设置下拉框 */
/* TODO 聚焦边框颜色 */

.custom-select-dropdown .el-select-dropdown__item {
  color: #a8abb2; /* 选项文字颜色 */
}

.custom-select-dropdown .el-select-dropdown__item:hover {
  background-color: rgba(255, 255, 255, 0.2) !important; /* 选项悬停背景色 */
  color: #fff !important; /* 选项悬停文字颜色 */
}
</style>
