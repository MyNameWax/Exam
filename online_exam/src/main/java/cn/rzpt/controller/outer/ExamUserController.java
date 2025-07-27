package cn.rzpt.controller.outer;

import cn.rzpt.anno.PassLogin;
import cn.rzpt.common.global.result.DataResult;
import cn.rzpt.model.request.ExamUserLoginRequest;
import cn.rzpt.model.response.ExamUserLoginResponse;
import cn.rzpt.service.ExamUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam/user")
@AllArgsConstructor
@Tag(name = "考生管理", description = "考生管理")
public class ExamUserController {

    private final ExamUserService examUserService;

    @PostMapping("/v1/login")
    @Operation(summary = "考生登录")
    @Parameters({
            @Parameter(name = "ExamUserLoginRequest", description = "考试登录信息",required = true),
    })
    @PassLogin()
    public DataResult<ExamUserLoginResponse> login(@RequestBody ExamUserLoginRequest request) {
        return DataResult.success(examUserService.login(request));
    }

}
