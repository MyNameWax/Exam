package cn.rzpt.controller.outer;

import cn.rzpt.common.global.result.DataResult;
import cn.rzpt.model.request.ExamSubmitRequest;
import cn.rzpt.model.response.ExamDetailVO;
import cn.rzpt.model.response.ExamScoreResponseVO;
import cn.rzpt.service.ExamineeExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/examinee-exam")
@AllArgsConstructor
@Tag(name = "考生考试管理", description = "考生考试管理")
public class ExamineeExamController {

    private final ExamineeExamService examineeExamService;

    @Operation(summary = "开始考试")
    @PostMapping("/v1/{id}/start")
    public DataResult<String> createExamRecord(@PathVariable String id) {
        return DataResult.success(examineeExamService.createExamRecord(id));
    }


    @Operation(summary = "考试题目")
    @PostMapping("/v1/exam/{examId}")
    public DataResult<ExamDetailVO> getExamDetails(@PathVariable String examId) {
        return DataResult.success(examineeExamService.getExamDetails(examId));
    }


    @PostMapping("/v1/submit-answer")
    @Operation(summary = "提交答案")
    public DataResult<Boolean> submitAnswers(@RequestBody ExamSubmitRequest examSubmitRequest) {
        return DataResult.success(examineeExamService.submitAnswers(examSubmitRequest));
    }

    @GetMapping("/v1/cheat/ping")
    @Operation(summary = "作弊检测")
    public DataResult<Integer> cheatPing(String examId) {
        return DataResult.success(examineeExamService.cheatPing(examId));
    }

    @GetMapping("/v1/examinee-exam/result")
    @Operation(summary = "考试结果")
    public DataResult<ExamScoreResponseVO> examineeExamResult(String examId) {
        return DataResult.success(examineeExamService.examineeExamResult(examId));
    }

}
