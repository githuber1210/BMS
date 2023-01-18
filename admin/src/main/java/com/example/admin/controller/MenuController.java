package com.example.admin.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.admin.config.Result.Result;
import com.example.admin.entity.Icons;
import com.example.admin.entity.Menu;
import com.example.admin.mapper.IconsMapper;
import com.example.admin.service.IMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.stream.Stream;


@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @Resource
    private IconsMapper iconsMapper;

    @PostMapping
    public Result saveOrUpdate(@RequestBody Menu menu) {
        menu.setTime(DateUtil.now());
        menuService.saveOrUpdate(menu);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result removeById(@PathVariable Integer id) {
        menuService.removeById(id);
        return Result.success();
    }

    @GetMapping("/ids")
    public Result listIds() {
        Stream<Integer> ids = menuService.list().stream().map(Menu::getId);
        return Result.success(ids);
    }

    @GetMapping
    public Result listMenus(){
        return Result.success(menuService.listAllMenus());
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(menuService.getById(id));
    }

    @GetMapping("/icons")
    public Result getIcons() {
        QueryWrapper<Icons> iconsQueryWrapper = new QueryWrapper<>();
        return Result.success(iconsMapper.selectList(iconsQueryWrapper));
    }

}

