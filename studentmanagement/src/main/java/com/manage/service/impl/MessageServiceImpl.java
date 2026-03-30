package com.manage.service.impl;

import com.manage.mapper.MessageMapper;
import com.manage.pojo.Message;
import com.manage.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 留言服务实现类
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void submitMessage(Message message) {
        message.setCreateTime(LocalDateTime.now());
        messageMapper.insert(message);
        log.info("用户 {} 提交了留言", message.getUsername());
    }

    @Override
    public List<Message> getAllMessages() {
        return messageMapper.selectAll();
    }

    @Override
    public List<Message> getMessagesByUserId(Integer userId) {
        return messageMapper.selectByUserId(userId);
    }
}
