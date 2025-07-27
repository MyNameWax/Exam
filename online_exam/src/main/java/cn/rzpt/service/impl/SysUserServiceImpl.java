package cn.rzpt.service.impl;

import cn.rzpt.common.context.BaseContext;
import cn.rzpt.common.global.result.DataResultCodeEnum;
import cn.rzpt.mapper.SysUserMapper;
import cn.rzpt.model.po.SysUser;
import cn.rzpt.model.request.AuthLoginRequest;
import cn.rzpt.model.response.AuthLoginResponse;
import cn.rzpt.properties.SysJwtProperties;
import cn.rzpt.service.SysUserService;
import cn.rzpt.util.PasswordUtil;
import cn.rzpt.util.SysUserJwtUtil;
import cn.rzpt.util.ThrowUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysUserJwtUtil jwtUtil;
    @Resource
    private SysJwtProperties sysJwtProperties;


    @Override
    public AuthLoginResponse login(AuthLoginRequest loginRequest) {
        String encodePassword = PasswordUtil.encode(loginRequest.getPassword());
        boolean matches = PasswordUtil.matches(loginRequest.getPassword(), encodePassword);
        // 密码加密
        SysUser user = this.lambdaQuery().eq(SysUser::getUsername, loginRequest.getUsername())
                .one();
        ThrowUtil.throwIf(user == null || !matches, DataResultCodeEnum.NOT_FOUND_USER);
        Map<String,String> map = new HashMap<>(1);
        map.put("id",user.getId());
        String token = jwtUtil.createJWT(sysJwtProperties.getSecret(), sysJwtProperties.getExpire(), map);
        return AuthLoginResponse.builder()
                .token(token)
                .id(user.getId())
                .realname(user.getRealname())
                .build();
    }

    @Override
    public SysUser getUserInfo() {
        SysUser user = this.lambdaQuery()
                .eq(SysUser::getId, BaseContext.getCurrentId())
                .one();

        return user;
    }
}
