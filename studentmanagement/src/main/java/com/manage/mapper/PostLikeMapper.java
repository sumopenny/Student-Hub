package com.manage.mapper;

import com.manage.pojo.PostLike;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PostLikeMapper {

    @Insert("INSERT INTO post_like (user_id, target_id, target_type, create_time) " +
            "VALUES (#{userId}, #{targetId}, #{targetType}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PostLike postLike);

    @Select("SELECT * FROM post_like WHERE user_id = #{userId} AND target_id = #{targetId} AND target_type = #{targetType}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "targetId", column = "target_id"),
        @Result(property = "targetType", column = "target_type"),
        @Result(property = "createTime", column = "create_time")
    })
    PostLike selectByUserAndTarget(@Param("userId") Integer userId, @Param("targetId") Integer targetId, @Param("targetType") String targetType);

    @Delete("DELETE FROM post_like WHERE user_id = #{userId} AND target_id = #{targetId} AND target_type = #{targetType}")
    int deleteByUserAndTarget(@Param("userId") Integer userId, @Param("targetId") Integer targetId, @Param("targetType") String targetType);

    @Select("SELECT COUNT(*) FROM post_like WHERE target_id = #{targetId} AND target_type = #{targetType}")
    int countByTarget(@Param("targetId") Integer targetId, @Param("targetType") String targetType);
}
