<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.9
-->
<!--
  使用方式：
  <template>
    <XFooter/>
  </template>
-->
<template>
  <footer
    class="x-footer"
    :style="{ transform: `translateY(${150 - footerVisible}%)` }"
  >
    <div class="footer-service spacer-bottom">
      <div class="footer-service-function">
        <p class="zh main white">联系我们</p>
        <p class="zh sub remark-highlight">联系邮箱：3162946784@qq.com</p>
        <p class="zh sub remark-highlight">加入我们：hire@code-intellix.com</p>
      </div>

      <div class="footer-service-function">
        <p class="zh main white">开发套件</p>
        <a href="#">幻羲智构矩阵</a>
      </div>

      <div class="footer-service-function">
        <p class="zh main white">解决方案</p>
        <a href="#">穿戴设备</a>
      </div>

      <div class="footer-service-function">
        <p class="zh main white">服务支持</p>
        <a href="#">技术支持</a>
        <a href="#">文档</a>
      </div>

      <div class="footer-service-function">
        <p class="zh main white">计划</p>
        <a href="#">开源</a>
      </div>
    </div>

    <div class="footer-studio spacer-top-s spacer-bottom">
      <a href="#"><img src="../../../res/logo_code_intellix_text.svg" alt="Code IntelliX"></a>

      <div class="footer-studio-workspace">
        <a href="#"><img src="../../../res/logo_intellic_lab.png" alt="IntelliC Lab">IntelliC Lab</a>
      </div>

      <div class="footer-studio-workspace">
        <a href="#"><img src="../../../res/logo_intellid_studio.png" alt="IntelliD Studio">IntelliD Studio</a>
      </div>

      <div class="footer-studio-workspace">
        <a href="#"><img src="../../../res/logo_intellia_visual.png" alt="IntelliA Visual">IntelliA Visual</a>
      </div>
    </div>

    <hr class="divider">

    <div class="footer-contact spacer-top">
      <a href="#"><img src="../../../res/ic_qq.svg" alt="QQ"></a>
      <a href="#"><img src="../../../res/ic_wechat.svg" alt="微信"></a>
      <a href="#"><img src="../../../res/ic_microblog.svg" alt="微博"></a>
      <a href="#"><img src="../../../res/ic_dingtalk.svg" alt="钉钉"></a>
      <a href="#"><img src="../../../res/ic_facebook.svg" alt="Facebook"></a>
      <a href="#"><img src="../../../res/ic_linkin.svg" alt="LinkIn"></a>
      <a href="#"><img src="../../../res/ic_instagram.svg" alt="Instagram"></a>
      <a href="#"><img src="../../../res/ic_github.svg" alt="GitHub"></a>
      <a href="#"><img src="../../../res/ic_x.svg" alt="X"></a>
      <a href="#"><img src="../../../res/ic_tiktok.svg" alt="TikTok"></a>
      <a href="#"><img src="../../../res/ic_youtube.svg" alt="YouTube"></a>
      <a href="#"><img src="../../../res/ic_bilibili.svg" alt="哔哩哔哩"></a>
    </div>

    <div class="footer-links spacer-top">
      <a class="remark-highlight" href="#">隐私安全</a>
      <a class="remark-highlight" href="#">隐私声明</a>
      <a class="remark-highlight" href="#">使用条款</a>
      <a class="remark-highlight" href="#">反馈</a>
    </div>

    <div class="footer-bottom spacer-top">
      <div class="footer-copyright">
        <p class="en remark-normal">Copyright © 2023-2025 Code IntelliX</p>
      </div>

      <div class="footer-build">
        <p class="en remark-normal">Developed with
          <a class="remark-highlight" href="https://www.jetbrains.com/zh-cn/">IntelliJ IDEA</a>
          by
          <a class="remark-highlight" href="https://github.com/WuQizhen-GitHub">Wu Qizhen</a>
        </p>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'XFooter',
  data() {
    return {
      footerVisible: 0,
      isAtBottom: false,
      isScrolling: false,
      scrollTimeout: null
    }
  },
  methods: {
    checkIfAtBottom() {
      const scrollTop = window.pageYOffset || document.documentElement.scrollTop
      const scrollHeight = document.documentElement.scrollHeight
      const clientHeight = document.documentElement.clientHeight
      return scrollHeight - scrollTop - clientHeight < 50
    },
    handleWheel(e) {
      if (!this.isAtBottom) return

      const delta = e.deltaY || e.detail || (-e.wheelDelta)
      const atMin = this.footerVisible <= 0 && delta < 0
      const atMax = this.footerVisible >= 150 && delta > 0

      if (atMin || atMax) {
        return
      }

      e.preventDefault()

      if (delta > 0) {
        this.footerVisible = Math.min(150, this.footerVisible + 30)
      } else {
        this.footerVisible = Math.max(0, this.footerVisible - 30)
      }

      this.isScrolling = true
      clearTimeout(this.scrollTimeout)
      this.scrollTimeout = setTimeout(() => {
        this.isScrolling = false
      }, 150)
    },
    handleScroll() {
      this.isAtBottom = this.checkIfAtBottom()
      if (!this.isAtBottom && this.footerVisible > 0) {
        this.footerVisible = 0
      }
    }
  },
  mounted() {
    window.addEventListener('scroll', this.handleScroll)
    window.addEventListener('wheel', this.handleWheel, { passive: false })
    window.addEventListener('DOMMouseScroll', this.handleWheel, { passive: false })

    this.isAtBottom = this.checkIfAtBottom()
  },
  beforeUnmount() {
    window.removeEventListener('scroll', this.handleScroll)
    window.removeEventListener('wheel', this.handleWheel)
    window.removeEventListener('DOMMouseScroll', this.handleWheel)
    clearTimeout(this.scrollTimeout)
  }
}
</script>

<style scoped>
.x-footer {
  position: fixed;
  bottom: 20px;
  left: 20px;
  right: 20px;
  margin: 0;
  display: flex;
  flex-direction: column;
  background: rgba(30, 35, 47, 0.8);
  padding: 20px 0;
  border-radius: 20px;
  box-shadow: 0 5px 50px black;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  justify-content: start;
  border: 1px solid rgba(255, 255, 255, 0.2);
  z-index: 999;
  transition: all 0.5s ease-out;
  transform: translateY(150%); /* 初始状态隐藏在下方 */
}

/* .footer.visible {
  transform: translateY(0); !* 显示状态 *!
} */

.footer-bottom {
  margin-left: 30px;
  margin-right: 30px;
  display: flex;
  justify-content: space-between;
  align-items: baseline;
}

.footer-links {
  margin-left: 30px;
  margin-right: 30px;
  display: flex;
  align-items: baseline;
  gap: 20px;
}

.footer-contact {
  margin-left: 30px;
  margin-right: 30px;
  display: flex;
  align-items: center;
  gap: 20px;
}

.footer-contact a img {
  width: 20px;
  height: auto;
}

.footer-studio {
  margin-left: 30px;
  margin-right: 30px;
  display: flex;
  align-items: baseline;
  gap: 20px;
}

.footer-studio a img {
  height: 20px;
  width: auto;
}

.footer-studio-workspace {
  height: 20px;
  width: auto;
}

.footer-studio-workspace a {
  display: flex;
  gap: 5px;
  align-items: center;
  text-decoration: none;
  color: white;
  font-family: "Google Sans Medium", sans-serif;
}

.footer-service {
  margin-left: 30px;
  margin-right: 30px;
  display: flex;
  align-items: start;
  gap: 100px;
}

.footer-service-function {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.footer-service-function a {
  text-decoration: none;
  color: #a5a5a7;
  font-size: 0.8em;
  font-family: "MiSans Medium", sans-serif;
}

.divider {
  border: 0; /* 移除默认边框 */
  height: 1px; /* 设置高度 */
  background: rgba(255, 255, 255, 0.2); /* 渐变背景 */
}
</style>
