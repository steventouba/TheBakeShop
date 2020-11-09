package com.steven.willysbakeshop.configuration;

import com.steven.willysbakeshop.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return new MyUserDetailsService();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(userDetailsService()).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/products/create").hasRole("SELLER")
                    .antMatchers("/products/(\\d+}/*}").hasRole("SELLER")
                    .antMatchers("/users/{\\d+}/*").hasAnyRole("SELLER", "BUYER", "ADMIN")
                    .antMatchers("/**").permitAll()
                .and()
                    .formLogin()
                        .defaultSuccessUrl("/users/")
                        .permitAll()
                .and()
                    .logout().permitAll()
                .and()
                    .httpBasic()
                .and()
                .csrf().disable()
        ;

    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
