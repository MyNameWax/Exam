package cn.rzpt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExamMarkDifferentEnums {

    EASY(1,"预约严格程度很松的"),
    NORMAL(2,"阅卷严格程度一般的"),
    HARD(3,"阅卷非常严格的");

    private final int code;
    private final String desc;

    // 根据code获取枚举
    public static ExamMarkDifferentEnums getByCode(int code) {
        for (ExamMarkDifferentEnums value : ExamMarkDifferentEnums.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
