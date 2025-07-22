package cn.rzpt.model.bo;

import cn.rzpt.enums.ExamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateInfo {

    /**
     * 考生类型
     */
    private ExamType examType;

    /**
     * 考试年份
     */
    private int examYear;

}
