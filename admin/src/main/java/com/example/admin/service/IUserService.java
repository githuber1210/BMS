package com.example.admin.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.Menu;
import com.example.admin.entity.User;
import com.example.admin.entity.dto.LoginUser;
import com.example.admin.entity.dto.Password;

import java.util.List;

public interface IUserService extends IService<User> {

    LoginUser login(LoginUser loginUser);

    User register(User user);

    void updatePassword(Password password);

    User findByUsername(String username);

    Page<User> list(String username, String password, Integer pageSize, Integer pageNum);

    List<Menu> getMenusByRoleKey(String roleKey);
}
