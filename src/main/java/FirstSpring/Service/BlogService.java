package FirstSpring.Service;

import FirstSpring.Dao.BlogDao;
import FirstSpring.entity.Blog;
import FirstSpring.entity.BlogListResult;
import FirstSpring.entity.BlogResult;
import FirstSpring.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

@Service
public class BlogService {
    private final BlogDao blogDao;

    @Inject
    public BlogService(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    public BlogListResult getBlogs(Integer page, Integer pageSize, Integer userId) {
        try {

            Integer count = blogDao.count(userId);
            Integer totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            if (count == 0 && userId == null) {
                throw new IllegalArgumentException("没有可展示的博客");
            }
            if (count == 0) {
                throw new IllegalArgumentException("您没有可展示的博客");
            }
            if (page > totalPage) {
                throw new IllegalArgumentException("输入的页码数超过总页码数");
            }
            List<Blog> blogs = blogDao.getBlogs(page, pageSize, userId);

            return BlogListResult.success(count, page, totalPage, blogs);
        } catch (IllegalArgumentException e) {
            return BlogListResult.failure(e.getMessage());
        } catch (Exception e) {
            return BlogListResult.failure("系统异常");
        }
    }

    public BlogResult getBlogById(int blogId) {
        try {
            return BlogResult.success("获取成功", blogDao.selectBlogById(blogId));
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    public BlogResult insertBlog(Blog blog) {
        try {
            return BlogResult.success("创建成功", blogDao.insertBlog(blog));
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    public BlogResult updateBlog(int blogId, Blog blog) {
        Blog blogInDb = blogDao.selectBlogById(blogId);
        if (blogInDb == null) {
            return BlogResult.failure("博客不存在");
        }
        if (!Objects.equals(blog.getUserId(), blogInDb.getUserId())) {
            return BlogResult.failure("无法修改别人的博客");
        }

        try {
            blog.setId(blogId);
            return BlogResult.success("修改成功", blogDao.updateBlog(blog));
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    public BlogResult deleteBlog(int blogId, User user) {
        Blog blogInDb = blogDao.selectBlogById(blogId);
        if (blogInDb == null) {
            return BlogResult.failure("博客不存在");
        }
        if (!Objects.equals(user.getId(), blogInDb.getUserId())) {
            return BlogResult.failure("无法删除别人的博客");
        }
        try {
            return BlogResult.success("删除成功", blogDao.deleteBlog(blogId));
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }
}
