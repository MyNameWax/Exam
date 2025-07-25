package cn.rzpt.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;


/**
 * Redis消息体模型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisMessageBO implements Serializable {

    private String messageId;

    private String topic;

    private String content;

    private Long timestamp;

    private String tag;

    private Map<String, Object> properties;

}
