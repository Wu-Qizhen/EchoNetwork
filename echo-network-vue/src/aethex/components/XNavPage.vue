<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.9
  Deprecated on 2025.10.31
-->
<!--
  使用方式：
  <template>
    <XPageContainer
      :nav-items="navItems"
      @nav-item-click="handleNavClick"
    >
      ! 在这里放置你的页面内容 !
      <section id="explore">
        <h1>探索</h1>
        ! 探索部分内容 !
      </section>

      <section id="dev-kit">
        <h1>开发套件</h1>
        ! 开发套件内容 !
      </section>

      ! 其他部分 !
    </XPageContainer>
  </template>
-->
<template>
  <div class="page-container">
    <XNavBar
      :nav-items="navItems"
      :initial-active-index="activeNavIndex"
      @nav-item-click="handleNavClick"
      @logo-click="handleLogoClick"
    />

    <main class="page-content">
      <slot></slot>
    </main>
  </div>
</template>

<script>
import XNavBar from '@/aethex/components/XNavBar.vue'

export default {
  name: 'App',
  components: {
    XNavBar
  },
  data() {
    return {
      activeNavIndex: 0,
      navItems: [
        { text: '探索', id: 'explore' },
        { text: '开发套件', id: 'dev-kit' },
        { text: '解决方案', id: 'solutions' },
        { text: '服务支持', id: 'support' },
        { text: '培训学习', id: 'training' },
        { text: '关于我们', id: 'about' }
      ]
    }
  },
  methods: {
    handleNavClick({ index, item }) {
      console.log('导航项点击:', index, item.text)
      this.activeNavIndex = index

      // 滚动到对应锚点
      const targetElement = document.getElementById(item.id)
      if (targetElement) {
        targetElement.scrollIntoView({
          behavior: 'smooth',
          block: 'start'
        })
      }

      // 或者使用 Vue Router 进行路由跳转
      // this.$router.push(`/${item.id}`);
    },

    handleLogoClick() {
      console.log('Logo 点击')
      // 滚动到顶部或跳转到首页
      window.scrollTo({ top: 0, behavior: 'smooth' })

      // 或者重置导航状态
      this.activeNavIndex = 0
    }
  }
}
</script>

<style scoped>
.page-container {
  min-height: 100vh;
}

.page-content {
  padding-top: 105px; /* 为固定导航栏留出空间 */
}
</style>
