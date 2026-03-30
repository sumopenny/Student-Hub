package com.manage.mapper;

import com.manage.pojo.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 留言数据访问层
 */
@Mapper
public interface MessageMapper {

    /**
     * 新增留言
     * @param message 留言对象
     */
    @Insert("INSERT INTO message (user_id, user_name, username, content, create_time) " +
            "VALUES (#{userId}, #{userName}, #{username}, #{content}, #{createTime})")
    void insert(Message message);

    /**
     * 查询所有留言列表（按创建时间倒序）
     * @return 留言列表
     */
    @Select("SELECT id, user_id AS userId, user_name AS userName, username, content, " +
            "create_time AS createTime, reply_content AS replyContent, reply_time AS replyTime " +
            "FROM message ORDER BY create_time DESC")
    List<Message> selectAll();

    /**
     * 查询当前用户的留言列表（按创建时间倒序）
     * @param userId 用户ID
     * @return 留言列表
     */
    @Select("SELECT id, user_id AS userId, user_name AS userName, username, content, " +
            "create_time AS createTime, reply_content AS replyContent, reply_time AS replyTime " +
            "FROM message WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Message> selectByUserId(@Param("userId") Integer userId);
}
