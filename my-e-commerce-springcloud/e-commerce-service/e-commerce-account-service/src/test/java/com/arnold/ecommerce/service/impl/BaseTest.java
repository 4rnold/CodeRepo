package com.arnold.ecommerce.service.impl;

import com.arnold.ecommerce.filter.AccessContext;
import com.arnold.ecommerce.vo.LoginUserInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BaseTest {
    protected final LoginUserInfo loginUserInfo = new LoginUserInfo(10L,"arnold@imooc.com");

    @Before
    public void init() {
        AccessContext.setLoginUserInfo(loginUserInfo);

    }

    @After
    public void destroy() {
        AccessContext.clearLoginUserInfo();
    }
}
