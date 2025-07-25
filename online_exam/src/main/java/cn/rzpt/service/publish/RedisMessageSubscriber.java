package cn.rzpt.service.publish;

import cn.rzpt.constants.RedisKeyConstants;
import cn.rzpt.model.bo.RedisMessageBO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class RedisMessageSubscriber implements MessageListener {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            System.out.println(new String(message.getBody()));
            RedisMessageBO redisMessageBO = objectMapper.readValue(new String(message.getBody()), RedisMessageBO.class);
            System.out.println(redisMessageBO);
        } catch (Exception e) {
            log.error("处理消息失败,原因:{}", e.getMessage(), e);
        }
    }



    private static String convertToJson(String javaToString) {
        // 替换=为:
        String jsonLike = javaToString.replaceAll("=", "\":\"")
                .replaceAll(", ", "\", \"")
                .replaceAll("\\{", "{\"")
                .replaceAll("}", "\"}");

        // 处理特殊值
        jsonLike = jsonLike.replaceAll("\"\\{", "{")
                .replaceAll("}\"", "}");

        return jsonLike;
    }



    private void handlerMessage(String channel, String body) {
        if (RedisKeyConstants.KEY.equals(channel)) {
            // AI判题
            this.handleAiMarkQuestion(body);
        }
    }

    /**
     * 处理AI判题
     */
    private void handleAiMarkQuestion(String body) {
        //TODO AI判题
        log.info("处理AI判题,body:{}", body);
    }


}
