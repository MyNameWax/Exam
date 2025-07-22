package cn.rzpt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExamType {

    NORMAL("normal", "常规考试"),
    QUIZ("quiz", "小测验"),
    CERT("cert", "认证考试"),
    FINAL("final", "期末考试");


    private final String type;

    private final String desc;

}
