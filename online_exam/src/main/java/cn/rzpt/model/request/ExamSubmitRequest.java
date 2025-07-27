package cn.rzpt.model.request;

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
@Schema(description = "考试提交请求")
public class ExamSubmitRequest {

    @Schema(description = "记录ID")
    private String id;
    @Schema(description = "考试ID")
    private String examId;
    @Schema(description = "考试题目答案")
    private List<QuestionAnswerDTO> answers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionAnswerDTO{

        @Schema(description = "题目ID")
        private String questionId;
        @Schema(description = "题目类型")
        private Integer type;
        @Schema(description = "选择题答案")
        private String choiceAnswer;
        @Schema(description = "判断题答案")
        private Boolean judgmentAnswer;
        @Schema(description = "填空题答案")
        private String fillAnswer;
        @Schema(description = "简答题答案")
        private String essayAnswer;
        @Schema(description = "用户提交答案")
        private List<String> userAnswer;
    }

}
