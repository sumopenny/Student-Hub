<template>
  <div class="contact-container">
    <!-- 页面标题区 -->
    <div class="page-header">
      <div class="header-title">
        <div class="title-icon-wrapper">
          <el-icon class="title-icon"><Message /></el-icon>
        </div>
        <div class="title-text">
          <h2>联系我</h2>
          <p class="subtitle">欢迎来找我玩呀</p>
        </div>
      </div>
    </div>

    <!-- 联系方式卡片 -->
    <el-card class="contact-card" shadow="never">
      <div class="contact-content">
        <!-- QQ 联系方式 -->
        <div class="contact-item">
          <div class="contact-icon-wrapper qq">
            <svg class="contact-icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
              <path d="M727.2 613.6c-17.6 0-32 14.4-32 32s14.4 32 32 32 32-14.4 32-32-14.4-32-32-32z" fill="#FFFFFF"/>
              <path d="M406.4 324c-139.2 0-252.8 65.6-252.8 146.4 0 26.4 18.4 50.4 48 66.4-5.6 14.4-18.4 30.4-36.8 39.2 0 0 22.4 28 58.4 32 0 0 15.2-8.8 30.4-8.8 0 0 21.6 13.6 34.4 13.6 0 0-52-24.8-63.2-73.6 0 0 9.6 4.8 24.8 4.8 0 0 25.6-2.4 39.2-9.6 0 0-11.2 32.8-33.6 41.6 0 0 19.2-9.6 32-20 40 48 104.8 78.4 177.6 78.4 139.2 0 252.8-65.6 252.8-146.4S545.6 324 406.4 324z" fill="#FFFFFF"/>
            </svg>
          </div>
          <div class="contact-info">
            <div class="contact-label">QQ</div>
            <div class="contact-value">2960366646</div>
          </div>
          <a
            href="https://qm.qq.com/q/bNaYmmCuRy"
            target="_blank"
            class="contact-btn"
          >
            <el-icon><Link /></el-icon>
            加我为QQ好友
          </a>
        </div>

        <!-- B站联系方式 -->
        <div class="contact-item">
          <div class="contact-icon-wrapper bilibili">
            <svg class="contact-icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
              <path d="M872.8 445.6c-12-12-28.8-19.2-46.4-20.8 0 0-28-2.4-52 10.4-14.4 7.2-20.8 12.8-20.8 12.8s-27.2-16-63.2-16c0 0-43.2-4-84.8 12.8-28 11.2-42.4 19.2-42.4 19.2s-23.2-16-52-16c-22.4 0-42.4 11.2-54.4 27.2-13.6 18.4-20 40.8-20 66.4 0 58.4 48 122.4 139.2 163.2 48 21.6 91.2 32 126.4 32 17.6 0 32-1.6 43.2-4.8 28-8 52-25.6 52-25.6s-6.4 12-20 22.4c-12.8 9.6-28.8 14.4-46.4 14.4-36.8 0-67.2-18.4-87.2-44-17.6-22.4-27.2-48.8-27.2-77.6 0-12.8 3.2-24.8 8.8-35.2 4.8-8.8 10.4-16 16.8-22.4-10.4-6.4-20-12.8-28.8-20-20-16.8-32-38.4-32-61.6 0-13.6 3.2-26.4 9.6-38.4 8-15.2 20-27.2 35.2-35.2 13.6-7.2 28.8-11.2 45.6-12 0 0 26.4-1.6 55.2 10.4 20.8 8.8 28 13.6 28 13.6s21.6-17.6 56-17.6c38.4 0 69.6 20.8 87.2 49.6 10.4 16.8 17.6 36 17.6 56.8 0 20-6.4 40-16.8 56-8 12.8-18.4 24-30.4 32.8 8 3.2 16.8 6.4 24.8 10.4 28 14.4 48 36 56 61.6 5.6 18.4 8 36.8 8 55.2 0 37.6-14.4 72.8-40 100-22.4 24-51.2 38.4-83.2 40 0 0 38.4 20 91.2 20c41.6 0 80-15.2 109.6-42.4 28-25.6 44-60 44-98.4 0-33.6-12-64.8-32-91.2 0 0 19.2-12 32-28 14.4-18.4 22.4-40 22.4-63.2 0-30.4-11.2-58.4-30.4-81.6z" fill="#FFFFFF"/>
              <path d="M232 368h96v32H232zM232 432h128v32H232zM232 496h80v32H232z" fill="#FFFFFF"/>
            </svg>
          </div>
          <div class="contact-info">
            <div class="contact-label">哔哩哔哩</div>
            <div class="contact-value">素茉penny</div>
          </div>
          <a
            href="https://space.bilibili.com/85588613"
            target="_blank"
            class="contact-btn"
          >
            <el-icon><Link /></el-icon>
            访问B站主页
          </a>
        </div>

        <!-- 微信联系方式 -->
        <div class="contact-item">
          <div class="contact-icon-wrapper wechat">
            <svg class="contact-icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
              <path d="M690.4 497.6c-12 0-23.2 1.6-34.4 4.8-14.4 4-28 10.4-38.4 20-10.4 9.6-17.6 22.4-20 38.4-1.6 10.4-0.8 21.6 2.4 32 5.6 17.6 16 32.8 29.6 44.8 13.6 12 30.4 20.8 48.8 26.4 17.6 5.6 36.8 8 56 8 20.8 0 40-2.4 57.6-7.2 17.6-4.8 33.6-12.8 47.2-24 13.6-11.2 24-25.6 30.4-42.4 6.4-16.8 8.8-35.2 6.4-53.6-2.4-18.4-8.8-36-19.2-51.2-10.4-15.2-24-27.2-40-35.2-16-8-34.4-12.8-53.6-13.6-17.6-0.8-36 1.6-53.6 6.4-1.6 0.8-3.2 0.8-4.8 1.6 1.6-0.8 3.2-0.8 4.8-1.6z" fill="#FFFFFF"/>
              <path d="M512 64C264 64 64 264 64 512s200 448 448 448 448-200 448-448S760 64 512 64z m209.6 656c-16.8 16.8-36.8 28-58.4 33.6-20 5.6-41.6 7.2-62.4 5.6-21.6-1.6-42.4-6.4-61.6-14.4-19.2-8-36.8-18.4-51.2-32-14.4-13.6-25.6-29.6-33.6-48-8-18.4-12.8-38.4-14.4-58.4-1.6-21.6 0-43.2 5.6-63.2 5.6-20 14.4-38.4 26.4-54.4 12-16 27.2-29.6 44.8-40 17.6-10.4 37.6-17.6 58.4-21.6 20-4 41.6-4.8 62.4-2.4 20.8 1.6 40.8 6.4 59.2 14.4 18.4 8 35.2 18.4 49.6 31.2 14.4 12.8 25.6 28 33.6 45.6 8 17.6 12.8 36.8 14.4 56.8 1.6 20.8-0.8 41.6-6.4 61.6z" fill="#FFFFFF"/>
              <path d="M352 384c-17.6 0-32 14.4-32 32s14.4 32 32 32 32-14.4 32-32-14.4-32-32-32z" fill="#FFFFFF"/>
              <path d="M512 384c-17.6 0-32 14.4-32 32s14.4 32 32 32 32-14.4 32-32-14.4-32-32-32z" fill="#FFFFFF"/>
              <path d="M672 512c-17.6 0-32 14.4-32 32s14.4 32 32 32 32-14.4 32-32-14.4-32-32-32z" fill="#FFFFFF"/>
              <path d="M352 544c-17.6 0-32 14.4-32 32s14.4 32 32 32 32-14.4 32-32-14.4-32-32-32z" fill="#FFFFFF"/>
              <path d="M512 544c-17.6 0-32 14.4-32 32s14.4 32 32 32 32-14.4 32-32-14.4-32-32-32z" fill="#FFFFFF"/>
              <path d="M672 544c-17.6 0-32 14.4-32 32s14.4 32 32 32 32-14.4 32-32-14.4-32-32-32z" fill="#FFFFFF"/>
            </svg>
          </div>
          <div class="contact-info">
            <div class="contact-label">微信</div>
            <div class="contact-value">你猜</div>
          </div>
          <div class="contact-tip">
            <el-icon><InfoFilled /></el-icon>
            添加请备注来源
          </div>
        </div>
      </div>

      <!-- 温馨提示 -->
      <div class="contact-tips">
        <el-icon><InfoFilled /></el-icon>
        <span>温馨提示：添加好友时请注明来源，否则可能不会通过哦~</span>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { Message, Link, InfoFilled } from '@element-plus/icons-vue'
</script>

<style scoped>
/* 页面容器 */
.contact-container {
  max-width: 800px;
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

/* 联系方式卡片 */
.contact-card {
  border-radius: 16px;
  border: none;
  background: #ffffff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.contact-card :deep(.el-card__body) {
  padding: 32px;
}

/* 联系方式列表 */
.contact-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 联系方式项 */
.contact-item {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #fdf9f7 0%, #faf8f5 100%);
  border-radius: 12px;
  transition: all 0.3s ease;
}

.contact-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(212, 165, 165, 0.15);
}

/* 联系方式图标 */
.contact-icon-wrapper {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.contact-icon-wrapper.qq {
  background: linear-gradient(135deg, #12b7f5 0%, #5bc0de 100%);
}

.contact-icon-wrapper.bilibili {
  background: linear-gradient(135deg, #00a1d6 0%, #00c8d4 100%);
}

.contact-icon-wrapper.wechat {
  background: linear-gradient(135deg, #07c160 0%, #06ad56 100%);
}

.contact-icon {
  width: 32px;
  height: 32px;
}

/* 联系方式信息 */
.contact-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.contact-label {
  font-size: 13px;
  color: #9ca3af;
  font-weight: 500;
}

.contact-value {
  font-size: 18px;
  font-weight: 600;
  color: #6b5b5b;
}

/* 联系方式按钮 */
.contact-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: linear-gradient(135deg, #d4a5a5 0%, #e8b4b8 100%);
  color: #ffffff;
  font-size: 14px;
  font-weight: 500;
  text-decoration: none;
  border-radius: 10px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(212, 165, 165, 0.3);
}

.contact-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(212, 165, 165, 0.4);
}

/* 联系方式提示 */
.contact-tip {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 10px 16px;
  background-color: rgba(107, 91, 91, 0.08);
  color: #9ca3af;
  font-size: 13px;
  border-radius: 10px;
}

/* 温馨提示 */
.contact-tips {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 24px;
  padding: 16px;
  background-color: rgba(212, 165, 165, 0.1);
  border-radius: 12px;
  color: #9ca3af;
  font-size: 14px;
}

.contact-tips .el-icon {
  color: #d4a5a5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .title-text h2 {
    font-size: 20px;
  }

  .contact-card :deep(.el-card__body) {
    padding: 20px;
  }

  .contact-item {
    flex-direction: column;
    text-align: center;
    gap: 12px;
  }

  .contact-btn {
    width: 100%;
    justify-content: center;
  }
}
</style>
