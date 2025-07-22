package cn.rzpt.service.impl;

import cn.rzpt.constants.SystemConstants;
import cn.rzpt.mapper.ExamUserMapper;
import cn.rzpt.model.bo.ExamUserLoginVO;
import cn.rzpt.model.po.ExamUser;
import cn.rzpt.model.request.ExamUserLoginRequest;
import cn.rzpt.model.response.ExamUserLoginResponse;
import cn.rzpt.properties.ExamJwtProperties;
import cn.rzpt.service.ExamUserService;
import cn.rzpt.util.ExamUserJwtUtil;
import cn.rzpt.util.SimpleKeyGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class ExamUserServiceImpl extends ServiceImpl<ExamUserMapper, ExamUser> implements ExamUserService {

    private ExamJwtProperties examJwtProperties;

    @Override
    public ExamUserLoginResponse login(ExamUserLoginRequest request) {
        ExamUser examUser = this.lambdaQuery().eq(ExamUser::getExamineeNumber, request.getExamineeNumber()).eq(ExamUser::getCheckCode, request.getCheckCode()).one();
        ExamUserLoginVO examUserLoginVO = this.validateExamUserCheckCode(examUser, SystemConstants.ExamUserLoginConstants.YES);
        return ExamUserLoginResponse.builder()
                .id(examUser.getId())
                .examineeName(examUser.getExamineeName())
                .token(examUserLoginVO.getToken())
                .build();
    }


    /**
     * 校验码是否正确
     */
    public ExamUserLoginVO validateExamUserCheckCode(ExamUser examUser,String flag) {
        String token = null;
        boolean result = SimpleKeyGenerator.validateLoginKey(examUser.getExamineeNumber(), examUser.getCheckCode());
        if (result && flag.equals(SystemConstants.ExamUserLoginConstants.YES)) {
            Map<String,String> map = new HashMap<>(1);
            map.put("id",examUser.getId());
            token = ExamUserJwtUtil.createJWT(examJwtProperties.getSecret(), examJwtProperties.getExpire(), map);
        }
        return ExamUserLoginVO.builder()
                .flag(result)
                .token(token)
                .build();
    }
}
