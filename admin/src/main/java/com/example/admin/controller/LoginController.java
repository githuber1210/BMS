package com.example.admin.controller;

import com.example.admin.common.Constants;
import com.example.admin.common.Result.Result;
import com.example.admin.config.redis.RedisService;
import com.example.admin.entity.User;
import com.example.admin.entity.dto.LoginUser;
import com.example.admin.entity.dto.Password;
import com.example.admin.service.ILogService;
import com.example.admin.service.IUserService;
import com.example.admin.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Api(tags = "登录模块")
@RestController
public class LoginController {


    @Resource
    private IUserService userService;

    @Resource
    private ILogService logService;

    @Resource
    private RedisService redisService;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginUser loginUser) {
        logService.log(Constants.LOGIN,loginUser.getUsername());
        LoginUser login = userService.login(loginUser);
        return Result.success(login);
    }

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        logService.log(Constants.REGISTER,user.getUsername());
        userService.register(user);
        return Result.success();
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("/password")
    public Result resetPsw(@RequestBody Password password) {
        userService.updatePassword(password);
        return Result.success();
    }

    @ApiOperation(value = "获取所有在线用户")
    @GetMapping("/online")
    public Result list()
    {
        Collection<String> keys = redisService.getKeysByPattern(Constants.LOGIN_TOKEN_KEY + "*");
        List<LoginUser> userOnlineList = new ArrayList<>();
        for (String key : keys)
        {
            LoginUser user = (LoginUser) redisService.get(key);
            userOnlineList.add(user);
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return Result.success(userOnlineList);
    }

    @ApiOperation(value = "登出系统")
    @DeleteMapping ("/logout/{token}")
    public Result logout(@PathVariable String token) {
        String username = TokenUtils.getCurrentUser().getUsername();
        logService.log(Constants.LOGOUT, username);
        redisService.delete(Constants.LOGIN_TOKEN_KEY + token);
        return Result.success();
    }

    @ApiOperation(value = "根据标识强退用户")
    @DeleteMapping("/break/{token}")
    public Result forceLogout(@PathVariable String token)
    {
        redisService.delete(Constants.LOGIN_TOKEN_KEY + token);
        return Result.success();
    }

}
