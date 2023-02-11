package com.example.admin.controller;


import com.example.admin.common.Result.Result;
import com.example.admin.entity.Menu;
import com.example.admin.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Stream;

@Api(tags = "菜单模块")
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;


    @ApiOperation(value = "添加菜单")
    @PostMapping
    public Result save(@RequestBody Menu menu) {
        menuService.saveMenu(menu);
        return Result.success();
    }

    @ApiOperation(value = "修改菜单信息")
    @PutMapping
    public Result update(@RequestBody Menu menu) {
        menuService.updateMenu(menu);
        return Result.success();
    }

    @ApiOperation(value = "根据菜单ID删除菜单")
    @DeleteMapping("/{id}")
    public Result removeById(@PathVariable Integer id) {
        menuService.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "列出所有菜单ID")
    @GetMapping("/ids")
    public Result listIds() {
        Stream<Integer> ids = menuService.list().stream().map(Menu::getId);
        return Result.success(ids);
    }

    @ApiOperation(value = "列出所有菜单")
    @GetMapping
    public Result listMenus(){
        List<Menu> menuList = menuService.listAllMenus();
        return Result.success(menuList);
    }

}

