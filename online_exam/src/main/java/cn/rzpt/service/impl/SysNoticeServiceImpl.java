package cn.rzpt.service.impl;

import cn.rzpt.mapper.SysNoticeMapper;
import cn.rzpt.model.po.SysNotice;
import cn.rzpt.service.SysNoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {
}
