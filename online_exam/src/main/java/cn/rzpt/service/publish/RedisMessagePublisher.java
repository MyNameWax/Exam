package cn.rzpt.service.publish;

import cn.rzpt.common.global.exception.GlobalException;
import cn.rzpt.common.global.result.DataResultCodeEnum;
import cn.rzpt.model.bo.RedisMessageBO;
import cn.rzpt.service.publish.inter.MessageCallBack;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class RedisMessagePublisher {


    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private final Gson gson = new Gson();

    private final Map<String, MessageCallBack> callBackRegister = new ConcurrentHashMap<>();


    public void publish(String channel, RedisMessageBO messageBO, MessageCallBack messageCallBack) {

        try {
            String correlationId = UUID.randomUUID().toString().replaceAll("-", "");
            messageBO.setTag(correlationId);
            redisTemplate.convertAndSend(channel, messageBO);
            log.info("发送消息成功：{},message:{}", channel, gson.toJson(messageBO));
        } catch (Exception e) {
            log.error("发送消息失败：{},message:{}", channel, e.getMessage(), e);
        }

    }

    /**
     * 回调结果
     */
    public void handleResult(String correlationId, boolean success, RedisMessageBO messageBO) {
        MessageCallBack callBack = callBackRegister.remove(correlationId);
        if (null != callBack) {
            if (success) {
                callBack.onSuccess(messageBO.toString());
            } else {
                callBack.onError(new GlobalException(DataResultCodeEnum.FAIL));
            }
        }
    }


}
