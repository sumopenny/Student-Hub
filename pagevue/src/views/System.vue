<template>
  <div class="system-container">
    <!-- 页面标题区 -->
    <div class="page-header">
      <div class="header-title">
        <div class="title-icon-wrapper">
          <el-icon class="title-icon"><Setting /></el-icon>
        </div>
        <div class="title-text">
          <h2>系统设置</h2>
          <p class="subtitle">个性化您的系统偏好</p>
        </div>
      </div>
    </div>

    <div class="settings-content">
      <!-- 主题色设置卡片 -->
      <el-card class="setting-card" shadow="never">
        <template #header>
          <div class="card-header">
            <div class="header-left">
              <div class="header-icon">
                <el-icon><Brush /></el-icon>
              </div>
              <div class="header-info">
                <span class="header-title-text">部分按键主题色设置</span>
                <span class="header-desc">选择您喜欢的按键颜色</span>
              </div>
            </div>
          </div>
        </template>

        <div class="theme-setting">
          <div class="color-palette">
            <div
              v-for="color in themeColors"
              :key="color.value"
              class="color-option"
              :class="{ active: currentTheme === color.value }"
              :style="{ backgroundColor: color.value }"
              @click="changeTheme(color.value)"
              :title="color.name"
            >
              <el-icon v-if="currentTheme === color.value"><Check /></el-icon>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 系统操作卡片 -->
      <el-card class="setting-card" shadow="never">
        <template #header>
          <div class="card-header">
            <div class="header-left">
              <div class="header-icon action-icon">
                <el-icon><Operation /></el-icon>
              </div>
              <div class="header-info">
                <span class="header-title-text">系统操作</span>
                <span class="header-desc">管理系统设置和数据</span>
              </div>
            </div>
          </div>
        </template>

        <div class="action-list">
          <div class="action-item">
            <div class="action-info">
              <div class="action-icon-wrapper reset">
                <el-icon><RefreshLeft /></el-icon>
              </div>
              <div class="action-text">
                <span class="action-title">恢复默认</span>
                <span class="action-desc">恢复系统默认主题色设置</span>
              </div>
            </div>
            <el-button class="action-btn secondary-btn" @click="resetSettings">
              <el-icon><RefreshRight /></el-icon>
              重置
            </el-button>
          </div>

          <div class="action-item">
            <div class="action-info">
              <div class="action-icon-wrapper clear">
                <el-icon><Delete /></el-icon>
              </div>
              <div class="action-text">
                <span class="action-title">清除缓存</span>
                <span class="action-desc">清除本地缓存数据（保留登录信息）</span>
              </div>
            </div>
            <el-button type="danger" class="action-btn danger-btn" @click="clearCache">
              <el-icon><Brush /></el-icon>
              清除
            </el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Setting,
  Brush,
  Check,
  Operation,
  RefreshLeft,
  RefreshRight,
  Delete
} from '@element-plus/icons-vue'

// 主题色选项 - 莫兰迪色系
const themeColors = [
  { name: '莫兰迪粉', value: '#d4a5a5' },
  { name: '莫兰迪蓝', value: '#7c9cb4' },
  { name: '莫兰迪绿', value: '#8fb9a8' },
  { name: '莫兰迪紫', value: '#a89bb8' },
  { name: '莫兰迪橙', value: '#d4a574' },
  { name: '莫兰迪青', value: '#7bb3b0' }
]

// 设置状态
const currentTheme = ref('#d4a5a5')

// 修改主题色 - 应用到全局并自动保存
const changeTheme = (color) => {
  currentTheme.value = color

  // 应用主题色到 Element Plus 主色
  document.documentElement.style.setProperty('--el-color-primary', color)

  // 生成不同透明度的主题色变量，用于全局配色
  const r = parseInt(color.slice(1, 3), 16)
  const g = parseInt(color.slice(3, 5), 16)
  const b = parseInt(color.slice(5, 7), 16)

  // 设置不同透明度的主题色
  document.documentElement.style.setProperty('--el-color-primary-light-1', `rgba(${r}, ${g}, ${b}, 0.9)`)
  document.documentElement.style.setProperty('--el-color-primary-light-3', `rgba(${r}, ${g}, ${b}, 0.7)`)
  document.documentElement.style.setProperty('--el-color-primary-light-5', `rgba(${r}, ${g}, ${b}, 0.5)`)
  document.documentElement.style.setProperty('--el-color-primary-light-7', `rgba(${r}, ${g}, ${b}, 0.3)`)
  document.documentElement.style.setProperty('--el-color-primary-light-8', `rgba(${r}, ${g}, ${b}, 0.2)`)
  document.documentElement.style.setProperty('--el-color-primary-light-9', `rgba(${r}, ${g}, ${b}, 0.1)`)
  document.documentElement.style.setProperty('--el-color-primary-dark-2', color)

  // 保存到本地存储
  localStorage.setItem('themeColor', color)
  localStorage.setItem('systemSettings', JSON.stringify({ themeColor: color }))

  ElMessage.success('按键主题色已更换')
}

// 恢复默认设置
const resetSettings = () => {
  currentTheme.value = '#d4a5a5'

  // 应用默认设置
  changeTheme('#d4a5a5')

  localStorage.removeItem('systemSettings')
  ElMessage.success('已恢复默认设置')
}

// 清除缓存
const clearCache = () => {
  // 清除除了用户登录信息外的其他缓存
  const userInfo = localStorage.getItem('userInfo')
  const token = localStorage.getItem('token')

  localStorage.clear()

  if (userInfo) localStorage.setItem('userInfo', userInfo)
  if (token) localStorage.setItem('token', token)

  ElMessage.success('缓存已清除')
}

// 从本地存储加载设置
const loadSettings = () => {
  try {
    const settingsStr = localStorage.getItem('systemSettings')
    if (settingsStr) {
      const settings = JSON.parse(settingsStr)
      currentTheme.value = settings.themeColor || '#d4a5a5'

      // 应用设置
      changeTheme(currentTheme.value)
    }
  } catch (e) {
    console.error('加载设置失败', e)
  }
}

onMounted(() => {
  loadSettings()
})
</script>

<style scoped>
/* 页面容器 */
.system-container {
  max-width: 1000px;
  margin: 0 auto;
}

/* 页面标题区 */
.page-header {
  margin-bottom: 24px;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 16px;
}

.title-icon-wrapper {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(212, 165, 165, 0.3);
}

.title-icon {
  font-size: 28px;
  color: #ffffff;
}

.title-text h2 {
  margin: 0 0 4px 0;
  font-size: 24px;
  font-weight: 600;
  color: #6b5b5b;
}

.subtitle {
  margin: 0;
  font-size: 14px;
  color: #9ca3af;
}

/* 设置内容 */
.settings-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 设置卡片 */
.setting-card {
  border-radius: 16px;
  border: none;
  background: #ffffff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.setting-card :deep(.el-card__header) {
  padding: 20px 24px;
  border-bottom: 1px solid #f3f4f6;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-size: 20px;
}

.header-icon.action-icon {
  background: linear-gradient(135deg, #7c9cb4 0%, #9ab4c7 100%);
}

.header-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.header-title-text {
  font-size: 16px;
  font-weight: 600;
  color: #6b5b5b;
}

.header-desc {
  font-size: 13px;
  color: #9ca3af;
}

/* 主题色设置 */
.theme-setting {
  padding: 24px;
}

.color-palette {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.color-option {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  cursor: pointer;
  position: relative;
  transition: all 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  font-size: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.color-option:hover {
  transform: scale(1.1) translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.color-option.active {
  border: 3px solid #ffffff;
  box-shadow: 0 0 0 3px currentColor, 0 8px 24px rgba(0, 0, 0, 0.2);
}

/* 操作列表 */
.action-list {
  padding: 12px 24px;
}

.action-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 0;
  border-bottom: 1px solid #f3f4f6;
  transition: all 0.3s ease;
}

.action-item:last-child {
  border-bottom: none;
}

.action-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.action-icon-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  transition: all 0.3s ease;
}

.action-icon-wrapper.reset {
  background: linear-gradient(135deg, #dbeafe 0%, #bfdbfe 100%);
  color: #3b82f6;
}

.action-icon-wrapper.clear {
  background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%);
  color: #ef4444;
}

.action-text {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.action-title {
  font-size: 15px;
  font-weight: 600;
  color: #6b5b5b;
}

.action-desc {
  font-size: 13px;
  color: #9ca3af;
}

.action-btn {
  height: 40px;
  padding: 0 20px;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.3s ease;
}

.action-btn[type="primary"] {
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  border: none;
  box-shadow: 0 4px 12px rgba(212, 165, 165, 0.3);
}

.action-btn[type="primary"]:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(212, 165, 165, 0.4);
}

.secondary-btn {
  background-color: #f9f5f3;
  border: 1px solid rgba(212, 165, 165, 0.3);
  color: #6b5b5b;
}

.secondary-btn:hover {
  background-color: #f5edea;
  border-color: rgba(212, 165, 165, 0.5);
  color: #d4a5a5;
}

.danger-btn {
  background: linear-gradient(135deg, #f87171 0%, #ef4444 100%);
  border: none;
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.3);
}

.danger-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(239, 68, 68, 0.4);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .color-palette {
    justify-content: center;
  }

  .color-option {
    width: 48px;
    height: 48px;
  }

  .action-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .action-btn {
    width: 100%;
    justify-content: center;
  }

  .header-left {
    gap: 12px;
  }

  .header-icon {
    width: 40px;
    height: 40px;
    font-size: 18px;
  }

  .title-text h2 {
    font-size: 20px;
  }
}
</style>
