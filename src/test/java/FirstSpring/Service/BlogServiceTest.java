package FirstSpring.Service;

import FirstSpring.Dao.BlogDao;
import FirstSpring.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {
    @Mock
    BlogDao blogDao;
    @InjectMocks
    public BlogService blogService;

    @Test
    public void getBlogsFromDb() {
        List<Blog> blogs = Arrays.asList(mock(Blog.class), mock(Blog.class));
        when(blogDao.getBlogs(1, 2, null)).thenReturn(blogs);
        when(blogDao.count(null)).thenReturn(3);

        BlogListResult result = blogService.getBlogs(1, 2, null);

        verify(blogDao).count(null);
        verify(blogDao).getBlogs(1, 2, null);

        Assertions.assertEquals(1, result.getPage());
        Assertions.assertEquals(3, result.getTotal());
        Assertions.assertEquals(2, result.getTotalPage());
        Assertions.assertEquals("ok", result.getStatus());
    }

    @Test
    public void returnFailureWhenExceptionThrow() {
        when(blogDao.getBlogs(1, 10, 1)).thenThrow(new RuntimeException());

        when(blogDao.count(1)).thenReturn(10);
        when(blogDao.count(null)).thenReturn(0);
        when(blogDao.count(3)).thenReturn(0);
        when(blogDao.count(4)).thenReturn(1);

        Result result1 = blogService.getBlogs(1, 10, 1);
        Result result2 = blogService.getBlogs(2, 10, null);
        Result result3 = blogService.getBlogs(3, 10, 3);
        Result result4 = blogService.getBlogs(4, 10, 4);

        verify(blogDao).count(1);
        verify(blogDao).count(null);
        verify(blogDao).count(3);
        verify(blogDao).count(4);

        verify(blogDao).getBlogs(1, 10, 1);

        Assertions.assertEquals("fail", result1.getStatus());
        Assertions.assertEquals("系统异常", result1.getMsg());

        Assertions.assertEquals("fail", result2.getStatus());
        Assertions.assertEquals("没有可展示的博客", result2.getMsg());

        Assertions.assertEquals("fail", result3.getStatus());
        Assertions.assertEquals("您没有可展示的博客", result3.getMsg());

        Assertions.assertEquals("fail", result4.getStatus());
        Assertions.assertEquals("输入的页码数超过总页码数", result4.getMsg());
    }

    @Test
    public void returnFailureWhenBlogNotFound() {
        when(blogDao.selectBlogById(1)).thenReturn(null);

        BlogResult result = blogService.deleteBlog(1, mock(User.class));

        Assertions.assertEquals("fail", result.getStatus());
        Assertions.assertEquals("博客不存在", result.getMsg());
    }

    @Test
    public void returnFailureWhenBlogUserIdNotMatch() {
        User blogAuthor = new User(123, "blogAuthor", "");
        User operator = new User(456, "operator", "");

        Blog targetBlog = new Blog();
        targetBlog.setId(1);
        targetBlog.setUser(operator);

        Blog blogInDb = new Blog();
        blogInDb.setId(2);
        blogInDb.setUser(blogAuthor);

        when(blogDao.selectBlogById(1)).thenReturn(blogInDb);

        BlogResult result1 = blogService.updateBlog(1, targetBlog);
        BlogResult result2 = blogService.deleteBlog(1, operator);

        Assertions.assertEquals("fail", result1.getStatus());
        Assertions.assertEquals("无法修改别人的博客", result1.getMsg());
        Assertions.assertEquals("fail", result2.getStatus());
        Assertions.assertEquals("无法删除别人的博客", result2.getMsg());
    }
}
