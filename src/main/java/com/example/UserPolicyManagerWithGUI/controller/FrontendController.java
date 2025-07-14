package com.example.UserPolicyManagerWithGUI.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {
    @RequestMapping({
            "/",
            "/users",
            "/policies",
            "/users/**",
            "/policies/**"
    })
    public String redirect() {
        return "forward:/index.html";
    }
}
