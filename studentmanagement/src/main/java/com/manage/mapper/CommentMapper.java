package com.manage.mapper;

import com.manage.pojo.Comment;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO comment (post_id, user_id, user_name, content, parent_id, reply_user_id, reply_user_name, create_time, like_count) " +
            "VALUES (#{postId}, #{userId}, #{userName}, #{content}, #{parentId}, #{replyUserId}, #{replyUserName}, NOW(), 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Comment comment);

    @Select("SELECT c.*, s.avatar as user_avatar FROM comment c " +
            "LEFT JOIN student s ON c.user_id = s.id " +
            "WHERE c.post_id = #{postId} ORDER BY c.create_time ASC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "postId", column = "post_id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "userName", column = "user_name"),
        @Result(property = "userAvatar", column = "user_avatar"),
        @Result(property = "content", column = "content"),
        @Result(property = "parentId", column = "parent_id"),
        @Result(property = "replyUserId", column = "reply_user_id"),
        @Result(property = "replyUserName", column = "reply_user_name"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "likeCount", column = "like_count")
    })
    List<Comment> selectByPostId(Integer postId);

    @Select("SELECT c.*, s.avatar as user_avatar FROM comment c " +
            "LEFT JOIN student s ON c.user_id = s.id " +
            "WHERE c.id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "postId", column = "post_id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "userName", column = "user_name"),
        @Result(property = "userAvatar", column = "user_avatar"),
        @Result(property = "content", column = "content"),
        @Result(property = "parentId", column = "parent_id"),
        @Result(property = "replyUserId", column = "reply_user_id"),
        @Result(property = "replyUserName", column = "reply_user_name"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "likeCount", column = "like_count")
    })
    Comment selectById(Integer id);

    @Update("UPDATE comment SET like_count = #{likeCount} WHERE id = #{id}")
    int updateLikeCount(@Param("id") Integer id, @Param("likeCount") Integer likeCount);

    @Delete("DELETE FROM comment WHERE id = #{id}")
    int deleteById(Integer id);

    @Select("SELECT COUNT(*) FROM comment WHERE post_id = #{postId}")
    int countByPostId(Integer postId);
}
