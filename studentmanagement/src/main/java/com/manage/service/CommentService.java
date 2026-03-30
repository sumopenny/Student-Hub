package com.manage.service;

import com.manage.pojo.Comment;
import java.util.List;

public interface CommentService {
    Comment createComment(Comment comment);
    List<Comment> getCommentsByPostId(Integer postId);
    Comment getCommentById(Integer id);
    void updateLikeCount(Integer id, Integer count);
    void deleteComment(Integer id);
    int getCommentCountByPostId(Integer postId);
}
