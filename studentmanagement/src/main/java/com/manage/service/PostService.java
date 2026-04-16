package com.manage.service;

import com.manage.pojo.Post;
import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
    Post createPost(Post post);
    List<Post> getAllPosts();
    Post getPostById(Integer id);
    void deletePost(Integer id);
    void updateLikeCount(Integer id, Integer count);
    void updateCommentCount(Integer id, Integer count);
    LocalDateTime getLastPostTimeByUserId(Integer userId);
    boolean canPost(Integer userId);
    long getRemainingTime(Integer userId);
}
