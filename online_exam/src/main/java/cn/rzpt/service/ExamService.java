package cn.rzpt.service;

import cn.rzpt.model.po.Exam;
import cn.rzpt.model.po.ExamQuestion;
import cn.rzpt.model.request.ExamAddRequest;
import cn.rzpt.model.response.ExamUserListResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ExamService extends IService<Exam> {

    /**
     * 增加考试
     */
    Boolean addExam(ExamAddRequest examAddRequest);

    /**
     * 考试列表
     */
    List<ExamUserListResponse> examUserList(String status);

    /**
     * 考试题目列表
     */
    List<ExamQuestion> examQuestionList(String id);
}
