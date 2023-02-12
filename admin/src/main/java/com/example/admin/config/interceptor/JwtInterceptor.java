package com.example.admin.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.admin.common.Constants;
import com.example.admin.common.Result.ResultCode;
import com.example.admin.common.exception.ServiceException;
import com.example.admin.config.redis.RedisService;
import com.example.admin.entity.User;
import com.example.admin.service.IUserService;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private IUserService userService;

    @Resource
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String token = request.getHeader("token");

        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        if (StrUtil.isBlank(token)) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "无token，请重新登录");
        }


        if (redisService.get(Constants.LOGIN_TOKEN_KEY + token) == null){
            throw new ServiceException(ResultCode.UNAUTHORIZED, "无token，请重新登录");
        }
        // userid验证
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException jwtDecodeException) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "token验证失败，请重新登录");
        }

        User user = userService.getById(userId);
        if (user == null) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "用户不存在，请重新登录");
        }
        //密码加签
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            //token验证
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, "token验证失败，请重新登录");
        }
        return true;
    }
}
