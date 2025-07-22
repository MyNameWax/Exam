
package cn.rzpt.controller;

import cn.rzpt.common.global.result.DataResult;
import cn.rzpt.model.po.ExamQuestion;
import cn.rzpt.model.request.ExamQuestionAddRequest;
import cn.rzpt.service.ExamQuestionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exam/question")
@AllArgsConstructor
@Tag(name = "题目管理", description = "题目管理")
public class ExamQuestionController {

    private final ExamQuestionService examQuestionService;

    @PostMapping("/v1/add")
    @Operation(summary = "新增题目")
    @Parameters({
            @Parameter(name = "ExamQuestionAddRequest", description = "考试题目", required = true),
    })
    public DataResult<Boolean> addExamQuestion(@RequestBody ExamQuestionAddRequest question) {
        return DataResult.success(examQuestionService.addExamQuestion(question));
    }

    @PostMapping("/v1/del/{id}")
    @Operation(summary = "删除题目")
    @Parameters({
            @Parameter(name = "id", description = "题目ID", required = true),
    })
    public DataResult<Boolean> deleteExamQuestion(@PathVariable String id) {
        examQuestionService.removeById(id);
        return DataResult.success(true);
    }

    @GetMapping("/v1/{id}")
    @Operation(summary = "获取题目")
    @Parameters({
            @Parameter(name = "id", description = "题目ID", required = true),
    })
    public DataResult<ExamQuestion> getQuestion(@PathVariable String id) {
        return DataResult.success(examQuestionService.getById(id));
    }

    @GetMapping("/v1/page")
    @Operation(summary = "题目分页列表")
    @Parameters({
            @Parameter(name = "current", description = "当前页", required = true),
            @Parameter(name = "size", description = "每页数量", required = true),
    })
    public DataResult<IPage<ExamQuestion>> page(@RequestParam(defaultValue = "10") String pageSize,
                                                @RequestParam(defaultValue = "1") String pageNumber,
                                                ExamQuestion examQuestion) {
        return DataResult.success(examQuestionService.pageList(pageSize,pageNumber,examQuestion));
    }

}
