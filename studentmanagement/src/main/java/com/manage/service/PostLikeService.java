package com.manage.service;

public interface PostLikeService {
    boolean isLiked(Integer userId, Integer targetId, String targetType);
    void addLike(Integer userId, Integer targetId, String targetType);
    void removeLike(Integer userId, Integer targetId, String targetType);
    int getLikeCount(Integer targetId, String targetType);
}
