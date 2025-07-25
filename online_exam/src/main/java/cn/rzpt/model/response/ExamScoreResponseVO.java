package cn.rzpt.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String examineeNumber;

}
