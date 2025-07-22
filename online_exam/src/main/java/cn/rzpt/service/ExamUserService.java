package cn.rzpt.service;

import cn.rzpt.model.po.ExamUser;
import cn.rzpt.model.request.ExamUserLoginRequest;
import cn.rzpt.model.response.ExamUserLoginResponse;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ExamUserService extends IService<ExamUser> {

    /**
     * 考生登录
     */
    ExamUserLoginResponse login(ExamUserLoginRequest request);
}
