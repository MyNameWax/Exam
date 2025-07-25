package cn.rzpt.config.redis;


import cn.rzpt.constants.RedisKeyConstants;
import cn.rzpt.service.publish.RedisMessageSubscriber;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Configurable
@Component
public class RedisPubSubConfig {


    @Resource
    private RedisMessageSubscriber redisMessageSubscriber;


    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {

        log.info("RedisMessageListenerContainer init...");
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        // 订阅多个频道

        container.addMessageListener(
                redisMessageSubscriber,
                new ChannelTopic(RedisKeyConstants.KEY)
        );


        return container;

    }

}
