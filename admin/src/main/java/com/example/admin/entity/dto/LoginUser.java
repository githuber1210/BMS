package com.example.admin.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.admin.entity.Menu;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data

public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String username;
    private String password;
    private Date loginTime;
    private String loginIp;
    private String role;
    private String browser;
    private String Ipaddr;
    private String loginLocation;
    private String Os;
    private Long expireTime;

    @TableField(exist = false)
    private String token;
    @TableField(exist = false)
    private List<Menu> menus;


}
