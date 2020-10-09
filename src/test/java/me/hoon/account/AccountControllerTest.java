package me.hoon.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Test
    public void index_anonymous() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void index_anonymous2() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void index_user() throws Exception{
        //이러한 유저가 로그인한 상태일 경우라고 가정
        mockMvc.perform(MockMvcRequestBuilders.get("/").with(user("goohoon").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "goohoon", roles = "USER")
    public void index_user2() throws Exception{
        //이러한 유저가 로그인한 상태일 경우라고 가정
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUser
    public void index_user3() throws Exception{
        //이러한 유저가 로그인한 상태일 경우라고 가정
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void admin() throws Exception{
        //이러한 유저가 로그인한 상태일 경우라고 가정
        mockMvc.perform(MockMvcRequestBuilders.get("/admin").with(user("admin").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void admin2() throws Exception{
        //이러한 유저가 로그인한 상태일 경우라고 가정
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void not_admin() throws Exception{
        //이러한 유저가 로그인한 상태일 경우라고 가정
        mockMvc.perform(MockMvcRequestBuilders.get("/admin").with(user("goohoon").roles("USER")))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void login_success() throws Exception{
        String username = "goohoon";
        String password = "123";
        Account user = this.createUser(username, password);
        mockMvc.perform(formLogin().user(username).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @Transactional
    public void login_fail() throws Exception{
        String username = "goohoon";
        String password = "123";
        Account user = this.createUser(username, password);
        mockMvc.perform(formLogin().user(username).password("12345"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

    public Account createUser(String username, String password){
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setRole("USER");
        return accountService.createNew(account);
    }

}