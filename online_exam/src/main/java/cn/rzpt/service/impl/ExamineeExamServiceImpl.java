package cn.rzpt.service.impl;

import cn.rzpt.common.context.BaseContext;
import cn.rzpt.common.global.result.DataResultCodeEnum;
import cn.rzpt.enums.ExamineeExamStatus;
import cn.rzpt.enums.QuestionType;
import cn.rzpt.mapper.ExamineeExamMapper;
import cn.rzpt.model.po.Exam;
import cn.rzpt.model.po.ExamQuestion;
import cn.rzpt.model.po.ExamineeExam;
import cn.rzpt.model.request.ExamSubmitRequest;
import cn.rzpt.model.response.BlankVO;
import cn.rzpt.model.response.ExamDetailVO;
import cn.rzpt.model.response.QuestionOption;
import cn.rzpt.model.response.QuestionVO;
import cn.rzpt.service.ExamQuestionService;
import cn.rzpt.service.ExamService;
import cn.rzpt.service.ExamineeExamService;
import cn.rzpt.util.ThrowUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExamineeExamServiceImpl extends ServiceImpl<ExamineeExamMapper, ExamineeExam> implements ExamineeExamService {

    @Resource
    private ExamService examService;
    @Resource
    private ExamQuestionService examQuestionService;

    private final static Gson gson = new Gson();

    @Override
    public String createExamRecord(String id) {
        Exam exam = examService.getById(id);
        ExamineeExam examineeExam = new ExamineeExam();
        String currentLoginUserId = BaseContext.getCurrentId();
        examineeExam.setExamineeId(currentLoginUserId);
        examineeExam.setExamId(exam.getId());
        examineeExam.setStartTime(LocalDateTime.now());
        examineeExam.setProgress(0);
        examineeExam.setStatus(ExamineeExamStatus.ONGOING.getCode());
        this.save(examineeExam);
        this.scheduleProgressUpdate(exam,examineeExam);
        return examineeExam.getId();

    }
    private void scheduleProgressUpdate(Exam exam,ExamineeExam examineeExam) {
        LocalDateTime systemTime = LocalDateTime.now();
        LocalDateTime startTime = exam.getStartTime();
        // 系统时间 - 开始时间 转换为分钟
        long minutes = systemTime.until(startTime, java.time.temporal.ChronoUnit.MINUTES);
        Double progress = ((minutes * 1.0D) / exam.getDuration());
        examineeExam.setProgress(Math.min((int)(progress * 100), 100));
        this.updateById(examineeExam);
    }

    @Override
    public Boolean submitAnswers(ExamSubmitRequest examSubmitRequest) {
        ExamineeExam examineeExam = this.getById(examSubmitRequest.getId());
        ThrowUtil.throwIf(examineeExam == null
                        || !examineeExam.getStatus().equals(ExamineeExamStatus.ONGOING.getCode())
                , DataResultCodeEnum.REQUEST_ERROR);
        // 获取考试题目
        List<ExamQuestion> questions = examQuestionService.lambdaQuery().eq(ExamQuestion::getExamId, examineeExam.getExamId())
                .list();
        // 验证答案完整性 (因为可能会被强制收卷)
        // ThrowUtil.throwIf(examSubmitRequest.getAnswers().size() != questions.size(), DataResultCodeEnum.REQUEST_ERROR);

        // 转换答案格式并计算分数

        Map<String, String> answerMap = new HashMap<>();
        Double totalScore = 0.0D;

        for (ExamSubmitRequest.QuestionAnswerDTO answer : examSubmitRequest.getAnswers()) {
            ExamQuestion question = questions.stream().filter(q -> q.getId().equals(answer.getQuestionId())).findFirst().orElse(null);
            ThrowUtil.throwIf(question == null, DataResultCodeEnum.REQUEST_ERROR);
            ThrowUtil.throwIf(!question.getQuestionType().equals(answer.getType()), DataResultCodeEnum.REQUEST_ERROR);
            String answerValue = "";
            if (answer.getType().equals(QuestionType.SINGLE.getCode()) || answer.getType().equals(QuestionType.MULTIPLE.getCode())) {
                answerValue = gson.toJson(answer.getUserAnswer());
            }
            if (answer.getType().equals(QuestionType.JUDGE.getCode())) {
                answerValue = answer.getJudgmentAnswer() ? "正确" : "错误";
            }
            if (answer.getType().equals(QuestionType.FILL.getCode())) {
                answerValue = answer.getFillAnswer();
            }
            if (answer.getType().equals(QuestionType.ESSAY.getCode())) {
                answerValue = answer.getEssayAnswer();
            }
            answerMap.put(answer.getQuestionId(), answerValue);
            // 计算得分
            Double questionScore = this.calculateQuestionScore(question, answerValue);
            totalScore += questionScore;
        }
        // 更新考试记录
        examineeExam.setStatus(ExamineeExamStatus.FINISH.getCode());
        examineeExam.setSubmitTime(LocalDateTime.now());
        examineeExam.setProgress(100);
        examineeExam.setScore(totalScore);
        examineeExam.setAnswers(gson.toJson(answerMap));

        this.updateById(examineeExam);

        return true;
    }

    @Override
    public ExamDetailVO getExamDetails(String examId) {
        Exam exam = examService.getById(examId);
        List<ExamQuestion> questions = examQuestionService.lambdaQuery().eq(ExamQuestion::getExamId, examId)
                .orderByAsc(ExamQuestion::getSort).list();
        // 构建返回对象
        ExamDetailVO examDetailVO = new ExamDetailVO();
        examDetailVO.setExamId(exam.getId());
        examDetailVO.setTitle(exam.getTitle());
        examDetailVO.setDescription(exam.getDescription());
        examDetailVO.setDuration(exam.getDuration());
        examDetailVO.setTotalScore(exam.getTotalScore());
        examDetailVO.setQuestions(convertQuestions(questions));
        return examDetailVO;
    }



    private List<QuestionVO> convertQuestions(List<ExamQuestion> questions) {
        return questions.stream().map(q -> {
            QuestionVO vo = new QuestionVO();
            vo.setQuestionId(q.getId());
            vo.setTitle(q.getContent());
            vo.setType(q.getQuestionType());
            vo.setContent(q.getContent());
            vo.setScore(q.getScore());

            if (q.getOptions() != null) {
                vo.setOptions(gson.fromJson(q.getOptions(), new TypeToken<List<QuestionOption>>(){}.getType()));
            }

            if (q.getQuestionType() == QuestionType.FILL.getCode()) {
                vo.setBlanks(extractBlanks(q.getContent()));
            }
            return vo;
        }).collect(Collectors.toList());
    }

    private List<BlankVO> extractBlanks(String content) {
        // 从题目内容中提取填空位置
        List<BlankVO> blanks = new ArrayList<>();
        Pattern pattern = Pattern.compile("_{3,}");
        Matcher matcher = pattern.matcher(content);

        int index = 1;
        while (matcher.find()) {
            blanks.add(new BlankVO(index++, "填空" + index));
        }

        return blanks;
    }

    /**
     * 判题
     */
    private Double calculateQuestionScore(ExamQuestion question, String answerValue) {
        Integer questionType = question.getQuestionType();
        if (questionType.equals(QuestionType.SINGLE.getCode()) || questionType.equals(QuestionType.JUDGE.getCode())) {
            return question.getAnswer().equals(answerValue) ? question.getScore() : 0.0D;
        }
        if (questionType.equals(QuestionType.MULTIPLE.getCode())) {
            return this.handleMultipleQuestion(question, answerValue, 3);
        }
        if (questionType.equals(QuestionType.FILL.getCode())) {
            return this.calculateFillBlankScore(question.getAnswer(), answerValue, question.getScore());
        }
        if (questionType.equals(QuestionType.ESSAY.getCode())) {
            //TODO AI 评分参考（）
            return 0.0D;
        }

        return 0.0D;
    }

    /**
     * 填空题评分
     */
    private Double calculateFillBlankScore(String answer, String answerValue, Double score) {
        return answerValue.trim().equals(answer.trim()) ? score : 0.0D;
    }

    /**
     * 多选题评分
     *
     * @param mode 评分模式： 1 - 严格模式 2 - 宽松模式 3 - 混合模式
     */
    private Double handleMultipleQuestion(ExamQuestion question, String answerValue, int mode) {
        Set<String> correctAnswers = new HashSet<>(Arrays.asList(question.getAnswer().split(",")));
        Set<String> userAnswers = new HashSet<>(Arrays.asList(answerValue.split(",")));
        switch (mode) {
            case 1:  //严格模式必须全对
                return userAnswers.equals(correctAnswers) ? question.getScore() : 0.0D;
            case 2:  // 宽松模式 部分得分
                long correctCount = userAnswers.stream().filter(correctAnswers::contains).count();
                long wrongCount = userAnswers.size() - correctCount;
                double scorePerOption = question.getScore() / correctAnswers.size();
                return Math.max(0, (correctCount - wrongCount) * scorePerOption);

            case 3:  // 混合模式（无错误答案的时候 按照比例给分
            default:
                boolean hasWrongAnswer = userAnswers.stream().anyMatch(answer -> !correctAnswers.contains(answer));
                if (hasWrongAnswer) {
                    return 0.0D;
                }
                return (userAnswers.size() * question.getScore() / correctAnswers.size());
        }
    }
}