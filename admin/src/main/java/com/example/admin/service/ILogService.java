package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.Log;

public interface ILogService extends IService<Log> {

    void log(String content,String username);
}
