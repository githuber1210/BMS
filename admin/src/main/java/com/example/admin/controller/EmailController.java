package com.example.admin.controller;

import com.example.admin.common.Result.Result;
import com.example.admin.common.Result.ResultCode;
import com.example.admin.common.exception.ServiceException;
import com.example.admin.config.redis.RedisService;
import com.example.admin.util.MailServiceUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
@Api(tags = "邮箱模块")
@RestController
@RequestMapping("/email")
public class EmailController {

    @Resource
    private MailServiceUtils mailServiceUtils;

    @Resource
    private RedisService redisService;

    @ApiOperation("发送邮箱")
    @PostMapping("/send")
    public Result email(@RequestBody String receiveEmail) {
        try {
            //生成6位随机数
            String Captcha = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
            //发送邮箱
            mailServiceUtils.sendMail("3353115002@qq.com", receiveEmail, "验证码", Captcha);
            //redis保存验证码
            redisService.set(receiveEmail, Captcha,30);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.SERVICE_ERROR,"发送失败！");
        }
        return Result.success("发送成功");
    }

    @ApiOperation("验证邮箱验证码")
    @PostMapping("/validate")
    public Result validate(@RequestParam String receiveEmail,
                           @RequestParam String captcha) {
        //根据邮箱帐号取出验证码
        String redisCaptcha = (String) redisService.get(receiveEmail);
        if (redisCaptcha.equals(captcha)){
            redisService.delete(receiveEmail);
            return Result.success("验证成功");
        }
        return Result.fail(ResultCode.SERVICE_ERROR,"验证失败");
    }

}