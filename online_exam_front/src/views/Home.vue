<template>
  <a-config-provider :locale="zhCN">
    <div class="home-dashboard">
      <div class="home-main-row">
        <div class="home-main-left">
          <div class="home-stats">
            <a-card class="stat-card stat-card-blue" bordered>
              <div class="stat-icon stat-icon-blue">
                <i class="iconfont icon-kaoshi"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">5</div>
                <div class="stat-label">进行中考试</div>
              </div>
            </a-card>
            <a-card class="stat-card stat-card-green" bordered>
              <div class="stat-icon stat-icon-green">
                <i class="iconfont icon-user"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">128</div>
                <div class="stat-label">已结束考试</div>
              </div>
            </a-card>
          </div>
          <div class="home-content-row">
            <a-card class="home-announcement" title="系统公告" bordered>
              <ul>
                <li>欢迎使用企业在线考试管理后台！</li>
                <li>请及时关注考试安排与数据统计。</li>
                <li>如有疑问请联系系统管理员。</li>
              </ul>
            </a-card>
            <a-card class="home-quick" title="快捷操作" bordered>
              <a-button type="primary" block style="margin-bottom: 12px"
                >新建考试</a-button
              >
            </a-card>
          </div>
        </div>
        <div class="home-main-right">
          <a-card title="考试日历" class="home-calendar" bordered>
            <a-calendar
              fullscreen="false"
              :value="calendarValue"
              style="background: #fff; border-radius: 8px"
            />
          </a-card>
        </div>
      </div>
    </div>
  </a-config-provider>
</template>

<script setup>
import { ref } from "vue";
import dayjs from "dayjs";
import zhCN from "ant-design-vue/es/locale/zh_CN";
const calendarValue = ref(dayjs());
</script>

<style scoped>
.home-dashboard {
  padding: 32px 0 32px 0;
  min-height: 100vh;
}
.home-header {
  margin-bottom: 32px;
  text-align: center;
}
.home-title {
  font-size: 2.4rem;
  font-weight: 800;
  color: #1d39c4;
  letter-spacing: 2px;
}
.home-desc {
  color: #888;
  margin-top: 8px;
  font-size: 18px;
}
.home-main-row {
  display: flex;
  gap: 32px;
  max-width: 1400px;
  margin: 0 auto;
}
.home-main-left {
  flex: 2.2;
  display: flex;
  flex-direction: column;
  gap: 24px;
}
.home-main-right {
  flex: 1.2;
  display: flex;
  flex-direction: column;
  gap: 24px;
}
.home-stats {
  display: flex;
  gap: 24px;
  margin-bottom: 8px;
}
.stat-card {
  flex: 1;
  display: flex;
  align-items: center;
  min-width: 180px;
  border-radius: 16px;
  box-shadow: 0 4px 24px 0 rgba(0, 0, 0, 0.06);
  padding: 0 0 0 0;
  background: linear-gradient(120deg, #fff 60%, #f0f4ff 100%);
}
.stat-card-blue {
  border: 1.5px solid #1677ff;
}
.stat-card-green {
  border: 1.5px solid #52c41a;
}
.stat-card-orange {
  border: 1.5px solid #faad14;
}
.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  margin: 0 18px 0 24px;
  color: #fff;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.08);
}
.stat-icon-blue {
  background: #1677ff;
}
.stat-icon-green {
  background: #52c41a;
}
.stat-icon-orange {
  background: #faad14;
}
.stat-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.stat-value {
  font-size: 2.1rem;
  font-weight: 700;
  margin-bottom: 2px;
}
.stat-label {
  color: #888;
  font-size: 15px;
}
.home-content-row {
  display: flex;
  gap: 24px;
  margin-top: 12px;
}
.home-announcement,
.home-quick {
  flex: 1;
}
@media (max-width: 1200px) {
  .home-main-row {
    flex-direction: column;
    gap: 16px;
  }
  .home-main-left,
  .home-main-right {
    flex: unset;
  }
  .home-content-row {
    flex-direction: column;
    gap: 16px;
  }
}
</style>
