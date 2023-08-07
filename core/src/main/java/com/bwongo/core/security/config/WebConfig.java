package com.bwongo.core.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/6/23
 **/
@RequiredArgsConstructor
@EnableWebSecurity
public class WebConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

}

