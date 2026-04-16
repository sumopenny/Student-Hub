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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传控制器
 */
@RestController
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    // 允许的图片扩展名白名单
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");

    @Autowired
    private FtpUtil ftpUtil;

    @Autowired
    private StudentService studentService;

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 校验文件扩展名是否在白名单中
     */
    private boolean isAllowedExtension(String extension) {
        return ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
    }

    /**
     * 校验文件是否为有效的图片文件（通过文件头 Magic Number）
     */
    private boolean isValidImageFile(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            byte[] header = new byte[8];
            int read = is.read(header);
            if (read < 4) {
                return false;
            }

            // 检查 JPEG: FF D8 FF
            boolean isJpeg = (header[0] == (byte) 0xFF && header[1] == (byte) 0xD8 && header[2] == (byte) 0xFF);

            // 检查 PNG: 89 50 4E 47
            boolean isPng = (header[0] == (byte) 0x89 && header[1] == (byte) 0x50
                    && header[2] == (byte) 0x4E && header[3] == (byte) 0x47);

            // 检查 GIF: GIF87a 或 GIF89a
            boolean isGif = (header[0] == (byte) 0x47 && header[1] == (byte) 0x49
                    && header[2] == (byte) 0x46 && header[3] == (byte) 0x38
                    && (header[4] == (byte) 0x37 || header[4] == (byte) 0x39)
                    && header[5] == (byte) 0x61);

            // 检查 WebP: RIFF....WEBP
            boolean isWebp = (header[0] == (byte) 0x52 && header[1] == (byte) 0x49
                    && header[2] == (byte) 0x46 && header[3] == (byte) 0x46
                    && header[8] == (byte) 0x57 && header[9] == (byte) 0x45
                    && header[10] == (byte) 0x42 && header[11] == (byte) 0x50);

            return isJpeg || isPng || isGif || isWebp;
        } catch (IOException e) {
            log.error("校验文件类型时发生异常: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 综合校验文件是否为合法图片
     */
    private Result validateImageFile(MultipartFile file) {
        // 校验文件是否为空
        if (file == null || file.isEmpty()) {
            return Result.error("上传失败：文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();

        // 校验文件扩展名
        String extension = getFileExtension(originalFilename);
        if (!isAllowedExtension(extension)) {
            log.warn("上传失败：不支持的文件扩展名，文件名={}", originalFilename);
            return Result.error("上传失败：只允许上传 jpg、jpeg、png、gif、webp 格式的图片");
        }

        // 校验 Content-Type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            log.warn("上传失败：Content-Type 不支持，contentType={}", contentType);
            return Result.error("上传失败：只允许上传图片文件");
        }

        // 校验文件大小（最大10MB）
        if (file.getSize() > 10 * 1024 * 1024) {
            log.warn("上传失败：文件过大，size={} bytes", file.getSize());
            return Result.error("上传失败：文件大小不能超过10MB");
        }

        // 校验文件头 Magic Number（防止伪造 Content-Type）
        if (!isValidImageFile(file)) {
            log.warn("上传失败：文件头校验失败，可能不是有效的图片文件，文件名={}", originalFilename);
            return Result.error("上传失败：文件内容不是有效的图片格式");
        }

        return null; // 校验通过
    }

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
            // 综合校验文件
            Result validationResult = validateImageFile(file);
            if (validationResult != null) {
                return validationResult;
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
            // 综合校验文件
            Result validationResult = validateImageFile(file);
            if (validationResult != null) {
                return validationResult;
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
