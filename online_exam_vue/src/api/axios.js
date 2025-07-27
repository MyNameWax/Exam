import axios from 'axios'
import router from "../router/index.js";
import { message } from 'ant-design-vue';
const [messageApi, contextHolder] = message.useMessage();
let apiBaseUrl = "http://localhost:8088"

const http = axios.create({
    baseURL: apiBaseUrl,
    timeout: 9000
})




// 请求拦截 -->在请求发送之前做一些事情
http.interceptors.request.use(
    (config) => {
        config.headers.Authorization = localStorage.getItem("token") || ""
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)



// 响应拦截 -->在返回结果之前做一些事情
http.interceptors.response.use(
    (res) => {
        if (res.data.code === 401) {
            localStorage.removeItem('token')
            router.push('/')
            messageApi.warning('登录已过期，请重新登录')
        }
        if (res.data.code !== 200) {
            messageApi.error(res.data.message)
        }
        if (res.status === 0 || res.status === 200) {
            return Promise.resolve(res)
        }
        return Promise.reject(res)
    },
    (err) => {
        console.log("err", err.response)
        if (err.response.status === 401) {
            localStorage.removeItem('token')
            router.push('/')
        }
        return Promise.reject(err)
    }
)
export default http
