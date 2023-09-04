package com.ivanrogulj.Blog.Config;


import com.ivanrogulj.Blog.Services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig  {

    private final CustomUserDetailsService customUserDetailsService;

    private final UserDetailsService userService;
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, UserDetailsService userService) {
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception
    {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/register","/auth/login").permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers("/api/**")
                .authenticated()
                .and().formLogin().and().build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();

        auth.setUserDetailsService(userService);

        auth.setPasswordEncoder(passwordEncoder());

        return auth;

    }
}

