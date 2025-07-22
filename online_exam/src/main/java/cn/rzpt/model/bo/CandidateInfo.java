package cn.rzpt.model.bo;

import cn.rzpt.enums.ExamUserType;
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
    private ExamUserType examType;

    /**
     * 考试年份
     */
    private int examYear;

}
