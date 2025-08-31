import http from "../../utils/axios";

export default {
    /**
     * 考试信息统计
     * @param {*} data
     * @returns
     */
    userExamDataStatic() {
        return http({
            url: `/index/v1/exam_data_static`,
            method: "get",
        });
    },
    /**
     * 即将开始考试的列表
     * @returns {*}
     */
    wellStartExamList() {
        return http({
            url: `/index/v1/well_start_exam_list`,
            method: 'get'
        })
    },
};
