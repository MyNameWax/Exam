import http from "@/api/axios.js";

export default {

   examList(params) {
        return http({
            url: `/exam/v1/list`,
            method: "get",
            params
        })
    }


}