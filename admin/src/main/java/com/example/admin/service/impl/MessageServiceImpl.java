package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.entity.Message;
import com.example.admin.entity.User;
import com.example.admin.mapper.MessageMapper;
import com.example.admin.mapper.UserMapper;
import com.example.admin.service.IMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public List<Message> findByForeign(Integer foreignId) {
        // 根据 foreignId 0 查询所有的留言数据
        LambdaQueryWrapper<Message> queryWrapper = Wrappers.<Message>lambdaQuery().eq(com.example.admin.entity.Message::getForeignId, foreignId).orderByDesc(com.example.admin.entity.Message::getId);
        List<Message> list = messageMapper.selectList(queryWrapper);
        // 循环所有留言数据
        for (Message Message : list) {
            User one = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, Message.getUsername()));
            Message.setAvatar(one.getAvatar());
            Long parentId = Message.getParentId();
            // 判断当前的留言是否有父级，如果有，则返回父级留言的信息
            // 原理：遍历所有留言数据，如果id跟当前留言信息的parentId相等，则将其设置为父级评论信息，也就是Message::setParentMessage
            list.stream().filter(c -> c.getId().equals(parentId)).findFirst().ifPresent(Message::setParentMessage);
        }
        return list;
    }
}
