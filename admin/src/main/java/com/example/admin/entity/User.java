package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("user")
@ToString
public class User implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String telephone;
    private String avatar;
    private String role;
    private Date createTime;
    @TableField(exist = false)
    private String token;
    @TableField(exist = false)
    private List<Menu> menus;





}
