package com.manage.service.impl;

import com.manage.mapper.PostLikeMapper;
import com.manage.pojo.PostLike;
import com.manage.service.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    @Autowired
    private PostLikeMapper postLikeMapper;

    @Override
    public boolean isLiked(Integer userId, Integer targetId, String targetType) {
        PostLike like = postLikeMapper.selectByUserAndTarget(userId, targetId, targetType);
        return like != null;
    }

    @Override
    public void addLike(Integer userId, Integer targetId, String targetType) {
        PostLike postLike = new PostLike();
        postLike.setUserId(userId);
        postLike.setTargetId(targetId);
        postLike.setTargetType(targetType);
        postLikeMapper.insert(postLike);
    }

    @Override
    public void removeLike(Integer userId, Integer targetId, String targetType) {
        postLikeMapper.deleteByUserAndTarget(userId, targetId, targetType);
    }

    @Override
    public int getLikeCount(Integer targetId, String targetType) {
        return postLikeMapper.countByTarget(targetId, targetType);
    }
}
