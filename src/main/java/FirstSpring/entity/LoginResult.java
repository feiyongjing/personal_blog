package FirstSpring.entity;

import java.util.List;

public class LoginResult extends Result<User> {
    boolean isLogin;


    public static LoginResult failure(String message){
        return new LoginResult("fail",message,false,null);
    }

    public static LoginResult success(String message, User data){
        return new LoginResult("ok",message,true,data);
    }

    private LoginResult(String status, String msg,  boolean isLogin,User data) {
        super(status, msg, data);
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
