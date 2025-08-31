package cn.rzpt.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IndexWellStartExamList {

    private String examName;

    private String examDate;

    private String examTime;

    private Double examPassScore;

    private Double score;

}
