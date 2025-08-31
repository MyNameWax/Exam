package cn.rzpt.service;

import cn.rzpt.model.response.IndexExamCalendarResponse;
import cn.rzpt.model.response.IndexExamDataStaticResponse;
import cn.rzpt.model.response.IndexWellStartExamList;

import java.util.List;
import java.util.Map;

public interface IndexService {
    /**
     * 考试信息统计
     *
     * @return 考试信息统计
     */
    IndexExamDataStaticResponse examDataStatic();

    /**
     * 即将开始考试的列表
     *
     * @return
     */
    List<IndexWellStartExamList> wellStartExamList();

    /**
     * 考试日历列表
     *
     * @param date 日期
     * @return 日期数据
     */
    Map<String, List<IndexExamCalendarResponse>> examCalendarListData(String date);

}
