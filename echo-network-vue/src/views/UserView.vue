<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.22
-->
<script setup>
import XMultiFunBar from "@/aethex/components/XMultiFunBar.vue";
import XBackgroundSpace from "@/aethex/components/XBackgroundSpace.vue";
import {computed, onMounted, ref} from "vue";
import {getUserInfo, isAuthorized, logout} from "@/net/index.js";
import router from "@/router/index.js";
import XTabBar from "@/aethex/components/XTabBar.vue";
import XSpacer from "@/aethex/components/XSpacer.vue";
import {useRoute} from "vue-router";
import ProfileCard from "@/views/common/ProfileCard.vue";

const isLoggedIn = ref(false)
const userInfo = ref(null)

const route = useRoute();
const userId = computed(() => route.params.id);

// 检查登录状态
function checkLoginStatus() {
  isLoggedIn.value = isAuthorized()
  if (isLoggedIn.value) {
    userInfo.value = getUserInfo()
  }
}

// 初始化时检查登录状态
onMounted(() => {
  checkLoginStatus()
})

const navItems = ref([
  {text: 'TA 的主页', id: 'user'},
  {text: '返回首页', id: 'home'},
])

// TODO 图片路径 BUG
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
  }
}

const handleLogoClick = () => {
  router.push('/welcome')
}

const handleButtonClick = (data) => {
  const button = data.button

  if (button.type === 'image' && button.title === '用户菜单') {
    if (userInfo.value) {
      /*router.push({
        name: 'user',
        params: {id: userInfo.value.id}
      })*/
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
        /*router.push({
          name: 'user',
          params: {id: userInfo.value.id}
        })*/
        window.open(
            '/user/' + userInfo.value.id,
            '_blank'
        )
      }
      break
    case 'settings':
      router.push('/settings')
      break
  }
}

// TODO 指标 BUG
const tabItems = computed(() => {
  if (!userInfo.value) {
    return [
      {text: '文章', id: 'articles'},
      {text: '圈子', id: 'circles'},
      {text: '粉丝', id: 'followers'},
    ]
  }

  const currentUserId = userInfo.value.id;
  if (userId.value === currentUserId.toString()) {  // 添加 .value 和可选链
    return [
      {text: '文章', id: 'articles'},
      {text: '圈子', id: 'circles'},
      {text: '粉丝', id: 'followers'},
      {text: '关注', id: 'following'},
      {text: '收藏', id: 'stars'},
    ]
  } else {
    return [
      {text: '文章', id: 'articles'},
      {text: '圈子', id: 'circles'},
      {text: '粉丝', id: 'followers'},
    ]
  }
})

const handleTabItemClick = (data) => {
  const item = data.item;
  const targetUserId = userId.value;

  if (item.id === 'articles') {
    router.push(`/user/${targetUserId}`);
  } else if (item.id === 'followers') {
    router.push(`/user/${targetUserId}/followers`);
  } else if (item.id === 'following') {
    router.push(`/user/${targetUserId}/following`);
  } else if (item.id === 'circles') {
    router.push(`/user/${targetUserId}/circles`);
  } else if (item.id === 'stars') {
    router.push(`/user/${targetUserId}/stars`);
  }
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
      :show-search="false"
  />
  <XBackgroundSpace>
    <div class="user">
      <div class="profile-area">
        <ProfileCard
            :user-id="userId"
            :current-user-id="userInfo?.id"
        ></ProfileCard>
      </div>

      <div class="display-area">
        <XTabBar
            :tab-items="tabItems"
            @tab-item-click="handleTabItemClick"
        ></XTabBar>
        <XSpacer type="vertical" height="20px"></XSpacer>
        <RouterView></RouterView>
      </div>
    </div>
  </XBackgroundSpace>
</template>

<style scoped>
.user {
  width: 100%;
  padding-top: 105px;
  display: flex;
  flex-direction: row;
  align-items: start;
}

.profile-area {
  width: 30%;
}

.display-area {
  width: 70%;
}
</style>
