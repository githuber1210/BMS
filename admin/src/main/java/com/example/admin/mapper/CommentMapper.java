package com.example.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.admin.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface CommentMapper extends BaseMapper<Comment> {

    @Select("select comment.*,user.username,user.avatar " +
            "from comment left join user on comment.uid = user.id " +
            "where comment.bid = #{blogId} " +
            "order by id desc")
    List<Comment> findCommentDetail(@Param("blogId") Integer blogId);

}
