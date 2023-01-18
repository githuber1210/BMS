package com.example.admin.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.config.Result.Result;
import com.example.admin.entity.User;
import com.example.admin.entity.dto.Password;
import com.example.admin.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        return Result.success(userService.login(user));
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        return Result.success(userService.register(user));
    }

    @PostMapping
    public Result saveOrUpdate(@RequestBody User user) {
        if(StrUtil.isBlank(user.getPassword())) {
            user.setPassword(SecureUtil.md5(user.getUsername()));
        }
        return Result.success(userService.saveOrUpdate(user));
    }

    @DeleteMapping("/{id}")
    public Result removeById(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result removeByIds(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids));
    }

    @PostMapping("/password")
    public Result resetPsw(@RequestBody Password password) {
        userService.updatePassword(password);
        return Result.success();
    }

    @GetMapping
    public Result list() {
        return Result.success(userService.list());
    }

    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        return Result.success(userService.findByUsername(username));
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/page")
    public Result page(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                       @RequestParam(defaultValue = "") String username,
                       @RequestParam(defaultValue = "") String telephone) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like("username",username).like("telephone",telephone);
        return Result.success(userService.page(new Page<>(pageNum,pageSize),userQueryWrapper));
    }

    @GetMapping("/export")
    public void excelExport(HttpServletResponse response) throws Exception {
        List<User> list = userService.list();

        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

}

