<template>
  <div class="result-page-container">
    <!-- 顶部结果概览 -->
    <a-page-header
        title="考试结果"
        class="result-header"
        @back="handleBack"
    >
      <template #extra>
        <div class="result-summary">
          <a-statistic
              title="总分"
              :value="examResult.totalScore"
              :precision="2"
              class="summary-item"
          />
          <a-statistic
              title="得分"
              :value="examResult.userScore"
              :precision="2"
              class="summary-item"
          />
          <a-statistic
              title="正确率"
              :value="examResult.correctRate"
              suffix="%"
              class="summary-item"
          />
          <a-statistic
              title="用时"
              :value="examResult.timeUsed"
              class="summary-item"
          />
        </div>
      </template>
    </a-page-header>

    <!-- 结果详情 -->
    <div class="result-detail">
      <!-- 题目结果列表 -->
      <a-list
          :data-source="examResult.questions"
          :grid="{ gutter: 16, column: 1 }"
          item-layout="vertical"
      >
        <template #renderItem="{ item, index }">
          <a-list-item class="question-result">
            <div class="question-header">
              <h3 class="question-title">
                第{{ index + 1 }}题 ({{ item.score }}分)
                <a-tag :color="getQuestionTagColor(item.type)">
                  {{ getQuestionTypeText(item.type) }}
                </a-tag>
              </h3>
              <div class="question-status">
                <a-tag :color="item.isCorrect ? 'green' : 'red'">
                  {{ item.isCorrect ? '正确' : '错误' }}
                </a-tag>
                <span>得分: {{ item.userScore }} / {{ item.score }}</span>
              </div>
            </div>

            <div class="question-content" v-html="item.title"></div>

            <!-- 题目解析 -->
            <div class="question-analysis">
              <div v-if="item.type === '1' || item.type === '2' || item.type === '3'" class="options-result">
                <div v-for="opt in item.options" :key="opt.value" class="option-item">
                  <span class="option-label">{{ opt.value }}.</span>
                  <span class="option-text">{{ opt.label }}</span>
                  <span v-if="opt.isCorrect" class="correct-mark">✓</span>
                  <span v-if="opt.isUserAnswer" class="user-mark">⦿</span>
                </div>
              </div>

              <div v-if="item.type === '5'" class="fill-result">
                <div v-for="(answer, idx) in item.correctAnswer" :key="idx" class="fill-item">
                  <p>填空 {{ idx + 1 }}:</p>
                  <p>你的答案: <span :class="{'correct-answer': answer === item.userAnswer[idx]}">{{ item.userAnswer[idx] || '未作答' }}</span></p>
                  <p>正确答案: <span class="correct-answer">{{ answer }}</span></p>
                </div>
              </div>

              <div v-if="item.type === '4'" class="essay-result">
                <p>你的答案:</p>
                <div class="user-answer" v-html="item.userAnswer || '未作答'"></div>
                <p>参考答案:</p>
                <div class="correct-answer" v-html="item.correctAnswer"></div>
              </div>

              <div v-if="item.analysis" class="analysis-text">
                <h4>题目解析:</h4>
                <p v-html="item.analysis"></p>
              </div>
            </div>
          </a-list-item>
        </template>
      </a-list>
    </div>

    <!-- 底部操作按钮 -->
    <div class="result-actions">
      <a-button type="primary" @click="handleReview">查看错题</a-button>
      <a-button @click="handleBack">返回考试列表</a-button>
      <a-button v-if="examResult.canRetry" type="dashed" @click="handleRetry">重新考试</a-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();

// 考试结果数据
const examResult = reactive({
  examId: '',
  examName: 'Java Web知识考核',
  totalScore: 14,
  userScore: 10.5,
  correctRate: 75,
  timeUsed: '32分15秒',
  questions: [],
  canRetry: true
});

// 获取题目类型文本
const getQuestionTypeText = (type) => {
  const typeMap = {
    '1': '单选',
    '2': '多选',
    '3': '判断',
    '4': '简答',
    '5': '填空'
  };
  return typeMap[type] || '未知';
};

// 获取题目标签颜色
const getQuestionTagColor = (type) => {
  const colorMap = {
    '1': 'blue',
    '2': 'purple',
    '3': 'orange',
    '4': 'red',
    '5': 'green'
  };
  return colorMap[type] || 'gray';
};

// 模拟API获取考试结果
const fetchExamResult = async () => {
  try {
    // 这里应该是API调用，我们模拟数据
    const response = await axios.get('/api/exam/result?id=123');
    // examResult = response.data;

    // 模拟数据
    examResult.questions = [
      {
        id: '1938534218825560065',
        type: '1',
        title: '在Servlet生命周期中，init()方法被调用的次数是',
        score: 2,
        userScore: 2,
        isCorrect: true,
        userAnswer: ['A'],
        correctAnswer: ['A'],
        options: [
          { label: '1次', value: 'A', isCorrect: true, isUserAnswer: true },
          { label: '2次', value: 'B', isCorrect: false, isUserAnswer: false },
          { label: '多次', value: 'C', isCorrect: false, isUserAnswer: false }
        ],
        analysis: 'Servlet的init()方法只在Servlet初始化时调用一次，用于一次性初始化工作。'
      },
      {
        id: '1938534274962124801',
        type: '2',
        title: '以下哪些是Servlet接口中的方法？（多选）',
        score: 3,
        userScore: 1.5,
        isCorrect: false,
        userAnswer: ['A', 'C'],
        correctAnswer: ['A', 'B', 'D'],
        options: [
          { label: 'service()', value: 'A', isCorrect: true, isUserAnswer: true },
          { label: 'init()', value: 'B', isCorrect: true, isUserAnswer: false },
          { label: 'destroy()', value: 'C', isCorrect: false, isUserAnswer: true },
          { label: 'getServletConfig()', value: 'D', isCorrect: true, isUserAnswer: false }
        ],
        analysis: 'Servlet接口包含的方法有：init(), service(), destroy(), getServletConfig(), getServletInfo()。'
      },
      {
        id: '1938534405568557058',
        type: '3',
        title: 'Filter的执行顺序与web.xml中的配置顺序无关',
        score: 2,
        userScore: 0,
        isCorrect: false,
        userAnswer: false,
        correctAnswer: true,
        options: [
          { label: '正确', value: true, isCorrect: true, isUserAnswer: false },
          { label: '错误', value: false, isCorrect: false, isUserAnswer: true }
        ],
        analysis: 'Filter的执行顺序完全由web.xml中的配置顺序决定，先配置的Filter会先执行。'
      },
      {
        id: '1938534497297985538',
        type: '5',
        title: '在Servlet中，获取Servlet配置信息的接口是__________,并且王新杰是____',
        score: 2,
        userScore: 2,
        isCorrect: true,
        userAnswer: ['ServletConfig', '帅气的'],
        correctAnswer: ['ServletConfig', '帅气的'],
        analysis: 'ServletConfig接口用于获取Servlet的配置信息，包括初始化参数等。'
      },
      {
        id: '1938534560715862017',
        type: '4',
        title: '简述Servlet的生命周期',
        score: 5,
        userScore: 5,
        isCorrect: true,
        userAnswer: 'Servlet生命周期包括：1. 初始化阶段(init())；2. 服务阶段(service())；3. 销毁阶段(destroy())',
        correctAnswer: 'Servlet生命周期包括：1. 初始化阶段(init())；2. 服务阶段(service())；3. 销毁阶段(destroy())',
        analysis: 'Servlet生命周期由容器管理，包括初始化、服务和销毁三个阶段。'
      }
    ];
  } catch (error) {
    console.error('获取考试结果失败:', error);
  }
};

// 返回考试列表
const handleBack = () => {
  router.push('/');
};

// 查看错题
const handleReview = () => {
  const wrongQuestions = examResult.questions.filter(q => !q.isCorrect);
  console.log('错题:', wrongQuestions);
  // 这里可以实现跳转到错题页面或筛选显示错题
};

// 重新考试
const handleRetry = () => {
  // 这里可以实现重新考试的逻辑
  console.log('重新考试');
};

// 组件挂载时获取数据
onMounted(() => {
  fetchExamResult();
});
</script>

<style scoped>
.result-page-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 16px;
}

.result-header {
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.03);
}

.result-summary {
  display: flex;
  gap: 24px;
}

.summary-item {
  text-align: center;
  min-width: 100px;
}

.result-detail {
  background-color: #fff;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.03);
}

.question-result {
  padding: 16px;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  margin-bottom: 16px;
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.question-title {
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.question-status {
  display: flex;
  align-items: center;
  gap: 8px;
}

.question-content {
  margin-bottom: 16px;
  font-size: 15px;
  line-height: 1.6;
}

.question-analysis {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px dashed #f0f0f0;
}

.options-result {
  margin: 12px 0;
}

.option-item {
  padding: 8px;
  margin: 4px 0;
  border-radius: 4px;
  position: relative;
}

.option-item:hover {
  background-color: #fafafa;
}

.option-label {
  font-weight: bold;
  margin-right: 8px;
}

.correct-mark {
  color: #52c41a;
  margin-left: 8px;
  font-weight: bold;
}

.user-mark {
  color: #1890ff;
  margin-left: 8px;
}

.fill-item {
  margin: 8px 0;
  padding: 8px;
  background-color: #fafafa;
  border-radius: 4px;
}

.essay-result {
  margin: 12px 0;
}

.user-answer, .correct-answer {
  padding: 8px;
  margin: 8px 0;
  background-color: #fafafa;
  border-radius: 4px;
}

.correct-answer {
  color: #52c41a;
}

.analysis-text {
  margin-top: 16px;
  padding: 8px;
  background-color: #f6f6f6;
  border-radius: 4px;
}

.analysis-text h4 {
  margin: 8px 0;
}

.result-actions {
  margin: 24px 0;
  text-align: center;
}

.result-actions button {
  margin: 0 8px;
}
</style>