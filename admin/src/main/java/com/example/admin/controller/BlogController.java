package com.example.admin.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.config.Result.Result;
import com.example.admin.entity.Blog;
import com.example.admin.service.IBlogService;
import com.example.admin.util.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private IBlogService blogService;

    @PostMapping
    public Result saveOrUpdate(@RequestBody Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(DateUtil.now());
            blog.setAuthor(TokenUtils.getCurrentUser().getUsername());
        }
        blogService.saveOrUpdate(blog);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result removeById(@PathVariable Integer id) {
        blogService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result removeByIds(@RequestBody List<Integer> ids) {
        blogService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findByTimeRange(@RequestParam(required = false) String start, @RequestParam(required = false) String end) {
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        blogQueryWrapper.ge("create_time", start).le("create_time", end);
        return Result.success(blogService.list(blogQueryWrapper));
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(blogService.getById(id));
    }

    @GetMapping("/page")
    public Result page(@RequestParam String title, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", title);
        return Result.success(blogService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

