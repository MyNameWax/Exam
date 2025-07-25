package cn.rzpt.controller;

import cn.rzpt.anno.PassLogin;
import cn.rzpt.common.global.result.DataResult;
import cn.rzpt.constants.RedisKeyConstants;
import cn.rzpt.model.bo.RedisMessageBO;
import cn.rzpt.model.po.ExamQuestion;
import cn.rzpt.model.request.ExamAddRequest;
import cn.rzpt.model.response.ExamUserListResponse;
import cn.rzpt.service.ExamService;
import cn.rzpt.service.publish.RedisMessagePublisher;
import cn.rzpt.service.publish.inter.MessageCallBack;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/exam")
@AllArgsConstructor
@Tag(name = "考试管理", description = "考试管理")
public class ExamController {

    private final ExamService examService;
    private final RedisMessagePublisher redisMessagePublisher;

    @PostMapping("/v1/add")
    @Operation(summary = "添加考试")
    @Parameters({
            @Parameter(name = "ExamAddRequest", description = "考试信息", required = true),
    })
    public DataResult<Boolean> addExam(@RequestBody ExamAddRequest examAddRequest) {
        return DataResult.success(examService.addExam(examAddRequest));
    }

    @GetMapping("/v1/list")
    @Operation(summary = "考试列表")
    @Parameters({
            @Parameter(name = "status", description = "状态", required = true),
    })
    public DataResult<List<ExamUserListResponse>> examUserList(@RequestParam(value = "status", required = false) String status) {
        return DataResult.success(examService.examUserList(status));
    }

    @GetMapping("/v1/exam/question/{id}")
    @Operation(summary = "考试题目列表")
    @Parameters({
            @Parameter(name = "id", description = "考试ID", required = true),
    })
    public DataResult<List<ExamQuestion>> examQuestionList(@PathVariable String id) {
        return DataResult.success(examService.examQuestionList(id));
    }


}
