package com.manage.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.manage.mapper.StudentMapper;
import com.manage.pojo.LoginInfo;
import com.manage.pojo.PageResult;
import com.manage.pojo.Student;
import com.manage.pojo.StudentVO;
import com.manage.service.StudentService;
import com.manage.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service//运行时会自动创建对象，并注入IOC容器中
public class StudentServiceImpl implements StudentService {
    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);
    @Autowired//还有构造函数注入
    private StudentMapper studentMapper;
    @Override
    public List<Student> findAll() {
        return studentMapper.findAll();
    }

    @Transactional(rollbackFor = Exception.class)//事务处理，所有异常都回滚
    @Override
    public void delete(Integer id) {
        studentMapper.delete(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)//新启一个事务
    @Override
    public void add(Student student) {
        studentMapper.add(student);
    }

    @Override
    public Student get(Integer id) {
        return studentMapper.get(id);
    }

    @Override
    public Student getSimple(Integer id) {
        return studentMapper.getSimple(id);
    }

    @Transactional
    @Override
    public void update(Student student) {
        studentMapper.update(student);
    }

    @Override
    public PageResult<StudentVO> page(Integer page, Integer pageSize,
                                    String name, String address,
                                    String gender, Integer age) {
        // 开启分页
        PageHelper.startPage(page, pageSize);
        // 执行查询
        List<StudentVO> students = studentMapper.findPage(name, address, gender, age);
        // 获取分页信息
        Page<StudentVO> p = (Page<StudentVO>) students;
        // 返回分页结果
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    //TODO 密码加密
    @Override
    public LoginInfo login(Student student) {
        //对密码进行MD5加密
        student.setPassword(DigestUtil.md5Hex(student.getPassword()));
        //查询数据库
        Student s = studentMapper.selectbyusernameandpassword(student);

        if(s != null){
            //1. 生成JWT令牌，
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("id", s.getId());
            dataMap.put("username", s.getUsername());

            String jwt = JwtUtils.generateJwt(dataMap);
            LoginInfo loginInfo = new LoginInfo(
                s.getId(), 
                s.getUsername(), 
                s.getName(), 
                s.getAvatar(), 
                jwt,
                s.getGender(),
                s.getAge(),
                s.getAddress(),
                s.getPhone()
            );
            return loginInfo;
        }
        return null;

    }

    @Override
    public boolean updatePassword(Integer id, String oldPassword, String newPassword) {
        // 对旧密码进行MD5加密后验证
        String encryptedOldPassword = DigestUtil.md5Hex(oldPassword);
        Student student = studentMapper.selectByIdAndPassword(id, encryptedOldPassword);
        if (student == null) {
            log.info("密码修改失败：用户ID={}, 旧密码不正确", id);
            return false;
        }
        
        // 对新密码进行MD5加密后更新
        String encryptedNewPassword = DigestUtil.md5Hex(newPassword);
        int rows = studentMapper.updatePassword(id, encryptedNewPassword);
        if (rows > 0) {
            log.info("密码修改成功：用户ID={}", id);
            return true;
        }
        
        log.info("密码修改失败：用户ID={}", id);
        return false;
    }

    @Override
    public boolean updateAvatar(Integer id, String avatarUrl) {
        int rows = studentMapper.updateAvatar(id, avatarUrl);
        if (rows > 0) {
            log.info("头像更新成功：用户ID={}", id);
            return true;
        }
        log.info("头像更新失败：用户ID={}", id);
        return false;
    }

    @Override
    public String getAvatarById(Integer id) {
        return studentMapper.getAvatarById(id);
    }

    @Override
    public boolean checkUsernameExists(String username) {
        Student student = studentMapper.selectByUsername(username);
        return student != null;
    }

    @Transactional
    @Override
    public boolean register(Student student) {
        // 检查用户名是否已存在
        if (checkUsernameExists(student.getUsername())) {
            log.info("注册失败：用户名已存在，username={}", student.getUsername());
            return false;
        }
        
        // 执行注册
        student.setPassword(DigestUtil.md5Hex(student.getPassword()));
        
        int rows = studentMapper.register(student);
        if (rows > 0) {
            log.info("注册成功：username={}", student.getUsername());
            return true;
        }
        
        log.info("注册失败：username={}", student.getUsername());
        return false;
    }
}
