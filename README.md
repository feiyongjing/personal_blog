# MySpringBoot

### 一个简单的博客平台

-----
使用的技术栈：

- 使用了SpringBoot框架进行整合
- 使用了正则表达式来对用户注册的用户名进行过滤
- 使用BCrypt强哈希函数的PasswordEncoder的实现来对用户的密码进行加密
- 使用了MySQL进行数据存储
- flyway进行数据库自动化迁移
- 引入了MyBatis简化了数据库操作
- 使用了jenkins和Travis-ci进行的自动化的测试
- 使用了Docker方式的jenkins进行自动化的部署和回滚
-----

启动MySQL数据库

```
docker run --name my-mysql --restart=always -v /root/mysql-data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=xdml -p 3306:3306 -d mysql:8.0.19
docker start my-mysql
```

启动Jenkins容器

```
docker run -d -p 8081:8080 -p 50000:50000 -v /root/jenkins-data:/var/jenkins_home -v /root/jenkins-m2:/var/jenkins_home/.m2 -v /var/run/docker.sock:/var/run/docker.sock virtualzone/jenkins-lts-docker
```

启动registry私有仓库
```
docker run -d -p 5000:5000 --restart always --name registry registry:2
```
