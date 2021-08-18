package com.dailyProject.config;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    UserDetailsService users(){
        UserDetails user1 = User.builder()
                .username("user1")
                .password(passwordEncoder().encode("1234"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("1234"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1, admin);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable() // csrf = 비활성화
                .formLogin(config->{ // 로그인 관련
                    config
                            .loginPage("/login") // 로그인 페이지 경로
                            .successForwardUrl("/") // 로그인 성공 시 경로
                            .failureForwardUrl("/login?error=true"); // 로그인 실패 시 경로
                })
                .authorizeRequests(config->{ // 인증 요청관련
                    config
                            .antMatchers("/login").permitAll() // 로그인 페이지는 누구나 접근가능
                            .antMatchers("/").authenticated(); // Home 페이지(/)는 인증이 필요함
                });
    }
}
