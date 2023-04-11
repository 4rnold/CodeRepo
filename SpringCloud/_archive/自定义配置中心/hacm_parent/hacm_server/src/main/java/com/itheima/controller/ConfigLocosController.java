package com.itheima.controller;

import com.itheima.common.PageResult;
import com.itheima.domain.ConfigLocos;
import com.itheima.service.ConfigLocosService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 轨迹的控制器
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Controller
@RequestMapping("/config")
public class ConfigLocosController {

    @Autowired
    private ConfigLocosService configLocosService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/findLocos")
    public String findLocos(String id, @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size){
        //1.根据条件查询轨迹
        Page<ConfigLocos> configLocosPage = configLocosService.findPage(id,page,size);
        //2.创建页面所需的分页对象
        PageResult pageResult = new PageResult(configLocosPage.getTotalElements(),configLocosPage.getContent(),page,size);
        //3.存入请求域中
        request.setAttribute("page",pageResult);
        //4.前往页面
        return "config/locos";
    }
}


