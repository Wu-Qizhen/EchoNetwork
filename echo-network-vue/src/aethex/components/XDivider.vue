<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.4
-->
<!--
  使用方式：
  <template>
    <XDivider label="分割线" contentPosition="center" lineColor="gray"></XDivider>
  </template>
-->
<template>
  <div class="x-divider" :style="{width: width}">
    <div class="line" :style="{width: leftWidth,background: lineColor}"></div>
    <span class="label" :style="{color: labelColor}">{{ label }}</span>
    <div class="line" :style="{width: rightWidth,background: lineColor}"></div>
  </div>
</template>

<script>
export default {
  name: 'XDivider',
  props: {
    // 文字
    label: {
      type: String,
      default: ''
    },
    // 文字颜色
    labelColor: {
      type: String,
      default: 'gray'
    },
    // 内容位置，左 left，右 right，中 center
    contentPosition: {
      type: String,
      default: 'center'
    },
    // 分割线总宽度
    width: {
      type: [String, Number],
      default: '100%'
    },
    // 分割线颜色
    lineColor: {
      type: String,
      default: 'gray'
    }
  },
  watch: {
    contentPosition() {
      this.setLineWidth()
    }
  },
  data() {
    return {
      leftWidth: '50%',
      rightWidth: '50%'
    }
  },
  methods: {
    setLineWidth() {
      let p = this.contentPosition
      switch (p) {
        case 'center': {
          this.leftWidth = '50%'
          this.rightWidth = '50%'
          break
        }
        case 'left': {
          this.leftWidth = '10%'
          this.rightWidth = '90%'
          break
        }
        case 'right': {
          this.leftWidth = '90%'
          this.rightWidth = '10%'
          break
        }
      }
    }
  },
  mounted() {
    this.setLineWidth()
  }
}
</script>

<!--<script setup>
import { ref, computed, watch, onMounted } from 'vue'

// 定义 props
const props = defineProps({
  label: {
    type: String,
    default: ''
  },
  labelColor: {
    type: String,
    default: 'gray'
  },
  contentPosition: {
    type: String,
    default: 'center'
  },
  width: {
    type: [String, Number],
    default: '100%'
  },
  lineColor: {
    type: String,
    default: 'gray'
  }
})

// 定义响应式数据
const leftWidth = ref('50%')
const rightWidth = ref('50%')

// 定义方法
const setLineWidth = () => {
  switch (props.contentPosition) {
    case 'center':
      leftWidth.value = '50%'
      rightWidth.value = '50%'
      break
    case 'left':
      leftWidth.value = '10%'
      rightWidth.value = '90%'
      break
    case 'right':
      leftWidth.value = '90%'
      rightWidth.value = '10%'
      break
  }
}

// 监听器
watch(() => props.contentPosition, setLineWidth)

// 生命周期钩子
onMounted(setLineWidth)
</script>-->

<style scoped>
.x-divider {
  position: relative;
  display: flex;
  flex-direction: row;
  align-items: center;
  margin: 15px 0;
  /* color: gray; */

  .line {
    /* background: gray; */
    height: 1px;
  }

  .label {
    width: auto;
    padding: 0 12px;
    font-size: 14px;
    font-family: "MiSans Medium", sans-serif;
    text-align: center;
    transform: translateY(-1px);
    white-space: nowrap;
  }
}
</style>
