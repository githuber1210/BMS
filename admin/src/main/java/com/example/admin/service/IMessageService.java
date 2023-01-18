package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.Message;

import java.util.List;

public interface IMessageService extends IService<Message> {


    List<Message> findByForeign(Integer id);
}
