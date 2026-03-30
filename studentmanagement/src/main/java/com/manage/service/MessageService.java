package com.manage.service;

import com.manage.pojo.Message;

import java.util.List;

/**
 * 留言服务接口
 */
public interface MessageService {

    /**
     * 提交留言
     * @param message 留言对象
     */
    void submitMessage(Message message);

    /**
     * 获取所有留言列表
     * @return 留言列表
     */
    List<Message> getAllMessages();

    /**
     * 获取当前用户的留言列表
     * @param userId 用户ID
     * @return 留言列表
     */
    List<Message> getMessagesByUserId(Integer userId);
}
