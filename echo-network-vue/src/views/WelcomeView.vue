<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.13
-->
<script setup>
import {onMounted, onUnmounted, ref} from "vue";
import XMultiFunBar from "@/aethex/components/XMultiFunBar.vue";
import XFooter from "@/aethex/components/XFooter.vue";
import router from "@/router/index.js";
/*import {ElMessage} from "element-plus";*/

const navItems = ref([
  {text: '平台介绍', id: 'about'},
  {text: '生态合作', id: 'cooperation'},
  {text: '使用协议', id: 'service'},
  {text: '隐私政策', id: 'privacy'},
])

const navButtons = ref([
  {
    type: 'text',
    content: '进入博客',
    title: '进入博客',
    dropdownItems: []
  }
])

const handleNavItemClick = (data) => {
  // console.log('导航项点击：', data)
  const item = data.item;
  if (item.id === 'service') {
    router.push('/service-terms')
  } else if (item.id === 'privacy') {
    router.push('/privacy-policy')
  } else if (item.id === 'cooperation') {
    ElMessage.warning('功能正在施工中')
    setTimeout(() => {
      router.go(0)
    }, 2000)
  } else {
    router.push('/welcome')
  }
}

const handleLogoClick = () => {
  // console.log('Logo 点击')
  router.push('/welcome')
}

const handleButtonClick = (data) => {
  // console.log('按钮点击：', data)
  const button = data.button

  // 根据按钮类型处理
  if (button.type === 'text' && button.content === '进入博客') {
    router.push('/')
  }
}

// 3D 倾斜效果
const interactElement = ref(null)

// 鼠标移动处理函数
const handleMouseMove = (e) => {
  if (!interactElement.value) return

  // 获取鼠标在视口中的位置
  const x = e.clientX / window.innerWidth
  const y = e.clientY / window.innerHeight

  // 计算倾斜角度
  const tiltX = -(y - 0.5) * 10
  const tiltY = (x - 0.5) * 10

  // 应用变换
  interactElement.value.style.transform = `rotateX(${tiltX}deg) rotateY(${tiltY}deg)`
}

// 鼠标离开处理函数
const handleMouseLeave = () => {
  if (!interactElement.value) return
  // 平滑回到原始位置
  interactElement.value.style.transform = 'rotateX(0deg) rotateY(0deg)'
}

const handleResize = () => {
  // 检测屏幕宽度是否小于 768px
  if (window.innerWidth < 768) {
    // 重定向到移动端页面
    window.location.href = '/';
  }
};

// 组件挂载时添加事件监听
onMounted(() => {
  handleResize();
  window.addEventListener('resize', handleResize);
  document.addEventListener('mousemove', handleMouseMove)
  if (interactElement.value) {
    interactElement.value.addEventListener('mouseleave', handleMouseLeave)
  }
})

// 组件卸载时移除事件监听
onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  document.removeEventListener('mousemove', handleMouseMove)
  if (interactElement.value) {
    interactElement.value.removeEventListener('mouseleave', handleMouseLeave)
  }
})
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
      :show-search="false"
  />

  <!-- 事件区 -->
  <div class="hero protected-content">
    <div class="hero-img">
      <img ref="interactElement" src="../../res/bg_code_intellix_fold.svg" alt="回声网络">
    </div>

    <div class="hero-content">
      <h1 class="en h1">Echo Network</h1>
      <h1 class="theme h1 spacer-top-l">回声网络</h1>
      <h2 class="en h2_5 spacer-top-l">Echo with depth in an era of superficiality</h2>
      <h2 class="theme h2 spacer-top-m">在浅薄的时代，做深刻的回响</h2>
      <a @click="router.push('/')" class="btn-l theme spacer-top-l">进入博客</a>
    </div>
  </div>

  <div class="instruction-creator protected-content">
    <div class="instruction-content">
      <h1 class="theme h1">面向 { 创作者 }</h1>
      <h2 class="theme h2_5 spacer-top-m">唤醒文字的力量，让每一篇思考都被听见</h2>

      <div class="instruction-creator-function spacer-top-l">
        <h2 class="theme h2_5">构建属于你的深度表达空间</h2>

        <div class="instruction-creator-function-option card spacer-top">
          <img src="../../res/ic_creating.svg" alt="创作" class="instruction-function-option-img">
          <div>
            <h2 class="en">发布文章、博客</h2>
            <p class="remark-highlight spacer-top-s">支持长文创作、发布与表达</p>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="instruction-thinker protected-content">
    <div class="instruction-content">
      <h1 class="theme h1">面向 { 思想者 }</h1>
      <h2 class="theme h2_5">连接深刻灵魂，在共鸣中拓展认知的边界</h2>

      <div class="instruction-thinker-function spacer-top">
        <h2 class="theme h2_5">打造基于信任的知识流动网络</h2>

        <div class="instruction-thinker-function-options">
          <div class="instruction-thinker-function-option card spacer-top">
            <img src="../../res/ic_learning.svg" alt="学习" class="instruction-function-option-img">
            <div>
              <h2 class="en">阅读、学习、讨论</h2>
              <p class="remark-highlight spacer-top-s">关注流、评论对话与深度推荐，好内容自然浮现</p>
            </div>
          </div>

          <div class="instruction-thinker-function-option card spacer-top">
            <img src="../../res/ic_circling.svg" alt="圈子" class="instruction-function-option-img">
            <div>
              <h2 class="en">加入圈子</h2>
              <p class="remark-highlight spacer-top-s">创建、加入、分享知识</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="next protected-content">
    <div class="next-img">
      <img src="../../res/logo_echo_network_curl.svg" alt="回声网络">
    </div>

    <div class="next-content">
      <h1 class="en h2">Thoughts give rise to sound,</h1>
      <h1 class="en h2">which reverberates into a web</h1>
      <h1 class="theme h1 spacer-top-l">声起于思，回荡成网</h1>
      <h2 class="en h2_5 spacer-top-l">Join the Echo and let the world hear your voice</h2>
      <h2 class="theme h2 spacer-top-m">加入回声，让世界听清你的思想</h2>
      <a @click="router.push('/')" class="btn-l theme spacer-top-l">立刻开始</a>
    </div>
  </div>

  <div class="honor protected-content">
    <h1 class="theme h1">“技术人的笔，不该只敲代码”</h1>
    <h2 class="theme h2_5 spacer-top-m">代码之外，是更深的思考，思想更值得被记录。欢迎技术人，在此写下你的洞见</h2>

    <div class="carousel-container spacer-top-m">
      <div class="carousel-track-lan">
        <div class="carousel-item">
          <img src="../../res/ic_java.svg" class="carousel-image" alt="Java">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_javascript.svg" class="carousel-image" alt="JavaScript">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_typescript.svg" class="carousel-image" alt="TypeScript">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_python.svg" class="carousel-image" alt="Python">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_dart.svg" class="carousel-image" alt="Dart">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_vbnet.svg" class="carousel-image" alt="Visual Basic.NET">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_csharp.svg" class="carousel-image" alt="C Sharp">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_php.svg" class="carousel-image" alt="PHP">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_go.svg" class="carousel-image" alt="Go">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_android.svg" class="carousel-image" alt="Android">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_kotlin.svg" class="carousel-image" alt="Kotlin">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_html.svg" class="carousel-image" alt="HTML">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_scala.svg" class="carousel-image" alt="Scala">
        </div>

        <!-- 重复一遍以实现无缝循环 -->

        <div class="carousel-item">
          <img src="../../res/ic_java.svg" class="carousel-image" alt="Java">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_javascript.svg" class="carousel-image" alt="JavaScript">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_typescript.svg" class="carousel-image" alt="TypeScript">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_python.svg" class="carousel-image" alt="Python">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_dart.svg" class="carousel-image" alt="Dart">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_vbnet.svg" class="carousel-image" alt="Visual Basic.NET">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_csharp.svg" class="carousel-image" alt="C Sharp">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_php.svg" class="carousel-image" alt="PHP">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_go.svg" class="carousel-image" alt="Go">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_android.svg" class="carousel-image" alt="Android">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_kotlin.svg" class="carousel-image" alt="Kotlin">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_html.svg" class="carousel-image" alt="HTML">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_scala.svg" class="carousel-image" alt="Scala">
        </div>
      </div>
    </div>

    <div class="carousel-container">
      <div class="carousel-track-tool">
        <div class="carousel-item">
          <img src="../../res/ic_android_studio.svg" class="carousel-image" alt="Android Studio">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_clion.svg" class="carousel-image" alt="CLion">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_intellij_idea.svg" class="carousel-image" alt="IntelliJ IDEA">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_phpstorm.svg" class="carousel-image" alt="PhpStorm">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_pycharm.svg" class="carousel-image" alt="PyCharm">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_webstorm.svg" class="carousel-image" alt="WebStorm">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_vscode.svg" class="carousel-image" alt="VS Code">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_visual_studio.svg" class="carousel-image" alt="Visual Studio">
        </div>

        <!-- 重复一遍以实现无缝循环 -->

        <div class="carousel-item">
          <img src="../../res/ic_android_studio.svg" class="carousel-image" alt="Android Studio">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_clion.svg" class="carousel-image" alt="CLion">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_intellij_idea.svg" class="carousel-image" alt="IntelliJ IDEA">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_phpstorm.svg" class="carousel-image" alt="PhpStorm">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_pycharm.svg" class="carousel-image" alt="PyCharm">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_webstorm.svg" class="carousel-image" alt="WebStorm">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_vscode.svg" class="carousel-image" alt="VS Code">
        </div>

        <div class="carousel-item">
          <img src="../../res/ic_visual_studio.svg" class="carousel-image" alt="Visual Studio">
        </div>
      </div>
    </div>
  </div>

  <XFooter/>
</template>

<style scoped>
.hero {
  width: 100%;
  height: 100vh;
  position: relative;
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #110a39 0%, black 40%, black 100% /* , #011b22 100% */);
  padding: 0 5%;
}

.hero-img {
  position: absolute;
  top: 50%;
  right: -300px;
  margin-top: 50px;
  transform: translateY(-50%);
  height: 175%;
  width: auto;
  overflow: hidden;
  z-index: 1;
  perspective: 1000px; /* 添加透视效果 */
}

.hero-img img {
  height: 100%;
  width: auto;
  object-fit: cover;
  filter: drop-shadow(0 10px 20px var(--theme-color-fade));
  transform-style: preserve-3d; /* 启用 3D 变换 */
}

.hero-content {
  position: relative;
  z-index: 2; /* 确保文字在图片上层 */
  padding-right: 20px;
}

.instruction-creator {
  width: 100%;
  height: 100vh;
  position: relative;
  display: flex;
  align-items: center;
  background: linear-gradient(330deg, rgba(31, 44, 162) 0%, rgba(11, 22, 120, 0.8) 10%, black 50%, black 75%, rgba(31, 44, 162) 90%, rgba(84, 57, 190) 100%);
  padding: 0 5%;
}

.instruction-content {
  position: relative;
  flex: 1;
  z-index: 2; /* 确保文字在图片上层 */
  padding-right: 20px;
}

.instruction-creator-function {
  flex: 1;
  background: rgba(10, 14, 54, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 30px 30px;
}

.instruction-creator-function-option {
  /* background: linear-gradient(330deg, rgba(98, 43, 205), rgba(19, 16, 90), black 50%, black 100%); */
  background: radial-gradient(circle at left bottom, rgba(98, 43, 205, 0.8), rgba(19, 16, 90, 0.8), black 70%, transparent 100%);
  width: 50%;
  display: flex;
  padding: 20px 20px;
  align-items: center;
  gap: 20px;
}

.instruction-function-option-img {
  height: 50px;
  width: auto;
}

.instruction-thinker {
  width: 100%;
  height: 100vh;
  position: relative;
  display: flex;
  align-items: center;
  background: linear-gradient(330deg, rgba(244, 253, 8, 0.6) 0%, rgba(85, 224, 116, 0.3) 20%, black 50%, black 75%, rgba(121, 251, 161, 0.6) 90%, rgba(17, 216, 233, 0.8) 100%);
  padding: 0 5%;
}

.instruction-thinker-function {
  flex: 1;
  background: rgba(85, 224, 116, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 30px 30px;
}

.instruction-thinker-function-options {
  width: 100%;
  display: flex;
  gap: 25px;
}

.instruction-thinker-function-option {
  /* background: linear-gradient(330deg, rgba(98, 43, 205), rgba(19, 16, 90), black 50%, black 100%); */
  background: radial-gradient(circle at left bottom, rgba(244, 253, 8, 0.4), rgba(85, 224, 116, 0.4), black 70%, transparent 100%);
  width: 50%;
  display: flex;
  padding: 20px 20px;
  align-items: center;
  gap: 20px;
}

.next {
  width: 100%;
  height: 100vh;
  position: relative;
  /* overflow: hidden; */
  display: flex;
  align-items: center;
  padding: 0 5%;
  background: radial-gradient(circle at center, rgba(109, 228, 116, 0.4), black 40%, black 100%);
  /* background-image: url("../../res/bg_next.jpg"); */
  /* background-size: 100% auto; */
}

.next-img {
  position: absolute;
  top: 50%;
  left: -160px;
  margin-top: 50px;
  transform: translateY(-50%);
  height: 155%;
  width: auto;
  z-index: 1;
}

.next-img img {
  height: 100%;
  width: auto;
  object-fit: cover;
}

.next-content {
  position: relative;
  margin-left: auto;
  z-index: 2;
  padding-left: 20px;
}

.honor {
  width: 100%;
  height: 100vh;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 0 0;
  background-image: url("../../res/bg_honor.png");
  background-size: auto 100%;
  background-position: center;
  /* background: radial-gradient(circle 2000px at center 150%, rgba(10, 83, 242, 0.8) 0%, black 40%, black 100%); */
}

.carousel-container {
  width: 100%;
  overflow: hidden;
  position: relative;
  /* margin: 0 0; */
  /* border-radius: 12px; */
  /* box-shadow: 0 10px 30px rgba(0, 0, 0, 0.4); */
}

.carousel-track-lan {
  display: flex;
  animation: scroll-lan 30s linear infinite;
}

.carousel-track-tool {
  display: flex;
  animation: scroll-tool 30s linear infinite;
}

.carousel-track-lan:hover {
  animation-play-state: paused;
}

.carousel-track-tool:hover {
  animation-play-state: paused;
}

.carousel-item {
  flex: 0 0 auto;
  width: 150px;
  height: 150px;
  margin: 0 0;
  padding: 30px 30px;
  border-radius: 8px;
  /* overflow: hidden; */
  transition: transform 0.3s ease;
}

.carousel-item:hover {
  transform: scale(1.05);
  /* box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3); */
}

.carousel-image {
  width: 50px;
  height: auto;
  object-fit: cover;
}

@keyframes scroll-lan {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(calc(-150px * 13));
  }
}

@keyframes scroll-tool {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(calc(-150px * 7));
  }
}
</style>
