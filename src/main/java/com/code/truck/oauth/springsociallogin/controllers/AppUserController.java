package com.code.truck.oauth.springsociallogin.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.code.truck.oauth.springsociallogin.entities.AppUser;

@Controller
@RequestMapping("/user")
public class AppUserController {

    @GetMapping
    public String showUserInfo(@AuthenticationPrincipal AppUser appUser, Model model) {
        model.addAttribute("appUser", appUser);
        return "app-user/user";
    }
}
