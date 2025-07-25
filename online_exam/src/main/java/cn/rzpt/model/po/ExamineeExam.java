package cn.rzpt.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("examinee_exam")
@AllArgsConstructor
@NoArgsConstructor
public class ExamineeExam extends BasePO {


    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 考生ID
     */
    private String examineeId;

    /**
     * 考试ID
     */
    private String examId;

    /**
     * 开始考试时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 提交考试时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;

    /**
     * 得分
     */
    private Double score;

    /**
     * 得分原因
     */
    private String reason;

    /**
     * 状态（0-未开始,1-进行中,2-已完成,3-已过期）
     */
    private Integer status;

    /**
     * 考试进度
     */
    private Integer progress;

    /**
     * 考生答案（JSON格式）
     */
    private String answers;

}