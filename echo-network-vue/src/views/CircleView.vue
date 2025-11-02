<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.27
-->
<script setup>
import {useRoute} from "vue-router";
import {computed, onMounted, ref} from "vue";
import CircleCard from "@/views/common/CircleCard.vue";
import ArticleList from "@/views/common/ArticleList.vue";
import XSpacer from "@/aethex/components/XSpacer.vue";
import {getUserInfo, isAuthorized, logout} from "@/net/index.js";
import router from "@/router/index.js";
import XBackgroundSpace from "@/aethex/components/XBackgroundSpace.vue";
import XMultiFunBar from "@/aethex/components/XMultiFunBar.vue";

const isLoggedIn = ref(false)
const userInfo = ref(null)

const route = useRoute();
const circleId = computed(() => route.params.id);

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
  {text: '圈子详情', id: 'circle'},
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
    <div class="circle">
      <CircleCard
          :circle-id=circleId
      ></CircleCard>

      <XSpacer height="20px"/>

      <ArticleList
          :request-config="{
            circleId: circleId,
          }"
          :enablePagination="true"
      ></ArticleList>
    </div>
  </XBackgroundSpace>
</template>

<style scoped>
.circle {
  width: 100%;
  padding: 105px 20px 100px 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: start;
}
</style>
