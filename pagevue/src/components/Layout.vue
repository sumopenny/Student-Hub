<template>
  <el-container class="layout-container">
    <!-- 顶部导航栏 -->
    <el-header height="64px" class="layout-header">
      <div class="header-left">
        <div class="logo-section">
          <el-icon class="logo-icon"><School /></el-icon>
          <span class="system-title">学生小站</span>
        </div>
      </div>
      <div class="header-right">
        <div class="visitor-count">
          <el-icon><UserFilled /></el-icon>
          <span>已有 {{ visitorCount }} 人访问</span>
        </div>
        <div class="power-by">Power by 素茉Penny</div>
        <el-dropdown>
          <span class="user-info">
            <div class="user-avatar-wrapper">
              <img v-if="userInfo.avatar" :src="userInfo.avatar" class="user-avatar-img" alt="头像" />
              <div v-else class="user-avatar-fallback">{{ userInitial }}</div>
            </div>
            <span class="user-name">{{ userName }}</span>
            <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu class="user-dropdown">
              <el-dropdown-item @click="handleProfile">
                <el-icon><User /></el-icon>
                个人中心
              </el-dropdown-item>
              <el-dropdown-item divided @click="handleLogout">
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-container class="main-container">
      <!-- 左侧侧边栏 -->
      <el-aside
        :width="sidebarWidth + 'px'"
        class="layout-aside"
        :class="{ 'aside-collapsed': isSidebarCollapsed }"
      >
        <el-menu
          :default-active="activeMenu"
          class="sidebar-menu"
          :collapse="isSidebarCollapsed"
          @select="handleMenuSelect"
          background-color="#ffffff"
          text-color="#6b5b5b"
          active-text-color="#d4a5a5"
          :collapse-transition="false"
        >
          <el-menu-item index="profile">
            <el-icon><User /></el-icon>
            <template #title>个人中心</template>
          </el-menu-item>
          <el-menu-item index="studentList">
            <el-icon><Document /></el-icon>
            <template #title>学生列表</template>
          </el-menu-item>
          
          <el-menu-item index="post">
            <el-icon><Edit /></el-icon>
            <template #title>发帖</template>
          </el-menu-item>

          
          <el-menu-item index="messageBoard">
            <el-icon><ChatDotRound /></el-icon>
            <template #title>留言板</template>
          </el-menu-item>
          <el-menu-item index="updateLog">
            <el-icon><Clock /></el-icon>
            <template #title>更新日志</template>
          </el-menu-item>
          <el-menu-item index="contact">
            <el-icon><Message /></el-icon>
            <template #title>联系我</template>
          </el-menu-item>
          <el-menu-item index="system">
            <el-icon><Setting /></el-icon>
            <template #title>系统设置</template>
          </el-menu-item>
          <el-menu-item index="newContent">
            <el-icon><FolderOpened /></el-icon>
            <template #title>更多新内容</template>
          </el-menu-item>
          
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  ArrowDown,
  User,
  Document,
  Setting,
  School,
  SwitchButton,
  ChatDotRound,
  FolderOpened,
  Clock,
  Message,
  UserFilled,
  Edit
} from '@element-plus/icons-vue'
import { getStudentByIdAPI, getVisitorCountAPI, incrementVisitorCountAPI } from '../api/index'

const router = useRouter()
const isSidebarCollapsed = ref(false)
const sidebarWidth = ref(200)
const userInfo = ref({})
const visitorCount = ref(0)

// 初始化访客计数
const initVisitorCount = async () => {
  try {
    // 先增加访客数
    await incrementVisitorCountAPI()
    // 然后获取当前访客数
    const res = await getVisitorCountAPI()
    if (res.data !== undefined && res.data !== null) {
      visitorCount.value = res.data
    }
  } catch (e) {
    console.error('访客计数失败', e)
  }
}

// 计算用户头像的首字母
const userInitial = computed(() => {
  const name = userInfo.value.name || userInfo.value.username || 'U'
  return name.charAt(0)
})

// 计算用户名
const userName = computed(() => {
  return userInfo.value.name || userInfo.value.username || '管理员'
})

// 计算当前激活的菜单
const activeMenu = computed(() => {
  const path = router.currentRoute.value.path
  const menuMap = {
    '/profile': 'profile',
    '/profile/info': 'profile',
    '/profile/password': 'profile',
    '/studentList': 'studentList',
    '/system': 'system',
    '/messageBoard': 'messageBoard',
    '/updateLog': 'updateLog',
    '/contact': 'contact',
    '/newContent': 'newContent',
    '/post': 'post'
  }
  return menuMap[path] || 'profile'
})

// 切换侧边栏展开/折叠
const toggleSidebar = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value
  sidebarWidth.value = isSidebarCollapsed.value ? 64 : 200
}

// 处理菜单选择
const handleMenuSelect = (key) => {
  router.push(`/${key}`)
}

// 处理个人中心点击
const handleProfile = () => {
  router.push('/profile')
}

// 处理退出登录
const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  router.push('/login')
}

// 监听头像更新事件
const handleAvatarUpdate = (event) => {
  if (event.detail && event.detail.avatar) {
    userInfo.value = { ...userInfo.value, avatar: event.detail.avatar }
  }
}

// 监听路由变化，确保菜单激活状态正确
onMounted(async () => {
  // 初始化时设置正确的激活菜单
  activeMenu.value

  // 初始化访客计数
  await initVisitorCount()

  // 从localStorage加载用户信息，并从后端获取完整信息
  const userInfoStr = localStorage.getItem('userInfo')
  if (userInfoStr) {
    try {
      const parsedUserInfo = JSON.parse(userInfoStr)
      const userId = parsedUserInfo.id
      
      if (userId) {
        const res = await getStudentByIdAPI(userId)
        if (res.data) {
          userInfo.value = res.data
          localStorage.setItem('userInfo', JSON.stringify(res.data))
        }
      } else {
        userInfo.value = parsedUserInfo
      }
    } catch (e) {
      console.error('获取用户信息失败', e)
      // 回退到 localStorage 中的信息
      if (userInfoStr) {
        userInfo.value = JSON.parse(userInfoStr)
      }
    }
  }

  // 监听头像更新事件
  window.addEventListener('avatar-updated', handleAvatarUpdate)
})

// 组件卸载时移除事件监听
onUnmounted(() => {
  window.removeEventListener('avatar-updated', handleAvatarUpdate)
})
</script>

<style scoped>
/* 主容器 */
.layout-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: linear-gradient(135deg, #fdf9f7 0%, #f5eeeb 25%, #faf8f5 50%, #f7f2ef 75%, #fdf9f7 100%);
  background-size: 400% 400%;
  animation: gradientShift 15s ease infinite;
}

/* 顶部导航栏 - 莫兰迪粉色风格 */
.layout-header {
  background: linear-gradient(135deg, #ffffff 0%, #fdf9f7 100%);
  box-shadow: 0 2px 12px rgba(200, 170, 160, 0.12);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  z-index: 100;
  border-bottom: 1px solid rgba(212, 165, 165, 0.15);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 28px;
  color: #d4a5a5;
}

.system-title {
  font-size: 20px;
  font-weight: 600;
  color: #6b5b5b;
  letter-spacing: 0.5px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.visitor-count {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #a89a9a;
  padding: 6px 12px;
  background-color: rgba(212, 165, 165, 0.08);
  border-radius: 20px;
  transition: all 0.3s ease;
}

.visitor-count:hover {
  background-color: rgba(212, 165, 165, 0.15);
}

.visitor-count .el-icon {
  color: #d4a5a5;
  font-size: 16px;
}

.power-by {
  font-size: 13px;
  color: #a89a9a;
  letter-spacing: 0.5px;
  white-space: nowrap;
}

/* 用户信息区域 */
.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 24px;
  transition: all 0.3s ease;
  background-color: rgba(212, 165, 165, 0.08);
}

.user-info:hover {
  background-color: rgba(212, 165, 165, 0.15);
}

/* 用户头像样式 - 与学生列表一致 */
.user-avatar-wrapper {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  overflow: hidden;
  border: 2px solid rgba(212, 165, 165, 0.3);
  transition: all 0.3s ease;
}

.user-info:hover .user-avatar-wrapper {
  border-color: rgba(212, 165, 165, 0.5);
  transform: scale(1.05);
}

.user-avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-avatar-fallback {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  font-weight: 600;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: #6b5b5b;
}

.dropdown-icon {
  font-size: 12px;
  color: #9ca3af;
  margin-left: 4px;
}

/* 下拉菜单样式 */
.user-dropdown {
  border-radius: 12px;
  padding: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.user-dropdown :deep(.el-dropdown-menu__item) {
  border-radius: 8px;
  padding: 10px 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  color: #6b5b5b;
  transition: all 0.2s ease;
}

.user-dropdown :deep(.el-dropdown-menu__item:hover) {
  background-color: rgba(212, 165, 165, 0.1);
  color: #d4a5a5;
}

.user-dropdown :deep(.el-dropdown-menu__item .el-icon) {
  font-size: 16px;
}

/* 侧边栏样式 */
.layout-aside {
  background-color: #ffffff;
  transition: width 0.3s ease;
  overflow: hidden;
  border-right: 1px solid rgba(212, 165, 165, 0.12);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.04);
}

.aside-collapsed {
  width: 64px !important;
}

.sidebar-menu {
  height: 100%;
  border-right: none;
  padding: 16px 8px;
}

.sidebar-menu :deep(.el-menu-item) {
  border-radius: 10px;
  margin-bottom: 8px;
  height: 48px;
  line-height: 48px;
  transition: all 0.3s ease;
}

.sidebar-menu :deep(.el-menu-item:hover) {
  background-color: rgba(212, 165, 165, 0.08) !important;
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, rgba(212, 165, 165, 0.15) 0%, rgba(232, 180, 184, 0.1) 100%);
  font-weight: 500;
}

.sidebar-menu :deep(.el-menu-item .el-icon) {
  font-size: 18px;
  margin-right: 12px;
}

.main-container {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.layout-main {
  padding: 24px;
  overflow-y: auto;
  background: linear-gradient(135deg, #fdf9f7 0%, #f5eeeb 25%, #faf8f5 50%, #f7f2ef 75%, #fdf9f7 100%);
  background-size: 400% 400%;
  animation: gradientShift 15s ease infinite;
  flex: 1;
}

@keyframes gradientShift {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .layout-header {
    padding: 0 16px;
  }

  .system-title {
    font-size: 16px;
  }

  .logo-icon {
    font-size: 24px;
  }

  .layout-aside {
    width: 64px !important;
  }

  .sidebar-menu {
    collapse: true;
  }

  .user-name {
    display: none;
  }

  .user-info {
    padding: 4px;
  }

  .layout-main {
    padding: 16px;
  }
}

/* 滚动条样式优化 */
.layout-main::-webkit-scrollbar {
  width: 6px;
}

.layout-main::-webkit-scrollbar-track {
  background: transparent;
}

.layout-main::-webkit-scrollbar-thumb {
  background-color: rgba(212, 165, 165, 0.3);
  border-radius: 3px;
}

.layout-main::-webkit-scrollbar-thumb:hover {
  background-color: rgba(212, 165, 165, 0.5);
}
</style>
