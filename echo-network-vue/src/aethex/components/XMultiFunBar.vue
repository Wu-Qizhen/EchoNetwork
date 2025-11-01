<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.12
-->
<!--
  使用方式：
  <script setup>
    import {ref} from "vue";
    import XMultiFunBar from "@/aethex/components/XMultiFunBar.vue";

    const navItems = ref([
      {text: '首页', id: 'home'},
      {text: '产品', id: 'products'},
      {text: '服务', id: 'services'},
      {text: '关于', id: 'about'}
    ])

    const navButtons = ref([
      {
        type: 'image',
        imageUrl: './res/ic_avatar_default.svg',
        alt: '用户头像',
        title: '用户菜单',
        dropdownItems: [
          {text: '个人资料', id: 'profile'},
          {text: '账户设置', id: 'settings'},
          {text: '退出登录', id: 'logout'}
        ]
      },
      {
        type: 'icon',
        svgUrl: './res/ic_message.svg',
        alt: '消息中心',
        title: '消息中心',
        dropdownItems: [
          {text: '收件箱', id: 'inbox'},
          {text: '已发送', id: 'sent'},
          {text: '草稿箱', id: 'drafts'}
        ]
      },
      {
        type: 'text',
        content: '登录',
        title: '点击登录',
        dropdownItems: [
          {text: '用户登录', id: 'user-login'},
          {text: '管理员登录', id: 'admin-login'},
          {text: '注册账号', id: 'register'}
        ]
      }
    ])

    const handleNavItemClick = (data) => {
      console.log('导航项点击：', data)
    }

    const handleLogoClick = () => {
      console.log('Logo 点击')
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

    </script>

    <template>
      <XMultiFunBar
          :nav-items="navItems"
          :nav-buttons="navButtons"
          @nav-item-click="handleNavItemClick"
          @logo-click="handleLogoClick"
          @button-click="handleButtonClick"
          @dropdown-item-click="handleDropdownItemClick"
          :show-search="true"
          search-placeholder="搜索产品、服务"
          search-button-text="查找"
          @search="handleSearch"
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

    <XSpacer type="horizontal" width="20px"/>

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

    <!-- 搜索框 -->
    <div v-if="showSearch" class="nav-search">
      <input
          type="text"
          :placeholder="searchPlaceholder"
          v-model="searchText"
          @keyup.enter="handleSearch"
          class="nav-search-input"
      />
      <button
          @click="handleSearch"
          class="nav-search-button"
          :title="searchButtonText"
      >
        {{ searchButtonText }}
      </button>
    </div>

    <!-- 右侧按钮卡槽 -->
    <div class="nav-buttons">
      <div
          v-for="(button, index) in navButtons"
          :key="`btn-${index}`"
          class="nav-button-wrapper"
          @mouseenter="showDropdown(index)"
          @mouseleave="hideDropdown(index)"
      >
        <!-- 文字按钮 -->
        <button
            v-if="button.type === 'text'"
            :class="['nav-button', `nav-button-${button.type}`]"
            @click="handleButtonClick(button, $event)"
        >
          {{ button.content }}
        </button>

        <!-- 图标按钮 -->
        <button
            v-else-if="button.type === 'icon'"
            :class="['nav-button', `nav-button-${button.type}`]"
            @click="handleButtonClick(button, $event)"
            :title="button.title"
        >
          <img :src="button.svgUrl" :alt="button.alt" class="nav-button-icon-inner"/>
        </button>

        <!-- 图片按钮 -->
        <button
            v-else-if="button.type === 'image'"
            :class="[`nav-button-${button.type}`]"
            @click="handleButtonClick(button, $event)"
            :title="button.title"
        >
          <img :src="button.imageUrl" :alt="button.alt" class="nav-button-image">
        </button>

        <!-- 下拉菜单 -->
        <div
            v-if="button.dropdownItems && button.dropdownItems.length > 0"
            :class="['nav-dropdown', { 'active': activeDropdownIndex === index }]"
        >
          <div
              v-for="(item, itemIndex) in button.dropdownItems"
              :key="itemIndex"
              class="nav-dropdown-item"
              @click="handleDropdownItemClick(button, item, $event)"
          >
            {{ item.text }}
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import {nextTick, onBeforeUnmount, onMounted, reactive, ref} from 'vue'
import XSpacer from "@/aethex/components/XSpacer.vue";

// 定义组件属性
const props = defineProps({
  // Logo 相关属性
  logoImage: {
    type: String,
    default: './res/logo_code_intellix_with_text.png'
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
  },

  // 搜索框相关属性
  showSearch: {
    type: Boolean,
    default: false
  },
  searchPlaceholder: {
    type: String,
    default: '搜索内容'
  },
  searchButtonText: {
    type: String,
    default: '搜索'
  },

  // 右侧按钮配置
  navButtons: {
    type: Array,
    default: () => [
      {
        type: 'image',
        imageUrl: './res/ic_avatar_default.svg',
        alt: '用户头像',
        title: '用户菜单',
        dropdownItems: [
          {text: '个人资料', id: 'profile'},
          {text: '账户设置', id: 'settings'},
          {text: '退出登录', id: 'logout'}
        ]
      },
      {
        type: 'icon',
        svgUrl: './res/ic_message.svg',
        alt: '消息中心',
        title: '消息中心',
        dropdownItems: [
          {text: '收件箱', id: 'inbox'},
          {text: '已发送', id: 'sent'},
          {text: '草稿箱', id: 'drafts'}
        ]
      },
      {
        type: 'text',
        content: '登录',
        title: '点击登录',
        dropdownItems: [
          {text: '用户登录', id: 'user-login'},
          {text: '管理员登录', id: 'admin-login'},
          {text: '注册账号', id: 'register'}
        ]
      }
    ]
  }
})

// 定义自定义事件
const emit = defineEmits([
  'nav-item-click',
  'logo-click',
  'button-click',
  'dropdown-item-click',
  'search'
])

// 响应式数据
const activeIndex = ref(props.initialActiveIndex)
const isScrolled = ref(false)
const activeDropdownIndex = ref(-1)
const searchText = ref('')

const markerStyle = reactive({
  left: '0px',
  width: '0px'
})

const navStyle = reactive({
  top: '20px',
  left: '20px',
  right: '20px',
  borderRadius: '20px'
})

// 方法
const initMarker = () => {
  nextTick(() => {
    const navElement = document.querySelector('.nav-links')
    if (!navElement) return

    const activeItem = navElement.querySelector('.nav-item.active') ||
        navElement.querySelector('.nav-item')
    if (activeItem) {
      markerStyle.left = activeItem.offsetLeft + 'px'
      markerStyle.width = activeItem.offsetWidth + 'px'
      activeIndex.value = Array.from(navElement.querySelectorAll('.nav-item')).indexOf(activeItem)
    }
  })
}

const handleNavClick = (index, event) => {
  activeIndex.value = index
  updateMarker(event.target)

  // 触发自定义事件
  emit('nav-item-click', {
    index,
    item: props.navItems[index],
    event
  })
}

const handleLogoClick = () => {
  emit('logo-click')
}

const updateMarker = (target) => {
  markerStyle.left = target.offsetLeft + 'px'
  markerStyle.width = target.offsetWidth + 'px'
}

const handleScroll = () => {
  const scrollPosition = window.scrollY

  if (scrollPosition > 50) {
    const progress = Math.min(scrollPosition / 200, 1)
    const margin = 20 - (20 * progress)
    const borderRadius = 20 - (20 * progress)

    navStyle.top = margin + 'px'
    navStyle.left = margin + 'px'
    navStyle.right = margin + 'px'
    navStyle.borderRadius = borderRadius + 'px'

    isScrolled.value = scrollPosition > 200
  } else {
    navStyle.top = '20px'
    navStyle.left = '20px'
    navStyle.right = '20px'
    navStyle.borderRadius = '20px'
    isScrolled.value = false
  }
}

const handleResize = () => {
  initMarker()
}

const showDropdown = (index) => {
  activeDropdownIndex.value = index
}

const hideDropdown = (index) => {
  if (activeDropdownIndex.value === index) {
    activeDropdownIndex.value = -1
  }
}

const handleSearch = () => {
  /*if (searchText.value.trim()) {
    emit('search', {
      query: searchText.value.trim(),
      event: event
    })
  }*/
  const query = searchText.value;
  if (typeof query === 'string' && query.trim()) {
    emit('search', {
      query: query.trim(),
      event
    });
  }
}

const handleButtonClick = (button, event) => {
  emit('button-click', {
    button,
    event
  })
}

const handleDropdownItemClick = (button, item, event) => {
  emit('dropdown-item-click', {
    button,
    item,
    event
  })

  // 点击菜单项后关闭下拉菜单
  activeDropdownIndex.value = -1
}

const addEventListeners = () => {
  window.addEventListener('scroll', handleScroll)
  window.addEventListener('resize', handleResize)
}

const removeEventListeners = () => {
  window.removeEventListener('scroll', handleScroll)
  window.removeEventListener('resize', handleResize)
}

// 生命周期
onMounted(() => {
  initMarker()
  addEventListeners()
})

onBeforeUnmount(() => {
  removeEventListeners()
})
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
/* .nav-logo a {
  display: flex;
  align-items: center;
  color: white;
  text-decoration: none;
  cursor: pointer;
} */

.nav-logo a {
  color: white;
  text-decoration: none;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.nav-img {
  height: 40px;
  width: auto;
}

/* 导航链接样式 */
.nav-links {
  display: flex;
  position: relative;
  flex: 1;
  /* justify-content: center; */
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


/* 搜索框样式 */
/* .nav-search{
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
} */

.nav-search {
  display: flex;
  align-items: center;
  margin: 0 10px;
  border-radius: 10px;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.2);
  background: transparent;
  transition: all 0.3s ease;
}

.nav-search:focus-within {
  border-color: white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.nav-search-input {
  background: transparent;
  border: none;
  padding: 8px 12px;
  color: white;
  font-size: 14px;
  width: 200px;
  outline: none;
}

.nav-search-input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.nav-search-button {
  background: transparent;
  border: none;
  color: white;
  padding: 8px 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  white-space: nowrap;
  border-left: 1px solid rgba(255, 255, 255, 0.2);
}

.nav-search-button:hover {
  background: rgba(255, 255, 255, 0.1);
}

/* 右侧按钮样式 */
.nav-buttons {
  display: flex;
  align-items: center;
  gap: 10px;
}

.nav-button-wrapper {
  position: relative;
}

.nav-button {
  background: var(--theme-color);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  color: rgba(255, 255, 255);
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-button:hover {
  color: white;
  border-color: transparent;
  background: var(--theme-color-darken);
  fill: white;
}

.nav-button-text {
  padding: 8px 16px;
  font-size: 14px;
}

.nav-button-icon {
  width: 36px;
  height: 36px;
  font-size: 16px;
}

.nav-button-icon-inner {
  width: 28px;
  height: 28px;
}

/* .nav-button-image {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  object-fit: cover;
} */

.nav-button-image {
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  object-fit: cover;
}

.nav-button-image:hover {
  color: white;
  border-color: transparent;
  background: rgba(255, 255, 255, 0.1);
}

/* 下拉菜单样式 */
.nav-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  background: rgba(30, 35, 47, 0.6);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.3);
  min-width: 150px;
  text-align: center;
  opacity: 0;
  overflow: hidden;
  visibility: hidden;
  transform: translateY(-10px);
  transition: all 0.6s ease;
  z-index: 1001;
  margin-top: 28px;
}

.nav-dropdown.active {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.nav-dropdown-item {
  padding: 10px 15px;
  color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  transition: all 0.2s ease;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.nav-dropdown-item:last-child {
  border-bottom: none;
}

.nav-dropdown-item:hover {
  color: white;
  /* border-radius: 10px; */
  background: rgba(255, 255, 255, 0.1);
}

@media (max-width: 768px) {
  .nav-logo {
    display: none;
  }

  /* .nav-links {
    margin-left: 0;
    display: flex;
  } */

  .nav-item {
    padding: 10px 15px;
  }

  .nav-search {
    display: none;
  }
}
</style>
