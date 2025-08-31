package cn.rzpt.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.rzpt.constants.SystemConstants;
import cn.rzpt.model.po.Exam;
import cn.rzpt.model.response.IndexExamCalendarResponse;
import cn.rzpt.model.response.IndexExamDataStaticResponse;
import cn.rzpt.model.response.IndexWellStartExamList;
import cn.rzpt.service.ExamService;
import cn.rzpt.service.IndexService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IndexServiceImpl implements IndexService {

    @Resource
    private ExamService examService;

    @Override
    public IndexExamDataStaticResponse examDataStatic() {
        List<Exam> examList = examService.lambdaQuery().select(
                Exam::getId,
                Exam::getStartTime,
                Exam::getEndTime
        ).list();
        // 统计出所有的进行中的考试
        // 当前时间在 考试开始和考试结束时间之间
        LocalDateTime now = LocalDateTime.now();
        long ingExamCount = examList.stream().filter(exam -> exam.getStartTime().isBefore(now) && exam.getEndTime().isAfter(now)).count();
        // 未开始的考试
        long notStartExamCount = examList.stream().filter(exam -> exam.getStartTime().isAfter(now)).count();
        // 已结束的考试
        long endExamCount = examList.stream().filter(exam -> exam.getEndTime().isBefore(now)).count();
        return IndexExamDataStaticResponse.builder()
                .ingExamCount(ingExamCount)
                .notStartExamCount(notStartExamCount)
                .endExamCount(endExamCount)
                .build();
    }

    @Override
    public List<IndexWellStartExamList> wellStartExamList() {
        LocalDateTime now = LocalDateTime.now();
        List<IndexWellStartExamList> returnList = new ArrayList<>(5);
        // 需要查询出在今天并且是没有超过当前时间的考试
        List<Exam> list = examService.lambdaQuery().select(
                        Exam::getId,
                        Exam::getTitle,
                        Exam::getStartTime,
                        Exam::getDuration,
                        Exam::getTotalScore,
                        Exam::getPassScore)
                .ge(Exam::getStartTime, now)
                .le(Exam::getStartTime, now.plusDays(1))
                .orderByDesc(Exam::getCreateTime)
                .last("limit 5")
                .list();
        for (Exam exam : list) {
            returnList.add(IndexWellStartExamList.builder()
                    .score(exam.getTotalScore())
                    .examName(exam.getTitle())
                    .examDate(exam.getStartTime().toLocalDate().toString())
                    .examTime(exam.getStartTime().toLocalTime().toString())
                    .examPassScore(exam.getPassScore())
                    .build());
        }
        return returnList;
    }

    @Override
    public Map<String, List<IndexExamCalendarResponse>> examCalendarListData(String date) {
        DateTime startTime = DateUtil.beginOfMonth(DateUtil.parse(date + "-01"));
        DateTime endTime = DateUtil.endOfMonth(startTime);
        List<Exam> examList = examService
                .lambdaQuery()
                .between(Exam::getStartTime, startTime, endTime)
                .list();
        // 组装需要返回的数据
        List<IndexExamCalendarResponse> examCalendarResponseList = examList.stream()
                .map(exam -> IndexExamCalendarResponse
                .builder()
                .key(exam.getId())
                .examStartTime(DateUtil.format(exam.getStartTime(), "yyyy-MM-dd"))
                .type(this.handleExamType(exam.getStatus()))
                .title(exam.getTitle())
                .build()
        ).toList();
        return examCalendarResponseList.stream().collect(Collectors.groupingBy(IndexExamCalendarResponse::getExamStartTime));
    }

    /**
     * 处理考试类型
     *
     * @param status 考试状态
     * @return 类型
     */
    private String handleExamType(Integer status) {
        if (Objects.equals(status, SystemConstants.ExamTypeStatusConstants.NOT_START)) {
            return SystemConstants.ExamTypeStatusConstants.PRIMARY;
        }
        if (Objects.equals(status, SystemConstants.ExamTypeStatusConstants.CAN_JOIN)) {
            return SystemConstants.ExamTypeStatusConstants.SUCCESS;
        }
        if (Objects.equals(status, SystemConstants.ExamTypeStatusConstants.FINISH)) {
            return SystemConstants.ExamTypeStatusConstants.WARNING;
        }
        if (Objects.equals(status, SystemConstants.ExamTypeStatusConstants.EXPIRE)) {
            return SystemConstants.ExamTypeStatusConstants.ERROR;
        }
        return null;
    }
}
