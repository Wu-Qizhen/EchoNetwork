<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.27
-->
<script setup>
import {computed, onMounted, ref, watch} from "vue";
import {useRoute} from "vue-router";
import router from "@/router/index.js";
import {getUserInfo, isAuthorized, logout} from "@/net/index.js";
import XMultiFunBar from "@/aethex/components/XMultiFunBar.vue";
import XBackgroundSpace from "@/aethex/components/XBackgroundSpace.vue";

const isLoggedIn = ref(false)
const userInfo = ref(null)

const route = useRoute();
// const fullPath = route.fullPath
// const path = route.path
const query = route.query;

const searchType = ref('articles')
const initialActiveIndex = ref(0)

const calculateInitialActiveIndex = () => {
  const path = route.path
  // console.log('当前路径：', path)

  if (path.includes('/search/articles')) {
    searchType.value = 'articles'
    return 0
  } else if (path.includes('/search/circles')) {
    searchType.value = 'circles'
    // console.log('当前搜索类型：', searchType.value)
    return 1
  } else if (path.includes('/search/users')) {
    searchType.value = 'users'
    return 2
  }
  return 0
}

function checkLoginStatus() {
  isLoggedIn.value = isAuthorized()
  if (isLoggedIn.value) {
    userInfo.value = getUserInfo()
  }
}

onMounted(() => {
  initialActiveIndex.value = calculateInitialActiveIndex()
  // console.log('初始激活项索引：', initialActiveIndex.value)
  checkLoginStatus()
})

watch(() => route.path, () => {
  initialActiveIndex.value = calculateInitialActiveIndex()
})

const navItems = ref([
  {text: '搜索文章', id: 'articles'},
  {text: '搜索圈子', id: 'circles'},
  {text: '搜索用户', id: 'users'},
  {text: '返回首页', id: 'home'},
])

const navButtons = computed(() => [
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
])

const handleNavItemClick = (data) => {
  const item = data.item;

  if (item.id === 'home') {
    router.push('/')
  } else {
    /*let path = '';
    if (item.id === 'articles') {
      path = `/search/articles`;
      searchType.value = 'articles'
    } else if (item.id === 'circles') {
      path = `/search/circles`;
      searchType.value = 'circles'
    } else if (item.id === 'users') {
      path = `/search/users`;
      searchType.value = 'users'
    }

    router.push({
      path: path,
      query: query
    });*/

    searchType.value = item.id
    router.push({
      path: `/search/${item.id}`,
      query: query
    });
  }
}

const handleLogoClick = () => {
  router.push('/welcome')
}

const handleButtonClick = (data) => {
  const button = data.button

  if (button.type === 'image' && button.title === '用户菜单') {
    if (userInfo.value) {
      window.open(
          '/user/' + userInfo.value.id,
          '_blank'
      )
    }
  }
}

const handleDropdownItemClick = (data) => {
  const dropdownItems = data.item;

  switch (dropdownItems.id) {
    case 'logout':
      userLogout()
      break
    case 'profile':
      if (userInfo.value) {
        window.open(
            '/user/' + userInfo.value.id,
            '_blank'
        )
      }
      break
    case 'settings':
      window.open(
          '/settings',
          '_blank'
      )
      break
  }
}

function userLogout() {
  logout(() => {
    isLoggedIn.value = false
    userInfo.value = null
    router.push("/")
  })
}

const handleSearch = (data) => {
  window.location.href = `/search/${searchType.value}?keyword=${data.query}`;
  // router.push(`/search/${searchType.value}?keyword=${data.query}`)
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
      :initial-active-index="initialActiveIndex"
  />
  <XBackgroundSpace>
    <div class="search">
      <div class="display-area">
        <RouterView></RouterView>
      </div>
    </div>
  </XBackgroundSpace>
</template>

<style scoped>
.search {
  width: 100%;
  padding: 105px 20px 100px 20px;
  display: flex;
  flex-direction: column;
  align-items: start;
}

.display-area {
  width: 100%;
}

/* @media (max-width: 768px) {
  .search {
    width: 100%;
    padding: 105px 20px 0 20px;
    display: flex;
    flex-direction: column;
    justify-content: start;
    align-items: center;
  }
} */
</style>
