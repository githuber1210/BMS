package com.example.admin.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.common.Result.Result;
import com.example.admin.entity.User;
import com.example.admin.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@Api(tags = "用户模块")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @ApiOperation(value = "添加用户")
    @PostMapping
    public Result save(@RequestBody User user) {
        if(StrUtil.isBlank(user.getPassword())) {
            user.setPassword(SecureUtil.md5(user.getUsername()));
        }
        return Result.success(userService.save(user));
    }

    @ApiOperation(value = "修改用户信息")
    @PutMapping
    public Result update(@RequestBody User user) {
        if(StrUtil.isBlank(user.getPassword())) {
            user.setPassword(SecureUtil.md5(user.getUsername()));
        }
        userService.updateById(user);
        return Result.success();
    }

    @ApiOperation(value = "删除用户", notes="根据单个ID删除用户")
    @DeleteMapping("/{id}")
    public Result removeById(@PathVariable Integer id) {
        userService.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "批量删除用户", notes="根据ID集合批量删除用户")
    @PostMapping("/del/batch")
    public Result removeByIds(@RequestBody List<Integer> ids) {
        userService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "获取所有用户信息")
    @GetMapping
    public Result list() {
        List<User> userList = userService.list();
        return Result.success(userList);
    }

    @ApiOperation(value = "通过用户名获取用户信息")
    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return Result.success(user);
    }

    @ApiOperation(value = "通过用户ID获取用户信息")
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public Result page(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                       @RequestParam(defaultValue = "") String username,
                       @RequestParam(defaultValue = "") String telephone) {
        Page<User> userList = userService.list(username,telephone,pageSize, pageNum);
        return Result.success(userList);
    }

    @ApiOperation("将用户信息导出EXCEL")
    @GetMapping("/export")
    public void excelExport(HttpServletResponse response) throws Exception {
        List<User> list = userService.list();

        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        String fileName = URLEncoder.encode("用户信息表", "UTF-8");

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    @ApiOperation("将用户信息EXCEL表导入")
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<User> userList = reader.readAll(User.class);
        userService.saveBatch(userList);
        return Result.success();
    }
}

