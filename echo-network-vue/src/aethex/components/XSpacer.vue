<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.9
-->
<!--
  使用方式：
  <template>
    <x-spacer type="vertical" height="10px"></x-spacer>
  </template>
-->
<template>
  <div class="x-spacer" :style="spacerStyle"></div>
</template>

<script>
export default {
  name: 'XSpacer',
  props: {
    type: {
      type: String,
      default: 'vertical' // 'vertical' | 'horizontal'
    },
    height: {
      type: [String, Number],
      default: '10px'
    },
    width: {
      type: [String, Number],
      default: '10px'
    }
  },
  computed: {
    spacerStyle() {
      const style = {}
      if (this.type === 'vertical') {
        style.height = typeof this.height === 'number' ? `${this.height}px` : this.height
        style.width = '100%'
      } else {
        style.width = typeof this.width === 'number' ? `${this.width}px` : this.width
        style.height = '100%'
      }
      return style
    }
  }
}
</script>

<style scoped>
.x-spacer {
  flex: none;
}
</style>
