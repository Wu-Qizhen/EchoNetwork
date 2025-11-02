<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.21
-->
<script setup lang="ts">
import './editor.css' // 引入 css
import {nextTick, onBeforeUnmount, ref, shallowRef, watch} from 'vue'
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

/*const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}*/

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
        @onCreated="handleCreatedWithContent"
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
