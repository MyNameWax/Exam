package cn.rzpt.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BasePO{


    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String username;

    private String password;

    private String realname;

    private String phone;

    private String idCard;

    private String address;

    private Integer sex;


}
