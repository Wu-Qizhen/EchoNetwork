<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.9
-->
<!--
  使用方式：
  <template>
    <XTitleBar title="Aetherial Design" logo-image="./res/logo_aetherial_design.svg" title-lang="en" />
  </template>
-->
<template>
  <nav :class="{ 'scrolled': isScrolled }" :style="navStyle">
    <!-- Logo -->
    <div class="nav-logo protected-content">
      <img :src="logoImage" :alt="logoAlt" class="nav-img">
    </div>

    <div class="nav-title protected-content">
      <p :class="titleLang">{{ title }}</p>
    </div>

    <div class="nav-subtitle protected-content">
      <p :class="subtitleLang" style="color: var(--grey_highlight);">{{ subTitle }}</p>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'XTitleBar',
  props: {
    // Logo 相关属性
    logoImage: {
      type: String,
      default: './res/logo_code_intellix_with_text.png'
    },
    logoAlt: {
      type: String,
      default: 'Code IntelliX'
    },
    title: {
      type: String,
      default: '智码'
    },
    titleLang: {
      type: String,
      default: 'theme'
    },
    subTitle: {
      type: String,
      default: 'Aetherial Design'
    },
    subtitleLang: {
      type: String,
      default: 'zh'
    }
  },
  data() {
    return {
      isScrolled: false,
      navStyle: {
        top: '20px',
        left: '20px',
        right: '20px',
        borderRadius: '20px'
      }
    }
  },
  mounted() {
    this.addEventListeners()
  },
  beforeUnmount() {
    this.removeEventListeners()
  },
  methods: {
    handleScroll() {
      const scrollPosition = window.scrollY

      if (scrollPosition > 50) {
        const progress = Math.min(scrollPosition / 200, 1)
        const margin = 20 - (20 * progress)
        const borderRadius = 20 - (20 * progress)

        this.navStyle.top = margin + 'px'
        this.navStyle.left = margin + 'px'
        this.navStyle.right = margin + 'px'
        this.navStyle.borderRadius = borderRadius + 'px'

        this.isScrolled = scrollPosition > 200
      } else {
        this.navStyle.top = '20px'
        this.navStyle.left = '20px'
        this.navStyle.right = '20px'
        this.navStyle.borderRadius = '20px'
        this.isScrolled = false
      }
    },

    handleResize() {
      this.initMarker()
    },

    addEventListeners() {
      window.addEventListener('scroll', this.handleScroll)
      window.addEventListener('resize', this.handleResize)
    },

    removeEventListeners() {
      window.removeEventListener('scroll', this.handleScroll)
      window.removeEventListener('resize', this.handleResize)
    }
  }
}
</script>

<style scoped>
nav {
  position: fixed;
  top: 20px;
  left: 20px;
  right: 20px;
  margin: 0;
  height: 65px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  padding: 0 0 0 20px;
  background: rgba(30, 35, 47, 0.8);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  box-shadow: 0 5px 50px black;
  z-index: 1000;
  transition: all 0.3s ease;
}

nav.scrolled {
  top: 0 !important;
  left: 0 !important;
  right: 0 !important;
  border-radius: 0 !important;
  border: none;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

/* Logo 样式 */
.nav-logo {
  color: white;
  text-decoration: none;
  display: flex;
  align-items: center;
}

.nav-img {
  height: 40px;
  width: auto;
}

.nav-title {
  margin-left: 20px;
  font-size: 30px;
  font-weight: bold;
  align-items: center;
  text-align: center;
  transform: translateY(2px); /* 微调垂直位置 */
}

.nav-subtitle {
  margin-left: 20px;
  font-size: 16px;
  font-weight: normal;
  align-items: center;
  text-align: center;
  transform: translateY(6px); /* 微调垂直位置 */
}
</style>
