package com.manage.service.impl;

import com.manage.mapper.PostMapper;
import com.manage.pojo.Post;
import com.manage.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

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
}
