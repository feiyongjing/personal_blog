package com.feiyongjing.blog.controller;

import com.feiyongjing.blog.service.AuthService;
import com.feiyongjing.blog.service.BlogService;
import com.feiyongjing.blog.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.apache.http.Consts.UTF_8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class BlogControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AuthService authService;
    @Mock
    private BlogService blogService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BlogController(authService, blogService)).build();
    }

    @Test
    void requireLoginBeforeProceeding() throws Exception {
        mockMvc.perform(post("/blog").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(UTF_8).contains("登录后才能操作")));
    }

    @Test
    void invalidRequestIfTitleIsEmpty() throws Exception {
        Mockito.when(authService.getCurrentUser()).thenReturn(new User(1, "mockUser", ""));
        mockMvc.perform(post("/blog").contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(UTF_8).contains("博客标题字数不符合规范")));
    }
}