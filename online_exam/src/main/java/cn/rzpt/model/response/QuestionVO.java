package cn.rzpt.model.response;

import lombok.Data;

import java.util.List;

@Data
public class QuestionVO {

    /**
     * 题目ID
     */
    private String questionId;
    /**
     * 1-单选 2-多选 3-判断 4-填空 5-简答
     */
    private Integer type;
    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 本体得分
     */
    private Double score;

    /**
     * 选项
     */
    private List<QuestionOption> options;

    /**
     * 填空题
     */
    private List<BlankVO> blanks;

}
