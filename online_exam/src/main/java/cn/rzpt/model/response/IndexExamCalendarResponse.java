package cn.rzpt.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IndexExamCalendarResponse {


    private String key;

    private String type;

    private String title;

    private String examStartTime;

}
