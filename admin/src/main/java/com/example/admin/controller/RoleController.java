package com.example.admin.controller;

import com.example.admin.common.Result.Result;
import com.example.admin.entity.Role;
import com.example.admin.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Api(tags = "角色模块")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    @ApiOperation(value = "添加角色")
    @PostMapping
    public Result save(@RequestBody Role role) {
        roleService.saveRole(role);
        return Result.success();
    }

    @ApiOperation(value = "修改角色信息")
    @PutMapping
    public Result update(@RequestBody Role role){
        roleService.updateRole(role);
        return Result.success();
    }

    @ApiOperation(value = "通过角色ID删除角色")
    @DeleteMapping("/{id}")
    public Result removeById(@PathVariable Integer id) {
        roleService.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "列出所有角色")
    @GetMapping
    public Result list() {
        List<Role> roleList = roleService.list();
        return Result.success(roleList);
    }
}

