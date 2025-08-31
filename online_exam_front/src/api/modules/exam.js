import http from "../../utils/axios";

export default {

    /**
     * 考试日历
     * @param data
     * @returns {Promise<axios.AxiosResponse<any>> | *}
     */
    examCalender(params) {
        return http({
            url: `/index/v1/exam_calendar_list_data?date=${params}`,
            method: "get",
        });
    },
};
