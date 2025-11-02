<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.9
-->
<script setup>
import {getUserInfo, isAuthorized, logout} from "@/net/index.js";
import router from "@/router/index.js";
import {computed, onMounted, ref} from "vue";
import XMultiFunBar from "@/aethex/components/XMultiFunBar.vue";
import XBackgroundSpace from "@/aethex/components/XBackgroundSpace.vue";
import {ElMessage} from "element-plus";

const isLoggedIn = ref(false)
const userInfo = ref(null)
// const userAvatar = ref(null)

const navItems = ref([
  {text: '首页', id: 'home'},
  {text: '圈子', id: 'circle'},
  {text: '留言板', id: 'board'},
  {text: '关于', id: 'about'}
])

// 根据登录状态生成导航按钮
const navButtons = computed(() => {
  if (isLoggedIn.value) {
    // 已登录状态
    return [
      /*{
        type: 'icon',
        svgUrl: '/res/ic_message.svg',
        alt: '消息中心',
        title: '消息中心',
        dropdownItems: [
          {text: '收件箱', id: 'inbox'},
          {text: '已发送', id: 'sent'},
          {text: '草稿箱', id: 'drafts'}
        ]
      },*/
      {
        type: 'text',
        content: '创建',
        title: '创建圈子',
        dropdownItems: [
          {text: '创建圈子', id: 'create'}
        ]
      },
      {
        type: 'text',
        content: '发布',
        title: '发布文章',
        dropdownItems: [
          {text: '写文章', id: 'write'}
        ]
      },
      {
        type: 'image',
        imageUrl: userInfo.value?.avatarUrl || '/res/ic_avatar_default.svg',
        alt: '用户头像',
        title: '用户菜单',
        dropdownItems: [
          {text: '个人资料', id: 'profile'},
          {text: '账户设置', id: 'settings'},
          {text: '退出登录', id: 'logout'}
        ]
      }
    ]
  } else {
    // 未登录状态
    return [
      {
        type: 'text',
        content: '登录',
        title: '登录账号',
        dropdownItems: [
          {text: '用户登录', id: 'login'},
          {text: '注册账号', id: 'register'}
        ]
      }
    ]
  }
})

// 检查登录状态
function checkLoginStatus() {
  isLoggedIn.value = isAuthorized()
  if (isLoggedIn.value) {
    userInfo.value = getUserInfo()
    // console.log(userInfo.value)
  }
}

/*function getUserAvatar() {
  getUser(userInfo.value.id, (data) => {
    userAvatar.value = data.avatarUrl
  })
}*/

// 初始化时检查登录状态
onMounted(() => {
  checkLoginStatus()
  // getUserAvatar()
})

const handleNavItemClick = (data) => {
  const item = data.item;
  if (item.id === 'about') {
    router.push('/welcome')
  } else if (item.id === 'circle') {
    router.push('/circle')
  } else if (item.id === 'board') {
    ElMessage.warning('功能正在施工中')
    setTimeout(() => {
      router.go(0)
    }, 1000)
  } else {
    router.push('/')
  }
}

const handleLogoClick = () => {
  router.push('/welcome')
}

const handleButtonClick = (data) => {
  const button = data.button

  if (button.type === 'text' && button.content === '登录') {
    router.push('/auth/login')
  } else if (button.type === 'text' && button.title === '创建圈子') {
    window.open(
        '/circle/create',
        '_blank'
    )
  } else if (button.type === 'text' && button.title === '发布文章') {
    window.open(
        '/editor',
        '_blank'
    )
  } else if (button.type === 'image' && button.title === '用户菜单') {
    window.open(
        '/user/' + userInfo.value.id,
        '_blank'
    )
  }
}

const handleDropdownItemClick = (data) => {
  const dropdownItems = data.item;

  switch (dropdownItems.id) {
    case 'login':
      router.push('/auth/login')
      break
    case 'register':
      router.push('/auth/register')
      break
    case 'logout':
      userLogout()
      break
    case 'create':
      window.open(
          '/circle/create',
          '_blank'
      )
      break
    case 'write':
      window.open(
          '/editor',
          '_blank'
      )
      break
    case 'profile':
      window.open(
          '/user/' + userInfo.value.id,
          '_blank'
      )
      break
    case 'settings':
      // router.push('/settings')
      window.open()
      break
  }
}

const handleSearch = (data) => {
  window.open(
      '/search/articles?keyword=' + data.query,
      '_blank'
  )
}

function userLogout() {
  logout(() => {
    isLoggedIn.value = false
    userInfo.value = null
    router.push("/")
  })
}
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
      :show-search="true"
      search-placeholder="搜索文章、圈子、用户"
      search-button-text="搜索"
      @search="handleSearch"
  />

  <XBackgroundSpace>
    <div class="app">
      <RouterView></RouterView>
    </div>
  </XBackgroundSpace>
</template>

<style scoped>
.app {
  width: 100%;
}
</style>
