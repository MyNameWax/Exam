package cn.rzpt.constants;

public interface SystemConstants {

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
