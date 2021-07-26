package com.feiyongjing.blog.controller;

import com.feiyongjing.blog.service.AuthService;
import com.feiyongjing.blog.service.BlogService;
import com.feiyongjing.blog.entity.Blog;
import com.feiyongjing.blog.entity.BlogListResult;
import com.feiyongjing.blog.entity.BlogResult;
import com.feiyongjing.blog.entity.User;
import com.feiyongjing.blog.utils.AssertUtils;
import com.feiyongjing.blog.utils.StringArgument;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Map;

@Controller
public class BlogController {
    private final AuthService authService;
    private final BlogService blogService;

    @Inject
    public BlogController(AuthService authService, BlogService blogService) {
        this.authService = authService;
        this.blogService = blogService;
    }

    @GetMapping("/blog")
    @ResponseBody
    public BlogListResult getBlogs(@RequestParam("page") Integer page, @RequestParam(value = "userId", required = false) Integer userId) {
        if (page < 0 || page == null) {
            page = 1;
        }
        return blogService.getBlogs(page, 10, userId);
    }

    @GetMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResult getBlog(@PathVariable("blogId") int blogId) {
        return blogService.getBlogById(blogId);
    }

    @PostMapping("/blog")
    @ResponseBody
    public BlogResult newBlog(@RequestBody Map<String, String> params) {
        try {
            return blogService.insertBlog(fromParam(params));
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    @PatchMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResult updateBlog(@PathVariable("blogId") int blogId, @RequestBody Map<String, String> params) {
        try {
            return blogService.updateBlog(blogId, fromParam(params));
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    @DeleteMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResult deleteBlog(@PathVariable("blogId") int blogId) {
        try {
            isLogin();
            return blogService.deleteBlog(blogId, authService.getCurrentUser());
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    private void isLogin() throws RuntimeException {
        if (authService.getCurrentUser() == null) {
            throw new RuntimeException("登录后才能操作");
        }
    }


    private Blog fromParam(Map<String, String> params) {
        Blog blog = new Blog();
        User user = authService.getCurrentUser();
        String title = params.get("title");
        String content = params.get("content");
        String description = params.get("description");

        isLogin();

        AssertUtils.assertTrue(StringArgument.StringMaxLengthArgument(title, 100), "博客标题字数不符合规范");
        AssertUtils.assertTrue(StringArgument.StringMaxLengthArgument(content, 10000), "博客内容字数不符合规范");

        if (StringArgument.isEmpty(description)) {
            description = content.substring(0, Math.min(content.length(), 10)) + "...";
        }
        blog.setContent(content);
        blog.setTitle(title);
        blog.setDescription(description);
        blog.setUser(user);
        blog.setCreatedAt(Instant.now());
        blog.setUpdatedAt(Instant.now());
        return blog;
    }

}
