package FirstSpring.entity;

public class Result {
    private String status;
    private String msg;
    private boolean isLogin;
    private Object data;

    public static Result failure(String message){
        return new Result("fail",message,false);
    }

    public static Result success(String message, Object data){
        return new Result("ok",message,true,data);
    }

    private Result(String status, String msg, boolean isLogin) {
        this.status = status;
        this.msg = msg;
        this.isLogin = isLogin;
    }

    private Result(String status, String msg, boolean isLogin, Object data) {
        this.status = status;
        this.msg = msg;
        this.isLogin = isLogin;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public Object getData() {
        return data;
    }
}
