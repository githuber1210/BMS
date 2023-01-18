package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;


@Data
@TableName("comment")
public class Comment{
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String content;
    private Integer uid;
    private Integer pid;
    private Integer ppid;
    private Integer bid;
    private String time;
    @TableField(exist = false)
    private String pUname;
    @TableField(exist = false)
    private Integer pUid;
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String avatar;
    @TableField(exist = false)
    private List<Comment> children;


}
