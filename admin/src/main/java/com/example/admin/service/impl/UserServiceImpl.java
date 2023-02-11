package com.example.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.common.Constants;
import com.example.admin.common.Result.ResultCode;
import com.example.admin.common.exception.ServiceException;
import com.example.admin.config.redis.RedisService;
import com.example.admin.entity.Menu;
import com.example.admin.entity.User;
import com.example.admin.entity.dto.LoginUser;
import com.example.admin.entity.dto.Password;
import com.example.admin.mapper.RoleMapper;
import com.example.admin.mapper.RoleMenuMapper;
import com.example.admin.mapper.UserMapper;
import com.example.admin.service.IMenuService;
import com.example.admin.service.IUserService;
import com.example.admin.util.ServletUtils;
import com.example.admin.util.TokenUtils;
import com.example.admin.util.ip.AddressUtils;
import com.example.admin.util.ip.IpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
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
    @Resource
    private RedisService redisService;

    @Override
    public LoginUser login(LoginUser loginUser) {

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", loginUser.getUsername());

        loginUser.setPassword(SecureUtil.md5(loginUser.getPassword()));
        userQueryWrapper.eq("password",loginUser.getPassword());

        User DBuser= getOne(userQueryWrapper);

        if (DBuser != null) {
            if (!DBuser.isEnabled()) {
                throw new ServiceException(ResultCode.SERVICE_ERROR,"该用户已被禁用");
            }
            String ip = IpUtils.getIpAddr(ServletUtils.getRequest());

            DBuser.setLoginTime(new Date());
            DBuser.setLoginIp(ip);

            BeanUtil.copyProperties(DBuser,loginUser);
            updateById(DBuser);

            String loginToken = TokenUtils.genToken(DBuser.getId().toString(), DBuser.getPassword());
            List<Menu> roleMenus = getMenusByRoleKey(DBuser.getRole());


            loginUser.setToken(loginToken);
            loginUser.setMenus(roleMenus);

            UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
            loginUser.setIpaddr(ip);
            loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
            loginUser.setBrowser(userAgent.getBrowser().getName());
            loginUser.setOs(userAgent.getOperatingSystem().getName());

            redisService.set(Constants.LOGIN_TOKEN_KEY + loginToken,loginUser);

            return loginUser;
        } else {
            throw new ServiceException(ResultCode.SERVICE_ERROR, "用户名或密码错误");
        }
    }


    @Override
    public User register(User user) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", user.getUsername());
        User DBuser= getOne(userQueryWrapper);
        if (DBuser == null) {
            user.setPassword(SecureUtil.md5(user.getPassword()));
            save(user);
            return user;
        } else {
            throw new ServiceException(ResultCode.SERVICE_ERROR, "用户已存在");
        }
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

    @Override
    public Page<User> list(String username, String telephone, Integer pageSize, Integer pageNum) {
        Page<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<User> lambda = wrapper.lambda();
        lambda.like(User::getUsername,username).like(User::getTelephone,telephone);
        return page(page,wrapper);
    }

    @Override
    public List<Menu> getMenusByRoleKey(String roleKey) {
        Integer roleId = roleMapper.selectByFlag(roleKey);
        List<Integer> mids = roleMenuMapper.selectMidsByRid(roleId);
        List<Menu> menus = menuService.listAllMenus();
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (mids.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // 移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !mids.contains(child.getId()));
        }
        return roleMenus;
    }

}
