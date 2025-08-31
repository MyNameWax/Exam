package cn.rzpt.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserScoreResponse {

    private String title;

    private Double score;

    /**
     * 得分原因
     */
    private String reason;
}
