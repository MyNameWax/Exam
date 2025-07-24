import http from "@/api/axios.js";

export default {

    examList(params) {
        return http({
            url: `/exam/v1/list`,
            method: "get",
            params
        })
    },
    startExam(id) {
        return http({
            url: `/examinee-exam/v1/${id}/start`,
            method: "post"
        })
    },
    examDetailList(id) {
        return http({
            url: `/examinee-exam/v1/exam/${id}`,
            method: "post"
        })
    },
    submitExamInfo(data) {
        return http({
            url: "/examinee-exam/v1/submit-answer",
            method: "post",
            data
        })
    }

}