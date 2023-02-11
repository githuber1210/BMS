package com.example.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.Notice;

import java.util.List;


public interface INoticeService extends IService<Notice> {

    List<Notice> list(String start, String end);

    Page<Notice> list(String title, Integer pageNum, Integer pageSize);
}
