import http from "../axios.js";

export default {
    examLogin(data) {
        return http({
            url: '/exam/user/v1/login',
            method: 'post',
            data
        })
    },
}
