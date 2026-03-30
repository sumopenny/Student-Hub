package com.manage.controller;

import com.manage.pojo.PageResult;
import com.manage.pojo.Result;
import com.manage.pojo.Student;
import com.manage.pojo.StudentVO;
import com.manage.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j// 日志记录
@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;
    //打开页面时自动显示数据
//    @GetMapping("/stus")
//    public Result list(){
//        System.out.println("查询全部数据");
//        List<Student> studentlist = studentService.findAll();
//        return Result.success(studentlist);
//    }

    //点击“删除”按钮，删除数据
    @DeleteMapping("/stus")
    public Result delete(Integer id){
//        System.out.println("删除数据id="+id);
        log.info("删除数据id="+id);
        studentService.delete(id);
        return Result.success();
    }
    
    // 修改密码
    @PostMapping("/stus/updatePassword")
    public Result updatePassword(@RequestBody Map<String, String> passwordMap){
        Integer id = Integer.parseInt(passwordMap.get("id"));
        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");
        
        log.info("修改密码：用户ID={}", id);
        boolean result = studentService.updatePassword(id, oldPassword, newPassword);
        
        if(result){
            return Result.success("密码修改成功");
        }else{
            return Result.error("旧密码不正确，请重新输入");
        }
    }

    //点击“添加”按钮，弹出添加窗口，输入数据后点击确定按钮，新增数据
    //requestBody将前台发送的json数据转为java对象封装到Student对象中
    @PostMapping("/stus")
    public Result add(@RequestBody Student student){
//        System.out.println("新增数据"+student);
        log.info("新增数据{}", student);
        studentService.add(student);
        return Result.success();
    }

    //前端点击“修改”按钮，弹出修改窗口，回显数据
    @GetMapping("/stus/{id}")
    public Result get(@PathVariable Integer id){
        log.info("查询数据id={}", id);
        Student student = studentService.getSimple(id);
        return Result.success(student);
    }

    //在弹出的修改窗口中修改完数据，点击“保存”按钮，完成修改数据
    @PutMapping("/stus")
    public Result update(@RequestBody Student student){
//        System.out.println("修改数据"+student);
        log.info("修改数据{}", student);
        studentService.update(student);
        return Result.success();
    }

    @GetMapping("/stus")
    public Result page(
            Integer page,
            Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer age) {

        log.info("条件分页查询: 姓名={}", name);
        PageResult<StudentVO> pageResult = studentService.page(page, pageSize, name, address, gender, age);
        return Result.success(pageResult);
    }


}
