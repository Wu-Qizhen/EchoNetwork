<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.21
-->

<!--<script lang="ts">
import './editor.css' // 引入 css

import {onBeforeUnmount, ref, shallowRef, onMounted} from 'vue'
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import XSpacer from "@/aethex/components/XSpacer.vue";

export default {
  components: {XSpacer, Editor, Toolbar},
  setup() {
    // 编辑器实例，必须用 shallowRef
    const editorRef = shallowRef()

    // 内容 HTML
    const valueHtml = ref('<p>hello</p>')

    // 模拟 Ajax 异步获取内容
    onMounted(() => {
      setTimeout(() => {
        valueHtml.value = ''
      }, 1500)
    })

    const toolbarConfig = {}
    const editorConfig = {placeholder: '文章内容'}

    // 组件销毁时，也及时销毁编辑器
    onBeforeUnmount(() => {
      const editor = editorRef.value
      if (editor == null) return
      editor.destroy()
    })

    const handleCreated = (editor) => {
      editorRef.value = editor // 记录 editor 实例，重要！
    }

    return {
      editorRef,
      valueHtml,
      mode: 'simple', // 或 'default'
      toolbarConfig,
      editorConfig,
      handleCreated,
    }
  },
}
</script>-->

<script setup lang="ts">
import './editor.css' // 引入 css
import {onBeforeUnmount, onMounted, ref, shallowRef, watch} from 'vue'
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import XSpacer from "@/aethex/components/XSpacer.vue"
import {IToolbarConfig} from '@wangeditor/editor'

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
const valueHtml = ref('')

// 模拟 Ajax 异步获取内容
onMounted(() => {
  setTimeout(() => {
    valueHtml.value = ''
  }, 1500)
})

// TODO 代码块文字重叠
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

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}

const props = defineProps({
  modelValue: String
})

const emit = defineEmits(['update:modelValue'])

// 监听内容变化
watch(valueHtml, (newValue) => {
  emit('update:modelValue', newValue)
})
</script>

<template>
  <div>
    <Toolbar
        :editor="editorRef"
        :defaultConfig="toolbarConfig"
        :mode="mode"
    />
    <XSpacer height="20px"/>
    <Editor
        class="editor"
        v-model="valueHtml"
        :defaultConfig="editorConfig"
        :mode="mode"
        @onCreated="handleCreated"
    />
  </div>
</template>

<style scoped>
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
