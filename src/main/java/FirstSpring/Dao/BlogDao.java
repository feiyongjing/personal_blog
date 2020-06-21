package FirstSpring.Dao;

import FirstSpring.entity.Blog;
import FirstSpring.entity.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogDao {
    private final SqlSession sqlSession;

    @Inject
    public BlogDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    private Map<String, Object> asMap(Object... args) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            result.put(args[i].toString(), args[i + 1]);
        }
        return result;
    }
    public List<Blog> getBlogs(Integer page, Integer pageSize, Integer userId){
        Map<String, Object> parameterMap=asMap("offset",(page-1)*pageSize,
                "pageSize",pageSize,
                "user_id",userId);
        return sqlSession.selectList("MyMapper.selectBlog",parameterMap);
    }

    public int count(Integer userId){
        return sqlSession.selectOne("MyMapper.countBlog", asMap("user_id",userId));
    }

    public Blog selectBlogById(int blogId) {
        return sqlSession.selectOne("MyMapper.selectBlogById", asMap("blog_id", blogId));
    }
    public Blog insertBlog(Blog blog){
        sqlSession.insert("MyMapper.insertBlog",blog);
        return selectBlogById(blog.getId());
    }

    public Blog updateBlog(Blog blog) {
        sqlSession.update("MyMapper.updateBlog", blog);
        return selectBlogById(blog.getId());
    }

    public Blog deleteBlog(int blogId) {
        sqlSession.delete("MyMapper.deleteBlog",blogId);
        return null;
    }
}
