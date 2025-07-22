import http from "../axios.js";

export default {
    // 分页查询卡券列表
    examLogin(data) {
        return http({
            url: '/exam/user/v1/login',
            method: 'post',
            data
        })
    },
}
