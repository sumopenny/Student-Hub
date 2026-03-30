package com.manage.mapper;

import com.manage.pojo.Post;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface PostMapper {

    @Insert("INSERT INTO post (user_id, user_name, title, content, images, create_time, like_count, comment_count) " +
            "VALUES (#{userId}, #{userName}, #{title}, #{content}, #{images}, NOW(), 0, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Post post);

    @Select("SELECT p.*, s.avatar as user_avatar FROM post p " +
            "LEFT JOIN student s ON p.user_id = s.id " +
            "ORDER BY p.create_time DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "userName", column = "user_name"),
        @Result(property = "userAvatar", column = "user_avatar"),
        @Result(property = "title", column = "title"),
        @Result(property = "content", column = "content"),
        @Result(property = "images", column = "images"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "likeCount", column = "like_count"),
        @Result(property = "commentCount", column = "comment_count")
    })
    List<Post> selectAll();

    @Select("SELECT p.*, s.avatar as user_avatar FROM post p " +
            "LEFT JOIN student s ON p.user_id = s.id " +
            "WHERE p.id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "userName", column = "user_name"),
        @Result(property = "userAvatar", column = "user_avatar"),
        @Result(property = "title", column = "title"),
        @Result(property = "content", column = "content"),
        @Result(property = "images", column = "images"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "likeCount", column = "like_count"),
        @Result(property = "commentCount", column = "comment_count")
    })
    Post selectById(Integer id);

    @Update("UPDATE post SET like_count = #{likeCount} WHERE id = #{id}")
    int updateLikeCount(@Param("id") Integer id, @Param("likeCount") Integer likeCount);

    @Update("UPDATE post SET comment_count = #{commentCount} WHERE id = #{id}")
    int updateCommentCount(@Param("id") Integer id, @Param("commentCount") Integer commentCount);

    @Delete("DELETE FROM post WHERE id = #{id}")
    int deleteById(Integer id);
}
