package com.dailyProject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@WebMvcTest({TestController.class, HomeController.class})
public class UserAccessTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDetails user1(){
        return User.builder()
                .username("user1")
                .password(passwordEncoder.encode("1234"))
                .roles("USER")
                .build();
    }


    public UserDetails admin(){
        return User.builder()
                .username("admin")
                .password(passwordEncoder.encode("1234"))
                .roles("ADMIN")
                .build();
    }

    @DisplayName("1.user 로 user 페이지를 접근 가능")
    @Test
//    @WithMockUser(username = "user1", roles = {"USER"})
    public void test_user_access_userPage() throws Exception {
        String resp = mockMvc.perform(get("/user").with(user(user1())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        SecurityMessage message = mapper.readValue(resp, SecurityMessage.class);
        assertEquals("user page", message.getMessage());
    }

    @DisplayName("2.user 가 admin 페이지를 접근할 수 없다.")
    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    public void test_user_cannot_access_adminpage() throws Exception{
        mockMvc.perform(get("/admin"))
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("3. admin 이 user 페이지와 admin 페이지를 접근할 수 있다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void admin은_유저및어드민_페이지에_접근할수있다() throws Exception{
        String resp = mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        SecurityMessage message = mapper.readValue(resp, SecurityMessage.class);
        assertEquals("user page", message.getMessage());

        resp = mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        message = mapper.readValue(resp, SecurityMessage.class);
        assertEquals("admin page", message.getMessage());
    }

    @DisplayName("4. 로그인 페이지는 아무나 접근할 수 있어야 한다.")
    @Test
    @WithAnonymousUser
    public void test_login_pag_can_accessed_anonymous() throws Exception{
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @DisplayName("5. 홈페이지는 로그인하지 않은 사람은 접근할 수 없다.")
    @Test
    @WithAnonymousUser
    public void test_need_login() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection()); // 302 redirect to /login
        mockMvc.perform(get("/user"))
                .andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection());
    }
}