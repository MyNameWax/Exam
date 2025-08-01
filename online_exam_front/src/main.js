import { createApp } from "vue";
import App from "./App.vue";
import Antd from "ant-design-vue";
import API from "../src/api/index";
import "ant-design-vue/dist/reset.css";
import router from "./router";

const app = createApp(App);
app.config.globalProperties.$API = API;
app.use(Antd);
app.use(router);
app.mount("#app");
