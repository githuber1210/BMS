package com.example.admin.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.admin.config.redis.RedisService;
import com.example.admin.entity.User;
import com.example.admin.service.IUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenUtils {


    @Resource
    private RedisService redisService;

    @Resource
    public  IUserService userService;

    public static IUserService staticUserService;

    @PostConstruct
    public void setStaticUserService(){
        staticUserService = userService;
    }

    public static String genToken(String userId,String sign){
       return JWT.create()
                   .withAudience(userId)
               .withExpiresAt(DateUtil.offsetHour(new Date(),2))
               .sign(Algorithm.HMAC256(sign));
    }

    public static User getCurrentUser(){
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();

            String token = request.getHeader("token");

            if (StrUtil.isNotBlank(token)){
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(userId);
        }}catch (Exception e){
            return null;
        }
        return null;
    }
}