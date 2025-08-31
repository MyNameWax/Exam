<template>
  <a-config-provider :locale="zhCN">
    <div class="home-dashboard">
      <!-- 页面标题 -->
      <div class="home-header">
        <h1 class="home-title">企业在线考试系统</h1>
        <p class="home-desc">高效管理您的考试流程，提升教学评估效率</p>
      </div>

      <div class="home-main-row">
        <div class="home-main-left">
          <!-- 统计卡片区域 -->
          <div class="home-stats">
            <a-card class="stat-card stat-card-blue" bordered>
              <div class="stat-icon stat-icon-blue">
                <ClockCircleOutlined/>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ examDataStaticObjForm.ingExamCount }}</div>
                <div class="stat-label">进行中考试</div>
              </div>
            </a-card>
            <a-card class="stat-card stat-card-orange" bordered>
              <div class="stat-icon stat-icon-orange">
                <CalendarOutlined/>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ examDataStaticObjForm.notStartExamCount }}</div>
                <div class="stat-label">未开始考试</div>
              </div>
            </a-card>
            <a-card class="stat-card stat-card-green" bordered>
              <div class="stat-icon stat-icon-green">
                <FileTextOutlined/>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ examDataStaticObjForm.endExamCount }}</div>
                <div class="stat-label">已结束考试</div>
              </div>
            </a-card>
          </div>

          <!-- 内容区域 -->
          <div class="home-content-row">
            <a-card class="home-announcement" title="系统公告" bordered>
              <ul>
                <li class="announcement-item">欢迎使用企业在线考试管理后台！</li>
                <li class="announcement-item">请及时关注考试安排与数据统计。</li>
                <li class="announcement-item">如有疑问请联系系统管理员。</li>
              </ul>
            </a-card>
            <a-card class="home-quick" title="快捷操作" bordered>
              <a-button type="primary" block style="margin-bottom: 12px">
                <FileAddOutlined/>
                新建考试
              </a-button>
              <a-button type="default" block>
                <FileSearchOutlined/>
                查看历史记录
              </a-button>
            </a-card>
          </div>

          <!-- 即将开始的考试列表 -->
          <a-card class="home-upcoming-exams" title="即将开始的考试" bordered>
            <a-table :columns="columns" :data-source="upcomingExams" :pagination="false" size="small">
              <template #bodyCell="{ column, record }">
                <template v-if="column.key === 'action'">
                  <a-button type="link" @click="viewExam(record.id)">查看详情</a-button>
                </template>
              </template>
            </a-table>
          </a-card>
        </div>


      </div>
    </div>
  </a-config-provider>
</template>

<script setup>
import {computed, getCurrentInstance, onMounted, ref} from "vue";
import dayjs from "dayjs";
import zhCN from "ant-design-vue/es/locale/zh_CN";
import {
  CalendarOutlined,
  ClockCircleOutlined,
  FileAddOutlined,
  FileSearchOutlined,
  FileTextOutlined
} from '@ant-design/icons-vue';

const calendarValue = ref(dayjs());
const {proxy} = getCurrentInstance();
const API = proxy.$API;

let examDataStaticObjForm = ref({
  endExamCount: 0,
  notStartExamCount: 0,
  ingExamCount: 0
})

onMounted(async () => {
  const examDataStaticObj = await examDataStaticAPI()
  const wellExamList = await wellStartExamListAPI()
  upcomingExams.value = wellExamList.data
  examDataStaticObjForm.value.endExamCount = examDataStaticObj.data.endExamCount
  examDataStaticObjForm.value.notStartExamCount = examDataStaticObj.data.notStartExamCount
  examDataStaticObjForm.value.ingExamCount = examDataStaticObj.data.ingExamCount
})

/**
 * 即将开始考试的列表
 * @returns {Promise<*>}
 */
const wellStartExamListAPI = async () => {
  const res = await API.index.wellStartExamList()
  return res.data
}
/**
 * 考试数据的统计
 * @returns {Promise<*>}
 */
const examDataStaticAPI = async () => {
  const res = await API.index.userExamDataStatic()
  return res.data
}

// 即将开始的考试数据
const upcomingExams = ref([]);

// 表格列配置
const columns = [
  {
    title: '考试名称',
    dataIndex: 'examName',
    key: 'examName',
    align: 'center'
  },
  {
    title: '考试日期',
    dataIndex: 'examDate',
    key: 'examDate',
    align: 'center'
  },
  {
    title: '时长',
    dataIndex: 'examTime',
    key: 'examTime',
    align: 'center'
  },
  {
    title: '及格分',
    dataIndex: 'examPassScore',
    key: 'examPassScore',
    align: 'center'
  },
  {
    title: '满分',
    dataIndex: 'score',
    key: 'score',
    align: 'center'
  },
  {
    title: '操作',
    key: 'action',
    align: 'center'
  },
];

// 日历事件数据
const calendarEvents = ref({
  '2025-08-31': [
    {key: '1', type: 'success', title: 'Vue.js框架应用测试'},
  ],

});

// 获取特定日期的事件
const getDayEvents = computed(() => {
  return (value) => {
    return calendarEvents.value[dayjs(value).format('YYYY-MM-DD')] || [];
  };
});

// 日历选择事件
const onSelect = (value) => {
  calendarValue.value = value;
};

// 日历面板变化事件
const onPanelChange = (value) => {
  calendarValue.value = value;
};

// 查看考试详情
const viewExam = (id) => {
  // 实际项目中这里会跳转到考试详情页
  console.log('查看考试详情:', id);
};
</script>

<style scoped>
.home-dashboard {
  padding: 24px 0;
  min-height: calc(100vh - 64px);
}

.home-header {
  margin-bottom: 32px;
  text-align: center;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.home-title {
  font-size: 2.4rem;
  font-weight: 800;
  color: black;
  margin-bottom: 8px;
  letter-spacing: 2px;
}

.home-desc {
  color: rgba(255, 255, 255, 0.9);
  font-size: 18px;
  color: black;
  margin: 0;
}

.home-main-row {
  display: flex;
  gap: 24px;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
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
  gap: 16px;
  margin-bottom: 8px;
}

.stat-card {
  flex: 1;
  display: flex;
  align-items: center;
  min-width: 160px;
  border-radius: 12px;
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  background: white;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px 0 rgba(0, 0, 0, 0.15);
}

.stat-card-blue {
  border-left: 4px solid #1677ff;
}

.stat-card-green {
  border-left: 4px solid #52c41a;
}

.stat-card-orange {
  border-left: 4px solid #faad14;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 16px;
  color: #fff;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.1);
}

.stat-icon-blue {
  background: linear-gradient(135deg, #1677ff 0%, #0958d9 100%);
}

.stat-icon-green {
  background: linear-gradient(135deg, #52c41a 0%, #389e0d 100%);
}

.stat-icon-orange {
  background: linear-gradient(135deg, #faad14 0%, #d48806 100%);
}

.stat-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.stat-value {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 2px;
  color: #333;
}

.stat-label {
  color: #888;
  font-size: 14px;
  font-weight: 500;
}

.home-content-row {
  display: flex;
  gap: 24px;
}

.home-announcement,
.home-quick,
.home-upcoming-exams,
.home-calendar {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.home-announcement:hover,
.home-quick:hover,
.home-upcoming-exams:hover,
.home-calendar:hover {
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.1);
}

.home-announcement,
.home-quick {
  flex: 1;
}

.announcement-item {
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
  list-style-type: none;
}

.announcement-item:last-child {
  border-bottom: none;
}

.home-upcoming-exams {
  margin-top: 8px;
}

.events {
  margin: 0;
  padding: 0;
  list-style: none;
}

.event-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 6px;
}

/* 响应式设计 */
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

  .home-stats {
    flex-wrap: wrap;
  }

  .stat-card {
    min-width: calc(50% - 8px);
  }
}

@media (max-width: 768px) {
  .home-stats {
    flex-direction: column;
  }

  .stat-card {
    min-width: 100%;
  }

  .home-title {
    font-size: 2rem;
  }
}
.events {
  list-style: none;
  margin: 0;
  padding: 0;
}
.events .ant-badge-status {
  overflow: hidden;
  white-space: nowrap;
  width: 100%;
  text-overflow: ellipsis;
  font-size: 12px;
}
.notes-month {
  text-align: center;
  font-size: 28px;
}
.notes-month section {
  font-size: 28px;
}
</style>
