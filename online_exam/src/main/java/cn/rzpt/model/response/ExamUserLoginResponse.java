package cn.rzpt.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "考生用户登录响应参数")
public class ExamUserLoginResponse {

    /**
     * 考生编号
     */
    @Schema(description = "考生编号",example = "1234567890")
    private String id;
    /**
     * 考生姓名
     */
    @Schema(description = "考生姓名",example = "张三")
    private String examineeName;
    /**
     * 认证信息
     */
    @Schema(description = "认证信息",example = "1234567890")
    private String token;

}
