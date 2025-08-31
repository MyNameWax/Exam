package cn.rzpt.serializer;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.rzpt.anno.SensitiveData;
import cn.rzpt.enums.SensitiveType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Arrays;

/**
 * 敏感数据序列化器
 */

public class SensitiveDataSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveType type;

    private int prefixLen;

    private int suffixLen;

    public SensitiveDataSerializer() {
    }

    public SensitiveDataSerializer(SensitiveType type, int prefixLen, int suffixLen) {
        this.type = type;
        this.prefixLen = prefixLen;
        this.suffixLen = suffixLen;
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (StrUtil.isEmpty(value)) {
            jsonGenerator.writeNull();
            return;
        }
        jsonGenerator.writeString(this.desensitize(value));
    }

    /**
     * 根据不同的数据类型进行脱敏
     *
     * @param value 数据类型
     * @return 脱敏后的具体数据
     */
    public String desensitize(String value) {
        if (value.length() <= prefixLen + suffixLen) {
            return value; // 数据长度不够,不脱敏直接返回
        }
        switch (type) {
            case EXAM_NUMBER:
                return desensitize(value, 4, 4);
            default:
                return desensitize(value, prefixLen, suffixLen);
        }
    }

    /**
     * 具体脱敏方法的实现
     *
     * @param value     需要脱敏的值
     * @param prefixLen 前缀长度
     * @param suffixLen 后缀长度
     * @return 脱敏后的数据
     */
    public String desensitize(String value, int prefixLen, int suffixLen) {
        String prefix = value.substring(0, prefixLen);
        String suffix = value.substring(value.length() - suffixLen);
        int middleLen = value.length() - prefixLen - suffixLen;
        String middle = "*".repeat(middleLen);
        return prefix + middle + suffix;

    }


    @Override
    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        if (ObjectUtil.isNotNull(property)) {
            SensitiveData annotation = property.getAnnotation(SensitiveData.class);
            if (ObjectUtil.isNull(annotation)) {
                annotation = property.getContextAnnotation(SensitiveData.class);
            }
            if (ObjectUtil.isNotNull(annotation)) {
                return new SensitiveDataSerializer(annotation.type(), annotation.prefixLen(), annotation.suffixLen());
            }
            return provider.findValueSerializer(property.getType(), property);

        }
        return provider.findNullValueSerializer(null);
    }
}
