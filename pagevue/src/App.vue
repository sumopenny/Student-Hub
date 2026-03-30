<template>
  <div class="app-container">
    <router-view />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'

// 初始化主题色
const initTheme = () => {
  const savedTheme = localStorage.getItem('themeColor') || '#d4a5a5'

  // 应用主题色到 Element Plus 主色
  document.documentElement.style.setProperty('--el-color-primary', savedTheme)

  // 生成不同透明度的主题色变量
  const r = parseInt(savedTheme.slice(1, 3), 16)
  const g = parseInt(savedTheme.slice(3, 5), 16)
  const b = parseInt(savedTheme.slice(5, 7), 16)

  // 设置不同透明度的主题色
  document.documentElement.style.setProperty('--el-color-primary-light-1', `rgba(${r}, ${g}, ${b}, 0.9)`)
  document.documentElement.style.setProperty('--el-color-primary-light-3', `rgba(${r}, ${g}, ${b}, 0.7)`)
  document.documentElement.style.setProperty('--el-color-primary-light-5', `rgba(${r}, ${g}, ${b}, 0.5)`)
  document.documentElement.style.setProperty('--el-color-primary-light-7', `rgba(${r}, ${g}, ${b}, 0.3)`)
  document.documentElement.style.setProperty('--el-color-primary-light-8', `rgba(${r}, ${g}, ${b}, 0.2)`)
  document.documentElement.style.setProperty('--el-color-primary-light-9', `rgba(${r}, ${g}, ${b}, 0.1)`)
  document.documentElement.style.setProperty('--el-color-primary-dark-2', savedTheme)
}

onMounted(() => {
  initTheme()
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  background-color: #f0f2f5;
  min-height: 100vh;
  width: 100%;
}

#app {
  min-height: 100vh;
  width: 100%;
}

.app-container {
  min-height: 100vh;
  width: 100%;
  max-width: 100%;
}

/* 全局主题变量 */
:root {
  --el-color-primary: #909399;
  --sidebar-width: 200px;
  --header-height: 60px;
}

/* 深色模式基础样式 */
html.dark {
  --el-bg-color: #1a1a1a;
  --el-text-color-primary: #ffffff;
  --el-text-color-regular: #dcdfe6;
}
</style>
