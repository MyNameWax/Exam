package cn.rzpt.controller.inner;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "公告", description = "公告")
@RequestMapping("/notice")
@RestController
public class SysNoticeController {
}
