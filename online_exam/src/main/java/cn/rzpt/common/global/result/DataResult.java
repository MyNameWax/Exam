package cn.rzpt.common.global.result;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "公共返回数据")
public class DataResult<T> {

    @Schema(description = "状态码",example = "200")
    private Integer code;

    @Schema(description = "数据",example = "null")
    private T data;

    @Schema(description = "信息",example = "操作成功")
    private String message;

    public static <T> DataResult<T> success(T data) {
        DataResult<T> objectDataResult = new DataResult<>();
        objectDataResult.data = data;
        objectDataResult.code = DataResultCodeEnum.SUCCESS.getCode();
        objectDataResult.message = DataResultCodeEnum.SUCCESS.getMessage();
        return objectDataResult;
    }

    public static <T> DataResult<T> fail() {
        DataResult<T> objectDataResult = new DataResult<>();
        objectDataResult.data = null;
        objectDataResult.code = DataResultCodeEnum.FAIL.getCode();
        objectDataResult.message = DataResultCodeEnum.FAIL.getMessage();
        return objectDataResult;
    }


    public static <T> DataResult<T> fail(Integer code,String message) {
        DataResult<T> objectDataResult = new DataResult<>();
        objectDataResult.data = null;
        objectDataResult.code = code;
        objectDataResult.message = message;
        return objectDataResult;
    }

    public static <T> DataResult<T> fail(DataResultCodeEnum dataResultCodeEnum) {
        DataResult<T> objectDataResult = new DataResult<>();
        objectDataResult.data = null;
        objectDataResult.code = dataResultCodeEnum.getCode();
        objectDataResult.message = dataResultCodeEnum.getMessage();
        return objectDataResult;
    }

}
