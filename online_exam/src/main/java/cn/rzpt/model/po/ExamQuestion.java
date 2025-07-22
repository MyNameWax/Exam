package cn.rzpt.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("exam_question")
@Schema(description = "考试题目")
public class ExamQuestion  extends BasePO implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 考试ID
     */
    private String examId;

    /**
     * 题目类型
     */
    private Integer questionType;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 题目选项
     */
    private String options;

    /**
     * 答案
     */
    private String answer;

    /**
     * 得分
     */
    private Double score;

    /**
     * 排序
     */
    private Double sort;

}
