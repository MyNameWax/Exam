package cn.rzpt.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
public class ExamAddRequest {

    /**
     * 标题
     */
    @Schema(description = "标题",defaultValue = "Java考试")
    private String title;

    /**
     * 描述
     */
    @Schema(description = "描述",defaultValue = "Java考试")
    private String description;

    /**
     * 类型
     */
    @Schema(description = "类型",defaultValue = "Java考试")
    private String type;

    /**
     * 考试时长
     */
    @Schema(description = "考试时长",defaultValue = "60")
    private Integer duration;

    /**
     * 开始时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间",defaultValue = "2023-05-01 00:00:00")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束时间",defaultValue = "2023-05-01 00:10:00")
    private LocalDateTime endTime;

    /**
     * 总分
     */
    @Schema(description = "总分",defaultValue = "100")
    private Double totalScore;

    /**
     * 及格分
     */
    @Schema(description = "及格分",defaultValue = "60")
    private Double passScore;


}
