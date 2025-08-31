<script setup lang="ts">
import {computed, getCurrentInstance, onMounted, ref} from 'vue';
import dayjs, {Dayjs} from 'dayjs';

const {proxy} = getCurrentInstance();
const API = proxy.$API;
const value = ref<Dayjs>();
const calendarEvents = ref({});
onMounted(async () => {
  await getExamCalenderAPI(dayjs(new Date()).format('YYYY-MM'))
})

const getExamCalenderAPI = async (date) => {
  const res = await API.exam.examCalender(date)
  calendarEvents.value = res.data.data

}

// 考试日期数据
const onPanelChange = async (value, mode) => {
  await getExamCalenderAPI(dayjs(value).format('YYYY-MM'))

};

// 获取特定日期的事件
const getDayEvents = computed(() => {
  return (value) => {
    return calendarEvents.value[dayjs(value).format('YYYY-MM-DD')] || [];
  };
})

</script>

<template>
  <div>
    <a-calendar v-model:value="value" @panelChange="onPanelChange">
      <template #dateCellRender="{ current }">
        <ul class="events">
          <li v-for="item in getDayEvents(current)" :key="item.id">
            <a-badge :status="item.type" :text="item.title"/>
          </li>
        </ul>
      </template>

    </a-calendar>
  </div>
</template>

<style scoped>
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