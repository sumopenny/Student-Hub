package com.manage.utils;

import com.manage.config.FtpProperties;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * FTP文件上传工具类
 */
@Component
public class FtpUtil {

    private static final Logger log = LoggerFactory.getLogger(FtpUtil.class);

    @Autowired
    private FtpProperties ftpProperties;

    /**
     * 上传文件到FTP服务器
     *
     * @param file 上传的文件
     * @return 上传后的文件访问URL
     * @throws RuntimeException 上传失败时抛出异常，包含详细错误信息
     */
    public String uploadFile(MultipartFile file) {
        // 检查文件是否为空
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("上传失败：文件为空");
        }

        String originalFilename = file.getOriginalFilename();
        log.info("开始上传文件: {}", originalFilename);

        // 生成唯一文件名，避免重名覆盖
        String fileExtension = getFileExtension(originalFilename);
        String newFileName = UUID.randomUUID().toString().replace("-", "") + fileExtension;
        log.info("生成新文件名: {}", newFileName);

        FTPClient ftpClient = new FTPClient();

        try {
            // 连接FTP服务器
            connectFtpServer(ftpClient);

            // 切换到指定目录
            changeWorkingDirectory(ftpClient);

            // 设置文件传输模式
            setTransferMode(ftpClient);

            // 上传文件
            uploadFileToFtp(file, ftpClient, newFileName);

            // 构建文件访问URL
            String fileUrl = buildFileUrl(newFileName);
            log.info("文件上传成功，访问URL: {}", fileUrl);

            return fileUrl;

        } catch (IOException e) {
            log.error("FTP文件上传IO异常: {}", e.getMessage(), e);
            throw new RuntimeException("上传失败：FTP服务器连接或传输异常 - " + e.getMessage());
        } finally {
            // 关闭FTP连接
            closeFtpConnection(ftpClient);
        }
    }

    /**
     * 连接FTP服务器
     */
    private void connectFtpServer(FTPClient ftpClient) throws IOException {
        log.info("正在连接FTP服务器: {}:{}", ftpProperties.getHost(), ftpProperties.getPort());

        ftpClient.setConnectTimeout(ftpProperties.getTimeout());
        ftpClient.connect(ftpProperties.getHost(), ftpProperties.getPort());

        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            throw new IOException("FTP服务器拒绝连接，响应码: " + replyCode);
        }

        boolean loginSuccess = ftpClient.login(ftpProperties.getUsername(), ftpProperties.getPassword());
        if (!loginSuccess) {
            throw new IOException("FTP登录失败，请检查用户名和密码");
        }

        log.info("FTP服务器连接并登录成功");
    }

    /**
     * 切换工作目录
     * FTP用户sumo的根目录已设置为/www/wwwroot/ftp，所以只需切换到根目录即可
     */
    private void changeWorkingDirectory(FTPClient ftpClient) throws IOException {
        // FTP用户sumo的根目录就是/www/wwwroot/ftp
        // 直接切换到根目录，使用"/"或"."表示当前目录
        boolean changeResult = ftpClient.changeWorkingDirectory("/");

        if (!changeResult) {
            // 如果切换到根目录失败，尝试获取当前工作目录
            String currentDir = ftpClient.printWorkingDirectory();
            log.info("FTP当前工作目录: {}", currentDir);

            // 如果当前目录已经是目标目录，不需要切换
            if (currentDir != null && currentDir.contains("/www/wwwroot/ftp")) {
                log.info("已在目标目录下，无需切换");
                return;
            }

            throw new IOException("无法切换到FTP根目录，当前目录: " + currentDir);
        }

        log.info("已切换到FTP根目录");
    }

    /**
     * 设置文件传输模式
     */
    private void setTransferMode(FTPClient ftpClient) throws IOException {
        // 设置被动模式
        if (ftpProperties.getPassivemode()) {
            ftpClient.enterLocalPassiveMode();
            log.info("已设置FTP被动模式");
        }

        // 设置文件类型为二进制
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    }

    /**
     * 上传文件到FTP
     */
    private void uploadFileToFtp(MultipartFile file, FTPClient ftpClient, String newFileName) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            boolean storeResult = ftpClient.storeFile(newFileName, inputStream);
            if (!storeResult) {
                String replyString = ftpClient.getReplyString();
                throw new IOException("FTP服务器存储文件失败，服务器响应: " + replyString);
            }
            log.info("文件存储成功: {}", newFileName);
        }
    }

    /**
     * 关闭FTP连接
     */
    private void closeFtpConnection(FTPClient ftpClient) {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
                log.info("FTP连接已关闭");
            } catch (IOException e) {
                log.error("关闭FTP连接异常: {}", e.getMessage());
            }
        }
    }

    /**
     * 构建文件访问URL
     * Nginx配置: location /ftp/ { alias /www/wwwroot/ftp/; }
     */
    private String buildFileUrl(String fileName) {
        // 通过HTTP访问FTP目录下的文件，添加 /ftp/ 前缀匹配Nginx配置
        return "http://" + ftpProperties.getHost() + "/ftp/" + fileName;
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    /**
     * 从FTP服务器删除文件
     *
     * @param fileUrl 文件的完整URL
     * @return 删除成功返回true，失败返回false
     */
    public boolean deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            log.warn("删除文件失败：文件URL为空");
            return false;
        }

        // 从URL中提取文件名
        String fileName = extractFileNameFromUrl(fileUrl);
        if (fileName == null || fileName.isEmpty()) {
            log.warn("删除文件失败：无法从URL提取文件名，URL={}", fileUrl);
            return false;
        }

        FTPClient ftpClient = new FTPClient();

        try {
            // 连接FTP服务器
            connectFtpServer(ftpClient);

            // 切换到指定目录
            changeWorkingDirectory(ftpClient);

            // 删除文件
            boolean deleteResult = ftpClient.deleteFile(fileName);

            if (deleteResult) {
                log.info("文件删除成功: {}", fileName);
                return true;
            } else {
                log.warn("文件删除失败，文件可能不存在: {}", fileName);
                return false;
            }

        } catch (IOException e) {
            log.error("FTP文件删除IO异常: {}", e.getMessage(), e);
            return false;
        } finally {
            // 关闭FTP连接
            closeFtpConnection(ftpClient);
        }
    }

    /**
     * 从文件URL中提取文件名
     */
    private String extractFileNameFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return null;
        }

        // URL格式: http://host/ftp/filename
        int lastSlashIndex = fileUrl.lastIndexOf('/');
        if (lastSlashIndex == -1 || lastSlashIndex == fileUrl.length() - 1) {
            return null;
        }

        return fileUrl.substring(lastSlashIndex + 1);
    }
}
