package cn.rzpt.common.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum DataResultCodeEnum {
    SUCCESS(200, "成功"),
    FAIL(500, "系统错误"),
    // 系统相关
    REQUEST_ERROR(501, "请求参数不合法"),
    // 考生相关
    UNAUTHORIZED(401, "请先登录"),
    USERNAME_PASSWORD_ERROR(402, "考生号或校验码错误"),
    USERNAME_NOT_ALLOW_EMPTY(403, "考生号不允许为空"),
    PASSWORD_NOT_ALLOW_EMPTY(404, "校验码不允许为空"),
    PHONE_ERROR(405, "手机号格式错误"),
    // 考试相关
    EXAM_SCORE_NOT_PUBLISHED(501, "成绩未公布,请耐心等待"),
    // 系统相关
    NOT_FOUND_USER(601, "用户名或密码错误");

    private final Integer code;
    private final String message;
}
