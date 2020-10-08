package me.hoon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/", "/info").permitAll()
                .mvcMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()  //기타 등등은 인증만 하면 된다.
                ;

        http.formLogin();  //form login을 사용하겠다.  form login 설정 안하면 그냥 모달창 같은거 나오네. logout 기능도 제공
        http.httpBasic();  //http basic을 사용하겠다.
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("goohoon").password("{noop}123").roles("USER").and()  //noop -> 패스워드 암호화 안한다. (패스워드 인코딩X)
                .withUser("admin").password("{noop}!@#").roles("ADMIN");
    }
}
