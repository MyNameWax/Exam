package cn.rzpt.service;

import cn.rzpt.model.po.ExamQuestion;
import cn.rzpt.model.request.ExamQuestionAddRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ExamQuestionService extends IService<ExamQuestion> {
    /**
     * 新增考试题目
     */
    Boolean addExamQuestion(ExamQuestionAddRequest question);

    /**
     * 考试分页题目列表
     */
    IPage<ExamQuestion> pageList(String pageSize, String pageNumber, ExamQuestion examQuestion);
}
