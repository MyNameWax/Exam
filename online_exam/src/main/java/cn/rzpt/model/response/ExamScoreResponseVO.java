package cn.rzpt.model.response;

import cn.rzpt.anno.SensitiveData;
import cn.rzpt.enums.SensitiveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamScoreResponseVO {

    /**
     * 考试成绩
     */
    private Double score;

    /**
     * 考试时长（分钟）
     */
    private Long minutes;

    /**
     * 考试类别
     */
    private String typeEnumsLabel;
    /**
     * 考生姓名
     */
    private String examUserName;

    /**
     * 考生号
     */
    @SensitiveData(type = SensitiveType.EXAM_NUMBER)
    private String examineeNumber;

    /**
     * 用户成绩信息
     */
    private List<UserScoreResponse> userScoreResponses;

    /**
     * 考试结果
     */
    private String examResult;

}
