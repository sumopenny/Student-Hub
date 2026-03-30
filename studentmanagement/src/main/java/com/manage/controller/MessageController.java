package com.manage.controller;

import com.manage.pojo.Message;
import com.manage.pojo.Result;
import com.manage.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 留言控制器
 */
@Slf4j
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 提交留言
     * @param message 留言信息
     * @return 操作结果
     */
    @PostMapping("/messages")
    public Result submitMessage(@RequestBody Message message) {
        log.info("收到留言提交请求: userId={}, userName={}, username={}",
                message.getUserId(), message.getUserName(), message.getUsername());

        if (message.getContent() == null || message.getContent().trim().isEmpty()) {
            return Result.error("留言内容不能为空");
        }

        if (message.getContent().length() > 500) {
            return Result.error("留言内容不能超过500字");
        }

        messageService.submitMessage(message);
        return Result.success("留言提交成功");
    }

    /**
     * 获取当前用户的留言列表
     * @param userId 用户ID
     * @return 留言列表
     */
    @GetMapping("/messages")
    public Result getMessages(@RequestParam(required = false) Integer userId) {
        log.info("获取留言列表请求: userId={}", userId);
        List<Message> messages;
        if (userId != null) {
            messages = messageService.getMessagesByUserId(userId);
        } else {
            messages = messageService.getAllMessages();
        }
        return Result.success(messages);
    }
}
