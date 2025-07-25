package cn.rzpt.model.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AiScoreBO {


    /**
     * 得分
     */
    private Double score;

    /**
     * 原因
     */
    private String reason;

}
