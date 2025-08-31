package cn.rzpt.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.rzpt.anno.SensitiveData;
import cn.rzpt.serializer.SensitiveDataSerializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * 脱敏注解解析器
 */
public class SensitiveAnnotationFilter extends JacksonAnnotationIntrospector {


    @Override
    public Object findSerializer(Annotated annotated) {
        SensitiveData sensitiveData = annotated.getAnnotation(SensitiveData.class);
        if (ObjectUtil.isNotNull(sensitiveData)) {
            return SensitiveDataSerializer.class;
        }
        return super.findSerializer(annotated);
    }
}
