<template>
  <div class="student-container">
    <!-- 页面标题区 -->
    <div class="page-header">
      <div class="header-title">
        <div class="title-icon-wrapper">
          <el-icon class="title-icon"><Document /></el-icon>
        </div>
        <div class="title-text">
          <h2>学生管理</h2>
          <p class="subtitle">查看和管理所有学生信息</p>
        </div>
      </div>
      <el-button type="primary" class="add-btn" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增学生
      </el-button>
    </div>

    <!-- 查询表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="姓名">
          <el-input
            v-model="searchForm.name"
            placeholder="请输入姓名"
            clearable
            :prefix-icon="User"
          />
        </el-form-item>
        <el-form-item label="地址">
          <el-input
            v-model="searchForm.address"
            placeholder="请输入地址"
            clearable
            :prefix-icon="Location"
          />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="searchForm.gender" placeholder="请选择" clearable>
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
        <el-form-item label="年龄">
          <el-input
            v-model="searchForm.age"
            type="number"
            placeholder="请输入年龄"
            clearable
            :prefix-icon="Calendar"
          />
        </el-form-item>
        <el-form-item class="search-actions">
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            查询
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><RefreshRight /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 学生列表 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="studentList"
        stripe
        style="width: 100%"
        v-loading="loading"
        :header-cell-style="headerCellStyle"
      >
        <el-table-column label="头像" width="80" align="center">
          <template #default="scope">
            <div class="avatar-wrapper">
              <img v-if="scope.row.avatar" :src="scope.row.avatar" class="student-avatar" alt="头像" />
              <div v-else class="avatar-fallback">{{ getFirstChar(scope.row.name) }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="id" label="学号" width="100" align="center" />
        <el-table-column prop="name" label="姓名" width="120" align="center">
          <template #default="scope">
            <span class="student-name">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="性别" width="90" align="center">
          <template #default="scope">
            <el-tag
              :type="scope.row.gender === '男' ? 'primary' : 'danger'"
              size="small"
              effect="light"
              class="gender-tag"
            >
              <el-icon v-if="scope.row.gender === '男'"><Male /></el-icon>
              <el-icon v-else><Female /></el-icon>
              {{ scope.row.gender }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="150" align="center">
          <template #default="scope">
            <span class="age-text">{{ scope.row.age }} 岁</span>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="地址" width="350" align="center" show-overflow-tooltip />
        <el-table-column prop="phone" label="电话" width="130" align="center" />
        <el-table-column label="操作"  fixed="right" align="center">
          <template #default="scope">
            <div class="action-buttons">
              <!-- 编辑按钮：只能编辑自己的信息 -->
              <el-button
                type="primary"
                size="small"
                class="action-btn edit-btn"
                :disabled="!canEdit(scope.row)"
                @click="handleEdit(scope.row)"
              >
                <el-icon><Edit /></el-icon>
              </el-button>
              <!-- 删除按钮：只有 admin 和 root 可以使用 -->
              <el-button
                type="danger"
                size="small"
                class="action-btn delete-btn"
                @click="handleDeleteClick(scope.row)"
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[5, 8, 10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      class="student-dialog"
      destroy-on-close
    >
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px" class="student-form">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入姓名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="formData.gender" placeholder="请选择性别" class="gender-select">
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
        <el-form-item label="年龄" prop="age">
          <el-input v-model.number="formData.age" type="number" placeholder="请输入年龄" :prefix-icon="Calendar" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="formData.address" placeholder="请输入地址" :prefix-icon="Location" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入电话" :prefix-icon="Phone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import {
  getStudentListAPI,
  addStudentAPI,
  updateStudentAPI,
  deleteStudentAPI
} from '../api/index'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Document,
  Plus,
  User,
  Location,
  Male,
  Female,
  Calendar,
  Search,
  RefreshRight,
  Edit,
  Delete,
  Phone
} from '@element-plus/icons-vue'

const searchForm = reactive({
  name: '',
  address: '',
  gender: '',
  age: ''
})

const studentList = ref([])
const total = ref(0)
const loading = ref(false)
const pagination = reactive({
  page: 1,
  pageSize: 8
})
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const formData = reactive({
  id: null,
  name: '',
  gender: '',
  age: 0,
  address: '',
  phone: ''
})

// 当前登录用户信息
const currentUser = ref({})

// 从 localStorage 获取当前登录用户信息
const getCurrentUser = () => {
  const userInfoStr = localStorage.getItem('userInfo')
  if (userInfoStr) {
    try {
      currentUser.value = JSON.parse(userInfoStr)
    } catch (e) {
      console.error('解析用户信息失败', e)
      currentUser.value = {}
    }
  }
}

// 计算当前登录用户的用户名
const currentUsername = computed(() => {
  return currentUser.value.username || ''
})

// 判断是否为管理员（admin 或 root）
const isAdmin = computed(() => {
  const username = currentUser.value.username
  return username === 'admin' || username === 'root'
})

// 判断是否可以编辑该学生（只能编辑自己的信息）
const canEdit = (row) => {
  return row.username === currentUsername.value
}

// 判断是否可以删除（只有 admin 和 root 可以删除）
const canDelete = () => {
  return isAdmin.value
}

const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  age: [
    { required: true, message: '请输入年龄', trigger: 'blur' },
    { type: 'number', min: 1, max: 100, message: '年龄应在1-100之间', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 表头样式
const headerCellStyle = () => ({
  background: '#faf8f5',
  color: '#6b5b5b',
  fontWeight: 600,
  fontSize: '14px'
})

const getStudentList = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      page: pagination.page,
      pageSize: pagination.pageSize
    }

    const response = await getStudentListAPI(params)

    if (response && response.data && response.data.rows) {
      studentList.value = response.data.rows || []
      total.value = response.data.total || 0
    } else if (Array.isArray(response)) {
      studentList.value = response
      total.value = response.length
    } else if (response && response.rows) {
      studentList.value = response.rows || []
      total.value = response.total || 0
    } else {
      console.error('返回数据格式异常:', response)
      studentList.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('获取学生列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  getStudentList()
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.address = ''
  searchForm.gender = ''
  searchForm.age = ''
  pagination.page = 1
  getStudentList()
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  getStudentList()
}

const handleCurrentChange = (current) => {
  pagination.page = current
  getStudentList()
}

const handleAdd = () => {
  dialogTitle.value = '新增学生'
  formData.id = null
  formData.name = ''
  formData.gender = ''
  formData.age = 0
  formData.address = ''
  formData.phone = ''
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑学生'
  formData.id = row.id
  formData.name = row.name
  formData.gender = row.gender
  formData.age = row.age
  formData.address = row.address
  formData.phone = row.phone
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()

    if (formData.age) {
      formData.age = Number(formData.age)
    }

    if (formData.id) {
      await updateStudentAPI(formData)
      ElMessage.success('更新成功')
    } else {
      await addStudentAPI(formData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    getStudentList()
  } catch (error) {
    if (error !== false) {
      console.error('操作失败:', error)
    }
  }
}

// 处理删除按钮点击
const handleDeleteClick = (row) => {
  // 检查是否有删除权限
  if (!canDelete()) {
    // 非管理员用户，显示提示框
    ElMessageBox.alert('v我50即可开放权限(›´ω`‹ )', '权限不足', {
      confirmButtonText: '知道了',
      type: 'warning',
      center: true
    })
    return
  }
  // 有权限，执行删除确认
  handleDelete(row.id)
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该学生吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await deleteStudentAPI(id)
    ElMessage.success('删除成功')
    getStudentList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

// 获取名字首字（用于默认头像展示）
const getFirstChar = (name) => {
  if (!name) return ''
  return name.charAt(0)
}

onMounted(() => {
  // 获取当前登录用户信息
  getCurrentUser()
  // 获取学生列表
  getStudentList()
})
</script>

<style scoped>
/* 页面容器 */
.student-container {
  max-width: 1400px;
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

.add-btn {
  height: 44px;
  padding: 0 24px;
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

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(212, 165, 165, 0.4);
}

/* 搜索卡片 */
.search-card {
  border-radius: 16px;
  border: none;
  background: #ffffff;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
}

.search-card :deep(.el-card__body) {
  padding: 24px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: flex-end;
}

.search-form :deep(.el-form-item) {
  margin-bottom: 0;
  margin-right: 0;
}

.search-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #6b5b5b;
}

.search-form :deep(.el-input__wrapper),
.search-form :deep(.el-select .el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 0 0 1px #e5e7eb inset;
}

.search-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #d4a5a5 inset;
}

.search-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #d4a5a5 inset, 0 0 0 3px rgba(212, 165, 165, 0.1);
}

.search-actions {
  margin-left: auto;
}

.search-actions .el-button {
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.search-actions .el-button--primary {
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  border: none;
}

/* 性别图标 */
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

/* 表格卡片 */
.table-card {
  border-radius: 16px;
  border: none;
  background: #ffffff;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
}

.table-card :deep(.el-card__body) {
  padding: 0;
}

/* 表格样式 */
.table-card :deep(.el-table) {
  border-radius: 16px 16px 0 0;
}

.table-card :deep(.el-table__header th) {
  background: #faf8f5 !important;
}

.table-card :deep(.el-table__row) {
  transition: all 0.2s ease;
}

.table-card :deep(.el-table__row:hover) {
  background-color: #fdf9f7 !important;
}

/* 头像样式 */
.avatar-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
}

.student-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(212, 165, 165, 0.2);
  transition: all 0.3s ease;
}

.student-avatar:hover {
  border-color: rgba(212, 165, 165, 0.4);
  transform: scale(1.05);
}

.avatar-fallback {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
  border: 2px solid rgba(212, 165, 165, 0.2);
}

/* 学生姓名 */
.student-name {
  font-weight: 500;
  color: #6b5b5b;
}

/* 性别标签 */
.gender-tag {
  border-radius: 12px;
  padding: 4px 12px;
}

.gender-tag :deep(.el-icon) {
  margin-right: 4px;
}

/* 年龄 */
.age-text {
  color: #9ca3af;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  justify-content: center;
  gap: 8px;
}

.action-btn {
  border-radius: 8px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  transition: all 0.3s ease;
  padding: 6px 10px;
  min-width: 32px;
}

.edit-btn {
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  border: none;
}

.edit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(212, 165, 165, 0.3);
}

/* 禁用状态的编辑按钮样式 */
.edit-btn.is-disabled {
  background: linear-gradient(135deg, #d1d5db 0%, #9ca3af 100%);
  opacity: 0.6;
  cursor: not-allowed;
}

.edit-btn.is-disabled:hover {
  transform: none;
  box-shadow: none;
}

.delete-btn {
  background: linear-gradient(135deg, #f87171 0%, #ef4444 100%);
  border: none;
}

.delete-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(239, 68, 68, 0.3);
}

/* 分页 */
.pagination-wrapper {
  padding: 20px 24px;
  display: flex;
  justify-content: flex-end;
  border-top: 1px solid #f3f4f6;
}

.pagination-wrapper :deep(.el-pagination) {
  --el-pagination-hover-color: #d4a5a5;
}

.pagination-wrapper :deep(.el-pagination .el-select .el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #d4a5a5 inset !important;
}

.pagination-wrapper :deep(.el-pagination .el-pager li.active) {
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  color: #ffffff;
}

/* 对话框 */
.student-dialog :deep(.el-dialog) {
  border-radius: 20px;
  overflow: hidden;
}

.student-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #faf8f5 0%, #fdf9f7 100%);
  padding: 20px 24px;
  margin-right: 0;
  border-bottom: 1px solid rgba(212, 165, 165, 0.1);
}

.student-dialog :deep(.el-dialog__title) {
  font-size: 18px;
  font-weight: 600;
  color: #6b5b5b;
}

.student-dialog :deep(.el-dialog__body) {
  padding: 24px;
}

.student-form :deep(.el-input__wrapper),
.student-form :deep(.el-select .el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 0 0 1px #e5e7eb inset;
}

.student-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #d4a5a5 inset;
}

.student-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #d4a5a5 inset, 0 0 0 3px rgba(212, 165, 165, 0.1);
}

.gender-select {
  width: 100%;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.dialog-footer .el-button {
  height: 40px;
  padding: 0 24px;
  border-radius: 10px;
}

.dialog-footer .el-button--primary {
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  border: none;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .add-btn {
    width: 100%;
    justify-content: center;
  }

  .search-form {
    flex-direction: column;
  }

  .search-form :deep(.el-form-item) {
    width: 100%;
    margin-right: 0;
  }

  .search-actions {
    margin-left: 0;
    width: 100%;
  }

  .search-actions .el-button {
    flex: 1;
  }

  .title-text h2 {
    font-size: 20px;
  }

  .pagination-wrapper {
    justify-content: center;
  }
}

@media (min-width: 769px) and (max-width: 1024px) {
  .search-form {
    gap: 12px;
  }
}
</style>
