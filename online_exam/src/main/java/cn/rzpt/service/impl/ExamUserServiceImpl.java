package cn.rzpt.service.impl;

import cn.rzpt.mapper.ExamUserMapper;
import cn.rzpt.model.po.ExamUser;
import cn.rzpt.service.ExamUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExamUserServiceImpl extends ServiceImpl<ExamUserMapper, ExamUser> implements ExamUserService {
}
