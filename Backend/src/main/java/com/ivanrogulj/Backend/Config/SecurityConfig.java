package com.ivanrogulj.Backend.Config;


import com.ivanrogulj.Backend.Services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig  {

    @Qualifier("customUserDetailsService")
    private final CustomUserDetailsService customUserDetailsService;



    private final UserDetailsService userService;
    public SecurityConfig(CustomUserDetailsService customUserDetailsService, UserDetailsService userService) {
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
    }


//    @Bean
//    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception
//    {
//         http.csrf().disable()
//                .authorizeHttpRequests()
//                .requestMatchers(
//                        "/register"
//                ).permitAll()
//                .requestMatchers("/api/**",
//                         "/logout",
//                         "/home",
//                         "/post/**",
//                         "/updatePost",
//                         "/register",
//                         "/post/*",
//                         "/comment/**",
//                         "comment/view",
//                         "/comment/view/**",
//                         "/explore",
//                         "/profile/**",
//                         "/search")
//                .authenticated()
//                .and()
//                 .formLogin()
//                 .loginPage("/login")
//                 .defaultSuccessUrl("/home")
//                 .permitAll()
//                 .and()
//                .logout()
//                 .logoutSuccessUrl("/login?logout")
//                 .permitAll()
//                .deleteCookies("JSESSIONID");
//
//
//         return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                "/register",
                                "/login",
                                "/logout",
                                "/error/**"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/home")
                        .failureUrl("/login-error")
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                )
        ;
        return http.build();
    }


//    @Bean
//    SecurityFilterChain web(HttpSecurity http) throws Exception {
//        http
//                // ...
//                .httpBasic((basic) -> basic
//                        .addObjectPostProcessor(new ObjectPostProcessor<BasicAuthenticationFilter>() {
//                            @Override
//                            public <O extends BasicAuthenticationFilter> O postProcess(O filter) {
//                                filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
//                                return filter;
//                            }
//                        })
//                );
//
//        return http.build();
//    }



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
        auth.setUserDetailsService(customUserDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
}

