package FirstSpring.Controller;

import FirstSpring.Service.AuthService;
import FirstSpring.Service.UserService;
import FirstSpring.entity.LoginResult;
import FirstSpring.entity.User;
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
import java.util.Map;

@Controller
public class AuthController {
    private AuthService authService;
    private UserService userService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(AuthService authService,UserService userService, AuthenticationManager authenticationManager) {
        this.authService=authService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    @ResponseBody
    public LoginResult auth() {
        User loggedInUser= authService.getCurrentUser();
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
    public LoginResult authRegister(@RequestBody Map<String, Object> usernameAndPasswordJson) {
        String username = usernameAndPasswordJson.get("username").toString();
        String password = usernameAndPasswordJson.get("password").toString();
        if (username == null || password == null) {
            return LoginResult.failure("username/password == null");
        }
        if (username.length() < 1 || username.length() > 15) {
            return LoginResult.failure("invalid username");
        }
        if (password.length() < 6 || password.length() > 16) {
            return LoginResult.failure("invalid password");
        }
        try {
            userService.save(username, password);
        } catch (DuplicateKeyException e) {
            return LoginResult.failure("user already exists");
        }
        return LoginResult.success("注册成功", userService.getUserByUsername(username));
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public LoginResult authLogin(@RequestBody Map<String, Object> usernameAndPasswordJson) {
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
