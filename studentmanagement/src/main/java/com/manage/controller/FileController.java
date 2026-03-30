package com.manage.controller;

import com.manage.pojo.Result;
import com.manage.service.StudentService;
import com.manage.utils.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 */
@RestController
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FtpUtil ftpUtil;

    @Autowired
    private StudentService studentService;

    /**
     * 文件上传接口
     *
     * @param file 上传的文件
     * @return 上传结果，包含文件访问URL
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        log.info("收到文件上传请求，文件名: {}", file != null ? file.getOriginalFilename() : "null");

        try {
            // 校验文件类型（只允许图片）
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                log.warn("上传失败：文件类型不支持，contentType={}", contentType);
                return Result.error("上传失败：只允许上传图片文件（jpg、png、gif等）");
            }

            // 校验文件大小（最大10MB）
            if (file.getSize() > 10 * 1024 * 1024) {
                log.warn("上传失败：文件过大，size={} bytes", file.getSize());
                return Result.error("上传失败：文件大小不能超过10MB");
            }

            // 上传文件到FTP
            String fileUrl = ftpUtil.uploadFile(file);

            // 返回文件URL
            Map<String, String> data = new HashMap<>();
            data.put("url", fileUrl);
            data.put("filename", file.getOriginalFilename());

            log.info("文件上传接口处理成功");
            return Result.success(data);

        } catch (RuntimeException e) {
            // 捕获FTP工具类抛出的运行时异常
            log.error("文件上传失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 捕获其他未知异常
            log.error("文件上传发生未知异常: {}", e.getMessage(), e);
            return Result.error("上传失败：系统异常，请稍后重试");
        }
    }

    /**
     * 头像上传接口 - 上传头像并更新用户头像URL，同时删除旧头像
     *
     * @param file 上传的头像文件
     * @param userId 用户ID
     * @return 上传结果，包含文件访问URL
     */
    @PostMapping("/upload/avatar")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file,
                               @RequestParam("userId") Integer userId) {
        log.info("收到头像上传请求，文件名: {}, 用户ID: {}",
                file != null ? file.getOriginalFilename() : "null", userId);

        if (userId == null) {
            return Result.error("上传失败：用户ID不能为空");
        }

        try {
            // 校验文件类型（只允许图片）
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                log.warn("上传失败：文件类型不支持，contentType={}", contentType);
                return Result.error("上传失败：只允许上传图片文件（jpg、png、gif等）");
            }

            // 校验文件大小（最大10MB）
            if (file.getSize() > 10 * 1024 * 1024) {
                log.warn("上传失败：文件过大，size={} bytes", file.getSize());
                return Result.error("上传失败：文件大小不能超过10MB");
            }

            // 获取旧头像URL
            String oldAvatarUrl = studentService.getAvatarById(userId);
            log.info("用户旧头像URL: {}", oldAvatarUrl);

            // 上传新文件到FTP
            String fileUrl = ftpUtil.uploadFile(file);
            log.info("新头像上传成功，URL: {}", fileUrl);

            // 更新数据库中的头像URL
            boolean updateSuccess = studentService.updateAvatar(userId, fileUrl);
            if (!updateSuccess) {
                // 如果数据库更新失败，删除刚上传的文件
                ftpUtil.deleteFile(fileUrl);
                log.error("头像上传失败：数据库更新失败，用户ID={}", userId);
                return Result.error("上传失败：更新用户信息失败");
            }

            // 数据库更新成功后，删除旧头像文件
            if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
                boolean deleteSuccess = ftpUtil.deleteFile(oldAvatarUrl);
                if (deleteSuccess) {
                    log.info("旧头像删除成功: {}", oldAvatarUrl);
                } else {
                    log.warn("旧头像删除失败或文件不存在: {}", oldAvatarUrl);
                }
            }

            // 返回文件URL
            Map<String, String> data = new HashMap<>();
            data.put("url", fileUrl);
            data.put("filename", file.getOriginalFilename());

            log.info("头像上传接口处理成功，用户ID={}", userId);
            return Result.success(data);

        } catch (RuntimeException e) {
            // 捕获FTP工具类抛出的运行时异常
            log.error("头像上传失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 捕获其他未知异常
            log.error("头像上传发生未知异常: {}", e.getMessage(), e);
            return Result.error("上传失败：系统异常，请稍后重试");
        }
    }
}
