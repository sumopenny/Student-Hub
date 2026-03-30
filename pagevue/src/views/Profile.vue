<template>
  <div class="profile-container">
    <!-- 页面标题区 -->
    <div class="page-header">
      <div class="header-title">
        <div class="title-icon-wrapper">
          <el-icon class="title-icon"><User /></el-icon>
        </div>
        <div class="title-text">
          <h2>个人中心</h2>
          <p class="subtitle">管理您的个人信息和账户设置</p>
        </div>
      </div>
    </div>

    <!-- 用户信息卡片 -->
    <el-card class="profile-card" shadow="never">
      <div class="profile-content">
        <div class="user-info-section">
          <!-- 头像上传 -->
          <div class="avatar-section">
            <el-upload
              class="avatar-uploader"
              action="#"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload"
              :http-request="handleAvatarUpload"
              accept="image/*"
            >
              <div class="avatar-wrapper">
                <img v-if="avatarUrl" :src="avatarUrl" class="avatar-image" alt="头像" />
                <div v-else class="avatar-fallback">{{ userInitial }}</div>
                <div class="avatar-overlay">
                  <el-icon><Camera /></el-icon>
                  <span>更换头像</span>
                </div>
              </div>
            </el-upload>
            <p class="avatar-tip">点击头像更换照片</p>
          </div>

          <!-- 用户信息 -->
          <div class="user-details">
            <div class="detail-item">
              <span class="detail-label">用户名</span>
              <span class="detail-value">{{ userInfo.username || '-' }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">真实姓名</span>
              <span class="detail-value">{{ userInfo.name || '-' }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">性别</span>
              <span class="detail-value">
                <el-tag
                  v-if="userInfo.gender"
                  :type="userInfo.gender === '男' ? 'primary' : 'danger'"
                  size="small"
                  effect="light"
                  class="gender-tag"
                >
                  <el-icon v-if="userInfo.gender === '男'"><Male /></el-icon>
                  <el-icon v-else><Female /></el-icon>
                  {{ userInfo.gender }}
                </el-tag>
                <span v-else>-</span>
              </span>
            </div>
            
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="action-section">
          <el-button type="primary" class="action-btn" @click="goToInfo">
            <el-icon><EditPen /></el-icon>
            编辑个人信息
          </el-button>
          <el-button class="action-btn secondary-btn" @click="goToPassword">
            <el-icon><Lock /></el-icon>
            修改密码
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 快捷入口卡片 -->
    <div class="quick-access">
      <el-card class="access-card" shadow="never" @click="goToStudentList">
        <div class="access-content">
          <div class="access-icon student-icon">
            <el-icon><Document /></el-icon>
          </div>
          <div class="access-info">
            <h3>学生列表</h3>
            <p>查看和管理所有学生信息</p>
          </div>
          <el-icon class="access-arrow"><ArrowRight /></el-icon>
        </div>
      </el-card>

      <el-card class="access-card" shadow="never" @click="goToSystem">
        <div class="access-content">
          <div class="access-icon system-icon">
            <el-icon><Setting /></el-icon>
          </div>
          <div class="access-info">
            <h3>系统设置</h3>
            <p>个性化您的系统偏好</p>
          </div>
          <el-icon class="access-arrow"><ArrowRight /></el-icon>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  User,
  EditPen,
  Lock,
  Camera,
  Document,
  Setting,
  ArrowRight,
  Male,
  Female
} from '@element-plus/icons-vue'
import { uploadAvatarAPI, getStudentByIdAPI } from '../api/index.js'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userInfo = ref({})
const avatarUrl = ref('')

// 计算用户头像的首字母
const userInitial = computed(() => {
  const name = userInfo.value.name || userInfo.value.username || 'U'
  return name.charAt(0)
})

// 跳转到个人信息编辑页面
const goToInfo = () => {
  router.push('/profile/info')
}

// 跳转到密码修改页面
const goToPassword = () => {
  router.push('/profile/password')
}

// 跳转到学生列表
const goToStudentList = () => {
  router.push('/studentList')
}

// 跳转到系统设置
const goToSystem = () => {
  router.push('/system')
}

// 上传前校验
const beforeAvatarUpload = (rawFile) => {
  const isImage = rawFile.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }

  const isLt10M = rawFile.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过 10MB！')
    return false
  }

  return true
}

// 自定义上传方法
const handleAvatarUpload = async (options) => {
  try {
    const currentUserInfoStr = localStorage.getItem('userInfo')
    if (!currentUserInfoStr) {
      ElMessage.error('用户未登录')
      return
    }

    const currentUserInfo = JSON.parse(currentUserInfoStr)
    const userId = currentUserInfo.id

    if (!userId) {
      ElMessage.error('用户ID不存在')
      return
    }

    const response = await uploadAvatarAPI(options.file, userId)

    if (response.code === 1) {
      avatarUrl.value = response.data.url
      currentUserInfo.avatar = response.data.url
      localStorage.setItem('userInfo', JSON.stringify(currentUserInfo))
      userInfo.value = currentUserInfo

      // 触发自定义事件通知其他组件头像已更新
      window.dispatchEvent(new CustomEvent('avatar-updated', {
        detail: { avatar: response.data.url }
      }))

      ElMessage.success('头像上传成功')
    } else {
      ElMessage.error(response.msg || '上传失败')
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    ElMessage.error(error.message || '上传失败，请重试')
  }
}

// 初始化用户信息
onMounted(async () => {
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
          if (res.data.avatar) {
            avatarUrl.value = res.data.avatar
          }
        }
      } else {
        userInfo.value = parsedUserInfo
        if (parsedUserInfo.avatar) {
          avatarUrl.value = parsedUserInfo.avatar
        }
      }
    } catch (e) {
      console.error('获取用户信息失败', e)
    }
  }
})
</script>

<style scoped>
/* 页面容器 */
.profile-container {
  max-width: 1200px;
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

/* 用户信息卡片 */
.profile-card {
  border-radius: 20px;
  border: none;
  background: #ffffff;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  margin-bottom: 24px;
}

.profile-card :deep(.el-card__body) {
  padding: 32px;
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.user-info-section {
  display: flex;
  align-items: flex-start;
  gap: 40px;
}

/* 头像区域 */
.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.avatar-uploader {
  cursor: pointer;
}

.avatar-wrapper {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  position: relative;
  border: 4px solid rgba(212, 165, 165, 0.2);
  transition: all 0.3s ease;
}

.avatar-wrapper:hover {
  border-color: rgba(212, 165, 165, 0.4);
  transform: scale(1.02);
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-fallback {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 48px;
  font-weight: 600;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.avatar-wrapper:hover .avatar-overlay {
  opacity: 1;
}

.avatar-overlay .el-icon {
  font-size: 24px;
  margin-bottom: 4px;
}

.avatar-overlay span {
  font-size: 12px;
}

.avatar-tip {
  margin: 0;
  font-size: 13px;
  color: #9ca3af;
}

/* 用户详情 */
.user-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding-top: 8px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 16px;
}

.detail-label {
  width: 80px;
  font-size: 14px;
  color: #9ca3af;
  font-weight: 500;
}

.detail-value {
  font-size: 16px;
  color: #6b5b5b;
  font-weight: 500;
}

.gender-tag {
  border-radius: 12px;
  padding: 4px 12px;
}

.gender-tag :deep(.el-icon) {
  margin-right: 4px;
}

/* 操作按钮 */
.action-section {
  display: flex;
  gap: 16px;
  padding-top: 24px;
  border-top: 1px solid rgba(212, 165, 165, 0.15);
}

.action-btn {
  height: 44px;
  padding: 0 24px;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.action-btn :deep(.el-icon) {
  font-size: 16px;
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

/* 快捷入口 */
.quick-access {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.access-card {
  border-radius: 16px;
  border: none;
  background: #ffffff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  cursor: pointer;
  transition: all 0.3s ease;
}

.access-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(212, 165, 165, 0.15);
}

.access-card :deep(.el-card__body) {
  padding: 24px;
}

.access-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.access-icon {
  width: 52px;
  height: 52px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  transition: all 0.3s ease;
}

.student-icon {
  background: linear-gradient(135deg, #dbeafe 0%, #bfdbfe 100%);
  color: #3b82f6;
}

.stats-icon {
  background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%);
  color: #10b981;
}

.system-icon {
  background: linear-gradient(135deg, #fce7f3 0%, #fbcfe8 100%);
  color: #ec4899;
}

.access-card:hover .access-icon {
  transform: scale(1.1);
}

.access-info {
  flex: 1;
}

.access-info h3 {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
  color: #6b5b5b;
}

.access-info p {
  margin: 0;
  font-size: 13px;
  color: #9ca3af;
}

.access-arrow {
  font-size: 20px;
  color: #d1d5db;
  transition: all 0.3s ease;
}

.access-card:hover .access-arrow {
  color: #d4a5a5;
  transform: translateX(4px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-card :deep(.el-card__body) {
    padding: 24px;
  }

  .user-info-section {
    flex-direction: column;
    align-items: center;
    gap: 24px;
  }

  .user-details {
    width: 100%;
    padding-top: 0;
  }

  .detail-item {
    justify-content: space-between;
  }

  .action-section {
    flex-direction: column;
  }

  .action-btn {
    width: 100%;
    justify-content: center;
  }

  .quick-access {
    grid-template-columns: 1fr;
  }

  .title-text h2 {
    font-size: 20px;
  }
}

@media (min-width: 769px) and (max-width: 1024px) {
  .quick-access {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
