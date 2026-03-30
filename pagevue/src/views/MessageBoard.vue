<template>
  <div class="message-board-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">
        <el-icon class="title-icon"><ChatDotRound /></el-icon>
        留言板
      </h2>
      <p class="page-subtitle">欢迎留下您的宝贵意见和建议</p>
    </div>

    <!-- 留言输入区域 -->
    <div class="message-input-section">
      <el-card class="message-card" shadow="hover">
        <div class="user-info-bar">
          <div class="user-avatar-wrapper">
            <img v-if="userInfo.avatar" :src="userInfo.avatar" class="user-avatar-img" alt="头像" />
            <div v-else class="user-avatar-fallback">{{ userInitial }}</div>
          </div>
          <div class="user-info-text">
            <span class="user-name">{{ userName }}</span>
            <span class="user-username">@{{ userInfo.username || 'user' }}</span>
          </div>
        </div>

        <el-input
          v-model="messageContent"
          type="textarea"
          :rows="5"
          placeholder="请输入您的留言内容..."
          maxlength="500"
          show-word-limit
          class="message-textarea"
          resize="none"
        />

        <div class="submit-bar">
          <span class="hint-text">您的反馈对我们很重要</span>
          <el-button
            type="primary"
            class="submit-btn"
            :loading="submitting"
            @click="handleSubmit"
          >
            <el-icon><Position /></el-icon>
            提交留言
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- 留言列表区域 -->
    <div class="message-list-section">
      <h3 class="list-title">
        <el-icon><ChatLineSquare /></el-icon>
        留言列表
        <span class="message-count">({{ messageList.length }})</span>
      </h3>

      <div v-if="loading" class="loading-wrapper">
        <el-skeleton :rows="3" animated />
      </div>

      <div v-else-if="messageList.length === 0" class="empty-wrapper">
        <el-empty description="暂无留言，快来发表第一条留言吧！" />
      </div>

      <div v-else class="message-list">
        <el-card
          v-for="message in messageList"
          :key="message.id"
          class="message-item-card"
          shadow="hover"
        >
          <!-- 留言用户信息 -->
          <div class="message-item-header">
            <div class="message-user-avatar">
              <div class="message-avatar-fallback">{{ (message.userName || message.username || 'U').charAt(0) }}</div>
            </div>
            <div class="message-user-info">
              <span class="message-user-name">{{ message.userName || message.username || '用户' }}</span>
              <span class="message-time">{{ formatTime(message.createTime) }}</span>
            </div>
          </div>

          <!-- 留言内容 -->
          <div class="message-content">
            {{ message.content }}
          </div>

          <!-- 回复区域 -->
          <div class="reply-section">
            <template v-if="message.replyContent">
              <div class="reply-badge">
                <el-icon><ChatDotRound /></el-icon>
                <span>作者回复</span>
              </div>
              <div class="reply-content">
                <div class="reply-text">{{ message.replyContent }}</div>
                <div class="reply-time">{{ formatTime(message.replyTime) }}</div>
              </div>
            </template>
            <template v-else>
              <div class="reply-pending">
                <el-icon><Clock /></el-icon>
                <span>请等待作者回复</span>
              </div>
            </template>
          </div>
        </el-card>
      </div>

      
    <!-- 提示信息区域 -->
    <div v-if="showTip" class="tips-section">
      <el-card class="tips-card" shadow="never">
        <template #header>
          <div class="tips-header">
            <el-icon><InfoFilled /></el-icon>
            <span>留言须知</span>
          </div>
        </template>
        <ul class="tips-list">
          <li>请文明发言，遵守相关法律法规</li>
          <li>留言内容请控制在500字以内</li>
          <li>请勿发布涉及个人隐私的敏感信息</li>
          <li>我们会认真阅读每一条留言</li>
        </ul>
      </el-card>
    </div>

    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { ChatDotRound, Position, InfoFilled, ChatLineSquare, Clock } from '@element-plus/icons-vue'
import { submitMessageAPI, getMessageListAPI } from '../api'

// 用户信息
const userInfo = ref({})
const messageContent = ref('')
const submitting = ref(false)
const showTip = ref(true)

// 留言列表相关
const messageList = ref([])
const loading = ref(false)

// 从localStorage加载用户信息
const loadUserInfo = () => {
  const userInfoStr = localStorage.getItem('userInfo')
  if (userInfoStr) {
    try {
      userInfo.value = JSON.parse(userInfoStr)
    } catch (e) {
      console.error('解析用户信息失败', e)
    }
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}`
}

// 加载留言列表
const loadMessageList = async () => {
  loading.value = true
  try {
    const userId = userInfo.value.id
    const res = await getMessageListAPI(userId)
    if (res.code === 1) {
      messageList.value = res.data || []
    }
  } catch (error) {
    console.error('获取留言列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 计算用户头像的首字母
const userInitial = computed(() => {
  const name = userInfo.value.name || userInfo.value.username || 'U'
  return name.charAt(0)
})

// 计算用户名
const userName = computed(() => {
  return userInfo.value.name || userInfo.value.username || '用户'
})

// 提交留言
const handleSubmit = async () => {
  const content = messageContent.value.trim()

  if (!content) {
    ElMessage.warning('请输入留言内容')
    return
  }

  if (content.length > 500) {
    ElMessage.warning('留言内容不能超过500字')
    return
  }

  submitting.value = true

  try {
    const messageData = {
      userId: userInfo.value.id,
      userName: userInfo.value.name || '',
      username: userInfo.value.username || '',
      content: content
    }

    const res = await submitMessageAPI(messageData)

    if (res.code === 1) {
      ElMessage.success('留言提交成功，感谢您的反馈！')
      messageContent.value = ''
      await loadMessageList()
    } else {
      ElMessage.error(res.msg || '提交失败，请稍后重试')
    }
  } catch (error) {
    console.error('提交留言失败:', error)
    ElMessage.error('提交失败，请检查网络连接')
  } finally {
    submitting.value = false
  }
}

// 初始化
onMounted(() => {
  loadUserInfo()
  loadMessageList()
})
</script>

<style scoped>
.message-board-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 16px;
}

/* 页面标题 */
.page-header {
  text-align: center;
  margin-bottom: 32px;
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  color: #6b5b5b;
  margin: 0 0 8px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.title-icon {
  font-size: 32px;
  color: #d4a5a5;
}

.page-subtitle {
  font-size: 14px;
  color: #9ca3af;
  margin: 0;
}

/* 留言输入区域 */
.message-input-section {
  margin-bottom: 24px;
}

.message-card {
  border-radius: 16px;
  border: none;
  background: linear-gradient(135deg, #ffffff 0%, #fdf9f7 100%);
  box-shadow: 0 4px 20px rgba(212, 165, 165, 0.1);
}

.message-card :deep(.el-card__body) {
  padding: 24px;
}

/* 用户信息栏 */
.user-info-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(212, 165, 165, 0.15);
}

.user-avatar-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  overflow: hidden;
  border: 2px solid rgba(212, 165, 165, 0.3);
  flex-shrink: 0;
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
  font-size: 18px;
  font-weight: 600;
}

.user-info-text {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-name {
  font-size: 16px;
  font-weight: 600;
  color: #6b5b5b;
}

.user-username {
  font-size: 12px;
  color: #9ca3af;
}

/* 文本输入框 */
.message-textarea {
  margin-bottom: 16px;
}

.message-textarea :deep(.el-textarea__inner) {
  border-radius: 12px;
  border-color: rgba(212, 165, 165, 0.3);
  background-color: #ffffff;
  padding: 16px;
  font-size: 14px;
  line-height: 1.6;
  transition: all 0.3s ease;
}

.message-textarea :deep(.el-textarea__inner:focus) {
  border-color: #d4a5a5;
  box-shadow: 0 0 0 2px rgba(212, 165, 165, 0.1);
}

.message-textarea :deep(.el-textarea__inner::placeholder) {
  color: #c0c4cc;
}

.message-textarea :deep(.el-input__count) {
  color: #9ca3af;
  font-size: 12px;
}

/* 提交栏 */
.submit-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.hint-text {
  font-size: 13px;
  color: #9ca3af;
}

.submit-btn {
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  border: none;
  border-radius: 24px;
  padding: 12px 28px;
  font-size: 14px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(212, 165, 165, 0.4);
}

.submit-btn:active {
  transform: translateY(0);
}

/* 提示信息区域 */
.tips-section {
  margin-top: 24px;
}

.tips-card {
  border-radius: 12px;
  border: 1px solid rgba(212, 165, 165, 0.15);
  background-color: #ffffff;
}

.tips-card :deep(.el-card__header) {
  padding: 16px 20px;
  border-bottom: 1px solid rgba(212, 165, 165, 0.1);
}

.tips-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #d4a5a5;
}

.tips-card :deep(.el-card__body) {
  padding: 20px;
}

.tips-list {
  margin: 0;
  padding-left: 20px;
  color: #6b5b5b;
  font-size: 13px;
  line-height: 2;
}

.tips-list li {
  color: #6b5b5b;
}

/* 留言列表区域 */
.message-list-section {
  margin-top: 32px;
}

.list-title {
  font-size: 18px;
  font-weight: 600;
  color: #6b5b5b;
  margin: 0 0 20px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.message-count {
  font-size: 14px;
  font-weight: 400;
  color: #9ca3af;
}

.loading-wrapper,
.empty-wrapper {
  padding: 40px 0;
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message-item-card {
  border-radius: 12px;
  border: 1px solid rgba(212, 165, 165, 0.15);
  background-color: #ffffff;
  transition: all 0.3s ease;
}

.message-item-card:hover {
  border-color: rgba(212, 165, 165, 0.3);
  box-shadow: 0 4px 12px rgba(212, 165, 165, 0.15);
}

.message-item-card :deep(.el-card__body) {
  padding: 20px;
}

.message-item-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.message-user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.message-avatar-fallback {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
}

.message-user-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.message-user-name {
  font-size: 15px;
  font-weight: 600;
  color: #6b5b5b;
}

.message-time {
  font-size: 12px;
  color: #9ca3af;
}

.message-content {
  font-size: 14px;
  color: #4b5563;
  line-height: 1.6;
  padding: 12px 0;
  border-bottom: 1px solid rgba(212, 165, 165, 0.1);
}

.reply-section {
  margin-top: 16px;
  padding-top: 12px;
}

.reply-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #d4a5a5;
  background-color: rgba(212, 165, 165, 0.15);
  padding: 4px 10px;
  border-radius: 12px;
  margin-bottom: 10px;
}

.reply-badge .el-icon {
  font-size: 14px;
}

.reply-content {
  padding-left: 4px;
}

.reply-text {
  font-size: 13px;
  color: #6b5b5b;
  line-height: 1.6;
  padding: 10px 12px;
  background-color: rgba(212, 165, 165, 0.08);
  border-radius: 8px;
  border-left: 3px solid #d4a5a5;
}

.reply-time {
  font-size: 11px;
  color: #9ca3af;
  margin-top: 8px;
  text-align: right;
}

.reply-pending {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #9ca3af;
  padding: 8px 0;
}

.reply-pending .el-icon {
  font-size: 16px;
  color: #d4a5a5;
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .message-board-container {
    padding: 0 12px;
  }

  .page-title {
    font-size: 22px;
  }

  .title-icon {
    font-size: 26px;
  }

  .message-card :deep(.el-card__body) {
    padding: 16px;
  }

  .user-avatar-wrapper {
    width: 40px;
    height: 40px;
  }

  .user-avatar-fallback {
    font-size: 16px;
  }

  .submit-bar {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .hint-text {
    text-align: center;
  }

  .submit-btn {
    width: 100%;
    justify-content: center;
  }

  .message-item-card :deep(.el-card__body) {
    padding: 16px;
  }

  .message-user-avatar {
    width: 36px;
    height: 36px;
  }

  .message-avatar-fallback {
    font-size: 14px;
  }
}
</style>
