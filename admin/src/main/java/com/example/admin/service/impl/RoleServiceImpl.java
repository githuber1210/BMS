package com.example.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.entity.Menu;
import com.example.admin.entity.Role;
import com.example.admin.entity.RoleMenu;
import com.example.admin.mapper.RoleMapper;
import com.example.admin.mapper.RoleMenuMapper;
import com.example.admin.service.IMenuService;
import com.example.admin.service.IRoleService;
import com.example.admin.util.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Resource
    private RoleMapper roleMapper;


    @Override
    public void saveRole(Role role) {
        role.setCreateTime(DateUtil.now());
        role.setCreateBy(TokenUtils.getCurrentUser().getUsername());
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(Role role) {
        role.setUpdateTime(DateUtil.now());
        role.setUpdateBy(TokenUtils.getCurrentUser().getUsername());
        roleMapper.updateById(role);
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectMidsByRid(roleId);
    }

    @Transactional
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rid", roleId);
        roleMenuMapper.delete(queryWrapper);
        List<Integer> menuIdsCopy = CollUtil.newArrayList(menuIds);
        for (Integer menuId : menuIds) {
            Menu menu = menuService.getById(menuId);
            // 二级菜单 && 传过来的menuIds没有pid
            if (menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())) {
                // 补pid
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRid(roleId);
                roleMenu.setMid(menu.getPid());
                roleMenuMapper.insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRid(roleId);
            roleMenu.setMid(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }



}
