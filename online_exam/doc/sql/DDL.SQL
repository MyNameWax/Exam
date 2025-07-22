CREATE TABLE exam_user
(
    `id`              VARCHAR(36) PRIMARY KEY NOT NULL COMMENT 'ID',
    `examinee_number` VARCHAR(16)             NOT NULL COMMENT '考生号',
    `check_code`      VARCHAR(64)             NOT NULL COMMENT '校验码',
    `examinee_name`   VARCHAR(30)             NOT NULL COMMENT '考生姓名',
    `phone`           VARCHAR(11)             NOT NULL COMMENT '手机号',
    `id_card`         VARCHAR(18)             NOT NULL COMMENT '身份证号',
    `address`         VARCHAR(256)            NOT NULL COMMENT '家庭住址',
    `sex`             TINYINT                 NOT NULL COMMENT '性别（0:男 1:女）',
    `type`            TINYINT                 NOT NULL COMMENT '考生类型（0:普通考生 1:艺术 2:体育 3:特殊政策）',
    `create_by`       BIGINT                  NOT NULL COMMENT '创建人',
    `create_time`     DATETIME                NOT NULL COMMENT '创建时间',
    `update_by`       BIGINT                  NOT NULL COMMENT '修改人',
    `update_time`     DATETIME                NOT NULL COMMENT '修改时间',
    `del_flag`        TINYINT                 NOT NULL DEFAULT '0' COMMENT '是否删除(0: 未删除 1:已删除)'
) COMMENT '考生表';

CREATE TABLE `exam`
(
    `id`          VARCHAR(36)  NOT NULL COMMENT '考试ID',
    `title`       varchar(100) NOT NULL COMMENT '考试标题',
    `description` text COMMENT '考试描述',
    `type`        varchar(10)           DEFAULT 'normal' COMMENT '考试类型',
    `start_time`  datetime     NOT NULL COMMENT '开始时间',
    `end_time`    datetime     NOT NULL COMMENT '结束时间',
    `duration`    int          NOT NULL COMMENT '考试时长(分钟)',
    `total_score` double NOT NULL COMMENT '总分',
    `pass_score` double DEFAULT NULL COMMENT '及格分数',
    `status`      tinyint               DEFAULT '0' COMMENT '状态(0-未开始,1-可参加,2-已完成,3已过期)',
    `create_by`   BIGINT       NOT NULL COMMENT '创建人',
    `create_time` DATETIME     NOT NULL COMMENT '创建时间',
    `update_by`   BIGINT       NOT NULL COMMENT '修改人',
    `update_time` DATETIME     NOT NULL COMMENT '修改时间',
    `del_flag`    TINYINT      NOT NULL DEFAULT '0' COMMENT '是否删除(0: 未删除 1:已删除)',
    PRIMARY KEY (`id`),
    KEY           `idx_time` (`create_time`,`update_time`),
    KEY           `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试信息表';


CREATE TABLE `exam_question`
(
    `id`            VARCHAR(36) NOT NULL COMMENT '题目ID',
    `exam_id`       VARCHAR(36) NOT NULL COMMENT '所属考试ID',
    `question_type` TINYINT     NOT NULL COMMENT '题目类型',
    `content`       text        NOT NULL COMMENT '题目内容',
    `options`       text COMMENT '选项(JSON格式)',
    `answer`        text        NOT NULL COMMENT '正确答案',
    `score`         DOUBLE         NOT NULL COMMENT '题目分值',
    `sort`          DOUBLE                  DEFAULT '0' COMMENT '排序',
    `create_by`     VARCHAR(36) NOT NULL COMMENT '创建人',
    `create_time`   DATETIME    NOT NULL COMMENT '创建时间',
    `update_by`     VARCHAR(36) NOT NULL COMMENT '修改人',
    `update_time`   DATETIME    NOT NULL COMMENT '修改时间',
    `del_flag`      TINYINT     NOT NULL DEFAULT '0' COMMENT '是否删除(0: 未删除 1:已删除)',
    PRIMARY KEY (`id`),
    KEY             `idx_exam_id` ( `exam_id` )
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '考试题目表';