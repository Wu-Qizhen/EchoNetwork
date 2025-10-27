<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.27
-->
<!--
  使用方式：
  <template>
    <XTabBar
      :tab-items="tabItems"
      @tab-item-click="handleTabItemClick"
    ></XTabBar>
  </template>

  <script setup>
    import XTabBar from "@/aethex/components/XTabBar.vue";
    const tabItems = ref([
      {text: '文章', id: 'articles'},
      {text: '粉丝', id: 'followers'},
    ])

    const handleTabItemClick = (data) => {
      const item = data.item;
      if (item.id === 'articles') {
        router.push('/')
      } else if (item.id === 'followers') {
        router.push('/followers')
      }
    }
  </script>
-->
<template>
  <div class="tab-links">
    <a
        v-for="(item, index) in tabItems"
        :key="index"
        :class="{ 'active': activeIndex === index, 'tab-item': true }"
        @click="handleTabClick(index, $event)"
    >
      {{ item.text }}
    </a>
    <div id="marker" :style="markerStyle"></div>
  </div>
</template>

<script setup>
import {nextTick, onBeforeUnmount, onMounted, reactive, ref} from 'vue'

// 定义组件属性
const props = defineProps({
  // 切换项配置
  tabItems: {
    type: Array,
    default: () => [
      {text: '用户', id: 'user'},
      {text: '角色', id: 'role'},
      {text: '任务', id: 'tasks'},
    ]
  },
  // 初始激活项索引
  initialActiveIndex: {
    type: Number,
    default: 0
  }
})

// 定义自定义事件
const emit = defineEmits([
  'tab-item-click',
])

// 响应式数据
const activeIndex = ref(props.initialActiveIndex)

const markerStyle = reactive({
  left: '0px',
  width: '0px'
})

// 方法
const initMarker = () => {
  nextTick(() => {
    const tabElement = document.querySelector('.tab-links')
    if (!tabElement) return

    const activeItem = tabElement.querySelector('.tab-item.active') ||
        tabElement.querySelector('.tab-item')
    if (activeItem) {
      markerStyle.left = activeItem.offsetLeft + 'px'
      markerStyle.width = activeItem.offsetWidth + 'px'
      activeIndex.value = Array.from(tabElement.querySelectorAll('.tab-item')).indexOf(activeItem)
    }
  })
}

const handleTabClick = (index, event) => {
  activeIndex.value = index
  updateMarker(event.target)

  // 触发自定义事件
  emit('tab-item-click', {
    index,
    item: props.tabItems[index],
    event
  })
}

const updateMarker = (target) => {
  markerStyle.left = target.offsetLeft + 'px'
  markerStyle.width = target.offsetWidth + 'px'
}

const handleResize = () => {
  initMarker()
}

const addEventListeners = () => {
  window.addEventListener('resize', handleResize)
}

const removeEventListeners = () => {
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
/* 链接样式 */
.tab-links {
  display: flex;
  position: relative;
  flex: 1;
  /* justify-content: center; */
}

.tab-item {
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  padding: 10px 20px;
  transition: all 0.3s ease;
  position: relative;
  cursor: pointer;
  user-select: none;
  z-index: 1;
}

.tab-item:hover {
  color: white;
}

.tab-item.active {
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
