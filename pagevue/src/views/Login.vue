<template>
  <div class="auth-container">
    <!-- 左侧装饰区域 -->
    <div class="auth-decoration">
      <div class="decoration-content">
        <div class="logo-section">
          <el-icon class="logo-icon"><School /></el-icon>
          <h1 class="system-title">学生管理系统</h1>
        </div>
        <p class="system-subtitle">智能化校园管理平台</p>
        <div class="feature-list">
          <div class="feature-item">
            <el-icon><Check /></el-icon>
            <span>高效的学生信息管理</span>
          </div>
          <div class="feature-item">
            <el-icon><Check /></el-icon>
            <span>便捷的数据统计分析</span>
          </div>
          <div class="feature-item">
            <el-icon><Check /></el-icon>
            <span>安全的权限控制体系</span>
          </div>
        </div>
      </div>
      <!-- 装饰圆圈 -->
      <div class="decoration-circle circle-1"></div>
      <div class="decoration-circle circle-2"></div>
      <div class="decoration-circle circle-3"></div>
    </div>

    <!-- 右侧表单区域 -->
    <div class="auth-form-section">
      <div class="form-container">
        <!-- 登录表单 -->
        <div v-if="isLogin" class="auth-form-wrapper">
          <div class="form-header">
            <h2 class="form-title">欢迎回来</h2>
            <p class="form-subtitle">请登录您的账户</p>
          </div>

          <el-form
            ref="loginFormRef"
            :model="loginForm"
            :rules="loginRules"
            class="auth-form"
            @keyup.enter="handleLogin"
          >
            <el-form-item prop="username">
              <el-input
                v-model="loginForm.username"
                placeholder="请输入用户名"
                size="large"
                :prefix-icon="User"
                clearable
              />
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                :prefix-icon="Lock"
                show-password
                clearable
              />
            </el-form-item>

            <div class="form-options">
              <el-checkbox v-model="rememberMe">记住我</el-checkbox>
            </div>

            <el-form-item>
              <el-button
                type="primary"
                size="large"
                class="submit-btn"
                :loading="loading"
                @click="handleLogin"
              >
                登 录
              </el-button>
            </el-form-item>
          </el-form>

          <div class="form-footer">
            <span class="footer-text">还没有账户？</span>
            <el-button link type="primary" class="switch-btn" @click="switchMode">
              立即注册
            </el-button>
          </div>
        </div>

        <!-- 注册表单 -->
        <div v-else class="auth-form-wrapper">
          <div class="form-header">
            <h2 class="form-title">创建账户</h2>
            <p class="form-subtitle">填写以下信息完成注册</p>
          </div>

          <el-form
            ref="registerFormRef"
            :model="registerForm"
            :rules="registerRules"
            class="auth-form"
            @keyup.enter="handleRegister"
          >
            <el-form-item prop="name">
              <el-input
                v-model="registerForm.name"
                placeholder="请输入姓名"
                size="large"
                :prefix-icon="UserFilled"
                clearable
              />
            </el-form-item>

            <el-form-item prop="gender">
              <el-select
                v-model="registerForm.gender"
                placeholder="请选择性别"
                size="large"
                class="gender-select"
              >
                <el-option label="男" value="男">
                  <el-icon class="gender-icon male"><Male /></el-icon>
                  <span>男</span>
                </el-option>
                <el-option label="女" value="女">
                  <el-icon class="gender-icon female"><Female /></el-icon>
                  <span>女</span>
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item prop="username">
              <el-input
                v-model="registerForm.username"
                placeholder="请输入用户名"
                size="large"
                :prefix-icon="User"
                clearable
              />
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                v-model="registerForm.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                :prefix-icon="Lock"
                show-password
                clearable
              />
            </el-form-item>

            <el-form-item prop="confirmPassword">
              <el-input
                v-model="registerForm.confirmPassword"
                type="password"
                placeholder="请确认密码"
                size="large"
                :prefix-icon="Lock"
                show-password
                clearable
              />
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                size="large"
                class="submit-btn"
                :loading="registerLoading"
                @click="handleRegister"
              >
                注 册
              </el-button>
            </el-form-item>
          </el-form>

          <div class="form-footer">
            <span class="footer-text">已有账户？</span>
            <el-button link type="primary" class="switch-btn" @click="switchMode">
              立即登录
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  User,
  Lock,
  School,
  Check,
  UserFilled,
  Male,
  Female
} from '@element-plus/icons-vue'
import { loginAPI, registerAPI, checkUsernameAPI } from '../api/index'

const router = useRouter()

// 当前模式：true为登录，false为注册
const isLogin = ref(true)

// 记住我
const rememberMe = ref(false)

// 加载状态
const loading = ref(false)
const registerLoading = ref(false)

// 表单引用
const loginFormRef = ref(null)
const registerFormRef = ref(null)

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: ''
})

// 注册表单数据
const registerForm = reactive({
  name: '',
  gender: '',
  username: '',
  password: '',
  confirmPassword: ''
})

// 自定义验证：确认密码
const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请确认密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

// 自定义验证：检查用户名是否已存在
const validateUsername = async (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入用户名'))
    return
  }
  try {
    const res = await checkUsernameAPI(value)
    if (res.code === 1 && res.data && res.data.exists) {
      callback(new Error('该用户名已被使用，请更换'))
    } else {
      callback()
    }
  } catch (error) {
    // 如果接口调用失败，允许继续，由后端最终验证
    callback()
  }
}

// 自定义验证：姓名必须是中文，不能包含数字
const validateName = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入姓名'))
    return
  }
  // 检查是否包含数字
  if (/\d/.test(value)) {
    callback(new Error('姓名不能包含数字'))
    return
  }
  // 检查是否包含英文字母
  if (/[a-zA-Z]/.test(value)) {
    callback(new Error('姓名必须是中文'))
    return
  }
  callback()
}

// 登录表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

// 注册表单验证规则
const registerRules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度应在2-20个字符之间', trigger: 'blur' },
    { validator: validateName, trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { validator: validateUsername, trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 切换登录/注册模式
const switchMode = () => {
  isLogin.value = !isLogin.value
  // 重置表单
  if (isLogin.value) {
    registerFormRef.value?.resetFields()
  } else {
    loginFormRef.value?.resetFields()
  }
}

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const response = await loginAPI(loginForm)
        let userInfo = response

        if (response.data) {
          userInfo = response.data
        }

        const token = userInfo.token || 'token_' + new Date().getTime()
        localStorage.setItem('token', token)
        localStorage.setItem('userInfo', JSON.stringify(userInfo))

        // 如果勾选了记住我，保存用户名
        if (rememberMe.value) {
          localStorage.setItem('rememberedUsername', loginForm.username)
        } else {
          localStorage.removeItem('rememberedUsername')
        }

        ElMessage.success('登录成功')
        router.push('/profile')
      } catch (error) {
        console.error('登录失败:', error)
        let errorMsg = '登录失败，请检查用户名和密码'
        if (error.response && error.response.data) {
          errorMsg = error.response.data.msg || error.response.data.message || errorMsg
        } else if (error.message) {
          errorMsg = error.message
        }
        ElMessage.error(errorMsg)
      } finally {
        loading.value = false
      }
    }
  })
}

// 处理注册
const handleRegister = async () => {
  if (!registerFormRef.value) return

  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      registerLoading.value = true
      try {
        // 构建注册数据（不包含确认密码）
        const registerData = {
          name: registerForm.name,
          gender: registerForm.gender,
          username: registerForm.username,
          password: registerForm.password
        }

        const response = await registerAPI(registerData)

        if (response.code === 1) {
          ElMessage.success('注册成功，请登录')
          // 切换到登录模式并填充用户名
          isLogin.value = true
          loginForm.username = registerForm.username
          loginForm.password = ''
          // 重置注册表单
          registerFormRef.value.resetFields()
        } else {
          ElMessage.error(response.msg || '注册失败')
        }
      } catch (error) {
        console.error('注册失败:', error)
        let errorMsg = '注册失败，请稍后重试'
        if (error.response && error.response.data) {
          errorMsg = error.response.data.msg || error.response.data.message || errorMsg
        } else if (error.message) {
          errorMsg = error.message
        }
        ElMessage.error(errorMsg)
      } finally {
        registerLoading.value = false
      }
    }
  })
}

// 页面加载时检查是否有记住的用户名
const initRememberedUser = () => {
  const rememberedUsername = localStorage.getItem('rememberedUsername')
  if (rememberedUsername) {
    loginForm.username = rememberedUsername
    rememberMe.value = true
  }
}

initRememberedUser()
</script>

<style scoped>
/* 主容器 */
.auth-container {
  display: flex;
  min-height: 100vh;
  width: 100%;
  background-color: #f5f7fa;
}

/* 左侧装饰区域 - 莫兰迪粉色黄色配色 */
.auth-decoration {
  flex: 1;
  background: linear-gradient(135deg, #e8b4b8 0%, #f3d7a9 50%, #e9d5c4 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  padding: 40px;
}

.decoration-content {
  position: relative;
  z-index: 2;
  color: #5a4a4a;
  text-align: center;
  max-width: 480px;
}

.logo-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-bottom: 16px;
}

.logo-icon {
  font-size: 48px;
  color: #c9a9a6;
}

.system-title {
  font-size: 36px;
  font-weight: 700;
  margin: 0;
  color: #6b5b5b;
}

.system-subtitle {
  font-size: 18px;
  opacity: 0.85;
  margin-bottom: 48px;
  font-weight: 400;
  color: #7a6a6a;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: flex-start;
  text-align: left;
  margin: 0 auto;
  max-width: 280px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 16px;
  opacity: 0.95;
  color: #6b5b5b;
}

.feature-item .el-icon {
  font-size: 20px;
  color: #d4a5a5;
}

/* 装饰圆圈 */
.decoration-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.25);
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  right: -100px;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: 10%;
  left: -60px;
}

.circle-3 {
  width: 150px;
  height: 150px;
  bottom: -40px;
  right: 20%;
}

/* 右侧表单区域 */
.auth-form-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  background-color: #faf8f5;
}

.form-container {
  width: 100%;
  max-width: 440px;
  background: #ffffff;
  border-radius: 20px;
  padding: 48px 40px;
  box-shadow: 0 8px 32px rgba(200, 170, 160, 0.15);
}

.form-header {
  text-align: center;
  margin-bottom: 32px;
}

.form-title {
  font-size: 28px;
  font-weight: 600;
  color: #6b5b5b;
  margin: 0 0 8px 0;
}

.form-subtitle {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

/* 表单样式 */
.auth-form {
  margin-top: 24px;
}

.auth-form :deep(.el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 0 0 1px #e5e7eb inset;
  padding: 4px 12px;
  transition: all 0.3s ease;
}

.auth-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #d1d5db inset;
}

.auth-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #d4a5a5 inset, 0 0 0 3px rgba(212, 165, 165, 0.12);
}

.auth-form :deep(.el-input__inner) {
  height: 44px;
  font-size: 15px;
}

.auth-form :deep(.el-input__icon) {
  color: #9ca3af;
}

/* 性别选择器 */
.gender-select {
  width: 100%;
}

.gender-select :deep(.el-input__wrapper) {
  border-radius: 10px;
}

.gender-icon {
  margin-right: 8px;
  font-size: 16px;
}

.gender-icon.male {
  color: #3b82f6;
}

.gender-icon.female {
  color: #ec4899;
}

/* 表单选项 */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.form-options :deep(.el-checkbox__label) {
  font-size: 14px;
  color: #4b5563;
}

/* 提交按钮 - 莫兰迪粉色 */
.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 10px;
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  border: none;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 8px 20px rgba(212, 165, 165, 0.35);
}

.submit-btn:active {
  transform: translateY(0);
}

/* 表单底部 */
.form-footer {
  text-align: center;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #e5e7eb;
}

.footer-text {
  color: #6b7280;
  font-size: 14px;
}

.switch-btn {
  font-size: 14px;
  font-weight: 500;
  margin-left: 4px;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .auth-decoration {
    display: none;
  }

  .auth-form-section {
    padding: 20px;
    background: linear-gradient(135deg, #e8b4b8 0%, #f3d7a9 50%, #e9d5c4 100%);
  }

  .form-container {
    padding: 36px 28px;
    box-shadow: 0 8px 32px rgba(150, 130, 120, 0.2);
  }
}

@media (max-width: 576px) {
  .auth-container {
    background-color: #faf8f5;
  }

  .auth-form-section {
    padding: 16px;
    background: linear-gradient(135deg, #e8b4b8 0%, #f3d7a9 50%, #e9d5c4 100%);
  }

  .form-container {
    padding: 24px 20px;
    box-shadow: 0 4px 20px rgba(150, 130, 120, 0.15);
    border-radius: 16px;
  }

  .form-title {
    font-size: 24px;
  }

  .submit-btn {
    height: 44px;
  }
}

/* 动画效果 */
.auth-form-wrapper {
  animation: fadeIn 0.4s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 表单验证错误提示样式 */
.auth-form :deep(.el-form-item__error) {
  padding-top: 4px;
  font-size: 12px;
}

/* 输入框前缀图标 */
.auth-form :deep(.el-input__prefix-inner) {
  color: #9ca3af;
}
</style>
