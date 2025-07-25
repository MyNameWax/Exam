package cn.rzpt.config.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
@Configurable
public class ThreadPoolConfig {


    private static final int CPU_CORE = Runtime.getRuntime().availableProcessors();



    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        log.info("ThreadPoolTaskExecutor init...");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CPU_CORE + 1);
        executor.setMaxPoolSize(CPU_CORE * 2);
        executor.setKeepAliveSeconds(60);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("thread-pool-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;


    }

}
