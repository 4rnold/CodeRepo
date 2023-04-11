package com.itheima.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Controller
public class LoginController {

    @RequestMapping(value="/login")
    public String login(String email,String password){
        return "home/main";
    }


    @RequestMapping("/home")
    public String home(){
        return "home/home";
    }
}
