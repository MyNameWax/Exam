package cn.rzpt.controller.inner;

import cn.rzpt.common.global.result.DataResult;
import cn.rzpt.model.response.IndexExamCalendarResponse;
import cn.rzpt.model.response.IndexExamDataStaticResponse;
import cn.rzpt.model.response.IndexWellStartExamList;
import cn.rzpt.service.IndexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "首页", description = "首页")
@RequestMapping("/index")
@RestController
public class IndexController {


    @Resource
    private IndexService indexService;


    @Operation(summary = "考试数据统计")
    @GetMapping("/v1/exam_data_static")
    public DataResult<IndexExamDataStaticResponse> examDataStatic() {
        return DataResult.success(indexService.examDataStatic());
    }

    @Operation(summary = "即将开始考试的列表")
    @GetMapping("/v1/well_start_exam_list")
    public DataResult<List<IndexWellStartExamList>> wellStartExamList() {
        return DataResult.success(indexService.wellStartExamList());
    }

    @Operation(summary = "考试日历列表")
    @GetMapping("/v1/exam_calendar_list_data")
    public DataResult<Map<String, List<IndexExamCalendarResponse>>> examCalendarListData(@RequestParam String date) {
        return DataResult.success(indexService.examCalendarListData(date));
    }


}
