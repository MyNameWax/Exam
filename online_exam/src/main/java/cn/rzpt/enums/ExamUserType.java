package cn.rzpt.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExamUserType {
    REGULAR(0, "普通考生"),
    ART(1, "艺术类"),
    SPORTS(2, "体育类"),
    SPECIAL(3, "特殊政策");

    private final int code;
    private final String desc;


}
