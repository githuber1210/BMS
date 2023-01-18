package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.Comment;

import java.util.List;

public interface ICommentService extends IService<Comment> {

    List<Comment> findCommentDetail(Integer articleId);
}
