package com.example.admin.controller;

import com.example.admin.common.Result.Result;
import com.example.admin.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
@Api(tags = "角色-菜单模块")
@RestController
@RequestMapping("/roleMenu")
public class RoleMenuController {

    @Resource
    private IRoleService roleService;

    @ApiOperation(value = "通过角色ID获取所绑定的菜单")
    @GetMapping("/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId) {
        List<Integer> roleMenu = roleService.getRoleMenu(roleId);
        return Result.success(roleMenu);
    }

    @ApiOperation(value = "修改角色绑定的菜单ID集合")
    @PostMapping("/{roleId}")
    public Result setRoleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId, menuIds);
        return Result.success();
    }

}
