package com.s2u2m.services.ac.config;

import com.s2u2m.services.ac.AppContextProvider;
import com.s2u2m.services.ac.service.account.AccountDetailsService;
import com.s2u2m.services.ac.service.account.UsernameAccountDetailsService;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Configuration
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
    public static class UsernameWebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.antMatcher("/login/username")
                    .authorizeRequests()
                    .antMatchers("/login/username").permitAll()
                    .anyRequest().authenticated();
            http.httpBasic();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            super.configure(auth);
            AccountDetailsService detailsService = AppContextProvider.getContext().getBean(UsernameAccountDetailsService.class);
            auth.userDetailsService(detailsService);
        }
    }

    @Configuration
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 9)
    public static class PhoneWebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.antMatcher("/login/phone")
                    .authorizeRequests()
                    .antMatchers("/login/phone").permitAll()
                    .anyRequest().authenticated();
            http.httpBasic();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            super.configure(auth);
            auth.userDetailsService(phoneUserDetailService());
        }

        UserDetailsService phoneUserDetailService(){
            String finalPassword = "123456";
            InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
            manager.createUser(User.withUsername("13912343456").password(finalPassword).authorities("USER").build());
            return manager;
        }
    }
}
