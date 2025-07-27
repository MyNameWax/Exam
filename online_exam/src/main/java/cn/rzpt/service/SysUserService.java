package cn.rzpt.service;

import cn.rzpt.model.po.SysUser;
import cn.rzpt.model.request.AuthLoginRequest;
import cn.rzpt.model.response.AuthLoginResponse;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysUserService extends IService<SysUser> {

    /**
     * 用户登录
     */
    AuthLoginResponse login(AuthLoginRequest loginRequest);

    /**
     * 个人信息
     */
    SysUser getUserInfo();


}
