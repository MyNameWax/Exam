package cn.rzpt.service.impl;


import cn.hutool.core.util.StrUtil;
import cn.rzpt.common.global.result.DataResultCodeEnum;
import cn.rzpt.enums.QuestionType;
import cn.rzpt.mapper.ExamQuestionMapper;
import cn.rzpt.model.po.ExamQuestion;
import cn.rzpt.model.request.ExamQuestionAddRequest;
import cn.rzpt.service.ExamQuestionService;
import cn.rzpt.util.ThrowUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class ExamQuestionServiceImpl extends ServiceImpl<ExamQuestionMapper, ExamQuestion> implements ExamQuestionService {

    private final static Gson gson = new Gson();

    @Override
    public Boolean addExamQuestion(ExamQuestionAddRequest question) {
        ThrowUtil.throwIf(!question.validate(), DataResultCodeEnum.REQUEST_ERROR);
        ExamQuestion examQuestion = new ExamQuestion();
        BeanUtils.copyProperties(question, examQuestion);
        if (question.getQuestionType().equals(QuestionType.SINGLE.getCode()) || question.getQuestionType().equals(QuestionType.MULTIPLE.getCode())) {
            examQuestion.setOptions(gson.toJson(question.getOptions()));
            examQuestion.setAnswer(question.getReferenceAnswer());
        }
        if (question.getQuestionType().equals(QuestionType.JUDGE.getCode())) {
            examQuestion.setAnswer(question.getCorrectAnswer() ? "正确" : "错误");
        }
        if (question.getQuestionType().equals(QuestionType.FILL.getCode()) || question.getQuestionType().equals(QuestionType.ESSAY.getCode())) {
            examQuestion.setAnswer(question.getReferenceAnswer());
        }
        this.save(examQuestion);
        return true;
    }

    @Override
    public IPage<ExamQuestion> pageList(String pageSize, String pageNumber, ExamQuestion examQuestion) {
        IPage<ExamQuestion> page = new Page<>(Long.parseLong(pageNumber), Long.parseLong(pageSize));
        LambdaQueryWrapper<ExamQuestion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(examQuestion.getQuestionType()),ExamQuestion::getQuestionType, examQuestion.getQuestionType())
                .eq(Objects.nonNull(examQuestion.getScore()),ExamQuestion::getScore, examQuestion.getScore())
                .like(StrUtil.isNotBlank(examQuestion.getContent()), ExamQuestion::getContent, examQuestion.getContent());
        queryWrapper.orderByDesc(ExamQuestion::getCreateTime);
        IPage<ExamQuestion> pageList = this.page(page, queryWrapper);
        return pageList;
    }
}
