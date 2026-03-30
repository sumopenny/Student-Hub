package com.manage.controller;

import com.manage.pojo.Comment;
import com.manage.pojo.Post;
import com.manage.pojo.Result;
import com.manage.pojo.Student;
import com.manage.service.CommentService;
import com.manage.service.PostLikeService;
import com.manage.service.PostService;
import com.manage.service.StudentService;
import com.manage.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@CrossOrigin
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private StudentService studentService;

    @PostMapping
    public Result createPost(@RequestBody Post post, @RequestHeader("Authorization") String token) {
        try {
            Map<String, Object> claims = JwtUtils.parseJWT(token.replace("Bearer ", ""));
            Integer userId = (Integer) claims.get("id");
            
            Student student = studentService.get(userId);
            String userName = student != null ? student.getName() : "匿名用户";
            
            post.setUserId(userId);
            post.setUserName(userName);
            postService.createPost(post);
            
            return Result.success(post);
        } catch (Exception e) {
            return Result.error("发帖失败");
        }
    }

    @GetMapping
    public Result getAllPosts(@RequestHeader(value = "Authorization", required = false) String token) {
        List<Post> posts = postService.getAllPosts();
        
        Integer userId = null;
        if (token != null && !token.isEmpty()) {
            try {
                Map<String, Object> claims = JwtUtils.parseJWT(token.replace("Bearer ", ""));
                userId = (Integer) claims.get("id");
            } catch (Exception e) {
            }
        }
        
        for (Post post : posts) {
            boolean liked = false;
            if (userId != null) {
                liked = postLikeService.isLiked(userId, post.getId(), "post");
            }
            post.setLikeCount(postLikeService.getLikeCount(post.getId(), "post"));
        }
        
        return Result.success(posts);
    }

    @GetMapping("/{id}")
    public Result getPostById(@PathVariable Integer id) {
        Post post = postService.getPostById(id);
        return Result.success(post);
    }

    @DeleteMapping("/{id}")
    public Result deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return Result.success("删除成功");
    }

    @PostMapping("/{id}/like")
    public Result togglePostLike(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        try {
            Map<String, Object> claims = JwtUtils.parseJWT(token.replace("Bearer ", ""));
            Integer userId = (Integer) claims.get("id");
            
            boolean isLiked = postLikeService.isLiked(userId, id, "post");
            if (isLiked) {
                postLikeService.removeLike(userId, id, "post");
            } else {
                postLikeService.addLike(userId, id, "post");
            }
            
            int likeCount = postLikeService.getLikeCount(id, "post");
            postService.updateLikeCount(id, likeCount);
            
            Map<String, Object> result = new HashMap<>();
            result.put("liked", !isLiked);
            result.put("likeCount", likeCount);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("操作失败");
        }
    }

    @GetMapping("/{id}/comments")
    public Result getComments(@PathVariable Integer id) {
        List<Comment> comments = commentService.getCommentsByPostId(id);
        return Result.success(comments);
    }

    @PostMapping("/{id}/comments")
    public Result createComment(@PathVariable Integer id, @RequestBody Comment comment, @RequestHeader("Authorization") String token) {
        try {
            Map<String, Object> claims = JwtUtils.parseJWT(token.replace("Bearer ", ""));
            Integer userId = (Integer) claims.get("id");
            
            Student student = studentService.get(userId);
            String userName = student != null ? student.getName() : "匿名用户";
            
            comment.setPostId(id);
            comment.setUserId(userId);
            comment.setUserName(userName);
            commentService.createComment(comment);
            
            int commentCount = commentService.getCommentCountByPostId(id);
            postService.updateCommentCount(id, commentCount);
            
            return Result.success(comment);
        } catch (Exception e) {
            return Result.error("评论失败");
        }
    }

    @PostMapping("/comments/{commentId}/like")
    public Result toggleCommentLike(@PathVariable Integer commentId, @RequestHeader("Authorization") String token) {
        try {
            Map<String, Object> claims = JwtUtils.parseJWT(token.replace("Bearer ", ""));
            Integer userId = (Integer) claims.get("id");
            
            boolean isLiked = postLikeService.isLiked(userId, commentId, "comment");
            if (isLiked) {
                postLikeService.removeLike(userId, commentId, "comment");
            } else {
                postLikeService.addLike(userId, commentId, "comment");
            }
            
            int likeCount = postLikeService.getLikeCount(commentId, "comment");
            commentService.updateLikeCount(commentId, likeCount);
            
            Map<String, Object> result = new HashMap<>();
            result.put("liked", !isLiked);
            result.put("likeCount", likeCount);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("操作失败");
        }
    }
}
