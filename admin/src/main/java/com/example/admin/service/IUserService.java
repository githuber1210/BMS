package com.example.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.User;
import com.example.admin.entity.dto.Password;

public interface IUserService extends IService<User> {

    User login(User user);

    User register(User user);

    void updatePassword(Password password);

    User findByUsername(String username);

}
