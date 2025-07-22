package cn.rzpt.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.rzpt.enums.ExamStatus;
import cn.rzpt.mapper.ExamMapper;
import cn.rzpt.model.po.Exam;
import cn.rzpt.model.request.ExamAddRequest;
import cn.rzpt.model.response.ExamUserListResponse;
import cn.rzpt.service.ExamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    @Override
    public Boolean addExam(ExamAddRequest examAddRequest) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(examAddRequest, exam);
        exam.setStatus(ExamStatus.NOT_START.getCode());
        this.save( exam );
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
            //TODO 考试进度  总题目数量  已答题数
            /*
            * // 总题数
                int totalQuestions = exam.getQuestions().size();
                // 已答题数
                int answeredQuestions = exam.getAnswers().stream()
                    .filter(answer -> answer != null && !answer.isEmpty())
                    .count();
                // 计算进度
                int progress = (int) ((answeredQuestions * 100) / totalQuestions);
            *
            * */
            return examUserListResponse;
        }).toList();
        return returnList;


    }


}
