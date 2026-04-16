package com.manage.service.impl;

import com.manage.mapper.PostMapper;
import com.manage.pojo.Post;
import com.manage.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    private static final int POST_INTERVAL_SECONDS = 5;

    @Override
    public Post createPost(Post post) {
        postMapper.insert(post);
        return post;
    }

    @Override
    public List<Post> getAllPosts() {
        return postMapper.selectAll();
    }

    @Override
    public Post getPostById(Integer id) {
        return postMapper.selectById(id);
    }

    @Override
    public void deletePost(Integer id) {
        postMapper.deleteById(id);
    }

    @Override
    public void updateLikeCount(Integer id, Integer count) {
        postMapper.updateLikeCount(id, count);
    }

    @Override
    public void updateCommentCount(Integer id, Integer count) {
        postMapper.updateCommentCount(id, count);
    }

    @Override
    public LocalDateTime getLastPostTimeByUserId(Integer userId) {
        return postMapper.selectLastPostTimeByUserId(userId);
    }

    public boolean canPost(Integer userId) {
        LocalDateTime lastPostTime = getLastPostTimeByUserId(userId);
        if (lastPostTime == null) {
            return true;
        }
        return lastPostTime.plusSeconds(POST_INTERVAL_SECONDS).isBefore(LocalDateTime.now());
    }

    public long getRemainingTime(Integer userId) {
        LocalDateTime lastPostTime = getLastPostTimeByUserId(userId);
        if (lastPostTime == null) {
            return 0;
        }
        LocalDateTime nextPostTime = lastPostTime.plusSeconds(POST_INTERVAL_SECONDS);
        if (nextPostTime.isAfter(LocalDateTime.now())) {
            return java.time.Duration.between(LocalDateTime.now(), nextPostTime).getSeconds();
        }
        return 0;
    }
}
