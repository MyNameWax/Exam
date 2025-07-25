package cn.rzpt.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreBO {

    private Double score;

    private String userAnswer;

    private String reason;



}
