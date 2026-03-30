package com.manage.mapper;

import com.manage.pojo.Student;
import com.manage.pojo.StudentVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    @Select("SELECT * FROM student ORDER BY id DESC")
    List<Student> findAll();

    @Delete("DELETE FROM student WHERE id=#{id}")
    void delete(Integer id);

    @Insert("INSERT INTO student(name,gender,age,address,phone) VALUES (#{name}, #{gender}, #{age}, #{address}, #{phone})")
    void add(Student student);

    @Select("SELECT * FROM student WHERE id=#{id}")
    Student get(Integer id);

    @Select("SELECT id, avatar, username, name, gender, age, address, phone FROM student WHERE id=#{id}")
    Student getSimple(Integer id);

    @Update("UPDATE student SET name=#{name}, gender=#{gender}, age=#{age}, address=#{address}, phone=#{phone} WHERE id=#{id}")
    void update(Student student);

    @Select("<script>" +
            "SELECT id, avatar, username, name, gender, age, address, phone FROM student " +
            "<where>" +
            "   <if test='name != null and name != \"\"'>AND name LIKE CONCAT('%', #{name}, '%')</if>" +
            "   <if test='address != null and address != \"\"'>AND address LIKE CONCAT('%', #{address}, '%')</if>" +
            "   <if test='gender != null and gender != \"\"'>AND gender = #{gender}</if>" +
            "   <if test='age != null'>AND age = #{age}</if>" +
            "</where>" +
            "ORDER BY id DESC" +
            "</script>")
    List<StudentVO> findPage(@Param("name") String name,
                           @Param("address") String address,
                           @Param("gender") String gender,
                           @Param("age") Integer age);

    @Select("SELECT * FROM student WHERE username=#{username} AND password=#{password}")
    Student selectbyusernameandpassword(Student student);
    
    // 根据ID和旧密码查询用户是否存在
    @Select("SELECT * FROM student WHERE id=#{id} AND password=#{oldPassword}")
    Student selectByIdAndPassword(@Param("id") Integer id, @Param("oldPassword") String oldPassword);//param保证参数对应
    
    // 修改密码
    @Update("UPDATE student SET password=#{newPassword} WHERE id=#{id}")
    int updatePassword(@Param("id") Integer id, @Param("newPassword") String newPassword);

    // 更新用户头像
    @Update("UPDATE student SET avatar=#{avatarUrl} WHERE id=#{id}")
    int updateAvatar(@Param("id") Integer id, @Param("avatarUrl") String avatarUrl);

    // 根据ID获取用户头像URL
    @Select("SELECT avatar FROM student WHERE id=#{id}")
    String getAvatarById(@Param("id") Integer id);

    // 根据用户名查询用户（用于检查用户名是否已存在）
    @Select("SELECT * FROM student WHERE username=#{username}")
    Student selectByUsername(@Param("username") String username);

    // 注册新用户（简化版，只包含必要字段）
    @Insert("INSERT INTO student(name, gender, username, password, age, address, phone) VALUES (#{name}, #{gender}, #{username}, #{password}, 18, '待完善', '待完善')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int register(Student student);
}
