package cn.rzpt;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.DesensitizedUtil;
import cn.rzpt.constants.SystemConstants;
import cn.rzpt.enums.ExamMarkDifferentEnums;
import cn.rzpt.enums.ExamUserType;
import cn.rzpt.model.bo.AiScoreBO;
import cn.rzpt.model.bo.CandidateInfo;
import cn.rzpt.serializer.SensitiveDataSerializer;
import cn.rzpt.service.CandidateNumberService;
import cn.rzpt.util.SimpleKeyGenerator;
import com.google.gson.Gson;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ExamBackendApplicationTests {

    @Resource
    private CandidateNumberService candidateNumberService;
    @Resource
    private OpenAiChatModel chatModel;
    private final static Gson Gson = new Gson();

    /**
     * 生成考生号
     */
    @Test
    void generatorExamineeNumber() {
        CandidateInfo info = new CandidateInfo(ExamUserType.ART, 2025);
        String number = candidateNumberService.generateNumber(info);
        System.out.println("考生号:" + number);  // 1250076971550728
    }

    /**
     * 生成校验码
     */
    @Test
    void generatorExamineeCheckCode() {
        String candidateId = "1250193674577921";
        String loginKey = SimpleKeyGenerator.generateLoginKey(candidateId);
        System.out.println("生成的登录密钥: " + loginKey);  //rgr4l6quyyp/JyFWY6Utqw==:Q34ZIhJmUDKLTjYLkn79htyqHgkFpXutkPHC9ejFKAY=
    }

    /**
     * 验证考生号和校验码是否正确
     */
    @Test
    void validateExamineeNumber() {
        boolean isValid = SimpleKeyGenerator.validateLoginKey("1250193674577921", "aP1fY0l/43zbetWLzy/0Q==:ruO5R+dRgQ56i7/phLR6baUjenflg3eIq3okYIIA3a4=");
//        System.out.println("密钥验证结果: " + isValid); //true
//        isValid = true ;
        // Junit
        Assert.isTrue(isValid);
    }

    @Test
    void testSensitive() {
        String number = "1947624138067492865";
        SensitiveDataSerializer sensitiveDataSerializer = new SensitiveDataSerializer();

        System.out.println(sensitiveDataSerializer.desensitize(number,3,4));
    }

    /**
     * 测试AI是否好用
     */
    @Test
    void aiTest() {
        String aiScoreMessage = SystemConstants.ExamMarkConstants.generatorMessage(
                ExamMarkDifferentEnums.EASY.getDesc(),
                "简述Java的多态性及其实现方式",
                "是面向对象特征之一",
                10.0
        );
        String aiScoreReason = chatModel.generate(aiScoreMessage);
        String aiScoreReasonClean = aiScoreReason.replaceAll("`", "").replaceAll("json", "");
        AiScoreBO aiScoreBO = Gson.fromJson(aiScoreReasonClean, AiScoreBO.class);
        Double score = aiScoreBO.getScore();
        String reason = aiScoreBO.getReason();
        System.out.println("得分: " + score);
        System.out.println("原因: " + reason);
    }


}
