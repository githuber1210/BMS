package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("icons")
public class Icons {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String iconName;
}
