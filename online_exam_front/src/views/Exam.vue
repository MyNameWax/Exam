<template>
  <a-card title="考试列表" class="exam-list-card">
    <a-table :columns="columns" :data-source="data" row-key="id" bordered>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button type="link" @click="viewDetail(record)">详情</a-button>
            <a-button type="link" @click="editExam(record)">编辑</a-button>
          </a-space>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="detailVisible" title="考试详情" :footer="null">
      <div v-if="currentExam">
        <p><b>考试名称：</b>{{ currentExam.name }}</p>
        <p><b>考试时间：</b>{{ currentExam.time }}</p>
        <p><b>考试状态：</b>{{ currentExam.status }}</p>
        <p><b>考试说明：</b>{{ currentExam.desc }}</p>
      </div>
    </a-modal>
    <a-modal v-model:open="editVisible" title="编辑考试" @ok="handleEditOk">
      <a-form :model="editForm">
        <a-form-item label="考试名称">
          <a-input v-model:value="editForm.name" />
        </a-form-item>
        <a-form-item label="考试说明">
          <a-input v-model:value="editForm.desc" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>

<script setup>
import { ref } from 'vue';
const columns = [
  { title: '考试名称', dataIndex: 'name', key: 'name' },
  { title: '考试时间', dataIndex: 'time', key: 'time' },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '操作', key: 'action' },
];
const data = ref([
  { id: 1, name: '2025年晋升考试', time: '2025-08-01 09:00', status: '未开始', desc: '年度晋升考试' },
  { id: 2, name: '安全知识测评', time: '2025-07-20 14:00', status: '已结束', desc: '企业安全知识考核' },
  { id: 3, name: '新员工入职考试', time: '2025-07-30 10:00', status: '进行中', desc: '新员工基础能力测试' },
]);
const detailVisible = ref(false);
const editVisible = ref(false);
const currentExam = ref(null);
const editForm = ref({ name: '', desc: '' });

function viewDetail(record) {
  currentExam.value = record;
  detailVisible.value = true;
}
function editExam(record) {
  editForm.value = { name: record.name, desc: record.desc };
  currentExam.value = record;
  editVisible.value = true;
}
function handleEditOk() {
  if (currentExam.value) {
    currentExam.value.name = editForm.value.name;
    currentExam.value.desc = editForm.value.desc;
  }
  editVisible.value = false;
}
</script>

<style scoped>
.exam-list-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.03);
  margin: 24px;
}
</style>
