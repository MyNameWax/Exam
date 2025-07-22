package cn.rzpt.service.impl;

import cn.rzpt.model.bo.CandidateInfo;
import cn.rzpt.service.CandidateNumberService;
import cn.rzpt.util.IdGenerator;
import cn.rzpt.util.LuhnUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CandidateNumberServiceImpl implements CandidateNumberService {

    private final IdGenerator idGenerator;

    public CandidateNumberServiceImpl() {
        this.idGenerator = new IdGenerator(1);
    }


    @Override
    public String generateNumber(CandidateInfo info) {
        // 生成序列号
        long sequence = idGenerator.nextId();

        // 组装号码
        String rawNumber = String.format("1%02d%02d%010d",
                info.getExamYear() % 100,
                info.getExamType().getCode(),
                sequence % 10_000_000_000L);

        // 添加校验位
        return rawNumber + LuhnUtil.calculateCheckDigit(rawNumber);
    }

    @Override
    public boolean validateNumber(String candidateNumber) {
        if (candidateNumber == null || candidateNumber.length() != 16) {
            return false;
        }
        String body = candidateNumber.substring(0, 15);
        char checkDigit = candidateNumber.charAt(15);
        return LuhnUtil.validateCheckDigit(body, checkDigit);
    }
}
