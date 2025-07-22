package cn.rzpt.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamUserListResponse {

    /**
     * ID
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 考试开始时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime examTime;

    /**
     * 考试时间
     */
    private Integer duration;

    /**
     * 总分
     */
    private Double totalScore;

    /**
     * 状态
     */
    private Integer status;

    /**
     *  类型
     */
    private String type;

    /**
     * 是否是新考试
     */
    private Boolean isNew;

    /**
     * 考试进度
     */
    private Integer progress;

}
