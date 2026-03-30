<template>
  <div class="new-content-container">
    <!-- 页面标题区 -->
    <div class="page-header">
      <div class="header-title">
        <div class="title-icon-wrapper">
          <el-icon class="title-icon"><FolderOpened /></el-icon>
        </div>
        <div class="title-text">
          <h2>新内容</h2>
          <p class="subtitle">探索更多精彩功能</p>
        </div>
      </div>
    </div>

    <!-- 敬请期待卡片 -->
    <el-card class="coming-soon-card" shadow="never">
      <div class="coming-soon-content">
        <div class="icon-wrapper">
          <el-icon class="soon-icon"><Clock /></el-icon>
        </div>
        <h3 class="coming-title">敬请期待</h3>
        <p class="coming-desc">新功能正在马不停蹄地开发中</p>
        <p class="coming-subdesc">敬请期待，更多精彩内容即将上线，也欢迎您向我提建议~</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { FolderOpened, Clock } from '@element-plus/icons-vue'
</script>

<style scoped>
/* 页面容器 */
.new-content-container {
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

/* 敬请期待卡片 */
.coming-soon-card {
  border-radius: 16px;
  border: none;
  background: #ffffff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.coming-soon-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 40px;
  text-align: center;
}

.icon-wrapper {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #f5eeeb 0%, #fdf9f7 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24px;
  box-shadow: 0 4px 20px rgba(212, 165, 165, 0.2);
}

.soon-icon {
  font-size: 48px;
  color: #d4a5a5;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.7;
    transform: scale(1.05);
  }
}

.coming-title {
  margin: 0 0 12px 0;
  font-size: 28px;
  font-weight: 600;
  color: #6b5b5b;
}

.coming-desc {
  margin: 0 0 8px 0;
  font-size: 16px;
  color: #9ca3af;
}

.coming-subdesc {
  margin: 0;
  font-size: 14px;
  color: #d4a5a5;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .title-text h2 {
    font-size: 20px;
  }

  .coming-soon-content {
    padding: 40px 20px;
  }

  .icon-wrapper {
    width: 80px;
    height: 80px;
  }

  .soon-icon {
    font-size: 36px;
  }

  .coming-title {
    font-size: 24px;
  }
}
</style>
