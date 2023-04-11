package com.tensquare.test;

import com.tensquare.base.BaseApplication;
import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试标签的CRUD操作
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BaseApplication.class)
public class LabelTest {

    @Autowired
    private LabelService labelService;

    @Test
    public void testSave(){
        Label label = new Label();
        label.setLabelname("Java");
        label.setState("1");
        label.setCount(0);
        label.setFans(0);
        label.setRecommend("1");
        labelService.save(label);
    }
}
