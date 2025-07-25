package cn.rzpt.model.bo;

import lombok.Builder;
import lombok.Data;

@Data
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
