package cn.rzpt.common.global.filter;

import cn.hutool.core.util.StrUtil;
import cn.rzpt.anno.PassLogin;
import cn.rzpt.common.context.BaseContext;
import cn.rzpt.common.global.exception.GlobalException;
import cn.rzpt.common.global.result.DataResultCodeEnum;
import cn.rzpt.properties.ExamJwtProperties;
import cn.rzpt.properties.SysJwtProperties;
import cn.rzpt.util.ExamUserJwtUtil;
import cn.rzpt.util.SysUserJwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;


@Component
@AllArgsConstructor
public class AuthorizationFilter implements HandlerInterceptor {

    public static final String AUTHORIZATION = "Authorization";
    private final ExamJwtProperties examJwtProperties;
    private final SysUserJwtUtil sysUserJwtUtil;
    private final SysJwtProperties sysJwtProperties;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassLogin.class)) {
            PassLogin passLogin = method.getAnnotation(PassLogin.class);
            if (passLogin.required()) {
                return true;
            }
        }

        if (StrUtil.isEmpty(request.getHeader(AUTHORIZATION))) {
            throw new GlobalException(DataResultCodeEnum.UNAUTHORIZED);
        }
        try {
            if (request.getRequestURL().toString().contains("/sys") || request.getRequestURL().toString().contains("/index")) {
                Claims claims = sysUserJwtUtil.parseJWT(sysJwtProperties.getSecret(), request.getHeader(AUTHORIZATION));
                String id = claims.get("id", String.class);
                BaseContext.setCurrentId(id);
            }else {
                Claims claims = ExamUserJwtUtil.parseJWT(examJwtProperties.getSecret(), request.getHeader(AUTHORIZATION));
                String id = claims.get("id", String.class);
                BaseContext.setCurrentId(id);
            }

        } catch (Exception e) {
            throw new GlobalException(DataResultCodeEnum.UNAUTHORIZED);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContext.removeCurrentId();
    }
}
