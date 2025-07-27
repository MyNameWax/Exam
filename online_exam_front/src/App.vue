<template>
  <a-config-provider :locale="locale">
    <RouterView />
  </a-config-provider>
</template>

<script setup>
import { ref } from "vue";
import zhCN from "ant-design-vue/es/locale/zh_CN";
let locale = ref(zhCN);
</script>

<style scoped>
body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
    "Helvetica Neue", Arial, "Noto Sans", sans-serif;
  background: #f0f2f5;
}
</style>
