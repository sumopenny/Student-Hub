<template>
  <div class="update-log-container">
    <!-- 页面标题区 -->
    <div class="page-header">
      <div class="header-title">
        <div class="title-icon-wrapper">
          <el-icon class="title-icon"><Clock /></el-icon>
        </div>
        <div class="title-text">
          <h2>更新日志</h2>
          <p class="subtitle">记录项目每一次的成长与变化</p>
        </div>
      </div>
    </div>

    <!-- 更新日志列表 -->
    <div class="log-list">
      <el-card
        v-for="log in logs"
        :key="log.id"
        class="log-card"
        shadow="never"
      >
        <div class="log-header">
          <div class="version-tag">{{ log.version }}</div>
          <div class="log-date">{{ log.date }}</div>
        </div>
        <div class="log-content">
          <div v-if="log.newFeatures && log.newFeatures.length" class="log-section">
            <div class="section-title new">
              <el-icon><Plus /></el-icon>
              <span>新增功能</span>
            </div>
            <ul class="feature-list">
              <li v-for="(feature, index) in log.newFeatures" :key="'new-' + index">
                {{ feature }}
              </li>
            </ul>
          </div>
          <div v-if="log.improvements && log.improvements.length" class="log-section">
            <div class="section-title improve">
              <el-icon><Edit /></el-icon>
              <span>优化改进</span>
            </div>
            <ul class="feature-list">
              <li v-for="(improvement, index) in log.improvements" :key="'imp-' + index">
                {{ improvement }}
              </li>
            </ul>
          </div>
          <div v-if="log.fixes && log.fixes.length" class="log-section">
            <div class="section-title fix">
              <el-icon><Tools /></el-icon>
              <span>问题修复</span>
            </div>
            <ul class="feature-list">
              <li v-for="(fix, index) in log.fixes" :key="'fix-' + index">
                {{ fix }}
              </li>
            </ul>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { Clock, Plus, Edit, Tools } from '@element-plus/icons-vue'

const logs = [
  {
    id: 1,
    version: 'v1.0.2',
    date: '2026-03-27',
    newFeatures: [
      '前端新增"新内容"页面',
      '更换标签页图标和名称'
    ],
    improvements: [
      '优化学生列表展示与接口',
      '完善学生列表权限控制',
      '优化后端传参，避免不必要参数传给前端。密码进行加密存储，提高安全性⭕⚠️'
    ],
    fixes: [
      '个人中心用户名显示问题'
    ]
  },
  {
    id: 2,
    version: 'v1.0.1',
    date: '2026-03-24',
    newFeatures: [
      '新增"新内容"模块，展示即将推出的功能',
      '新增"更新日志"模块，记录项目每次更新内容'
    ],
    improvements: [
      '优化左侧菜单结构，新增菜单项图标'
    ],
    fixes: []
  },
  {
    id: 3,
    version: 'v1.0.0',
    date: '2026-01-15',
    newFeatures: [
      '学生管理系统正式上线',
      '个人中心模块：查看和编辑个人信息',
      '学生列表模块：管理学生信息（增删改查）',
      '系统设置模块：自定义主题颜色',
      '留言板模块：用户反馈留言功能'
    ],
    improvements: [
      '采用莫兰迪粉色系设计，界面更加温馨舒适',
      '实现完整的用户登录认证流程',
      '响应式布局设计，支持不同屏幕尺寸'
    ],
    fixes: []
  }
]
</script>

<style scoped>
/* 页面容器 */
.update-log-container {
  max-width: 900px;
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

/* 更新日志列表 */
.log-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 日志卡片 */
.log-card {
  border-radius: 16px;
  border: none;
  background: #ffffff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.log-card :deep(.el-card__body) {
  padding: 24px;
}

/* 日志头部 */
.log-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f3f4f6;
}

.version-tag {
  display: inline-flex;
  align-items: center;
  padding: 6px 14px;
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  color: #ffffff;
  font-size: 14px;
  font-weight: 600;
  border-radius: 20px;
  box-shadow: 0 2px 8px rgba(212, 165, 165, 0.3);
}

.log-date {
  font-size: 14px;
  color: #9ca3af;
}

/* 日志内容 */
.log-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.log-section {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  font-weight: 600;
  color: #6b5b5b;
}

.section-title.new {
  color: #67c23a;
}

.section-title.improve {
  color: #e6a23c;
}

.section-title.fix {
  color: #f56c6c;
}

.section-title .el-icon {
  font-size: 16px;
}

.feature-list {
  margin: 0;
  padding-left: 24px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.feature-list li {
  font-size: 14px;
  color: #6b5b5b;
  line-height: 1.6;
  position: relative;
}

.feature-list li::marker {
  color: #d4a5a5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .title-text h2 {
    font-size: 20px;
  }

  .log-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .log-card :deep(.el-card__body) {
    padding: 16px;
  }
}
</style>
