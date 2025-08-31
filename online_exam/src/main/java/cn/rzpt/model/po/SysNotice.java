package cn.rzpt.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "公告")
@NoArgsConstructor
@AllArgsConstructor
public class SysNotice extends BasePO{

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String title;

    private String content;

}
