package cn.rzpt;

import cn.rzpt.enums.ExamType;
import cn.rzpt.model.bo.CandidateInfo;
import cn.rzpt.service.CandidateNumberService;
import cn.rzpt.util.SimpleKeyGenerator;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExamBackendApplicationTests {

    @Resource
    private CandidateNumberService candidateNumberService;

    /**
     * 生成考生号
     */
    @Test
    void generatorExamineeNumber() {
        CandidateInfo info = new CandidateInfo(ExamType.REGULAR, 2025);
        String number = candidateNumberService.generateNumber(info);
        System.out.println("考生号:" + number);  // 1250076971550728
    }

    /**
     * 生成校验码
     */
    @Test
    void generatorExamineeCheckCode() {
        String candidateId = "1250076971550728";
        String loginKey = SimpleKeyGenerator.generateLoginKey(candidateId);
        System.out.println("生成的登录密钥: " + loginKey);  //rgr4l6quyyp/JyFWY6Utqw==:Q34ZIhJmUDKLTjYLkn79htyqHgkFpXutkPHC9ejFKAY=
    }

    /**
     * 验证考生号和校验码是否正确
     */
    @Test
    void validateExamineeNumber() {
        boolean isValid = SimpleKeyGenerator.validateLoginKey("1250002265907207", "mL/BAwcx29062tuQOwqBGw==:hUoEL8H9z0UktyJBl75n55aKb1kbfD33cv/Wsj42+eQ=");
        System.out.println("密钥验证结果: " + isValid); //true
    }

}
