package cn.rzpt.model.request;

import cn.hutool.core.util.StrUtil;
import cn.rzpt.enums.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "考试题目添加参数")
public class ExamQuestionAddRequest {

    @Schema(description = "考试id")
    private String examId;
    @Schema(description = "题目类型")
    private Integer questionType;
    @Schema(description = "题目内容")
    private String content;
    @Schema(description = "题目选项(选择、判断)")
    private List<QuestionOption> options;
    @Schema(description = "填空、简答、选择答案")
    private String referenceAnswer;
    @Schema(description = "题目答案(判断)")
    private Boolean correctAnswer;
    @Schema(description = "题目分数")
    private Double score;
    @Schema(description = "题目排序")
    private Double sort;


    public boolean validate() {
        if (questionType == null || content == null || score == null) {
            return false;
        }
        if (questionType.equals(QuestionType.SINGLE.getCode()) || questionType.equals(QuestionType.MULTIPLE.getCode())) {
            return options != null && options.size() >= 2;
        }
        if (questionType.equals(QuestionType.FILL.getCode()) || questionType.equals(QuestionType.ESSAY.getCode())) {
            return StrUtil.isNotBlank(referenceAnswer);
        }
        if (questionType.equals(QuestionType.JUDGE.getCode())) {
            return correctAnswer != null;
        }
        return true;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionOption {
        private String key;
        private String value;
    }

}
