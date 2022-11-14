package com.example.sprintjabackend.configuration;

import com.example.sprintjabackend.constant.Authorities;
import com.example.sprintjabackend.constant.SecurityConstant;
import com.example.sprintjabackend.filter.JwtAccessDeniedFilter;
import com.example.sprintjabackend.filter.JwtAuthenticationEntryPoint;
import com.example.sprintjabackend.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.sprintjabackend.constant.Authorities.SUPER_ADMIN_AUTHORITIES;
import static com.example.sprintjabackend.constant.SecurityConstant.ADMIN_URLS;
import static com.example.sprintjabackend.constant.SecurityConstant.PUBLIC_URLS;
import static com.example.sprintjabackend.enums.Role.ROLE_SUPER_ADMIN;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    private final JwtAccessDeniedFilter jwtAccessDeniedFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;


    @Autowired
    public SecurityConfiguration(JwtAuthorizationFilter jwtAuthorizationFilter,
                                 JwtAccessDeniedFilter jwtAccessDeniedFilter,
                                 JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                 BCryptPasswordEncoder passwordEncoder,
                                 UserDetailsService userDetailsService) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.jwtAccessDeniedFilter = jwtAccessDeniedFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public DaoAuthenticationConfigurer<AuthenticationManagerBuilder, UserDetailsService> contextSourceFactoryBean(AuthenticationManagerBuilder auth) throws Exception {
        return auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()
                .disable()
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(ADMIN_URLS)
                .hasAuthority(ROLE_SUPER_ADMIN.getAuthorities().toString())
                .antMatchers(PUBLIC_URLS)
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(jwtAccessDeniedFilter)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
