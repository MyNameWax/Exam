package cn.rzpt.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt.sys")
public class SysJwtProperties {

    private String secret;

    private long expire;
}
