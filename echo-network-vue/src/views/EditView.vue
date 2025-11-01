<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.21
-->
<script setup>
import {getUserInfo, isAuthorized, logout} from "@/net/index.js";
import router from "@/router/index.js";
import {onMounted, reactive, ref} from "vue";
import XMultiFunBar from "@/aethex/components/XMultiFunBar.vue";
import XBackgroundSpace from "@/aethex/components/XBackgroundSpace.vue";
import {ElMessage} from "element-plus";
import {Plus} from "@element-plus/icons-vue";
import EditorPage from "@/views/edit/EditorPage.vue";
import XSpacer from "@/aethex/components/XSpacer.vue";
import {
  getArticle,
  getCircles,
  publishArticle,
  updateArticle as updateArticleApi,
  deleteArticle as deleteArticleApi
} from "@/net/request.js";
import {useRoute} from "vue-router";
import XAlert from "@/aethex/components/XAlert.vue";

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
    if (button.content === '发布' || button.content === '更新') {
      handlePublish()
    } else if (button.content === '删除') {
      deleteArticle()
    }
  }
}

const handleDropdownItemClick = (data) => {
  const dropdownItems = data.item

  switch (dropdownItems.id) {
    case 'logout':
      userLogout()
      break
    case 'update':
      handlePublish()
      break
    case 'draft':
      ElMessage.warning("功能正在施工中")
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

// 文章表单数据
const route = useRoute()
const articleData = ref(null)
const articleForm = reactive({
  title: '',
  content: '',
  circleId: null,
  tags: [],
  status: 0
})
const articleFormRef = ref()
const articleId = ref(null)
const circleOptions = ref([])
const circleLoading = ref(false)
const circleSearchTimer = ref(null)
const tagInput = ref('')
const publishing = ref(false)
const isEditMode = ref(false)
const canEdit = ref(false)
const deleteClicked = ref(false)

// 加载文章数据
const loadArticle = async () => {
  if (!articleId.value) return

  try {
    await getArticle(articleId.value, (response) => {
          articleData.value = response

          // 检查编辑权限：当前用户是否为文章作者
          canEdit.value = userInfo.value && userInfo.value.id === response.author.id

          if (!canEdit.value) {
            ElMessage.error('您没有编辑此文章的权限')
            router.push('/')
            return
          }

          // 填充表单数据
          articleForm.title = response.title
          articleForm.content = response.content
          articleForm.circleId = response.circle?.id || null
          articleForm.tags = response.tags.map(tag => tag.name)
          articleForm.status = response.status

          console.log('加载文章成功：', response)
          console.log('文章数据：', articleForm)
        },
        (message, code, url) => {
          console.warn(`请求地址：${url}，状态码：${code}，错误信息：${message || '未知错误'}`)
          ElMessage.error(message || '未知错误')
          router.push('/')
        }
    )
  } catch (error) {
    console.error('加载文章失败：', error)
    ElMessage.error('加载文章失败')
    await router.push('/')
  }
}

// 更新文章
const updateArticle = async () => {
  if (!articleForm.title.trim()) {
    ElMessage.warning('请输入文章标题')
    return
  }

  if (isContentEmpty(articleForm.content)) {
    ElMessage.warning('请输入文章内容')
    return
  }

  publishing.value = true

  try {
    await updateArticleApi(
        articleId.value,
        {
          title: articleForm.title.trim(),
          content: articleForm.content,
          circleId: articleForm.circleId || null,
          tags: articleForm.tags,
          status: articleForm.status
        },
        () => {
          ElMessage.success('文章更新成功！')
          router.push(`/article/${articleId.value}`)
        }
    )
  } catch (error) {
    ElMessage.error('更新失败，请重试')
    console.error('更新文章失败：', error)
  } finally {
    publishing.value = false
  }
}

// 删除文章
/*const deleteArticle = () => {
  ElMessageBox.confirm(
      '确定要删除这篇文章吗？此操作不可恢复。',
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
        confirmButtonClass: 'el-button--danger'
      }
  ).then(async () => {
    try {
      await deleteArticleApi(
          articleId.value,
          () => {
            ElMessage.success('文章删除成功')
            router.push('/')
          })
    } catch (error) {
      console.error('删除文章失败：', error)
      ElMessage.error('删除失败，请重试')
    }
  }).catch(() => {
    // 用户取消删除
  })
}*/

const deleteArticle = () => {
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
}

// 实际执行删除操作
const performDelete = async () => {
  try {
    await deleteArticleApi(
        articleId.value,
        () => {
          ElMessage.success('文章删除成功')
          router.push('/')
        })
  } catch (error) {
    console.error('删除文章失败：', error)
    ElMessage.error('删除失败，请重试')
  }
}

// 远程搜索圈子
const searchCircles = (keyword) => {
  if (circleSearchTimer.value) {
    clearTimeout(circleSearchTimer.value)
  }

  circleSearchTimer.value = setTimeout(async () => {
    circleLoading.value = true
    try {
      // 调用搜索接口，限制返回 10 条结果
      await getCircles({
        keyword: encodeURIComponent(keyword),
        page: 1,
        size: 10
      }, (data) => {
        if (data && data.list) {
          circleOptions.value = [
            {id: null, name: '无'},
            ...data.list.map(circle => ({
              id: circle.id,
              name: circle.name,
              description: circle.description
            }))
          ]
        } else {
          console.warn('返回的数据结构不符合预期：', data)
          circleOptions.value = [{id: null, name: '无'}]
        }
      })
    } catch (error) {
      console.error('圈子搜索失败：', error)
      ElMessage.error('圈子搜索失败')
      // 确保至少有无选项
      circleOptions.value = [{id: null, name: '无'}]
    } finally {
      circleLoading.value = false
    }
  }, 800) // 防抖延迟
}

// 下拉框打开时加载默认数据（用户加入的圈子）
const loadDefaultCircles = async () => {
  if (circleOptions.value.length > 0) return // 避免重复加载

  circleLoading.value = true
  try {
    // 获取当前用户加入的圈子
    await getCircles({
      userId: userInfo.value.id,
      page: 1,
      size: 10
    }, (data) => {
      if (data && data.list) {
        circleOptions.value = [
          {id: null, name: '无'},
          ...data.list.map(circle => ({
            id: circle.id,
            name: circle.name,
            description: circle.description
          }))
        ]
      } else {
        console.warn('返回的数据结构不符合预期：', data)
        circleOptions.value = [{id: null, name: '无'}]
      }
    })
  } catch (error) {
    console.error('加载默认圈子失败：', error)
    // 设置默认选项
    circleOptions.value = [{id: null, name: '无'}]
  } finally {
    circleLoading.value = false
  }
}

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
  if (!articleForm.title.trim()) {
    ElMessage.warning('请输入文章标题')
    return
  }

  if (isContentEmpty(articleForm.content)) {
    ElMessage.warning('请输入文章内容')
    return
  }

  publishing.value = true

  try {
    if (isEditMode.value) {
      // 编辑模式：更新文章
      await updateArticle()
    } else {
      // 创建模式：发布新文章
      await publishArticle({
        title: articleForm.title.trim(),
        content: articleForm.content,
        circleId: articleForm.circleId || null,
        tags: articleForm.tags,
        status: 1
      }, () => {
        ElMessage.success('文章发布成功！')
        router.push('/')
      })
    }
  } catch (error) {
    ElMessage.error(isEditMode.value ? '更新失败，请重试' : '发布失败，请重试')
    console.error(`${isEditMode.value ? '更新' : '发布'}文章失败：`, error)
  } finally {
    publishing.value = false
  }
}

// 根据模式动态设置导航栏
const updateNavBar = () => {
  if (isEditMode.value) {
    navItems.value = [
      {text: '编辑文章', id: 'edit'},
      {text: '返回首页', id: 'home'}
    ]

    navButtons.value = [
      {
        type: 'text',
        content: '更新',
        title: '更新文章',
        dropdownItems: [
          {text: '立即更新', id: 'update'},
          {text: '存草稿', id: 'draft'}
        ]
      },
      {
        type: 'text',
        content: '删除',
        title: '删除文章',
        style: 'danger',
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
  } else {
    // 保持原有的创建模式导航栏
    navItems.value = [
      {text: '写文章', id: 'write'},
      {text: '返回首页', id: 'home'}
    ]

    navButtons.value = [
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
  articleId.value = route.params.id
  isEditMode.value = !!articleId.value

  if (isEditMode.value) {
    // 编辑模式：加载文章数据和用户圈子
    Promise.all([
      loadArticle(),
      loadDefaultCircles()
    ])
  } else {
    // 创建模式：只加载用户圈子
    loadDefaultCircles()
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
      <div class="editor frosted-glass">
        <!-- 编辑模式提示 -->
        <div v-if="isEditMode">
          <XSpacer type="vertical" height="10px"/>
          <XAlert title="编辑模式"
                  description="您正在编辑已发布的文章"
                  type="warning"/>
          <XSpacer type="vertical" height="10px"/>
        </div>

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
            <!--<el-form-item label="圈子" prop="circleId" style="width: 30%">
                  <el-select
                      v-model="articleForm.circleId"
                      placeholder="选择圈子（可选）"
                      filterable
                      clearable
                      popper-class="custom-select-dropdown"
                  >
                    &lt;!&ndash;:teleported="false"&ndash;&gt;
                    <el-option
                        v-for="circle in circleOptions"
                        :key="circle.id"
                        :label="circle.name"
                        :value="circle.id"
                    />
                  </el-select>
                </el-form-item>-->
            <el-form-item label="圈子" prop="circleId" class="operation-selector">
              <el-select
                  v-model="articleForm.circleId"
                  placeholder="选择圈子（可选）"
                  filterable
                  remote
                  clearable
                  :remote-method="searchCircles"
                  :loading="circleLoading"
                  @focus="loadDefaultCircles"
                  popper-class="custom-select-dropdown"
              >
                <el-option
                    v-for="circle in circleOptions"
                    :key="circle.id"
                    :label="circle.name"
                    :value="circle.id"
                    :title="circle.name || '未知圈子'"
                >
                  <div class="circle-option">
                    <span class="circle-name">{{ circle.name }}</span>
                  </div>
                </el-option>
              </el-select>
            </el-form-item>

            <!-- 标签输入 -->
            <el-form-item label="标签" prop="tags" class="operation-selector">
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

.operation-selector {
  width: 30%;
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
  font-weight: bold;
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

/* 下拉框样式优化 */
/* .custom-select-dropdown .el-select-dropdown__item {
  color: #a8abb2; !* 选项文字颜色 *!
}

.custom-select-dropdown .el-select-dropdown__item:hover {
  background-color: rgba(255, 255, 255, 0.2) !important; !* 选项悬停背景色 *!
  color: #fff !important; !* 选项悬停文字颜色 *!
} */

:deep(.custom-select-dropdown .el-select-dropdown__item:hover) {
  background-color: rgba(255, 255, 255, 0.2) !important;
  color: #fff !important;
}

:deep(.custom-select-dropdown .el-select-dropdown__item.is-selected) {
  background-color: rgba(255, 255, 255, 0.1) !important;
  color: var(--theme-color-lighten) !important;
}

@media (max-width: 768px) {
  .editor {
    padding: 20px;
  }

  .operation {
    flex-direction: column;
    gap: 0;
  }

  .operation-selector {
    width: 100%;
  }
}
</style>
