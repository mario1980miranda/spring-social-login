package com.code.truck.oauth.springsociallogin.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.code.truck.oauth.springsociallogin.entities.AppUser;
import com.code.truck.oauth.springsociallogin.fomanticUI.Toast;
import com.code.truck.oauth.springsociallogin.services.AppUserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/user")
@Log4j2
@Value
public class AppUserController {

    AppUserService appUserService;

    @ModelAttribute
    public AppUser appUser(@AuthenticationPrincipal AppUser appUser) {
        return appUser;
    }

    @GetMapping
    public String showUserInfo() {
        return "app-user/user";
    }

    @PostMapping("/password")
    public String changePassword(RedirectAttributes attributes) {

        attributes.addFlashAttribute("tab", "pass");
        attributes.addFlashAttribute("toast", Toast.success("Password", "Change password"));

        return "redirect:/user";
    }

    record SignUpForm(
            @NotBlank String username,
            @Size(min = 4, message = "password must have a minimum of {min} characters") String password) {

    }

    @GetMapping("/signup")
    public String showSignUp(@ModelAttribute SignUpForm signUpForm) {
        return "app-user/signup";
    }

    @PostMapping("/signup")
    public String signUp(@Valid SignUpForm signUpForm, Errors errors, RedirectAttributes attributes) {

        log.info("posted sign up form => {}", signUpForm);

        if (errors.hasErrors()) {
            return "app-user/signup";
        }

        try {
            appUserService.createUser(signUpForm.username, signUpForm.password);

            attributes.addFlashAttribute("toast",
                    Toast.success("User sign up", "User " + signUpForm.username + " was created."));

            return "redirect:/login";

        } catch (Exception e) {

            attributes.addFlashAttribute("toast",
                    Toast.error("User sign up", e.getMessage()));

            return "redirect:/user/signup";
        }
    }
}
