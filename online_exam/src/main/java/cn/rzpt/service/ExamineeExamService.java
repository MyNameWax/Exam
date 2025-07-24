package cn.rzpt.service;

import cn.rzpt.model.po.ExamineeExam;
import cn.rzpt.model.request.ExamSubmitRequest;
import cn.rzpt.model.response.ExamDetailVO;
import cn.rzpt.model.response.ExamScoreResponseVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ExamineeExamService extends IService<ExamineeExam> {

    /**
     * 开始考试
     */
    String createExamRecord(String id);

    /**
     * 提交答案
     */
    Boolean submitAnswers(ExamSubmitRequest examSubmitRequest);

    /**
     * 考试题目
     */
    ExamDetailVO getExamDetails(String examId);

    /**
     * 考生查看考试结果
     */
    ExamScoreResponseVO examineeExamResult(String examId);
}