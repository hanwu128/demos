package com.example.cache.controller;

import com.example.cache.bean.Department;
import com.example.cache.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentContoller {

    @Autowired
    DepartmentService departmentService;

    @GetMapping("/dep/{id}")
    public Department getDepById(@PathVariable("id") Integer id) {
        return departmentService.getDepById(id);
    }
}
