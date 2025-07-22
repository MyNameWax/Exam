package cn.rzpt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExamStatus {

    NOT_START(0, "未开始"),
    CAN_JOIN(1, "可参加"),
    FINISH(2, "已完成"),
    EXPIRE(3, "已过期");

    private final int code;
    private final String desc;
}
