package com.manage.controller;

import com.manage.pojo.LoginInfo;
import com.manage.pojo.Result;
import com.manage.pojo.Student;
import com.manage.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private StudentService studentService;

    @PostMapping("/login")
    public Result login(@RequestBody Student student){
        log.info("用户登录 , username={} ", student.getUsername());// 打印用户名和姓名
        LoginInfo loginInfo = studentService.login(student);
        if(loginInfo != null){
            return Result.success(loginInfo);
        }
        return Result.error("用户名或密码错误~");
    }

    @PostMapping("/register")
    public Result register(@RequestBody Student student){
        log.info("新用户注册 , username={}", student.getUsername());
        
        // 参数校验
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            return Result.error("姓名不能为空");
        }
        if (student.getGender() == null || student.getGender().trim().isEmpty()) {
            return Result.error("性别不能为空");
        }
        if (student.getUsername() == null || student.getUsername().trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (student.getPassword() == null || student.getPassword().trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        
        // 执行注册
        boolean success = studentService.register(student);
        if (success) {
            return Result.success("注册成功");
        } else {
            return Result.error("用户名已存在");
        }
    }

    @GetMapping("/checkUsername")
    public Result checkUsername(@RequestParam String username){
        log.info("检查用户名是否已存在 , username={}", username);
        
        if (username == null || username.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        
        boolean exists = studentService.checkUsernameExists(username);
        Map<String, Boolean> data = new HashMap<>();
        data.put("exists", exists);
        return Result.success(data);
    }
}
