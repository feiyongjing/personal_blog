package com.feiyongjing.blog.controller;

import com.feiyongjing.blog.service.AuthService;
import com.feiyongjing.blog.service.UserService;
import com.feiyongjing.blog.entity.LoginResult;
import com.feiyongjing.blog.entity.User;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Pattern;

import static com.feiyongjing.blog.utils.AntiReptile.checkCrawler;

@Controller
public class AuthController {
    private AuthService authService;
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private Pattern UserNameLimit = Pattern.compile("^[a-zA-Z0-9_\\u4e00-\\u9fa5]{1,15}$");

    @Inject
    public AuthController(AuthService authService, UserService userService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    @ResponseBody
    public LoginResult auth() {
        User loggedInUser = authService.getCurrentUser();
        if (loggedInUser == null) {
            return LoginResult.failure("用户没有登录");
        } else {
            return LoginResult.success(null, loggedInUser);
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public LoginResult authLogout() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByUsername(userName);
        if (loggedInUser == null) {
            return LoginResult.failure("用户没有登录");
        } else {
            SecurityContextHolder.clearContext();
            return LoginResult.success("注销成功", loggedInUser);
        }
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public Object authRegister(@RequestBody Map<String, Object> usernameAndPasswordJson, HttpServletRequest request) {
        if (checkCrawler(request)){
            return "死爬虫去死吧";
        }
        String username = usernameAndPasswordJson.get("username").toString();
        String password = usernameAndPasswordJson.get("password").toString();
        if (username == null || password == null) {
            return LoginResult.failure("用户名或密码为空");
        }
        if (!UserNameLimit.matcher(username).find()) {
            return LoginResult.failure("用户名不符合规范");
        }
        if (password.length() < 6 || password.length() > 16) {
            return LoginResult.failure("密码长度不符合规范");
        }
        try {
            userService.save(username, password);
        } catch (DuplicateKeyException e) {
            return LoginResult.failure("该用户名已被注册");
        }
        return LoginResult.success("注册成功", userService.getUserByUsername(username));
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Object authLogin(@RequestBody Map<String, Object> usernameAndPasswordJson, HttpServletRequest request) {
        if (checkCrawler(request)){
            return "死爬虫去死吧";
        }
        String username = usernameAndPasswordJson.get("username").toString();
        String password = usernameAndPasswordJson.get("password").toString();
        UserDetails userDetails = null;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return LoginResult.failure("用户不存在");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);

            return LoginResult.success("登录成功", userService.getUserByUsername(username));
        } catch (BadCredentialsException e) {
            return LoginResult.failure("密码不正确");
        }
    }
}
