<template>
  <a-layout class="main-layout">
    <a-layout-sider
      collapsible
      :collapsed="collapsed"
      @collapse="collapsed = $event"
      width="200"
    >
      <div class="sider-logo">
        <img src="../assets/logo.jpg" alt="logo" />
        <span v-if="!collapsed">企业在线考试</span>
      </div>
      <a-menu theme="dark" mode="inline" :selectedKeys="[selectedKey]">
        <a-menu-item key="home" @click="goPage('/home')">
          <template #icon><icon-home /></template>
          首页
        </a-menu-item>
        <a-menu-item key="exam" @click="goPage('/exam')">
          <template #icon><icon-file-text /></template>
          我的考试
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <AppHeader />
      <a-layout-content class="main-content">
        <a-config-provider :locale="locale">
          <RouterView />
        </a-config-provider>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup>
import { ref, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import AppHeader from "./AppHeader.vue";
import {
  HomeOutlined as IconHome,
  FileTextOutlined as IconFileText,
} from "@ant-design/icons-vue";
import zhCN from "ant-design-vue/es/locale/zh_CN";
let locale = ref(zhCN);
const collapsed = ref(false);
const router = useRouter();
const route = useRoute();
const selectedKey = computed(() => route.path.split("/")[1] || "home");
const goPage = (path) => {
  router.push(path);
};
</script>

<style scoped>
.main-layout {
  min-height: 100vh;
}
.sider-logo {
  height: 48px;
  display: flex;
  align-items: center;
  padding: 16px;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
}
.sider-logo img {
  width: 32px;
  margin-right: 10px;
}
.main-content {
  margin: 24px 16px;
  background: #fff;
  min-height: 360px;
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.03);
  padding: 24px;
}
</style>
