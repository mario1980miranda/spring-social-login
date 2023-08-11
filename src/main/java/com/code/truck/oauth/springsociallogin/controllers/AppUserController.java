package com.code.truck.oauth.springsociallogin.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.code.truck.oauth.springsociallogin.entities.AppUser;

@Controller
@RequestMapping("/user")
public class AppUserController {

    @ModelAttribute
    public AppUser appUser(@AuthenticationPrincipal AppUser appUser) {
        return appUser;
    }

    @GetMapping
    public String showUserInfo() {
        return "app-user/user";
    }
}
