<template>
  <div class="exam-page-container">
    <!-- 考试头部信息 -->
    <a-page-header
        :title="examInfo.title"
        class="exam-header"
        @back="handleBack"
    >
      <template #extra>
        <div class="exam-timer">
          <a-statistic-countdown
              :value="countdownTime"
              format="HH:mm:ss"
              :value-style="{ color: countdownColor }"
              @finish="handleTimeUp"
          />
          <span class="timer-label">剩余时间</span>
        </div>
      </template>
    </a-page-header>

    <!-- 考试主体 -->
    <div class="exam-body">
      <!-- 左侧题目区域 -->
      <div class="question-area">
        <!-- 当前题目 -->
        <div class="current-question" v-if="currentQuestion">
          <div class="question-header">
            <h3 class="question-title">
              第{{ currentIndex + 1 }}题 ({{ currentQuestion.score }}分)
            </h3>
            <a-tag :color="getQuestionTagColor(currentQuestion.type)">
              {{ getQuestionTypeText(currentQuestion.type) }}
            </a-tag>
          </div>
          <div class="question-title" v-html="currentQuestion.title"></div>
          <div class="question-content" v-html="currentQuestion.content"></div>
          <!-- 单选题 -->
          <div v-if="currentQuestion.type == '1'" class="question-options">
            <a-radio-group
                v-model:value="currentQuestion.userAnswer[0]"
                :options="currentQuestion.options"
                option-type="button"
                button-style="solid"
            />
          </div>
          <!-- 多选题 -->
          <div v-if="currentQuestion.type === '2'" class="question-options">
            <a-checkbox-group
                v-model:value="currentQuestion.userAnswer"
                :options="currentQuestion.options"
            >
              <template #label="{ label }">
                <div class="option-item">{{ label }}</div>
              </template>
            </a-checkbox-group>
          </div>

          <!-- 判断题 -->
          <div v-if="currentQuestion.type === '3'" class="question-options">
            <a-radio-group
                v-model:value="currentQuestion.userAnswer[0]"
                :options="[
                { label: '正确', value: 'true' },
                { label: '错误', value: 'false' },
              ]"
                option-type="button"
                button-style="solid"
            />
          </div>
          <!-- 填空题 -->
          <div v-if="currentQuestion.type === '4'" class="question-fill">

            <a-input
                v-model:value="currentQuestion.userAnswer[0]"
                :placeholder="'请输入答案'"
                allow-clear
            />
          </div>

          <!-- 简答题 -->
          <div v-if="currentQuestion.type === '5'" class="question-essay">
            <a-textarea
                v-model:value="currentQuestion.userAnswer[0]"
                placeholder="请输入详细回答"
                :rows="6"
                show-count
                :maxlength="2000"
            />
          </div>

          <!-- 题目导航 -->
          <div class="question-navigation">
            <a-button
                type="primary"
                ghost
                :disabled="currentIndex === 0"
                @click="prevQuestion"
            >
              上一题
            </a-button>
            <a-button
                type="primary"
                :disabled="currentIndex === examInfo.questions.length - 1"
                @click="nextQuestion"
            >
              下一题
            </a-button>
            <a-button type="dashed" @click="markQuestion">
              {{ currentQuestion.marked ? "取消标记" : "标记此题" }}
            </a-button>
          </div>
        </div>

        <!-- 加载状态 -->
        <div v-else class="loading-state">
          <a-spin size="large" tip="加载题目中..."/>
        </div>
      </div>

      <!-- 右侧答题卡 -->
      <div class="answer-card" v-if="examInfo.questions.length > 0">
        <div class="card-header">
          <h3>答题卡</h3>
          <div class="card-stats">
            <span
            >已答: {{ answeredCount }}/{{ examInfo.questions.length }}</span
            >
            <span>标记: {{ markedCount }}</span>
          </div>
        </div>

        <div class="question-grid">
          <div
              v-for="(q, index) in examInfo.questions"
              :key="index"
              class="question-cell"
              :class="{
              current: index === currentIndex,
              answered: q.userAnswer && q.userAnswer.length > 0,
              marked: q.marked,
            }"
              @click="goToQuestion(index)"
          >
            {{ index + 1 }}
          </div>
        </div>

        <div class="card-footer">
          <a-button
              type="primary"
              size="large"
              block
              @click="showSubmitConfirm"
          >
            提交试卷
          </a-button>
        </div>
      </div>
    </div>

    <!-- 提交确认对话框 -->
    <a-modal
        v-model:visible="submitModalVisible"
        title="确认提交试卷"
        ok-text="确认提交"
        cancel-text="再检查一下"
        @ok="handleSubmitExam"
        @cancel="submitModalVisible = false"
    >
      <p>您还有 {{ unansweredCount }} 题未作答，确定要提交试卷吗？</p>
      <p>提交后不可修改，请确认已完成所有题目。</p>
    </a-modal>
  </div>
</template>

<script setup>
import {computed, getCurrentInstance, onMounted, reactive, ref, watch} from "vue";
import {useRoute, useRouter} from "vue-router";
import {message, Modal} from "ant-design-vue";
import {useWindowFocusHook} from "../util/useWindowFocusHook.js"

const {isWindowFocus} = useWindowFocusHook()
const {proxy} = getCurrentInstance();
const API = proxy.$API;
const router = useRouter();
const route = useRoute()

// 考试信息
const examInfo = reactive({
  title: "",
  duration: 30, // 分钟
  questions: [],
  startTime: new Date().getTime(),
});
watch(isWindowFocus,(newVal => {
  if (!newVal) {
    console.log("newVal", newVal)
    API.exam.submitExamCheatPing(examId.value).then(res => {
      const checkPingCount = res.data.data
      if (checkPingCount === 1) {
        // 第一次作弊 给用户提示
        message.warn("检测到您作弊一次,给予警告,超过三次则强制收卷")
      }
      if (checkPingCount > 3) {
        // 让他提交交卷。
        message.warn("您已作弊超过三次,系统将为您强制收卷")
        // 延迟1秒
        setTimeout(() => {
          handleSubmitExam()
        },1000)
      }
    })
  }
}))
// 当前题目索引
const currentIndex = ref(0);
// 倒计时时间
const countdownTime = ref(0);
// 倒计时颜色
const countdownColor = ref("#1890ff");
// 提交确认对话框可见性
const submitModalVisible = ref(false);

// 当前题目
const currentQuestion = computed(() => {
  return examInfo.questions[currentIndex.value] || null;
});

// 修改后的formatQuestions函数
const formatQuestions = (apiData) => {
  return apiData.map((item) => {
    const formattedQuestion = {
      id: item.questionId.toString(),
      type: item.type.toString(),
      title: `${item.title}`,
      content: "",
      score: item.score,
      userAnswer: [], // 初始化为空数组
      choiceAnswer: [],
      judgmentAnswer: [],
      fillAnswer: [],
      essayAnswer: [],
      marked: false,
      options: item.options,
      blanks: [],
    };

    // 处理选项数据
    if (item.option && item.option.length > 0) {
      if (item.type === 3) {
        // 判断题
        formattedQuestion.options = [
          {label: "正确", value: "true"},
          {label: "错误", value: "false"},
        ];
      } else if (item.type === 1 || item.type === 2) {
        // 单选或多选
        formattedQuestion.options = item.option.map((opt, i) => {
          const value = String.fromCharCode(65 + i);
          const label = opt;
          return {label, value};
        });
      }
    }

    // 处理填空题
    if (item.type === 5) {
      const blankLabels = item.title.match(/_{3,}/g) || [];
      formattedQuestion.blanks = blankLabels.map((_, i) => ({
        label: `填空 ${i + 1}:`,
        placeholder: item.option?.[i] || "请输入答案",
      }));
      formattedQuestion.userAnswer = Array(blankLabels.length).fill("");
    }

    return formattedQuestion;
  });
};
// 获取题目类型文本
const getQuestionTypeText = (type) => {
  const typeMap = {
    1: "单选",
    2: "多选",
    3: "判断",
    4: "填空",
    5: "简答",
  };
  return typeMap[type] || "未知";
};

// 获取题目标签颜色
const getQuestionTagColor = (type) => {
  const colorMap = {
    1: "blue",
    2: "purple",
    3: "orange",
    5: "green",
    4: "red",
  };
  return colorMap[type] || "gray";
};

// 已答题数
const answeredCount = computed(() => {
  return examInfo.questions.filter(
      (q) => q.userAnswer && q.userAnswer.length > 0
  ).length;
});

// 未答题数
const unansweredCount = computed(() => {
  return examInfo.questions.length - answeredCount.value;
});

// 标记题数
const markedCount = computed(() => {
  return examInfo.questions.filter((q) => q.marked).length;
});

// 初始化考试
const initExam = () => {
  try {
    API.exam.examDetailList(examId.value).then(res => {
      examInfo.title = res.data.data.title;
      examInfo.duration = res.data.data.duration;
      examInfo.questions = formatQuestions(res.data.data.questions);
      countdownTime.value = examInfo.startTime + examInfo.duration * 60 * 1000;
    })

  } catch (error) {
    console.error("获取考试题目失败:", error);
    message.error("获取考试题目失败，请稍后重试");
  }
};

// 上一题
const prevQuestion = () => {
  if (currentIndex.value > 0) {
    currentIndex.value--;
  }
};

// 下一题
const nextQuestion = () => {
  if (currentIndex.value < examInfo.questions.length - 1) {
    currentIndex.value++;
  }
};

// 跳转到指定题目
const goToQuestion = (index) => {
  currentIndex.value = index;
};

// 标记题目
const markQuestion = () => {
  if (currentQuestion.value) {
    currentQuestion.value.marked = !currentQuestion.value.marked;
  }
};

// 显示提交确认对话框
const showSubmitConfirm = () => {
  submitModalVisible.value = true;
};

// 处理提交考试
const handleSubmitExam = () => {
  submitModalVisible.value = false;
  const submitData = {
    id: recordId.value,
    examId: examId.value,
    answers: examInfo.questions.map(item => ({
      questionId: item.id,
      type: parseInt(item.type),
      userAnswer: item.userAnswer,
    }))
  }
  API.exam.submitExamInfo(submitData).then(res => {
    console.log("考试结果响应", res)
    Modal.success({
      title: "提交成功",
      content: "您的试卷已成功提交,系统正在批改",
      okText: "确定",
      onOk: () => {
        router.push("/")
      }
    })
  })
}

// 处理时间到
const handleTimeUp = () => {
  countdownColor.value = "#ff4d4f";
  Modal.warning({
    title: "考试时间已结束",
    content: "考试时间已到，系统将自动提交您的试卷。",
    okText: "确定",
    onOk: () => {
      handleSubmitExam();
    },
  });
};

// 返回考试列表
const handleBack = () => {
  Modal.confirm({
    title: "确认离开考试",
    content: "考试过程中离开可能会影响您的成绩，确定要返回吗？",
    okText: "确定",
    cancelText: "取消",
    onOk: () => {
      router.back();
    },
  });
};

// 监听倒计时变化
const updateCountdownColor = () => {
  const remaining = countdownTime.value - new Date().getTime();
  const minutes = remaining / (60 * 1000);
  if (minutes < 10) {
    countdownColor.value = "#faad14"; // 黄色警告
  }
  if (minutes < 5) {
    countdownColor.value = "#ff4d4f"; // 红色紧急
  }
};

const examId = ref("")
const recordId = ref("")
// 组件挂载时初始化
onMounted(() => {
  examId.value = route.query.examId
  recordId.value = route.query.recordId
  initExam();
  // 每分钟更新一次倒计时颜色
  setInterval(updateCountdownColor, 60 * 1000);
});
</script>

<style scoped>
.loading-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}

.exam-page-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.exam-header {
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
  z-index: 1;
}

.exam-timer {
  display: flex;
  align-items: center;
  gap: 8px;
}

.timer-label {
  color: rgba(0, 0, 0, 0.45);
}

.exam-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.question-area {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  background-color: #fff;
  margin: 16px;
  border-radius: 8px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.03);
}

.answer-card {
  width: 280px;
  padding: 16px;
  background-color: #fff;
  margin: 16px 16px 16px 0;
  border-radius: 8px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.03);
  display: flex;
  flex-direction: column;
}

.current-question {
  max-width: 900px;
  margin: 0 auto;
}

.question-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
  gap: 16px;
}

.question-title {
  margin: 0;
  font-size: 18px;
}

.question-content {
  margin-bottom: 24px;
  font-size: 16px;
  line-height: 1.6;
}

.question-options {
  margin-bottom: 32px;
}

.option-item {
  padding: 8px 0;
}

.question-fill,
.question-essay {
  margin-bottom: 32px;
}

.question-navigation {
  display: flex;
  justify-content: space-between;
  margin-top: 32px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.card-header {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.card-header h3 {
  margin: 0 0 8px 0;
}

.card-stats {
  display: flex;
  justify-content: space-between;
  color: rgba(0, 0, 0, 0.45);
  font-size: 14px;
}

.question-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 12px;
  margin-bottom: 24px;
  flex: 1;
}

.question-cell {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  border: 1px solid #d9d9d9;
  cursor: pointer;
  transition: all 0.3s;
}

.question-cell:hover {
  border-color: #1890ff;
  color: #1890ff;
}

.question-cell.current {
  border-color: #1890ff;
  background-color: #e6f7ff;
  color: #1890ff;
  font-weight: bold;
}

.question-cell.answered {
  border-color: #52c41a;
  background-color: #f6ffed;
  color: #52c41a;
}

.question-cell.marked {
  position: relative;
}

.question-cell.marked::after {
  content: "";
  position: absolute;
  top: 2px;
  right: 2px;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: #faad14;
}

.card-footer {
  margin-top: auto;
}

/* 新增填空题样式 */
.fill-blank-item {
  margin-bottom: 16px;
}

.fill-blank-item p {
  margin-bottom: 8px;
  font-weight: 500;
}
</style>
