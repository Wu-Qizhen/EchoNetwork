<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.12
-->
<!--
  使用方式：
  <template>
    <XBackgroundSpace>
        其它组件
    </XBackgroundSpace>
  </template>
-->
<template>
  <div class="background-container">

    <div class="sky-bg" aria-hidden="true"></div>

    <!-- 星空画布 -->
    <canvas ref="universeCanvas" class="universe-canvas"></canvas>

    <!-- 动态背景 -->
    <div class="animated-bg" aria-hidden="true"></div>

    <!-- 内容插槽 -->
    <div class="content-wrapper">
      <slot></slot>
    </div>
  </div>
</template>

<script>
import {onMounted, onUnmounted, ref} from 'vue'

export default {
  name: 'XBackgroundSpace',
  setup() {
    const universeCanvas = ref(null)
    let animationId = null
    let resizeObserver = null

    // 星空动画代码
    const initUniverse = () => {
      const canvas = universeCanvas.value
      if (!canvas) return

      // 获取容器尺寸
      const container = canvas.parentElement
      const containerRect = container.getBoundingClientRect()
      const containerWidth = containerRect.width
      const containerHeight = containerRect.height

      // 设置canvas尺寸
      canvas.width = containerWidth
      canvas.height = containerHeight

      // 星空动画变量
      let n, e, i, h, t = 0.05
      const s = canvas
      let o = true
      const a = "180,184,240"
      const r = "226,225,142"
      const d = "226,225,224"
      const c = []

      // 调整尺寸函数
      const f = () => {
        const containerRect = container.getBoundingClientRect()
        n = containerRect.width
        e = containerRect.height
        i = 0.216 * n
        s.width = n
        s.height = e
      }

      // 绘制函数
      const u = () => {
        h.clearRect(0, 0, n, e)
        for (let t = c.length, i = 0; i < t; i++) {
          const s = c[i]
          s.move()
          s.fadeIn()
          s.fadeOut()
          s.draw()
        }
      }

      // 星星对象构造函数
      const y = function () {
        this.reset = function () {
          this.giant = m(3)
          this.comet = !this.giant && !o && m(10)
          this.x = l(0, n - 10)
          this.y = l(0, e)
          this.r = l(1.1, 2.6)
          this.dx = l(t, 6 * t) + (this.comet + 1 - 1) * t * l(50, 120) + 2 * t
          this.dy = -l(t, 6 * t) - (this.comet + 1 - 1) * t * l(50, 120)
          this.fadingOut = null
          this.fadingIn = true
          this.opacity = 0
          this.opacityTresh = l(0.2, 1 - 0.4 * (this.comet + 1 - 1))
          this.do = l(5e-4, 0.002) + 0.001 * (this.comet + 1 - 1)
        }

        this.fadeIn = function () {
          this.fadingIn && (this.fadingIn = !(this.opacity > this.opacityTresh), this.opacity += this.do)
        }

        this.fadeOut = function () {
          this.fadingOut && (this.fadingOut = !(this.opacity < 0), this.opacity -= this.do / 2, (this.x > n || this.y < 0) && (this.fadingOut = false, this.reset()))
        }

        this.draw = function () {
          if (h.beginPath(), this.giant) {
            h.fillStyle = "rgba(" + a + "," + this.opacity + ")"
            h.arc(this.x, this.y, 2, 0, 2 * Math.PI, false)
          } else if (this.comet) {
            h.fillStyle = "rgba(" + d + "," + this.opacity + ")"
            h.arc(this.x, this.y, 1.5, 0, 2 * Math.PI, false)
            for (let t = 0; t < 30; t++) {
              h.fillStyle = "rgba(" + d + "," + (this.opacity - this.opacity / 20 * t) + ")"
              h.rect(this.x - this.dx / 4 * t, this.y - this.dy / 4 * t - 2, 2, 2)
              h.fill()
            }
          } else {
            h.fillStyle = "rgba(" + r + "," + this.opacity + ")"
            h.rect(this.x, this.y, this.r, this.r)
          }
          h.closePath()
          h.fill()
        }

        this.move = function () {
          this.x += this.dx
          this.y += this.dy
          this.fadingOut === false && this.reset()
          ;(this.x > n - n / 4 || this.y < 0) && (this.fadingOut = true)
        }

        setTimeout(function () {
          o = false
        }, 50)
      }

      // 辅助函数
      const m = (t) => {
        return Math.floor(1000 * Math.random()) + 1 < 10 * t
      }

      const l = (t, i) => {
        return Math.random() * (i - t) + t
      }

      // 初始化
      f()

      // 监听容器尺寸变化
      resizeObserver = new ResizeObserver(f)
      resizeObserver.observe(container)

      // 设置canvas上下文
      h = s.getContext("2d")
      for (let t = 0; t < i; t++) {
        c[t] = new y()
        c[t].reset()
      }
      u()

      // 动画循环
      const animate = () => {
        u()
        animationId = requestAnimationFrame(animate)
      }
      animate()
    }

    // 组件挂载时初始化
    onMounted(() => {
      initUniverse()
    })

    // 组件卸载时清理
    onUnmounted(() => {
      if (animationId) {
        cancelAnimationFrame(animationId)
      }
      if (resizeObserver) {
        resizeObserver.disconnect()
      }
    })

    return {
      universeCanvas
    }
  }
}
</script>

<style scoped>
.background-container {
  position: relative;
  width: 100%;
  min-height: 100vh;
  background-color: #000; /* 超出部分用纯黑背景 */
  overflow: hidden;
}

.sky-bg {
  position: absolute;
  width: 100vw;
  min-height: 100vh;
  background-image: url("../../../res/bg_aurora_4.png");
  background-size: cover;
  overflow: hidden;
}

.universe-canvas {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none;
}

.animated-bg {
  position: absolute;
  top: -20%;
  left: -20%;
  right: -20%;
  bottom: -20%;
  z-index: 2;
  pointer-events: none;
  background: radial-gradient(600px 300px at 20% 20%, rgba(59, 130, 246, .3), transparent 60%),
  radial-gradient(600px 300px at 80% 30%, rgba(14, 165, 233, .3), transparent 60%),
  radial-gradient(700px 400px at 50% 30%, rgba(99, 102, 241, .3), transparent 60%);
  filter: blur(20px);
  animation: floatBg 18s ease-in-out infinite alternate;
  opacity: .6;
}

@keyframes floatBg {
  0% {
    transform: translate3d(0, 0, 0) scale(1);
  }
  100% {
    transform: translate3d(2%, -3%, 0) scale(1.03);
  }
}

@media (prefers-reduced-motion: reduce) {
  .animated-bg {
    animation: none;
  }
}

.content-wrapper {
  position: relative;
  z-index: 3;
  width: 100%;
  min-height: 100vh;
}
</style>
