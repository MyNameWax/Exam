package cn.rzpt.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreBO {

    /**
     * 得分情况
     */
    private Double score;

    /**
     * 用户答案
     */
    private String userAnswer;

    /**
     * 得分原因
     */
    private String reason;

    /**
     * 标题
     */
    private String title;

    /**
     * 总成绩
     */
    private Double totalScore;


}
