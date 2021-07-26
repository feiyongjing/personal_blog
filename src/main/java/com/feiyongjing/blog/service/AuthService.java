package com.feiyongjing.blog.service;

import com.feiyongjing.blog.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class AuthService {
    private UserService userService;

    @Inject
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUsername(authentication == null ? null : authentication.getName());
    }
}
