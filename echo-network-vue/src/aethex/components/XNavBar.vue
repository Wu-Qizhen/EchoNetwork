<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.9
-->
<!--
  使用方式：
  <script>
    export default {
      name: 'App',
      components: {
        XSpacer,
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

  <template>
    <XNavBar
      :nav-items="navItems"
      :initial-active-index="activeNavIndex"
      @nav-item-click="handleNavClick"
      @logo-click="handleLogoClick"
    />
  </template>
-->
<template>
  <nav :class="{ 'scrolled': isScrolled }" :style="navStyle">
    <!-- Logo -->
    <div class="nav-logo protected-content">
      <a @click="handleLogoClick" :title="logoTitle">
        <img :src="logoImage" :alt="logoAlt" class="nav-img">
      </a>
    </div>

    <!-- 选项卡 -->
    <div class="nav-links">
      <a
          v-for="(item, index) in navItems"
          :key="index"
          :class="{ 'active': activeIndex === index, 'nav-item': true }"
          @click="handleNavClick(index, $event)"
      >
        {{ item.text }}
      </a>
      <div id="marker" :style="markerStyle"></div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'XNavBar',
  props: {
    // Logo 相关属性
    logoImage: {
      type: String,
      default: '/res/logo_code_intellix_with_text.png'
    },
    logoTitle: {
      type: String,
      default: '智码'
    },
    logoAlt: {
      type: String,
      default: 'Code IntelliX'
    },

    // 导航项配置
    navItems: {
      type: Array,
      default: () => [
        {text: '探索', id: 'explore'},
        {text: '开发套件', id: 'dev-kit'},
        {text: '解决方案', id: 'solutions'},
        {text: '服务支持', id: 'support'},
        {text: '培训学习', id: 'training'},
        {text: '关于我们', id: 'about'}
      ]
    },

    // 初始激活项索引
    initialActiveIndex: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      activeIndex: this.initialActiveIndex,
      isScrolled: false,
      markerStyle: {
        left: '0px',
        width: '0px'
      },
      navStyle: {
        top: '20px',
        left: '20px',
        right: '20px',
        borderRadius: '20px'
      }
    }
  },
  mounted() {
    this.initMarker()
    this.addEventListeners()
  },
  beforeUnmount() {
    this.removeEventListeners()
  },
  methods: {
    initMarker() {
      this.$nextTick(() => {
        const activeItem = this.$el.querySelector('.nav-links .nav-item.active') ||
            this.$el.querySelector('.nav-links .nav-item')
        if (activeItem) {
          this.markerStyle.left = activeItem.offsetLeft + 'px'
          this.markerStyle.width = activeItem.offsetWidth + 'px'
          this.activeIndex = Array.from(this.$el.querySelectorAll('.nav-links .nav-item')).indexOf(activeItem)
        }
      })
    },

    handleNavClick(index, event) {
      this.activeIndex = index
      this.updateMarker(event.target)

      // 触发自定义事件
      this.$emit('nav-item-click', {
        index,
        item: this.navItems[index],
        event
      })
    },

    handleLogoClick() {
      this.$emit('logo-click')
    },

    updateMarker(target) {
      this.markerStyle.left = target.offsetLeft + 'px'
      this.markerStyle.width = target.offsetWidth + 'px'
    },

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
  justify-content: flex-end;
  align-items: center;
  padding: 10px 20px;
  background: rgba(30, 35, 47, 0.6);
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
.nav-logo a {
  position: absolute;
  left: 20px;
  top: 50%;
  transform: translateY(-50%);
  color: white;
  text-decoration: none;
  display: flex;
  align-items: center;
  z-index: 2;
}

.nav-img {
  height: 25px;
  width: auto;
}

/* 导航链接样式 */
.nav-links {
  display: flex;
  position: relative;
}

.nav-item {
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  padding: 10px 20px;
  transition: all 0.3s ease;
  position: relative;
  cursor: pointer;
  user-select: none;
  z-index: 1;
}

.nav-item:hover {
  color: white;
}

.nav-item.active {
  color: white !important;
  text-shadow: 0 0 10px rgba(var(--theme-color-rgb), 0.7);
}

/* 标记指示器 */
#marker {
  position: absolute;
  bottom: 0;
  border-radius: 10px;
  transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  pointer-events: none;
  border-bottom: 3px solid var(--theme-color);
  box-shadow: 0 0 20px var(--theme-color),
  0 -5px 25px var(--theme-color),
  0 -10px 30px var(--theme-color),
  0 -25px 35px var(--theme-color);
  z-index: 0;
}
</style>
