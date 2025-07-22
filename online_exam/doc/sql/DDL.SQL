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

