package cn.rzpt.service.impl;

import cn.rzpt.common.context.BaseContext;
import cn.rzpt.common.global.result.DataResultCodeEnum;
import cn.rzpt.constants.SystemConstants;
import cn.rzpt.enums.ExamMarkDifferentEnums;
import cn.rzpt.enums.ExamUserType;
import cn.rzpt.enums.ExamineeExamStatus;
import cn.rzpt.enums.QuestionType;
import cn.rzpt.mapper.ExamineeExamMapper;
import cn.rzpt.model.bo.AiScoreBO;
import cn.rzpt.model.bo.ScoreBO;
import cn.rzpt.model.po.Exam;
import cn.rzpt.model.po.ExamQuestion;
import cn.rzpt.model.po.ExamUser;
import cn.rzpt.model.po.ExamineeExam;
import cn.rzpt.model.request.ExamSubmitRequest;
import cn.rzpt.model.response.*;
import cn.rzpt.service.ExamQuestionService;
import cn.rzpt.service.ExamService;
import cn.rzpt.service.ExamUserService;
import cn.rzpt.service.ExamineeExamService;
import cn.rzpt.util.ThrowUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    private ExamineeExamService examineeExamService;
    @Resource
    private ExamUserService examUserService;
    @Resource
    private OpenAiChatModel chatModel;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

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
        this.scheduleProgressUpdate(exam, examineeExam);
        return examineeExam.getId();

    }

    private void scheduleProgressUpdate(Exam exam, ExamineeExam examineeExam) {
        LocalDateTime systemTime = LocalDateTime.now();
        LocalDateTime startTime = exam.getStartTime();
        // 系统时间 - 开始时间 转换为分钟
        long minutes = systemTime.until(startTime, java.time.temporal.ChronoUnit.MINUTES);
        Double progress = ((minutes * 1.0D) / exam.getDuration());
        examineeExam.setProgress(Math.min((int) (progress * 100), 100));
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

        Map<String, ScoreBO> answerMap = new HashMap<>();
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
                answerValue = gson.toJson(answer.getUserAnswer());
            }
            if (answer.getType().equals(QuestionType.FILL.getCode())) {
                answerValue = gson.toJson(answer.getUserAnswer());
            }
            if (answer.getType().equals(QuestionType.ESSAY.getCode())) {
                answerValue = gson.toJson(answer.getUserAnswer());
            }
            ScoreBO scoreBO = new ScoreBO();
            scoreBO.setUserAnswer(answerValue);
            // 计算得分
            if (answer.getType().equals(QuestionType.ESSAY.getCode())) {

            }
            AiScoreBO aiScoreBO = this.calculateQuestionScore(question, answerValue, examineeExam);
            scoreBO.setScore(aiScoreBO.getScore());
            totalScore += aiScoreBO.getScore();
            scoreBO.setReason(aiScoreBO.getReason() == null ? "" : aiScoreBO.getReason());
            answerMap.put(answer.getQuestionId(), scoreBO);
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

    @Override
    public ExamScoreResponseVO examineeExamResult(String examId) {
        String currentLoginUserId = BaseContext.getCurrentId();
        ExamUser examUser = examUserService.lambdaQuery().eq(ExamUser::getId, currentLoginUserId).one();
        ExamineeExam examineeExam = examineeExamService.lambdaQuery().eq(ExamineeExam::getExamId, examId)
                .eq(ExamineeExam::getExamineeId, currentLoginUserId).one();
        ThrowUtil.throwIf(examineeExam == null ||
                !examineeExam.getStatus().equals(ExamineeExamStatus.FINISH.getCode()) ||
                examineeExam.getSubmitTime() == null, DataResultCodeEnum.REQUEST_ERROR);
        ThrowUtil.throwIf(examineeExam.getScore() == null, DataResultCodeEnum.EXAM_SCORE_NOT_PUBLISHED);
        LocalDateTime startTime = examineeExam.getStartTime();
        LocalDateTime submitTime = examineeExam.getSubmitTime();
        Integer type = examUser.getType();
        String examineeName = examUser.getExamineeName();
        String examineeNumber = examUser.getExamineeNumber();
        return ExamScoreResponseVO.builder()
                .typeEnumsLabel(ExamUserType.getByCode(type))
                .examUserName(examineeName)
                .score(Math.round(examineeExam.getScore() * 100) / 100.0D)
                .examineeNumber(examineeNumber)
                .minutes(startTime.until(submitTime, java.time.temporal.ChronoUnit.MINUTES))
                .build();

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
                vo.setOptions(gson.fromJson(q.getOptions(), new TypeToken<List<QuestionOption>>() {
                }.getType()));
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
    private AiScoreBO calculateQuestionScore(ExamQuestion question, String answerValue, ExamineeExam examineeExam) {
        AiScoreBO aiScoreBO = new AiScoreBO();
        Integer questionType = question.getQuestionType();
        if (questionType.equals(QuestionType.SINGLE.getCode()) || questionType.equals(QuestionType.JUDGE.getCode())) {
            List answerList = gson.fromJson(answerValue, List.class);
            if (questionType.equals(QuestionType.JUDGE.getCode())) {
                String compareFlag = Boolean.parseBoolean(answerList.get(0).toString()) ? "正确" : "错误";
                aiScoreBO.setScore(question.getAnswer().equals(compareFlag) ? question.getScore() : SystemConstants.DefaultScoreConstants.ZERO);
                return aiScoreBO;
            }
            aiScoreBO.setScore(question.getAnswer().equals(answerList.get(0)) ? question.getScore() : SystemConstants.DefaultScoreConstants.ZERO);
            return aiScoreBO;
        }
        if (questionType.equals(QuestionType.MULTIPLE.getCode())) {
            return this.handleMultipleQuestion(aiScoreBO, question, answerValue, SystemConstants.ExamDifferentConstants.MIXED);
        }
        if (questionType.equals(QuestionType.FILL.getCode())) {
            return this.calculateFillBlankScore(aiScoreBO, question.getAnswer(), answerValue, question.getScore());
        }
        if (questionType.equals(QuestionType.ESSAY.getCode())) {
            aiScoreBO = this.calulateEaSayAnswer(question, answerValue);
            return aiScoreBO;
        }
        aiScoreBO.setScore(SystemConstants.DefaultScoreConstants.ZERO);
        return aiScoreBO;
    }

    /**
     * 简单题阅卷（AI阅卷）
     */
    private AiScoreBO calulateEaSayAnswer(ExamQuestion question, String answerValue) {
        String aiScoreMessage = SystemConstants.ExamMarkConstants.generatorMessage(
                ExamMarkDifferentEnums.EASY.getDesc(),
                question.getContent(),
                answerValue,
                question.getScore()
        );
        String aiScoreReason = chatModel.generate(aiScoreMessage);
        // 数据清洗
        String aiScoreReasonClean = aiScoreReason.replaceAll("`", "").replaceAll("json", "");
        AiScoreBO aiScoreBO = gson.fromJson(aiScoreReasonClean, AiScoreBO.class);
        return aiScoreBO;
    }

    /**
     * 填空题评分
     */
    private AiScoreBO calculateFillBlankScore(AiScoreBO aiScoreBO, String answer, String answerValue, Double score) {
        List<String> questionAnswer = Arrays.asList(answer.split(","));  // 题目的答案
        List<String> userAnswer = gson.fromJson(answerValue, List.class);
        questionAnswer.sort(String::compareTo);
        userAnswer.sort(String::compareTo);
        aiScoreBO.setScore(questionAnswer.equals(userAnswer) ? score : SystemConstants.DefaultScoreConstants.ZERO);
        return aiScoreBO;
    }

    /**
     * 多选题评分
     *
     * @param mode 评分模式： 1 - 严格模式 2 - 宽松模式 3 - 混合模式
     */
    private AiScoreBO handleMultipleQuestion(AiScoreBO scoreBO, ExamQuestion question, String answerValue, int mode) {
        Set<String> correctAnswers = new HashSet<>(Arrays.asList(question.getAnswer().split(",")));
        Set<String> userAnswerList = gson.fromJson(answerValue, Set.class);
        switch (mode) {
            case 1:  //严格模式必须全对
                scoreBO.setScore(userAnswerList.equals(correctAnswers) ? question.getScore() : SystemConstants.DefaultScoreConstants.ZERO);
                return scoreBO;
            case 2:  // 宽松模式 部分得分
                long correctCount = userAnswerList.stream().filter(correctAnswers::contains).count();
                long wrongCount = userAnswerList.size() - correctCount;
                double scorePerOption = question.getScore() / correctAnswers.size();
                double score = Math.max(0, (correctCount - wrongCount) * scorePerOption);
                scoreBO.setScore(score);
                return scoreBO;
            case 3:  // 混合模式（无错误答案的时候 按照比例给分
            default:
                boolean hasWrongAnswer = userAnswerList.stream().anyMatch(answer -> !correctAnswers.contains(answer));
                if (hasWrongAnswer) {
                    scoreBO.setScore(SystemConstants.DefaultScoreConstants.ZERO);
                    return scoreBO;
                }
                List<String> questionAnswer = Arrays.asList(question.getAnswer().split(","));  // 题目的答案
                List<String> userAnswer = gson.fromJson(answerValue, List.class);
                questionAnswer.sort(String::compareTo);
                userAnswer.sort(String::compareTo);
                scoreBO.setScore(questionAnswer.equals(userAnswer) ? question.getScore() : SystemConstants.DefaultScoreConstants.ZERO);
                return scoreBO;
        }
    }
}