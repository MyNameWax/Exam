package cn.rzpt.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 每年考试的相应加分政策
 */
@Getter
@AllArgsConstructor
public enum ExamUserType {
    REGULAR(0, "普通考生"),
    ART(1, "艺术类"),
    SPORTS(2, "体育类"),
    SPECIAL(3, "特殊政策");

    private final Integer code;
    private final String desc;


    public static String getByCode(int code) {
        for (ExamUserType value : ExamUserType.values()) {
            if (value.code == code) {
                return value.getDesc();
            }
        }
        return null;
    }

}
