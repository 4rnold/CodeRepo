package com.heima.storage.mapper;


import com.heima.modules.po.AccountPO;
import com.heima.storage.StorageApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StorageApplication.class)
public class MapperTest {
    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void test() {
        AccountPO accountPO = new AccountPO();
        accountPO.setUsername("xx");
        AccountPO tmp = accountMapper.checkLogin(accountPO);
        System.out.println(tmp);
    }
}
