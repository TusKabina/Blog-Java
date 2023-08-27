package com.ivanrogulj.Blog.Config;


import com.ivanrogulj.Blog.Security.AuthenticationFilter;
import com.ivanrogulj.Blog.Services.CustomUserDetailsService;
import com.ivanrogulj.Blog.Services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final CustomUserDetailsService customUserDetailsService;

    private final UserDetailsService userService;
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, UserDetailsService userService) {
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/api/**").hasAnyRole("USER","ADMIN")
                        .anyRequest().authenticated()


                )
                .csrf().disable()
                .addFilterBefore(new AuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//
//        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
//
//        auth.setUserDetailsService(userService);
//
//        auth.setPasswordEncoder(passwordEncoder());
//
//        return auth;
//
//    }
}