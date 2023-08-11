package com.code.truck.oauth.springsociallogin.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.code.truck.oauth.springsociallogin.config.TestSecurityConfiguration;
import com.code.truck.oauth.springsociallogin.services.AppUserService;

@WebMvcTest
@Import(TestSecurityConfiguration.class)
public class HomeControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AppUserService appUserService;

    /*
     * @Test
     * 
     * @WithMockUser
     * public void AuthenticatedUserShouldDisplayHomePage() throws Exception {
     * mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(
     * MockMvcResultMatchers.status().isOk());
     * }
     */

    @Test
    public void annonymousUserShouldBeAllowedToVisitHomePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
