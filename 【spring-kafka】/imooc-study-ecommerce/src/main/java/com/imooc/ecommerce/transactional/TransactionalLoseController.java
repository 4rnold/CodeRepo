package com.imooc.ecommerce.transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>TransactionalLose Controller</h1>
 * */
@Slf4j
@RestController
@RequestMapping("/transactional-lose")
public class TransactionalLoseController {

    private final TransactionalLose transactionalLose;

    public TransactionalLoseController(TransactionalLose transactionalLose) {
        this.transactionalLose = transactionalLose;
    }

    @GetMapping("/wrong-rollback-for")
    public void wrongRollbackFor() throws Exception {

        log.info("coming in call wrong rollback for");
        transactionalLose.wrongRollbackFor();
    }

    @GetMapping("/wrong-inner-call")
    public void wrongInnerCall() throws Exception {

        log.info("coming in call wrong inner call");
        transactionalLose.wrongInnerCall();
    }
}
