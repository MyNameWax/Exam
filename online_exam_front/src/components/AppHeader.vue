<template>
  <a-layout-header class="header-pro">
    <div class="header-left">
      <a-breadcrumb>
        <a-breadcrumb-item v-for="(item, idx) in breadcrumbList" :key="idx">
          {{ item }}
        </a-breadcrumb-item>
      </a-breadcrumb>
    </div>
    <div class="header-right">
      <a-dropdown placement="bottomRight">
        <a-avatar :size="36" :src="userAvatar" style="cursor: pointer" />
        <template #overlay>
          <a-menu>
            <a-menu-item @click="logout">退出登录</a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
    </div>
  </a-layout-header>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
const username = ref("");
const userAvatar = ref("");
const route = useRoute();
const router = useRouter();

onMounted(() => {
  username.value = localStorage.getItem("username") || "用户";
  userAvatar.value = "http://localhost:5173/src/assets/logo.jpg";
});

const logout = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("username");
  router.push("/login");
};

const breadcrumbList = computed(() => {
  // 简单面包屑，实际可根据路由meta或配置优化
  const pathArr = route.path.split("/").filter(Boolean);
  // if (pathArr.length === 0) return ["首页"];
  // if (pathArr[0] === "home") return ["首页"];
  return [
    "首页",
    ...pathArr.slice(1).map((i) => (i === "exam" ? "考试列表" : i)),
  ];
});
</script>

<style scoped>
.header-pro {
  background: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  height: 64px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.03);
}
.header-title {
  font-size: 20px;
  font-weight: 600;
  color: #1d39c4;
}
.header-right {
  display: flex;
  align-items: center;
}
.header-username {
  margin-left: 12px;
  font-size: 16px;
  color: #222;
}
</style>
