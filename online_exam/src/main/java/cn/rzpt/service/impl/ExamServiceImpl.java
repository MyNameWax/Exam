package cn.rzpt.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.rzpt.common.context.BaseContext;
import cn.rzpt.enums.ExamStatus;
import cn.rzpt.mapper.ExamMapper;
import cn.rzpt.model.po.Exam;
import cn.rzpt.model.po.ExamQuestion;
import cn.rzpt.model.po.ExamineeExam;
import cn.rzpt.model.request.ExamAddRequest;
import cn.rzpt.model.response.ExamUserListResponse;
import cn.rzpt.service.ExamQuestionService;
import cn.rzpt.service.ExamService;
import cn.rzpt.service.ExamineeExamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    @Resource
    private ExamQuestionService examQuestionService;
    @Resource
    private ExamineeExamService examineeExamService;

    @Override
    public Boolean addExam(ExamAddRequest examAddRequest) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(examAddRequest, exam);
        exam.setStatus(ExamStatus.NOT_START.getCode());
        this.save(exam);
        return true;
    }

    @Override
    public List<ExamUserListResponse> examUserList(String status) {
        String currentLoginUserId = BaseContext.getCurrentId();
        List<Exam> list = lambdaQuery()
                .eq(StrUtil.isNotBlank(status), Exam::getStatus, status)
                .orderByDesc(Exam::getCreateTime)
                .list();
        List<ExamUserListResponse> returnList = list.stream().map(exam -> {
            ExamUserListResponse examUserListResponse = new ExamUserListResponse();
            BeanUtils.copyProperties(exam, examUserListResponse);
            examUserListResponse.setExamTime(exam.getStartTime());
            examUserListResponse.setIsNew(list.indexOf(exam) < 3);
            ExamineeExam examineeExam = examineeExamService.lambdaQuery().eq(ExamineeExam::getExamId, exam.getId())
                    .eq(ExamineeExam::getExamineeId, currentLoginUserId).one();
            if (examineeExam != null) {
                examUserListResponse.setProgress(examineeExam.getProgress());
            }else {
                examUserListResponse.setProgress(0);
            }
            return examUserListResponse;
        }).toList();
        return returnList;


    }

    @Override
    public List<ExamQuestion> examQuestionList(String id) {
        return this.examQuestionService.lambdaQuery()
                .eq(ExamQuestion::getExamId, id)
                .orderByAsc(ExamQuestion::getSort)
                .list();
    }


}
