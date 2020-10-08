package me.hoon.config;

import me.hoon.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountService accountService;  //스프링 시큐리티에서 명시적으로 선언

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**").permitAll()
                .mvcMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()  //기타 등등은 인증만 하면 된다.
                ;

        http.formLogin();  //form login을 사용하겠다.  form login 설정 안하면 그냥 모달창 같은거 나오네. logout 기능도 제공
        http.httpBasic();  //http basic을 사용하겠다.
    }

    /*
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("goohoon").password("{noop}123").roles("USER").and()  //noop -> 패스워드 암호화 안한다. (패스워드 인코딩X)
                .withUser("admin").password("{noop}!@#").roles("ADMIN");
    }
     */

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService); //우리가 사용할 userDetailService가 이거라고 명시. 이렇게 하지 않아도 빈으로 등록만 되있으면 가져다가 씀.
    }
}
