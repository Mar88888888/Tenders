package com.example.tendersystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

import com.example.tendersystem.user.UserService;

@Configuration
public class SecurityConfig {

  @Autowired
  private UserService userService;

  public SecurityConfig() {
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/user/login", "/user/signup", "/*.css", "/error.html", "/h2-console/**").permitAll()
            .anyRequest().authenticated())
        .formLogin(form -> form
            .loginPage("/user/login")
            .defaultSuccessUrl("/tenders/active", true)
            .permitAll())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/user/login")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .permitAll())
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**")
            .csrfTokenRepository(org.springframework.security.web.csrf.CookieCsrfTokenRepository.withHttpOnlyFalse()))
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.sameOrigin()));

    return http.build();

  }

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> userService.findByUsername(username)
        .map(user -> org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
