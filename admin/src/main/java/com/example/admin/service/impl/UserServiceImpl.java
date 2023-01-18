package com.example.admin.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.config.Result.ResultCode;
import com.example.admin.config.exception.ServiceException;
import com.example.admin.entity.Menu;
import com.example.admin.entity.User;
import com.example.admin.entity.dto.Password;
import com.example.admin.mapper.RoleMapper;
import com.example.admin.mapper.RoleMenuMapper;
import com.example.admin.mapper.UserMapper;
import com.example.admin.service.IMenuService;
import com.example.admin.service.IUserService;
import com.example.admin.util.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private IMenuService menuService;
    @Override
    public User login(User user) {
        user.setPassword(SecureUtil.md5(user.getPassword()));
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", user.getUsername()).eq("password",user.getPassword());
        User DBuser= getOne(userQueryWrapper);
        if (DBuser != null) {
             DBuser.setToken(TokenUtils.genToken(DBuser.getId().toString(),DBuser.getPassword()));
             List<Menu> roleMenus = getMenusByRoleKey(DBuser.getRole());
             DBuser.setMenus(roleMenus);
             return DBuser;
        } else {
            throw new ServiceException(ResultCode.SERVICE_ERROR, "用户名或密码错误");
        }
    }


    @Override
    public User register(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User DBuser = getOne(queryWrapper);
        if (DBuser == null) {
            user.setPassword(SecureUtil.md5(user.getPassword()));
            user.setRole("USER");
            save(user);
        } else {
            throw new ServiceException(ResultCode.SERVICE_ERROR, "注册失败，用户名重复");
        }
        return user;
    }

    @Override
    public void updatePassword(Password password) {
        password.setPassword(SecureUtil.md5(password.getPassword()));
        password.setNewPassword(SecureUtil.md5(password.getNewPassword()));
        if (userMapper.updatePassword(password) < 1) {
            throw new ServiceException(ResultCode.SERVICE_ERROR, "原密码错误,修改密码失败!");
        }
    }

    @Override
    public User findByUsername(String username){
        return userMapper.findByUsername(username);
    }

    private List<Menu> getMenusByRoleKey(String roleKey) {
        Integer roleId = roleMapper.selectByFlag(roleKey);
        List<Integer> ids = roleMenuMapper.selectMidByRid(roleId);
        List<Menu> menus = menuService.listAllMenus();
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (ids.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !ids.contains(child.getId()));
        }
        return roleMenus;
    }

}
