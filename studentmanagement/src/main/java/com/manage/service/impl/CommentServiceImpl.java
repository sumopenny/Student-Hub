package com.manage.service.impl;

import com.manage.mapper.CommentMapper;
import com.manage.pojo.Comment;
import com.manage.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Comment createComment(Comment comment) {
        commentMapper.insert(comment);
        return comment;
    }

    @Override
    public List<Comment> getCommentsByPostId(Integer postId) {
        return commentMapper.selectByPostId(postId);
    }

    @Override
    public Comment getCommentById(Integer id) {
        return commentMapper.selectById(id);
    }

    @Override
    public void updateLikeCount(Integer id, Integer count) {
        commentMapper.updateLikeCount(id, count);
    }

    @Override
    public void deleteComment(Integer id) {
        commentMapper.deleteById(id);
    }

    @Override
    public int getCommentCountByPostId(Integer postId) {
        return commentMapper.countByPostId(postId);
    }
}
