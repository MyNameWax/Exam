package cn.rzpt.util;


import cn.rzpt.common.global.exception.GlobalException;
import cn.rzpt.common.global.result.DataResultCodeEnum;

/**
 * 异常抛出工具类
 */
public class ThrowUtil {


    /**
     * 条件成立则抛出异常
     * @param condition 条件
     * @param runtimeException 异常
     */
    public static void throwIf(boolean condition,RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }

    }


    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param errorCode 错误代码
     */
    public static void throwIf(boolean condition, DataResultCodeEnum errorCode) {
        throwIf(condition, new GlobalException(errorCode));
    }



}
