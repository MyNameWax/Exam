package cn.rzpt.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt.exam")
public class ExamJwtProperties {

    private String secret;

    private long expire;
}
