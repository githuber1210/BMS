package com.example.admin.controller;

import cn.hutool.core.date.DateUtil;
import com.example.admin.config.Result.Result;
import com.example.admin.entity.Message;
import com.example.admin.service.IMessageService;
import com.example.admin.util.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/message")
public class MessageController{

    @Resource
    private IMessageService messageService;

    @PostMapping
    public Result saveOrUpdate(@RequestBody Message message) {
        message.setUsername(TokenUtils.getCurrentUser().getUsername());
        message.setTime(DateUtil.formatDateTime(new Date()));
        return Result.success(messageService.saveOrUpdate(message));
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id) {
        return Result.success(messageService.removeById(id));
    }

    @GetMapping
    public Result list() {
        return Result.success(messageService.list());
    }

    @GetMapping("/foreign/{id}")
    public Result findByForeign(@PathVariable Integer id) {
        return Result.success(messageService.findByForeign(id));
    }




}
