package cn.rzpt;

import cn.rzpt.enums.ExamType;
import cn.rzpt.model.bo.CandidateInfo;
import cn.rzpt.service.CandidateNumberService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "007在线智能考试系统",
        version = "v1.0",
        description = "007在线智能考试系统接口文档",
        contact = @Contact(name = "007", email = "waxjava04@163.com")
))
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
public class ExamBackendApplication {



    public static void main(String[] args) {
        SpringApplication app = new SpringApplicationBuilder(ExamBackendApplication.class).build(args);
        Environment env = app.run(args).getEnvironment();
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        log.info("--/\n---------------------------------------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}\n\t" +
                        "Swagger: \t\t{}://localhost:{}/{}\n\t" +
                        "Profile(s): \t{}" +
                        "\n---------------------------------------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                env.getProperty("server.port"),
                protocol,
                env.getProperty("server.port"),
                "doc.html",
                env.getActiveProfiles());
    }

}
