package cn.rzpt.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 考生实体
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "考生")
@NoArgsConstructor
@AllArgsConstructor
public class ExamUser extends BasePO implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 考生号
     */
    private String examineeNumber;

    /**
     * 校验码
     */
    private String checkCode;

    /**
     * 考生姓名
     */
    private String examineeName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 考生类别
     */
    private Integer type;


}
