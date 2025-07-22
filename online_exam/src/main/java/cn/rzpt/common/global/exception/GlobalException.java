package cn.rzpt.common.global.exception;


import cn.rzpt.common.global.result.DataResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class GlobalException extends RuntimeException{

    private DataResultCodeEnum dataResultCodeEnum;

}
