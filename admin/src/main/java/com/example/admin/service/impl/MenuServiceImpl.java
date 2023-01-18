package com.example.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.entity.Menu;
import com.example.admin.mapper.MenuMapper;
import com.example.admin.service.IMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Override
    public List<Menu> listAllMenus() {
        List<Menu> menuList = list();
        List<Menu> collect = list().stream()
                                    .filter(menu -> menu.getPid() == null)
                                    .collect(Collectors.toList());
        for (Menu menu : collect) {
            menu.setChildren(menuList.stream().filter(menu1 -> menu.getId().equals(menu1.getPid())).collect(Collectors.toList()));
        }
        return collect;
    }
}
