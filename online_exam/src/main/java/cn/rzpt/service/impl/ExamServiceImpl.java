package cn.rzpt.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.rzpt.enums.ExamStatus;
import cn.rzpt.mapper.ExamMapper;
import cn.rzpt.model.po.Exam;
import cn.rzpt.model.po.ExamQuestion;
import cn.rzpt.model.request.ExamAddRequest;
import cn.rzpt.model.response.ExamUserListResponse;
import cn.rzpt.service.ExamQuestionService;
import cn.rzpt.service.ExamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
@AllArgsConstructor
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    private ExamQuestionService examQuestionService;

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
        List<Exam> list = lambdaQuery()
                .eq(StrUtil.isNotBlank(status), Exam::getStatus, status)
                .orderByDesc(Exam::getCreateTime)
                .list();
        List<ExamUserListResponse> returnList = list.stream().map(exam -> {
            ExamUserListResponse examUserListResponse = new ExamUserListResponse();
            BeanUtils.copyProperties(exam, examUserListResponse);
            examUserListResponse.setExamTime(exam.getStartTime());
            examUserListResponse.setIsNew(list.indexOf(exam) < 3);
            //TODO  这个考试应该多少人考、已经考了多少人
            Random random = new Random();
            examUserListResponse.setProgress(random.nextInt(100) + 1);
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
