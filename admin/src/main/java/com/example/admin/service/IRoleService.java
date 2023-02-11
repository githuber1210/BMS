package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.Role;

import java.util.List;


public interface IRoleService extends IService<Role> {


    void saveRole(Role role);

    void updateRole(Role role);

    List<Integer> getRoleMenu(Integer roleId);

    void setRoleMenu(Integer roleId, List<Integer> menuIds);


}
