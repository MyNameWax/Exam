package cn.rzpt.anno;

import cn.rzpt.enums.SensitiveType;

/**
 * 脱敏数据注解,用于表示需要脱敏的字段
 */

public @interface SensitiveData {

    /**
     * 脱敏类型 默认为考试编号
     *
     * @return 脱敏类型
     */
    SensitiveType type() default SensitiveType.EXAM_NUMBER;


    /**
     * 保留前几位不脱敏
     *
     * @return 不脱敏前缀
     */
    int prefixLen() default 3;

    /**
     * 保留后几位不脱敏
     *
     * @return 不脱敏后缀
     */
    int suffixLen() default 3;


}
