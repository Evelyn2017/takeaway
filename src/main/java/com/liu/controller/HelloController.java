package com.liu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello(Map<String, Object> para) {
        para.put("name","liuxiao");
        para.put("age",20);

        return "hello";
    }
}
