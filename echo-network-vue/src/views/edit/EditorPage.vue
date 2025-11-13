<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.21
-->
<script setup lang="ts">
import './editor.css' // 引入 css
import {nextTick, onBeforeUnmount, ref, shallowRef, watch, onMounted, onUnmounted} from 'vue'
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import XSpacer from "@/aethex/components/XSpacer.vue"
import {IToolbarConfig} from '@wangeditor/editor'

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
const valueHtml = ref('')

// 工具栏配置
const toolbarConfig: Partial<IToolbarConfig> = {
  excludeKeys: [
    'fullScreen',     // 全屏
    'uploadImage',    // 图片
    'insertImage',    // 图片
    'insertTable',    // 表格
    'insertVideo'     // 视频
  ]
}

const editorConfig = {placeholder: '文章内容'}
const mode = ref('simple') // 或 'default'

// 工具栏固定相关
const toolbarRef = ref<HTMLElement>()
const toolbarFixed = ref(false)
const toolbarWrapperRef = ref<HTMLElement>()
const cloneToolbarRef = ref<HTMLElement>()

// 处理滚动事件
const handleScroll = () => {
  if (!toolbarRef.value || !toolbarWrapperRef.value) return

  const scrollTop = window.pageYOffset || document.documentElement.scrollTop
  const wrapperRect = toolbarWrapperRef.value.getBoundingClientRect()
  const threshold = 85 // 距离顶部固定

  // 计算包装器距离顶部的绝对位置
  const wrapperTop = wrapperRect.top + scrollTop

  if (scrollTop > wrapperTop - threshold) {
    if (!toolbarFixed.value) {
      toolbarFixed.value = true

      // 创建工具栏的克隆并固定在顶部
      const toolbarRect = toolbarRef.value.getBoundingClientRect()
      const wrapperWidth = toolbarWrapperRef.value.offsetWidth

      // 创建克隆工具栏
      const clone = toolbarRef.value.cloneNode(true) as HTMLElement
      clone.style.position = 'fixed'
      clone.style.top = `${threshold}px`
      clone.style.left = `${toolbarRect.left}px`
      clone.style.width = `${wrapperWidth}px`
      clone.style.zIndex = '1000'
      // clone.style.boxShadow = '0 2px 4px rgba(255, 255, 255, 0.1)';
      // clone.style.background = 'var(--dark-bg-s)'
      // clone.style.borderRadius = '10px'
      // clone.style.border = '1px solid var(--dark-line-m)'
      // clone.style.padding = '10px'
      clone.style.boxSizing = 'border-box'

      // 添加到 body
      document.body.appendChild(clone)
      cloneToolbarRef.value = clone

      // 隐藏原始工具栏但保留占位空间
      toolbarRef.value.style.visibility = 'hidden'
    }
  } else {
    if (toolbarFixed.value) {
      toolbarFixed.value = false

      // 移除克隆工具栏
      if (cloneToolbarRef.value && document.body.contains(cloneToolbarRef.value)) {
        document.body.removeChild(cloneToolbarRef.value)
        cloneToolbarRef.value = undefined
      }

      // 显示原始工具栏
      toolbarRef.value.style.visibility = 'visible'
    }
  }
}

// 处理窗口大小变化
const handleResize = () => {
  if (toolbarFixed.value && cloneToolbarRef.value && toolbarWrapperRef.value) {
    const wrapperWidth = toolbarWrapperRef.value.offsetWidth
    const toolbarRect = toolbarRef.value.getBoundingClientRect()

    cloneToolbarRef.value.style.width = `${wrapperWidth}px`
    cloneToolbarRef.value.style.left = `${toolbarRect.left}px`
  }
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const props = defineProps({
  modelValue: String
})

const emit = defineEmits(['update:modelValue'])

// 监听内容变化
watch(valueHtml, (newValue) => {
  emit('update:modelValue', newValue)
})

// 监听父组件传递的 modelValue 变化，更新编辑器内容
watch(() => props.modelValue, (newValue) => {
  if (newValue !== valueHtml.value) {
    valueHtml.value = newValue || ''

    // 如果编辑器已经创建，强制设置内容
    if (editorRef.value) {
      nextTick(() => {
        editorRef.value.setHtml(newValue || '')
      })
    }
  }
}, {immediate: true})

// 在编辑器创建后，如果已经有内容，设置内容
const handleCreatedWithContent = (editor) => {
  editorRef.value = editor
  // 如果已经有内容，设置到编辑器中
  if (props.modelValue) {
    nextTick(() => {
      editor.setHtml(props.modelValue)
    })
  }
}

onMounted(() => {
  // 添加事件监听
  window.addEventListener('scroll', handleScroll)
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  // 移除事件监听
  window.removeEventListener('scroll', handleScroll)
  window.removeEventListener('resize', handleResize)

  // 清理克隆的工具栏
  if (cloneToolbarRef.value && document.body.contains(cloneToolbarRef.value)) {
    document.body.removeChild(cloneToolbarRef.value)
  }
})
</script>

<template>
  <div class="editor-container">
    <!-- 工具栏包装器，用于计算位置 -->
    <div class="toolbar-wrapper" ref="toolbarWrapperRef">
      <!-- 原始工具栏 -->
      <div class="toolbar-original" ref="toolbarRef">
        <Toolbar
            :editor="editorRef"
            :defaultConfig="toolbarConfig"
            :mode="mode"
        />
      </div>
    </div>
    <XSpacer height="20px"/>
    <Editor
        class="editor"
        v-model="valueHtml"
        :defaultConfig="editorConfig"
        :mode="mode"
        @onCreated="handleCreatedWithContent"
    />
  </div>
</template>

<style scoped>
.editor-container {
  position: relative;
}

.toolbar-wrapper {
  position: relative;
}

.toolbar-original {
  transition: opacity 0.3s ease;
}

/* .toolbar {
  overflow: hidden;
  background-color: var(--dark-bg-s);
  border-radius: 10px;
  border: 1px solid var(--dark-line-m);
} */

.editor {
  /* overflow: hidden; */
  background-color: var(--dark-bg-s);
  border-radius: 10px;
  border: 1px solid var(--dark-line-m);
}
</style>
