package FirstSpring.entity;

public class BlogResult extends Result<Blog> {

    private BlogResult(String status, String msg, Blog data) {
        super(status, msg, data);
    }

    public BlogResult(String status, String msg) {
        super(status, msg);
    }

    public static BlogResult success(String message, Blog blog) {
        return new BlogResult("ok", message, blog);
    }

    public static BlogResult success(String message) {
        return new BlogResult("ok", message);
    }

    public static BlogResult failure(Exception e) {
        return new BlogResult("fail", e.getMessage());
    }

    public static BlogResult failure(String message) {
        return new BlogResult("fail", message);
    }
}
