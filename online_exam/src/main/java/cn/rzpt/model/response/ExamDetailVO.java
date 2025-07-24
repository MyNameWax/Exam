package cn.rzpt.model.response;

import lombok.Data;

import java.util.List;

@Data
public class ExamDetailVO {

    /**
     * 考试ID
     */
    private String examId;

    /**
     * 考试标题
     */
    private String title;

    /**
     * 考试描述
     */
    private String description;

    /**
     * 考试时长分钟
     */
    private Integer duration;

    /**
     * 总分
     */
    private Double totalScore;

    /**
     * 题目
     */
    private List<QuestionVO> questions;
}