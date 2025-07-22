<template>
  <div class="user-exam-container">
    <!-- 登录模态框 -->
    <a-modal
        v-model:visible="loginModalVisible"
        title="用户登录"
        :closable="false"
        :maskClosable="false"
        :footer="null"
        centered
        width="400px"
    >
      <a-form
          :model="loginForm"
          :rules="loginRules"
          ref="loginFormRef"
          @finish="handleLogin"
          layout="vertical"
      >
        <a-form-item label="考生号" name="examineeNumber">
          <a-input
              v-model:value="loginForm.examineeNumber"
              placeholder="请输入考生号"
              size="large"
          >
            <template #prefix>
              <UserOutlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item label="密码" name="checkCode">
          <a-input-password
              v-model:value="loginForm.checkCode"
              placeholder="请输入密码"
              size="large"
          >
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item>
          <a-button
              type="primary"
              html-type="submit"
              block
              size="large"
              :loading="loginLoading"
          >
            登录
          </a-button>
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 顶部导航和标题 -->
    <a-page-header title="我的考试" class="page-header" :show-back="false">
      <template #extra>
        <a-button type="text" @click="refreshList">
          <template #icon><SyncOutlined /></template>
          刷新
        </a-button>
      </template>
    </a-page-header>

    <!-- 考试分类筛选 -->
    <div class="exam-filter">
      <a-radio-group
          v-model:value="filterStatus"
          button-style="solid"
          @change="handleFilterChange"
      >
        <a-radio-button value="all">全部考试</a-radio-button>
        <a-radio-button value="available">可参加</a-radio-button>
        <a-radio-button value="completed">已完成</a-radio-button>
        <a-radio-button value="expired">已过期</a-radio-button>
      </a-radio-group>
    </div>

    <!-- 考试列表 -->
    <div class="exam-list">
      <a-list
          :data-source="filteredExams"
          :loading="loading"
          item-layout="vertical"
      >
        <template #renderItem="{ item }">
          <a-list-item class="exam-card">
            <template #actions>
              <span class="exam-time">
                <clock-circle-outlined /> {{ item.examTime }}
              </span>
              <span class="exam-duration">
                <field-time-outlined /> 时长: {{ item.duration }}分钟
              </span>
              <span class="exam-score">
                <star-outlined /> 总分: {{ item.totalScore }}分
              </span>
            </template>
            <template #extra>
              <a-button
                  type="primary"
                  size="large"
                  :disabled="item.status !== 1"
                  @click="handleStartExam(item)"
              >
                {{ getActionText(item.status) }}
              </a-button>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <div class="exam-title">
                  <span>{{ item.title }}</span>
                  <a-tag v-if="item.isNew" color="red" style="margin-left: 8px"
                  >新</a-tag
                  >
                </div>
              </template>
              <template #avatar>
                <div
                    class="exam-avatar"
                    :style="{ backgroundColor: getExamColor(item.type) }"
                >
                  {{ getExamTypeIcon(item.type) }}
                </div>
              </template>
            </a-list-item-meta>
            <div class="exam-progress" v-if="item.progress > 0">
              <a-progress
                  :percent="item.progress"
                  :stroke-color="getProgressColor(item.progress)"
                  :show-info="false"
              />
              <span class="progress-text">已完成 {{ item.progress }}%</span>
            </div>
            <div class="exam-status">
              <a-badge
                  :status="getStatusType(item.status)"
                  :text="getStatusText(item.status)"
              />
            </div>
          </a-list-item>
        </template>
      </a-list>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted,getCurrentInstance } from "vue";
import {
  SyncOutlined,
  ClockCircleOutlined,
  FieldTimeOutlined,
  StarOutlined,
  UserOutlined,
  LockOutlined,
} from "@ant-design/icons-vue";
import { message } from "ant-design-vue";
import router from "../router/index.js";
const { proxy } = getCurrentInstance()
const API = proxy.$API
// 登录模态框显示状态
const loginModalVisible = ref(false);
// 登录加载状态
const loginLoading = ref(false);
// 登录表单引用
const loginFormRef = ref();

// 登录表单数据
const loginForm = reactive({
  examineeNumber: "",
  checkCode: "",
});

// 登录表单验证规则
const loginRules = {
  examineeNumber: [
    { required: true, message: "请输入考生号", trigger: "blur" },
    { min: 16, max: 16, message: "考生号格式错误", trigger: "blur" },
  ],
  checkCode: [
    { required: true, message: "请输入校验码", trigger: "blur" },
  ],
};

// 检查用户是否已登录
const checkLoginStatus = () => {
  const token = localStorage.getItem("token");
  if (!token) {
    loginModalVisible.value = true;
    return false;
  }
  return true;
};

// 处理登录
const handleLogin = async () => {
  try {
    loginLoading.value = true;
    await loginFormRef.value.validate();
    API.examUser.examLogin(loginFormRef.value)
    // 模拟登录成功
    if (loginForm.username === "admin" && loginForm.password === "123456") {
      localStorage.setItem("token", "mock-token-" + Date.now());
      message.success("登录成功");
      loginModalVisible.value = false;
      fetchExamList(); // 重新加载考试列表
    } else {
      message.error("用户名或密码错误");
    }
  } catch (error) {
    console.error("登录失败:", error);
    if (error?.errorFields) {
      message.error("请正确填写登录信息");
    } else {
      message.error("登录失败，请稍后重试");
    }
  } finally {
    loginLoading.value = false;
  }
};

// 筛选状态
const filterStatus = ref("all");
// 加载状态
const loading = ref(false);
// 考试列表数据
const examList = ref([]);

// 考试类型图标
const examTypeIcons = {
  normal: "📝",
  quiz: "✏️",
  cert: "🏆",
  final: "🎯",
};

// 考试类型颜色
const examTypeColors = {
  normal: "#1890ff",
  quiz: "#722ed1",
  cert: "#fa8c16",
  final: "#f5222d",
};

// 状态文本映射
const statusMap = {
  0: "未开始",
  1: "可参加",
  2: "已完成",
  3: "已过期",
};

// 状态颜色映射
const statusTypeMap = {
  0: "default",
  1: "processing",
  2: "success",
  3: "error",
};

// 获取考试类型图标
const getExamTypeIcon = (type) => {
  return examTypeIcons[type] || "📋";
};

// 获取考试颜色
const getExamColor = (type) => {
  return examTypeColors[type] || "#52c41a";
};

// 获取状态文本
const getStatusText = (status) => {
  return statusMap[status] || "未知状态";
};

// 获取状态类型
const getStatusType = (status) => {
  return statusTypeMap[status] || "default";
};

// 获取进度条颜色
const getProgressColor = (percent) => {
  if (percent < 30) return "#ff4d4f";
  if (percent < 70) return "#faad14";
  return "#52c41a";
};

// 获取操作按钮文本
const getActionText = (status) => {
  const texts = {
    0: "未开始",
    1: "开始考试",
    2: "查看结果",
    3: "已过期",
  };
  return texts[status] || "--";
};

// 筛选后的考试列表
const filteredExams = computed(() => {
  if (filterStatus.value === "all") return examList.value;
  const statusMap = {
    available: 1,
    completed: 2,
    expired: 3,
  };
  const targetStatus = statusMap[filterStatus.value];
  return examList.value.filter((item) => item.status === targetStatus);
});

// 模拟获取考试列表数据
const fetchExamList = async () => {
  // 检查登录状态
  if (!checkLoginStatus()) {
    return;
  }

  loading.value = true;
  try {
    // 这里应该是API调用，我们模拟数据
    const mockData = generateMockData();
    examList.value = mockData.list;
  } catch (error) {
    message.error("获取考试列表失败");
    console.error(error);
  } finally {
    loading.value = false;
  }
};

// 生成模拟数据
const generateMockData = () => {
  const examTypes = ["normal", "quiz", "cert", "final"];
  const allData = Array.from({ length: 15 }, (_, i) => ({
    id: i + 1,
    title: `${
        [
          "季度考核",
          "25-26年JavaWeb期末考试试题",
          "专业技能",
          "入职考试",
          "晋升考核",
        ][i % 5]
    } - ${["基础测试", "进阶评估", "综合考核"][i % 3]}`,
    description: "请认真阅读考试说明，在规定时间内完成所有题目",
    examTime: `2025-${(i % 12) + 1}-${(i % 28) + 1} ${i % 24}:00`,
    duration: [30, 45, 60, 90][i % 4],
    totalScore: [100, 120, 150][i % 3],
    status: i % 4,
    type: examTypes[i % examTypes.length],
    isNew: i < 3,
    progress:
        [0, 20, 50, 80, 100][i % 5] * (i % 3 === 0 ? 1 : i % 3 === 1 ? 0.8 : 0.6),
  }));

  return {
    list: allData,
    total: allData.length,
  };
};

// 处理筛选变化
const handleFilterChange = () => {
  pagination.current = 1;
};

// 刷新列表
const refreshList = () => {
  fetchExamList();
};

// 开始考试
const handleStartExam = (exam) => {
  if (!checkLoginStatus()) {
    return;
  }

  if (exam.status === 1) {
    message.info(`准备开始考试: ${exam.title}`);
    // 这里应该是跳转到考试页面的逻辑
    router.push({
      path: "/exam",
    });
  } else if (exam.status === 2) {
    message.info(`查看考试结果: ${exam.title}`);
  }
};

// 组件挂载时获取数据
onMounted(() => {
  fetchExamList();
});
</script>

<style scoped>
.user-exam-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 16px;
}

.page-header {
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.exam-filter {
  margin: 24px 0;
  padding: 0 8px;
}

.exam-list {
  margin-top: 16px;
}

.exam-card {
  margin-bottom: 16px;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.03),
  0 1px 6px -1px rgba(0, 0, 0, 0.02), 0 2px 4px 0 rgba(0, 0, 0, 0.02);
  transition: all 0.3s;
}

.exam-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.exam-avatar {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: white;
}

.exam-title {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 500;
}

.exam-time,
.exam-duration,
.exam-score {
  display: inline-flex;
  align-items: center;
  margin-right: 16px;
  color: rgba(0, 0, 0, 0.45);
}

.exam-time :deep(svg),
.exam-duration :deep(svg),
.exam-score :deep(svg) {
  margin-right: 4px;
}

.exam-progress {
  margin-top: 12px;
}

.progress-text {
  display: block;
  margin-top: 4px;
  font-size: 12px;
  color: rgba(0, 0, 0, 0.45);
}

.exam-status {
  margin-top: 12px;
}

:deep(.ant-list-pagination) {
  margin-top: 24px;
  text-align: center;
}

:deep(.ant-list-item-meta-description) {
  color: rgba(0, 0, 0, 0.65);
}

:deep(.ant-modal-body) {
  padding: 24px;
}

:deep(.ant-form-item-label) {
  font-weight: 500;
}
</style>
