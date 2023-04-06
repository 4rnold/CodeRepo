package com.heima.storage.web;

import com.heima.commons.domin.vo.response.ResponseVO;
import com.heima.modules.po.AttachmentPO;
import com.heima.storage.handler.AttachmentHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/")
@Api(value = "文件操作Controller", tags = {"文件管理"})
@ApiResponses(@ApiResponse(code = 200, message = "处理成功"))
public class APIController {


    @Autowired
    private AttachmentHandler attachmentHandler;


    @ApiOperation(value = "文件上传接口", tags = {"文件管理"})
    @PostMapping("/upload")
    public ResponseVO<AttachmentPO> upload(@RequestParam("file") MultipartFile file) throws Exception {
        return attachmentHandler.uploadFile(file);
    }

    @GetMapping("/test1")
    @Cacheable(cacheNames = "cache-demo",key = "#id")
    public Object test1(@RequestParam String id){
        System.out.println("load,"+id);
        return "hello,"+id;
    }

    @GetMapping("/test2")
    @CacheEvict(cacheNames = "cache-demo",key = "#id")
    public Object test2(@RequestParam String id){
        return "hello,"+id;
    }

}
