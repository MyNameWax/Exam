import {createApp} from "vue";
import Antd from "ant-design-vue";
import router from "./router";
import App from "./App.vue";
import API from '@/api/index'
const app = createApp(App)

app.config.globalProperties.$API = API
app.config.warnHandler = () => null
app.use(Antd)
app.use(router)
app.mount("#app");
