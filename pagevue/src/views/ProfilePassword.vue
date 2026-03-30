<template>
  <div class="profile-password-container">
    <!-- 页面标题区 -->
    <div class="page-header">
      <div class="header-title">
        <div class="title-icon-wrapper">
          <el-icon class="title-icon"><Lock /></el-icon>
        </div>
        <div class="title-text">
          <h2>修改密码</h2>
          <p class="subtitle">更新您的账户密码以保证安全</p>
        </div>
      </div>
      <el-button class="back-btn" @click="handleCancel">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
    </div>

    <!-- 密码安全提示 -->
    <el-alert
      title="密码安全提示"
      description="请设置包含字母和数字的强密码，长度至少6位。新密码不能与旧密码相同。"
      type="info"
      :closable="false"
      show-icon
      class="security-tips"
    />

    <!-- 表单卡片 -->
    <el-card class="form-card" shadow="never">
      <el-form
        :model="formData"
        :rules="rules"
        ref="formRef"
        label-width="120px"
        class="password-form"
        status-icon
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            v-model="formData.oldPassword"
            type="password"
            placeholder="请输入原密码"
            :prefix-icon="Key"
            show-password
          />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="formData.newPassword"
            type="password"
            placeholder="请输入新密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input
            v-model="formData.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            :prefix-icon="CircleCheck"
            show-password
          />
        </el-form-item>

        <div class="form-actions">
          <el-button class="cancel-btn" @click="handleCancel">取消</el-button>
          <el-button
            type="primary"
            class="submit-btn"
            @click="handleSubmit"
            :loading="loading"
          >
            <el-icon><Check /></el-icon>
            确定修改
          </el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Lock,
  ArrowLeft,
  Key,
  CircleCheck,
  Check
} from '@element-plus/icons-vue'
import { updatePasswordAPI } from '../api/index'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const formData = reactive({
  id: '',
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const rules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '新密码长度至少为6位', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value === formData.oldPassword) {
          callback(new Error('新密码不能与原密码相同'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== formData.newPassword) {
          callback(new Error('两次输入的新密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    // 调用修改密码接口
    const response = await updatePasswordAPI(formData)

    ElMessage.success('密码修改成功！')

    // 密码修改成功后返回个人中心
    router.push('/profile')
  } catch (error) {
    if (error !== false) {
      console.error('密码修改失败:', error)
      let errorMsg = '密码修改失败，请重试'
      if (error.response && error.response.data) {
        errorMsg = error.response.data.msg || error.response.data.message || errorMsg
      } else if (error.message) {
        errorMsg = error.message
      }
      ElMessage.error(errorMsg)
    }
  } finally {
    loading.value = false
  }
}

const handleCancel = () => {
  router.push('/profile')
}

// 初始化用户ID
const initUserData = () => {
  const userInfoStr = localStorage.getItem('userInfo')
  if (userInfoStr) {
    try {
      const userInfo = JSON.parse(userInfoStr)
      formData.id = userInfo.id || ''
    } catch (e) {
      console.error('解析用户信息失败', e)
    }
  }
}

// 组件加载时初始化用户数据
initUserData()
</script>

<style scoped>
/* 页面容器 */
.profile-password-container {
  max-width: 600px;
  margin: 0 auto;
}

/* 页面标题区 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.back-btn {
  height: 40px;
  padding: 0 20px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  gap: 6px;
  background-color: #f9f5f3;
  border: 1px solid rgba(212, 165, 165, 0.3);
  color: #6b5b5b;
  transition: all 0.3s ease;
}

.back-btn:hover {
  background-color: #f5edea;
  border-color: rgba(212, 165, 165, 0.5);
  color: #d4a5a5;
}

/* 安全提示 */
.security-tips {
  margin-bottom: 20px;
  border-radius: 12px;
  border: none;
  background: linear-gradient(135deg, #dbeafe 0%, #eff6ff 100%);
}

.security-tips :deep(.el-alert__title) {
  color: #3b82f6;
  font-weight: 600;
}

.security-tips :deep(.el-alert__description) {
  color: #6b7280;
}

.security-tips :deep(.el-alert__icon) {
  color: #3b82f6;
}

/* 表单卡片 */
.form-card {
  border-radius: 20px;
  border: none;
  background: #ffffff;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
}

.form-card :deep(.el-card__body) {
  padding: 32px;
}

/* 表单样式 */
.password-form :deep(.el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 0 0 1px #e5e7eb inset;
  padding: 4px 12px;
}

.password-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #d4a5a5 inset;
}

.password-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #d4a5a5 inset, 0 0 0 3px rgba(212, 165, 165, 0.1);
}

.password-form :deep(.el-input__inner) {
  height: 44px;
}

.password-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #6b5b5b;
}

.password-form :deep(.el-form-item) {
  margin-bottom: 24px;
}

/* 表单操作 */
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  padding-top: 24px;
  border-top: 1px solid rgba(212, 165, 165, 0.15);
}

.cancel-btn {
  height: 44px;
  padding: 0 32px;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  background-color: #f9f5f3;
  border: 1px solid rgba(212, 165, 165, 0.3);
  color: #6b5b5b;
  transition: all 0.3s ease;
}

.cancel-btn:hover {
  background-color: #f5edea;
  border-color: rgba(212, 165, 165, 0.5);
  color: #d4a5a5;
}

.submit-btn {
  height: 44px;
  padding: 0 32px;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  border: none;
  box-shadow: 0 4px 12px rgba(212, 165, 165, 0.3);
  transition: all 0.3s ease;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(212, 165, 165, 0.4);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .back-btn {
    width: 100%;
    justify-content: center;
  }

  .form-card :deep(.el-card__body) {
    padding: 24px;
  }

  .password-form :deep(.el-form-item__label) {
    float: none;
    display: block;
    text-align: left;
    margin-bottom: 8px;
  }

  .password-form :deep(.el-form-item__content) {
    margin-left: 0 !important;
  }

  .form-actions {
    flex-direction: column;
  }

  .form-actions .el-button {
    width: 100%;
    justify-content: center;
  }

  .title-text h2 {
    font-size: 20px;
  }
}
</style>
