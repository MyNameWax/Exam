package cn.rzpt.adapter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页响应")
public class PageAdapter<T> {

    @Schema(description = "每页数量",example = "10")
    private Integer recordsFiltered;

    @Schema(description = "总页数",example = "10")
    private Long recordsTotal;

    @Schema(description = "页码",example = "1")
    private Integer draw;

    @Schema(description = "是否成功",example = "true")
    private Boolean success;

    @Schema(description = "数据",example = "[]")
    private List<T> data;

    @Schema(description = "错误信息",example = "操作成功")
    private Object error;
}
