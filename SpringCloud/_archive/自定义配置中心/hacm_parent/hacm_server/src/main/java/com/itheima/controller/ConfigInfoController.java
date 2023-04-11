package com.itheima.controller;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.itheima.common.PageResult;
import com.itheima.domain.ConfigInfo;
import com.itheima.service.ConfigInfoService;
import com.itheima.utils.PropertiesUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * 配置信息的控制器
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Controller
@RequestMapping("/config")
public class ConfigInfoController {

    @Autowired
    private ConfigInfoService configInfoService;

    @Autowired
    private HttpServletRequest request;
    /**
     * 查询所有
     * @param configInfo
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/list")
    public String list(ConfigInfo configInfo,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5") int size){
        //1.调用业务层查询所有
        Page configInfoPage = configInfoService.findAll(configInfo,page,size);
        //2.创建分页对象
        PageResult pageResult = new PageResult(configInfoPage.getTotalElements(),configInfoPage.getContent(),page,size);
        //3.存入请求域中
        request.setAttribute("page",pageResult);
        request.setAttribute("projectName",configInfo.getProjectName());
        request.setAttribute("envName",configInfo.getEnvName());
        //4.前往配置信息的列表页面
        return "config/list";
    }

    /**
     * 前往创建配置页面
     * @return
     */
    @RequestMapping("/toCreate")
    public String toCreate(String projectName){
        //1.把项目信息携带到浏览器端
        request.setAttribute("projectName",projectName);
        //2.前往创建页面
        return "config/create";
    }

    /**
     * 创建配置
     * @param configInfo
     * @return
     */
    @RequestMapping("/create")
    public String create(ConfigInfo configInfo){
        //目前没有登录操作，所以此处先写死。当有登录操作时，这两个信息是从登录用户的信息里取出来填充的。
        configInfo.setProjectGroup("itheima");
        configInfo.setUserId("1");
        //1.调用业务层创建配置
        configInfoService.create(configInfo);
        //2.前往列表操作
        return "redirect:/config/list.do?projectName="+configInfo.getProjectName()+"&envName="+configInfo.getEnvName();
    }

    /**
     * 删除整个配置
     * @param id
     * @return
     */
    @RequestMapping("/removeAll")
    public String removeAll(String id){
        //1.根据Id查询出要删除的对象
        ConfigInfo configInfo = configInfoService.findById(id);
        //2.调用业务层删除
        configInfoService.removeAll(configInfo);
        //3.前往列表操作
        return "redirect:/config/list.do?projectName="+configInfo.getProjectName()+"&envName="+configInfo.getEnvName();
    }

    /**
     * 前往添加配置项的页面
     * @param id
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(String id){
       request.setAttribute("id",id);
       return "config/add";
    }

    /**
     * 添加配置项
     * @param id
     * @param configName
     * @param configValue
     * @return
     */
    @RequestMapping("/add")
    public String add(String id,String configName,String configValue){
        //1.调用业务层添加配置项
        configInfoService.add(id,configName,configValue);
        //2.前往配置详情操作
        return "redirect:/config/toView.do?id="+id;
    }

    /**
     * 查看详情
     * @param id
     * @return
     */
    @RequestMapping("/toView")
    public String toView(String id){
        //1.根据id查询配置信息
        ConfigInfo configInfo = configInfoService.findById(id);
        //2.存入请求域
        request.setAttribute("configInfo",configInfo);
        //把配置详情转成Properties存入请求域
        Properties properties = PropertiesUtil.toProperties(configInfo.getConfigDetail());
        request.setAttribute("props",properties);
        //3.前往详情页面
        return "config/view";
    }


    /**
     * 删除指定id下的指定配置项
     * @param id
     * @param configName
     * @return
     */
    @RequestMapping("/remove")
    public String remove(String id,String configName){
        //1.调用业务层删除
        configInfoService.remove(id,configName);
        //2.前往详情操作
        return "redirect:/config/toView.do?id="+id;
    }

    /**
     * 前往编辑页面
     * @param id
     * @return
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //1.根据id查询配置信息
        ConfigInfo configInfo = configInfoService.findById(id);
        //2.存入请求域
        request.setAttribute("configInfo",configInfo);
        //把配置详情转成Properties存入请求域
        Properties properties = PropertiesUtil.toProperties(configInfo.getConfigDetail());
        request.setAttribute("props",properties);
        //3.前往编辑页面
        return "config/update";
    }

    /**
     * 更新配置（configInfo中不包含configDetail）
     * @param configInfo
     * @return
     */
    @RequestMapping("/update")
    public String update(ConfigInfo configInfo){
        //1.取出请求参数的map
        Map map = request.getParameterMap();
        //2.把map转成properties
        Properties properties = PropertiesUtil.toProperties(map);
        //3.把properties转成Json
        String json = PropertiesUtil.toJson(properties);
        //4.给ConfigInfo赋值
        configInfo.setConfigDetail(json);
        //5.更新
        configInfoService.update(configInfo);
        //6.前往详情操作
        return "redirect:/config/toView.do?id="+configInfo.getId();
    }

    /**
     * 前往上传页面
     * @return
     */
    @RequestMapping("/toPush")
    public String toPush(){
        return "config/push";
    }

    /**
     * 上传配置文件
     * @param configFile
     * @return
     */
    @RequestMapping("/push")
    public String push(MultipartFile configFile){
        String projectName = "";
        String envName = "";
        String clusterNumber = "";
        String serviceName = "";
        Properties properties = null;
        try{
            //1.取出文件名
            String fileName = configFile.getOriginalFilename();
            //2.分隔文件名
            String[] fileNameArray = fileName.split("\\.");
            int len = fileNameArray.length;
            String suffix = fileNameArray[len - 1];
            //3.把文件内容转成Properties
            properties = PropertiesUtil.toProperties(configFile.getBytes(),suffix);
            //4.给必要的参数赋值
            projectName = properties.getProperty("project.name");
            envName = properties.getProperty("env.name");
            clusterNumber = properties.getProperty("cluster.number");
            serviceName = properties.getProperty("service.name");
            //5.创建ConfigInfo对象
            ConfigInfo configInfo = new ConfigInfo();
            //6.填充
            configInfo.setProjectName(projectName);
            configInfo.setEnvName(envName);
            configInfo.setClusterNumber(clusterNumber);
            configInfo.setServiceName(serviceName);
            //应该从登陆用户中取到的信息
            configInfo.setUserId("1");
            configInfo.setProjectGroup("itheima");
            configInfo.setCreateTime(new Date());
            configInfo.setUpdateTime(new Date());
            //7.把properties转成json
            String json = PropertiesUtil.toJson(properties);
            //8.给详情赋值
            configInfo.setConfigDetail(json);
            //9.保存
            String configInfoId = configInfoService.save(configInfo);
            //10.前往详情操作
            return "redirect:/config/toView.do?id="+configInfoId;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 客户端获取最新配置的方法
     * @param id
     * @return
     */
    @RequestMapping(value = "refreshConfig",method = RequestMethod.POST)
    public @ResponseBody String refreshConfig(String id){
        //1.根据id查询配置
        ConfigInfo configInfo = configInfoService.findById(id);
        //2.判断配置是否存在
        if(configInfo == null){
            return "";
        }
        //3.取出配置详情
        String configDetail = configInfo.getConfigDetail();
        //4.把它响应给客户端
        return configDetail;
    }
}
