package com.example.admin.controller;

import com.example.admin.common.Result.Result;
import com.example.admin.entity.domain.Server;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "服务器模块")
@RestController
@RequestMapping("/server")
public class ServerController
{
    @ApiOperation(value = "获取服务器信息")
    @GetMapping
    public Result getInfo() throws Exception
    {
        Server server = new Server();
        server.copyTo();
        return Result.success(server);
    }
}
