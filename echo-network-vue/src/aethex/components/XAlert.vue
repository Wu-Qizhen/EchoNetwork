<!--
  代码不注释，同事两行泪！（给！爷！写！）
  Elegance is not a dispensable luxury but a quality that decides between success and failure!
  Created by Wu Qizhen on 2025.10.31
-->
<!--
  使用方式：
  <template>
    <XAlert title="编辑模式"
                  description="您正在编辑已发布的文章"
                  type="warning"/>
  </template>
-->
<template>
  <div
      class="x-alert protected-content"
      :class="[
      `alert-${type}`,
      'alert-fixed'
    ]"
  >
    <div class="icon">
      <!--<el-icon :size="40" v-if="type === 'success'">
            <SuccessFilled/>
          </el-icon>
          <el-icon :size="40" v-if="type === 'warning'">
            <WarningFilled/>
          </el-icon>
          <el-icon :size="40" v-if="type === 'info'">
            <InfoFilled/>
          </el-icon>
          <el-icon :size="40" v-if="type === 'error'">
            <CircleCheckFilled/>
          </el-icon>-->
      <img v-if="type === 'success'" src="../../../res/ic_correct.svg" alt="">
      <img v-if="type === 'warning'" src="../../../res/ic_warning.svg" alt="">
      <img v-if="type === 'info'" src="../../../res/ic_info.svg" alt="">
      <img v-if="type === 'error'" src="../../../res/ic_error.svg" alt="">
    </div>

    <div class="content">
      <div class="title">{{ title }}</div>
      <div v-if="description" class="description">{{ description }}</div>
    </div>
  </div>
</template>

<script setup>
import {defineProps} from 'vue';

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  description: {
    type: String,
    default: ''
  },
  type: {
    type: String,
    default: 'info',
    validator: (value) => ['success', 'warning', 'info', 'error'].includes(value)
  }
});
</script>

<style scoped>
.x-alert {
  width: 100%;
  padding: 10px 20px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
}

/* 类型样式 */
.alert-success {
  background-color: rgba(var(--green-rgb), 0.2);
  color: var(--green);
}

.alert-warning {
  background-color: rgba(var(--yellow-rgb), 0.2);
  color: var(--yellow);
}

.alert-info {
  background-color: rgba(var(--theme-color-rgb), 0.2);
  color: var(--theme-color-lighten);
}

.alert-error {
  background-color: rgba(var(--red-rgb), 0.2);
  color: var(--red);
}

.icon {
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon img {
  width: 40px;
  height: 40px;
}

.content {
  display: flex;
  flex-direction: column;
  align-items: start;
  justify-content: center;
}

.title {
  font-size: 16px;
}

.description {
  margin-top: 3px;
  font-size: 14px;
  color: var(--dark-content-m);
}
</style>
