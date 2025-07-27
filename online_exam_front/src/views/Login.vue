<template>
  <div class="login-bg-pro">
    <div class="login-container-pro">
      <div class="login-header-pro">
        <img class="login-logo-pro" src="../assets/logo.jpg" alt="logo" />
        <div class="login-title-group">
          <div class="login-title-pro">企业在线考试系统</div>
          <div class="login-desc-pro">企业级安全 · 高效 · 智能</div>
        </div>
      </div>
      <a-card class="login-card-pro" :bordered="false">
        <a-form @submit.prevent="handleLogin" layout="vertical">
          <a-form-item label="用户名">
            <a-input
              v-model:value="loginForm.username"
              size="large"
              placeholder="请输入用户名"
              allow-clear
            />
          </a-form-item>
          <a-form-item label="密码">
            <a-input-password
              v-model:value="loginForm.password"
              size="large"
              placeholder="请输入密码"
              allow-clear
            />
          </a-form-item>
          <a-form-item>
            <a-button type="primary" html-type="submit" size="large" block
              >登录</a-button
            >
          </a-form-item>
        </a-form>
      </a-card>
      <div class="login-footer-pro">
        © {{ new Date().getFullYear() }} 企业在线考试系统
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance } from "vue";
import { useRouter } from "vue-router";
import { message } from "ant-design-vue";
const { proxy } = getCurrentInstance();
const API = proxy.$API;

const router = useRouter();
const loginForm = ref({
  username: "",
  password: "",
});

const handleLogin = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    message.error("请输入用户名和密码");
    return;
  }
  const res = await API.user.userLogin(loginForm.value);
  localStorage.setItem("username", res.data.data.realname);
  localStorage.setItem("token", res.data.data.token);
  message.success("登录成功");
  router.push("/home");
};
</script>

<style scoped>
.login-bg-pro {
  min-height: 100vh;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
}
.login-container-pro {
  width: 100%;
  max-width: 420px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.login-header-pro {
  display: flex;
  align-items: center;
  margin-bottom: 32px;
  width: 100%;
}
.login-logo-pro {
  width: 48px;
  height: 48px;
  margin-right: 16px;
}
.login-title-group {
  display: flex;
  flex-direction: column;
}
.login-title-pro {
  font-size: 1.8rem;
  font-weight: 700;
  color: #222;
  letter-spacing: 1px;
}
.login-desc-pro {
  font-size: 14px;
  color: #888;
  margin-top: 2px;
}
.login-card-pro {
  width: 100%;
  box-shadow: 0 4px 24px 0 rgba(0, 0, 0, 0.08);
  border-radius: 12px;
  padding: 32px 24px 8px 24px;
  background: #fff;
}
.login-footer-pro {
  margin-top: 32px;
  color: #b0b0b0;
  font-size: 13px;
  text-align: center;
}
@media (max-width: 600px) {
  .login-container-pro {
    max-width: 95vw;
  }
  .login-card-pro {
    padding: 20px 8px 8px 8px;
  }
  .login-header-pro {
    flex-direction: column;
    align-items: flex-start;
  }
  .login-logo-pro {
    margin-bottom: 8px;
  }
}
</style>
