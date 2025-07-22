package cn.rzpt.common.global.filter;

import cn.hutool.core.util.StrUtil;
import cn.rzpt.anno.PassLogin;
import cn.rzpt.common.context.BaseContext;
import cn.rzpt.common.global.exception.GlobalException;
import cn.rzpt.common.global.properties.JwtProperties;
import cn.rzpt.common.global.result.DataResultCodeEnum;
import cn.rzpt.util.JwtUtil;
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
    private final JwtProperties tokenProperties;


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
            JwtUtil.parseJWT(tokenProperties.getSecret(), request.getHeader(AUTHORIZATION));
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
