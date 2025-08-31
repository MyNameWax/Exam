package cn.rzpt.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IndexExamDataStaticResponse {

    private Long ingExamCount;
    private Long endExamCount;
    private Long notStartExamCount;

}

