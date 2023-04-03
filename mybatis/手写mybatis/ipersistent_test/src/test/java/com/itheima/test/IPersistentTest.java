package com.itheima.test;

import com.itheima.dao.IUserDao;
import com.itheima.io.Resources;
import com.itheima.pojo.User;
import com.itheima.sqlSession.SqlSession;
import com.itheima.sqlSession.SqlSessionFactory;
import com.itheima.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class IPersistentTest {


    /**
     * 传统方式（不使用mapper代理）测试
     */
    @Test
    public void test1() throws Exception {

        // 1.根据配置文件的路径，加载成字节输入流，存到内存中 注意：配置文件还未解析
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");

        // 2.解析了配置文件，封装了Configuration对象  2.创建sqlSessionFactory工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);

        // 3.生产sqlSession 创建了执行器对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 4.调用sqlSession方法
        User user = new User();
        user.setId(1);
        user.setUsername("tom");
    /*    User user2 = sqlSession.selectOne("user.selectOne", user);

        System.out.println(user2);*/
        List<User> list = sqlSession.selectList("user.selectList", null);
        for (User user1 : list) {
            System.out.println(user1);
        }

        // 5.释放资源
        sqlSession.close();


    }


    /**
     * mapper代理测试
     */
    @Test
    public void test2() throws Exception {

        // 1.根据配置文件的路径，加载成字节输入流，存到内存中 注意：配置文件还未解析
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");

        // 2.解析了配置文件，封装了Configuration对象  2.创建sqlSessionFactory工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);

        // 3.生产sqlSession 创建了执行器对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 4.调用sqlSession方法
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

      /*  User user1 = new User();
        user1.setId(1);
        user1.setUsername("tom");
        User user3 = userDao.findByCondition(user1);
        System.out.println(user3);*/
        List<User> all = userDao.findAll();
        for (User user : all) {
            System.out.println(user);
        }

        // 5.释放资源
        sqlSession.close();


    }


}
