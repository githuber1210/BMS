package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.Menu;

import java.util.List;


public interface IMenuService extends IService<Menu> {


    List<Menu> listAllMenus();

    void saveMenu(Menu menu);

    void updateMenu(Menu menu);
}
