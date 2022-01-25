package com.works.controllers;

import com.works.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    final UserService uService;

    public DashboardController(UserService uService) {
        this.uService = uService;
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return uService.control("dashboard");
    }
}
