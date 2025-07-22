package cn.rzpt.service;

import cn.rzpt.model.bo.CandidateInfo;

public interface CandidateNumberService {

    /**
     * 生成考生号
     */
    String generateNumber(CandidateInfo info);

    /**
     * 验证考生号
     */
    boolean validateNumber(String candidateNumber);

}
