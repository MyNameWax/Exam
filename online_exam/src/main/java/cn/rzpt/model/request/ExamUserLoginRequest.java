package cn.rzpt.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "考生用户登录请求参数")
public class ExamUserLoginRequest {

    /**
     * 考生号
     */
    @Schema(description = "考生号",defaultValue = "12025XXXXXXXX")
    private String examineeNumber;

    /**
     * 校验码
     */
    @Schema(description = "校验码",defaultValue = "123456")
    private String checkCode;

}
