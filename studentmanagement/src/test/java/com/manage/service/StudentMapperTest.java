package com.manage.service;

import com.manage.mapper.StudentMapper;
import com.manage.pojo.Student;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * StudentMapper 测试类
 */
@DisplayName("学生 Mapper 测试类")
@SpringBootTest
public class StudentMapperTest {

    @Autowired
    private StudentMapper studentMapper;

    @BeforeAll
    public static void beforeAll() {
        System.out.println("开始测试 StudentMapper...");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("StudentMapper 测试结束...");
    }

    /**
     * 测试 findAll 方法
     */
    @Test
    @DisplayName("测试查询所有学生")
    public void testFindAll() {
        List<Student> students = studentMapper.findAll();

        students.forEach(System.out::println);
    }
}
