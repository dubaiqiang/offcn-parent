package com.offcn.user.controller;

import com.offcn.user.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/hello")
@Api(tags = "第一个Swagger测试")    //在类上加说明
public class HelloController {

    @ApiOperation("测试方法hello")   //在方法上加说明
    @ApiImplicitParam(name = "name", value = "姓名", required = true)
    @GetMapping("/sayHello")
    public String sayHello(String name) {
        return "hello," + name;
    }




    @ApiOperation("添加用户")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "name", value = "姓名", required = true), @ApiImplicitParam(name = "email", value = "电子邮箱", required = true)})
    @PostMapping("/addUser")
    public User addUser(String name, String email) {
        return new User(1L, name, email);
    }
}
