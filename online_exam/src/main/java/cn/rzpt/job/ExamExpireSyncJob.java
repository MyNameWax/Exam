package cn.rzpt.job;

import cn.rzpt.enums.ExamStatus;
import cn.rzpt.model.po.Exam;
import cn.rzpt.service.ExamService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 考试过期定时任务
 */
@Slf4j
@Component
public class ExamExpireSyncJob {

    @Resource
    private ExamService examService;

    @Scheduled(cron = "0 0 * * * *")
    public void execute() {
        log.info("同步考试状态,当前时间:{}", LocalDateTime.now());
        LocalDateTime now = LocalDateTime.now();
        List<Exam> list = examService.lambdaQuery().list();
        // 如果结束时间 小于当前时间，则修改状态为已结束
        list.stream().filter(exam -> exam.getEndTime().isBefore(now)).forEach(exam -> {
            exam.setStatus(ExamStatus.EXPIRE.getCode());
            examService.updateById(exam);
        });
    }

}
