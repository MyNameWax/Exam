<script setup>
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import {ref} from "vue";
let locale = ref(zhCN);
</script>

<template>
  <a-config-provider :locale="locale">
    <RouterView />
  </a-config-provider>

</template>

<style scoped></style>
