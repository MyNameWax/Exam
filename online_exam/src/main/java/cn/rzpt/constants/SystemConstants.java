package cn.rzpt.constants;

public interface SystemConstants {


    interface ExamTypeStatusConstants {

        /**
         * 未开始
         */
        Integer NOT_START = 0;

        /**
         * 可参加
         */
        Integer CAN_JOIN = 1;

        /**
         * 已完成
         */
        Integer FINISH = 2;

        /**
         * 已过期
         */
        Integer EXPIRE = 3;

        String SUCCESS = "success";

        String PRIMARY = "primary";

        String ERROR = "error";

        String WARNING = "warning";
    }

    interface ExamUserLoginConstants {

        /**
         * 考试是否生成Token（是）
         */
        String YES = "YES";
        /**
         * 考试是否生成Token（否）
         */
        String NO = "NO";

    }

    /**
     * 考试成绩相关
     */
    interface DefaultScoreConstants {
        /**
         * 考试默认分数
         */
        Double ZERO = 0.0D;
        /**
         * 成绩公开
         */
        Integer PUBLIC = 1;
        /**
         * 成绩不公开
         */
        Integer PRIVATE = 2;

    }

    /**
     * 阅卷相关
     */
    class ExamMarkConstants {


        public static String generatorMessage(String type, String question, String answer,Double score) {
            return String.format("您是一名%s阅卷老师,题目是:%s,答案是:%s,您需要根据这个题目给这个答案来进行评分,满分为:%f" +
                            "您需要返回给JSON格式的数据,分别返回score分数和reason原因。其中分数的数据类型是double,原因请用中文来说明。"
                    , type, question, answer,score);

        }

    }


    /**
     * 考试难度
     */
    interface ExamDifferentConstants {

        /**
         * 考试模式（严格）
         */
        Integer STRICT = 1;

        /**
         * 考试模式（宽松）
         */
        Integer EASING = 2;

        /**
         * 考试模式（混合）
         */
        Integer MIXED = 3;

    }

}
