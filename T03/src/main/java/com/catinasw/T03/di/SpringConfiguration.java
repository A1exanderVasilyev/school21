package com.catinasw.T03.di;

import com.catinasw.T03.web.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringConfiguration {
    private final AuthFilter authFilter;

    @Autowired
    public SpringConfiguration(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/auth/register", "/auth/login", "/auth/update-access-token")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .addFilterBefore(authFilter, BasicAuthenticationFilter.class);
        return http.build();
    }
}
