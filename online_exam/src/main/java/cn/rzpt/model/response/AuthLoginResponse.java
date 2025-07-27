package cn.rzpt.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthLoginResponse {

    private String id;

    private String realname;

    private String token;

}
