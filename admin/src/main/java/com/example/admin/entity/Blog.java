package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("blog")
public class Blog {
      @TableId(value = "id",type = IdType.AUTO)
      private Integer id;
      private String title;
      private String content;
      private String author;
      private String createTime;


}
