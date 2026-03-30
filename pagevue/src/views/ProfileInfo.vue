<template>
  <div class="profile-info-container">
    <!-- 页面标题区 -->
    <div class="page-header">
      <div class="header-title">
        <div class="title-icon-wrapper">
          <el-icon class="title-icon"><EditPen /></el-icon>
        </div>
        <div class="title-text">
          <h2>编辑个人信息</h2>
          <p class="subtitle">修改您的个人资料和联系方式</p>
        </div>
      </div>
      <el-button class="back-btn" @click="handleCancel">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
    </div>

    <!-- 表单卡片 -->
    <el-card class="form-card" shadow="never">
      <el-form
        :model="formData"
        :rules="rules"
        ref="formRef"
        label-width="100px"
        class="profile-form"
        status-icon
      >
        <div class="form-section">
          <div class="section-title">
            <el-icon><User /></el-icon>
            <span>基本信息</span>
          </div>

          <el-form-item label="用户名" prop="username">
            <el-input v-model="formData.username" disabled :prefix-icon="User" />
          </el-form-item>

          <el-form-item label="真实姓名" prop="name">
            <el-input v-model="formData.name" placeholder="请输入真实姓名" :prefix-icon="Avatar" />
          </el-form-item>

          <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="formData.gender" class="gender-radio-group">
              <el-radio-button label="男">
                <el-icon><Male /></el-icon>
                男
              </el-radio-button>
              <el-radio-button label="女">
                <el-icon><Female /></el-icon>
                女
              </el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="年龄" prop="age">
            <el-input-number 
              v-model="formData.age" 
              :min="1" 
              :max="150" 
              placeholder="请输入年龄" 
            />
          </el-form-item>

          <el-form-item label="地址" prop="address">
            <el-input v-model="formData.address" placeholder="请输入地址" :prefix-icon="Location" />
          </el-form-item>

          <el-form-item label="电话" prop="phone">
            <el-input v-model="formData.phone" placeholder="请输入联系电话" :prefix-icon="Phone" />
          </el-form-item>
        </div>

        <div class="form-actions">
          <el-button class="cancel-btn" @click="handleCancel">取消</el-button>
          <el-button type="primary" class="submit-btn" @click="handleSubmit" :loading="submitting">
            <el-icon><Check /></el-icon>
            保存修改
          </el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  EditPen,
  ArrowLeft,
  User,
  Avatar,
  Male,
  Female,
  Phone,
  Check,
  Location
} from '@element-plus/icons-vue'
import { getStudentByIdAPI, updateStudentAPI } from '../api/index'

const router = useRouter()
const formRef = ref(null)
const submitting = ref(false)

const formData = reactive({
  id: '',
  username: '',
  name: '',
  phone: '',
  gender: '男',
  age: null,
  address: ''
})

const rules = {
  name: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度应在2-20个字符之间', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 保存修改
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitting.value = true

    await updateStudentAPI(formData)

    const currentUserInfoStr = localStorage.getItem('userInfo')
    if (currentUserInfoStr) {
      let currentUserInfo = JSON.parse(currentUserInfoStr)
      currentUserInfo = { ...currentUserInfo, ...formData }
      localStorage.setItem('userInfo', JSON.stringify(currentUserInfo))
    }

    ElMessage.success('个人信息修改成功')
    router.push('/profile')
  } catch (error) {
    if (error !== false) {
      console.error('保存失败:', error)
      ElMessage.error('保存失败，请重试')
    }
  } finally {
    submitting.value = false
  }
}

// 取消操作
const handleCancel = () => {
  router.push('/profile')
}

// 初始化表单数据
onMounted(async () => {
  const userInfoStr = localStorage.getItem('userInfo')
  if (userInfoStr) {
    try {
      const userInfo = JSON.parse(userInfoStr)
      const userId = userInfo.id
      if (userId) {
        const res = await getStudentByIdAPI(userId)
        if (res.data) {
          formData.id = res.data.id || ''
          formData.username = res.data.username || ''
          formData.name = res.data.name || ''
          formData.phone = res.data.phone || ''
          formData.gender = res.data.gender || '男'
          formData.age = res.data.age || null
          formData.address = res.data.address || ''
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
.profile-info-container {
  max-width: 800px;
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
.profile-form :deep(.el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 0 0 1px #e5e7eb inset;
  padding: 4px 12px;
}

.profile-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #d4a5a5 inset;
}

.profile-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #d4a5a5 inset, 0 0 0 3px rgba(212, 165, 165, 0.1);
}

.profile-form :deep(.el-input__inner) {
  height: 40px;
}

.profile-form :deep(.el-input.is-disabled .el-input__wrapper) {
  background-color: #f9f5f3;
}

.profile-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #6b5b5b;
}

/* 表单分区 */
.form-section {
  margin-bottom: 32px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #6b5b5b;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(212, 165, 165, 0.15);
}

.section-title .el-icon {
  font-size: 18px;
  color: #d4a5a5;
}

.form-divider {
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(212, 165, 165, 0.2), transparent);
  margin: 32px 0;
}

/* 性别单选组 */
.gender-radio-group {
  display: flex;
  gap: 12px;
}

.gender-radio-group :deep(.el-radio-button__inner) {
  border-radius: 10px;
  padding: 10px 20px;
  display: flex;
  align-items: center;
  gap: 6px;
  border: 1px solid #e5e7eb;
  box-shadow: none;
}

.gender-radio-group :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  border-color: #d4a5a5;
  box-shadow: 0 4px 12px rgba(212, 165, 165, 0.3);
}

.gender-radio-group :deep(.el-radio-button:first-child .el-radio-button__inner) {
  border-radius: 10px;
}

.gender-radio-group :deep(.el-radio-button:last-child .el-radio-button__inner) {
  border-radius: 10px;
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

  .profile-form :deep(.el-form-item__label) {
    float: none;
    display: block;
    text-align: left;
    margin-bottom: 8px;
  }

  .profile-form :deep(.el-form-item__content) {
    margin-left: 0 !important;
  }

  .gender-radio-group {
    flex-wrap: wrap;
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
