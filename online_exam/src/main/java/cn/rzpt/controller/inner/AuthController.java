package cn.rzpt.controller.inner;

import cn.rzpt.anno.PassLogin;
import cn.rzpt.common.global.result.DataResult;
import cn.rzpt.model.po.SysUser;
import cn.rzpt.model.request.AuthLoginRequest;
import cn.rzpt.model.response.AuthLoginResponse;
import cn.rzpt.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理", description = "认证管理")
@RequestMapping("/sys/auth")
@RestController
public class AuthController {


    @Resource
    private SysUserService sysUserService;


    @Operation(summary = "登录")
    @PostMapping("/v1/login")
    @PassLogin
    public DataResult<AuthLoginResponse> authLogin(@RequestBody AuthLoginRequest loginRequest) {
        return DataResult.success(sysUserService.login(loginRequest));
    }

    @Operation(summary = "个人信息")
    @GetMapping("/v1/user_info")
    public DataResult<SysUser> userInfo() {
        return DataResult.success(sysUserService.getUserInfo());
    }

}
