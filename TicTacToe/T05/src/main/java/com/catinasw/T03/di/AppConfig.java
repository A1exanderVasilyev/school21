package com.catinasw.T03.di;

import com.catinasw.T03.web.mapper.DomainWebMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan("com.catinasw.T03")
public class AppConfig {

    @Bean
    public DomainWebMapper getDomainWebMapper() {
        return new DomainWebMapper();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
