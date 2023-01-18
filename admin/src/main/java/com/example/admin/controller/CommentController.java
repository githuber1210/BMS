package com.example.admin.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.config.Result.Result;
import com.example.admin.entity.Comment;
import com.example.admin.service.ICommentService;
import com.example.admin.util.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private ICommentService commentService;

    @PostMapping
    public Result saveOrUpdate(@RequestBody Comment comment) {
        if (comment.getId() == null) {
            comment.setUid(TokenUtils.getCurrentUser().getId());
            comment.setTime(DateUtil.now());
            if (comment.getPid() != null) {
                Integer pid = comment.getPid();
                Comment pComment = commentService.getById(pid);
                if (pComment.getPpid() != null) {
                    comment.setPpid(pComment.getPpid());
                } else {
                    comment.setPpid(comment.getPid());
                }
            }
        }
        commentService.saveOrUpdate(comment);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result removeById(@PathVariable Integer id) {
        commentService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result removeByIds(@RequestBody List<Integer> ids) {
        commentService.removeByIds(ids);
        return Result.success();
    }


    @GetMapping
    public Result list() {
        return Result.success(commentService.list());
    }

    @GetMapping("/tree/{blogId}")
    public Result listTree(@PathVariable Integer blogId) {
        List<Comment> commentList = commentService.findCommentDetail(blogId);
        List<Comment> originList = commentList.stream()
                .filter(comment -> comment.getPpid() == null)
                .collect(Collectors.toList());
        // 设置评论数据的子节点，也就是回复内容
        for (Comment origin : originList) {
            // 表示回复对象集合
            List<Comment> comments = commentList.stream()
                    .filter(comment -> origin.getId().equals(comment.getPpid()))
                    .collect(Collectors.toList());
            comments.forEach(comment -> {
                // 找到当前评论的父级
                Optional<Comment> pComment = commentList.stream()
                        .filter(c1 -> c1.getId().equals(comment.getPid()))
                        .findFirst();
                pComment.ifPresent((v -> {
                    comment.setPUid(v.getUid());
                    comment.setPUname(v.getUsername());
                }));
            });
            origin.setChildren(comments);
        }
        return Result.success(originList);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(commentService.getById(id));
    }

    @GetMapping("/page")
    public Result page(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(commentService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

