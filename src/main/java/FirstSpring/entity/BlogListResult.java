package FirstSpring.entity;

import java.util.List;

public class BlogListResult extends Result<List<Blog>> {
    private int total;
    private int page;
    private int totalPage;

    public static BlogListResult failure(String message) {
        return new BlogListResult("fail", message);
    }

    public static BlogListResult success(int total, int page, int totalPage, List<Blog> data) {
        return new BlogListResult("ok", "获取成功", total, page, totalPage, data);
    }


    public BlogListResult(String status, String msg) {
        super(status, msg);
    }

    public BlogListResult(String status, String msg, int total, int page, int totalPage, List<Blog> data) {
        super(status, msg, data);
        this.total = total;
        this.page = page;
        this.totalPage = totalPage;
    }


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
