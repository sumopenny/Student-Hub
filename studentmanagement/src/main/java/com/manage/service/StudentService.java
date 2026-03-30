package com.manage.service;

import com.manage.pojo.LoginInfo;
import com.manage.pojo.PageResult;
import com.manage.pojo.Student;
import com.manage.pojo.StudentVO;

import java.util.List;
import java.util.Map;

public interface StudentService {
    List<Student> findAll();//查询所有数据

    void delete(Integer id);

    void add(Student student);

    Student get(Integer id);

    Student getSimple(Integer id);

    void update(Student student);

    PageResult<StudentVO> page(Integer page, Integer pageSize,
                             String name, String address,
                             String gender, Integer age);

    LoginInfo login(Student student);

    // 修改密码
    boolean updatePassword(Integer id, String oldPassword, String newPassword);

    // 更新用户头像
    boolean updateAvatar(Integer id, String avatarUrl);

    // 根据ID获取用户头像URL
    String getAvatarById(Integer id);

    // 检查用户名是否已存在
    boolean checkUsernameExists(String username);

    // 用户注册
    boolean register(Student student);
}

