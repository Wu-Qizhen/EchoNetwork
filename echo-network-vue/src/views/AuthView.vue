<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.9
-->
<script setup>
import {onMounted, onUnmounted, ref, watch} from 'vue'
import {useRoute} from 'vue-router'

const route = useRoute()
const transitionName = ref('slide-left')

// 监听路由变化，决定过渡方向
watch(() => route.path, (to, from) => {
  // 定义页面层级
  const pageLevels = {
    '/auth/login': 0,           // 默认页面层级为 0
    '/auth/register': 1,   // 注册页面层级为 1
    '/auth/reset': 1       // 重置页面层级为 1
  }

  // 获取目标页面和来源页面的层级，如果没有定义则默认为 0
  const toDepth = pageLevels[to] !== undefined ? pageLevels[to] : 0
  const fromDepth = pageLevels[from] !== undefined ? pageLevels[from] : 0

  // 根据层级关系决定动画方向
  if (toDepth > fromDepth) {
    transitionName.value = 'slide-left'    // 进入更深层级
  } else if (toDepth < fromDepth) {
    transitionName.value = 'slide-right'   // 返回上一层
  } else {
    transitionName.value = 'fade'          // 同级切换使用淡入淡出
  }
})

// 3D 倾斜效果
const interactElement = ref(null)

// 鼠标移动处理函数
const handleMouseMove = (e) => {
  if (!interactElement.value) return

  // 获取鼠标在视口中的位置
  const x = e.clientX / window.innerWidth
  const y = e.clientY / window.innerHeight

  // 计算倾斜角度
  const tiltX = (y - 0.5) * 20
  const tiltY = (x - 0.5) * -20

  // 应用变换
  interactElement.value.style.transform = `scaleX(-1) rotateX(${tiltX}deg) rotateY(${tiltY}deg)`
}

// 鼠标离开处理函数
const handleMouseLeave = () => {
  if (!interactElement.value) return
  // 平滑回到原始位置
  interactElement.value.style.transform = 'scaleX(-1) rotateX(0deg) rotateY(0deg)'
}

// 组件挂载时添加事件监听
onMounted(() => {
  document.addEventListener('mousemove', handleMouseMove)
  if (interactElement.value) {
    interactElement.value.addEventListener('mouseleave', handleMouseLeave)
  }
})

// 组件卸载时移除事件监听
onUnmounted(() => {
  document.removeEventListener('mousemove', handleMouseMove)
  if (interactElement.value) {
    interactElement.value.removeEventListener('mouseleave', handleMouseLeave)
  }
})
</script>

<template>
  <div class="auth">
    <div class="welcome-bg protected-content">
      <img ref="interactElement" src="../../res/bg_code_intellix_fold.svg" alt="">
    </div>
    <div class="welcome-title protected-content">
      <h1 class="en h1">Echo of Thought, Here</h1>
      <h1 class="theme h1">思想，在此共鸣</h1>
    </div>
    <div class="welcome-page">
      <div class="welcome-card">
        <div class="echo-network-logo protected-content">
          <img src="../../res/logo_echo_network_with_double_text.svg" alt="">
        </div>
        <transition :name="transitionName" mode="out-in">
          <RouterView></RouterView>
        </transition>
        <!--<router-view v-slot="{ Component }">
              <transition :name="transitionName" mode="out-in">
                <component :is="Component"/>
              </transition>
            </router-view>-->
      </div>
    </div>
  </div>
</template>

<style scoped>
body {
  overflow: hidden;
}

.auth {
  width: 100vw;
  height: 100vh;
  position: relative;
  display: flex;
  overflow: hidden;
  align-items: center;
  background: linear-gradient(135deg, #110a39 0%, black 40%, black 100% /* , #011b22 100% */);
}

.welcome-bg {
  position: absolute;
  top: 50%;
  margin-top: 5%;
  left: -15%;
  transform: translateY(-50%);
  height: 175%;
  width: auto;
  overflow: hidden;
  z-index: 1;
  perspective: 1000px; /* 添加透视效果 */
}

.welcome-bg img {
  transform: scaleX(-1);
  height: 100%;
  width: auto;
  object-fit: cover;
  transition: transform 0.5s cubic-bezier(0.23, 1, 0.32, 1);
  transform-style: preserve-3d; /* 启用 3D 变换 */
}

.welcome-title {
  position: relative;
  z-index: 2;
  margin-left: 5%;
  margin-right: 20px;
}

.welcome-page {
  width: 500px;
  height: 100%;
  box-shadow: 0 5px 50px black;
  background: rgba(30, 35, 47, 0.6);
  backdrop-filter: blur(15px);
  display: flex;
  justify-content: center;
  align-items: center;
  -webkit-backdrop-filter: blur(15px);
  margin-left: auto;
  z-index: 10;
}

.welcome-card {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  margin: 0 0;
  text-align: center;
  justify-content: flex-start;
  align-content: center;
  align-items: center;
  overflow-x: hidden;
  overflow-y: auto; /* 支持垂直滚动 */
}

/* 自定义滚动条 */
.welcome-card::-webkit-scrollbar {
  width: 5px;
}

.welcome-card::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
}

.welcome-card::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 3px;
}

.welcome-card::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.5);
}

.echo-network-logo {
  margin: 30px;
  align-self: flex-start;
}

.echo-network-logo img {
  height: 60px;
}

/* 非线性过渡动画 */
.slide-left-enter-active,
.slide-left-leave-active,
.slide-right-enter-active,
.slide-right-leave-active {
  transition: all 0.6s cubic-bezier(0.68, -0.55, 0.27, 1.55); /* 非线性贝塞尔曲线 */
}

.slide-left-enter-from {
  opacity: 0;
  transform: translateX(50px) scale(0.9);
}

.slide-left-leave-to {
  opacity: 0;
  transform: translateX(-50px) scale(0.9);
}

.slide-right-enter-from {
  opacity: 0;
  transform: translateX(-50px) scale(0.9);
}

.slide-right-leave-to {
  opacity: 0;
  transform: translateX(50px) scale(0.9);
}

/* !* 确保路由视图容器有相对定位 *!
.welcome-card > * {
  width: 100%;
} */
</style>
