package com.manage.controller;

import com.manage.pojo.Result;
import com.manage.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    @GetMapping("/visitor/count")
    public Result getVisitorCount() {
        int count = visitorService.getVisitorCount();
        return Result.success(count);
    }

    @PostMapping("/visitor/increment")
    public Result incrementVisitorCount() {
        int count = visitorService.incrementVisitorCount();
        return Result.success(count);
    }
}
