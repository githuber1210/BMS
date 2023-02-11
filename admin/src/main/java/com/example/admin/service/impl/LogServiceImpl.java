package com.example.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.entity.Log;
import com.example.admin.mapper.LogMapper;
import com.example.admin.service.ILogService;
import com.example.admin.util.ServletUtils;
import com.example.admin.util.ip.IpUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    @Resource
    private LogMapper logMapper;

    @Override
    public void log(String content,String username) {
        Log log = new Log();
        log.setUser(username);
        log.setTime(DateUtil.formatDateTime(new Date()));
        log.setIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        log.setContent(content);
        logMapper.insert(log);
    }



}
