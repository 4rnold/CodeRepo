package com.arnold.ecommerce;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("220307")
@RestController
public class TestController {

    @RequestMapping("test1")
    public AsyncTaskInfo test() {
        return null;
    }
}
