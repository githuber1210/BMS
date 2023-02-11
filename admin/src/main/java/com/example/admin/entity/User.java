package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("sys_user")
@ToString
public class User implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(value = "用户ID")
    private Integer id;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "电话号码")
    private String telephone;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "用户头像")
    private String avatar;
    @ApiModelProperty(value = "用户角色")
    private String role;
    @ApiModelProperty(value = "用户创建时间")
    private Date createTime;
    @ApiModelProperty(value = "用户最后登录时间")
    private Date loginTime;
    @ApiModelProperty(value = "用户最后登入IP")
    private String loginIp;
    @ApiModelProperty(value = "用户是否启用 1启用 0停用")
    private boolean enabled;
    @ApiModelProperty(value = "用户是否删除 1删除")
    private boolean remove;
    @ApiModelProperty(value = "性别")
    private String sex;

    @TableField(exist = false)
    private String token;
    @TableField(exist = false)
    private List<Menu> menus;


}
