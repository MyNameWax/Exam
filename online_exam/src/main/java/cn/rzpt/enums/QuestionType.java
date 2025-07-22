package cn.rzpt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionType {

    SINGLE(1,"单选"),
    MULTIPLE(2,"多选"),
    JUDGE(3,"判断"),
    FILL(4,"填空"),
    ESSAY(5,"简答");

    private final int code;

    private final String desc;

}
