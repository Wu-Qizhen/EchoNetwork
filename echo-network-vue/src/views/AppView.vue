<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.9
-->
<script setup>
import {logout} from "@/net/index.js";
import router from "@/router/index.js";
import {ref} from "vue";
import XMultiFunBar from "@/aethex/components/XMultiFunBar.vue";
import XBackgroundSpace from "@/aethex/components/XBackgroundSpace.vue";
import BlogArticleList from "@/views/app/ArticleList.vue";
import RankingList from "@/views/app/RankingList.vue";

const navItems = ref([
  {text: '首页', id: 'home'},
  {text: '圈子', id: 'circle'},
  {text: '留言板', id: 'board'},
  {text: '关于', id: 'about'}
])

const navButtons = ref([
  /*{
    type: 'image',
    imageUrl: './res/ic_avatar_default.svg',
    alt: '用户头像',
    title: '用户菜单',
    dropdownItems: [
      {text: '个人资料', id: 'profile'},
      {text: '账户设置', id: 'settings'},
      {text: '退出登录', id: 'logout'}
    ]
  },*/
  /*{
    type: 'icon',
    svgUrl: './res/ic_message.svg',
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
    content: '登录',
    title: '点击登录',
    dropdownItems: [
      {text: '用户登录', id: 'user-login'},
      {text: '注册账号', id: 'register'}
    ]
  }
])

const handleNavItemClick = (data) => {
  // console.log('导航项点击：', data)
  const item = data.item;
  if (item.id === 'about') {
    router.push('/welcome')
  }
}

const handleLogoClick = () => {
  // console.log('Logo 点击')
  router.push('/welcome')
}

const handleButtonClick = (data) => {
  console.log('按钮点击：', data)
}

const handleDropdownItemClick = (data) => {
  console.log('下拉菜单项点击：', data)
}

const handleSearch = (data) => {
  console.log('搜索关键词：', data.query)
  // 执行搜索逻辑
}

function userLogout() {
  logout(() => {
    router.push("/")
  })
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
      :show-search="true"
      search-placeholder="搜索圈子、文章、标签"
      search-button-text="查找"
      @search="handleSearch"
  />

  <XBackgroundSpace>
    <div class="root">
      <div class="article-list">
        <BlogArticleList/>
      </div>

      <div class="ranking-list">
        <RankingList/>
      </div>
    </div>
  </XBackgroundSpace>
</template>

<style scoped>
.root {
  width: 100vw;
  padding: 105px 100px 80px 80px;
  display: flex;
  justify-content: center;
  align-items: start;
}

.article-list {
  width: 60%;
  margin-right: 20px;
}

.ranking-list {
  width: 40%;
}
</style>
