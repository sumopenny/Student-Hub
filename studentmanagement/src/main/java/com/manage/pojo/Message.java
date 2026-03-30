package com.manage.pojo;

import java.time.LocalDateTime;

/**
 * 留言实体类（不使用Lombok，手动实现getter/setter）
 */
public class Message {
    private Integer id;
    private Integer userId;
    private String userName;
    private String username;
    private String content;
    private LocalDateTime createTime;
    private String replyContent;
    private LocalDateTime replyTime;

    // 无参构造器（Spring框架解析JSON时需要）
    public Message() {
    }

    // getter方法：每个字段对应一个getXxx()
    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    // 关键：这个方法对应username字段，解决控制器中getUsername()报错问题
    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public LocalDateTime getReplyTime() {
        return replyTime;
    }

    // setter方法：每个字段对应一个setXxx()
    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public void setReplyTime(LocalDateTime replyTime) {
        this.replyTime = replyTime;
    }
}