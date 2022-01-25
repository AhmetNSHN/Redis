package com.works.controllers;

import com.works.entities.User;
import com.works.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    final UserService uService;
    public LoginController(UserService uService) {
        this.uService = uService;
    }

    @GetMapping({ "", "/login" })
    public String home() {
        return "login";
    }


    @PostMapping("/login")
    public String login(User user, @RequestParam(defaultValue = "") String remember_me) {
        boolean status = uService.loginControl(user, remember_me);
        if(status)
        {
            return "redirect:/dashboard";
        }
        System.out.println("Status :"+ status);
        return "login";
    }

    @GetMapping("/logOut")
    public String logOut(){
        uService.logOut();
        return "redirect:/dashboard";
    }

}
