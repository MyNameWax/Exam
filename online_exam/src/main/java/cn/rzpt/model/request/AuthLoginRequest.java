package cn.rzpt.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthLoginRequest {


    private String username;

    private String password;

}
